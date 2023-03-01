package kr.co.pulmuone.v1.policy.payment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.comm.enums.PolicyEnums;
import kr.co.pulmuone.v1.comm.enums.PolicyEnums.Payment;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.policy.payment.dto.PolicyPaymentCardBenefitDto;
import kr.co.pulmuone.v1.policy.payment.dto.vo.PolicyPaymentCardBenefitVo;

@Service
public class PolicyPaymentCardBenefitBizImpl implements PolicyPaymentCardBenefitBiz {

    @Autowired
    private PolicyPaymentCardBenefitService policyPaymentCardBenefitService;
    /**
     * 신용카드혜택안내 조회
     *
     * @param psCardBenefitId
     * @return ApiResult
     * @throws 	Exception
     */
    @Override
    public ApiResult<?> getPolicyPaymentCardBenefitInfo(Long psCardBenefitId) {
    	return ApiResult.success(policyPaymentCardBenefitService.getPolicyPaymentCardBenefitInfo(psCardBenefitId));
    }
    /**
     * 신용카드혜택안내 목록 조회
     *
     * @param PolicyPaymentCardBenefitDto
     * @return ApiResult
     * @throws 	Exception
     */
    @Override
    public ApiResult<?> getPolicyPaymentCardBenefitList(PolicyPaymentCardBenefitDto dto) {
    	return ApiResult.success(policyPaymentCardBenefitService.getPolicyPaymentCardBenefitList(dto));
    }
    /**
     * 신용카드혜택안내 신규 등록
     *
     * @param PolicyPaymentCardBenefitDto
     * @return ApiResult
     * @throws 	Exception
     */
    @Override
    public ApiResult<?> addPolicyPaymentCardBenefit(PolicyPaymentCardBenefitDto dto) {
    	return policyPaymentCardBenefitService.addPolicyPaymentCardBenefit(dto);
    }
    /**
     * 신용카드혜택안내 수정
     *
     * @param PolicyPaymentCardBenefitDto
     * @return ApiResult
     * @throws 	Exception
     */
    @Override
    public ApiResult<?> putPolicyPaymentCardBenefit(PolicyPaymentCardBenefitDto dto) {
    	return ApiResult.result(policyPaymentCardBenefitService.putPolicyPaymentCardBenefit(dto));
    }
    /**
     * 신용카드혜택안내 삭제
     *
     * @param psCardBenefitId
     * @return ApiResult
     * @throws 	Exception
     */
    @Override
    public ApiResult<?> delPolicyPaymentCardBenefit(Long psCardBenefitId) {
    	int result = policyPaymentCardBenefitService.delPolicyPaymentCardBenefit(psCardBenefitId);
		if(result > 0) return ApiResult.success();
		else return ApiResult.fail();
    }
}


