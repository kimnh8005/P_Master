package kr.co.pulmuone.v1.pg.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class VirtualAccountDataResponseDto {

	// 성공여부
	private boolean success;

	// 메세지
	private String message;

	// 거래번호
	private String tid;

	// 입금 금액
	private String paymentPrice;

	// 은행명
	private String bankName;

	// 은행코드
	private String bankCode;

	// 예금주명
	private String depositor;

	// 가상계좌번호
	private String account;

	// 승인일자
	private String authDate;

	// 입금예정일자
	private String validDate;
}
