package kr.co.pulmuone.v1.order.claim.dto;


import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.goods.goods.dto.ArrivalScheduledDateDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임 재배송 주문 결제 정보 조회 결과 Dt
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 03. 18.     김명진         최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "주문 클레임 재배송 주문 결제 정보 조회 결과 Dto")
public class OrderClaimReShippingPaymentInfoResponseDto {

	@ApiModelProperty(value = "총건수 ")
	private long total;

	@ApiModelProperty(value = "상품 금액 정보 리스트")
	List<OrderClaimReShippingGoodsPaymentInfoDto> goodsInfoList;

	@ApiModelProperty(value = "출고정보 ")
	private List<OrderClaimViewDeliveryDto> deliveryInfoList;

	@ApiModelProperty(value = "변경 결제 정보")
	private OrderClaimReShippingPaymentInfoDto orderPaymentInfo;
}
