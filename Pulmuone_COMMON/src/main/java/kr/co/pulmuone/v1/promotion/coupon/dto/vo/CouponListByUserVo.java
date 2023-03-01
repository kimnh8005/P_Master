package kr.co.pulmuone.v1.promotion.coupon.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "유저 쿠폰 정보 List Result")
public class CouponListByUserVo {

    @ApiModelProperty(value = "쿠폰번호 PK")
    private String pmCouponId;

    @ApiModelProperty(value = "쿠폰종류")
    private String couponType;

    @ApiModelProperty(value = "쿠폰명")
    private String displayCouponName;

    @ApiModelProperty(value = "할인방식")
    private String discountType;

    @ApiModelProperty(value = "할인값")
    private String discountValue;

    @ApiModelProperty(value = "정률할인 최대할인금액")
    private String percentageMaxDiscountAmount;

    @ApiModelProperty(value = "최소결재금액")
    private String minPaymentAmount;

    @ApiModelProperty(value = "사용기간-시작")
    private String validityStartDate;

    @ApiModelProperty(value = "사용기간-만료")
    private String validityEndDate;

    @ApiModelProperty(value = "유효일")
    private String validityDay;

    @ApiModelProperty(value = "사용범위-PC")
    private String usePcYn;

    @ApiModelProperty(value = "사용범위-MOBILE")
    private String useMobileWebYn;

    @ApiModelProperty(value = "사용범위-APP")
    private String useMobileAppYn;

    @ApiModelProperty(value = "중복갯수")
    private String couponCount;

    @ApiModelProperty(value = "상태")
    private String statusName;

    @ApiModelProperty(value = "발급방법")
    private String issueType;

    @ApiModelProperty(value = "발급방법 세부 지급방법")
    private String issueDetailType;

    @ApiModelProperty(value = "무료배송 구분")
    private String shippingFreeYn = "N";

    @ApiModelProperty(value = "신규가입 구분")
    private String userJoinYn = "N";
}
