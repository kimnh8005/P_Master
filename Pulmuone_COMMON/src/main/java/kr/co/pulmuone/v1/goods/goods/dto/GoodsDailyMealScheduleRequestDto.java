package kr.co.pulmuone.v1.goods.goods.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "식단 스케줄 조회 요청 Dto")
public class GoodsDailyMealScheduleRequestDto {

	/**
	 * 상품PK
	 */
	private Long ilGoodsId;

	/**
	 * 일괄배송여부 : Y:일괄배송, N:일일배송
	 */
	private String bulkYn;

	/**
	 * 시작일자
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate startDate;

	/**
	 * 종료일자
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate endDate;
}


