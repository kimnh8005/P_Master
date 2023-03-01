package kr.co.pulmuone.v1.order.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@ToString
@ApiModel(description = "OrderListDto")
public class OrderDetailHistoryRequestDto {

    @ApiModelProperty(value = "주문 PK")
    private String odOrderId;

    @ApiModelProperty(value = "배치 UR_USER_ID")
    private long batchId;

    @ApiModelProperty(value = "비회원 UR_USER_ID")
    private long guestId;

    @ApiModelProperty(value = "가상계좌 UR_USER_ID")
    private long virtualId;
}
