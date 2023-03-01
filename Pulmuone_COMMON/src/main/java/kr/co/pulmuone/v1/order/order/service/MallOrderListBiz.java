package kr.co.pulmuone.v1.order.order.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.order.order.dto.mall.MallOrderDailyListRequestDto;
import kr.co.pulmuone.v1.order.order.dto.mall.MallOrderListRequestDto;

/**
 * <PRE>
 * Forbiz Korea
 * Mall 주문리스트 관련 Interface
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 01. 12.	이규한		최초작성
 * =======================================================================
 * </PRE>
 */
public interface MallOrderListBiz {

	/**
	 * 주문/배송 리스트 조회
	 * @param mallOrderListRequestDto
	 * @return
	 */
	public ApiResult<?> getOrderList(MallOrderListRequestDto mallOrderListRequestDto);

	/**
	 * 취소/반품 리스트 조회
	 * @param mallOrderListRequestDto
	 * @return
	 */
	public ApiResult<?> getOrderClaimList(MallOrderListRequestDto mallOrderListRequestDto);

	/**
	 * 일일배송 리스트 조회
	 * @param mallOrderDailyListRequestDto
	 * @return
	 */
	public ApiResult<?> getOrderDetailDailyList(MallOrderDailyListRequestDto mallOrderDailyListRequestDto);

	/**
	 * 보낸선물함 리스트 조회
	 * @param mallOrderListRequestDto
	 * @return
	 */
	public ApiResult<?> getOrderPresentList(MallOrderListRequestDto mallOrderListRequestDto);
}