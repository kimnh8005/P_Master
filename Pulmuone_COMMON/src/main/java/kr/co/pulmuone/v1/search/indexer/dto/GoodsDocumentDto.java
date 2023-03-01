package kr.co.pulmuone.v1.search.indexer.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GoodsDocumentDto {

    /**
     * doc_id
     */
    private String id;

    /**
     * 상품번호
     */
    private String goodsId;

    /**
     * 상품명
     */
    private String goodsName;

    /**
     * 프로모션명
     */
    private String promotionName;


    /**
     * 사이즈/용량
     */
    private String sizeUnit;

    /**
     * 공급업체id
     */
    private String supplierId;

    /**
     * 프로모션명+상품명+사이즈/용량
     */
    private String goodsFullName;


    /**
     * 상품검색어
     */
    private String searchKeyword;


    /**
     * 썸네일이미지 경로
     */
    private String thumbnailPath;


    /**
     * 브랜드코드
     */
    private String brandId;

    /**
     * 브랜드명
     */
    private String brandName;


    /**
     * 브랜드관 운영 여부 YN
     */
    private String brandPavilionYn;

    /**
     * 공급업체가 orga 인지 여부 YN
     */
    private String supplierOrgaYn;


    /**
     * 전시 카테고리코드 문자열 (lev3)
     * "001001001,001001002"
     */
    private String lev3CategoryId;

    /**
     * 전시 카테고리코드와 카테고리명 문자열 (lev3)
     * "001001001:소카1,001001002:소카2"
     */
    private String lev3CategoryIdName;

    private String[] lev3CategoryIdList;

    private String[] lev3CategoryIdNameList;


    /**
     * 전시 카테고리코드 문자열 (lev2)
     * "001001,001002"
     */
    private String lev2CategoryId;
    /**
     * 전시 카테고리코드와 카테고리명 문자열 (lev2)
     * "001001:중카1,001002:중카2"
     */
    private String lev2CategoryIdName;

    private String[] lev2CategoryIdList;

    private String[] lev2CategoryIdNameList;


    /**
     * 전시 카테고리코드와 카테고리명 문자열 (lev1)
     * "001,002"
     */
    private String lev1CategoryId;


    /**
     * 전시 카테고리코드와 카테고리명 문자열 (lev1)
     * "001:대카1,002:대카2"
     */
    private String lev1CategoryIdName;

    private String[] lev1CategoryIdList;

    private String[] lev1CategoryIdNameList;


    /**
     * 통합몰 전시 카테고리코드(lev1)
     */
    private String mainLev1CategoryId;

    /**
     * 통합몰 전시 카테고리코드와 코드명 (lev1)
     */
    private String mainLev1CategoryIdName;


    private String[] mainLev1CategoryIdList;


    private String[] mainLev1CategoryIdNameList;


    /**
     * 판매상태코드
     */
    private String statusCode;


    /**
     * 정상가
     */
    private int recommendedPrice;


    /**
     * 판매가
     */
    private int salePrice;


    /**
     * 할인율
     */
    private int discountRate;


    /**
     * 임직원 할인가
     */
    private int employeeDiscountPrice;

    /**
     * 임직원 할인율
     */
    private int employeeDiscountRate;


    /**
     * 몰코드
     */
    private String mallId;

    /**
     * 몰코드와 몰이름
     */
    private String mallIdName;

    private String[] mallIdList;

    private String[] mallIdNameList;


    /**
     * 배송유형코드 문자열
     * "DLV_DAILY,DLV_NORMAL"
     */
    private String deliveryTypeId;

    /**
     * 배송유형코드 배열
     * ["DLV_DAILY","DLV_NORMAL"]
     */
    private String[] deliveryTypeIdList;


    /**
     * 배송유형코드와 코드명 문자열
     * "DLV_DAILY:일일배송,DLV_NORMAL:일반배송"
     */
    private String deliveryTypeIdName;


    /**
     * 배송유형코드와 코드명 배열
     * ["DLV_DAILY:일일배송","DLV_NORMAL:일반배송"]
     */
    private String[] deliveryTypeIdNameList;



    /**
     * 혜택유형코드 문자열
     * "GIFT,FREE_SHIPPING"
     */
    private String benefitTypeId;

    /**
     * 혜택유형코드와 코드명 문자열
     * "GIFT:증정품,FREE_SHIPPING:무료배송"
     */
    private String benefitTypeIdName;


    /**
     * 혜택유형코드 배열
     * ["GIFT","FREE_SHIPPING"]
     */
    private String[] benefitTypeIdList;

    /**
     * 혜택유형코드와 코드명 배열
     * ["GIFT:증정품","FREE_SHIPPING:무료배송"]
     */
    private String[] benefitTypeIdNameList;


    /**
     * 인증유형코드 문자열
     * "HACCP,LOW_CARBON"
     */
    private String certificationTypeId;


    /**
     * 인증유형코드와 코드명 문자열
     * "HACCP:HACCP,LOW_CARBON:저탄소"
     */
    private String certificationTypeIdName;

    /**
     * 인증유형코드 배열
     * ["HACCP","LOW_CARBON"]
     */
    private String[] certificationTypeIdList;

    /**
     * 인증유형코드와 코드명 배열
     * ["HACCP:HACCP","LOW_CARBON:저탄소"]
     */
    private String[] certificationTypeIdNameList;

    /**
     * 보관방법코드
     */
    private String storageMethodId;

    /**
     * 보관방법명
     */
    private String storageMethodName;


    /**
     * 신상품여부
     */
    private Boolean isNewGoods;


    /**
     * MD추천상품여부
     */
    private Boolean isRecommendedGoods;


    /**
     * 베스트상품여부
     */
    private Boolean isBestGoods;

    /**
     * 상품등록일시
     */
    private String createDateTime;

    /**
     * 인기도점수; 랭킹점수
     */
    private float popularityScore;


    /**
     * 인기도 랭킹
     */
    private int popularityRanking;

    /**
     * 증정품 제공 여부
     */
    private Boolean hasGift;

    /**
     * 무료배송 상품 여부
     */
    private Boolean isFreeShippingGoods;

    /**
     * 사용가능한 쿠폰이 있는지 여부
     */
    private Boolean availableCoupon;

    /**
     * 비회원구매가능여부
     */
    private String purchaseNonMemberYn;

    /**
     * 임직원구매가능여부
     */
    private String purchaseEmployeeYn;

    /**
     * 회원구매가능여부
     */
    private String purchaseMemberYn;

    /**
     * 구매대상타입
     */
    private String purchaseTargetType;

    /**
     * PC 전시 여부
     */
    private String displayPcYn;

    /**
     * 모바일Web 전시 여부
     */
    private String displayMobileYn;

    /**
     * 모바일App 전시 여부
     */
    private String displayAppYn;

    /**
     * 전시 허용 범위 문자열
     */
    private String displayChannel;

    /**
     * 전시 허용 범위 배열
     */
    private String[] displayChannelList;

    /**
     * 만족도점수(후기점수)
     */
    private float satisfactionScore;

    /**
     * 상품 타입 
     */
    private String goodsType;

    public void setDocumentId() {
        this.id = this.goodsId;
    }


    public void toArrayMultiValueField() {
        this.mallIdNameList = this.mallIdName.split(",");
        this.mallIdList = this.mallId.split(",");
        this.mainLev1CategoryIdNameList = this.mainLev1CategoryIdName.split(",");
        this.mainLev1CategoryIdList = this.mainLev1CategoryId.split(",");
        this.mainLev1CategoryIdNameList = this.mainLev1CategoryIdName.split(",");
        this.lev1CategoryIdList = this.lev1CategoryId.split(",");
        this.lev1CategoryIdNameList = this.lev1CategoryIdName.split(",");

        if (StringUtils.isNotEmpty(lev2CategoryId)) {
            this.lev2CategoryIdList = this.lev2CategoryId.split(",");
        }
        if (StringUtils.isNotEmpty(lev2CategoryId)) {
            this.lev2CategoryIdNameList = this.lev2CategoryId.split(",");
        }
        if (StringUtils.isNotEmpty(lev3CategoryId)) {
            this.lev3CategoryIdList = this.lev3CategoryId.split(",");
        }
        if (StringUtils.isNotEmpty(lev3CategoryId)) {
            this.lev3CategoryIdNameList = this.lev3CategoryId.split(",");
        }
        if (StringUtils.isNotEmpty(deliveryTypeId)) {
            this.deliveryTypeIdList = this.deliveryTypeId.split(",");
        }
        if (StringUtils.isNotEmpty(deliveryTypeIdName)) {
            this.deliveryTypeIdNameList = this.deliveryTypeIdName.split(",");
        }
        if (StringUtils.isNotEmpty(benefitTypeId)) {
            this.benefitTypeIdList = this.benefitTypeId.split(",");
        }
        if (StringUtils.isNotEmpty(benefitTypeIdName)) {
            this.benefitTypeIdNameList = this.benefitTypeIdName.split(",");
        }
        if (StringUtils.isNotEmpty(certificationTypeId)) {
            this.certificationTypeIdList = this.certificationTypeId.split(",");
        }
        if (StringUtils.isNotEmpty(certificationTypeIdName)) {
            this.certificationTypeIdNameList = this.certificationTypeIdName.split(",");
        }

        this.displayChannelList = this.displayChannel.split(",");
    }

}
