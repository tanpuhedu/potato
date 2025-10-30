package com.ktpm.potatoapi.rating.service;

import com.ktpm.potatoapi.common.exception.AppException;
import com.ktpm.potatoapi.common.exception.ErrorCode;
import com.ktpm.potatoapi.common.utils.SecurityUtils;
import com.ktpm.potatoapi.merchant.entity.Merchant;
import com.ktpm.potatoapi.merchant.repo.MerchantRepository;
import com.ktpm.potatoapi.order.entity.Order;
import com.ktpm.potatoapi.order.entity.OrderStatus;
import com.ktpm.potatoapi.order.repo.OrderRepository;
import com.ktpm.potatoapi.rating.dto.RatingRequest;
import com.ktpm.potatoapi.rating.entity.Rating;
import com.ktpm.potatoapi.rating.repo.RatingRepository;
import com.ktpm.potatoapi.user.entity.User;
import com.ktpm.potatoapi.user.repo.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RatingServiceImpl implements RatingService {
    RatingRepository ratingRepository;
    UserRepository userRepository;
    OrderRepository orderRepository;
    MerchantRepository merchantRepository;
    SecurityUtils securityUtils;

    @Override
    @Transactional
    public void createRating(RatingRequest ratingRequest) {
        User customer = userRepository.findByEmail(securityUtils.getCurrentUserEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Order order = orderRepository.findById(ratingRequest.getOrderId())
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        Merchant merchant = order.getMerchant();

        // Check xem order đã ở trạng thái COMPLETED chưa
        if(order.getStatus() != OrderStatus.COMPLETED)
            throw new AppException(ErrorCode.ORDER_NOT_COMPLETED);

        // Check lại xem order này có phải thuộc về customer này không
        if(!orderRepository.existsByIdAndCustomerId(order.getId(), customer.getId()))
            throw new AppException(ErrorCode.ORDER_NOT_OWNED_BY_CURRENT_USER);

        Rating rating = Rating.builder()
                .customer(customer)
                .order(order)
                .merchant(merchant)
                .rating(ratingRequest.getRating())
                .build();
        try {
            ratingRepository.save(rating);
            log.info("Rating with order code {} is created", order.getCode());
        } catch (DataIntegrityViolationException e) {
            log.error("This order is already rated");
            throw new AppException(ErrorCode.ORDER_RATED);
        }

        merchant.setRatingCount(merchant.getRatingCount() + 1); // update rating count
        merchant.setAvgRating(ratingRepository.calcAvgRatingByMerchant(merchant)); // calc avg rating
        merchantRepository.save(merchant);
    }
}
