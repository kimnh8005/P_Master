package kr.co.pulmuone.v1.user.login.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "LoginResponseDto")
public class LoginResponseDto {

	@ApiModelProperty(value = "UserVo")
	private UserVo userVo;

	@ApiModelProperty(value = "회원상태")
	private String statusType;

	@ApiModelProperty(value = "이차인증 적용 여부")
	private String twoFactorAuthYn;
}
