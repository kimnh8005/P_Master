package kr.co.pulmuone.v1.promotion.linkprice.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "링크프라이스 엑셀 내역조회 ResultVo")
public class LinkPriceExcelResultVo {

	@ApiModelProperty(value = "순번")
	private String rowNumber;

	/* 주문번호 */
	private String ordNo;

	/* 결제일자 */
	private String paidDt;

	/* 상품번호 */
	private String ilGoodsId;

	/* 상품명 */
	private String goodsNm;

	/* 임직원할인여부 */
	private String isEmpDiscount;

	/* 일일배송/정기배송여부 */
	private String isNotDlv;

	/* 녹즙, 잇슬림 브랜드여부 */
	private String isNotBrand;

	/* 주문수량 */
	private String orderCnt;

	/* 총결제금액 */
	private String ordTotalPrice;

	/* 취소수량 */
	private String claimCnt;

	/* 취소금액 */
	private String cnclTotalPrice;

	/* 적립금 사용금액 */
	private String ordPointPrice;

	/* LPINFO */
	private String lpinfo;
}
