package kr.co.pulmuone.v1.pg.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReceiptIssueResponseDto {

	// 성공여부
	private boolean success;

	// 메세지
	private String message;

	// 현금영수증발급 거래번호 TID
	private String tid;

	// 현금영수증 승인번호
	private String receiptNo;

	// 승인/발급 시간
	private String authDateTime;
}
