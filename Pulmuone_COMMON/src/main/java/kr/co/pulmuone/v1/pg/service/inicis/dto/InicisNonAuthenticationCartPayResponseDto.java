package kr.co.pulmuone.v1.pg.service.inicis.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class InicisNonAuthenticationCartPayResponseDto {

	// 성공여부
	private boolean success;

	// 메세지
	private String message;

	// 거래 번호
	private String tid;

	// 결제일자
	private String payDate;

	// 결제시간
	private String payTime;

	// 승인번호
	private String payAuthCode;

	// 카드코드
	private String cardCode;

	// 카드종류구분
	private String checkFlg;

	// 할부기간
	private String payAuthQuota;

	// 결제금액
	private String price;

	// 부분환불 가능여부(1 - 가능 / 0 - 불가능)
	private String prtcCode;
}
