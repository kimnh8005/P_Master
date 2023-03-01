package kr.co.pulmuone.v1.order.regular.dto.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderRegularPaymentKeyVo {
	/**
	 * 정기결제 배치 키 PK
	 */
	private Long odRegularPaymentKeyId;

	/**
	 * 회원 PK
	 */
	private Long urUserId;

	/**
	 * KCP 배치키
	 */
	private String batchKey;

	/**
	 * 카드사명
	 */
	private String cardName;

	/**
	 * 카드 마스킹 정보
	 */
	private String cardMaskNumber;
}
