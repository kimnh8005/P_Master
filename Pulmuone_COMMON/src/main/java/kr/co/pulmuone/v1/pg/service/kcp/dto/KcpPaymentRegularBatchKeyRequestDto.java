package kr.co.pulmuone.v1.pg.service.kcp.dto;

import kr.co.pulmuone.v1.comm.util.DeviceUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KcpPaymentRegularBatchKeyRequestDto {

	// 결제금액
	private int paymentPrice;

	private int taxPaymentPrice;

	private int taxFreePaymentPrice;

	// 아이피
	private String ip = DeviceUtil.getServerIp();

	private String odid;

	private String goodsName;

	private String buyerName;

	private String buyerEmail;

	private String buyerMobile;

	private String batchKey;
}
