package kr.co.pulmuone.v1.promotion.exhibit.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.search.searcher.dto.GoodsSearchResultDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class SelectGoodsResultDto {

    /**
     * 상품번호
     */
    private Long goodsId;

    /**
     * 상품명
     */
    private String goodsName;

    /**
     * 프로모션명
     */
    private String promotionName;

    /**
     * sizeUnit
     */
    private String sizeUnit;

    /**
     * 썸네일이미지 경로
     */
    private String thumbnailPath;

    /**
     * 브랜드코드
     */
    private String brandId;

    /**
     * 리스팅 전시용 브랜드명
     */
    private String brandName;


    /**
     * 카테고리코드(lev3)
     */
    private String lev3CategoryId;

    /**
     * 카테고리코드(lev2)
     */
    private String lev2CategoryId;

    /**
     * 카테고리코드(lev1)
     */
    private String lev1CategoryId;

    /**
     * 판매상태코드
     */
    private String statusCode;

    /**
     * 정상가
     */
    private Integer recommendedPrice;

    /**
     * 판매가
     */
    private Integer salePrice;

    /**
     * 할인율
     */
    private Integer discountRate;

    /**
     * 임직원 할인가
     */
    private Integer employeeDiscountPrice;

    /**
     * 임직원 할인율
     */
    private Integer employeeDiscountRate;

    /**
     * 몰코드
     */
    private String mallId;

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
     * 비회원구매가능여부
     */
    private String purchaseNonMemberYn;

    /**
     * 회원구매가능여부
     */
    private String purchaseMemberYn;

    /**
     * 임직원구매가능여부
     */
    private String purchaseEmployeeYn;

    /**
     * 상품 구매 허용범위 유형 코드
     */
    private String purchaseTargetType;

    /**
     * 상품등록일
     */
    private String createDateTime;

    /**
     * 구매후기 만족도 점수
     */
    private float satisfactionScore;

    @ApiModelProperty(value = "상품 상세 기본정보")
    private String basicDescription;

    @ApiModelProperty(value = "상품 상세 주요정보")
    private String detailDescription;

    @ApiModelProperty(value = "재고수량")
    private int stockQty = 0;

    public SelectGoodsResultDto(GoodsSearchResultDto dto) {
        this.goodsId = dto.getGoodsId();
        this.goodsName = dto.getGoodsName();
        this.promotionName = dto.getPromotionName();
        this.sizeUnit = dto.getSizeUnit();
        this.thumbnailPath = dto.getThumbnailPath();
        this.brandId = dto.getBrandId();
        this.brandName = dto.getBrandName();
        this.lev3CategoryId = dto.getLev3CategoryId();
        this.lev2CategoryId = dto.getLev2CategoryId();
        this.lev1CategoryId = dto.getLev1CategoryId();
        this.statusCode = dto.getStatusCode();
        this.recommendedPrice = dto.getRecommendedPrice();
        this.salePrice = dto.getSalePrice();
        this.discountRate = dto.getDiscountRate();
        this.employeeDiscountPrice = dto.getEmployeeDiscountPrice();
        this.employeeDiscountRate = dto.getEmployeeDiscountRate();
        this.mallId = dto.getMallId();
        this.isNewGoods = dto.getIsNewGoods();
        this.isRecommendedGoods = dto.getIsRecommendedGoods();
        this.isBestGoods = dto.getIsBestGoods();
        this.hasGift = dto.getHasGift();
        this.isFreeShippingGoods = dto.getIsFreeShippingGoods();
        this.availableCoupon = dto.getAvailableCoupon();
        this.displayPcYn = dto.getDisplayPcYn();
        this.displayMobileYn = dto.getDisplayMobileYn();
        this.displayAppYn = dto.getDisplayAppYn();
        this.purchaseNonMemberYn = dto.getPurchaseNonMemberYn();
        this.purchaseMemberYn = dto.getPurchaseMemberYn();
        this.purchaseEmployeeYn = dto.getPurchaseEmployeeYn();
        this.purchaseTargetType = dto.getPurchaseTargetType();
        this.createDateTime = dto.getCreateDateTime();
        this.satisfactionScore = dto.getSatisfactionScore();
    }
}
