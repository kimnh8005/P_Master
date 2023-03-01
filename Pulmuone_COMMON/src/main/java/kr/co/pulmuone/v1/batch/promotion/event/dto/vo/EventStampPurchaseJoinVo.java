package kr.co.pulmuone.v1.batch.promotion.event.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EventStampPurchaseJoinVo {

    @ApiModelProperty(value = "스탬프 이벤트 참여 PK")
    private Long evEventStampJoinId;

    @ApiModelProperty(value = "유저 PK")
    private Long urUserId;

}
