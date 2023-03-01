package kr.co.pulmuone.v1.promotion.linkprice.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "링크프라이스 합계 내역조회 ResultVo")
public class LinkPriceTotalResultVo {

	@ApiModelProperty(value = "순번")
	private String rowNumber;

	/* 총 주문건수 */
	private String totOrdCnt;

	/* 총 결제금액 */
	private String totOrdPrice;

	/* 총 취소건수 */
	private String totCnclCnt;

	/* 총 취소금액 */
	private String totCnclPrice;
}
