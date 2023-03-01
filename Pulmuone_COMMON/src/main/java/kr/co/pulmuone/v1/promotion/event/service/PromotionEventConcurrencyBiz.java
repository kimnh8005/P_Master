package kr.co.pulmuone.v1.promotion.event.service;

import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.promotion.event.dto.EventBenefitInfoRequestDto;

public interface PromotionEventConcurrencyBiz {

    MessageCommEnum concurrencyEventBenefit(EventBenefitInfoRequestDto dto) throws Exception;

    MessageCommEnum concurrencyEventCoupon(Long evEventCouponId) throws Exception;

    MessageCommEnum concurrencyNormalBenefit(Long evEventId) throws Exception;

}