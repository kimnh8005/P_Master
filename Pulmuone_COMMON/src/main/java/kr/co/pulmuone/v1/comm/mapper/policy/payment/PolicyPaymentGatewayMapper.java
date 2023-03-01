package kr.co.pulmuone.v1.comm.mapper.policy.payment;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.pulmuone.v1.policy.payment.dto.PolicyPaymentGatewayDto;
import kr.co.pulmuone.v1.policy.payment.dto.PolicyPaymentPromotionRequestDto;
import kr.co.pulmuone.v1.policy.payment.dto.vo.PolicyPaymentGatewayMethodVo;
import kr.co.pulmuone.v1.policy.payment.dto.vo.PolicyPaymentGatewayVo;

@Mapper
public interface PolicyPaymentGatewayMapper {

	List<PolicyPaymentGatewayVo> getPolicyPaymentGatewayList();
	List<PolicyPaymentGatewayVo> getPolicyPaymentGatewayRatioList();
	int putPolicyPaymentGateway(PolicyPaymentGatewayDto dto);
	int putPolicyPaymentGatewayRatio(PolicyPaymentGatewayDto dto);
	List<PolicyPaymentGatewayMethodVo> getPolicyPaymentGatewayMethodList(String psPayCd);
	int putPolicyPaymentGatewayMethod(PolicyPaymentGatewayDto dto);

	List<PolicyPaymentGatewayVo> getPgBankCodeList(PolicyPaymentPromotionRequestDto dto);
}
