package kr.co.pulmuone.v1.batch.order.dto.search;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 용인물류 주문 List 검색 RequestDto
 * </PRE>
 */

@Getter
@Setter
@ToString
@Builder
@ApiModel(description = "DeliveryOrderSearchRequestDto")
public class DeliveryOrderSearchRequestDto {

    @ApiModelProperty(value = "출고처")
    private String urWarehouseId;

    @ApiModelProperty(value = "배송유형")
    private String[] goodsDeliveryType;

    @ApiModelProperty(value = "주문상태")
    private String orderStatusCd;

    @ApiModelProperty(value = "마감시간 ")
    private String orderDeadlineTime;

    @ApiModelProperty(value = "판매처 리스트")
    private List<String> omSellersIdList;
}
