package kr.co.pulmuone.v1.order.claim.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임 추가 배송비 결제 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 03. 22.    김명진         최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "주문 클레임 추가 배송비 결제 Dto")
public class OrderClaimAddPaymentShippingPriceDto {

	@ApiModelProperty(value = "배송정책PK")
	private long ilShippingTmplId;

	@ApiModelProperty(value = "출고처PK")
	private long urWarehouseId;

	@ApiModelProperty(value = "추가결제배송비")
	private int addPaymentShippingPrice;
}
