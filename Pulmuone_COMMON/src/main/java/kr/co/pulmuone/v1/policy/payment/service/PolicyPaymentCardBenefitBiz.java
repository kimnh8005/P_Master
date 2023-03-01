package kr.co.pulmuone.v1.policy.payment.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.policy.payment.dto.PolicyPaymentCardBenefitDto;

public interface PolicyPaymentCardBenefitBiz {

	ApiResult<?> getPolicyPaymentCardBenefitInfo(Long psCardBenefitId);

	ApiResult<?> getPolicyPaymentCardBenefitList(PolicyPaymentCardBenefitDto dto);

	ApiResult<?> addPolicyPaymentCardBenefit(PolicyPaymentCardBenefitDto dto);

	ApiResult<?> putPolicyPaymentCardBenefit(PolicyPaymentCardBenefitDto dto);

	ApiResult<?> delPolicyPaymentCardBenefit(Long psCardBenefitId);
}
