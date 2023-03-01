package kr.co.pulmuone.v1.promotion.event.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RouletteJoinResponseDto {

    @ApiModelProperty(value = "당첨여부")
    private String winnerYn;

    @ApiModelProperty(value = "룰렛 아이템 PK")
    private Long evEventRouletteItemId;

    public RouletteJoinResponseDto(BenefitResponseDto dto) {
        this.winnerYn = dto.getWinnerYn();
        this.evEventRouletteItemId = dto.getEvEventRouletteItemId();
    }
}
