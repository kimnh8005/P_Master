package kr.co.pulmuone.v1.goods.goods.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "상품 적용가능 쿠폰 Dto")
public class GoodsApplyCouponResponseDto {

    @ApiModelProperty(value = "전시 쿠폰명")
    private String displayCouponName;

    @ApiModelProperty(value = "쿠폰 타입")
    private String couponType;

    @ApiModelProperty(value = "발급기간 시작일")
    private String issueStartDate;

    @ApiModelProperty(value = "발급기간 종료일")
    private String issueEndDate;

    @ApiModelProperty(value = "유효기간 설정타입")
    private String validityType;

    @ApiModelProperty(value = "유효기간 시작일")
    private String validityStartDate;

    @ApiModelProperty(value = "유효기간 종료일")
    private String validityEndDate;

    @ApiModelProperty(value = "유효기간 유효일")
    private String validityDay;

    @ApiModelProperty(value = "발급수량제한(1인당 발급제한 건수)")
    private int issueQtyLimit;

    @ApiModelProperty(value = "사용범위 PC")
    private String usePcYn;

    @ApiModelProperty(value = "사용범위 MOBILE")
    private String useMobileWebYn;

    @ApiModelProperty(value = "사용범위 APP")
    private String useMobileAppYn;

    @ApiModelProperty(value = "할인방식")
    private String discountType;

    @ApiModelProperty(value = "정률:퍼센트 / 정액:금액")
    private int discountValue;

    @ApiModelProperty(value = "쿠폰번호 PK")
    private String pmCouponId;

    @ApiModelProperty(value = "정률할인 최대할인금액")
    private String percentageMaxDiscountAmount;

    @ApiModelProperty(value = "최소결재금액")
    private String minPaymentAmount;

    @ApiModelProperty(value = "정률:퍼센트 / 정액:금액")
    private String discountValueName;

    @ApiModelProperty(value = "고객 기 발급 수량")
    private String issueCount;

    public GoodsApplyCouponResponseDto(GoodsApplyCouponDto dto) {
        this.displayCouponName = dto.getDisplayCouponName();
        this.couponType = dto.getCouponType();
        this.issueStartDate = dto.getIssueStartDate();
        this.issueEndDate = dto.getIssueEndDate();
        this.validityType = dto.getValidityType();
        this.validityStartDate = dto.getValidityStartDate();
        this.validityEndDate = dto.getValidityEndDate();
        this.validityDay = dto.getValidityDay();
        this.issueQtyLimit = dto.getIssueQtyLimit();
        this.usePcYn = dto.getUsePcYn();
        this.useMobileWebYn = dto.getUseMobileWebYn();
        this.useMobileAppYn = dto.getUseMobileAppYn();
        this.discountType = dto.getDiscountType();
        this.discountValue = dto.getDiscountValue();
        this.pmCouponId = dto.getPmCouponIdEncrypt();
        this.percentageMaxDiscountAmount = dto.getPercentageMaxDiscountAmount();
        this.minPaymentAmount = dto.getMinPaymentAmount();
        this.discountValueName = dto.getDiscountValueName();
        this.issueCount = dto.getIssueCount();
    }
}
