package kr.co.pulmuone.v1.pg.service.kcp.dto;

import kr.co.pulmuone.v1.comm.util.DeviceUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KcpApplyRegularBatchKeyRequestDto {

	// 요청 종류
	private String req_tx;

	// 처리 종류
	private String tran_cd;

	// 아이피
	private String ip = DeviceUtil.getServerIp();

	// 결제 방법
	private String pay_method;

	// 쇼핑몰 주문번호
	private String ordr_idxx;

	// 상품명
	private String good_name;

	// 결제 총금액
	private String good_mny;

	// 주문자명
	private String buyr_name;

	private String enc_data;

	private String enc_info;

	// 추가 옵션 1
	private String param_opt_1;

	// 추가 옵션 2
	private String param_opt_2;

	// 추가 옵션 3
	private String param_opt_3;

	// 카드 마스킹 정보
	private String card_mask_no;

}
