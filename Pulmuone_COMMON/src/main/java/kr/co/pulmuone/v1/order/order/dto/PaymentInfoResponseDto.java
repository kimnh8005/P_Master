package kr.co.pulmuone.v1.order.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "주문 완료 정보 조회 응답 DTO")
public class PaymentInfoResponseDto {

	@ApiModelProperty(value = "주문번호")
	private String odid;

	@ApiModelProperty(value = "결제 총 금액")
	private int totalPrice;

	@ApiModelProperty(value = "전체 주문 수량")
	private int totalOrderCnt;

	@ApiModelProperty(value = "전체 결제 상품 목록")
	private List<PaymentGoodsDetailInfoResponseDto> paymentGoodsList;
}
