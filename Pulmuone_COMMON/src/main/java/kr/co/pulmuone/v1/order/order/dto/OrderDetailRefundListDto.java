package kr.co.pulmuone.v1.order.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 상세 결제정보(환불) 리스트 관련 Dto
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
@ApiModel(description = "주문 상세 결제정보(환불) 리스트 관련 Dto")
public class OrderDetailRefundListDto {

    @ApiModelProperty(value = "요청일")
    private String createDt;

    @ApiModelProperty(value = "환불일")
    private String approvalDt;

    @ApiModelProperty(value = "총 환불 상품금액")
    private int goodsPrice;

    @ApiModelProperty(value = "배송비")
    private int shippingPrice;

    @ApiModelProperty(value = "귀책구분 B:구매자, G:판매자")
    private String targetTp;

    @ApiModelProperty(value = "쿠폰할인차감")
    private int discountCouponPrice;

    @ApiModelProperty(value = "환불대상금액")
    private int refundTargetPrice;

    @ApiModelProperty(value = "환불포인트금액")
    private int refundPointPrice;

    @ApiModelProperty(value = "임직원환불금")
    private int refundEmployeePrice;

    @ApiModelProperty(value = "환불금액")
    private int refundPrice;

    @ApiModelProperty(value = "클레임상태")
    private String claimStatus;
    
    @ApiModelProperty(value = "결제방법")
    private String payType;

    @ApiModelProperty(value = "환불수단 (D:원결제내역, C:무통장입금)")
    private String refundType;

    @ApiModelProperty(value = "클레임 번호 PK")
	private long odClaimId;

    @ApiModelProperty(value = "연동PG")
    private String pgService;

    @ApiModelProperty(value = "결제타입 (G : 결제, F : 환불 , A : 추가)")
    private String type;

    @ApiModelProperty(value = "결제타입코드")
    private String typeCd;

    @ApiModelProperty(value = "카드번호 (카드)")
    private String cardQuota;

    @ApiModelProperty(value = "결제기관(PG)")
    private String payInfo;

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

    @ApiModelProperty(value = "결제금액")
    private int paymentPrice;

    @ApiModelProperty(value = "매출전표 URL")
    private String salesSlipUrl;

    @ApiModelProperty(value = "클레임상태코드")
    private String claimStatusCd;

    @ApiModelProperty(value = "총 환불금액")
    private int totalRefundPrice;

    @ApiModelProperty(value = "즉시할인")
    private int directPrice;
    
    @ApiModelProperty(value = "환불은행명")
    private String bankNm;
    
    @ApiModelProperty(value = "환불계좌번호")
    private String accountNumber;
    
    @ApiModelProperty(value = "환불예금주")
    private String accountHolder;

    @ApiModelProperty(value = "추가결제타입코드")
    private String addPaymentTp;

    @ApiModelProperty(value = "추가결제타입명")
    private String addPaymentTpNm;

    @ApiModelProperty(value = "결제상태")
    private String status;
}
