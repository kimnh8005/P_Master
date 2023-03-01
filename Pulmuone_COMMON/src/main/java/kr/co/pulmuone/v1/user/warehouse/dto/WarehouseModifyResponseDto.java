package kr.co.pulmuone.v1.user.warehouse.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.user.warehouse.dto.vo.WarehouseResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "WarehouseModifyResponseDto")
public class WarehouseModifyResponseDto extends BaseResponseDto{

	@ApiModelProperty(value = "상세조회")
	private	WarehouseResultVo rows;
}
