package kr.co.pulmuone.v1.goods.goods.dto.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderSelectGoodsVo {

	/**
	 * 배송비 정책 PK
	 */
	private Long ilShippingTmplId;

	/**
	 * 출고처 PK
	 */
	private Long urWarehouseId;

	/**
	 * 공급업체 PK
	 */
	private Long urSupplierId;

	/**
	 * 출고처 그룹 코드
	 */
	private String warehouseGroupCode;

	/**
	 * 품목 보관방법
	 */
	private String storageMethodType;

	/**
	 * 상품유형
	 */
	private String goodsType;

	/**
	 * 판매유형
	 */
	private String saleType;

	/**
	 * 표준 카테고리PK
	 */
	private Long ilCtgryStdId;

	/**
	 * 전시 카테고리PK
	 */
	private Long ilCtgryDisplayId;

	/**
	 * 몰인몰 카테고리
	 */
	private Long ilCtgryMallId;

	/**
	 * 품목 바코드
	 */
	private String itemBarcode;

	/**
	 * 품목 코드
	 */
	private String ilItemCode;

	/**
	 * 상품 원가
	 */
	private int standardPrice;

	/**
	 * 상품 정가
	 */
	private int recommendedPrice;

	/**
	 * 표준 브랜드
	 */
	private Long urBrandId;

	/**
	 * 일일상품 유형(공통코드)
	 */
	private String goodsDailyType;
}
