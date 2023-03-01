package kr.co.pulmuone.v1.pg.service.kcp.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KcpRemitResponseDto {

	// 성공여부
	private boolean success;

	// 메세지
	private String message;

	// 결과 코드
	private String res_cd;

	// 처리일자
	private String app_time;

	// 거래일자
	private String trade_date;

	// 거래일련번호
	private String trade_seq;

	// 남은금액
	private String bal_amount;
}
