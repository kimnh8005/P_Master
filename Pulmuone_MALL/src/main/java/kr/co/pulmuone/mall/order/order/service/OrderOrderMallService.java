package kr.co.pulmuone.mall.order.order.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;

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
 *  1.0    20201224		 	천혜현            최초작성
 * =======================================================================
 * </PRE>
 */

public interface OrderOrderMallService {

	/**
	 * 주문 완료 가상계좌 정보 조회
	 *
	 * @param	odid
	 * @return	virtualAccountResponseDto
	 * @throws	Exception
	 */
	ApiResult<?> getVirtualAccount(String odid) throws Exception;

	/**
	 * 주문 완료 결제 정보 조회
	 *
	 * @param	odid
	 * @return	paymentInfoResponseDto
	 * @throws	Exception
	 */
	ApiResult<?> getPaymentInfo(String odid) throws Exception;

}
