package com.ktpm.potatoapi.order.service;

import com.ktpm.potatoapi.cart.dto.CartItemRequest;
import com.ktpm.potatoapi.common.exception.AppException;
import com.ktpm.potatoapi.common.exception.ErrorCode;
import com.ktpm.potatoapi.common.utils.SecurityUtils;
import com.ktpm.potatoapi.menu.entity.MenuItem;
import com.ktpm.potatoapi.menu.repo.MenuItemRepository;
import com.ktpm.potatoapi.merchant.entity.Merchant;
import com.ktpm.potatoapi.option.entity.Option;
import com.ktpm.potatoapi.option.entity.OptionValue;
import com.ktpm.potatoapi.option.repo.OptionValueRepository;
import com.ktpm.potatoapi.order.dto.OrderItemOptionValueResponse;
import com.ktpm.potatoapi.order.dto.OrderItemResponse;
import com.ktpm.potatoapi.order.dto.OrderRequest;
import com.ktpm.potatoapi.order.dto.OrderResponse;
import com.ktpm.potatoapi.order.entity.Order;
import com.ktpm.potatoapi.order.entity.OrderItem;
import com.ktpm.potatoapi.order.entity.OrderItemOptionValue;
import com.ktpm.potatoapi.order.mapper.OrderItemMapper;
import com.ktpm.potatoapi.order.mapper.OrderItemOptionValueMapper;
import com.ktpm.potatoapi.order.mapper.OrderMapper;
import com.ktpm.potatoapi.order.repo.OrderItemOptionValueRepository;
import com.ktpm.potatoapi.order.repo.OrderItemRepository;
import com.ktpm.potatoapi.order.repo.OrderRepository;
import com.ktpm.potatoapi.user.entity.User;
import com.ktpm.potatoapi.user.repo.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OrderServiceImpl implements OrderService {
    UserRepository userRepository;
    OrderMapper orderMapper;
    OrderRepository orderRepository;
    OrderItemRepository orderItemRepository;
    MenuItemRepository menuItemRepository;
    OptionValueRepository optionValueRepository;
    SecurityUtils securityUtils;
    OrderItemOptionValueRepository orderItemOptionValueRepository;
    OrderItemMapper orderItemMapper;
    OrderItemOptionValueMapper orderItemOptionValueMapper;

    @Override
    @Transactional
    public void createOrder(OrderRequest orderRequest) {
        User customer = userRepository.findByEmail(securityUtils.getCurrentUserEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // get merchant from menu item
        Merchant merchant = getMerchantFromMenuItemId(orderRequest.getCartItems().get(0).getMenuItemId());

        // build order
        Order order = buildOrder(orderRequest, customer, merchant);
        orderRepository.save(order); // lưu để tạo id

        // calc total
        long total = calculateTotal(orderRequest, order);
        long deliveryFee = 15000L;
        order.setDeliveryFee(deliveryFee);
        order.setTotalAmount(total + deliveryFee);
        orderRepository.save(order);

        log.info("Order from {} created", customer.getEmail());
    }

    @Override
    public List<OrderResponse> getAllOrdersOfCustomer() {
        User customer = userRepository.findByEmail(securityUtils.getCurrentUserEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        List<OrderResponse> orderResponses = new ArrayList<>();
        List<Order> orders = orderRepository.findAllByCustomer(customer);
        for(Order order : orders) {
            List<OrderItemResponse> orderItemResponses = mapOrderItemsWithOptionValuesToResponse(order.getOrderItems());
            OrderResponse orderResponse = orderMapper.toResponse(order);
            orderResponse.setOrderItems(orderItemResponses);
            orderResponses.add(orderResponse);
        }
        return orderResponses;
    }

    private Merchant getMerchantFromMenuItemId(Long menuItemId) {
        MenuItem firstMenuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new AppException(ErrorCode.MENU_ITEM_NOT_FOUND));
        return firstMenuItem.getMerchant();
    }

    private Order buildOrder(OrderRequest orderRequest, User customer, Merchant merchant) {
        Order order = orderMapper.toEntity(orderRequest); // fullName, phone, deliveryAddress, note
        order.setCustomer(customer);
        order.setMerchant(merchant);
        return order;
    }

    private long calculateTotal(OrderRequest orderRequest, Order order) {
        long total = 0L;
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItemRequest cartItem : orderRequest.getCartItems()) {
            MenuItem menuItem = menuItemRepository.findById(cartItem.getMenuItemId())
                    .orElseThrow(() -> new AppException(ErrorCode.MENU_ITEM_NOT_FOUND));

            // kiểm tra món có visible không
            if (!menuItem.isVisible()) throw new AppException(ErrorCode.MENU_ITEM_INVISIBLE);

            OrderItem orderItem = buildOrderItem(cartItem, menuItem, order);
            orderItemRepository.save(orderItem); // lưu để tạo id

            // tính subtotal của mỗi item
            long subtotal = calculateSubtotal(menuItem, cartItem, orderItem);
            orderItem.setSubtotal(subtotal);
            orderItemRepository.save(orderItem);

            total += subtotal; // Tổng tiền chưa tính phí giao hàng
            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);
        return total;
    }

    private OrderItem buildOrderItem(CartItemRequest cartItem, MenuItem menuItem, Order order) {
        return OrderItem.builder()
                .quantity(cartItem.getQuantity())
                .note(cartItem.getNote())
                .menuItem(menuItem)
                .menuItemName(menuItem.getName())
                .menuItemBasePrice(menuItem.getBasePrice())
                .order(order)
                .build();
    }

    private long calculateSubtotal(MenuItem menuItem, CartItemRequest cartItem, OrderItem orderItem) {
        long subtotal = menuItem.getBasePrice();
        List<Long> selectedOptionValueIds = cartItem.getOptionValueIds();
        if (!selectedOptionValueIds.isEmpty()) {
            List<OptionValue> optionValues = optionValueRepository.findAllById(selectedOptionValueIds);
            if (optionValues.size() != selectedOptionValueIds.size())
                throw new AppException(ErrorCode.OPTION_VALUE_NOT_FOUND);

            // set để lưu optionId đã có (đối với required option)
            Set<Long> seenRequiredOptions = new HashSet<>();

            for (OptionValue optionValue : optionValues) {
                // kiểm tra option value có visible không
                if (!optionValue.isVisible()) throw new AppException(ErrorCode.OPTION_VALUE_INVISIBLE);

                Option parentOption = optionValue.getOption();

                // kiểm tra option có liên kết với menuItem không
                boolean belongsToMenuItem = menuItem.getOptions()
                        .stream()
                        .anyMatch(opt -> opt.getId().equals(parentOption.getId()));
                if (!belongsToMenuItem) throw new AppException(ErrorCode.MENU_ITEM_NOT_ASSIGNED_TO_OPTION);

                // kiểm tra nếu có nhiều hơn 1 option value trong 1 option có isRequired = true
                if (parentOption.isRequired()) {
                    if (seenRequiredOptions.contains(parentOption.getId()))
                        throw new AppException(ErrorCode.ORDER_HAS_MULTIPLE_OPTION_VALUES_FOR_REQUIRED_OPTION);

                    seenRequiredOptions.add(parentOption.getId());
                }

                subtotal += optionValue.getExtraPrice();

                orderItemOptionValueRepository.save(buildOrderItemOptionValue(orderItem, optionValue));
            }
        }

        return subtotal * cartItem.getQuantity();
    }

    private OrderItemOptionValue buildOrderItemOptionValue(OrderItem orderItem, OptionValue optionValue) {
        return OrderItemOptionValue.builder()
                .orderItem(orderItem)
                .optionValue(optionValue)
                .optionValueName(optionValue.getName())
                .extraPrice(optionValue.getExtraPrice())
                .build();
    }

    private List<OrderItemResponse> mapOrderItemsWithOptionValuesToResponse(List<OrderItem> orderItems) {
        List<OrderItemResponse> orderItemResponses = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            List<OrderItemOptionValue> orderItemOptionValues = orderItemOptionValueRepository.findAllByOrderItem(orderItem);
            List<OrderItemOptionValueResponse> orderItemOptionValueResponses = new ArrayList<>();

            for (OrderItemOptionValue orderItemOptionValue : orderItemOptionValues) {
                OrderItemOptionValueResponse orderItemOptionValueResponse = orderItemOptionValueMapper.toResponse(orderItemOptionValue);
                orderItemOptionValueResponses.add(orderItemOptionValueResponse);
            }

            OrderItemResponse orderItemResponse = orderItemMapper.toResponse(orderItem);
            orderItemResponse.setOptionValues(orderItemOptionValueResponses);
            orderItemResponses.add(orderItemResponse);
        }

        return orderItemResponses;
    }
}
