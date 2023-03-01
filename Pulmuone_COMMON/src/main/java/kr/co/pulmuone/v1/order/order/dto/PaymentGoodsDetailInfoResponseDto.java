package kr.co.pulmuone.v1.order.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "주문 완료 상품 정보 조회 응답 DTO")
public class PaymentGoodsDetailInfoResponseDto {

	@ApiModelProperty(value = "상품코드")
	private String goodsId;

	@ApiModelProperty(value = "상품이름")
	private String goodsNm;

	@ApiModelProperty(value = "상품 금액")
	private int recommendedPrice;

	@ApiModelProperty(value = "결제 금액")
	private int paidPrice;

	@ApiModelProperty(value = "상품 주문 수량")
	private int orderCnt;

}
