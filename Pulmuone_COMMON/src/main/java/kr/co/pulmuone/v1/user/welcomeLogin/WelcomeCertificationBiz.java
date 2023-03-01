package kr.co.pulmuone.v1.user.welcomeLogin;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.user.welcomeLogin.dto.GetEmployeeWelcomeLoginDto;

import javax.servlet.http.HttpServletResponse;

public interface WelcomeCertificationBiz {

    ApiResult<?> employeesCertification(GetEmployeeWelcomeLoginDto getEmployeeWelcomeLoginDto) throws Exception;
    ApiResult<?> employeesCertificationVeriyfy(HttpServletResponse response, String certificationCode) throws Exception;
    ApiResult<?> publishWelcomeToeken(HttpServletResponse response, String employeeNumber) throws Exception;
}