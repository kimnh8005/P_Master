package kr.co.pulmuone.mall.user.dormancy;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.user.dormancy.service.UserDormancyBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
 *  1.0    20200617   	 	김경민            최초작성
 * =======================================================================
 * </PRE>
 */
@ComponentScan(basePackages = { "kr.co.pulmuone.common" })
@RestController
@Api(description = "휴면회원")
public class UserDormancyController
{
	@Autowired
	private UserDormancyBiz userDormancyBiz;

	/**
	 * 휴면회원 해제
	 *
	 * @param
	 * @throws Exception
	 */
	@ApiOperation(value = "휴면회원 해제")
	@PostMapping(value = "/user/move/putUserActive")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : null"),
			@ApiResponse(code = 901, message = "" +
					"[NOT_ANY_CERTI] 1203 - 본인 인증정보 없음 \n" +
					"[NO_FIND_USER_MOVE] 1501 - 휴면 본인인증 정보 불일치 \n" +
					"[SAME_PASSWORD_NOTI] 1224 - 이전 비밀번호 동일 사용 \n"
			)
	})
	public ApiResult<?> putUserActive(@RequestParam(value = "password", required = true) String password) throws Exception
	{
		return userDormancyBiz.putUserActive(password);
	}
}
