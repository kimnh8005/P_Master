package kr.co.pulmuone.v1.pg.service.inicis.dto;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InicisApprovalResponseDto {

	private String resultCode;

	private String resultMsg;

	// 거래 번호
	private String tid;

	// 결제방법(지불수단)
	private String payMethod;

	// 결제완료금액
	private String totPrice;

	// 주문 번호
	private String moid;

	// 승인날짜
	private String applDate;

	// 승인시간
	private String applTime;

	// "1":무이자할부
	private String CARD_Interest;

	// 카드 할부기간
	private String CARD_Quota;

	// 부분취소 가능여부 ["1":가능 , "0":불가능]
	private String CARD_PRTC_CODE;

	// 실시간 계좌이체, 가상계좌 은행코드
	private String bankCode;

	// 가상계좌 번호
	private String VACT_Num;

	// 가상계좌 예금주명
	private String VACT_Name;

	// 가상계좌 송금 일자
	private String VACT_Date;

	// 가상계좌 송금 시각
	private String VACT_Time;

	// 가상계좌 입금은행명
	private String vactBankName;

	// 카드번호
	private String cardNum;

	// 승인번호
	private String applNum;

	// 카드 종류
	private String cardCode;

	// 현금영수증 발행 정상여부 ["220000": 정상]
	private String CSHR_ResultCode;

	// 현금영수증구분 ["0":소득공제, "1":지출증빙]
	private String CSHR_Type;

	// 승인단계에서 callback 받을 기타 정보
	private String etcData;

	// 망취소요청 Url (승인요청 후 인증결과 수신 실패 / DB저장 실패시)
	private String netCancelUrl;

	// 승인단계에서 callback 받을 기타 정보
	private Map<String, String> authMap;
}
