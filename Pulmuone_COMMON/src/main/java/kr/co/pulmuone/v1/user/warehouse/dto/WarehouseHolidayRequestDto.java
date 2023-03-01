package kr.co.pulmuone.v1.user.warehouse.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetCompanyListRequestDto")
public class WarehouseHolidayRequestDto extends BaseRequestPageDto{

	@ApiModelProperty(value = "출고처 명")
	private String warehouseName;

	@ApiModelProperty(value = "출고처 그룹")
	private String warehouseGroup;

	@ApiModelProperty(value = "출고처 그룹명")
	private String warehouseGroupName;

	@ApiModelProperty(value = "구분명")
	private String dawnYnName;

	@ApiModelProperty(value = "구분명")
	private String dawnYn;

	@ApiModelProperty(value = "검색 시작일")
	private String startHoliday;

	@ApiModelProperty(value = "검색 종료일")
	private String endHoliday;

	@ApiModelProperty(value = "연휴 시작일")
	private String fromDate;

	@ApiModelProperty(value = "연휴 종료일")
	private String toDate;

	@ApiModelProperty(value = "출고처 ID")
	private String urWarehouseId;

	@ApiModelProperty(value = "휴무일")
	private String holiday;

	@ApiModelProperty(value = "연휴구분")
	private String groupYn;

	@ApiModelProperty(value = "휴무일")
	private String scheduleWarehouseName;

	@ApiModelProperty(value = "휴무일")
	private String calendarWarehouseGroup;

	@ApiModelProperty(value = "목록구분")
	private String listYn;

}
