package kr.co.pulmuone.v1.goods.itemprice.dto;

import java.util.HashMap;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "품목가격 Response VO")
public class ItemPriceResponseDto {

	@ApiModelProperty(value = "상품가격 성공건수")
	private long addPriceCount;

}

