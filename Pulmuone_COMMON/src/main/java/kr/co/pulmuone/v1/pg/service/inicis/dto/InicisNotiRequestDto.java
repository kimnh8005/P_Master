package kr.co.pulmuone.v1.pg.service.inicis.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InicisNotiRequestDto {

	// 거래상태 02:가상계좌입금통보 (00:가상계좌 채번)
	private String P_STATUS;

	// 거래번호
	private String P_TID;

	// 지불수단 VBANK(가상계좌)
	private String P_TYPE;

	// 승인일자 YYMMDDhhmmss
	private String P_AUTH_DT;

	// 상점아이디
	private String P_MID;

	// 상점주문번호
	private String P_OID;

	// 은행코드
	private String P_FN_CD1;

	// 금융사코드 빈값전달
	private String P_FN_CD2;

	// 은행명
	private String P_FN_NM;

	// 거래금액
	private String P_AMT;

	// 주문자명
	private String P_UNAME;

	// 메시지1 채번된 가상계좌번호|입금기한 예)P_VACCT_NO=01440064018781|P_EXP_DT=20100325
	private String P_RMESG1;

	// 메시지2 빈값전달
	private String P_RMESG2;

	// 주문정보 거래요청시 입력한 P_NOTI 값을 그대로 반환합니다.
	private String P_NOTI;

	// 승인번호 (빈값전달)
	private String P_AUTH_NO;

	// 현금영수증 거래 금액
	private String P_CSHR_AMT;

	// 현금영수증 공급가액
	private String P_CSHR_SUP_AMT;

	// 현금영수증 부가가치세
	private String P_CSHR_TAX;

	// 현금영수증 봉사료
	private String P_CSHR_SRVC_AMT;

	// 현금영수증 거래자 구분 ["0": 소득공제용, "1": 지출증빙용]
	private String P_CSHR_TYPE;

	// 현금영수증 발행일자 YYYYMMDDhhmmss
	private String P_CSHR_DT;

	// 현금영수증 발행승인번호
	private String P_CSHR_AUTH_NO;
}
