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
 * 주문 상세 스케줄 리스트 Request Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일                		:	작성자      :	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 01. 29.		이규한	최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "주문 상세 스케줄 변경 Request Dto")
public class OrderDetailScheduleUpdateRequestDto {

	@ApiModelProperty(value = "주문상세 PK")
	private long odOrderDetlId;

	@ApiModelProperty(value = "변경 적용일(YYYY-MM-DD)")
	private String changeDate;

	@ApiModelProperty(value = "배송 요일")
	private String deliveryDayOfWeek;

    @ApiModelProperty(value = "배송 요일 리스트")
    private List<String> deliveryDayOfWeekList;

	@ApiModelProperty(value = "배송 요일별 수량")
	private String deliveryDayOfWeekCnt;

    @ApiModelProperty(value = "배송 요일별 수량 리스트")
    private List<String> deliveryDayOfWeekCntList;

	@ApiModelProperty(value = "스케줄 변경 목록")
	private List<OrderDetailScheduleUpdateDto> scheduleUpdateList;

    @ApiModelProperty(value = "배송 요일 건수")
    private int scheduleLimit;

	@ApiModelProperty(value = "출고처 PK")
	private long urWarehouseId;

	private String scheduleUpdateListData;

	@ApiModelProperty(value = "도착일 변경 최대 선택가능일자")
	private int changeScheduleMaxDate;
}