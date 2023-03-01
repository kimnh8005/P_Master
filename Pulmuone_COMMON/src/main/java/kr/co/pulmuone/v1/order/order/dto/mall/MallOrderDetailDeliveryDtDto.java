package kr.co.pulmuone.v1.order.order.dto.mall;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * Mall 도착예정일별 주문상세 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 01. 22.	이규한		최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "Mall 도착예정일별 주문상세 Dto")
public class MallOrderDetailDeliveryDtDto {

    @ApiModelProperty(value = "도착 예정일")
    private String deliveryDt;

    @ApiModelProperty(value = "배송유형 공통코드(GOODS_DELIVERY_TYPE)")
    private String goodsDeliveryType;

    @ApiModelProperty(value = "배송비 - 상담원주문결제시 사용")
    private int shippingPrice;

    @ApiModelProperty(value = "도착 예정일별 결제금액 - 상담원주문결제시 사용")
    private int deliveryDtPaymentPrice;

	@ApiModelProperty(value = "도착예정일별 주문상세 목록")
	private List<MallOrderDetailtrackingNoDto> deliveryDtOrderDetailList;
}
