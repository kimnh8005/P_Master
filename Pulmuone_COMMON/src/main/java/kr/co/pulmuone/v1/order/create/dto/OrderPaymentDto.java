package kr.co.pulmuone.v1.order.create.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
*
* <PRE>
* Forbiz Korea
* 주문 복사 관련 I/F
* </PRE>
*
* <PRE>
* <B>History:</B>
 * =======================================================================
 *  버전  :	작성일                		:	작성자      :	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 02. 24.		이규한	최초작성
 * =======================================================================
* </PRE>
*/

@Getter
@Setter
@ToString
@ApiModel(description = "주문복사에서 결제정보 Dto")
public class OrderPaymentDto {

    @ApiModelProperty(value = "주문 결제 마스터 PK")
    private long odPaymentId;

    @ApiModelProperty(value = "주문 결제 마스터 PK")
    private long odOrderId;

    @ApiModelProperty(value = "주문 결제 마스터 PK")
    private long odClaimId;

    @ApiModelProperty(value = "주문 결제 마스터 PK")
    private long odPaymentMasterId;

    @ApiModelProperty(value = "판매가")
    private int salePrice;

    @ApiModelProperty(value = "장바구니 쿠폰 할인금액 합")
    private int cartCouponPrice;

    @ApiModelProperty(value = "상품 쿠폰 할인금액")
    private int goodsCouponPrice;

    @ApiModelProperty(value = "즉시 할인금액 합")
    private int directPrice;

    @ApiModelProperty(value = "결제금액금액 합")
    private int paidPrice;

    @ApiModelProperty(value = "배송비합")
    private int shippingPrice;

    @ApiModelProperty(value = "과세결제금액")
    private int taxablePrice;

    @ApiModelProperty(value = "비과세결제금액")
    private int nonTaxablePrice;

    @ApiModelProperty(value = "결제금액")
    private int paymentPrice;

    @ApiModelProperty(value = "사용적립금금액")
    private int pointPrice;
}
