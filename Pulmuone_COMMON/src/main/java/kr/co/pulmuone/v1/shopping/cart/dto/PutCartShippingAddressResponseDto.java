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
@ApiModel(description = "장바구니 배송지 저장 응답 DTO")
public class PutCartShippingAddressResponseDto{

	@ApiModelProperty(value = "배송 불가 안내 상품 안내")
	private boolean isNotShippingGoods;

	@ApiModelProperty(value = "배송 불가 안내 상품 안내")
	private boolean isNotShippingAllGoods;

	@ApiModelProperty(value = "배송 불가 안내 상품 안내")
	private List<String> notShippingGoodsList;

}
