package kr.co.pulmuone.v1.shopping.cart.dto;

import java.util.HashMap;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.policy.clause.dto.vo.GetLatestJoinClauseListResultVo;
import kr.co.pulmuone.v1.store.delivery.dto.ShippingAddressPossibleDeliveryInfoDto;
import kr.co.pulmuone.v1.store.shop.dto.vo.ShopVo;
import kr.co.pulmuone.v1.user.buyer.dto.vo.CodeInfoVo;
import kr.co.pulmuone.v1.user.buyer.dto.vo.CommonGetRefundBankResultVo;
import kr.co.pulmuone.v1.user.certification.dto.GetSessionShippingResponseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "주문서 정보 조회 응답 DTO")
public class GetOrderPageInfoResponseDto{

	@ApiModelProperty(value = "배송지 정보")
	private GetSessionShippingResponseDto shippingAddress;

	@ApiModelProperty(value = "배송가능 아이콘 정보")
	private ShippingAddressPossibleDeliveryInfoDto deliveryInfo;

	@ApiModelProperty(value = "장바구니 정보")
	private List<CartDeliveryDto> cart;

	@ApiModelProperty(value = "장바구니 집계 정보")
	private CartSummaryDto cartSummary;

	@ApiModelProperty(value = "결제방법 정보 리스트")
	private List<HashMap<String,String>> paymentType;

	@ApiModelProperty(value = "카드 정보 리스트")
	private List<HashMap<String,String>> cardList;

	@ApiModelProperty(value = "할부기간")
	private List<HashMap<String,String>> installmentPeriod;

	@ApiModelProperty(value = "신용카드 혜택")
	private List<HashMap<String,String>> cartBenefit;

	@ApiModelProperty(value = "환불 계좌 정보")
	private CommonGetRefundBankResultVo refundBank;

	@ApiModelProperty(value = "사용가능 할인정보")
	private HashMap<String,Object> discountInfo;

	@ApiModelProperty(value = "약관 정보 리스트")
	private List<GetLatestJoinClauseListResultVo> clause;

	@ApiModelProperty(value = "정기결제 카드 정보")
	private HashMap regularPayment;

	@ApiModelProperty(value = "무통장 입금 결제 가능 여부")
	private String virtualAccountYn;

	@ApiModelProperty(value = "회원 사용 결제&카드 정보")
	private HashMap<String,String> userPayment;

	@ApiModelProperty(value = "환불계좌 은행정보 리스트")
	private List<CodeInfoVo> refundBankList;

	@ApiModelProperty(value = "매장 정보")
	private ShopVo store;

	@ApiModelProperty(value = "정기배송 추가 주문 여부")
	private boolean isAdditionalOrder;
}
