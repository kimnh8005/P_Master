package kr.co.pulmuone.v1.policy.regularshipping.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.policy.regularshipping.dto.PolicyRegularShippingDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class PolicyRegularShippingServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private PolicyRegularShippingService policyRegularShippingService;

    @Test
    void 정기배송기본설정_조회() throws Exception {

    	// given
    	PolicyRegularShippingDto dto = new PolicyRegularShippingDto();

        // when
    	dto = policyRegularShippingService.getPolicyRegularShipping(dto);

        // then
        assertTrue(dto != null);

    }

    @Test
    void 정기배송기본설정_수정() {
        // given
    	PolicyRegularShippingDto requestVo = new PolicyRegularShippingDto();
    	requestVo.setRegularShippingBasicDiscountRate(5);
    	requestVo.setRegularShippingAdditionalDiscountApplicationTimes(3);
    	requestVo.setRegularShippingAdditionalDiscountRate(5);
    	requestVo.setRegularShippingMaxCustomerCycle(1);
    	requestVo.setRegularShippingPaymentFailTerminate(1);

        // when
        int updatedCount = policyRegularShippingService.putPolicyRegularShipping(requestVo);

        // then
        assertTrue(updatedCount > 0);
    }

}
