package kr.co.pulmuone.v1.user.warehouse.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "WarehouseHolidayResultVo")
public class WarehouseHolidayResultVo {

	@ApiModelProperty(value = "출고처 휴일그룹 ID")
	private String urWarehouseHolidayId;

	@ApiModelProperty(value = "출고처 그룹 코드")
	private String warehouseGroupCd;

	@ApiModelProperty(value = "출고처 그룹명")
	private String warehouseGroupName;

	@ApiModelProperty(value = "출고처 명")
	private String warehouseName;

	@ApiModelProperty(value = "휴일")
	private String holiday;

	@ApiModelProperty(value = "그룹구분")
	private String groupYn;

	@ApiModelProperty(value = "휴일 시작일")
	private String fromDate;

	@ApiModelProperty(value = "휴일 종료일")
	private String toDate;

	@ApiModelProperty(value = "출고처 ID")
	private String urWarehouseId;

	@ApiModelProperty(value = "새벽구분 YN")
	private String dawnDlvryYn;

	@ApiModelProperty(value = "새벽구분")
	private String dawnYn;

	@ApiModelProperty(value = "등록정보")
	private String createInfo;

	@ApiModelProperty(value = "사용자 ID")
	private String urUserId;

	@ApiModelProperty(value = "새벽구분")
	private String dawnYnName;

	@ApiModelProperty(value = "출고처휴일별 그룹키")
	private String holidayGroupKey;

}
