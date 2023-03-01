package kr.co.pulmuone.v1.order.claim.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임 추가배송비 정보 조회 결과 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 05. 17.            김명진         최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "주문 클레임 추가배송비 정보 조회 결과 Dto")
public class OrderClaimAddShippingPaymentInfoDto {

	@ApiModelProperty(value = "부분취소여부 true : 부분취소, false : 전체취소")
	private boolean partial;

	@ApiModelProperty(value = "주문 결제 마스터 PK")
	private long odPaymentMasterId;

	@ApiModelProperty(value = "결제타입 (G : 결제, F : 환불 , A : 추가)")
	private String type;

	@ApiModelProperty(value = "결제상태(IR:입금예정,IC:입금완료)")
	private String status;

	@ApiModelProperty(value = "결제방법 공통코드(PAY_TP)")
	private String payTp;

	@ApiModelProperty(value = "PG 종류 공통코드(PG_SERVICE)")
	private String pgService;

	@ApiModelProperty(value = "거래 ID")
	private String tid;

	@ApiModelProperty(value = "승인코드")
	private String authCd;

	@ApiModelProperty(value = "카드번호")
	private String cardNumber;

	@ApiModelProperty(value = "카드 무이자 구분 (Y: 무이자, N: 일반)")
	private String cardQuotaInterest;

	@ApiModelProperty(value = "카드 할부기간")
	private String cardQuota;

	@ApiModelProperty(value = "가상계좌번호")
	private String virtualAccountNumber;

	@ApiModelProperty(value = "입금은행")
	private String bankNm;

	@ApiModelProperty(value = "결제 정보")
	private String info;

	@ApiModelProperty(value = "입금기한")
	private LocalDateTime paidDueDt;

	@ApiModelProperty(value = "입금자명")
	private String paidHolder;

	@ApiModelProperty(value = "부분취소 가능여부")
	private String partCancelYn;

	@ApiModelProperty(value = "에스크로결여부")
	private String escrowYn;

	@ApiModelProperty(value = "배송비")
	private int shippingPrice;

	@ApiModelProperty(value = "과세결제금액")
	private int taxablePrice;

	@ApiModelProperty(value = "결제금액")
	private int paymentPrice;

	@ApiModelProperty(value = "추가결제배송비")
	private int addPaymentShippingPrice;

	@ApiModelProperty(value = "환불 가능 추가 배송비")
	private int refundShippingPrice;

	@ApiModelProperty(value = "환불 요청 배송비")
	private int refundRequestShippingPrice;

	@ApiModelProperty(value = "주문번호")
	private String odid;

	@ApiModelProperty(value = "주문PK")
	private long odOrderId;

	@ApiModelProperty(value = "클레임PK")
	private long odClaimId;
}
