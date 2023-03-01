package kr.co.pulmuone.v1.policy.payment.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import kr.co.pulmuone.v1.policy.payment.dto.vo.PolicyPaymentPromotionVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "프로모션 결제수단 정보 Dto")
public class PolicyPaymentPromotionDto extends BaseRequestDto{

	@ApiModelProperty(value = "PG 설정정보보 리스트")
	private	List<PolicyPaymentPromotionVo> rows;
}
