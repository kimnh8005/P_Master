package kr.co.pulmuone.v1.order.regular.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송 결과 결제 대상 목록 조회 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 02. 21.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "정기배송 결과 결제 대상 목록 조회 Dto")
public class RegularResultPaymentListDto {

	@ApiModelProperty(value = "정기배송주문신청PK")
	private long odRegularReqId;

	@ApiModelProperty(value = "주문PK")
	private long odOrderId;

	@ApiModelProperty(value = "정기배송주문결과PK")
	private long odRegularResultId;

	@ApiModelProperty(value = "상품명")
	private String goodsNm;

	@ApiModelProperty(value = "배치키")
	private String batchKey;

	@ApiModelProperty(value = "카드마스킹번호")
	private String cardMaskNumber;

	@ApiModelProperty(value = "카드명")
	private String cardNm;

	@ApiModelProperty(value = "주문결제마스터PK")
	private long odPaymentMasterId;

	@ApiModelProperty(value = "회원PK")
	private long urUserId;

	@ApiModelProperty(value = "주문자명")
	private String buyerNm;

	@ApiModelProperty(value = "주문자이메일")
	private String buyerMail;

	@ApiModelProperty(value = "주문자핸드폰")
	private String buyerHp;

	@ApiModelProperty(value = "주문번호")
	private String odid;

	@ApiModelProperty(value = "결제금액")
	private int paymentPrice;

	@ApiModelProperty(value = "과세금액")
	private int taxablePrice;

	@ApiModelProperty(value = "비과세금액")
	private int nonTaxablePrice;

	@ApiModelProperty(value = "전체회차")
	private int totCnt;

	@ApiModelProperty(value = "이번회차")
	private int reqRound;

	@ApiModelProperty(value = "이전결제실패건수")
	private int prevFailCnt;
}
