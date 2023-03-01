package kr.co.pulmuone.v1.goods.goods.dto;

import java.time.LocalDate;
import java.util.List;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "식단 Dto")
public class GoodsPageInfoMealDto {

	/**
	 * 식단 분류 (MALL_DIV.BABYMEAL, MALL_DIV.EATSLIM)
	 */
	private String mallDiv;

	/**
	 * 식단 스케줄 도착예정일 목록
	 */
	private List<LocalDate> deliveryDateList;
}
