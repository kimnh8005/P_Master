package kr.co.pulmuone.v1.pg.service.kcp.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KcpVirtualAccountReturnRequestDto {
	private String site_cd; // 사이트 코드
	private String tno; // KCP 거래번호
	private String order_no; // 주문번호
	private String tx_cd; // 업무처리 구분 코드
	private String tx_tm; // 업무처리 완료 시간
	private String ipgm_name; // 주문자명
	private String remitter; // 입금자명
	private String ipgm_mnyx; // 입금 금액
	private String totl_mnyx; // 해당 계좌에 입금 된 금액의 합계
	private String ipgm_time; // 가상계좌에 입금된 시간
	private String bank_code; // 은행코드
	private String account; // 가상계좌 입금계좌번호
	private String op_cd; // 처리구분 코드
	private String noti_id; // 통보 아이디
	private String cash_a_no; // 현금영수증 승인번호
	private String cash_a_dt; // 현금영수증 승인시간
	private String cash_no; // 현금영수증 거래번호
}
