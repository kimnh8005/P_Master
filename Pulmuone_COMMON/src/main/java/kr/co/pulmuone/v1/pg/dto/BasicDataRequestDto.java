package kr.co.pulmuone.v1.pg.dto;

import kr.co.pulmuone.v1.comm.enums.OrderEnums.PaymentType;
import kr.co.pulmuone.v1.comm.util.DeviceUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasicDataRequestDto {

	private boolean isAddPay;

	private String odid;

	private PaymentType paymentType;

	// 카드 또는 은행 PG사 코드
	private String pgBankCode;

	// 할부
	private String quota;

	private String virtualAccountDateTime;

	private String goodsName;

	private int paymentPrice;

	private int taxPaymentPrice;

	private int taxFreePaymentPrice;

	private String buyerName;

	private String buyerEmail;

	private String buyerMobile;

	private String loginId;

	private String flgCash;	// 현금영수증 발행여부(0: 미발행, 1: 소득공제 발행, 2: 지출증빙)

	private String cashReceiptNumber;

	// 아이피
	private String ip = DeviceUtil.getServerIp();

	// 승인단계에서 callback 받을 기타 정보
	private String etcData;
}
