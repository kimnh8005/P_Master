package kr.co.pulmuone.v1.order.schedule.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 상세 배송일자별 스케줄 리스트 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일                		:	작성자      :	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 02. 05.		이규한	최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "주문 상세 배송일자별 스케줄 리스트 Dto")
public class OrderDetailScheduleDelvDateDto {

	@ApiModelProperty(value = "배송일자")
	private String delvDate;

	@ApiModelProperty(value = "배송일자")
	private String orgDelvDate;

	@ApiModelProperty(value = "배송일자(요일)")
	private String delvDateWeekDay;

	@ApiModelProperty(value = "배송완료 여부")
	private String deliveryYn;

	@ApiModelProperty(value = "마감시간 여부")
	private String cutoffTimeYn;

	@ApiModelProperty(value = "스케줄 리스트")
	private	List<OrderDetailScheduleListDto> rows;

	@ApiModelProperty(value = "도착일변경 버튼 노출 YN")
	private String changeDeliveryDateBtnYn;
}