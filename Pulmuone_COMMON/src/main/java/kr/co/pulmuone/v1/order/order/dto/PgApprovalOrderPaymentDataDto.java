package kr.co.pulmuone.v1.order.order.dto;

import java.time.LocalDateTime;

import kr.co.pulmuone.v1.comm.enums.PgEnums;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PgApprovalOrderPaymentDataDto {

	// 주문번호
	private PgEnums.PgAccountType pgAccountType;

	// 거래번호
	private String tid;

	// 승인번호
	private String authCode;

	// 카드번호
	private String cardNumber;

	// 카드 무이자 구분 (Y: 무이자, N: 일반)
	private String cardQuotaInterest;

	// 카드 할부기간
	private String cardQuota;

	// 가상계좌번호
	private String virtualAccountNumber;

	// 은행명
	private String bankName;

	// 결제정보
	private String info;

	// 입금 기한
	private LocalDateTime paidDueDate;

	// 입금자명
	private String paidHolder;

	// 부분취소 가능여부
	private String partCancelYn;

	// 에스크로 결제 여부
	private String escrowYn;

	// 승인 일자
	private LocalDateTime approvalDate;

	// 현금 영수증 발행 여부
	private String cashReceiptYn;

	// 현금 영수증 신청번호
	private String cashReceiptNo;

	// 현금 영수증 승인번호
	private String cashReceiptAuthNo;

	// 현금 영수증 거래구분
	private String cashReceiptType;

	// PG 응답 데이터
	private String responseData;
}