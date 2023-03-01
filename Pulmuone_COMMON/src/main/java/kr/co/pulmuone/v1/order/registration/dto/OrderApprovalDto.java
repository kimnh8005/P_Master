package kr.co.pulmuone.v1.order.registration.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderCashReceiptVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderPaymentMasterVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderVo;
import lombok.Builder;
import lombok.Getter;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 승인 관련 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 01. 21.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Getter
@ApiModel(description = "주문 승인 관련 Dto")
public class OrderApprovalDto {

    @ApiModelProperty(value = "주문 VO")
    OrderVo order;

    @ApiModelProperty(value = "주문상세 VO")
    OrderDetlVo orderDetlVo;

    @ApiModelProperty(value = "주문 결제 마스터 VO")
    OrderPaymentMasterVo orderPaymentMasterVo;

    @ApiModelProperty(value = "주문 현금영수증 VO")
    OrderCashReceiptVo orderCashReceipt;
}
