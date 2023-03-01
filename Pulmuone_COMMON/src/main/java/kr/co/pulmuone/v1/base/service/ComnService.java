package kr.co.pulmuone.v1.base.service;


import kr.co.pulmuone.v1.base.dto.MenuOperLogRequestDto;
import kr.co.pulmuone.v1.base.dto.PrivacyMenuOperLogRequestDto;
import kr.co.pulmuone.v1.base.dto.SessionResponseDto;
import kr.co.pulmuone.v1.base.dto.vo.MenuUrlResultVo;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.SystemUtil;
import kr.co.pulmuone.v1.system.basic.service.SystemBasicEnvironmentBiz;
import kr.co.pulmuone.v1.user.login.dto.LoginRequestDto;
import kr.co.pulmuone.v1.user.login.service.UserLoginBosBiz;
import kr.co.pulmuone.v1.comm.mapper.base.ComnMapper;
import kr.co.pulmuone.v1.system.basic.dto.GetEnvironmentListRequestDto;
import kr.co.pulmuone.v1.system.basic.dto.vo.GetEnvironmentListResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20200804    강윤경              최초작성
 * =======================================================================
 * </PRE>
 */

@Service
public class ComnService {

	private static final Logger log = LoggerFactory.getLogger(ComnService.class);

	@Autowired
	private UserLoginBosBiz userLoginBosBiz;

	@Autowired
	private SystemBasicEnvironmentBiz systemBasicEnvironmentBiz;

	@Autowired
	ComnMapper comnMapper;


	/**
	 * 주기적으로 세션을 체크 후
	 * 후입자 우선으로 자동 로그아웃 처리
	 * @param req
	 * @return
	 * @throws Exception
	 */
	protected ApiResult<?> hasSessionInfoByLoginId(HttpServletRequest req) throws Exception {
		SessionResponseDto sessionResponseDto = new SessionResponseDto();

		UserVo myUserVo = SessionUtil.getBosUserVO();

		if( myUserVo != null ){
			//로그인 동시접속 가능여부 조회
			GetEnvironmentListRequestDto getEnvironmentListRequestDto = new GetEnvironmentListRequestDto();
			getEnvironmentListRequestDto.setSearchEnvironmentKey("BOS_UR_LOGIN_CONCURRENCY_YN"); //BOS 동시 로그인 가능여부
			GetEnvironmentListResultVo getEnvironmentListResultVo = systemBasicEnvironmentBiz.getEnvironment(getEnvironmentListRequestDto);

			//접속 불가시
			if(UserEnums.BosLoginType.BOS_UR_LOGIN_CONCURRENCY_N.getCode().equals(getEnvironmentListResultVo.getEnvironmentValue())) {
				//이전 로그인 세션 삭제
				if (delPreviousLoginSession(req, myUserVo)) return ApiResult.result(UserEnums.Login.LOGIN_ANOTHER_ROUTE);
			}
		}

		return ApiResult.success(sessionResponseDto);

	}

	protected boolean delPreviousLoginSession(HttpServletRequest req, UserVo myUserVo) throws Exception {
		//나의 최근 로그인 시퀀스 받기
		UserVo latestUserVo = comnMapper.hasSessionInfoByLoginId(myUserVo);

		// 나의 로그인 시퀀스와 가장 최근에 로그인한 시퀀스를 비교
		if( myUserVo.getConnectionId() < latestUserVo.getConnectionId() ){
			//로그아웃처리
			LoginRequestDto loginRequestDto = new LoginRequestDto();
			loginRequestDto.setUrConnectLogId(myUserVo.getConnectionId());

			userLoginBosBiz.delLoginData(loginRequestDto, req);

			return true;
		}
		return false;
	}


	/**
	 * @Desc url로 정보 받기
	 * @param systemMenuId
	 * @param url
	 * @return
	 * @throws Exception
	 * @return MenuUrlResultVo
	 */
	protected MenuUrlResultVo getMenuUrlDataByUrl(String systemMenuId, String url) throws Exception {
		return comnMapper.getMenuUrlDataByUrl(systemMenuId, url);
	}


	/**
	 * @Desc 메뉴이력 저장
	 * @param menuOperLogRequestDto
	 * @return
	 * @throws Exception
	 * @return int
	 */
	protected int addMenuOperLog(MenuOperLogRequestDto menuOperLogRequestDto, HttpServletRequest req) throws Exception {
		 //접속 정보 받기
/*
		String addressIp = req.getHeader("X-FORWARDED-FOR");
	    if (addressIp == null){
	    	addressIp = req.getRemoteAddr();
	    }
*/
	    String addressIp = SystemUtil.getIpAddress(req);

	    menuOperLogRequestDto.setIp(addressIp);

		return comnMapper.addMenuOperLog(menuOperLogRequestDto);
	}


	/**
	 *
	 * @Desc 개인정보 처리 이력 로그 저장
	 * @param privacyMenuOperLogRequestDto
	 * @param req
	 * @return
	 * @throws Exception
	 * @return int
	 */
	protected int addPrivacyMenuOperLog(PrivacyMenuOperLogRequestDto privacyMenuOperLogRequestDto, HttpServletRequest req) throws Exception {
		 //접속 정보 받기
/*
		String addressIp = req.getHeader("X-FORWARDED-FOR");
	    if (addressIp == null){
	    	addressIp = req.getRemoteAddr();
	    }
*/
	    String addressIp = SystemUtil.getIpAddress(req);

	    privacyMenuOperLogRequestDto.setIp(addressIp);

		return comnMapper.addPrivacyMenuOperLog(privacyMenuOperLogRequestDto);
	}

}

