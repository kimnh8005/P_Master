package kr.co.pulmuone.v1.promotion.event.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CalcBenefitRequestDto {

    @ApiModelProperty(value = "id")
    private int id;

    @ApiModelProperty(value = "비율")
    private long rate;

    @ApiModelProperty(value = "계산용 수")
    private long count;

}
