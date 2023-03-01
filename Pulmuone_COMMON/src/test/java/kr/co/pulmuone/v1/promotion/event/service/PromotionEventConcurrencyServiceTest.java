package kr.co.pulmuone.v1.promotion.event.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.promotion.event.dto.EventBenefitInfoRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class PromotionEventConcurrencyServiceTest extends CommonServiceTestBaseForJunit5 {
    @Autowired
    private PromotionEventConcurrencyService promotionEventConcurrencyService;

    @Test
    void concurrencyEventBenefit_정상() throws Exception {
        //given
        EventBenefitInfoRequestDto dto = EventBenefitInfoRequestDto.builder()
                .evEventId(529L)
                .build();

        //when, then
        promotionEventConcurrencyService.concurrencyEventBenefit(dto);
    }

    @Test
    void concurrencyEventCoupon_정상() throws Exception {
        //given
        Long evEventCouponId = 1L;

        //when, then
        promotionEventConcurrencyService.concurrencyEventCoupon(evEventCouponId);
    }

    @Test
    void concurrencyNormalBenefit_정상() throws Exception {
        //given
        Long evEventId = 529L;

        //when, then
        promotionEventConcurrencyService.concurrencyNormalBenefit(evEventId);
    }

}