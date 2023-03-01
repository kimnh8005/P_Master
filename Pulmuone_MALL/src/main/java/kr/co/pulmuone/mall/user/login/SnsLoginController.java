package kr.co.pulmuone.mall.user.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.user.buyer.dto.CallbackSocialLoginResponseDataDto;
import kr.co.pulmuone.v1.user.certification.dto.UserSocialInformationDto;
import kr.co.pulmuone.v1.user.login.dto.SyncAccountRequestDto;
import kr.co.pulmuone.v1.user.login.dto.UnlinkAccountRequestDto;
import kr.co.pulmuone.v1.user.login.service.UserSnsLoginBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Slf4j
/*
 * SpringBootBosApplication.java에 선언하지 말고 Controller에서 필요한 Domain의 Package를
 * Scan해서 사용해야 함 kr.co.pulmuone.mall는 명시해도 되고 안해도 됨
 */

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
 *  1.0    20200716   	 	천혜현            최초작성
 * =======================================================================
 * </PRE>
 */

@ComponentScan(basePackages = { "kr.co.pulmuone.common" })
@RestController
@Api(description = "SNS 간편 로그인")
public class SnsLoginController
{
	@Autowired
	private UserSnsLoginBiz userSnsLoginBiz;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private HttpServletResponse response;

	private ObjectMapper mapper = new ObjectMapper();

	/**
	 * SNS 로그인(네이버)
	 *
	 * @return GetUrlNaverResponseDto
	 * @throws Exception
	 */
	@ApiOperation(value = "SNS 로그인(네이버)")
	@GetMapping(value = "/user/snsLogin/getUrlNaver")
	public ApiResult<?> getUrlNaver() throws Exception
	{
		return userSnsLoginBiz.getUrlNaver(request);
	}

	/**
	 * SNS 로그인 응답 (네이버)
	 *
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "SNS 로그인 응답 (네이버)")
	@GetMapping(value = "/user/snsLogin/callbackNaver")
	public void callbackNaver() throws Exception
	{
		try
		{
			ApiResult<?> snsResult = userSnsLoginBiz.callbackNaver(request, response);

			if(snsResult.getData() == null){
				CallbackSocialLoginResponseDataDto callbackNaverResponseDataDto = new CallbackSocialLoginResponseDataDto();
				snsResult = ApiResult.result(callbackNaverResponseDataDto, snsResult.getMessageEnum());
			}

			response.setContentType("text/html;charset=UTF-8");

			String jsonData = mapper.writeValueAsString(snsResult);

			PrintWriter writer = response.getWriter();
			writer.print("<script>" + "var data = " + jsonData + "; " + "       \r\n" + "window.opener.returnSnsLogin(data);" + "       \r\n" + "</script>");

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * SNS 로그인 응답 (카카오)
	 *
	 * @param userSocialInformationDto
	 * @throws Exception
	 */
	@ApiOperation(value = "SNS 로그인 응답 (카카오)")
	@PostMapping(value = "/user/snsLogin/callbackKakao")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = CallbackSocialLoginResponseDataDto.class),
			@ApiResponse(code = 901, message = "" +
					"[NO_SNS_SYNC] 1701 - SNS 계정연동되어 있는 계정 없음 \n" +
					"[-1] API 통신 실패 \n"
			)
	})
	public ApiResult<?> callbackKakao(UserSocialInformationDto userSocialInformationDto) throws Exception{
		return userSnsLoginBiz.callbackKakao(userSocialInformationDto, request, response);
	}

	/**
	 * SNS 로그인 계정연동
	 *
	 * @param syncAccountRequestDto
	 * @return SyncAccountResponseDto
	 * @throws Exception
	 */
	@ApiOperation(value = "SNS 로그인 계정연동")
	@PostMapping(value = "/user/snsLogin/syncAccount")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = SyncAccountRequestDto.class),
			@ApiResponse(code = 901, message = "" +
					"[NO_SNS_SYNC] 1701 - SNS 계정연동되어 있는 계정 없음 \n"
			)
	})
	public ApiResult<?> syncAccount(SyncAccountRequestDto syncAccountRequestDto) throws Exception
	{
		return userSnsLoginBiz.syncAccount(syncAccountRequestDto, request, response);
	}

	/**
	 * SNS 로그인 계정 연결끊기
	 *
	 * @param unlinkAccountRequestDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "SNS 로그인 계정 연결끊기")
	@PostMapping(value = "/user/snsLogin/unlinkAccount")
	public ApiResult<?> unlinkAccount(UnlinkAccountRequestDto unlinkAccountRequestDto) throws Exception
	{
		return userSnsLoginBiz.unlinkAccount(unlinkAccountRequestDto);
	}

	/**
	 * SNS 로그인 응답 (구글)
	 *
	 * @param userSocialInformationDto
	 * @throws Exception
	 */
	@ApiOperation(value = "SNS 로그인 응답(구글)")
	@PostMapping(value = "/user/snsLogin/callbackGoogle")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = CallbackSocialLoginResponseDataDto.class),
			@ApiResponse(code = 901, message = "" +
					"[NO_SNS_SYNC] 1701 - SNS 계정연동되어 있는 계정 없음 \n" +
					"[-1] API 통신 실패 \n"
			)
	})
	public ApiResult<?> callbackGoogle(UserSocialInformationDto userSocialInformationDto) throws Exception{
		return userSnsLoginBiz.callbackGoogle(userSocialInformationDto, request, response);
	}

	/**
	 * SNS 로그인 응답 (페이스북)
	 *
	 * @param userSocialInformationDto
	 * @throws Exception
	 */
	@ApiOperation(value = "SNS 로그인 응답(페이스북)")
	@PostMapping(value = "/user/snsLogin/callbackFacebook")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = CallbackSocialLoginResponseDataDto.class),
			@ApiResponse(code = 901, message = "" +
					"[NO_SNS_SYNC] 1701 - SNS 계정연동되어 있는 계정 없음 \n" +
					"[-1] API 통신 실패 \n"
			)
	})
	public ApiResult<?> callbackFacebook(UserSocialInformationDto userSocialInformationDto) throws Exception{
		return userSnsLoginBiz.callbackFacebook(userSocialInformationDto, request, response);
	}

	/**
	 * SNS 로그인 응답 (애플)
	 *
	 * @param userSocialInformationDto
	 * @throws Exception
	 */
	@ApiOperation(value = "SNS 로그인 응답(애플)")
	@PostMapping(value = "/user/snsLogin/callbackApple")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = CallbackSocialLoginResponseDataDto.class),
			@ApiResponse(code = 901, message = "" +
					"[NO_SNS_SYNC] 1701 - SNS 계정연동되어 있는 계정 없음 \n" +
					"[-1] API 통신 실패 \n"
			)
	})
	public ApiResult<?> callbackApple(UserSocialInformationDto userSocialInformationDto) throws Exception{
		return userSnsLoginBiz.callbackApple(userSocialInformationDto, request, response);
	}

}
