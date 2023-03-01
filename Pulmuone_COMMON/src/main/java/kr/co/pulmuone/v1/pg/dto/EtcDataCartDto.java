package kr.co.pulmuone.v1.pg.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EtcDataCartDto {
	// 장바구니 타입
	private String cartType;

	// 장바구니 타입
	private String odid;

	// 장바구니 PK
	private List<Long> spCartId;

	// 현재 결제 요청 URL
	private String orderInputUrl;

	// 결제 정보 코드
	private String paymentType;

	// 링크프라이스 lpinfo
	private String lpinfo;

	// 링크프라이스 userAgent
	private String userAgent;

	// 링크프라이스 Client IP Address
	private String ip;

	// 링크프라이스 Device Type
	private String deviceType;
}