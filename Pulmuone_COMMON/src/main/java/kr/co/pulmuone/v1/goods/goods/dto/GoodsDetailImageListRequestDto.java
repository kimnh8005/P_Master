package kr.co.pulmuone.v1.goods.goods.dto;

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
@ApiModel(description = "상품 상세 이미지 다운로드 리스트 request")
public class GoodsDetailImageListRequestDto extends BaseRequestPageDto {

    @ApiModelProperty(value = "검색조건")
    private String searchCondition;

    @ApiModelProperty(value = "단일_복수조건검색")
    private String selectConditionType;

    @ApiModelProperty(value = "검색어(단일조건 검색)")
    private String findKeyword;

    @ApiModelProperty(value = "업데이트 항목 조회")
    private String searchUpdateType;

    @ApiModelProperty(value = "검색어 리스트(단일조건 검색)")
    private List<String> findKeywordList;

    @ApiModelProperty(value = "검색어문자열여부")
    private String findKeywordStrFlag;

    @ApiModelProperty(value = "검색어(복수조건 검색)")
    private String findKeywordOnMulti;

    @ApiModelProperty(value = "검색어 리스트(복수조건 검색)")
    private List<String> findKeywordOnMultiList;

    @ApiModelProperty(value = "상품명")
    private String goodsName;

    @ApiModelProperty(value = "담당자")
    private String managerType;

    @ApiModelProperty(value = "담당자")
    private String manager;

    @ApiModelProperty(value = "기간검색유형")
    private String dateSearchType;

    @ApiModelProperty(value = "기간검색 시작일자")
    private String dateSearchStart;

    @ApiModelProperty(value = "기간검색 종료일자")
    private String dateSearchEnd;

    @ApiModelProperty(value = "담당자 검색 조건")
    private String chargeType;

    @ApiModelProperty(value = "검색 내용")
    private String charge;

    @ApiModelProperty(value = "엑셀구분")
    private String excelYn;

}
