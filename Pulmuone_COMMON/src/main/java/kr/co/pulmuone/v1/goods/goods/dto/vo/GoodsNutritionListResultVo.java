package kr.co.pulmuone.v1.goods.goods.dto.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GoodsNutritionListResultVo
{

	/**
	 * 영양정보 분류명
	 */
	private String nutritionName;


	/**
	 * 영양정보 분류단위
	 */
	private String nutritionUnit;


	/**
	 * 영양소 기준치 사용여부
	 */
	private String nutritionPercentYn;


	/**
	 * 영양성분량
	 */
	private String nutritionQty;


	/**
	 * 영양성분 기준치대비 함량
	 */
	private String nutritionPercent;


}
