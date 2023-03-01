package kr.co.pulmuone.v1.promotion.event.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class RouletteItemByUserVo {

    @ApiModelProperty(value = "룰렛 아이템 PK")
    private Long evEventRouletteItemId;

    @ApiModelProperty(value = "당첨자 혜택")
    private String eventBenefitType;

    @ApiModelProperty(value = "쿠폰 할인 유형")
    private String couponDiscountType;

    @ApiModelProperty(value = "혜택 금액")
    private String benefitPrice;

    @ApiModelProperty(value = "당첨자 혜택 경품 명")
    private String benefitName;

    @ApiModelProperty(value = "쿠폰 list")
    private List<EventCouponVo> coupon;

}
