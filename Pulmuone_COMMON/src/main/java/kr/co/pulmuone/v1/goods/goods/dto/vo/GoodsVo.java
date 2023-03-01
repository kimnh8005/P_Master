package kr.co.pulmuone.v1.goods.goods.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@ApiModel(description = "상품목록 Vo")
public class GoodsVo {

    @ApiModelProperty(value = "상품 ID")
    private long goodsId;

    @ApiModelProperty(value = "품목코드")
    private String itemCode;

    @ApiModelProperty(value = "품목바코드")
    private String itemBarcode;

    @ApiModelProperty(value = "ERP품목바코드")
    private String erpItemBarcode;

    @ApiModelProperty(value = "품목연동여부")
    private boolean erpIfYn;

    @ApiModelProperty(value = "품목연동여부명")
    private String erpIfYnName;

    @ApiModelProperty(value = "품목유형")
    private String itemTp;

    @ApiModelProperty(value = "품목유형명")
    private String itemTpNm;

    @ApiModelProperty(value = "매장전용유형")
    private String erpO2oExposureTp;

    @ApiModelProperty(value = "상품관리유형")
    private String erpProductTp;

    @ApiModelProperty(value = "프로모션명")
    private String promotionName;

    @ApiModelProperty(value = "상품명")
    private String goodsName;

    @ApiModelProperty(value = "상품등록일")
    private String goodsCreateDate;

    @ApiModelProperty(value = "상품수정일")
    private String goodsModifyDate;

    @ApiModelProperty(value = "상품수정일(시스템)")
    private String goodsSystemModifyDate;

    @ApiModelProperty(value = "프로모션 시작일")
    private String promotionStartDate;

    @ApiModelProperty(value = "프로모션 종료일")
    private String promotionEndDate;

    @ApiModelProperty(value = "상품유형코드")
    private String goodsTypeCode;

    @ApiModelProperty(value = "상품유형명")
    private String goodsTypeName;

    @ApiModelProperty(value = "회원 구매여부")
    private String purchaseMemberYn;

    @ApiModelProperty(value = "임직원 구매여부")
    private String purchaseEmployeeYn;

    @ApiModelProperty(value = "비회원 구매여부")
    private String purchaseNonmemberYn;

    @ApiModelProperty(value = "WEB PC 전시여부")
    private String displayWebPcYn;

    @ApiModelProperty(value = "WEB MOBILE 전시여부")
    private String displayWebMobileYn;

    @ApiModelProperty(value = "APP 전시여부")
    private String displayAppYn;

    @ApiModelProperty(value = "판매유형코드")
    private String saleTypeCode;

    @ApiModelProperty(value = "판매유형명")
    private String saleTypeName;

    @ApiModelProperty(value = "승인상태코드")
    private String approvalStatusCode;

    @ApiModelProperty(value = "승인상태명")
    private String approvalStatusName;

    @ApiModelProperty(value = "판매상태코드")
    private String saleStatusCode;

    @ApiModelProperty(value = "판매상태명")
    private String saleStatusName;

    @ApiModelProperty(value = "전시유무")
    private String displayYn;

    @ApiModelProperty(value = "전시유무명")
    private String displayYnNm;

    @ApiModelProperty(value = "판매시작일")
    private String saleStartDate;

    @ApiModelProperty(value = "판매종료일")
    private String saleEndDate;

    @ApiModelProperty(value = "표준카테고리 ID")
    private Long categoryStandardId;

    @ApiModelProperty(value = "표준카테고리명")
    private String categoryStandardDepthName;

    @ApiModelProperty(value = "전시카테고리 ID")
    private Long categoryId;

    @ApiModelProperty(value = "전시카테고리명")
    private String categoryDepthName;

    @ApiModelProperty(value = "출고처 ID")
    private Long warehouseId;

    @ApiModelProperty(value = "출고처명")
    private String warehouseName;

    @ApiModelProperty(value = "공급처 ID")
    private Long supplierId;

    @ApiModelProperty(value = "공급처 회사 ID")
    private Long supplierCompanyId;

    @ApiModelProperty(value = "공급처명")
    private String supplierName;

    @ApiModelProperty(value = "브랜드 ID")
    private Long brandId;

    @ApiModelProperty(value = "브랜드명")
    private String brandName;

    @ApiModelProperty(value = "보관방법코드")
    private String storageMethodTypeCode;

    @ApiModelProperty(value = "보관방법명")
    private String storageMethodTypeName;

    @ApiModelProperty(value = "재고연동유무")
    private String erpStockIfYn;

    @ApiModelProperty(value = "추가상품 유무")
    private String additionalGoodsYn;

    @ApiModelProperty(value = "쿠폰사용유무")
    private String couponUseYn;

    @ApiModelProperty(value = "MD추천유무")
    private String mdRecommendYn;

