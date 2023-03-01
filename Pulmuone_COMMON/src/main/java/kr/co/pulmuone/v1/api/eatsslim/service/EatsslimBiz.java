package kr.co.pulmuone.v1.api.eatsslim.service;

import kr.co.pulmuone.v1.api.eatsslim.dto.EatsslimOrderDeliveryListDto;
import kr.co.pulmuone.v1.api.eatsslim.dto.EatsslimOrderInfoDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleListRequestDto;


/**
 * <PRE>
 * Forbiz Korea
 * 잇슬림 I/F Biz
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일				: 작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 01. 29.	     이규한		최초작성
 * =======================================================================
 * </PRE>
 */

public interface EatsslimBiz {
	/** 잇슬림 주문정보, 배송 스케쥴정보 조회 */
	public ApiResult<?> getOrderScheduleList(OrderDetailScheduleListRequestDto orderDetailScheduleListRequestDto) throws Exception;
	/** 잇슬림 스케줄 배송일자 변경 */
	public ApiResult<?> putScheduleArrivalDate(EatsslimOrderInfoDto eatsslimOrderInfoDto, EatsslimOrderDeliveryListDto eatsslimOrderDeliveryListDto) throws Exception;
}