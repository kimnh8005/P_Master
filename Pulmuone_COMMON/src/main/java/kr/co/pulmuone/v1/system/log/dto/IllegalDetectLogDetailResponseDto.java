package kr.co.pulmuone.v1.system.log.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.system.log.dto.vo.IllegalDetectLogResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "IllegalDetectLogDetailResponseDto")
public class IllegalDetectLogDetailResponseDto extends BaseResponseDto {

    @ApiModelProperty(value = "부정거래 탐지 Vo")
    private IllegalDetectLogResultVo rows;
}
