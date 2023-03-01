package kr.co.pulmuone.v1.order.ifday.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "I/F 일자 변경 엑셀업로드 FAIL 검색조건 Request Dto")
public class IfDayExcelFailRequestDto extends BaseRequestPageDto {


    @ApiModelProperty(value = "I/F 일자 변경 업로드 현황 PK")
    private long ifIfDayExcelInfoId;

    @ApiModelProperty(value = "다운로드 타입")
    private String failType;


}

