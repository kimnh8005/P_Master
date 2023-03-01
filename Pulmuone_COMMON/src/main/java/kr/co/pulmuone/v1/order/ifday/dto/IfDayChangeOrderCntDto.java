package kr.co.pulmuone.v1.order.ifday.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class IfDayChangeOrderCntDto {



    @ApiModelProperty(value = "주문건수")
    private int orderCnt;

    @ApiModelProperty(value = "주문상세건수")
    private int orderDetlCnt;

    @ApiModelProperty(value = "외부몰건수")
    private int outmallCnt;

}
