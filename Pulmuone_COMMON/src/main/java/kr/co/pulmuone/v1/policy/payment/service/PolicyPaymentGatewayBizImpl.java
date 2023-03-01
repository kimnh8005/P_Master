package kr.co.pulmuone.v1.policy.payment.service;

import kr.co.pulmuone.v1.policy.payment.dto.PolicyPaymentPromotionRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.policy.payment.dto.PolicyPaymentGatewayDto;

@Service
public class PolicyPaymentGatewayBizImpl implements PolicyPaymentGatewayBiz {

    @Autowired
    private PolicyPaymentGatewayService policyPaymentGatewayService;

    /**
     * PG사 기본설정 정보 목록 조회
     *
     * @param
     * @return PolicyPaymentGatewayDto
     */
    public ApiResult<?> getPolicyPaymentGatewayList() {
    	return ApiResult.success(policyPaymentGatewayService.getPolicyPaymentGatewayList());
    }

    /**
     * PG사 이중화 비율 정보 목록 조회
     *
     * @param
     * @return PolicyPaymentGatewayDto
     */
    @Override
    public ApiResult<?> getPolicyPaymentGatewayRatioList() {
    	return ApiResult.success(policyPaymentGatewayService.getPolicyPaymentGatewayRatioList());
    }

    /**
     * PG 결제수단 관리 목록 조회
     *
     * @param
     * @return PolicyPaymentGatewayDto
     */
    public ApiResult<?> getPolicyPaymentGatewayMethodList(String psPayCd) {
    	return ApiResult.success(policyPaymentGatewayService.getPolicyPaymentGatewayMethodList(psPayCd));
    }

    /**
     * PG사 설정 정보 수정
     *
     * @param PolicyPaymentGatewayDto
     * @return int
     */
    @Override
    public ApiResult<?> putPolicyPaymentGateway(PolicyPaymentGatewayDto dto) {
    	return policyPaymentGatewayService.putPolicyPaymentGateway(dto);
    }
    /**
     * PG 결제수단 정보 수정
     *
     * @param PolicyPaymentGatewayDto
     * @return int
     */
    @Override
    public ApiResult<?> putPolicyPaymentGatewayMethod(PolicyPaymentGatewayDto dto) {
    	int result = policyPaymentGatewayService.putPolicyPaymentGatewayMethod(dto);
		if(result > 0) return ApiResult.success();
		else return ApiResult.fail();
    }

    /**
     * PG 가상계좌 결제은행 목록 조회
     */
    @Override
    public ApiResult<?> getPgBankCodeList(PolicyPaymentPromotionRequestDto dto) {
        return policyPaymentGatewayService.getPgBankCodeList(dto);
    }

}


