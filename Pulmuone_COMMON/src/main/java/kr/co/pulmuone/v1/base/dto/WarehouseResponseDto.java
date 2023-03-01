package kr.co.pulmuone.v1.base.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.base.dto.vo.WarehouseVo;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "출고처 Response")
public class WarehouseResponseDto extends BaseResponseDto{

    @ApiModelProperty(value = "출고처 리스트")
    private List<WarehouseVo> rows;
}
