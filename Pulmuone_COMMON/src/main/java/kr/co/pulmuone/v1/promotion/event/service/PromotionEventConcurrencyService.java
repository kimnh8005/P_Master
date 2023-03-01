package kr.co.pulmuone.v1.promotion.event.service;

import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.comm.mapper.promotion.event.PromotionEventMapper;
import kr.co.pulmuone.v1.promotion.event.dto.EventBenefitInfoRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PromotionEventConcurrencyService {
    private final PromotionEventMapper promotionEventMapper;

    /**
     * 이벤트 혜택 동시성 적용(룰렛, 체험단 - 선착순)
     *
     * @param dto EventBenefitInfoRequestDto
     * @return MessageCommEnum
     */
    protected MessageCommEnum concurrencyEventBenefit(EventBenefitInfoRequestDto dto) throws Exception {
        promotionEventMapper.getEventBenefitInfo(dto);  // DB LOCK

        if (promotionEventMapper.putEventBenefitInfoConcurrency(dto) != 1) {
            return BaseEnums.Default.FAIL;
        }

        return BaseEnums.Default.SUCCESS;
    }

    /**
     * 이벤트 혜택 쿠폰 동시성 적용(일반 이벤트)
     *
     * @param evEventCouponId Long
     * @return MessageCommEnum
     */
    protected MessageCommEnum concurrencyEventCoupon(Long evEventCouponId) throws Exception {
        promotionEventMapper.getEventCouponConcurrency(evEventCouponId);  // DB LOCK

        if (promotionEventMapper.putEventCouponConcurrency(evEventCouponId) != 1) {
            return BaseEnums.Default.FAIL;
        }

        return BaseEnums.Default.SUCCESS;
    }

    /**
     * 일반 이벤트 - 혜택 - 적립금, 경품 케이스 - 동시성 적용
     *
     * @param evEventId Long
     * @return MessageCommEnum
     */
    protected MessageCommEnum concurrencyNormalBenefit(Long evEventId) throws Exception {
        promotionEventMapper.getNormalBenefitConcurrency(evEventId);  // DB LOCK

        if (promotionEventMapper.putNormalBenefitConcurrency(evEventId) != 1) {
            return BaseEnums.Default.FAIL;
        }

        return BaseEnums.Default.SUCCESS;
    }

}