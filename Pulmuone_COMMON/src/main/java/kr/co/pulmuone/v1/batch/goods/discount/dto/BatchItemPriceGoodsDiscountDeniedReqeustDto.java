package kr.co.pulmuone.v1.batch.goods.discount.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class BatchItemPriceGoodsDiscountDeniedReqeustDto {

	@ApiModelProperty(value = "승인상태", required = true)
	private String apprStat;

	@ApiModelProperty(value = "현재날짜", required = true)
	private String toDateTime;
}
