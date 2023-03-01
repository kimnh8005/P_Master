package kr.co.pulmuone.v1.policy.payment.dto;

import java.util.HashMap;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "결제 정보 리스트 DTO")
public class PayUseListDto{

	@ApiModelProperty(value = "결제방법 정보 리스트")
	private List<HashMap<String,String>> paymentType;

	@ApiModelProperty(value = "카드 정보 리스트")
	private List<HashMap<String,String>> cardList;

	@ApiModelProperty(value = "할부기간")
	private List<HashMap<String,String>> installmentPeriod;

	@ApiModelProperty(value = "신용카드 혜택")
	private List<HashMap<String,String>> cartBenefit;

}
