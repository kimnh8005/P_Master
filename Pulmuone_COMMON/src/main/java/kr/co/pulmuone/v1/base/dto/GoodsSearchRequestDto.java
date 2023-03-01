package kr.co.pulmuone.v1.base.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "상품검색 Request")
public class GoodsSearchRequestDto extends BaseRequestPageDto {

    @ApiModelProperty(value = "조회조건 구분(단일/복수)")
    private String selectConditionType;

    @ApiModelProperty(value = "검색 파라미터 구분")
    private String goodsCallType;

    @ApiModelProperty(value = "검색조건")
    private String searchCondition;

    @ApiModelProperty(value = "검색어")
    private String findKeyword;

    @ApiModelProperty(value = "검색어 리스트")
    private List<String> findKeywordList;

    @ApiModelProperty(value = "상품명")
    private String goodsName;

    @ApiModelProperty(value = "공급업체")
    private String supplierId;

    @ApiModelProperty(value = "출고처")
    private String warehouseId;

    @ApiModelProperty(value = "브랜드")
    private String brandId;

    @ApiModelProperty(value = "전시 브랜드")
    private String dpBrandId;

    @ApiModelProperty(value = "상품유형")
    private String goodsType;

    @ApiModelProperty(value = "상품유형 리스트")
    private List<String> goodsTypeList;

    @ApiModelProperty(value = "배송불가지역")
    private String undeliverableAreaType;

    @ApiModelProperty(value = "카테고리구분")
    private String categoryType;

    @ApiModelProperty(value = "표준카테고리 대분류")
    private String categoryStandardDepth1;

    @ApiModelProperty(value = "표준카테고리 중분류")
    private String categoryStandardDepth2;

    @ApiModelProperty(value = "표준카테고리 소분류")
    private String categoryStandardDepth3;

    @ApiModelProperty(value = "표준카테고리 세분류")
    private String categoryStandardDepth4;

    @ApiModelProperty(value = "전시카테고리 대분류")
    private String categoryDepth1;

    @ApiModelProperty(value = "전시카테고리 중분류")
    private String categoryDepth2;

    @ApiModelProperty(value = "전시카테고리 소분류")
    private String categoryDepth3;

    @ApiModelProperty(value = "전시카테고리 세분류")
    private String categoryDepth4;

    @ApiModelProperty(value = "기간검색유형")
    private String dateSearchType;

    @ApiModelProperty(value = "기간검색 시작일자")
    private String dateSearchStart;

    @ApiModelProperty(value = "기간검색 종료일자")
    private String dateSearchEnd;

    @ApiModelProperty(value = "판매상태")
    private String saleStatus;

    @ApiModelProperty(value = "판매상태 리스트")
    private List<String> saleStatusList;

    @ApiModelProperty(value = "전시여부")
    private String displayYn;

    @ApiModelProperty(value = "판매유형")
    private String saleType;

    @ApiModelProperty(value = "판매유형 리스트")
    private List<String> saleTypeList;

    @ApiModelProperty(value = "합배송여부")
    private String bundleYn;

    @ApiModelProperty(value = "배송정책ID")
    private String ilShippingTmplId;

}
