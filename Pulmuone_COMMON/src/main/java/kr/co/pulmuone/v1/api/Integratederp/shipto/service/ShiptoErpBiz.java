package kr.co.pulmuone.v1.api.Integratederp.shipto.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;

/**
 * <PRE>
 * Forbiz Korea
 * 주문|취소 ERP 연동
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20210115   	천혜현     최초작성
 * =======================================================================
 * </PRE>
 */

public interface ShiptoErpBiz {

	// 납품처정보 조회
	public  ApiResult<?> getIfShiptoSrchByErp(String splId) throws Exception;

}
