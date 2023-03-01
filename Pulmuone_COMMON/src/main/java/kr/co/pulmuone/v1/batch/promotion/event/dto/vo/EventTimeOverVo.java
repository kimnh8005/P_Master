package kr.co.pulmuone.v1.batch.promotion.event.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EventTimeOverVo {

    @ApiModelProperty(value = "이벤트 PK")
    private Long evEventId;

    @ApiModelProperty(value = "종료 일시")
    private String endDateTime;

}
