package kr.co.pulmuone.v1.promotion.event.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StampJoinResponseDto {

    @ApiModelProperty(value = "스탬프 번호")
    private int stampCount;

    @ApiModelProperty(value = "스탬프 모두 참여여부(Y : 모두참여)")
    private String joinAllYn;

    @ApiModelProperty(value = "당첨여부")
    private String winnerYn;

    @ApiModelProperty(value = "당첨자 혜택")
    private String eventBenefitType;

    @ApiModelProperty(value = "경품명")
    private String benefitName;

    public StampJoinResponseDto(BenefitResponseDto dto) {
        if(dto == null) return;
        this.stampCount = dto.getStampCount();
        this.winnerYn = dto.getWinnerYn();
        this.eventBenefitType = dto.getEventBenefitType();
        this.benefitName = dto.getBenefitName();
        this.joinAllYn = dto.getJoinAllYn();
    }
}
