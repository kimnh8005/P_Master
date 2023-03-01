package kr.co.pulmuone.v1.goods.goods.dto;

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
@ApiModel(description = "상품리스트 Request")
public class GoodsListRequestDto extends BaseRequestPageDto {

    // 조회조건
    @ApiModelProperty(value = "검색조건")
    private String searchCondition;

    @ApiModelProperty(value = "단일_복수조건검색")
    private String selectConditionType;

    @ApiModelProperty(value = "검색어(단일조건 검색)")
    private String findKeyword;

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

    @ApiModelProperty(value = "업체기준")
    private String companyStandardType;

    @ApiModelProperty(value = "공급업체")
    private String supplierId;

    @ApiModelProperty(value = "공급업체 기준 출고처")
    private String supplierByWarehouseId;

    @ApiModelProperty(value = "출고처 그룹")
    private String warehouseGroup;

    @ApiModelProperty(value = "출고처 그룹 기준 출고처")
    private String warehouseId;

    @ApiModelProperty(value = "표준 브랜드")
    private String brandId;

    @ApiModelProperty(value = "전시 브랜드")
    private String dpBrandId;

    @ApiModelProperty(value = "카테고리 구분")
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

    @ApiModelProperty(value = "상품유형")
    private String goodsType;

    @ApiModelProperty(value = "상품유형 리스트")
    private List<String> goodsTypeList;

    @ApiModelProperty(value = "판매유형")
    private String saleType;

    @ApiModelProperty(value = "판매유형 리스트")
    private List<String> saleTypeList;

    @ApiModelProperty(value = "배송유형")
    private String goodsDeliveryType;

    @ApiModelProperty(value = "배송유형 리스트")
    private List<String> goodsDeliveryTypeList;

    @ApiModelProperty(value = "판매상태")
    private String saleStatus;

    @ApiModelProperty(value = "판매상태 리스트")
    private List<String> saleStatusList;

    @ApiModelProperty(value = "재고운영형태")
    private String stockStatus;

    @ApiModelProperty(value = "재고운영형태 리스트")
    private List<String> stockStatusList;

    @ApiModelProperty(value = "전시여부")
    private String displayYn;

    @ApiModelProperty(value = "ERP 연동여부")
    private String erpIfYn;

    @ApiModelProperty(value = "구매허용범위 전체조회 유무")
    private String purchaseTargetAllYn;

    @ApiModelProperty(value = "회원 구매여부")
    private String purchaseMemberYn;

    @ApiModelProperty(value = "임직원 구매여부")
    private String purchaseEmployeeYn;

    @ApiModelProperty(value = "비회원 구매여부")
    private String purchaseNonmemberYn;

    @ApiModelProperty(value = "판매허용범위 전체조회 유무")
    private String salesAllowanceAllYn;

    @ApiModelProperty(value = "WEB PC 전시여부")
    private String displayWebPcYn;

    @ApiModelProperty(value = "WEB MOBILE 전시여부")
    private String displayWebMobileYn;

    @ApiModelProperty(value = "APP 전시여부")
    private String displayAppYn;

    @ApiModelProperty(value = "보관방법")
    private String storageMethodType;

    @ApiModelProperty(value = "보관방법 리스트")
    private List<String> storageMethodTypeList;

    @ApiModelProperty(value = "외부몰 판매상태")
    private String goodsOutMallSaleStat;

    @ApiModelProperty(value = "외부몰 판매상태 리스트")
    private List<String> goodsOutMallSaleStatList;

    @ApiModelProperty(value = "쿠폰허용여부")
    private String couponUseYn;

    @ApiModelProperty(value = "추가상품유무")
    private String additionalGoodsYn;

    @ApiModelProperty(value = "재고연동 유무")
    private String stockIfYn;

    @ApiModelProperty(value = "MD 추천유무")
    private String mdRecommendYn;

    @ApiModelProperty(value = "매장전용상품유형")
    private String erpProductType;

    @ApiModelProperty(value = "할인적용 유무")
    private String discountType;

    @ApiModelProperty(value = "상품목록 정렬방법")
    private String gridDataSort;

    // 수정
    @ApiModelProperty(value = "상품 ID 리스트")
    private List<Long> goodsIdList;

    @ApiModelProperty(value = "품목코드")
    private String itemCode;

    @ApiModelProperty(value = "판매상태")
    private String gridSaleStatus;

    @ApiModelProperty(value = "엑셀 양식")
    private String psExcelTemplateId;

    @ApiModelProperty(value = "판매기간 구분")
    private String saleDateType;

    @ApiModelProperty(value = "접근권한 출고처 ID 리스트")
    private List<String> listAuthWarehouseId;

    @ApiModelProperty(value = "접근권한 공급업체 ID 리스트")
    private List<String> listAuthSupplierId;

    @ApiModelProperty(value = "선물하기 가능여부")
    private String presentYn;

}
