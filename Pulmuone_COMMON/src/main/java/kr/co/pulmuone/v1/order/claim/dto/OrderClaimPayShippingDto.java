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
 * 주문 클레임, 금액, 배송지 Response Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 01. 22.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Getter
@ToString
@ApiModel(description = "주문 클레임, 금액, 배송지 Response Dto")
public class OrderClaimPayShippingDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "주문PK")
    private long odOrderId;

	@ApiModelProperty(value = "주문ID")
	private String odid;

	@ApiModelProperty(value = "상품명")
	private String goodsNm;

	@ApiModelProperty(value = "결제금액")
    private int paidPrice;

    @ApiModelProperty(value = "장바구니쿠폰금액")
    private int cartCouponPrice;

    @ApiModelProperty(value = "상품쿠폰 금액")
    private int goodsCouponPrice;

    @ApiModelProperty(value = "적립금 금액")
    private int pointPrice;

    @ApiModelProperty(value = "배송비")
    private int shippingPrice;

    @ApiModelProperty(value = "결제금액 (적립금 제외)")
    private int paymentPrice;

    @ApiModelProperty(value = "과세결제금액")
    private int taxablePrice;

    @ApiModelProperty(value = "비과세결제금액")
    private int nonTaxablePrice;

    @ApiModelProperty(value = "수령인우편번호")
    private String recvZipCd;

    @ApiModelProperty(value = "환불금액")
    private int refundPrice;
}