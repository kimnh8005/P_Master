package kr.co.pulmuone.v1.order.email.dto;

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
 *  1.0    2021. 03. 11.	천혜현		최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "자동메일 정기배송주문신청 결과 조회 Dto")
public class OrderRegularResultDto {

	@ApiModelProperty(value = "정기배송주문신청PK")
	private long odRegularReqId;

	@ApiModelProperty(value = "주문PK")
	private long odOrderId;

	@ApiModelProperty(value = "정기배송주문결과PK")
	private long odRegularResultId;

	@ApiModelProperty(value = "정기배송주문 신청결과상세PK")
	private long odRegularResultDetlId;

	@ApiModelProperty(value = "상품명")
	private String goodsNm;

	@ApiModelProperty(value = "주문 수")
	private int orderCnt;

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

    @ApiModelProperty(value = "주문금액 (정가 + 배송비)")
    private int totalSalePrice;

    @ApiModelProperty(value = "총 할인금액")
    private int discountPrice;

    @ApiModelProperty(value = "적립금금액")
    private int pointPrice;

    @ApiModelProperty(value = "잔여 결제예정금액")
    private int remainPaymentPrice;

	@ApiModelProperty(value = "요청회차")
	private int reqRound;

	@ApiModelProperty(value = "전체회차")
	private int totalRound;

	@ApiModelProperty(value = "결제실패건수")
	private int paymentFailCnt;

	@ApiModelProperty(value = "응답데이터")
	private String responseData;

	@ApiModelProperty(value = "결제실패일자")
	private String paymentFailDate;

	@ApiModelProperty(value = "결제일자")
	private String createDate;

	@ApiModelProperty(value = "실패사유")
	private String paymentFailResponseMsg;

	@ApiModelProperty(value = "도착예정일")
	private String arriveDt;

	@ApiModelProperty(value = "도착예정일_(MM)")
	private String arriveDtMonth;

	@ApiModelProperty(value = "도착예정일_(DD)")
	private String arriveDtDay;

    @ApiModelProperty(value = "결제타입")
    private String payType;

    @ApiModelProperty(value = "결제타입명")
    private String payTypeName;

    @ApiModelProperty(value = "결제정보")
    private String info;

    @ApiModelProperty(value = "카드 할부기간")
    private String cardQuota;

	@ApiModelProperty(value = "수령인 주소")
    private String recvAddr;


}
