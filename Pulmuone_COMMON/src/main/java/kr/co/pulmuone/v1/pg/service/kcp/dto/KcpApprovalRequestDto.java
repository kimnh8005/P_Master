package kr.co.pulmuone.v1.pg.service.kcp.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KcpApprovalRequestDto {

	// 추가결제 승인 여부
	private boolean addApproval;

	private String res_cd;

	private String res_msg;

	// 요청 종류
	private String req_tx;

	// 처리 종류
	private String tran_cd;

	// 쇼핑몰 주문번호
	private String ordr_idxx;

	// 상품명
	private String good_name;

	// KCP 거래 고유 번호
	private String tno;

	// 주문자명
	private String buyr_name;

	// 주문자 전화번호
	private String buyr_tel1;

	// 주문자 핸드폰 번호
	private String buyr_tel2;

	// 주문자 E-mail 주소
	private String buyr_mail;

	// 결제 방법
	private String use_pay_method;

	// 가맹점 고객 아이디
	private String shop_user_id;

	// 현금 영수증 등록 여부
	private String cash_yn;

	// 현금 영수증 발행 구분
	private String cash_tr_code;

	// 현금 영수증 등록 번호
	private String cash_id_info;

	private String enc_data;

	private String enc_info;

	// 결제금액
	private int paymentPrice;

	// 업체 추가 파라미터
	private String param_opt_1;

	// 업체 추가 파라미터
	private String param_opt_2;

	// 업체 추가 파라미터
	private String param_opt_3;

	// 간편결제유형
	private String card_other_pay_type;
}
