package kr.co.pulmuone.v1.shopping.cart.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.enums.ShoppingEnums.ApplyPayment;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "장바구니 체크 응답 DTO")
public class CheckCartResponseDto {

	@ApiModelProperty(value = "결과 코드")
	private ApplyPayment result;

	@ApiModelProperty(value = "에러 상품 리스트")
	private List<CheckCartGoodsDto> goodsList;

	@ApiModelProperty(value = "체크 장바구니 상품 정보")
	List<CartDeliveryDto> cart;

	@ApiModelProperty(value = "알림정보")
	CartNotiDto noti;
}
