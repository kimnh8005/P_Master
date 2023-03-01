package kr.co.pulmuone.v1.pg.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReceiptCancelResponseDto {

	// 성공여부
	private boolean success;

	// 메세지
	private String message;
}
