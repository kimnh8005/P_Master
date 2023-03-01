package kr.co.pulmuone.v1.order.schedule.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 스케줄 변경정보 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일                		:	작성자      :	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 02. 08.		이규한	최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "스케줄 변경정보 Dto")
public class OrderDetailScheduleUpdateDto {

	@ApiModelProperty(value = "UDMS I/F ID(PK) | 주문 상세 일일배송 스케줄 PK")
	private String id;

	@ApiModelProperty(value = "변경할 배송일자(YYYY-MM-DD)")
	private String delvDate;

	@ApiModelProperty(value = "변경할 수량")
	private String orderCnt;
}