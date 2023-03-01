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
@ApiModel(description = "도착예정일 Dto")
public class GoodsDailyMealScheduleDto {

	/**
	 * 주문일자 (주문 I/F 일자)
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
	 * 일반
	 */
	private List<GoodsDailyMealScheduleDataDto> basic;

	/**
	 * 알러지제외식단
	 */
	private List<GoodsDailyMealScheduleDataDto> noAllergy;
}
