package kr.co.pulmuone.v1.order.claim.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * Mall 주문 클레임 추가결제 정보 Response Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 05. 14.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "Mall 주문 클레임 추가결제 정보 Response Dto")
public class MallOrderClaimAddPaymentRequest {

	@ApiModelProperty(value = "주문PK")
	private long odOrderId;

	@ApiModelProperty(value = "주문클레임PK")
	private long odClaimId;

	@ApiModelProperty(value = "결제정보 PK")
	private String psPayCd;

	@ApiModelProperty(value = "카드 정보 PK")
	private String cardCode;

	@ApiModelProperty(value = "할부 기간")
	private String installmentPeriod;

	@ApiModelProperty(value = "현재 결제 요청 URL")
	private String orderInputUrl;

	@ApiModelProperty(value = "로그인ID")
	private String loginId;

	@ApiModelProperty(value = "MALL 회원 ID : UR_USER.UR_USER_ID")
	private String customUrUserId;

	@ApiModelProperty(value = "회원 ID : UR_USER.UR_USER_ID")
	private String urUserId;

	@ApiModelProperty(value = "비회원 CI")
	private String guestCi;
}
