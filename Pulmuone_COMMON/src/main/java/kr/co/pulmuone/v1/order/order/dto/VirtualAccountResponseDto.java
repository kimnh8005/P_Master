package kr.co.pulmuone.v1.order.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "주문 완료 가상계좌 정보 조회 응답 DTO")
public class VirtualAccountResponseDto{

	@ApiModelProperty(value = "가상계좌 입금기한")
	private String paidDueDate;

	@ApiModelProperty(value = "입금 은행명")
	private String bankName;

	@ApiModelProperty(value = "입금 계좌번호")
	private String info;

	@ApiModelProperty(value = "입금 예금주")
	private String paidHolder;

	@ApiModelProperty(value = "입금 금액")
	private String paymentPrice;

	@ApiModelProperty(value = "주문 PK")
	private Long odOrderId;

}
