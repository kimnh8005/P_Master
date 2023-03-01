package kr.co.pulmuone.v1.promotion.event.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EventBenefitInfoVo {

    @ApiModelProperty(value = "PK")
    private Long evEventBenefitCntId;

    @ApiModelProperty(value = "최대 수")
    private int awardMaxCount;

    @ApiModelProperty(value = "현재 수")
    private int joinCount;

}
