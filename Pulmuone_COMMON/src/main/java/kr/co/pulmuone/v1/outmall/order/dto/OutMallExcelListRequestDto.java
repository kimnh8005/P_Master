package kr.co.pulmuone.v1.outmall.order.dto;

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
@ApiModel(description = "외부몰 주문 엑셀업로드 내역 검색조건 Request Dto")
public class OutMallExcelListRequestDto extends BaseRequestPageDto {

    @ApiModelProperty(value = "엑셀 형식")
    private String outMallType;

    @ApiModelProperty(value = "관리자 아이디 리스트")
    private List<String> adminIdList;

    @ApiModelProperty(value = "관리자 아이디")
    private String adminSearchValue;

    @ApiModelProperty(value = "등록 시작일자")
    private String createStartDate;

    @ApiModelProperty(value = "등록 종료일자")
    private String createEndDate;

}