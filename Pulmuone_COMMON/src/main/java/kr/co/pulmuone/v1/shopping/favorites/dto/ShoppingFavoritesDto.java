package kr.co.pulmuone.v1.shopping.favorites.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "찜한 상품 Dto")
public class ShoppingFavoritesDto
{

	@ApiModelProperty(value = "찜한 상품 PK")
	private Long spFavoritesGoodsId;


}
