package kr.co.pulmuone.v1.promotion.coupon.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "쿠폰발급/사용 RequestDto")
public class CouponIssueRequestDto {
    @ApiModelProperty(value = "쿠폰발급 PK")
    private Long pmCouponIssueId;

    @ApiModelProperty(value = "쿠폰 PK")
    private Long pmCouponId;

    @ApiModelProperty(value = "개별난수번호 PK")
    private Long pmSerialNumberId;

    @ApiModelProperty(value = "유저 PK")
    private Long urUserId;

    @ApiModelProperty(value = "쿠폰발행타입")
    private String couponIssueType;

    @ApiModelProperty(value = "상태")
    private String status;

    @ApiModelProperty(value = "쿠폰사용시작일자")
    private String validityStartDate;
    
    @ApiModelProperty(value = "쿠폰만료일")
    private String expirationDate;
}
