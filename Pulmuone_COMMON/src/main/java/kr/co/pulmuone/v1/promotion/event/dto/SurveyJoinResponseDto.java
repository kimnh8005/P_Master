package kr.co.pulmuone.v1.promotion.event.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
public class SurveyJoinResponseDto {

    @ApiModelProperty(value = "당첨여부")
    private String winnerYn;

    @ApiModelProperty(value = "당첨자 혜택")
    private String eventBenefitType;

    @ApiModelProperty(value = "경품명")
    private String benefitName;

    @ApiModelProperty(value = "쿠폰명")
    List<EventCouponResponseDto> coupon;

    public SurveyJoinResponseDto(BenefitResponseDto dto) {
        this.winnerYn = dto.getWinnerYn();
        this.eventBenefitType = dto.getEventBenefitType();
        this.benefitName = dto.getBenefitName();
        if (dto.getCoupon() == null) return;
        this.coupon = dto.getCoupon().stream()
                .map(EventCouponResponseDto::new)
                .collect(Collectors.toList());
    }
}
