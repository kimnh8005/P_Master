package kr.co.pulmuone.v1.batch.promotion.event.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class EventStampPurchaseVo {

    @ApiModelProperty(value = "이벤트 PK")
    private Long evEventId;

    @ApiModelProperty(value = "시작 일자")
    private String startDate;

    @ApiModelProperty(value = "종료 일자")
    private String endDate;

    @ApiModelProperty(value = "스탬프 지급조건 금액")
    private int orderPrice;

    @ApiModelProperty(value = "스탬프 최대 수량")
    private int stampCount2;
    
    @ApiModelProperty(value = "스탬프 이벤트 참여 PK")
    private List<EventStampPurchaseJoinVo> join;

}
