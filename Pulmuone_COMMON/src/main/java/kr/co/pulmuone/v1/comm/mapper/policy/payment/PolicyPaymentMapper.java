package kr.co.pulmuone.v1.comm.mapper.policy.payment;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.pulmuone.v1.policy.payment.dto.PolicyPaymentCardBenefitDto;
import kr.co.pulmuone.v1.policy.payment.dto.PolicyPaymentPromotionRequestDto;
import kr.co.pulmuone.v1.policy.payment.dto.vo.PolicyPaymentPromotionVo;

@Mapper
public interface PolicyPaymentMapper
{

	List<HashMap<String,String>> getPayUseList() throws Exception;

	List<HashMap<String,String>> getPayCardUseList() throws Exception;

	List<HashMap<String,String>> getCartBenefitList() throws Exception;

	List<PolicyPaymentPromotionVo> getPaymentList(PolicyPaymentCardBenefitDto policyPaymentCardBenefitDto) throws Exception;

	List<PolicyPaymentPromotionVo> getPaymentUseList(PolicyPaymentCardBenefitDto policyPaymentCardBenefitDto) throws Exception;

	List<PolicyPaymentPromotionVo> getPayCardList(PolicyPaymentPromotionRequestDto policyPaymentPromotionRequestDto) throws Exception;
}
