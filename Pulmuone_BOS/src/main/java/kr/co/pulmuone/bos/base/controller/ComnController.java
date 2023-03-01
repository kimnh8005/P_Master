package kr.co.pulmuone.bos.base.controller;

import javax.servlet.http.HttpServletRequest;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.base.dto.SessionResponseDto;
import kr.co.pulmuone.v1.base.service.ComnBiz;


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
 *  1.0    20200605    오영민              최초작성
 * =======================================================================
 * </PRE>
 */

@Controller("ComnControllerHangaram")
public class ComnController {

	@Autowired
	private ComnBiz comnBiz;
	
	@Value("${spring.profiles.active}")
	private String activeProfile;

	@GetMapping(value = "/system/getSession")
	@ResponseBody
	@ApiOperation(value = "세션정보 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = SessionResponseDto.class)
	})
	public ApiResult<?> getSession() {
		SessionResponseDto dto = new SessionResponseDto();
		UserVo userVo = SessionUtil.getBosUserVO();
		dto.setSession(userVo);

		return ApiResult.success(dto);
	}

	/**
	 *  세션체크용
	 *  - 엑셀 다운로드 등에서 사용
	 *  - getSession와 동일 기능이나 "최근 접속 정보 알림 팝업" 열지 않음
	 * @return
	 */
	@RequestMapping(value = "/system/getSessionCheck")
	@ApiOperation(value = "세션정보 조회")
	@ApiResponses(value = {
	    @ApiResponse(code = 900, message = "response data", response = SessionResponseDto.class)
	})
	@ResponseBody
	public ApiResult<?> getSessionCheck() {
	  SessionResponseDto dto = new SessionResponseDto();
	  UserVo userVo = SessionUtil.getBosUserVO();
	  dto.setSession(userVo);

	  return ApiResult.success(dto);
	}

	@PostMapping(value = "/admin/comn/hasSessionInfoByLoginId")
	@ResponseBody
	@ApiOperation(value = "이전 로그인 세션 로그아웃 처리", notes = "주기적으로 세션을 체크 후 후입자 우선으로 자동 로그아웃 처리")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = SessionResponseDto.class),
			@ApiResponse(code = 901, message = "" +
					"[LOGIN_ANOTHER_ROUTE] LOGIN_ANOTHER_ROUTE - 다른 PC에서 로그인한 사용자가 있어 자동로그아웃 처리됩니다. \n"
			)
	})
	public ApiResult<?> hasSessionInfoByLoginId(HttpServletRequest req) throws Exception{
		return comnBiz.hasSessionInfoByLoginId(req);
	}
	
	
	/**
	 * Spring profiles 조회 (prod, ver, qa, dev)
	 */
	@RequestMapping(value = "/admin/comn/getProfilesActive")
	@ApiOperation(value = "스프링 어프리케이션 프로필 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data(prod|ver|qa|dev)", response = String.class)
	})
	@ResponseBody
	public ApiResult<?> getProfilesActive() {
		return ApiResult.success(activeProfile);
	}
}
