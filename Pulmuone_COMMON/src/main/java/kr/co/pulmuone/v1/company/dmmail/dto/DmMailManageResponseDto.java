package kr.co.pulmuone.v1.company.dmmail.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.company.dmmail.dto.vo.DmMailVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "DmMailManageResponseDto")
public class DmMailManageResponseDto {

    @ApiModelProperty(value = "결과코드")
    private String resultCode;

    @ApiModelProperty(value = "결과메시지")
    private String resultMessage;

    @ApiModelProperty(value = "리스트전체건수")
    private long total;

    @ApiModelProperty(value = "리스트")
    private List<?> rows;

    @ApiModelProperty(value = "상세(기본)")
    private DmMailVo detail;

}
