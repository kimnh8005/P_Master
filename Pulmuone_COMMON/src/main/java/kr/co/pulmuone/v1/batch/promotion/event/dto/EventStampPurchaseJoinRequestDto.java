package kr.co.pulmuone.v1.batch.promotion.event.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class EventStampPurchaseJoinRequestDto {

    @ApiModelProperty(value = "스탬프 이벤트 참여 PK")
    private Long evEventStampJoinId;

    @ApiModelProperty(value = "스탬프 수")
    private int stampCount;

}
