package kr.co.pulmuone.v1.order.order.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class putVirtualBankOrderPaymentDataDto {

	// 승인번호
	private String authCode;

	// 승인 일자
	private LocalDateTime approvalDate;

	// 현금 영수증 발행 여부
	private String cashReceiptYn;

	// 현금 영수증 신청번호
	private String cashReceiptNo;

	// 현금 영수증 승인번호
	private String cashReceiptAuthNo;

	// 현금 영수증 발급 구분
	private String cashReceiptType;

	// PG 응답 데이터
	private String responseData;
}