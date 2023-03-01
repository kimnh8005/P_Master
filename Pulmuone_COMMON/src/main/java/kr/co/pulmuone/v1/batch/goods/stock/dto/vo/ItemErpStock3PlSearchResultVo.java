package kr.co.pulmuone.v1.batch.goods.stock.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "ERP 재고 연계 Result Vo")
public class ItemErpStock3PlSearchResultVo extends BaseRequestDto {

	@ApiModelProperty(value = "남은 일수")
	private long restDay;

	@ApiModelProperty(value = "재고 수량")
	private long stockQty;

	@ApiModelProperty(value = "예정일")
	private String scheduleDt;

	@ApiModelProperty(value = "출고 기준일")
	private long delivery;

	@ApiModelProperty(value = "품목별 출고처 PK")
	private long ilItemWarehouseId;

	@ApiModelProperty(value = "기준일")
	private String baseDt;

	@ApiModelProperty(value = "유통기한")
	private String expirationDt;

	@ApiModelProperty(value = "재고타입")
	private String stockTp;

	@ApiModelProperty(value = "품목코드")
	private String ilItemCd;

	@ApiModelProperty(value = "출고 기준 관리ID")
	private String ilStockDeadlineId;

	@ApiModelProperty(value = "고객사코드")
	private String strKey;

}
