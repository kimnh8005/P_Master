package kr.co.pulmuone.v1.order.claim.dto;


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
public class OrderClaimReShippingPaymentInfoDto {

	@ApiModelProperty(value = "주문상세PK")
	private long odOrderDetlId;

	@ApiModelProperty(value = "주문유형")
	private String agentTypeCd;

	@ApiModelProperty(value = "정상가")
	private int recommendedPrice;

	@ApiModelProperty(value = "판매가")
	private int salePrice;

	@ApiModelProperty(value = "할인금액")
	private int discountPrice;

	@ApiModelProperty(value = "주문금액")
	private int orderPrice;

	@ApiModelProperty(value = "결제금액")
	private int paidPrice;

	@ApiModelProperty(value = "판매가총합")
	private int totSalePrice;

	@ApiModelProperty(value = "상품쿠폰금액")
	private int goodsCouponPrice;

	@ApiModelProperty(value = "장바구니쿠폰금액")
	private int cartCouponPrice;

	@ApiModelProperty(value = "임직원할인금액")
	private int employeeDiscountPrice;
}
