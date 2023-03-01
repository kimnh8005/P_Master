package kr.co.pulmuone.v1.order.create.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 주문생성  배송 스케줄 변경 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일                		:	작성자      :	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 04. 16.		이규한	최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "주문생성  배송 스케줄 변경 Dto")
public class OrderCreateScheduledDto {

	@ApiModelProperty(value = "배송 스케쥴 변경 도착 예정일자")
	private String arrivalScheduledDate;

	@ApiModelProperty(value = "새벽배송 여부")
	private String dawnDeliveryYn;
}