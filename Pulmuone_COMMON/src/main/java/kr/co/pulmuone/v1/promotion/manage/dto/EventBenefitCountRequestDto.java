package kr.co.pulmuone.v1.promotion.manage.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class EventBenefitCountRequestDto {

    @ApiModelProperty(value = "이벤트 PK")
    private String evEventId;

    @ApiModelProperty(value = "룰렛 이벤트 아이템 PK")
    private String evEventRouletteItemId;

    @ApiModelProperty(value = "당첨 최대 인원 수")
    private int awardMaxCount;

    @ApiModelProperty(value = "생성자 PK")
    private String createId;

}
