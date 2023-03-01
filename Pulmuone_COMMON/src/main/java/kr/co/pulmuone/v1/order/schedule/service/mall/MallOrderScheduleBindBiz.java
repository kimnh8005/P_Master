package kr.co.pulmuone.v1.order.schedule.service.mall;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleListRequestDto;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleUpdateRequestDto;

/**
*
* <PRE>
* Forbiz Korea
* 주문 스캐줄 리스트 관련 Interface
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2021. 2. 26.       석세동         최초작성
* =======================================================================
* </PRE>
*/

public interface MallOrderScheduleBindBiz {
	/** 주문 녹즙/잇슬림/베이비밀 스케줄 리스트 조회 */
    public ApiResult<?> getOrderScheduleList(OrderDetailScheduleListRequestDto orderDetailScheduleListRequestDto) throws Exception;
	/** 주문 녹즙/잇슬림/베이비밀 스케줄 도착일,수량 변경 */
	public ApiResult<?> putScheduleArrivalDate(OrderDetailScheduleUpdateRequestDto orderDetailScheduleUpdateRequestDto) throws Exception;
	/** 주문 녹즙/잇슬림/베이비밀 스케줄 배송요일 변경 */
	public ApiResult<?> putScheduleArrivalDay(OrderDetailScheduleUpdateRequestDto orderDetailScheduleUpdateRequestDto) throws Exception;
}