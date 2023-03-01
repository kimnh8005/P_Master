package kr.co.pulmuone.v1.comm.mapper.policy.payment;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.policy.payment.dto.PolicyPaymentCardBenefitDto;
import kr.co.pulmuone.v1.policy.payment.dto.vo.PolicyPaymentCardBenefitVo;

@Mapper
public interface PolicyPaymentCardBenefitMapper {

	PolicyPaymentCardBenefitVo getPolicyPaymentCardBenefitInfo(Long psCardBenefitId);
	Page<PolicyPaymentCardBenefitVo> getPolicyPaymentCardBenefitList(PolicyPaymentCardBenefitDto dto);
	PolicyPaymentCardBenefitVo checkDuplicateTermPolicyPaymentCardBenefit(PolicyPaymentCardBenefitDto dto);
	int addPolicyPaymentCardBenefit(PolicyPaymentCardBenefitDto dto);
	int putPolicyPaymentCardBenefit(PolicyPaymentCardBenefitDto dto);
	int delPolicyPaymentCardBenefit(Long psCardBenefitId);

}
