package kr.co.pulmuone.v1.order.schedule.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * <PRE>
 * Forbiz Korea
 * 주문리스트 스캐줄 관련 dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 2. 10.       석세동         최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "주문 상세 스케줄 요일 리스트 Dto")
public class OrderDetailScheduleDayOfWeekListDto {

	@ApiModelProperty(value = "배송 일자")
	private String delvDate;

	@ApiModelProperty(value = "배송 요일")
	private String delvDateWeekDay;

	@ApiModelProperty(value = "주문 수량")
	private int orderCnt;
}