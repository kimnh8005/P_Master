package kr.co.pulmuone.v1.order.schedule.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleListRequestDto;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleUpdateRequestDto;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 녹즙/잇슬림/베이비밀 스캐줄 관련 I/F
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 1. 20.		석세동         	최초작성
 *  1.1	   2021. 1. 28.		이규한		주문 녹즙/잇슬림/베이비밀 스캐줄 리스트 조회 I/F 추가
 * =======================================================================
 * </PRE>
 */

public interface OrderScheduleBindBiz {
	/** 주문 녹즙/잇슬림/베이비밀 스케줄 리스트 조회 */
    public ApiResult<?> getOrderScheduleList(OrderDetailScheduleListRequestDto orderDetailScheduleListRequestDto) throws Exception;
	/** 주문 녹즙/잇슬림/베이비밀 스케줄 도착일,수량 변경 */
	public ApiResult<?> putScheduleArrivalDate(OrderDetailScheduleUpdateRequestDto orderDetailScheduleUpdateRequestDto) throws Exception;
	/** 주문 녹즙/잇슬림/베이비밀 스케줄 배송요일 변경 */
	public ApiResult<?> putScheduleArrivalDay(OrderDetailScheduleUpdateRequestDto orderDetailScheduleUpdateRequestDto) throws Exception;
	/** 주문 녹즙/잇슬림/베이비밀 스케줄 건너뛰기 */
	public ApiResult<?> putScheduleSkip(OrderDetailScheduleUpdateRequestDto orderDetailScheduleUpdateRequestDto) throws Exception;
}