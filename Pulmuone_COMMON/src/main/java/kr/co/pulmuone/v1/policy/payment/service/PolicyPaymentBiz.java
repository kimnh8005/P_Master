package kr.co.pulmuone.v1.policy.payment.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.policy.payment.dto.PayUseListDto;
import kr.co.pulmuone.v1.policy.payment.dto.PolicyPaymentCardBenefitDto;
import kr.co.pulmuone.v1.policy.payment.dto.PolicyPaymentPromotionRequestDto;

public interface PolicyPaymentBiz {

	PayUseListDto getPayUseList() throws Exception;

	ApiResult<?> getPaymentList(PolicyPaymentCardBenefitDto policyPaymentCardBenefitDto) throws Exception;

	ApiResult<?> getPaymentUseList(PolicyPaymentCardBenefitDto policyPaymentCardBenefitDto) throws Exception;

	ApiResult<?> getPayCardList(PolicyPaymentPromotionRequestDto policyPaymentPromotionRequestDto) throws Exception;

}
