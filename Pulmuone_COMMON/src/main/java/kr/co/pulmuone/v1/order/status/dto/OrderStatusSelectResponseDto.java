package kr.co.pulmuone.v1.order.status.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
@ApiModel(description = " 주문상태 조회결과 Response Dto")
public class OrderStatusSelectResponseDto {
    @ApiModelProperty(value = "전체 건수")
    private String orderStatusCd;

}
