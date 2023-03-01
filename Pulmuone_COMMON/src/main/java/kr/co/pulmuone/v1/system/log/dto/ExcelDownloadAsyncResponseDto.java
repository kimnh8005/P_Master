package kr.co.pulmuone.v1.system.log.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class ExcelDownloadAsyncResponseDto extends BaseResponseDto {

    @ApiModelProperty(value = "결과값 row")
    private String rows;

    @ApiModelProperty(value = "총 count")
    private long total;

}
