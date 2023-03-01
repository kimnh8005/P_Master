package kr.co.pulmuone.v1.pg.service.inicis.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InicisVirtualAccountReturnRequestDto {
	private String no_tid; // 이니시스 거래번호 (입금거래에 대한 TID)
	private String no_oid; // 상점 주문번호 (가상계좌 채번요청 시 주문번호)
	private String cd_bank; // 계좌를 발급한 은행 코드
	private String cd_deal; // 거래 취급 기관 코드 (실제입금은행)
	private String dt_trans; // 금융기관 발생 거래 일자
	private String tm_trans; // 금융기관 발생 거래 시각
	private String no_vacct; // 계좌번호
	private String amt_input; // 입금금액
	private String flg_close; // 마감구분 [0:당일마감전, 1:당일마감후]
	private String cl_close; // 마감구분코드 [0:당일마감전, 1:당일마감후]
	private String type_msg; // 거래구분 [0200:정상, 0400:취소]
	private String nm_inputbank; // 입금은행명
	private String nm_input; // 입금자명
	private String dt_inputstd; // 입금기준일자
	private String dt_calculstd; // 정산기준일자
	private String dt_transbase; // 거래기준일자
	private String cl_trans; // 거래구분코드 (1100)
	private String cl_kor; // 한글구분코드
	private String dt_cshr; // 현금영수증 발급일자
	private String tm_cshr; // 현금영수증 발급시간
	private String no_cshr_appl; // 현금영수증 발급번호
	private String no_cshr_tid; // 현금영수증 발급TID
}
