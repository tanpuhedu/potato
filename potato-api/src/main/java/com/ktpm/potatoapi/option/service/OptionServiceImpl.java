package com.ktpm.potatoapi.option.service;

import com.ktpm.potatoapi.common.exception.AppException;
import com.ktpm.potatoapi.common.exception.ErrorCode;
import com.ktpm.potatoapi.common.utils.SecurityUtils;
import com.ktpm.potatoapi.merchant.entity.Merchant;
import com.ktpm.potatoapi.merchant.repo.MerchantRepository;
import com.ktpm.potatoapi.option.dto.OptionCreationRequest;
import com.ktpm.potatoapi.option.dto.OptionResponse;
import com.ktpm.potatoapi.option.dto.OptionUpdateRequest;
import com.ktpm.potatoapi.option.dto.OptionValueRequest;
import com.ktpm.potatoapi.option.entity.Option;
import com.ktpm.potatoapi.option.entity.OptionValue;
import com.ktpm.potatoapi.option.mapper.OptionMapper;
import com.ktpm.potatoapi.option.mapper.OptionValueMapper;
import com.ktpm.potatoapi.option.repo.OptionRepository;
import com.ktpm.potatoapi.option.repo.OptionValueRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OptionServiceImpl implements OptionService {
    OptionRepository optionRepository;
    OptionMapper optionMapper;
    OptionValueMapper optionValueMapper;
    SecurityUtils securityUtils;
    MerchantRepository merchantRepository;
    OptionValueRepository optionValueRepository;

    @Override
    public List<OptionResponse> getAllOptionsOfMyMerchant() {
        log.info("Get all options for Merchant Admin");
        return optionRepository.findAllByMerchantIdAndIsActiveTrue(securityUtils.getCurrentMerchant().getId())
                .stream()
                .map(optionMapper::toResponse)
                .toList();
    }

    @Override
    public List<OptionResponse> getAllOptionsForCustomer(Long merchantId) {
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new AppException(ErrorCode.MERCHANT_NOT_FOUND));

        if (!merchant.isOpen())
            throw new AppException(ErrorCode.MERCHANT_CLOSED);

        log.info("Get all options for Customer");

        return optionRepository.findAllByMerchantIdAndIsVisibleTrue(merchantId)
                .stream()
                .map(optionMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void createOptionAndOptionValue(OptionCreationRequest optionRequest) {
        Option option = optionMapper.toEntity(optionRequest);
        option.setRequired(optionRequest.isRequired());

        Merchant merchant = securityUtils.getCurrentMerchant();
        option.setMerchant(merchant);

        List<OptionValue> optionValues = optionRequest.getOptionValues().stream()
                .map(optionValueRequest -> {
                    OptionValue entity = optionValueMapper.toEntity(optionValueRequest);
                    entity.setOption(option);
                    entity.setDefault(false);
                    return entity;
                })
                .toList();

        if (optionRequest.isRequired()) {
            // nếu option có isRequired=true thì value[0] phải isDefault=true
            optionValues.get(0).setDefault(true);
        }
        option.setOptionValues(optionValues);

        try {
            optionRepository.save(option);
            log.info("Create option '{}' for merchant {} with {} value(s)",
                    option.getName(), merchant.getName(), optionValues.size());
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.OPTION_EXISTED);
        }
    }

    @Override
    public void createOptionValueForExistingOption(Long optionId, OptionValueRequest request) {
        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new AppException(ErrorCode.OPTION_NOT_FOUND));

        OptionValue optionValue = optionValueMapper.toEntity(request);
        optionValue.setOption(option);
        optionValue.setDefault(false);

        if (!option.isVisible())
            option.setVisible(true);

        if (option.isRequired())
            optionValue.setDefault(true);

        try {
            optionValueRepository.save(optionValue);
            log.info("Create option value {} for option {}", optionValue.getName(), option.getName());
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.OPTION_VALUE_EXISTED);
        }
    }

    @Override
    public void updateOption(Long optionId, OptionUpdateRequest request) {
        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new AppException(ErrorCode.OPTION_NOT_FOUND));

        option.setName(request.getName());
        try {
            optionRepository.save(option);
            log.info("Update name of option {}", option.getName());
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.OPTION_EXISTED);
        }
    }

    @Override
    public void updateOptionValue(Long valueId, OptionValueRequest request) {
        OptionValue optionValue = optionValueRepository.findById(valueId)
                .orElseThrow(() -> new AppException(ErrorCode.OPTION_VALUE_NOT_FOUND));

        optionValue.setName(request.getName());
        optionValue.setExtraPrice(request.getExtraPrice());

        try {
            optionValueRepository.save(optionValue);
            log.info("Update option value {}", optionValue.getName());
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.OPTION_VALUE_EXISTED);
        }
    }

    @Override
    public void updateOptionValueVisibleStatus(Long valueId, boolean isVisible) {
        OptionValue optionValue = optionValueRepository.findById(valueId)
                .orElseThrow(() -> new AppException(ErrorCode.OPTION_VALUE_NOT_FOUND));

        optionValue.setVisible(isVisible);

        Option option = optionValue.getOption();
        List<OptionValue> allValues = option.getOptionValues();
        List<OptionValue> visibleValues = allValues.stream()
                .filter(OptionValue::isVisible)
                .toList();

        if (!isVisible && optionValue.isDefault()) {
            // bỏ default option value đang muốn xóa hiện tại
            optionValue.setDefault(false);

            // chọn cái visible đầu tiên trong visibleValues làm default mới
            if (!visibleValues.isEmpty())
                visibleValues.get(0).setDefault(true);
        }

        option.setVisible(!visibleValues.isEmpty());

        optionValueRepository.saveAll(allValues);
        optionRepository.save(option);

        log.info("Update {}'s visible status", optionValue.getName());
    }

    public void deleteOptionValue(Long valueId) {
        OptionValue optionValue = optionValueRepository.findById(valueId)
                .orElseThrow(() -> new AppException(ErrorCode.OPTION_VALUE_NOT_FOUND));

        optionValue.setActive(false);
        optionValue.setVisible(false);

        Option option = optionValue.getOption();
        option.getOptionValues().remove(optionValue);

        List<OptionValue> allValues = option.getOptionValues();
        List<OptionValue> visibleValues = allValues.stream()
                .filter(OptionValue::isVisible)
                .toList();

        // xử lí nếu optionValue là default
        if (optionValue.isDefault()) {
            optionValue.setDefault(false);

            // chọn cái visible đầu tiên trong visibleValues làm default mới
            if (!visibleValues.isEmpty())
                visibleValues.get(0).setDefault(true);
        }

        // xử lí nếu đó là optionValue duy nhất của option
        option.setActive(!visibleValues.isEmpty());

        optionValueRepository.saveAll(allValues);
        optionRepository.save(option);

        log.info("Delete option value {}", optionValue.getName());
    }

    public void deleteOption(Long optionId) {
        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new AppException(ErrorCode.OPTION_NOT_FOUND));

        option.setActive(false);
        option.setVisible(false);

        List<OptionValue> optionValues = option.getOptionValues();
        for (OptionValue optionValue : optionValues) {
            optionValue.setActive(false);
            optionValue.setVisible(false);
        }

        optionRepository.save(option);

        log.info("Delete option {}", option.getName());
    }
}
