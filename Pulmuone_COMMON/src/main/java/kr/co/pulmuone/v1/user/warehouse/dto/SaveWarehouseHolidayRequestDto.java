package kr.co.pulmuone.v1.user.warehouse.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import kr.co.pulmuone.v1.user.warehouse.dto.vo.WarehouseHolidayResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "SaveWarehouseHolidayRequestDto")
public class SaveWarehouseHolidayRequestDto extends BaseRequestDto{

	@ApiModelProperty(value = "연휴시작일", required = false)
	String fromDate;

	@ApiModelProperty(value = "연휴종료일", required = false)
	String toDate;

	@ApiModelProperty(value = "변경전 연휴시작일", required = false)
	String oldFromDate;

	@ApiModelProperty(value = "변경전 연휴종료일", required = false)
	String oldToDate;

	@ApiModelProperty(value = "등록일자", required = false)
	String insertData;

	@ApiModelProperty(value = "휴일", required = false)
	String holiday;

	@ApiModelProperty(value = "사용자 ID", required = false)
	String urUserId;

	@ApiModelProperty(value = "등록데이터 리스트", hidden = true)
	List<WarehouseHolidayResultVo> insertSaveDataList = new ArrayList<WarehouseHolidayResultVo>();

	@ApiModelProperty(value = "새벽 구분명")
	private String dawnYn;

	@ApiModelProperty(value = "출고처 ID")
	private String urWarehouseId;

	@ApiModelProperty(value = "연휴구분")
	private String groupYn;

}
