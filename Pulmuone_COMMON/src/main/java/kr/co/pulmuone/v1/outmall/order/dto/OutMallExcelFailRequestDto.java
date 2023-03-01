package kr.co.pulmuone.v1.outmall.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "외부몰 주문 엑셀업로드 FAIL 검색조건 Request Dto")
public class OutMallExcelFailRequestDto extends BaseRequestPageDto {

    @ApiModelProperty(value = "외부몰 타입 S:샤방넷 E:이지어드민")
    private String outmallType;

    @ApiModelProperty(value = "엑셀업로드 현황 PK")
    private long ifOutmallExcelInfoId;

    @ApiModelProperty(value = "실패내역 구분 U:업로드 B:배치 O:업로드원본")
    private String failType;

}

