package kr.co.pulmuone.v1.pg.dto;

import kr.co.pulmuone.v1.comm.enums.OrderEnums.CashReceipt;
import kr.co.pulmuone.v1.comm.util.DeviceUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReceiptIssueRequestDto {

	// 아이피
	private String ip = DeviceUtil.getServerIp();

	// 주문번호
	private String odid;

	// 상품명
	private String goodsName;

	// 발행용도 (소득공제용, 지출증빙)
	private CashReceipt receiptType;

	// 현금영수증 식별번호
	private String regNumber;

	// 결제금액
	private int totalPrice;

	// 공급가액
	private int supPrice;

	// 부가세
	private int tax;

	// 봉사료
	private int srcvPrice;

	// 구매자명
	private String buyerName;

	// 구매자 이메일
	private String buyerEmail;

	// 구매자 연락처
	private String buyerMobile;
}
