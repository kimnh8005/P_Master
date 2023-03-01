package kr.co.pulmuone.v1.order.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.order.order.dto.mall.MallOrderDetailPayResultDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 상세 결제정보(결제상세) 리스트 관련 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 01. 14.            석세동         최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "주문 상세 결제정보(결제상세) 리스트 관련 Dto")
public class OrderDetailPayDetlListDto {

    @ApiModelProperty(value = "주문일")
    private String createDt;

    @ApiModelProperty(value = "결제일")
    private String approvalDt;

    @ApiModelProperty(value = "판매가 합계 (총상품금액)")
    private String salePrice;

    @ApiModelProperty(value = "배송비 합계 ( 배송비 + 배송비할인금액 )")
    private String shippingPrice;

    @ApiModelProperty(value = "쿠폰할인 합계( 상품쿠폰 + 장바구니쿠폰 + 베송비쿠폰 )")
    private String discountCouponPrice;

    @ApiModelProperty(value = "결제대상금액 ( 총 주문금액 - 쿠폰할인합계 - 즉시할인(임직원인경우, 임직원지원금) )")
    private String paymentTargetPrice;

    @ApiModelProperty(value = "사용적립금")
    private String pointPrice;

    @ApiModelProperty(value = "임직원지원금")
    private String discountEmployeePrice;

    @ApiModelProperty(value = "최종결제금액 ( 결제대상금액 - 적립금사용금액 )")
    private String paymentPrice;

    @ApiModelProperty(value = "결제상태")
    private String status;

    @ApiModelProperty(value = "결제방법")
    private String payType;

    @ApiModelProperty(value = "카드번호 (카드)")
    private String cardQuota;

    @ApiModelProperty(value = "결제기관(PG)")
    private String payInfo;

    @ApiModelProperty(value = "배송비 쿠폰할인금 합계")
    private String shippingCouponPrice;

    @ApiModelProperty(value = "장바구니 쿠폰 할인금액 합계")
    private String cartCouponPrice;

    @ApiModelProperty(value = "상품 쿠폰할인금 합계")
    private String goodsCouponPrice;

    @ApiModelProperty(value = "즉시할인금액 합계")
    private String directPrice;

    @ApiModelProperty(value = "결제금액 합계")
    private String paidPrice;

    @ApiModelProperty(value = "과세결제금액 합계")
    private String taxablePrice;

    @ApiModelProperty(value = "비과세결제금액 합계")
    private String nonTaxablePrice;

    @ApiModelProperty(value = "장바구니 쿠폰 + 상품 쿠폰 할인금 합계")
    private String couponPrice;

    @ApiModelProperty(value = "임직원 여부")
    private String urEmployeeYn;
    
    @ApiModelProperty(value = "거래 ID")
    private String tid;

    @ApiModelProperty(value = "주문번호")
    private String odid;

    @ApiModelProperty(value = "결제방법 공통코드")
    private String payTypeCd;

    @ApiModelProperty(value = "PG종류 공통코드")
    private String pgServiceCd;

    @ApiModelProperty(value = "현금영수증 신청번호")
    private String cashReceiptNo;

    @ApiModelProperty(value = "현금영수증 승인번호")
    private String cashReceiptAuthNo;

    @ApiModelProperty(value = "매출전표 URL")
    private String salesSlipUrl;

    @ApiModelProperty(value = "총 주문금액")
    private String totalOrderPrice;

    @ApiModelProperty(value = "최종결제금액의 합계 ( 결제마스터 결제금액 )")
    private String paymentPriceMaster;

}
