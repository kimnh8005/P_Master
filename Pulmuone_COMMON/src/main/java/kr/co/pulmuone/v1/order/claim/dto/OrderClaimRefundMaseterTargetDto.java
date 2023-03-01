package kr.co.pulmuone.v1.order.claim.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 적립금 환불, 환불금액 대상 조회 결과 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 15.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "적립금 환불, 환불금액 대상 조회 결과 Dto")
public class OrderClaimRefundMaseterTargetDto {

	@ApiModelProperty(value = "주문 클레임 PK")
	private long odClaimId;

	@ApiModelProperty(value = "주문 PK")
	private long odOrderId;

	@ApiModelProperty(value = "귀책구분 B: 구매자, S: 판매자")
	private String targetTp;

	@ApiModelProperty(value = "상품금액")
	private int goodsPrice;

	@ApiModelProperty(value = "장바구니쿠폰금액")
	private int cartCouponPrice;

	@ApiModelProperty(value = "상품쿠폰 금액")
	private int goodsCouponPrice;

	@ApiModelProperty(value = "배송비")
	private int shippingPrice;

	@ApiModelProperty(value = "환불금액 (주문상품금액 - 상품쿠폰 - 장바구니 쿠폰 +- 배송비)")
	private int refundPrice;

	@ApiModelProperty(value = "환불적립금")
	private int refundPointPrice;

	@ApiModelProperty(value = "회원 ID : UR_USER.UR_USER_ID")
	private String urUserId;

	@ApiModelProperty(value = "주문상세 PK")
	private long odOrderDetlId;

	@ApiModelProperty(value = "클레임 처리 수량")
	private int claimCnt;
}
