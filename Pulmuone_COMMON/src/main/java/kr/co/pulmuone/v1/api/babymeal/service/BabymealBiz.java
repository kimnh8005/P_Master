package kr.co.pulmuone.v1.api.babymeal.service;

import java.util.List;

import kr.co.pulmuone.v1.api.babymeal.dto.BabymealOrderDeliveryListDto;
import kr.co.pulmuone.v1.api.babymeal.dto.BabymealOrderInfoDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleListRequestDto;

/**
 * <PRE>
 * Forbiz Korea
 * 베이비밀  API I/F
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 01. 26.	이규한		최초작성
 * =======================================================================
 * </PRE>
 */

public interface BabymealBiz {
	/** 베이비밀 주문정보, 배송 스케줄정보 조회 */
	public ApiResult<?> getOrderScheduleList(OrderDetailScheduleListRequestDto orderDetailScheduleListRequestDto) throws Exception;
	/** 베이비밀 배송 스케줄정보 변경 */
	public ApiResult<?> putScheduleArrivalDate(BabymealOrderInfoDto babymealOrderInfoDto, List<BabymealOrderDeliveryListDto> babymealOrderDeliveryListDtoList) throws Exception;
}