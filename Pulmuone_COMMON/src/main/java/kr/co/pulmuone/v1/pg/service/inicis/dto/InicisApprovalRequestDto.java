package kr.co.pulmuone.v1.pg.service.inicis.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InicisApprovalRequestDto {
	// "0000": 정상, 이외 실패
	private String resultCode;

	// 결과메시지 (성공시 : 성공, 실패시 : 기타 오류 메시지)
	private String resultMsg;

	// 가맹점 ID
	private String mid;

	// 가맹점 주문번호
	private String orderNumber;

	// 승인요청 검증 토큰
	private String authToken;

	// 승인요청 Url (해당 URL로 HTTPS API Request 승인요청 - POST)
	private String authUrl;

	// 망취소요청 Url (승인요청 후 인증결과 수신 실패 / DB저장 실패시)
	private String netCancelUrl;

	// 인증결과 인코딩 (default : UTF-8)
	private String charset;

	// 가맹점 데이터
	private String merchantData;

	// 결제 금액 검증
	private int paymentPrice;
}
