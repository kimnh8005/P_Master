package kr.co.pulmuone.v1.policy.payment.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "프로모션 결제수단 정보")
public class PolicyPaymentPromotionRequestDto extends BaseRequestDto{

	@ApiModelProperty(value = "제휴구분 PG 코드")
	private String psCode;

	@ApiModelProperty(value = "제휴구분 결제수단")
	private String psPgCode;


}
