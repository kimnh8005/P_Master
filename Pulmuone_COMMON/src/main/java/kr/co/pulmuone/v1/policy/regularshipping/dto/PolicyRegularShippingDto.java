package kr.co.pulmuone.v1.policy.regularshipping.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "정기배송설정 Dto")
public class PolicyRegularShippingDto extends BaseResponseDto{

	@ApiModelProperty(value = "정기배송설정.기본할인율")
	private int regularShippingBasicDiscountRate;

	@ApiModelProperty(value = "정기배송설정.추가할인 적용회차")
	private int regularShippingAdditionalDiscountApplicationTimes;

	@ApiModelProperty(value = "정기배송설정.추가할인율")
	private int regularShippingAdditionalDiscountRate;

	@ApiModelProperty(value = "정기배송설정.고객배송주기")
	private int regularShippingMaxCustomerCycle;

	@ApiModelProperty(value = "정기배송설정.결제실패시강제종료")
	private int regularShippingPaymentFailTerminate;
}
