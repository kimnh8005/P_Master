package kr.co.pulmuone.v1.order.schedule.service.mall;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleListDto;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleListRequestDto;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleUpdateRequestDto;

import java.util.List;

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

public interface MallOrderScheduleBiz {
	/** 주문 녹즙/잇슬림/베이비밀 스캐줄 리스트 조회 */
	ApiResult<?> getOrderScheduleList(Long odOrderDetlId) throws Exception;
	/** 주문 녹즙/잇슬림/베이비밀 스케줄 배송일자,수량 변경 */
	ApiResult<?> putScheduleArrivalDate(OrderDetailScheduleUpdateRequestDto orderDetailScheduleUpdateRequestDto) throws Exception;
	/** 주문 녹즙/잇슬림/베이비밀 스케줄 배송요일 변경 */
	ApiResult<?> putScheduleArrivalDay(OrderDetailScheduleUpdateRequestDto orderDetailScheduleUpdateRequestDto) throws Exception;
	/** 주문 녹즙/잇슬림/베이비밀 배송가능 스케줄 리스트 조회 */
	ApiResult<?> getOrderDeliverableScheduleList(Long parseLong) throws Exception;

	/**주문 녹즙 스캐줄 리스트 조회*/
	List<OrderDetailScheduleListDto> getOrderDetailScheduleList(OrderDetailScheduleListRequestDto orderDetailScheduleListRequestDto);
	/**주문 녹즙 스캐줄 주문 배송지 PK 수정*/
	int putOrderDetlScheduleOdShippingZoneId(Long odShippingZoneId, Long odOrderDetlDailyId, String changeDate);
}