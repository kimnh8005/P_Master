package kr.co.pulmuone.v1.shopping.cart.dto.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ShippingTemplateGroupByVo {

	/**
	 * 출고처 PK
	 */
	private Long urWarehouseId;

	/**
	 * 출고처명
	 */
	private String warehouseNm;

	/**
	 * 합배송 여부
	 */
	private String bundleYn;

	/**
	 * 합배송 제외시 배송비 탬플릿 PK
	 */
	private Long ilShippingTmplId;

	/**
	 * 합배송 제외시 장바구니 PK
	 */
	private Long spCartId;

	/**
	 * 예약상품 도착 예정일자
	 */
	private String reserveArriveDate;

	/**
	 * 새벽배송YN
	 */
	private String dawnDeliveryYn;

	/**
	 * 상품 타입
	 */
	private String goodsType;
}