    @ApiModelProperty(value = "상품가격 ID")
    private Long goodsPriceId;

    @ApiModelProperty(value = "품목가격 ID")
    private Long itemPriceId;

    @ApiModelProperty(value = "상품할인 ID")
    private Long goodsDiscountId;

    @ApiModelProperty(value = "원가")
    private int standardPrice;

    @ApiModelProperty(value = "정상가")
    private int recommendedPrice;

    @ApiModelProperty(value = "판매가")
    private int salePrice;

    @ApiModelProperty(value = "할인유형코드")
    private String discountTypeCode;

    @ApiModelProperty(value = "할인유형명")
    private String discountTypeName;

    @ApiModelProperty(value = "가격 시작일")
    private String priceStartDate;

    @ApiModelProperty(value = "가격 종료일")
    private String priceEndDate;

    @ApiModelProperty(value = "정상재고갯수")
    private int normalStockCount;

    @ApiModelProperty(value = "임박재고갯수")
    private int imminentStockCount;

    @ApiModelProperty(value = "폐기임박갯수")
    private int disposalStockCount;

    @ApiModelProperty(value = "상품이미지경로")
    private String goodsImagePath;

    @ApiModelProperty(value = "등록자")
    private String createId;

    @ApiModelProperty(value = "전시브랜드 ID")
    private String dpBrandId;

    @ApiModelProperty(value = "전시브랜드명")
    private String dpBrandName;

    @ApiModelProperty(value = "브랜드LV2")
    private String erpBrandName;

    @ApiModelProperty(value = "전시브랜드명")
    private String urSupplierWarehouseId;

    @ApiModelProperty(value = "공급처_출고처_고유값")
    private String ilItemWarehouseId;

    @ApiModelProperty(value = "선주문 여부(Y: 선주문 사용)")
    private String preOrderYn;

    @ApiModelProperty(value = "ilItemStock SEQ")
    private String ilItemStockId;

    @ApiModelProperty(value = "ERP 대카테고리")
    private String erpCtgryLv1Id;

    @ApiModelProperty(value = "전일마감재고/미연동재고")
    private String stockClosingCount;

    @ApiModelProperty(value = "입고확정량")
    private int stockConfirmedCount;

    @ApiModelProperty(value = "입고예정량")
    private int stockScheduledCount;

    @ApiModelProperty(value = "오프라인물류재고량")
    private int stockOfflineCount;

    @ApiModelProperty(value = "단종여부")
    private String extinctionYn;

    @ApiModelProperty(value = "재고여부")
    private String stockOrderYn;

    @ApiModelProperty(value = "재고운영형태")
    private String preOrderNm;

    @ApiModelProperty(value = "외부몰상태")
    private String goodsOutMallSaleStat;

    @ApiModelProperty(value = "외부몰상태명")
    private String goodsOutMallSaleStatName;

    @ApiModelProperty(value = "미연동시재고수량")
    private String notIfStockCnt;

    @ApiModelProperty(value = "품목명")
    private String itemNm;

    @ApiModelProperty(value = "무제한여부")
    private String unlimitStockYn;

    @ApiModelProperty(value = "검색키워드")
    private String searchKywrd;

    @ApiModelProperty(value = "ERP카테고리")
    private String erpCategory;

    @ApiModelProperty(value = "ERP카테고리 1")
    private String erpCategory1;

    @ApiModelProperty(value = "ERP카테고리 2")
    private String erpCategory2;

    @ApiModelProperty(value = "ERP카테고리 3")
    private String erpCategory3;

    @ApiModelProperty(value = "ERP카테고리 4")
    private String erpCategory4;

    @ApiModelProperty(value = "상품군(ERP)")
    private String erpItemGrp;

    @ApiModelProperty(value = "보관방법(ERP)")
    private String erpStorageMethod;

    @ApiModelProperty(value = "원산지(ERP)")
    private String erpOriginNm;

    @ApiModelProperty(value = "원산지(BOS)")
    private String bosOriginNm;

    @ApiModelProperty(value = "유통기간")
    private int distributionPeriod;

    @ApiModelProperty(value = "박스체적")
    private String boxVolume;

    @ApiModelProperty(value = "박스입수량")
    private int pcsPerBox;

    @ApiModelProperty(value = "UOM")
    private String uom;

    @ApiModelProperty(value = "포장단위별 용량")
    private String sizePerPackage;

    @ApiModelProperty(value = "용량(중량)단위")
    private String sizeUnit;

    @ApiModelProperty(value = "구성정보 용량")
    private int qtyPerPackage;

    @ApiModelProperty(value = "구성정보 단위")
    private String packageUnit;

    @ApiModelProperty(value = "PDM 그룹코드")
    private String pdmGroupCd;

    @ApiModelProperty(value = "구매허용범위")
    private String purchaseTarget;

    @ApiModelProperty(value = "판매허용범위")
    private String displayTarget;

