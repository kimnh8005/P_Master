package kr.co.pulmuone.v1.pg.service.inicis.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InicisMobileApprovalRequestDto {
	// "0000": 정상, 이외 실패
	private String P_STATUS;

	// 결과메시지 (성공시 : 성공, 실패시 : 기타 오류 메시지)
	private String P_RMESG1;

	// 인증거래번호(성공시에만 전달)
	private String P_TID;

	// 승인요청 URL(거래마다 상이)
	private String P_REQ_URL;

	// 가맹점정보필드
	private String P_NOTI;

	// 거래금액
	private String P_AMT;
}
