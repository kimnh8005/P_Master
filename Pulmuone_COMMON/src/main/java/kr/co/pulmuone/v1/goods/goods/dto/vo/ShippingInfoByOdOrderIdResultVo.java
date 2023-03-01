package kr.co.pulmuone.v1.goods.goods.dto.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ShippingInfoByOdOrderIdResultVo {

	/**
	 * 상품 배송비 정책 PK
	 */
	private Long ilGoodsShippingTemplateId;

	/**
	 * 상품 배송 타입(택배배송, 새벽배송)
	 */
	private String goodsDeliveryType;

	/**
	 * 상품 PK
	 */
	private Long ilGoodsId;

	/**
	 * 주문 수
	 */
	private int totalOrderCnt;

	/**
	 * 판매가 - 상품,장바구니쿠폰 할인 제외한 할인금액
	 */
	private int totalPaymentPrice;

	/**
	 * 배송템플릿 PK
	 */
	private Long ilShippingTmplId;

	/**
	 * 배송비
	 */
	private int orgShippingPrice;

	/**
	 * 상품의 배송불가지역 코드
	 */
	private String undeliverableAreaTp;

}
