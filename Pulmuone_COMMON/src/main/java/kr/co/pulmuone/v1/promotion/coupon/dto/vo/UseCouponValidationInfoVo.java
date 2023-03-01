package kr.co.pulmuone.v1.promotion.coupon.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "쿠폰 사용 검증 정보 조회 Result")
public class UseCouponValidationInfoVo {

    @ApiModelProperty(value = "쿠폰 결재 상태")
    private String couponStatus;

    @ApiModelProperty(value = "PC 사용 가능여부")
    private String usePcYn;

    @ApiModelProperty(value = "WEB 사용 가능여부")
    private String useMobileWebYn;

    @ApiModelProperty(value = "APP 사용 가능여부")
    private String useMobileAppYn;

    @ApiModelProperty(value = "쿠폰 상태")
    private String issueStatus;

    @ApiModelProperty(value = "쿠폰 만료일")
    private String expirationDate;

}
