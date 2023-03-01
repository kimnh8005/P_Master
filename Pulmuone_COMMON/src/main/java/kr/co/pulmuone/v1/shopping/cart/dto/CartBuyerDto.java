package kr.co.pulmuone.v1.shopping.cart.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.enums.OrderEnums.PaymentType;
import kr.co.pulmuone.v1.comm.enums.SystemEnums.AgentType;
import kr.co.pulmuone.v1.comm.enums.UserEnums.BuyerType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "주문자 정보")
public class CartBuyerDto {

	/**
	 * 구매자 회원 pk
	 */
	private long urUserId;

	/**
	 * 회원그룹 pk
	 */
	private long urGroupId;

	/**
	 * 임직원사번
	 */
	private String urEmployeeCd;

	/**
	 * 비회원 CI
	 */
	private String guestCi;

	/**
	 * 주문자 명
	 */
	private String buyerName;

	/**
	 * 주문자 핸드폰
	 */
	private String buyerMobile;

	/**
	 * 주문자 이메일
	 */
	private String buyerEmail;

	/**
	 * 결제 수단
	 */
	private PaymentType paymentType;

	/**
	 * 구매자 타입
	 */
	private BuyerType buyerType;

	/**
	 * 구매자 타입
	 */
	private AgentType agentType;

	/**
	 * 사용자환경정보
	 */
	private String urPcidCd;

	/**
	 * 환불 계좌 은행코드
	 */
	private String bankCode;

	/**
	 * 환불 계좌 계좌번호
	 */
	private String accountNumber;

	/**
	 * 환불 계좌 예금주
	 */
	private String holderName;

	/**
	 * 주문생성여부
	 */
	private String orderCreateYn;

	/**
	 * 선물하기여부
	 */
	private String presentYn;

	/**
	 * 받는분 - 선물하기, 무형
	 */
	private String receiveName;

	/**
	 * 받는사람 휴대전화 - 선물하기, 무형
	 */
	private String receiveMobile;

	/**
	 * 선물카드타입
	 */
	private String presentCardType;

	/**
	 * 선물카드내용
	 */
	private String presentCardMessage;
}