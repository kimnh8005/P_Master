package kr.co.pulmuone.v1.promotion.coupon.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "주문 사용 가능 상품,장바구니 쿠폰 조회 Result")
public class CouponApplicationListByUserVo {

    @ApiModelProperty(value = "쿠폰 이슈 PK")
    private Long pmCouponIssueId;

    @ApiModelProperty(value = "전시 쿠폰명")
    private String displayCouponName;

    @ApiModelProperty(value = "할인 방식")
    private String discountType;

    @ApiModelProperty(value = "할인 값")
    private int discountValue = 0;

    @ApiModelProperty(value = "정률할인 최대할인금액")
    private String percentageMaxDiscountAmount;

    @ApiModelProperty(value = "PC 사용여부")
    private String usePcYn;

    @ApiModelProperty(value = "모바일 사용여부")
    private String useMobileWebYn;

    @ApiModelProperty(value = "APP 사용여부")
    private String useMobileAppYn;

    @ApiModelProperty(value = "장바구니 쿠폰 적용여부")
    private String cartCouponApplyYn;

    @ApiModelProperty(value = "최소결제금액")
    private int minPaymentAmount = 0;

    @ApiModelProperty(value = "쿠폰유형")
    private String couponType;

    @ApiModelProperty(value = "PG 행사여부")
    private String pgPromotionYn;

    @ApiModelProperty(value = "PG 행사 Config Id")
    private String pgPromotionPayConfigId;

    @ApiModelProperty(value = "PG 행사 Group Id")
    private String pgPromotionPayGrpId;

    @ApiModelProperty(value = "PG 행사 Pay Id")
    private String pgPromotionPayId;

    @ApiModelProperty(value = "ERP 코드")
    private String urErpOrganizationCd;

    @ApiModelProperty(value = "percent")
    private int percentage = 0;

}