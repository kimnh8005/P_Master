package kr.co.pulmuone.v1.order.order.dto.mall;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * Mall 주문상세 결제정보 sql 결과 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 01. 23.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "Mall 주문상세 결제정보 sql 결과 Dto")
public class MallOrderDetailPayResultDto {

    @ApiModelProperty(value = "상품판매금액")
    private int salePrice;

	@ApiModelProperty(value = "배송금액")
    private int shippingPrice;

	@ApiModelProperty(value = "장바구니쿠폰금액")
    private int cartCouponPrice;

	@ApiModelProperty(value = "상품쿠폰금액")
    private int goodsCouponPrice;

    @ApiModelProperty(value = "쿠폰할인금액")
    private int discountPrice;

    @ApiModelProperty(value = "즉시할인금액")
    private int directPrice;

    @ApiModelProperty(value = "주문금액")
    private int paidPrice;

    @ApiModelProperty(value = "결제금액")
    private int paymentPrice;

    @ApiModelProperty(value = "결제마스터금액")
    private int paymentPriceMaster;

    @ApiModelProperty(value = "적립금금액")
    private int pointPrice;

    @ApiModelProperty(value = "결제타입")
    private String payTp;

    @ApiModelProperty(value = "결제일자")
    private String approvalDt;

    @ApiModelProperty(value = "결제정보")
    private String info;

    @ApiModelProperty(value = "가상계좌번호")
    private String virtualAccountNumber;

    @ApiModelProperty(value = "은행명")
    private String bankNm;

    @ApiModelProperty(value = "입금기한")
    private String paidDueDt;

    @ApiModelProperty(value = "카드번호")
    private String cardNumber;

    @ApiModelProperty(value = "카드 무이자 구분")
    private String cardQuotaInterest;

    @ApiModelProperty(value = "카드 할부 기")
    private String cardQuota;

    @ApiModelProperty(value = "주문 상태 코드")
    private String orderStatusCd;

    @ApiModelProperty(value = "거래 ID")
    private String tid;

    @ApiModelProperty(value = "결제방법 공통코드")
    private String payType;

    @ApiModelProperty(value = "주문번호")
    private String odid;

    @ApiModelProperty(value = "PG 종류 공통코드")
    private String pgService;

    @ApiModelProperty(value = "매출전표 URL")
    private String salesSlipUrl;

    @ApiModelProperty(value = "현금영수증 신청번호")
    private String cashReceiptNo;

    @ApiModelProperty(value = "현금영수증 승인번호")
    private String cashReceiptAuthNo;

    @ApiModelProperty(value = "추가결제타입코드")
    private String addPaymentTp;

    @ApiModelProperty(value = "추가결제타입명")
    private String addPaymentTpNm;

    @ApiModelProperty(value = "결제상태")
    private String status;

    /** FRONT 사용 변수 Set */
    @ApiModelProperty(value = "환불신청금액")
    private int refundReqPrice;

    @ApiModelProperty(value = "환불예정 상품 금액")
    private int refundGoodsPrice;

    @ApiModelProperty(value = "주문시 부과된 배송비")
    private int orderShippingPrice;

    @ApiModelProperty(value = "추가결제 배송비")
    private int addPaymentShippingPrice;

    @ApiModelProperty(value = "총환불금액")
    private int totalRefundPrice;

    @ApiModelProperty(value = "결제수단 환불금액")
    private int paymentRefundPrice;

    @ApiModelProperty(value = "결제수단 환불금액")
    private int pointRefundPrice;
    /** FRONT 사용 변수 Set */
}