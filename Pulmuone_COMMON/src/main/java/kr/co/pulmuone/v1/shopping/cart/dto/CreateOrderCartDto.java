package kr.co.pulmuone.v1.shopping.cart.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.user.certification.dto.GetSessionShippingResponseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "주문 생성 장바구니 정보")
public class CreateOrderCartDto {

	/**
	 * 구매자 정보
	 */
	private CartBuyerDto buyer;

	/**
	 * 배송지 정보
	 */
	private GetSessionShippingResponseDto shippingZone;

	/**
	 * 장바구니 정보
	 */
	private List<CartDeliveryDto> cartList;

	/**
	 * 장바구니 집계 정보
	 */
	private CartSummaryDto cartSummary;

	/**
	 * 장바구니 정기배송 정보
	 */
	private CartRegularDto regular;

	/**
	 * 매장 정보
	 */
	private CartStoreDto store;
}
