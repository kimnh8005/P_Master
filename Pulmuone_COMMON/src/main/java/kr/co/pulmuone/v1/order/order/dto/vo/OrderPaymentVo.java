package kr.co.pulmuone.v1.order.order.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "주문 결제 OD_PAYMENT VO")
public class OrderPaymentVo {
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
