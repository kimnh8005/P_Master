package kr.co.pulmuone.v1.goods.item.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ItemStoreStockResponseDto {

	@ApiModelProperty(value = "목록")
	private List<ItemStoreStockDto> rows;

	@ApiModelProperty(value = "목록 카운트")
	private	Long total;

}
