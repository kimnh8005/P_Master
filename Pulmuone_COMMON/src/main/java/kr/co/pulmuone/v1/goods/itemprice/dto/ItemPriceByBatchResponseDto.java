package kr.co.pulmuone.v1.goods.itemprice.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "ItemPriceByBatchResponseDto")
public class ItemPriceByBatchResponseDto {

	@ApiModelProperty(value = "원본 등록 성공건구")
	private int addOrigCount;

	@ApiModelProperty(value = "상품가격 성공건수")
	private long addPriceCount;

}

