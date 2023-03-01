package kr.co.pulmuone.v1.shopping.cart.dto;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "결제 요청 DTO")
public class ApplyPaymentRequestDto {

	@ApiModelProperty(value = "장바구니 타입", required = true)
	private String cartType;

	@ApiModelProperty(value = "장바구니 PK", required = true)
	private List<Long> spCartId;

	@ApiModelProperty(value = "임직원 구매 여부")
	private String employeeYn;

	@ApiModelProperty(value = "배송 스케쥴 변경 정보 리스트")
	private List<ChangeArrivalScheduledDto> arrivalScheduled;

	@ApiModelProperty(value = "상품 쿠폰 사용 정보")
	private List<UseGoodsCouponDto> useGoodsCoupon;

	@ApiModelProperty(value = "배송 쿠폰 사용 정보")
	private List<UseShippingCouponDto> useShippingCoupon;

	@ApiModelProperty(value = "장바구니 쿠폰 사용 발급 PK")
	private Long useCartPmCouponIssueId;

	@ApiModelProperty(value = "사용 적립금")
	private int usePoint;

	@ApiModelProperty(value = "결제 정보 PK", required = true)
	private String psPayCd;

	@ApiModelProperty(value = "카드 정보 PK")
	private String cardCode;

	@ApiModelProperty(value = "할부 기간")
	private String installmentPeriod;

	@ApiModelProperty(value = "환불 계좌 은행코드")
	private String bankCode;

	@ApiModelProperty(value = "환불 계좌 계좌번호")
	private String accountNumber;

	@ApiModelProperty(value = "환불 계좌 예금주")
	private String holderName;

	@ApiModelProperty(value = "내가 선택한 결제수한 다음에도 사용 여부")
	private String savePaymentMethodYn;

	@ApiModelProperty(value = "구매자명 - 렌탈,회원 일때만")
	private String buyerName;

	@ApiModelProperty(value = "구매자 휴대 전화 - 렌탈,회원 일때만")
	private String buyerMobile;

	@ApiModelProperty(value = "비회원 이메일")
	private String buyerMail;

	@ApiModelProperty(value = "정기배송 배송주기")
	private String regularShippingCycleType;

	@ApiModelProperty(value = "정기배송 배송기간")
	private String regularShippingCycleTermType;

	@ApiModelProperty(value = "정기배송 시작일자")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate regularShippingArrivalScheduledDate;

	@ApiModelProperty(value = "증정품 행사PK,상품PK 리스트")
	private List<CartGiftDto> gift;

	@ApiModelProperty(value = "상품정보 리스트")
	private List<ArrivalGoodsDto> arrivalGoods;

	@ApiModelProperty(value = "현재 결제 요청 URL")
	private String orderInputUrl;

	@ApiModelProperty(value = "매장 배송 유형")
	private String storeDeliveryType;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@ApiModelProperty(value = "매장 도착예정일")
	private LocalDate storeArrivalScheduledDate;

	@ApiModelProperty(value = "매장배송 회차 PK")
	private Long urStoreScheduleId;

	@ApiModelProperty(value = "선물하기여부")
	private String presentYn;

	@ApiModelProperty(value = "받는분 - 선물하기, 무형")
	private String receiveName;

	@ApiModelProperty(value = "받는사람 휴대전화 - 선물하기, 무형")
	private String receiveMobile;

	@ApiModelProperty(value = "선물카드타입")
	private String presentCardType;

	@ApiModelProperty(value = "선물카드내용")
	private String presentCardMessage;
}
