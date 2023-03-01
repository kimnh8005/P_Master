package kr.co.pulmuone.v1.shopping.cart.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.store.delivery.dto.ShippingAddressPossibleDeliveryInfoDto;
import kr.co.pulmuone.v1.store.shop.dto.vo.ShopVo;
import kr.co.pulmuone.v1.user.certification.dto.GetSessionShippingResponseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "장바구니 조회 응답 DTO")
public class GetCartInfoResponseDto {

	/**
	 * 배송지 정보
	 */
	private GetSessionShippingResponseDto shippingAddress;

	/**
	 * 배송지 정보
	 */
	private ShippingAddressPossibleDeliveryInfoDto deliveryInfo;

	/**
	 * 장바구니 정보
	 */
	private List<CartDeliveryDto> cart;

	/**
	 * 장바구니 집계 정보
	 */
	private CartSummaryDto cartSummary;

	/**
	 * 정기배송 저보
	 */
	private CartRegularShippingDto regularShipping;

	private CartNotiDto noti;

	private ShopVo store;

	List<CartStoreShippingDto> storeShipping;

	CartStoreShippingFastestScheduleDto fastestSchedule;
}
