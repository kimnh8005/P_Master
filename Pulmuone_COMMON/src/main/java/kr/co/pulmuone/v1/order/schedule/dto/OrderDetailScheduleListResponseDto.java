package kr.co.pulmuone.v1.order.schedule.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 상세 스케줄 리스트 Response Dto
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

@Builder
@Getter
@Setter
@ToString
@ApiModel(description = "주문 상세 스케줄 리스트 Response Dto")
public class OrderDetailScheduleListResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "주문 상세 스케줄 상품정보")
	private OrderDetailScheduleGoodsDto goodsInfo;

	@ApiModelProperty(value = "주문 상세 배송일자별 스케줄 리스트")
	private	List<OrderDetailScheduleDelvDateDto> scheduleDelvDateList;
}
