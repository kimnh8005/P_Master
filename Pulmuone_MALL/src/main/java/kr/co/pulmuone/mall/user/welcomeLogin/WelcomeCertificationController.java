package kr.co.pulmuone.mall.user.welcomeLogin;

import io.swagger.annotations.*;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.user.certification.dto.GetLoginResponseDataDto;
import kr.co.pulmuone.v1.user.welcomeLogin.WelcomeCertificationBiz;
import kr.co.pulmuone.v1.user.welcomeLogin.dto.GetEmployeeWelcomeLoginDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@ComponentScan(basePackages = {"kr.co.pulmuone.common"})
@RestController
@Api(description = "임직원 오픈 (CBT) 로그인 프로세스")
public class WelcomeCertificationController {

    private static final Logger log = LoggerFactory.getLogger(WelcomeCertificationController.class);

    @Autowired
    WelcomeCertificationBiz welcomeCertificationBiz;

    /**
     * 임직원 인증
     *
     * @param getEmployeeWelcomeLoginDto
     * @return BaseResponseDto
     * @throws Exception
     */
    @ApiOperation(value = "임직원 인증")
    @PostMapping(value = "/user/welcomeLogin/employeesCertification")
    @ApiResponses(value = {@ApiResponse(code = 900, message = "response data", response = GetLoginResponseDataDto.class),
            @ApiResponse(code = 901, message = "" + "[ALREADY_EMPLOYEE_CERTIFI_DONE] 1401 - 이미 임직원인증을 완료 \n")})
    public ApiResult<?> employeesCertification(GetEmployeeWelcomeLoginDto getEmployeeWelcomeLoginDto) throws Exception {
        return welcomeCertificationBiz.employeesCertification(getEmployeeWelcomeLoginDto);
    }

    /**
     * 임직원 인증 확인
     *
     * @param certificationCode
     * @return BaseResponseDto
     * @throws Exception
     */
    @ApiOperation(value = "임직원 인증확인")
    @PostMapping(value = "/user/welcomeLogin/employeesCertificationVeriyfy")
    @ApiResponses(value = {@ApiResponse(code = 900, message = "response data : null"),
            @ApiResponse(code = 901, message = "" + "[FAIL_EMPLOYEE_CERTIFI_CODE] 1402 - 유효하지 않은 인증코드 \n"
                    + "[NEED_LOGIN] 0001 - 로그인필요 \n" + "[OVER5_FAIL_CERTIFI_CODE] 1403 - 5회 연속 실패 \n")})
    @ApiImplicitParams({@ApiImplicitParam(name = "certificationCode", value = "인증코드", dataType = "String")})
    public ApiResult<?> employeesCertificationVeriyfy(@RequestParam(value = "certificationCode", required = true) String certificationCode,
                                                      HttpServletResponse response) throws Exception {
        return welcomeCertificationBiz.employeesCertificationVeriyfy(response, certificationCode);
    }

}
