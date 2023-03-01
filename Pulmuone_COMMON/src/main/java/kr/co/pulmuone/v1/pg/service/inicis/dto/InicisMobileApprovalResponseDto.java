package kr.co.pulmuone.v1.pg.service.inicis.dto;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InicisMobileApprovalResponseDto {

	// 거래상태 "00":성공, 이외 : 실패
	private String P_STATUS;
	// 지불결과메시지
	private String P_RMESG1;
	// 승인거래번호
	private String P_TID;
	// 지불수단
	private String P_TYPE;
	// 승인일자
	private String P_AUTH_DT;
	// 상점아이디
	private String P_MID;
	// 상점 주문번호
	private String P_OID;
	// 결제금액
	private String P_AMT;
	// 주문자명
	private String P_UNAME;
	// 가맹점명
	private String P_MNAME;

	// 가맹점 전달 NOTI_URL
	private String P_NOTEURL;
	// 가맹점 전달 NEXT_URL
	private String P_NEXT_URL;

	// 신용카드
	// 발급사 코드
	private String P_CARD_ISSUER_CODE;
	// 신용카드할부개월
	private String P_RMESG2;
	// 승인번호
	private String P_AUTH_NO;
	// 무이자 할부여부 [0:일반, 1:무이자]
	private String P_CARD_INTEREST;
	// 부분취소 가능여부 ["1":가능 , "0":불가능]
	private String P_CARD_PRTC_CODE;

	// 앱연동구분
	// 앱연동여부
	private String P_SRC_CODE;

	// 계좌이체 AND 신용카드
	// 은행코드 , 카드코드
	private String P_FN_CD1;
	// 결제은행 한글명 , 결제카드한글명
	private String P_FN_NM;
	// 카드번호
	private String P_CARD_NUM;


	// 가상계좌
	// 입금할 계좌번호
	private String P_VACT_NUM;
	// 입금마감일자 [YYYYMMDD]
	private String P_VACT_DATE;
	// 입금마감시간 [hhmmss]
	private String P_VACT_TIME;
	// 계좌주명
	private String P_VACT_NAME;
	// 은행코드
	private String P_VACT_BANK_CODE;

	// 현금 영수증
	// 결과코드 ["220000":정상, 그외 실패]
	private String P_CSHR_CODE;
	//용도구분 ["0":소득공제, "1":지출증빙]
	private String P_CSHR_TYPE;
	// 발행 승인번호
	private String P_CSHR_AUTH_NO;

	// 주문정보에 입력한 값 반환
	private String etcData;

	// 승인요청 URL(거래마다 상이)
	private String P_REQ_URL;

	// 망취소에 필요한 값
	private Map<String, String> netCancelAuthMap;
}
