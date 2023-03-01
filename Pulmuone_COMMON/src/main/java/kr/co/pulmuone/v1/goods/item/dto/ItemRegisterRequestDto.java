package kr.co.pulmuone.v1.goods.item.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.framework.dto.UploadFileDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemPriceVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "마스터 품목 등록 request dto")
public class ItemRegisterRequestDto {

    /*
     * 마스터 품목 등록 request dto
     */

    @ApiModelProperty(value = "품목코드", required = false)
    private String ilItemCode; // 품목코드 PK

    @ApiModelProperty(value = "품목유형", required = false)
    private String itemType; // 품목유형 (공통, 매장전용, 무형, 렌탈)

    @ApiModelProperty(value = "마스터 품목명", required = true)
    private String itemName; // 마스터 품목명

    @ApiModelProperty(value = "BOS 품목바코드", required = false)
    private String itemBarcode; // BOS 품목바코드

    @ApiModelProperty(value = "ERP 연동여부", required = true)
    private boolean erpLinkIfYn; // ERP 연동여부 ( DB 저장시 => Y:연동, N:미연동 )

    @ApiModelProperty(value = "ERP 재고연동여부", required = true)
    private Boolean erpStockIfYn; // ERP 재고연동여부 ( DB 저장시 => 연동, N:미연동 )

    @ApiModelProperty(value = "표준 카테고리 PK", required = true)
    private String ilCategoryStandardId; // 표준 카테고리 PK

    @ApiModelProperty(value = "공급업체", required = true)
    private String urSupplierId; // 공급업체 PK

    @ApiModelProperty(value = "BOS 브랜드", required = true)
    private String urBrandId; // 브랜드 PK

    @ApiModelProperty(value = "전시 브랜드", required = true)
    private String dpBrandId;

    @ApiModelProperty(value = "BOS 보관방법", required = true)
    private String storageMethodType; // BOS 보관방법

    @ApiModelProperty(value = "BOS 상품군", required = true)
    private String bosItemGroup; // BOS 상품군

    @ApiModelProperty(value = "BOS 원산지", required = true)
    private String originType; // 원산지 ( 해외/국내/기타 )

    @ApiModelProperty(value = "BOS 원산지 상세", required = false)
    private String originDetail; // 원산지 상세( 해외 : 국가코드, 기타:입력 )

    @ApiModelProperty(value = "BOS 유통기간", required = true)
    private int distributionPeriod; // BOS 유통기간

    @ApiModelProperty(value = "BOS 박스 가로", required = true)
    private Double boxWidth; // 박스 가로

    @ApiModelProperty(value = "BOS 박스 세로", required = true)
    private Double boxDepth; // 박스 세로

    @ApiModelProperty(value = "BOS 박스 높이", required = true)
    private Double boxHeight; // 박스 높이

    @ApiModelProperty(value = "ERP 품목 가로", required = false)
    private Double erpItemWidth;

    @ApiModelProperty(value = "ERP 품목 세로", required = false)
    private Double erpItemDepth;

    @ApiModelProperty(value = "ERP 품목 높이", required = false)
    private Double erpItemHeight;

    @ApiModelProperty(value = "BOS 박스 입수량", required = true)
    private Integer piecesPerBox; // 박스 입수량

    @ApiModelProperty(value = "UOM/OMS", required = false)
    private String unitOfMeasurement; // UOM/OMS

    @ApiModelProperty(value = "포장단위별 용량", required = true)
    private Double sizePerPackage; // 포장단위별 용량

    @ApiModelProperty(value = "용량단위", required = true)
    private String sizeUnit; // 용량단위

    @ApiModelProperty(value = "용량단위 (기타)", required = false)
    private String sizeUnitEtc; // 용량단위 (기타)

    @ApiModelProperty(value = "포장 구성 수량", required = false)
    private Integer quantityPerPackage; // 포장 구성 수량

    @ApiModelProperty(value = "포장 구성 단위", required = false)
    private String packageUnit; // 포장 구성 단위

    @ApiModelProperty(value = "포장 구성 단위 (기타)", required = false)
    private String packageUnitEtc; // 포장 구성 단위 (기타)

    @ApiModelProperty(value = "PDM 그룹코드", required = false)
    private String pdmGroupCode; // PDM 그룹코드

    @ApiModelProperty(value = "BOS 과세구분", required = true)
    private boolean taxYn; // 과세구분 ( DB 저장시 => Y: 과세, N: 면세 )

    @ApiModelProperty(value = "가격 적용 시작일", required = true)
    private String priceApplyStartDate; // 가격 적용 시작일

