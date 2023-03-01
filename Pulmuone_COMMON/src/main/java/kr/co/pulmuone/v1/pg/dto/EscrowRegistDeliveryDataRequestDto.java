package kr.co.pulmuone.v1.pg.dto;

import java.time.LocalDateTime;

import kr.co.pulmuone.v1.comm.util.DeviceUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EscrowRegistDeliveryDataRequestDto {

	// 거래번호
	private String tid;

	// 주문번호
	private String odid;

	// 결제금액
	private int paymentPrice;

	// 송장번호
	private String trackingNo;

	// 배송등록자
	private String registTrakingNoUserName;

	// 택배사명
	private String shippingCompanyName;

	// 이니시스 택배사 코드
	private String inicisSshippingCompanyCode;

	// 배송등록일자
	private LocalDateTime registTrackingNoDate;

	// 송신자 이름
	private String sendName;

	// 송신자 전화번호
	private String sendTel;

	// 송신자 우편번호
	private String sendZipCode;

	// 송신자 주소 1
	private String sendAddress1;

	// 수신자 이름
	private String receiverName;

	// 수신자 전화번호
	private String receiverMobile;

	// 수신자 우편번호
	private String receiverZipCode;

	// 수신자 주소 1
	private String receiverAddress1;

	// 아이피
	private String ip = DeviceUtil.getServerIp();
}
