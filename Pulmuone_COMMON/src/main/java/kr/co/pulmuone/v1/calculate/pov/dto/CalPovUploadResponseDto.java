package kr.co.pulmuone.v1.calculate.pov.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CalPovUploadResponseDto {

    @ApiModelProperty(value = "전체 건수")
    private int totalCount;

    @ApiModelProperty(value = "성공 건수")
    private int successCount;

    @ApiModelProperty(value = "실패 건수")
    private int failCount;

    @ApiModelProperty(value = "실패 메세지")
    private String failMessage;

}
