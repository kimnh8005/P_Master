package kr.co.pulmuone.v1.goods.item.dto.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MasterItemListVo {

    /*
     * 마스터 품목 리스트 조회 VO
     */

    private String rowNumber; // 순번

    private String ilItemCode; // 품목 코드

    private String itemBarcode; // 품목 바코드

    private String itemType; // 마스터 품목유형

    private String itemName; // 마스터 품목명

    private String itemTypeName; // 마스터  품목유형명

    private boolean erpLinkIfYn; // ERP 연동여부 ( DB 저장시 => Y:연동, N:미연동 )

    private String erpLinkIfYnName;

    private String supplierName; // 공급업체명

    private String brandName; // 브랜드명

    private String dpBrandName; // 전시 브랜드명

    private String distributionPeriod; // 유통기간

    private String storageMethodName; // 보관방법

    private String standardCategoryFullName; // 표준 카테고리 전체 이름

    private String sizeUnitName; // 용량 (중량) 단위

    private String poTypeName; // 발주유형명

    private String urWarehouseId; // 출고처 PK

    private String warehouseName; // 출고처명

    private String warehouseAddress; // 출고처 주소

    private String preOrderYn; // 선주문 가능여부

    private String stockOrderYn; // 해당 출고처의 재고발주여부

    private String storeYn;	// 해당 출고처의 매장(가맹점)여부

    private String supplierCode;	//해당 공급업체의 코드

    private String mallDivId;	//해당 브랜드의 MALL_DIV_ID(베이비밀, 잇슬림 구분자로 사용)

    private int packageCount;

    private int goodsCount;

    private String erpO2oExposureTp; //매장전용유형

    private String extinctionYn;	// 품목 단종여부

    private String poTpNm;

    private String createDt; //등록일자

    private String modifyDt; //관리자수정일

    private String systemModifyDt; //시스템수정일

    private String erpItemBarcode; //ERP품목바코드

    private String erpProductTp; //상품관리유형

    private String erpCategory; //ERP카테고리

    private String erpItemGrp; //ERP상품군

    private String erpStorageMethod; //ERP보관방법

    private String erpOriginNm; //ERP원산지

    private String bosOriginNm; //BOS원산지

    private String boxVolume; //박스체적

    private int pcsPerBox; //박스입수량

    private String uom; //UOM

    private String sizePerPackage; //포장단위별 용량

    private String sizeUnit; //용량(중량)단위

    private int qtyPerPackage; //구성정보 용량

    private String packageUnit; //구성정보 단위

    private String pdmGroupCd; //PDM 그룹코드

    private String taxNm; //과세구분

    private String priceStartDt; //가격시작일

    private String priceEndDt; //가격종료일

    private int standardPrice; //원가

    private int recommendedPrice; //정상가

    private String marginRatio; //마진율

    private String specMasterNm; //상품정보고시 상품군

    private String nutritionDispYn; //영양정보표시

    private String returnPeriodNm; //반품가능기간

    private String undeliverableAreaTpNm; //배송불가지역

    private String videoUrl; //동영상URL

    private String erpPoType; //발주정보(ERP)

    private String erpItemNm; //ERP 품목명

    private String priceMgm; //가격구분

    private String priceApprTargetYn; //가격승인대상여부

    private String itemPriceApprReqUser; //품목가격 승인요청자

    private String itemPriceApprSubUser; //품목가격 1차승인관리자

    private String itemPriceApprUser; //품목가격 승인관리자

    private String itemRegistApprReqUserId; //품목등록 승인요청자 ID

    private String itemRegistApprStat; //품목등록 승인상태 코드

    private String itemRegistApprStatNm; //품목등록 승인상태명

    private String itemClientApprReqUserId; //품목 거래처 수정 승인요청자 ID

    private String itemClientApprStat; //품목 거래서 수정 승인상태 코드

    private String itemClientApprStatNm; //품목 거래서 수정 승인상태명

    private String approvalStatusName; // 승인상태명

    private String itemStatusTp; //품목 상태 코드

    private String ilGoodsId;		//  상품코드

    private String goodsNm; 		// 상품명

    private String erpSupplierName; // ERP 공급업체

}
