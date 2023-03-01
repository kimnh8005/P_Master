package kr.co.pulmuone.v1.pg.service.kcp.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KcpApprovalResponseDto {

	// 결과 코드
	private String res_cd;

	// 결과 메세지
	private String res_msg;

	// 쇼핑몰 주문번호
	private String ordr_idxx;

	// KCP 거래 고유 번호
	private String tno;

	// KCP 실제 거래 금액
	private String amount;

	// 에스크로 결제 여부
	private String escw_yn;

	// 결제 포인트사 코드
	private String pnt_issue;

	// 쿠폰금액
	private String coupon_mny;

	// 카드사 코드
	private String card_cd;

	// 카드사 명
	private String card_name;

	// 카드사 명
	private String card_no;

	// 승인시간
	private String app_time;

	// 승인번호
	private String app_no;

	// 무이자 여부
	private String noinf;

	// 할부 개월 수
	private String quota;

	// 부분취소 가능유무
	private String partcanc_yn;

	// 카드구분1
	private String card_bin_type_01;

	// 카드구분2
	private String card_bin_type_02;

	// 카드결제금액
	private String card_mny;

	// 적립금액 or 사용금액
	private String pnt_amount;

	// 승인시간
	private String pnt_app_time;

	// 승인번호
	private String pnt_app_no;

	// 발생 포인트
	private String add_pnt;

	// 사용가능 포인트
	private String use_pnt;

	// 총 누적 포인트
	private String rsv_pnt;

	// 복합결제시 총 거래금액
	private String total_amount;

	// 계좌이체 은행명
	private String bank_name;

	// 계좌이체 은행코드
	private String bank_code;

	// 계좌이체 결제금액
	private String bk_mny;

	// 가솅계좌 입금할 은행 이름
	private String bankname;

	// 가솅계좌 입금할 계좌 예금주
	private String depositor;

	// 가솅계좌 입금할 계좌 번호
	private String account;

	// 가상계좌 입금마감시간
	private String va_date;

	// 현금 영수증 발행 구분
	private String cash_tr_code;

	// 현금영수증 승인번호
	private String cash_authno;

	// 현금영수증 거래번호
	private String cash_no;

	// 승인단계에서 callback 받을 기타 정보
	private String etcData;
}
