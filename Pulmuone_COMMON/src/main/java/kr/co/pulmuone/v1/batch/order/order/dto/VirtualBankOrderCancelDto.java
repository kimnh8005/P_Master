package kr.co.pulmuone.v1.batch.order.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "입금기한 지난 가상계좌 취소 대상 DTO")
public class VirtualBankOrderCancelDto{

	@ApiModelProperty(value = "주문번호")
	private String odid;

	@ApiModelProperty(value = "주문 PK")
	private Long odOrderId;

	@ApiModelProperty(value = "회원 PK")
	private Long urUserId;

	@ApiModelProperty(value = "비회원 CI")
	private String guestCi;

	@ApiModelProperty(value = "회원 로그인 ID")
	private String loginId;

	@ApiModelProperty(value = "결제금액")
	private int paymentPrice;

	@ApiModelProperty(value = "사용적립금 금액")
	private Long pointPrice;

	@ApiModelProperty(value = "상품명")
	private String goodsName;



}
