package kr.co.pulmuone.v1.batch.goods.stock.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class BatchItemStockByGoodsSaleStatusReqeustDto {

	@ApiModelProperty(value = "기준일", required = true)
	private String baseDate;

	@ApiModelProperty(value = "상품유형", required = true)
	private String goodsType;

	@ApiModelProperty(value = "판매상태", required = true)
	private String saleStatus;

	@ApiModelProperty(value = "변경할판매상태", required = true)
	private String chgSaleStatus;

	@ApiModelProperty(value = "비교연산자(재고수량비교용)", required = true)
	private String comparisonOperator;

	@ApiModelProperty(value = "판매 유형", required = true)
	private String saleType;
}
