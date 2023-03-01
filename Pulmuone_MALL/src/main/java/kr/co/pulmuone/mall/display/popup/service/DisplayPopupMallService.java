package kr.co.pulmuone.mall.display.popup.service;

import javax.servlet.http.HttpServletRequest;

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
 * 버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 * 1.0    20201123   	     천혜현            최초작성
 * =======================================================================
 * </PRE>
 */

public interface DisplayPopupMallService {

    ApiResult<?> getPopupInfoMobile(HttpServletRequest request) throws Exception;

    ApiResult<?> getPopupInfoPc(HttpServletRequest request) throws Exception;

}
