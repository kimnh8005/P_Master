package kr.co.pulmuone.v1.user.certification.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PasswordModifyDto {
    @ApiModelProperty(value = "회원아이디")
    private String urUserId;

    @ApiModelProperty(value = "비밀번호 변경 보안 코드")
    private String passwordChangeCd;

    @ApiModelProperty(value = "비밀번호")
    private String password;
}
