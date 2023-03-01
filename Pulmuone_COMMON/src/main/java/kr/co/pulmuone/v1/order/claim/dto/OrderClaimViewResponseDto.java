package kr.co.pulmuone.v1.order.claim.dto;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.user.buyer.dto.vo.CodeInfoVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임 정보 조회 Response Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 15.   강상국         최초작성
 * =======================================================================
 * </PRE>
 */

@Builder
@Getter
@Setter
@ToString
@ApiModel(description = "주문 클레임 정보 조회 결과 Dto")
public class OrderClaimViewResponseDto {
	@ApiModelProperty(value = "주문진행상태")
	private String orderStatusCd;

	@ApiModelProperty(value = "주문진행상태명")
	private String orderStatusNm;

	@ApiModelProperty(value = "클레임진행상태목록")
	private List<OrderClaimStatusInfoDto> claimStatusList;

	@ApiModelProperty(value = "고객 사유조회 결과")
	private OrderClaimCustomerReasonResponseDto customerReasonInfo;

	@ApiModelProperty(value = "주문마스터 정보 조회 결과")
	private OrderMasterInfoDto orderMasterInfo;

	@ApiModelProperty(value = "상품정보 조회 결과")
	private List<OrderClaimGoodsInfoDto> orderGoodList;

	@ApiModelProperty(value = "신청 상품 정보")
	private List<OrderClaimGoodsInfoDto> changeGoodsList;

	@ApiModelProperty(value = "상품정보 조회 결과")
	private List<OrderClaimGoodsListDto> goodList;

	@ApiModelProperty(value = "미출정보 조회 결과")
	private List<OrderClaimGoodsInfoDto> undeliveredList;

	@ApiModelProperty(value = "증정품 정보 조회 결과")
	private List<OrderClaimGoodsInfoDto> giftList;

	@ApiModelProperty(value = "결제정보 조회 결과")
	private OrderClaimPaymentInfoDto paymentInfo;

	@ApiModelProperty(value = "환불정보에서 상품금액 조회 결과")
	private OrderClaimPriceInfoDto priceInfo;

	@ApiModelProperty(value = "환불정보에서 상품쿠폰금액 조회 결과")
	private List<OrderClaimCouponInfoDto> goodsCouponList;

	@ApiModelProperty(value = "환불정보에서 장바구니쿠폰금액 조회 결과")
	private List<OrderClaimCouponInfoDto> cartCouponList;

	@ApiModelProperty(value = "주문클레임 환불계좌 조회 결과")
	private OrderClaimAccountInfoDto accountInfo;

	@ApiModelProperty(value = "주문클레임 첨부파일 조회 결과")
	private List<OrderClaimAttcInfoDto> attcInfoList;

	@ApiModelProperty(value = "주문클레임 보내는 배송지 조회 결과")
	private OrderClaimSendShippingInfoDto sendShippingInfo;

	@ApiModelProperty(value = "주문클레임 받는 배송지 조회 결과")
	private List<OrderClaimShippingInfoDto> receShippingInfoList;

	@ApiModelProperty(value = "주문클레임 상세 상태일자 조회 결과")
	private List<OrderClaimDetlDtInfoDto> detlDtInfoList;

	@ApiModelProperty(value = "주문클레임 마스터 조회 결과")
	private OrderClaimMasterInfoDto claimInfo;

	@ApiModelProperty(value = "출고정보 ")
	private List<OrderClaimViewDeliveryDto> deliveryInfoList;

	@ApiModelProperty(value = "환불 은행 조회 결과")
	private List<CodeInfoVo> refundBankList;

	@ApiModelProperty(value = "결제방법 정보 리스트")
	private List<HashMap<String,String>> paymentTypeList;

	@ApiModelProperty(value = "카드 정보 리스트")
	private List<HashMap<String,String>> cardList;

	@ApiModelProperty(value = "할부기간")
	private List<HashMap<String,String>> installmentPeriodList;

	@ApiModelProperty(value = "클레임 정보")
	private OrderClaimInfoDto odClaimInfo;
}
