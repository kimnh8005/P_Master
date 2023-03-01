package kr.co.pulmuone.v1.calculate.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 상품 정산 조회 Request Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 03. 05.	이명수		최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Setter
@Getter
@ToString
@ApiModel(description = "상품 정산 조회 Request Dto")
public class CalOrderListDto {

    @ApiModelProperty(value = "상품정산구분")
    private String typeNm;

    @ApiModelProperty(value = "주문경로(유형)")
    private String agentTypeCdNm;

    @ApiModelProperty(value = "BOS 주문번호")
    private String odid;

    @ApiModelProperty(value = "외부몰 주문번호")
    private String outmallId;

    @ApiModelProperty(value = "수집몰 주문번호")
    private String collectionMallId;

    @ApiModelProperty(value = "판매처")
    private String sellersNm;

    @ApiModelProperty(value = "매장명")
    private String storeName;

    @ApiModelProperty(value = "주문자유형")
    private String buyerTypeCdNm;

    @ApiModelProperty(value = "결제수단")
    private String payTpNm;

    @ApiModelProperty(value = "PG")
    private String pgServiceNm;

    @ApiModelProperty(value = "주문일자")
    private String orderDt;

    @ApiModelProperty(value = "결제일자/환불일자")
    private String approvalDt;

    @ApiModelProperty(value = "주문금액")
    private Long salePrice;

    @ApiModelProperty(value = "배송비")
    private Long shippingPrice;

    @ApiModelProperty(value = "결제금액(환불금액)")
    private Long paymentPrice;

    @ApiModelProperty(value = "매출금액")
    private Long settlePrice;

    @ApiModelProperty(value = "매출금액(VAT제외)")
    private Long vatRemoveSettlePrice;

    @ApiModelProperty(value = "VAT금액")
    private Long vat;

    @ApiModelProperty(value = "적립금사용금액")
    private Long pointPrice;

    @ApiModelProperty(value = "적립금사용금액(이용권)")
    private Long ticketPointPrice;

    @ApiModelProperty(value = "적립금사용금액(무상)")
    private Long freePointPrice;

    @ApiModelProperty(value = "상품쿠폰할인금액합계")
    private Long goodsCouponPrice;

    @ApiModelProperty(value = "장바구니쿠폰할인금액합계")
    private Long cartCouponPrice;

    @ApiModelProperty(value = "배송비쿠폰할인금액합계")
    private Long shippingDiscountPrice;

    @ApiModelProperty(value = "매출금액 합계")
    private Long totalTaxablePrice;

}
