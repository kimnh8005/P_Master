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
@ApiModel(description = "기획전 전용 장바구니 담기 RequestDto")
public class AddCartPickGoodsRequestDto extends AddCartInfoRequestDto{

	@ApiModelProperty(value = "장바구니 기획전 상품 리스트")
	private List<SpCartPickGoodsRequestDto> pickGoodsList;

	@ApiModelProperty(value = "기획전 골라담기 유형")
	private String cartPromotionType;

	@ApiModelProperty(value = "골라담기 기획전 PK")
	private Long evExhibitId;

}

