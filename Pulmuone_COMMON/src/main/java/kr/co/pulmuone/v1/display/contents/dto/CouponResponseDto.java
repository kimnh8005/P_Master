package kr.co.pulmuone.v1.display.contents.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.CouponInfoByUserJoinVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "전시 신규회원 용 쿠폰 정보 ResponseDto")
public class CouponResponseDto {

    @ApiModelProperty(value = "전시 쿠폰 명")
    private String displayCouponName;

    @ApiModelProperty(value = "할인방식")
    private String discountType;

    @ApiModelProperty(value = "할인값")
    private String discountValue;

    @ApiModelProperty(value = "정률:퍼센트 / 정액:금액")
    private String discountValueName;

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

    @ApiModelProperty(value = "사용 PC")
    private String usePcYn;

    @ApiModelProperty(value = "사용 모바일")
    private String useMobileWebYn;

    @ApiModelProperty(value = "사용 App")
    private String useMobileAppYn;

    @ApiModelProperty(value = "최소결재금액")
    private String minPaymentAmount;

    @ApiModelProperty(value = "정률할인 최대할인금액")
    private String percentageMaxDiscountAmount;

    @ApiModelProperty(value = "발급수량제한(1인당 발급제한 건수)")
    private int issueQtyLimit;

    public CouponResponseDto(CouponInfoByUserJoinVo vo) {
        this.displayCouponName = vo.getDisplayCouponName();
        this.discountType = vo.getDiscountType();
        this.discountValue = vo.getDiscountValue();
        this.discountValueName = vo.getDiscountValueName();
        this.issueStartDate = vo.getIssueStartDate();
        this.issueEndDate = vo.getIssueEndDate();
        this.validityType = vo.getValidityType();
        this.validityStartDate = vo.getValidityStartDate();
        this.validityEndDate = vo.getValidityEndDate();
        this.validityDay = vo.getValidityDay();
        this.usePcYn = vo.getUsePcYn();
        this.useMobileWebYn = vo.getUseMobileWebYn();
        this.useMobileAppYn = vo.getUseMobileAppYn();
        this.minPaymentAmount = vo.getMinPaymentAmount();
        this.percentageMaxDiscountAmount = vo.getPercentageMaxDiscountAmount();
        this.issueQtyLimit = vo.getIssueQtyLimit();
    }
}
