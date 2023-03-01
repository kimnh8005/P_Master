package kr.co.pulmuone.v1.order.claim.dto;


import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
public class OrderClaimReShippingPaymentInfoRequestDto {

	@ApiModelProperty(value = "주문PK")
	private long odOrderId;

	@ApiModelProperty(value = "주문상세PK")
	private long odOrderDetlId;

	@ApiModelProperty(value = "클레임수량")
	private int claimCnt;

	@ApiModelProperty(value = "상품 금액 정보 리스트")
	List<OrderClaimReShippingGoodsPaymentInfoDto> goodsInfoList;
}
