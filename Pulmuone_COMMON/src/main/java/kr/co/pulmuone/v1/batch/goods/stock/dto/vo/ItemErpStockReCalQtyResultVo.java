package kr.co.pulmuone.v1.batch.goods.stock.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "품목 유통기한별 재고 Result Vo")
public class ItemErpStockReCalQtyResultVo extends BaseRequestDto {

	@ApiModelProperty(value = "재고 수량")
	private int stockQty;

	@ApiModelProperty(value = "재계산된 수량")
	private long totalSum;

	@ApiModelProperty(value = "품목별 출고처 PK")
	private long ilItemWarehouseId;

	@ApiModelProperty(value = "품목 유통기한별 재고ID")
	private long ilItemStockExprId;

	@ApiModelProperty(value = "기준일")
	private String baseDt;

	@ApiModelProperty(value = "유통기한")
	private String expirationDt;

	@ApiModelProperty(value = "재고타입")
	private String stockTp;

}
