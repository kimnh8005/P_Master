package kr.co.pulmuone.v1.order.regular.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송 결제 카드 정보 Response Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 02. 17.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "정기배송 결제 카드 정보 Response Dto")
public class RegularReqPaymentCardResponseDto {

	@ApiModelProperty(value = "정기배송카드정보PK")
	private long odRegularPaymentKeyId;

	@ApiModelProperty(value = "정기결제카드 등록 여부")
	private String isExistPaymentKey;

	@ApiModelProperty(value = "카드사명")
	private String cardNm;

	@ApiModelProperty(value = "카드마스킹정보")
	private String cardMaskNumber;

	@ApiModelProperty(value = "결제가능여부")
	private String paymentYn;

	@ApiModelProperty(value = "결제불가사유")
	private String noPaymentReason;

	@ApiModelProperty(value = "정기배송주문건존재 여부")
	private String orderRegularYn;

	@ApiModelProperty(value = "다음회차결제예정일")
	private String paymentDt;
}
