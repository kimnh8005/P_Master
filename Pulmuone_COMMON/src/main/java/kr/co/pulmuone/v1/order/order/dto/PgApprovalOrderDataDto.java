package kr.co.pulmuone.v1.order.order.dto;

import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PgApprovalOrderDataDto {

	// 주문 pk
	private Long odOrderId;

	// 주문번호
	private String odid;

	// 회원 pk
	private Long urUserId;

	// 결제 방법
	private String originOrderPaymentType;

	// 결제 방법
	private String orderPaymentType;

	// 주문 결제 마스터 pk
	private Long odPaymentMasterId;

	// 주문 결제 상태
	private String status;

	// 결제금액
	private int paymentPrice;

	// 결제금액 (과세)
	private int taxablePrice;

	// 결제금액 (비과세)
	private int nonTaxablePrice;

	// 포인트 사용금액
	private int pointPrice;

	// 쿠폰 사용 리스트
	private List<Long> pmCouponIssueIds;

	// 선물하기 주문 여부
	private String presentYn;

	// 선물하기 ID (암호화 주문 ID)
	private String presentId;

	public String getUrlEncodPresentId() {
		return URLEncoder.encode(presentId);
	}

	// 선물 받는사람명
	private String presentReceiveNm;

	// 선물 받는 핸드폰명
	private String presentReceiveHp;

	// 선물 인증 번호
	private String presentAuthCd;

	// 선물하기 만료일
	private LocalDateTime presentExpirationDt;

	public String getFormatPresentExpirationDate(String formatter) {
		return presentExpirationDt.format(DateTimeFormatter.ofPattern(formatter));
	}

	// 링크프라이스 lpinfo
	private String lpinfo;

	// 링크프라이스 userAgent
	private String userAgent;

	// 링크프라이스 Client IP Address
	private String ip;

	// 링크프라이스 Device Type
	private String deviceType;

	// 정상주문여부
	private String orderYn;
}