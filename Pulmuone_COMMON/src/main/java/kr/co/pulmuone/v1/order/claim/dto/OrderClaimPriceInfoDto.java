package kr.co.pulmuone.v1.order.claim.dto;


import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.order.claim.dto.vo.ClaimDetlDiscountVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임 환불정보에서 상품금액 조회 결과 Dto
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
@ApiModel(description = "주문 클레임 환불정보에서 상품금액 조회 결과 Dto")
public class OrderClaimPriceInfoDto {

	@ApiModelProperty(value = "주문상품금액(판매가합계)= 환불상품예정금액")
	private int goodsPrice;

	@ApiModelProperty(value = "상품판매가 합계 금액(할인금액 제외) = 상품 판매가 * 수량")
	private int goodsSalePriceSum;

	@ApiModelProperty(value = "상품쿠폰")
	private int goodsCouponPrice;

	@ApiModelProperty(value = "장바구니쿠폰")
	private int cartCouponPrice;

	@ApiModelProperty(value = "즉시할인 금액")
	private int directPrice;

	@ApiModelProperty(value = "임직원지원금")
	private int employeePrice;

	@ApiModelProperty(value = "환불금액 = 결제수단 환불금액")
	private int refundPrice;

	@ApiModelProperty(value = "잔여결제금액")
	private int remainPaymentPrice;

	@ApiModelProperty(value = "환불적립금액 = 적립금환불금액")
	private int refundPointPrice;

	@ApiModelProperty(value = "잔여적립금")
	private int remainPointPrice;

	@ApiModelProperty(value = "추가 배송비 = 환불시 추가 배송비")
	private int shippingPrice;

	@ApiModelProperty(value = "환불시 추가 배송비")
	private int addPaymentShippingPrice;

	@ApiModelProperty(value = "기 결제한 추가 배송비")
	private int prevAddPaymentShippingPrice;

	@ApiModelProperty(value = "추가결제배송비 건 수")
	private int addPaymentCnt;

	@ApiModelProperty(value = "직접결제여부")
	private String directPaymentYn;

	@ApiModelProperty(value = "환불수단 (D: 원결제 내역 C : 무통장입금)")
	private String refundType;

	@ApiModelProperty(value = "환불금액합계 = 총 환불 예정 금액")
	private int refundTotalPrice;

	@ApiModelProperty(value = "환불신청금액")
	private int refundRegPrice;

	@ApiModelProperty(value = "과세금액")
	private int taxablePrice;

	@ApiModelProperty(value = "비과세금액")
	private int nonTaxablePrice;

	@ApiModelProperty(value = "취소 수량 체크 성공여부")
	private String claimCntCheckYn;

	@ApiModelProperty(value = "상태 값 체크 성공여부")
	private String claimStatusCheckYn;

	@ApiModelProperty(value = "현재 상태 값 체크 성공여부")
	private String claimStatusDiffYn;

	@ApiModelProperty(value = "클레임 상세 등록 수")
	private int claimDetlCnt;

	@ApiModelProperty(value = "클레임 귀책구분")
	private String targetTp;

	@ApiModelProperty(value = "클레임 회수여부")
	private String returnsYn;

	@ApiModelProperty(value ="추가결제구분")
	private String addPaymentTp;

	@ApiModelProperty(value = "추가결제리스트")
	private List<OrderClaimAddPaymentShippingPriceDto> addPaymentList;

	@ApiModelProperty(value = "배송비 쿠폰 재발급 리스트")
	private List<OrderClaimCouponInfoDto> deliveryCouponList;

	@ApiModelProperty(value = "상품 별 클레임 금액정보 리스트")
	private List<OrderClaimGoodsPriceInfoDto> goodsPriceList;

	@ApiModelProperty(value = "과세배송비")
	private int taxableOrderShippingPrice;

	/** MALL 관련 필드 추가 START */
	@ApiModelProperty(value = "환불신청금액 (환불예정상품금액 + 주문시부과된 배송비)")
	private int refundReqPrice;

	@ApiModelProperty(value = "환불예정상품금액 (할인금액 제외)")
	private int refundGoodsPrice;

	@ApiModelProperty(value = "주문시 부과된 배송비")
	private int orderShippingPrice;

	@ApiModelProperty(value = "환불시 추가 배송비")
	private int refundAddShippingPrice;

	@ApiModelProperty(value = "총 환불 예정금액")
	private int totalRefundPrice;

	@ApiModelProperty(value = "결제수단 환불 금액")
	private int paymentRefundPrice;

	@ApiModelProperty(value = "적립금 환불 금액")
	private int pointRefundPrice;

	@ApiModelProperty(value = "배송비 추가 결제 여부")
	private String addPaymentShippingPriceYn;
	/** MALL 관련 필드 추가 END */
}
