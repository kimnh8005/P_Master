package kr.co.pulmuone.v1.promotion.event.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StampJoinByUserVo {

    @ApiModelProperty(value = "스탬프 참여 PK")
    private Long evEventStampJoinId;

    @ApiModelProperty(value = "스탬프번호")
    private int stampCount;

}
