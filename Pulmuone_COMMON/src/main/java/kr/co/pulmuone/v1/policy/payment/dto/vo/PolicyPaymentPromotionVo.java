package kr.co.pulmuone.v1.policy.payment.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "프로모션 결제수단 VO")
public class PolicyPaymentPromotionVo {

	@ApiModelProperty(value = "PG 설정정보 명")
	private String psPayCodeName;

	@ApiModelProperty(value = "PG 설정정보 코드")
	private String psPayCode;

}
