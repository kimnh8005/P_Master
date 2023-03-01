package kr.co.pulmuone.v1.user.welcomeLogin.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "외부접근 제한 임직원 인증")
public class GetEmployeeWelcomeLoginDto {

    private String email; // 이메일 또는 이름
    private String urErpEmployeeCode;
    private String certificationCode;

}