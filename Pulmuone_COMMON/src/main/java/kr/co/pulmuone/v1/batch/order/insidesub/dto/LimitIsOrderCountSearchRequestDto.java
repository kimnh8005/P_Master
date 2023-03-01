package kr.co.pulmuone.v1.batch.order.insidesub.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 제한시간내 주문건수 조회 검색 RequestDto
 * </PRE>
 */

@Getter
@Setter
@ToString
@Builder
@ApiModel(description = "LimitIsOrderCountSearchRequestDto")
public class LimitIsOrderCountSearchRequestDto {

    @ApiModelProperty(value = "주문제한 시작시간")
    private String orderLimitStartTime;

    @ApiModelProperty(value = "주문제한 종료시간")
    private String orderLimitEndTime;

    @ApiModelProperty(value = "출고처")
    private String urWarehouseId;

    @ApiModelProperty(value = "배송유형")
    private String goodsDeliveryType;

    @ApiModelProperty(value = "주문상태")
    private String[] orderStatusCd;

}
