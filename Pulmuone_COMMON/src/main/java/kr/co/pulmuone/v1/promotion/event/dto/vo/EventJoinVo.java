package kr.co.pulmuone.v1.promotion.event.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.promotion.event.dto.BenefitResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class EventJoinVo {

    @ApiModelProperty(value = "이벤트 참여 PK")
    private Long evEventJoinId;

    @ApiModelProperty(value = "이벤트 PK")
    private Long evEventId;

    @ApiModelProperty(value = "유저 PK")
    private Long urUserId;

    @ApiModelProperty(value = "당첨여부")
    private String winnerYn;

    @ApiModelProperty(value = "당첨자 혜택 유형")
    private String eventBenefitType;

    @ApiModelProperty(value = "댓글 분류")
    private String commentValue;

    @ApiModelProperty(value = "댓글")
    private String comment;

    @ApiModelProperty(value = "적립금 PK")
    private Long pmPointId;

    @ApiModelProperty(value = "당첨자 혜택 경품 명")
    private String benefitName;

    @ApiModelProperty(value = "룰렛이벤트 아이템 PK")
    private Long evEventRouletteItemId;

    @ApiModelProperty(value = "스탬프 개수")
    private int stampCount;

    @ApiModelProperty(value = "쿠폰 list pk")
    private List<EventCouponVo> coupon;

    public void setBenefit(BenefitResponseDto dto) {
        this.winnerYn = dto.getWinnerYn();
        this.eventBenefitType = dto.getEventBenefitType();
        this.pmPointId = dto.getPmPointId();
        this.benefitName = dto.getBenefitName();
        this.coupon = dto.getCoupon();
    }

}
