package kr.co.pulmuone.v1.promotion.event.service;

import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.promotion.event.dto.EventBenefitInfoRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PromotionEventConcurrencyBizImpl implements PromotionEventConcurrencyBiz {

    @Autowired
    private PromotionEventConcurrencyService promotionEventConcurrencyService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class})
    public MessageCommEnum concurrencyEventBenefit(EventBenefitInfoRequestDto dto) throws Exception {
        return promotionEventConcurrencyService.concurrencyEventBenefit(dto);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class})
    public MessageCommEnum concurrencyEventCoupon(Long evEventCouponId) throws Exception {
        return promotionEventConcurrencyService.concurrencyEventCoupon(evEventCouponId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class})
    public MessageCommEnum concurrencyNormalBenefit(Long evEventId) throws Exception {
        return promotionEventConcurrencyService.concurrencyNormalBenefit(evEventId);
    }
}