    @ApiModelProperty(value = "원가", required = true)
    private String standardPrice; // 원가

    @ApiModelProperty(value = "정상가", required = true)
    private String recommendedPrice; // 정상가

    @ApiModelProperty(value = "상품정보제공고시분류 PK", required = true)
    private int ilSpecMasterId; // 상품정보제공고시분류 PK

    @ApiModelProperty(value = "영양정보 표시여부", required = true)
    private boolean nutritionDisplayYn; // 영양정보 표시여부 ( DB 저장시 => Y:표시 )

    @ApiModelProperty(value = "영양정보 기본정보", required = false)
    private String nutritionDisplayDefalut; // 영양정보 기본정보 ( NUTRITION_DISP_Y 값이 N 일때 노출되는 항목 )

    @ApiModelProperty(value = "영양분석 단위 (1회 분량)", required = false)
    private String nutritionQuantityPerOnce; // 영양분석 단위 (1회 분량)

    @ApiModelProperty(value = "영양분석 단위 (총분량)", required = false)
    private String nutritionQuantityTotal; // 영양분석 단위 (총분량)

    @ApiModelProperty(value = "영양성분 기타", required = false)
    private String nutritionEtc; // 영양성분 기타

//    @ApiModelProperty(value = "BOS 발주유형 PK", required = true)
//    private Integer ilPoTypeId; // BOS 발주유형 PK

    @ApiModelProperty(value = "도서산간지역 (1권역) 배송여부", required = true)
    private boolean islandShippingYn; // 도서산간지역 (1권역) 배송여부 ( true : 배송가능 )

    @ApiModelProperty(value = "제주지역 (2권역) 배송여부", required = true)
    private boolean jejuShippingYn; // 제주지역 (2권역) 배송여부 ( true : 배송가능 )

    @ApiModelProperty(value = "배송불가지역 공통코드", required = true)
    private String undeliverableAreaType; // 배송불가지역 공통코드 (UNDELIVERABLE_AREA_TP)

    @ApiModelProperty(value = "단종여부", required = true)
    private boolean extinctionYn; // 단종여부 ( DB 저장시 => Y:단종 )

    @ApiModelProperty(value = "동영상 URL", required = false)
    private String videoUrl; // 동영상 URL

    @ApiModelProperty(value = "비디오 자동재생 유무", required = true)
    private boolean videoAutoplayYn; // 비디오 자동재생 유무 (Y:자동재생)

    @ApiModelProperty(value = "상품상세 기본정보", required = false)
    private String basicDescription; // 상품상세 기본정보

    @ApiModelProperty(value = "상품상세 주요정보", required = false)
    private String detaillDescription; // 상품상세 주요정보

    @ApiModelProperty(value = "기타정보", required = false)
    private String etcDescription; // 기타정보

    @ApiModelProperty(value = "ERP 품목명", required = false)
    private String erpItemName; // ERP 품목명

    @ApiModelProperty(value = "ERP 품목바코드", required = false)
    private String erpItemBarcode; // ERP 품목바코드

    @ApiModelProperty(value = "ERP 법인코드", required = false)
    private String erpLegalTypeCode;

    @ApiModelProperty(value = "ERP 대카테고리", required = false)
    private String erpCategoryLevel1Id; // ERP 대카테고리

    @ApiModelProperty(value = "ERP 중카테고리", required = false)
    private String erpCategoryLevel2Id; // ERP 중카테고리

    @ApiModelProperty(value = "ERP 소카테고리", required = false)
    private String erpCategoryLevel3Id; // ERP 소카테고리

    @ApiModelProperty(value = "ERP 세부카테고리", required = false)
    private String erpCategoryLevel4Id; // ERP 세부카테고리

    @ApiModelProperty(value = "ERP 브랜드명", required = false)
    private String erpBrandName; // ERP 브랜드명

    @ApiModelProperty(value = "ERP 상품군", required = false)
    private String erpItemGroup; // ERP 상품군

    @ApiModelProperty(value = "ERP 보관정보", required = false)
    private String erpStorageMethod; // ERP 보관정보

    @ApiModelProperty(value = "ERP 원산지", required = false)
    private String erpOriginName; // ERP 원산지

    @ApiModelProperty(value = "ERP 유통기간", required = false)
    private Integer erpDistributionPeriod; // ERP 유통기간

    @ApiModelProperty(value = "ERP 박스 가로", required = false)
    private Double erpBoxWidth; // ERP 박스 가로

    @ApiModelProperty(value = "ERP 박스 세로", required = false)
    private Double erpBoxDepth; // ERP 박스 세로

    @ApiModelProperty(value = "ERP 박스 높이", required = false)
    private Double erpBoxHeight; // ERP 박스 높이

