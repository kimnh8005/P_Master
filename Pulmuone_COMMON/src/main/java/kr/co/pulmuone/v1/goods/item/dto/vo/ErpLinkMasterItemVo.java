package kr.co.pulmuone.v1.goods.item.dto.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ErpLinkMasterItemVo {

    /*
     * ERP 연동 마스터 품목 조회 VO
     */

    private String itemName; // 마스터 품목명

    private String bosCategoryStandardFirstId; // 표준 카테고리 대분류 ID

    private String bosCategoryStandardSecondId; // 표준 카테고리 중분류 ID

    private String bosCategoryStandardThirdId; // 표준 카테고리 소분류 ID

    private String bosCategoryStandardFourthId; // 표준 카테고리 세분류 PK

    private String originType; // 원산지 (해외/국내/기타)

    private String originDetail; // 원산지 상세

    private Double sizePerPackage; // 포장단위별 용량

    private String sizeUnit; // 용량단위

    private String sizeUnitEtc; // 용량단위 기타

    private Integer quantityPerPackage; // 포장 구성 수량

    private String packageUnit; // 포장 구성 단위

    private String packageUnitEtc; // 포장 구성 단위 기타

    private boolean nutritionDisplayYn; // 영양정보 표시여부

    private int ilSpecMasterId; // 상품정보제공고시분류 PK

    private String videoUrl; // 동영상 URL

    private boolean videoAutoplayYn; // 비디오 자동재생 유무 (Y:자동재생)

    private String basicDescription; // 상품상세 기본정보

    private String detaillDescription; // 상품상세 주요정보

}
