package kr.co.pulmuone.v1.goods.goods.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GoodsDirectLinkCategoryMappingListVo {

    // # 조회조건 -------------------------------------------------------------------------------------------------------
    @ApiModelProperty(value = "단일조건 _ 복수조건 검색")
    private String searchType;

    // # 그리드 ---------------------------------------------------------------------------------------------------------
    @ApiModelProperty(value = "직연동몰")
    private String gearCateType;

    @ApiModelProperty(value = "표준 카테고리 코드")
    private String ilCtgryStdId;

    @ApiModelProperty(value = "풀무원 표준 카테고리")
    private String ilCtgryStdFullName;

    @ApiModelProperty(value = "카테고리 코드")
    private String categoryId;

    @ApiModelProperty(value = "직연동몰 카테고리")
    private String categoryName;

    @ApiModelProperty(value = "매핑여부")
    private String mappingYn;

    // # 네이버 카테고리 매핑 ---------------------------------------------------------------------------------------------
    @ApiModelProperty(value = "카테고리 코드")
    private String code;

    @ApiModelProperty(value = "카테고리명")
    private String name;

    @ApiModelProperty(value = "카테고리 전체명")
    private String fullCategoryName;

    @ApiModelProperty(value = "마지막 카테고리 여부")
    private String lastYn;

    @ApiModelProperty(value = "depth 구분")
    private int depth;

    @ApiModelProperty(value = "(대)카테고리")
    private String categoryId1;
    private String ctgryNmEx1;

    @ApiModelProperty(value = "(중)카테고리")
    private String categoryId2;
    private String ctgryNmEx2;

    @ApiModelProperty(value = "(소)카테고리")
    private String categoryId3;
    private String ctgryNmEx3;

    @ApiModelProperty(value = "(세)카테고리")
    private String categoryId4;
    private String ctgryNmEx4;

    // # 엑셀다운로드 ----------------------------------------------------------------------------------------------
    @ApiModelProperty(value = "(대)카테고리")
    private String ilCtgryStdId1;
    private String ilCtgryStdNm1;

    @ApiModelProperty(value = "(중)카테고리")
    private String ilCtgryStdId2;
    private String ilCtgryStdNm2;

    @ApiModelProperty(value = "(소)카테고리")
    private String ilCtgryStdId3;
    private String ilCtgryStdNm3;

    private String createDt;
    private String createId;

}
