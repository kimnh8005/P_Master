package kr.co.pulmuone.v1.goods.stock.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "StockDeadlineHistParamDto")
@Builder
public class StockDeadlineHistParamDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "타입")
	private String histTp;

	@ApiModelProperty(value = "재고 기한관리 SEQ")
	private long ilStockDeadlineId;

	@ApiModelProperty(value = "공급처 PK")
	private long urSupplierId;

	@ApiModelProperty(value = "출고처 PK")
    private String urWarehouseId;

	@ApiModelProperty(value = "유통기간")
	private String distributionPeriod;

	@ApiModelProperty(value = "임박기준(일)")
	private long imminent;

	@ApiModelProperty(value = "출고기준(일)")
    private long delivery;

	@ApiModelProperty(value = "목표재고(%)")
	private long targetStockRatio;

	@ApiModelProperty(value = "org 등록자")
	private int origCreateId;

	@ApiModelProperty(value = "org 등록날짜")
	private String origCreateDate;

	@ApiModelProperty(value = "org 수정자")
	private int origModifyId;

	@ApiModelProperty(value = "org 수정날짜")
	private String origModifyDate;

	@ApiModelProperty(value = "등록자")
	private int createId;

	@ApiModelProperty(value = "기본설정")
	private String basicYn ;

	@ApiModelProperty(value = "팝업-기본설정")
	private String popBasicYn ;

}


