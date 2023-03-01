package kr.co.pulmuone.v1.pg.service.kcp.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KcpApplyRegularBatchKeyResponseDto {

	// 결과 코드
	private String res_cd;

	// 결과 메세지
	private String res_msg;

	// 카드 코드
	private String card_cd;

	// 카드명
	private String card_name;

	// 배치 인증키
	private String batch_key;

	// 추가 옵션 1
	private String param_opt_1;

	// 추가 옵션 2
	private String param_opt_2;

	// 추가 옵션 3
	private String param_opt_3;

	// 카드 마스킹 정보
	private String card_mask_no;
}
