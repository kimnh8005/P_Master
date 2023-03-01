package kr.co.pulmuone.v1.goods.goods.dto.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ShippingDataResultVo {

	/**
	 * 배송템플릿 PK
	 */
	private Long ilShippingTmplId;

	/**
	 * 배송비 금액
	 */
	private int shippingPrice;

	/**
	 * 배송 조건
	 */
	private int conditionValue;

	/**
	 * 조건배송비 구분
	 */
	private String conditionType;

	/**
	 * 배송불가지역 유형
	 */
	private String undeliverableAreaType;

	/**
	 * 지역별 배송비 사용 여부(Y: 사용, N: 미사용)
	 */
	private String areaShippingYn;

	/**
	 * 제주 추가 배송비
	 */
	private int jejuShippingPrice;

	/**
	 * 도서산간 추가 배송비
	 */
	private int islandShippingPrice;
}
