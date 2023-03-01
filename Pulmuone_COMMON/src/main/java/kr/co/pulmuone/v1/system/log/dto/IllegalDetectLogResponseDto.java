package kr.co.pulmuone.v1.system.log.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.system.log.dto.vo.IllegalDetectLogResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "IllegalDetectLogResponseDto")
public class IllegalDetectLogResponseDto extends BaseResponseDto {

    @ApiModelProperty(value = "부정거래 탐지 리스트")
    private List<IllegalDetectLogResultVo> rows;

    @ApiModelProperty(value = "총 개수")
    private int total;
}
