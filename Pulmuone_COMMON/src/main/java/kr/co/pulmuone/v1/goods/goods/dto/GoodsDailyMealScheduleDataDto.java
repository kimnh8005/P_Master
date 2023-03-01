package kr.co.pulmuone.v1.goods.goods.dto;

import java.time.LocalDate;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "도착예정일 Dto")
public class GoodsDailyMealScheduleDataDto {

	/**
	 * 배송예정일
	 */
	private LocalDate deliveryDate;

	/**
	 * 휴일여부
	 */
	private String holidayYn;

	/**
	 * 휴일명
	 */
	private String holidayName;

	/**
	 * 알러지 유발 여부
	 */
	private String allergyYn;

	/**
	 * 식단 컨텐츠 코드
	 */
	private String ilGoodsDailyMealContsCd;

	/**
	 * 식단 썸네일 이미지
	 */
	private String thumbnailImg;

	/**
	 * 식단명
	 */
	private String mealName;

	/**
	 * 세트순번
	 */
	private String setNo;

	/**
	 * 칼로리
	 */
	private String calorie;
}
