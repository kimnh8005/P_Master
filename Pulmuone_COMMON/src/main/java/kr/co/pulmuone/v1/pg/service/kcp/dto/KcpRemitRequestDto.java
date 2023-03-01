package kr.co.pulmuone.v1.pg.service.kcp.dto;

import kr.co.pulmuone.v1.comm.util.DeviceUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KcpRemitRequestDto {

	// 결제금액
	private int paymentPrice;

	// 아이피
	private String ip = DeviceUtil.getServerIp();

	// 환불계좌번호
	private String refundBankNumber;

	// 환불계좌은행코드 - PG 은행 코드
	private String refundBankCode;

	// 보내는사람 예금주명
	private String sendName;
}
