package kr.co.pulmuone.v1.order.claim.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 취소 시 배송비 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 01. 27.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Getter
@ToString
@ApiModel(description = "주문 클레임 출고처 기준 상세 조회 Dto")
public class OrderClaimShippingDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "환불신청금액")
	private int refundPrice;

	@ApiModelProperty(value = "환불신청예정 상품금액")
	private int refundSalePrice;

	@ApiModelProperty(value = "주문시 부과된 배송비")
	private int orderShippingPrice;

	@ApiModelProperty(value = "환불 시 추가 배송비")
	private int refundAddShippingPrice;

	@ApiModelProperty(value = "총환불예정금액")
	private int totalRefundPrice;

	@ApiModelProperty(value = "결제수단 환불금액")
	private int payTpRefundPrice;

	@ApiModelProperty(value = "적립금 환불금액")
	private int refundPoint;
}