package kr.co.pulmuone.v1.promotion.event.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.promotion.event.dto.vo.EventCouponVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class BenefitResponseDto {

    @ApiModelProperty(value = "스탬프 번호 - 스탬프 이벤트 사용")
    private int stampCount;

    @ApiModelProperty(value = "스탬프 모두 참여여부(Y : 모두참여)")
    private String joinAllYn;

    @ApiModelProperty(value = "룰렛 아이템 PK - 룰렛 이벤트 사용")
    private Long evEventRouletteItemId;
    
    @ApiModelProperty(value = "당첨여부")
    private String winnerYn;

    @ApiModelProperty(value = "당첨자 혜택")
    private String eventBenefitType;

    @ApiModelProperty(value = "혜택 PK")
    private Long pmPointId;

    @ApiModelProperty(value = "경품명")
    private String benefitName;

    @ApiModelProperty(value = "쿠폰 리스트")
    private List<EventCouponVo> coupon;

}
