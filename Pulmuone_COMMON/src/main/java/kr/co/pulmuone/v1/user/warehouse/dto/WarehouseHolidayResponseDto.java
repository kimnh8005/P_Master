package kr.co.pulmuone.v1.user.warehouse.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.user.warehouse.dto.vo.WarehouseHolidayResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "WarehouseHolidayResponseDto")
public class WarehouseHolidayResponseDto extends BaseResponseDto{

	@ApiModelProperty(value = "출고처 휴일 설정 리스트")
	private	List<WarehouseHolidayResultVo> rows;

	@ApiModelProperty(value = "출고처 리스트 총 count")
	private long total;

    @ApiModelProperty(value = "Validation 코드")
    private String validationCode;
}