    @ApiModelProperty(value = "ERP 박스입수량", required = false)
    private Integer erpPiecesPerBox; // ERP 박스입수량

    @ApiModelProperty(value = "ERP 과세구분", required = false)
    private Boolean erpTaxYn; // ERP 과세구분

    @ApiModelProperty(value = "ERP 발주정보", required = false)
    private String erpPoType; // ERP 발주정보

    @ApiModelProperty(value = "ERP 발주가능여부", required = false)
    private Boolean erpCanPoYn; // ERP 발주가능여부

    @ApiModelProperty(value = "ERP 영양분석단위 (1회 분량)", required = false)
    private String erpNutritionQtyPerOnce; // ERP 영양분석단위 (1회분량)

    @ApiModelProperty(value = "ERP 영양분석단위 (총분량)", required = false)
    private String erpNutritionQtyTotal; // ERP 영양분석단위 (총분량)

    @ApiModelProperty(value = "ERP 영양성분 기타", required = false)
    private String erpNutritionEtc; // ERP 영양성분 기타

    @ApiModelProperty(value = "ERP O2O 매장상품 전시구분 (매장품목유형)", required = false)
    private String erpO2oExposureType;

    @ApiModelProperty(value = "ERP 제품/상품 구분(상품판매유형)", required = false)
    private String erpProductType;

    @ApiModelProperty(value = "ERP 생산처", required = false)
    private String erpSupplierName;

    @ApiModelProperty(value = "품목별 상품정보제공고시 상세항목 목록", required = false)
    private List<ItemSpecValueRequestDto> addItemSpecValueList;

    @ApiModelProperty(value = "품목별 영양정보 상세항목 목록", required = false)
    private List<ItemNutritionDetailDto> addItemNutritionDetailList;

    @ApiModelProperty(value = "품목별 영양정보 상세항목 정렬 순서", required = false)
    private List<String> itemNutritionDetailOrderList;

    @ApiModelProperty(value = "품목별 상품 인증정보 목록", required = false)
    private List<ItemCertificationDto> addItemCertificationList;

    @ApiModelProperty(value = "품목별 출고처 목록", required = false)
    private List<ItemWarehouseDto> addItemWarehouseList;

    @ApiModelProperty(value = "해당 품목의 대표 이미지 file 명", required = false)
    private String representativeImageName; // 해당 품목의 대표 이미지 file 명

    @ApiModelProperty(value = "상품 이미지 목록", required = false)
    List<UploadFileDto> itemImageUploadResultList;

    @ApiModelProperty(value = "상품 이미지 정렬 순서", required = false)
    private List<String> itemImageOrderList;


    private List<ItemPriceVo> priceList;
    /*
     * 상품 이미지를 저장할 최상위 저장 디렉토리 경로 ( 리사이징 이미지 포함 )
     * BOS 상의 도메인별 파일 업로드시 저장 경로 정보 BosStorageInfoEnum 을 참조할 수 있는 Controller 단에서 값 세팅
     */
    private String imageRootStoragePath;

    private Long createId; // 등록자

    private Long modifyId; // 수정자

    @ApiModelProperty(value = "렌탈료/월", required = false)
    private Integer rentalFeePerMonth;

    @ApiModelProperty(value = "렌탈 의무사용기간(월)", required = false)
    private Integer rentalDueMonth;

    @ApiModelProperty(value = "렌탈계약금", required = false)
    private Integer rentalDeposit;

    @ApiModelProperty(value = "렌탈설치비", required = false)
    private Integer rentalInstallFee;

    @ApiModelProperty(value = "렌탈등록비", required = false)
    private Integer rentalRegistFee;

    @ApiModelProperty(value = "렌탈총금액", required = false)
    private Integer rentalTotalPrice;

    @ApiModelProperty(value = "반품가능기간", required = false)
    private String returnPeriod;

    /* 승인관리자 정보 시작 */
	@ApiModelProperty(value = "승인 관리자 정보", required = false)
	private List<ItemRegistApprRequestDto> itemApprList;

	@ApiModelProperty(value = "승인 종류", required = false)
	private String apprKindTp;

	@ApiModelProperty(value = "페이지 Load 시간", required = false)
	private String loadDateTime;

	@ApiModelProperty(value = "품목 상태 유형 공통코드", required = false)
	private String itemStatusTp;
	/* 승인관리자 정보 끝 */

    @ApiModelProperty(value = "단품표시중량", required = false)
    private Double itemDispWeight;

    @ApiModelProperty(value = "단품실중량", required = false)
    private Double itemRealWeight;

}
