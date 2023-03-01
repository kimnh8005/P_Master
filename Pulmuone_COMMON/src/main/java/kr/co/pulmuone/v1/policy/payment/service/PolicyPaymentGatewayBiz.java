package kr.co.pulmuone.v1.policy.payment.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.policy.payment.dto.PolicyPaymentGatewayDto;
import kr.co.pulmuone.v1.policy.payment.dto.PolicyPaymentPromotionRequestDto;

public interface PolicyPaymentGatewayBiz {

	ApiResult<?> getPolicyPaymentGatewayList();

	ApiResult<?> getPolicyPaymentGatewayRatioList();

	ApiResult<?> putPolicyPaymentGateway(PolicyPaymentGatewayDto dto);

    ApiResult<?> getPolicyPaymentGatewayMethodList(String psPayCd);

    ApiResult<?> putPolicyPaymentGatewayMethod(PolicyPaymentGatewayDto dto);

	ApiResult<?> getPgBankCodeList(PolicyPaymentPromotionRequestDto dto);
}
