package kr.co.pulmuone.v1.goods.item.dto.vo;

import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MasterItemVo {

    /*
     * 해당 품목코드로 IL_ITEM 테이블의 전체 정보 조회 Vo
     */

    private String ilItemCode; // 품목코드 PK

    private String itemType; // 품목유형 (공통, 매장전용, 무형, 렌탈)

    private String itemName; // 마스터 품목명

    private String itemBarcode; // BOS 품목바코드

    private boolean erpLinkIfYn; // ERP 연동여부 ( DB 저장시 => Y:연동, N:미연동 )

    private boolean erpStockIfYn; // ERP 재고연동여부 ( DB 저장시 => 연동, N:미연동 )

    private String bosCategoryStandardFirstId; // 표준 카테고리 대분류 ID

    private String bosCategoryStandardSecondId; // 표준 카테고리 중분류 ID

    private String bosCategoryStandardThirdId; // 표준 카테고리 소분류 ID

    private String bosCategoryStandardFourthId; // 표준 카테고리 세분류 ID

    private String urSupplierId; // 공급업체 PK

    private String supplierCd; // 공급업체 코드

    private String urBrandId; // 브랜드 PK

    private String dpBrandId; // 전시 브랜드 PK

    private String storageMethodType; // BOS 보관방법

    private String bosItemGroup; // BOS 상품군

    private String originType; // 원산지 ( 해외/국내/기타 )

    private String originDetail; // 원산지 상세( 해외 : 국가코드, 기타:입력 )

    private int distributionPeriod; // BOS 유통기간

    private Double boxWidth; // 박스 가로

    private Double boxDepth; // 박스 세로

    private Double boxHeight; // 박스 높이

    private Integer piecesPerBox; // 박스 입수량

    private String unitOfMeasurement; // UOM/OMS

    private Double sizePerPackage; // 포장단위별 용량

    private String sizeUnit; // 용량단위

    private String sizeUnitEtc; // 용량단위 (기타)

    private Integer quantityPerPackage; // 포장 구성 수량

    private String packageUnit; // 포장 구성 단위

    private String packageUnitEtc; // 포장 구성 단위 (기타)

    private String pdmGroupCode; // PDM 그룹코드

    private Boolean taxYn; // 과세구분 ( DB 저장시 => Y: 과세, N: 면세 )

    private Integer ilSpecMasterId; // 상품정보제공고시분류 PK

    private boolean nutritionDisplayYn; // 영양정보 표시여부 ( DB 저장시 => Y:표시 )

    private String nutritionDisplayDefalut; // 영양정보 기본정보 ( NUTRITION_DISP_Y 값이 N 일때 노출되는 항목 )

    private String nutritionQuantityPerOnce; // 영양분석 단위 (1회 분량)

    private String nutritionQuantityTotal; // 영양분석 단위 (총분량)

    private String nutritionEtc; // 영양성분 기타

    private Integer ilPoTypeId; // BOS 발주유형 PK : ERP 연동상품일 때만 저장됨

    private String undeliverableAreaType; // 배송불가지역 공통코드 (UNDELIVERABLE_AREA_TP)

    private boolean extinctionYn; // 단종여부 ( DB 저장시 => Y:단종 )

    private String videoUrl; // 동영상 URL

    private boolean videoAutoplayYn; // 비디오 자동재생 유무 (Y:자동재생)

    private String basicDescription; // 상품상세 기본정보

    private String detaillDescription; // 상품상세 주요정보

    private String etcDescription; // 기타정보

    private String erpItemName; // ERP 품목명

    private String erpItemBarcode; // ERP 품목바코드

    private String erpLegalTypeCode; // ERP 법인코드

    private String erpCategoryLevel1Id; // ERP 대카테고리

    private String erpCategoryLevel2Id; // ERP 중카테고리

    private String erpCategoryLevel3Id; // ERP 소카테고리

    private String erpCategoryLevel4Id; // ERP 세부카테고리

    private String erpBrandName; // ERP 브랜드명

    private String erpItemGroup; // ERP 상품군

    private String erpStorageMethod; // ERP 보관정보

    private String erpOriginName; // ERP 원산지

    private Integer erpDistributionPeriod; // ERP 유통기간

    private Double erpBoxWidth; // ERP 박스 가로

    private Double erpBoxDepth; // ERP 박스 세로

    private Double erpBoxHeight; // ERP 박스 높이

    private Double erpItemWidth; // ERP 품목 가로

    private Double erpItemDepth; // ERP 품목 세로

    private Double erpItemHeight; // ERP 품목 높이

    private Double itemDispWeight; // 단품표시중량

    private Double itemRealWeight; // 단품실중량

    private Integer erpPiecesPerBox; // ERP 박스입수량

    private Boolean erpTaxYn; // ERP 과세구분

    private String erpPoType; // ERP 발주정보

    private Boolean erpCanPoYn; // ERP 발주가능여부

    private String erpSupplierName;     // ERP 공급업체

    private String erpNutritionQtyPerOnce; // ERP 영양분석단위 (1회분량)

    private String erpNutritionQtyTotal; // ERP 영양분석단위 (총분량)

    private String erpNutritionEtc; // ERP 영양성분 기타

    private String erpO2oExposureType; // 올가 ERP 전용 : O2O 매장상품 전시구분 (매장품목유형)

    private String erpProductType; // 건강생활 ERP 전용 : 제품/상품 구분 (상품판매유형)

    private String createUserId; // 등록자 ID

    @UserMaskingUserName
    private String createUserName; // 등록자 이름

    private String createDate; // 등록일

    private String recentModifyUserId; // 수정자 ID

    @UserMaskingUserName
    private String recentModifyUserName; // 수정자 이름

    private String recentModifyDate; // 수정일

    /*
     * ERP 품목 정보 업데이트시 수정자 ID
     */
    private Long modifyId; // 수정자 ID

    private Integer rentalFeePerMonth;	// 렌탈료(월)

    private Integer rentalDueMonth;		// 의무사용기간(월)

    private Integer rentalDeposit;		// 계약금

    private Integer rentalInstallFee;	// 설치비

    private Integer rentalRegistFee;   // 등록비

    private String ilItemApprId;

    private String apprStat;

    private String statusCmnt;

    private String itemStatusTp;

    private String apprStatName;

    private String modifyDt;

    private String itemRegistItemApprId; // 품목등록 승인 ID

    private String itemRegistApprReqUserId; // 품목등록 승인 요청자 ID

    private String itemRegistApprStat; // 품목등록 승인상태

    private String itemRegistApprStatName; // 품목등록 승인상태명

    private String itemRegistApprStatusCmnt; // 품목등록 승인상태 변경 코멘트

    private String itemClientItemApprId; // 거래처 품목수정 승인 ID

    private String itemClientApprReqUserId; // 거래처 품목수정 승인 요청자 ID

    private String itemClientApprStat; // 거래처 품목수정 승인상태

    private String itemClientApprStatName; // 거래처 품목수정 승인상태명

    private String itemClientApprStatusCmnt; // 거래처 품목수정 승인상태 변경 코멘트

    private String goodsRegistApprStat; // 상품등록 승인상태

    private String goodsRegistApprStatName; // 상품등록 승인상태명

    private String goodsClientApprStat; // 거래처 상품수정 승인상태

    private String goodsClientApprStatName; // 거래처 상품수정 승인상태명

    // 거래처 승인 화면에서 사용할 변경유무 체크 flag S
    private boolean changedStdBrand; // 표준브랜드 변경여부
    private boolean changedDpBrand; // 전시브랜드 변경여부
    private boolean changedItemGroup; // 상품군 변경여부
    private boolean changedStorageTp; // 보관방법 변경여부
    private boolean changedOrigin; // 원산지 변경여부
    private boolean changedDistributionPeriod; // 유통기간 변경여부
    private boolean changedBoxVolume; // 박스체적 변경여부
    private boolean changedPiecesPerBox; // 박스입수량/UOM 변경여부
    private boolean changedSizePerPackage; // 포장단위별용량 변경여부
    private boolean changedSizeUnit; // 용량(중량)단위 변경여부
    private boolean changedQtyPerPackage; // 포장구성수량 변경여부
    private boolean changedPackageUnit; // 포장구성단위 변경여부
    private boolean changedSpecMaster; // 상품정보 제공고시 변경여부
    private boolean changedItemNutrition; // 상품영양정보 변경여부
    private boolean changedItemNutritionDisplayYn; // 영양정보 표시여부 변경여부
    private boolean changedItemNutritionQuantity; // 영양분석 단위 변경여부
    private boolean changedItemNutritionEtc; // 영양분석 기타 변경여부
    private boolean changedItemCertification; // 상품인증정보 변경여부
    private boolean changedItemImage; // 상품이미지 변경여부
    private boolean changedVideoInfo; // 동영상정보 변경여부
    private boolean changedBasicDesc; // 상품상세 기본정보 변경여부
    private boolean changedDetlDesc; // 상품상세 주요정보 변경여부
    // 거래처 승인 화면에서 사용할 변경유무 체크 flag E

    // 매장 배송 출고처 여부
    private String storeWarehouseYn;
}
