package kr.co.pulmuone.v1.statics.data.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "DataDownloadStaticsRequestDto")
public class DataDownloadStaticsRequestDto extends BaseRequestPageDto {

    @ApiModelProperty(value = "검색시작일자")
    private String startDt;

    @ApiModelProperty(value = "검색종료일자")
    private String endDt;

    @ApiModelProperty(value = "추출유형 Filter")
    private String dataDownloadFilter;

    @ApiModelProperty(value = "추출유형 List")
    private List<String> dataDownloadList;

}
