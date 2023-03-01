package kr.co.pulmuone.v1.batch.order.insidesub.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

/**
 * <PRE>
 * Forbiz Korea
 * I/F 외 주문 vo
 * </PRE>
 */

@Getter
@Builder
@ApiModel(description = "InterfaceExceptOrderVo")
public class InterfaceExceptOrderVo {

    @ApiModelProperty(value = "주문 PK")
    private String odOrderId;

    @ApiModelProperty(value = "주문번호")
    private String odid;

    @ApiModelProperty(value = "주문상세 PK")
    private String odOrderDetlId;

    @ApiModelProperty(value = "주문상품 순번")
    private String odOrderDetlSeq;

    @ApiModelProperty(value = "주문마감시간")
    private String cutoffTime;

}
