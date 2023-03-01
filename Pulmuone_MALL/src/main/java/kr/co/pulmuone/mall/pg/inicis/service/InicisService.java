package kr.co.pulmuone.mall.pg.inicis.service;

import javax.servlet.http.HttpServletResponse;

import kr.co.pulmuone.v1.pg.service.inicis.dto.InicisApprovalRequestDto;
import kr.co.pulmuone.v1.pg.service.inicis.dto.InicisMobileApprovalRequestDto;
import kr.co.pulmuone.v1.pg.service.inicis.dto.InicisNotiRequestDto;
import kr.co.pulmuone.v1.pg.service.inicis.dto.InicisVirtualAccountReturnRequestDto;

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
 *  1.0    20201016		 	홍진영            최초작성
 * =======================================================================
 * </PRE>
 */

public interface InicisService {

	void close(HttpServletResponse httpServletResponse) throws Exception;

	String result(InicisApprovalRequestDto reqDto) throws Exception;

	String next(InicisMobileApprovalRequestDto reqDto) throws Exception;

	void virtualAccountReturn(HttpServletResponse httpServletResponse, InicisVirtualAccountReturnRequestDto reqDto)
			throws Exception;

	void noti(HttpServletResponse httpServletResponse, InicisNotiRequestDto reqDto)
			throws Exception;

	String addPayResult(InicisApprovalRequestDto reqDto) throws Exception;

	String addPayNext(InicisMobileApprovalRequestDto reqDto) throws Exception;
}
