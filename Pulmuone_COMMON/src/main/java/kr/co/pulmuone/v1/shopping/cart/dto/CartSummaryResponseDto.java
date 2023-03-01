package kr.co.pulmuone.v1.shopping.cart.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "장바구니 예정금액 조회 응답 DTO")
public class CartSummaryResponseDto {

	@ApiModelProperty(value = "장바구니 배송타입별 정보 DTO")
	private List<CartSummaryShippingDto> shipping;

	@ApiModelProperty(value = "장바구니 총 금액 DTO")
	private CartSummaryDto cartSummary;
}
