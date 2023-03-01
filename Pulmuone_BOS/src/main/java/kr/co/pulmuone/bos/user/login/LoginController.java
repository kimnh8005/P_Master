package kr.co.pulmuone.bos.user.login;

import javax.servlet.http.HttpServletRequest;

import io.swagger.annotations.*;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.util.SHA256Util;
import kr.co.pulmuone.v1.user.certification.dto.GetLoginResponseDataDto;
import kr.co.pulmuone.v1.user.group.dto.vo.UserGroupMasterResultVo;
import kr.co.pulmuone.v1.user.login.dto.LoginRequestDto;
import kr.co.pulmuone.v1.user.login.dto.LoginResponseDto;
import kr.co.pulmuone.v1.user.login.service.UserLoginBosBiz;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
* <PRE>
* Forbiz Korea
* BOS 로그인
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 7. 20.    강윤경          최초작성
* =======================================================================
* </PRE>
*/
@RestController
public class LoginController {

	private static final Logger log = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private UserLoginBosBiz userLoginBosBiz;

	@Autowired(required=true)
	private HttpServletRequest request;

	/**
	 * 관리자 로그인
	 * @param loginRequestDto
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "관리자 로그인")
	@PostMapping(value = "/admin/login/hasLoginData")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = UserGroupMasterResultVo.class),
			@ApiResponse(code = 901, message = "" +
                    "LOGIN_NO_DATA_FAIL - 아이디 또는 비밀번호가 일치하지 않습니다.\n"
                  + "LOGIN_STATUS_TEMPORARY_STOP - 계정이 일시정지 되어 비밀번호 재설정 페이지로 이동합니다 \n"
                  + "LOGIN_STATUS_ADMINISTRATIVE_LEAVE - 해당 계정정보는 접속이 불가능한 계정입니다.<br>관리자에게 문의 바랍니다.\n"
                  + "LOGIN_PASSWORD_CHANGE - 비밀번호 변경이 필요합니다\n"
                  + "LOGIN_TEMPORARY_PASSWORD - 임시 비밀번호입니다. \n"
        )
	})
	public ApiResult<?> hasLoginData(LoginRequestDto loginRequestDto, HttpServletRequest req) throws Exception {
		loginRequestDto.setPassword(SHA256Util.getUserPassword(loginRequestDto.getPassword()));
		return userLoginBosBiz.hasLoginData(loginRequestDto, req);
	}


	/**
	 * 관리자 비밀번호 찾기
	 * @param loginRequestDto
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/login/getPassWordByData")
	public ApiResult<?> getPassWordByData(LoginRequestDto loginRequestDto) throws Exception {
		return userLoginBosBiz.getPassWordByData(loginRequestDto);
	}



	/**
	 * 관리자 비밀번호 재설정
	 * @param loginRequestDto
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/login/putPassWordByLogin")
	public ApiResult<?> putPassWordByLogin(LoginRequestDto loginRequestDto) throws Exception {
		loginRequestDto.setPassword(SHA256Util.getUserPassword(loginRequestDto.getPassword()));
		loginRequestDto.setNewPassword(SHA256Util.getUserPassword(loginRequestDto.getNewPassword()));
		return userLoginBosBiz.putPassWordByLogin(loginRequestDto);
	}


	/**
	 * 관리자 로그아웃
	 * @param loginRequestDto
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/login/delLoginData")
	public ApiResult<?> delLoginData(LoginRequestDto loginRequestDto, HttpServletRequest req) throws Exception {
		return userLoginBosBiz.delLoginData(loginRequestDto, req);
	}

	/**
	 * BOS 2차 인증 확인
	 *
	 * @param authCertiNo
	 * @return BaseResponseDto
	 * @throws Exception
	 */
	@ApiOperation(value = "BOS 2차 인증 확인")
	@PostMapping(value = "/admin/login/bosTwoFactorAuthetificationVeriyfy")
	@ApiResponses(value = { @ApiResponse(code = 900, message = "response data : null"),
			@ApiResponse(code = 901, message = "" + "[BOS_TWO_FACTOR_AUTH_NO_DATA] BOS_TWO_FACTOR_AUTH_NO_DATA - 2차인증을 위한 연락처 정보가 없습니다. \n"
					+ "[BOS_TWO_FACTOR_AUTH_SUCCESS] BOS_TWO_FACTOR_AUTH_SUCCESS - 2차인증 성공 \n"
					+ "[BOS_TWO_FACTOR_AUTH_FAIL] BOS_TWO_FACTOR_AUTH_FAIL - 잘못된 2차인증 코드 입니다. \n"
					+ "[BOS_TWO_FACTOR_AUTH_FIVE_FAIL] BOS_TWO_FACTOR_AUTH_FIVE_FAIL - 2차인증 입력이 5회 실패되어 로그인페이지로 돌아갑니다. \n"
					+ "[LOGIN_BOS_TWO_FACTOR_AUTH_FAIL] LOGIN_BOS_TWO_FACTOR_AUTH_FAIL - 2차 인증 진행이 실패하였습니다. \n"
					+ "[NEED_LOGIN] 0001 - 로그인필요 \n") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "authCertiNo", value = "2차인증 코드", dataType = "String") })
	public ApiResult<?> bosTwoFactorAuthetificationVeriyfy(@RequestParam(value = "authCertiNo", required = true) String authCertiNo) throws Exception {
		return userLoginBosBiz.bosTwoFactorAuthentificationVeriyfy(authCertiNo);
	}
}
