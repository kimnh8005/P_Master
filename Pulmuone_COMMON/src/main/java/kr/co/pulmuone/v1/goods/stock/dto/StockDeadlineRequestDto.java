package kr.co.pulmuone.v1.goods.stock.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "StockDeadlineRequestDto")
public class StockDeadlineRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "공급처 PK")
	private String searchUrSupplierId;

	@ApiModelProperty(value = "출고처 PK")
    private String searchUrWarehouseId;

	@ApiModelProperty(value = "유통기간")
	private String searchDistributionPeriod;

	@ApiModelProperty(value = "유통기간 시작일")
	private String searchDistributionPeriodStart;

	@ApiModelProperty(value = "유통기간 종료일")
	private String searchDistributionPeriodEnd;

	@ApiModelProperty(value = "임박기준(일)")
	private String searchImminent;

	@ApiModelProperty(value = "임박기준 범위")
	private String searchImminentBelow;

	@ApiModelProperty(value = "출고기준(일)")
    private String searchDelivery;

	@ApiModelProperty(value = "출고기준  범위")
    private String searchDeliveryBelow;

	@ApiModelProperty(value = "목표재고(%)")
	private String searchTargetStockRatio;

	@ApiModelProperty(value = "목표재고 범위")
	private String searchTargetStockRatioBelow;

	@ApiModelProperty(value = "검색타입")
	private String searchType;

	@ApiModelProperty(value = "검색 일")
	private String searchDate;

	@ApiModelProperty(value = "검색 시작일")
	private String startCreateDate;

	@ApiModelProperty(value = "검색 종료일")
	private String endCreateDate;

	//입력&수정
	@ApiModelProperty(value = "SEQ")
	private long ilStockDeadlineId;

	@ApiModelProperty(value = "공급처 PK")
	private long urSupplierId;

	@ApiModelProperty(value = "출고처 PK")
    private String urWarehouseId;

	@ApiModelProperty(value = "유통기간")
	private String distributionPeriod;

	@ApiModelProperty(value = "유통기간 시작일")
	private String distributionPeriodStart;

	@ApiModelProperty(value = "유통기간 종료일")
	private String distributionPeriodEnd;

	@ApiModelProperty(value = "임박기준(일)")
	private long imminent;

	@ApiModelProperty(value = "출고기준(일)")
    private long delivery;

	@ApiModelProperty(value = "목표재고(%)")
	private long targetStockRatio;

	@ApiModelProperty(value = "기준")
	private String stdType;

	@ApiModelProperty(value = "등록자")
	private int createId;

	@ApiModelProperty(value = "수정자")
	private int modifyId;

	@ApiModelProperty(value = "org 등록자")
	private int origCreateId;

	@ApiModelProperty(value = "org 등록날짜")
	private String origCreateDate;

	@ApiModelProperty(value = "org 수정자")
	private int origModifyId;

	@ApiModelProperty(value = "org 수정날짜")
	private String origModifyDate;

	@ApiModelProperty(value = "기본설정")
	private String basicYn;

	@ApiModelProperty(value = "팝업-기본설정")
	private String popBasicYn;

}


