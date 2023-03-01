package kr.co.pulmuone.v1.shopping.cart.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsRequestDto;
import kr.co.pulmuone.v1.search.searcher.constants.SortCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "배송비 절약 상품 목록 요청 DTO")
public class GetSaveShippingCostGoodsListRequestDto extends GoodsRequestDto {

	@ApiModelProperty(value = "배송 타입")
	private String deliveryType;

	@ApiModelProperty(value = "출고처 PK")
	private Long urWarehouseId;

	@ApiModelProperty(value = "분류 타입")
	private SortCode sortCode;

	@ApiModelProperty(value = "판매 타입")
	private String saleType;

}
