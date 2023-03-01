package kr.co.pulmuone.v1.promotion.event.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.promotion.event.dto.vo.EventCouponVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EventCouponResponseDto {

    @ApiModelProperty(value = "쿠폰 명")
    private String displayCouponName;

    @ApiModelProperty(value = "지급 수량")
    private int couponCount;

    public EventCouponResponseDto(EventCouponVo vo) {
        this.displayCouponName = vo.getDisplayCouponName();
        this.couponCount = vo.getCouponCount();
    }
}
