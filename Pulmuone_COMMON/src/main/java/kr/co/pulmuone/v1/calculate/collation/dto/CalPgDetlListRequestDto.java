package kr.co.pulmuone.v1.calculate.collation.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
@ApiModel(description = "PG 대사 상세내역 조회 Request Dto")
public class CalPgDetlListRequestDto extends BaseRequestPageDto {

    @ApiModelProperty(value = "기간 조건")
    private String dateSearchType;

    @ApiModelProperty(value = "기간 시작일")
    private String dateSearchStart;

    @ApiModelProperty(value = "기간 종료일")
    private String dateSearchEnd;

    @ApiModelProperty(value = "구분")
    private String salesOrderGubun;

    @ApiModelProperty(value = "구분 List")
    private List<String> salesOrderGubunList;

    @ApiModelProperty(value = "PG 구분")
    private String findPgGubn;

    @ApiModelProperty(value = "매칭여부")
    private String findMatchingType;

    @ApiModelProperty(value = "결제수단")
    private String paymentMethodCode;

    @ApiModelProperty(value = "결제수단 List")
    private List<String> paymentMethodCodeList;

    @ApiModelProperty(value = "검색어 조회")
    private String searchMultiType;

    @ApiModelProperty(value = "검색어")
    private String findKeyword;

    @ApiModelProperty(value = "요약정보보기 구분")
    private String summaryListView;

    @ApiModelProperty(value = "엑셀다운 구분")
    private String excelYn;

    @ApiModelProperty(value = "PG거래내역 업로드 PK")
    private String odPgCompareUploadInfoId;


}
