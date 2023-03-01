package kr.co.pulmuone.v1.shopping.cart.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.shopping.cart.dto.vo.CartDeliveryTypeGroupByVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "장바구니 배송 DTO")
public class CartDeliveryDto {

	public CartDeliveryDto(CartDeliveryTypeGroupByVo vo) {
		this.setDeliveryType(vo.getDeliveryType());
		this.setDeliveryTypeName(vo.getDeliveryTypeName());
		this.setDeliveryTypeTotal(vo.getDeliveryTypeTotal());
	}

	/**
	 * 배송 타입
	 */
	private String deliveryType;

	/**
	 * 배송 타입명
	 */
	private String deliveryTypeName;

	/**
	 * 배송 타입 갯수
	 */
	private String deliveryTypeTotal;

	/**
	 * 배송 정책별
	 */
	private List<CartShippingDto> shipping;
}
