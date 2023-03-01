package kr.co.pulmuone.v1.pg.service.inicis.dto;

import kr.co.pulmuone.v1.comm.util.DeviceUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InicisNonAuthenticationCartPayRequestDto {

	// 아이피
	private String ip = DeviceUtil.getServerIp();

	// 주문번호
	private String odid;

	// 상품명
	private String goodsName;

	// 결제 금액
	private int paymentPrice;

	// 결제 과세 금액
	private int taxPaymentPrice;

	// 결제 비과세 금액
	private int taxFreePaymentPrice;

	// 구매자명
	private String buyerName;

	// 구매자 이메일
	private String buyerEmail;

	// 구매자 연락처
	private String buyerMobile;

	// 할부기간(일시불 : 00 / 그 외 : 02, 03 ....)
	private String quota;

	// 카드번호
	private String cardNumber;

	// 카드유효기간(YYMM)
	private String cardExpire;

	// 생년월일(YYMMDD)/사업자번호
	private String regNo;

	// 카드비밀번호 앞 2자리
	private String cardPw;
}
