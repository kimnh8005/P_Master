package kr.co.pulmuone.v1.pg.dto;

import kr.co.pulmuone.v1.comm.enums.OrderEnums.PaymentType;
import kr.co.pulmuone.v1.comm.util.DeviceUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CancelRequestDto {

	// 부분취소 여부
	private boolean partial = false;

	// 거래번호
	private String tid;

	// 아이피
	private String ip = DeviceUtil.getServerIp();

	// 취소 지불 수단
	private PaymentType paymentType;

	// 취소사유
	private String cancelMessage;

	// 주문번호
	private String odid;

	// 취소 금액
	private int cancelPrice;

	// 취소 과세 금액
	private int taxCancelPrice;

	// 취소 비과세 금액
	private int taxFreecancelPrice;

	// 취소후 남은 금액 (부분취소시 필수)
	private int expectedRestPrice;

	private boolean refundBank = true;

	// 환불계좌번호 (가상계좌 환불 필수)
	private String refundBankNumber;

	// 환불계좌은행코드 - PG 은행 코드 (가상계좌 환불 필수)
	private String refundBankCode;

	// 환불계좌 예금주명 (가상계좌 환불 필수)
	private String refundBankName;

	// 에스크로결제 여부
	private String escrowYn;
}
