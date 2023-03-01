package kr.co.pulmuone.v1.order.order.dto.vo;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 선물하기 VO
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 07. 15.            홍진영         최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Getter
@Setter
@ToString
@ApiModel(description = "주문 선물하기 VO")
public class OrderPresentVo {
	@ApiModelProperty(value = "주문 PK")
	private long odOrderId;

	@ApiModelProperty(value = "선물하기 ID (암호화 주문 ID)")
	private String presentId;

	@ApiModelProperty(value = "선물 받는사람명")
	private String presentReceiveNm;

	@ApiModelProperty(value = "선물 받는 핸드폰명")
	private String presentReceiveHp;

	@ApiModelProperty(value = "선물카드타입")
	private String presentCardType;

	@ApiModelProperty(value = "선물카드내용")
	private String presentCardMsg;

	@ApiModelProperty(value = "선물 인증 번호")
	private String presentAuthCd;

	@ApiModelProperty(value = "선물하기 만료일")
	private LocalDateTime presentExpirationDt;

	@ApiModelProperty(value = "선물하기상태 - 공통코드 (PRESENT_ORDER_STATUS.WAIT:대기, REJECT:거절, EXPIRED:유효기간만료, RECEIVE_COMPLET:받기완료)")
	private String presentOrderStatus;

	@ApiModelProperty(value = "선물 받은 일자(주소입력완료일자)")
	private LocalDateTime presentReceiveDt;

	@ApiModelProperty(value = "메시지 재발송 횟수")
	private int presentMsgSendCnt;
}
