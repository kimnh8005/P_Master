package kr.co.pulmuone.v1.promotion.event.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class RouletteItemValidation {

    @ApiModelProperty(value = "룰렛 아이템 PK")
    private Long evEventRouletteItemId;

    @ApiModelProperty(value = "당첨자 혜택 유형")
    private String eventBenefitType;

    @ApiModelProperty(value = "혜택 PK")
    private Long pmPointId;

    @ApiModelProperty(value = "당첨자 혜택")
    private String benefitName;

    @ApiModelProperty(value = "당첨 확률")
    private Double awardRate;

    @ApiModelProperty(value = "최대 당첨자 수")
    private int awardMaxCount;

    @ApiModelProperty(value = "당첨자 수")
    private int winnerCount;

    @ApiModelProperty(value = "룰렛 아이템 번호")
    private String itemNumber;

    @ApiModelProperty(value = "쿠폰 list")
    private List<EventCouponVo> coupon;

    @ApiModelProperty(value = "예상 참여자 수")
    private int expectJoinUserCount;

}
