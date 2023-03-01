package kr.co.pulmuone.v1.statics.data.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.statics.data.dto.vo.DataDownloadStaticsVo;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "DataDownloadStaticsResponseDto")
public class DataDownloadStaticsResponseDto {

    @ApiModelProperty(value = "결과값")
    private List<DataDownloadStaticsVo> rows;

    @ApiModelProperty(value = "건수")
    private long total;

    @ApiModelProperty(value = "추출유형별 결과값")
    private List<String> dataDownloadTypeResult;

}
