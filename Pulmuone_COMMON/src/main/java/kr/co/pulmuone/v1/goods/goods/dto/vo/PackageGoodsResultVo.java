package kr.co.pulmuone.v1.goods.goods.dto.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PackageGoodsResultVo
{

	/**
	 * 묶음 구성 상품 PK
	 */
	private Long ilGoodsId;


	/**
	 * 구성수량
	 */
	private int goodsQty;


	/**
	 * 구성상품 판매가(개당)
	 */
	private int unitSalePrice;


	/**
	 * 판매가
	 */
	private int salePrice;

	/**
	 * 과세여부
	 */
	private String taxYn;

	/**
	 * 품목코드
	 */
	private String ilItemCd;
}
