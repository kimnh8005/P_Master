package kr.co.pulmuone.v1.promotion.linkprice.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "링크프라이스 내역조회 ResultVo")
public class LinkPriceResultVo {

	@ApiModelProperty(value = "순번")
	private String rowNumber;

	/* 주문번호 */
	private String ordNo;

	/* 결제일자 */
	private String paidDt;

	/* 총결제금액 */
	private String ordTotalPrice;

	/* 취소금액 */
	private String cnclTotalPrice;

	/* 적립금 사용금액 */
	private String ordPointPrice;

	/* 임직원 할인 금액 */
	private String ordEmpDisPrice;

	/* LPINFO */
	private String lpinfo;
}
