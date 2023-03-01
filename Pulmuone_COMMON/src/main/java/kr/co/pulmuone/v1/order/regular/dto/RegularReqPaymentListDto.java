package kr.co.pulmuone.v1.order.regular.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송 주문 신청 결제 목록 리스트 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 02. 10.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "정기배송 주문 신청 결제 목록 리스트 Dto")
public class RegularReqPaymentListDto {

	@ApiModelProperty(value = "정기배송주문결과PK")
	private long odRegularResultId;

	@ApiModelProperty(value = "결제정보 회차")
	private int reqRound;

	@ApiModelProperty(value = "회차 완료여부")
	private String reqRoundYn;

	@ApiModelProperty(value = "정상가")
	private int recommendedPrice;

	@ApiModelProperty(value = "판매가")
	private int salePrice;

	@ApiModelProperty(value = "배송비")
	private int shippingPrice;

	@ApiModelProperty(value = "할인금액")
	private int discountPrice;

	@ApiModelProperty(value = "기본할인율")
	private int discountRate;

	@ApiModelProperty(value = "추가할인금액")
	private int addDiscountPrice;

	@ApiModelProperty(value = "추가할인율")
	private int addDiscountRate;

	@ApiModelProperty(value = "추가할인회차")
	private int addDiscountReqRound;

	@ApiModelProperty(value = "결제금액 배송비 제외")
	private int paidPrice;

	@ApiModelProperty(value = "결제금액 배송비 포함")
	private int paymentPrice;

	@ApiModelProperty(value = "출고처배송비")
	private int warehouseShippingPrice;

	@ApiModelProperty(value = "출고처배송정책PK")
	private long ilShippingTmplId;

	@ApiModelProperty(value = "수령인우편번호")
	private String recvZipCd;
}
