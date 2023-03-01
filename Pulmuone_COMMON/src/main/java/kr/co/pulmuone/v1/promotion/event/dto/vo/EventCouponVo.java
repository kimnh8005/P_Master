package kr.co.pulmuone.v1.promotion.event.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EventCouponVo {

    @ApiModelProperty(value = "이벤트 쿠폰 PK", hidden = true)
    private Long evEventCouponId;

    @ApiModelProperty(value = "쿠폰 PK", hidden = true)
    private Long pmCouponId;

    @ApiModelProperty(value = "쿠폰 명", hidden = true)
    private String displayCouponName;

    @ApiModelProperty(value = "지급 수량", hidden = true)
    private int couponCount;

    @ApiModelProperty(value = "총 지급 수량", hidden = true)
    private int couponTotalCount;

    @ApiModelProperty(value = "실 지급 수량", hidden = true)
    private int couponJoinCount;

    @ApiModelProperty(value = "할인 유형", hidden = true)
    private String discountType;

    @ApiModelProperty(value = "할인 값", hidden = true)
    private String discountValue;



}
