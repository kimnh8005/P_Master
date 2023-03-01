package kr.co.pulmuone.mall.pg.kcp.service;

import javax.servlet.http.HttpServletResponse;

import kr.co.pulmuone.v1.pg.service.inicis.dto.InicisVirtualAccountReturnRequestDto;
import kr.co.pulmuone.v1.pg.service.kcp.dto.KcpApplyRegularBatchKeyRequestDto;
import kr.co.pulmuone.v1.pg.service.kcp.dto.KcpApprovalRequestDto;
import kr.co.pulmuone.v1.pg.service.kcp.dto.KcpVirtualAccountReturnRequestDto;

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
 *  1.0    20200810		 	천혜현            최초작성
 * =======================================================================
 * </PRE>
 */

public interface KcpService {

	String approval(KcpApprovalRequestDto approvalRequestDto) throws Exception;

	void virtualAccountReturn(HttpServletResponse httpServletResponse, KcpVirtualAccountReturnRequestDto reqDto)
			throws Exception;

	void applyRegularBatchKey(HttpServletResponse httpServletResponse,
			KcpApplyRegularBatchKeyRequestDto kcpReturnApplyRegularBatchKeyRequestDto) throws Exception;

	String addPayApproval(KcpApprovalRequestDto approvalRequestDto) throws Exception;
}