    @ApiModelProperty(value = "과세구분")
    private String taxNm;

    @ApiModelProperty(value = "렌탈료/월")
    private Integer rentalFeePerMonth;

    @ApiModelProperty(value = "렌탈 의무사용기간(월)")
    private Integer rentalDueMonth;

    @ApiModelProperty(value = "렌탈 계약금")
    private Integer rentalDeposit;

    @ApiModelProperty(value = "렌탈 설치비")
    private Integer rentalInstallFee;

    @ApiModelProperty(value = "렌탈 등록비")
    private Integer rentalRegistFee;

    @ApiModelProperty(value = "(일일상품)알러지 식단")
    private String goodsDailyAllergyYn;

    @ApiModelProperty(value = "(일일상품)일괄배달설정 여부")
    private String goodsDailyBulkYn;

    @ApiModelProperty(value = "상품정보고시 상품군")
    private String specMasterNm;

    @ApiModelProperty(value = "영양정보표시")
    private String nutritionDispYn;

    @ApiModelProperty(value = "반품가능기간명")
    private String returnPeriodNm;

    @ApiModelProperty(value = "배송불가지역")
    private String undeliverableAreaTpNm;

    @ApiModelProperty(value = "동영상정보")
    private String videoUrl;

    @ApiModelProperty(value = "출고처주소")
    private String warehouseAddress;

    @ApiModelProperty(value = "발주정보(ERP)")
    private String erpPoType;

    @ApiModelProperty(value = "발주정보(BOS)")
    private String poTpNm;

    @ApiModelProperty(value = "배송정책명")
    private String shippingTemplateName;

    @ApiModelProperty(value = "판매유형(매장)")
    private String saleShopYnNm;

    @ApiModelProperty(value = "(일일판매)식단주기")
    private String goodsDailyCycle;

    @ApiModelProperty(value = "ERP 품목명")
    private String erpItemIm;

    @ApiModelProperty(value = "몰인몰카테고리명(기본)")
    private String mallInMallCategoryNm;

    @ApiModelProperty(value = "가격구분")
    private String priceMgm;

    @ApiModelProperty(value = "할인율")
    private String discountRatio;

    @ApiModelProperty(value = "할인액")
    private String discountAmt;

    @ApiModelProperty(value = "상품할인 승인요청자")
    private String goodsDiscountApprReqUser;

    @ApiModelProperty(value = "상품할인 1차승인관리자")
    private String goodsDiscountApprSubUser;

    @ApiModelProperty(value = "상품할인 승인관리자")
    private String goodsDiscountApprUser;

    @ApiModelProperty(value = "가격시작일(임직원할인)")
    private String emplPriceStartDate;

    @ApiModelProperty(value = "가격종료일(임직원할인)")
    private String emplPriceEndDate;

    @ApiModelProperty(value = "할인구분(임직원할인)")
    private String emplDiscountTypeName;

    @ApiModelProperty(value = "할인율(임직원할인)")
    private String emplDiscountRatio;

    @ApiModelProperty(value = "판매가(임직원할인)")
    private String emplDiscountSalePrice;

    @ApiModelProperty(value = "임직원할인 승인요청자")
    private String emplDiscountApprReqUser;

    @ApiModelProperty(value = "임직원할인 1차승인관리자")
    private String emplDiscountApprSubUser;

    @ApiModelProperty(value = "임직원할인 승인관리자")
    private String emplDiscountApprUser;

    @ApiModelProperty(value = "상품 등록 승인 요청자ID")
    private String goodsRegistApprReqUserId;

    @ApiModelProperty(value = "상품 등록 승인 상태 코드")
    private String goodsRegistApprStat;

    @ApiModelProperty(value = "상품 등록 승인 상태 코드명")
    private String goodsRegistApprStatNm;

    @ApiModelProperty(value = "상품 수정 승인 요청자ID")
    private String goodsClientApprReqUserId;

    @ApiModelProperty(value = "상품 수정 승인 상태 코드")
    private String goodsClientApprStat;

    @ApiModelProperty(value = "상품 수정 승인 상태 코드명")
    private String goodsClientApprStatNm;

    @ApiModelProperty(value = "품목 수정 승인 요청자ID")
    private String itemClientApprReqUserId;

    @ApiModelProperty(value = "품목 수정 승인 상태 코드")
    private String itemClientApprStat;

    @ApiModelProperty(value = "품목 수정 승인 상태 코드명")
    private String itemClientApprStatNm;

    @ApiModelProperty(value = "선물하기 허용여부")
    private String presentYn;

    @ApiModelProperty(value = "출고처 코드값 (PS_CONFIG.PS_KEY)")
    private String warehouseCode;

    @ApiModelProperty(value = "주문마감시간")
    private String cutoffTime;

    @ApiModelProperty(value = "배송패턴명")
    private String shippingPatternTitle;

}
