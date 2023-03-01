package kr.co.pulmuone.v1.order.present.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 선물하기 DTO
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
@Getter
@Setter
@ToString
@ApiModel(description = "주문 선물하기 DTO")
public class OrderPresentDto {

	@ApiModelProperty(value = "주문자명")
	private String buyerNm;

	@ApiModelProperty(value = "주문자 핸드폰 번호")
	private String buyerHp;

	@ApiModelProperty(value = "선물 만료일")
	@JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
	private LocalDateTime presentExpirationDt;

	@ApiModelProperty(value = "선물 받기상태")
	private String presentOrderStatus;

	@ApiModelProperty(value = "주문 PK")
	private Long odOrderId;

	@ApiModelProperty(value = "주문번호")
	private String odid;

	@ApiModelProperty(value = "선물카드타입")
	private String presentCardType;

	@ApiModelProperty(value = "선물카드내용")
	private String presentCardMsg;

	@ApiModelProperty(value = "주문자 PK")
	private Long buyerUrUserId;

	@ApiModelProperty(value = "선물 상품명")
	private String goodsNm;

	@ApiModelProperty(value = "결제금액 (환불금액)")
	private int paymentPrice;

	@ApiModelProperty(value = "선물 받는사람명")
	private String presentReceiveNm;
}