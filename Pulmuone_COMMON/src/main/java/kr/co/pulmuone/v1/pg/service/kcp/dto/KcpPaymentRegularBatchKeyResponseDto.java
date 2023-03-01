package kr.co.pulmuone.v1.pg.service.kcp.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KcpPaymentRegularBatchKeyResponseDto {

	// 결과 코드
	private String res_cd;

	// 결과 메세지
	private String res_msg;

	// KCP 거래 고유 번호
	private String tno;

	// 카드사 코드
	private String card_cd;

	// 카드사 명
	private String card_name;

	// 승인시간
	private String app_time;

	// 승인번호
	private String app_no;

	// 무이자 여부
	private String noinf;

	// 할부 개월 수
	private String quota;
}
