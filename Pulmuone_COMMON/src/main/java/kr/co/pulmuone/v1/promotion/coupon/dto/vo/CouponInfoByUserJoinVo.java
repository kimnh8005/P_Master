package kr.co.pulmuone.v1.promotion.coupon.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "회원가입 대상 쿠폰 Result")
public class CouponInfoByUserJoinVo {

    @ApiModelProperty(value = "쿠폰 PK")
    private Long pmCouponId;

    @ApiModelProperty(value = "쿠폰 종류")
    private String couponType;

    @ApiModelProperty(value = "발급 방법")
    private String issueType;

    @ApiModelProperty(value = "발급 방법 세부 지급 방법")
    private String issueDetailType;

    @ApiModelProperty(value = "전시 쿠폰 명")
    private String displayCouponName;

    @ApiModelProperty(value = "사용 PC")
    private String usePcYn;

    @ApiModelProperty(value = "사용 모바일")
    private String useMobileWebYn;

    @ApiModelProperty(value = "사용 App")
    private String useMobileAppYn;

    @ApiModelProperty(value = "할인방식")
    private String discountType;

    @ApiModelProperty(value = "할인값")
    private String discountValue;

    @ApiModelProperty(value = "정률할인 최대할인금액")
    private String percentageMaxDiscountAmount;

    @ApiModelProperty(value = "최소결재금액")
    private String minPaymentAmount;

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

    @ApiModelProperty(value = "정률:퍼센트 / 정액:금액")
    private String discountValueName;
}
