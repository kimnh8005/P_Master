package kr.co.pulmuone.v1.order.delivery.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.order.order.dto.mall.MallArriveDateListRequestDto;
import kr.co.pulmuone.v1.order.order.dto.mall.MallArriveDateUpdateRequestDto;

/**
 * <PRE>
 * Forbiz Korea
 * Mall 주문 배송 관련 Interface
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 01. 18.	이규한		최초작성
 * =======================================================================
 * </PRE>
 */

public interface MallOrderDeliveryBiz {
	/** 도착예정일 변경일자 조회 */
	public ApiResult<?> getArriveDateList(MallArriveDateListRequestDto mallArriveDateListRequestDto) throws Exception;
	/** 도착 예정일 변경 */
	public ApiResult<?> putArriveDate(MallArriveDateUpdateRequestDto mallArriveDateUpdateRequestDto, long createId) throws Exception;
}