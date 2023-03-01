package kr.co.pulmuone.v1.batch.order.insidesub.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

/**
 * <PRE>
 * Forbiz Korea
 * 주문제한정보 vo
 * </PRE>
 */

@Getter
@Builder
@ApiModel(description = "OrderLimitInfoVo")
public class OrderLimitInfoVo {

    @ApiModelProperty(value = "주문제한 PK")
    private long odOrderLimitId;

    @ApiModelProperty(value = "주문제한 시작시간")
    private int orderLimitStartTime;

    @ApiModelProperty(value = "주문제한 종료시간")
    private int orderLimitEndTime;

    @ApiModelProperty(value = "주문제한 수량")
    private int orderLimitCount;

    @ApiModelProperty(value = "제한 뒤 미뤄질 배송예정일 일수")
    private int orderLimitAddDay;

    @ApiModelProperty(value = "사용여부")
    private String useYn;

    @ApiModelProperty(value = "등록일")
    private String createDt;

}
