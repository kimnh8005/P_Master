package kr.co.pulmuone.v1.goods.stock.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "재고 기한관리 Result Vo")
public class StockDeadlineResultVo {

	@ApiModelProperty(value = "NO")
	private long no;

	@ApiModelProperty(value = "SEQ")
	private long ilStockDeadlineId;

	@ApiModelProperty(value = "공급처 PK")
	private long urSupplierId;

	@ApiModelProperty(value = "출고처 PK")
    private long urWarehouseId;

	@ApiModelProperty(value = "유통기간")
	private long distributionPeriod;

	@ApiModelProperty(value = "임박기준(일)")
	private long imminent;

	@ApiModelProperty(value = "출고기준(일)")
    private long delivery;

	@ApiModelProperty(value = "목표재고(%)")
	private long targetStockRatio;

	@ApiModelProperty(value = "공급처 명")
	private String compName;

	@ApiModelProperty(value = "출고처 명")
	private String warehouseName;

	@ApiModelProperty(value = "등록자")
	private int createId;

	@ApiModelProperty(value = "등록 일자")
	private String createDate;

	@ApiModelProperty(value = "수정자")
	private int modifyId;

	@ApiModelProperty(value = "수정 일자")
	private String modifyDate;

	@ApiModelProperty(value = "기본설정")
	private String basicYn;

	@ApiModelProperty(value = "팝업-기본설정")
	private String popBasicYn;

	@ApiModelProperty(value = "임박기준%")
	private String imminentPer;

	@ApiModelProperty(value = "출고기준%")
	private String deliveryPer;

}
