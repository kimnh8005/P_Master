package kr.co.pulmuone.mall.order.present.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.order.present.dto.IsShippingPossibilityRequestDto;
import kr.co.pulmuone.v1.order.present.dto.OrderPresentReceiveRequestDto;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20210715   	 	홍진영            최초작성
 * =======================================================================
 * </PRE>
 */

public interface OrderPresentMallService {

	/**
	 * 선물 확인
	 *
	 * @param presentId
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> getOrderPresentConfirm(String presentId) throws Exception;

	/**
	 * 선물인증하기
	 *
	 * @param presentId
	 * @param presentAuthCd
	 * @param captcha
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> getOrderPresentAuth(String presentId, String presentAuthCd, String captcha) throws Exception;

	/**
	 * 선물하기 배송가능 체크
	 *
	 * @param presentId
	 * @param odOrderId
	 * @param zipCode
	 * @param buildingCode
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> isShippingPossibility(IsShippingPossibilityRequestDto reqDto) throws Exception;

	/**
	 * 선물 거절
	 *
	 * @param presentId
	 * @param odOrderId
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> reject(String presentId, Long odOrderId) throws Exception;

	/**
	 * 선물 받기
	 *
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> receive(OrderPresentReceiveRequestDto reqDto) throws Exception;

	/**
	 * 메세지 재발송
	 *
	 * @param odOrderId
	 * @param odid
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> reSendMessage(String odid) throws Exception;
}
