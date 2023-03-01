package kr.co.pulmuone.v1.promotion.coupon.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "회원인증 이벤트 쿠폰지급관련 관련테이블")
public class CouponJoinCertEventRequestDto {

    @ApiModelProperty(value = "AS-IS 고객번호")
    private String customerNumber;

    @ApiModelProperty(value = "TO_BE 고객아이디")
    private String urUserId;

    @ApiModelProperty(value = "사이트번호 풀샵-0000000000, 올가-0000200000")
    private String siteNo;
}
