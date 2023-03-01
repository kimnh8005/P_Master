package kr.co.pulmuone.v1.order.order.service;

import java.util.List;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.order.order.dto.OrderDetailDateUpdateRequestDto;
import kr.co.pulmuone.v1.order.order.dto.OrderDetailDeliveryStatusRequestDto;
import kr.co.pulmuone.v1.order.order.dto.OrderDetailIfDateListRequestDto;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlHistVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlVo;

/**
 * <PRE>
 * Forbiz Korea
 * 주문상태 관련 Interface
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 14.  이명수            최초작성
 *  1.1    2021. 01. 08.  이규한            주문상세 배송상태 업데이트 추가
 * =======================================================================
 * </PRE>
 */
public interface OrderDetailDeliveryBiz {
	/** 주문상세 배송상태 업데이트 */
	public ApiResult<?> putOrderDetailDeliveryStatus(List<OrderDetailDeliveryStatusRequestDto> orderDetailDeliveryStatusRequestDtoList);
	/** 주문상세 주문I/F 출고지시일 목록 조회 */
	ApiResult<?> getIfDayList(OrderDetailIfDateListRequestDto orderDetailIfDateListRequestDto) throws Exception;
	/** 주문상세 주문I/F 정보 업데이트 */
	ApiResult<?> putIfDay(OrderDetailDateUpdateRequestDto orderDetailDateUpdateRequestDto)  throws Exception;
	/** 주문상세 주문I/F 정보 업데이트 */
	ApiResult<?> putIfDay(OrderDetailDateUpdateRequestDto orderDetailDateUpdateRequestDto, long createId)  throws Exception;
	/** 주문생성 주문I/F 출고지시일 목록 조회 */
	ApiResult<?> getIfDayListForOrderCreate(OrderDetailIfDateListRequestDto orderDetailIfDateListRequestDto) throws Exception;

	OrderDetlVo getHistMsgOdOrderDetlId(Long odOrderDetlId, String type) throws Exception;

	int putOrderDetailStatusHist(OrderDetlHistVo orderDetlHistVo) throws Exception;
}
