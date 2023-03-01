package kr.co.pulmuone.v1.goods.item.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ItemStoreInfoDto {

	@ApiModelProperty(value = "매장PK")
	private String urStoreId;

	@ApiModelProperty(value = "매장명")
	private String storeName;

	@ApiModelProperty(value = "매장 판매가")
	private int storeSalePrice;

	@ApiModelProperty(value = "현재재고")
	private int storeStock;

}
