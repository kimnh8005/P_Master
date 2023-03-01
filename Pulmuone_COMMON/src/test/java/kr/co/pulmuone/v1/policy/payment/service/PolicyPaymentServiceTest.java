package kr.co.pulmuone.v1.policy.payment.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.policy.payment.dto.PolicyPaymentCardBenefitDto;
import kr.co.pulmuone.v1.policy.payment.dto.PolicyPaymentPromotionRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class PolicyPaymentServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private PolicyPaymentService policyPaymentService;



    @Test
    void 결제방법_리스트_조회() throws Exception {

        List<HashMap<String,String>> result = policyPaymentService.getPayUseList();

        //then
        assertTrue(result.size()>0);
    }

    @Test
    void 카드정보_리스트_조회() throws Exception {
        List<HashMap<String,String>> result = policyPaymentService.getPayCardUseList();

        //then
        assertTrue(result.size()>0);
    }

    @Test
    void 신용카드_혜택_리스트_조회() throws Exception {
        List<HashMap<String,String>> result = policyPaymentService.getCartBenefitList();

        //then
        assertTrue(result.size()>0);
    }

    @Test
    void 제휴구분_PG_조회() throws Exception {
        PolicyPaymentCardBenefitDto policyPaymentCardBenefitDto = new PolicyPaymentCardBenefitDto();

        ApiResult result = policyPaymentService.getPaymentList(policyPaymentCardBenefitDto);

        //then
        assertEquals(ApiResult.success().getCode(), result.getCode());
    }

    @Test
    void 제휴구분_결제수단_조회() throws Exception {
        PolicyPaymentCardBenefitDto policyPaymentCardBenefitDto = new PolicyPaymentCardBenefitDto();

        ApiResult result = policyPaymentService.getPaymentUseList(policyPaymentCardBenefitDto);

        //then
        assertEquals(ApiResult.success().getCode(), result.getCode());
    }

    @Test
    void 제휴구분_결제수단_상세_조회() throws Exception {
        PolicyPaymentPromotionRequestDto policyPaymentCardBenefitDto = new PolicyPaymentPromotionRequestDto();

        ApiResult result = policyPaymentService.getPayCardList(policyPaymentCardBenefitDto);

        //then
        assertEquals(ApiResult.success().getCode(), result.getCode());
    }
}