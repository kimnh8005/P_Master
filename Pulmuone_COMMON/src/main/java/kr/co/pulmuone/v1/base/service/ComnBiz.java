package kr.co.pulmuone.v1.base.service;

import javax.servlet.http.HttpServletRequest;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.send.template.dto.vo.GetEmailSendResultVo;

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
 *  1.0    20200803    강윤경              최초작성
 * =======================================================================
 * </PRE>
 */

public interface ComnBiz {

	//나의 아이디로 가장 최근에 로그인한 시퀀스를 가져온다.
	ApiResult<?> hasSessionInfoByLoginId(HttpServletRequest req) throws Exception;

	ApiResult<?> addMenuAccessLog(HttpServletRequest request, UserVo userVo) throws Exception;

	ApiResult<?> getSendTmplt(String templateCode, Object vo);

	String getEmailTmplt(GetEmailSendResultVo getEmailSendResultVo, Object vo);

	String getSMSTmplt(GetEmailSendResultVo getEmailSendResultVo, Object vo);

	String getTmpltContext(String Context, Object vo);

}

