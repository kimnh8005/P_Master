package kr.co.pulmuone.v1.goods.item.dto.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ErpNotLinkMasterItemVo {

    /*
     * ERP 미연동 마스터 품목 조회 VO
     */

    private String itemName; // 마스터 품목명

    private String bosCategoryStandardFirstId; // 표준 카테고리 대분류 ID

    private String bosCategoryStandardSecondId; // 표준 카테고리 중분류 ID

    private String bosCategoryStandardThirdId; // 표준 카테고리 소분류 ID

    private String bosCategoryStandardFourthId; // 표준 카테고리 세분류 PK

    private String urSupplierId; // 공급업체 PK

    private String urBrandId; // BOS 브랜드

    private String dpBrandId; // BOS 전시브랜드

    private String bosItemGroup; // BOS 상품군

    private String storageMethodType; // BOS 보관방법

    private int distributionPeriod; // BOS 유통기간

    private Double boxWidth; // 박스 가로

    private Double boxDepth; // 박스 세로

    private Double boxHeight; // 박스 높이

    private Integer piecesPerBox; // 박스 입수량

    private String unitOfMeasurement; // UOM/OMS

    private String originType; // 원산지 ( 해외/국내/기타 )

    private String originDetail; // 원산지 상세( 해외 : 국가코드, 기타:입력 )

    private Double sizePerPackage; // 포장단위별 용량

    private String sizeUnit; // 용량단위

    private String sizeUnitEtc; // 용량단위 (기타)

    private Integer quantityPerPackage; // 포장 구성 수량

    private String packageUnit; // 포장 구성 단위

    private String packageUnitEtc; // 포장 구성 단위 (기타)

    private String taxYn; // BOS 과세구분

    private boolean nutritionDisplayYn; // 영양정보 표시여부

    private String nutritionDisplayDefalut; // 영양정보 기본정보 ( NUTRITION_DISP_Y 값이 N 일때 노출되는 항목 )

    private String nutritionQuantityPerOnce; // 영양분석 단위 (1회 분량)

    private String nutritionQuantityTotal; // 영양분석 단위 (총분량)

    private String nutritionEtc; // 영양성분 기타

    private int ilSpecMasterId; // 상품정보제공고시분류 PK

    private String videoUrl; // 동영상 URL

    private boolean videoAutoplayYn; // 비디오 자동재생 유무 (Y:자동재생)

    private String basicDescription; // 상품상세 기본정보

    private String detaillDescription; // 상품상세 주요정보

}
