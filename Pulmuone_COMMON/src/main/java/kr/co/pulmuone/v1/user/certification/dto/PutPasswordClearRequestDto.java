package kr.co.pulmuone.v1.user.certification.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "PutPasswordClearRequestDto")
public class PutPasswordClearRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "유지시퀀스아이디")
	private String urUserId;

	@ApiModelProperty(value = "임시비밀번호발급 타입")
	private String putPasswordType;

	@ApiModelProperty(value = "모바일")
	private String mobile;

	@ApiModelProperty(value = "메일")
	private String mail;

	@ApiModelProperty(value = "임시비밀번호발급")
	private String password;

	@ApiModelProperty(value = "임시비밀번호발급")
	private String loginId;

	@ApiModelProperty(value = "사용자 타입(buyer:구매자, employee:임직원)")
	private String userType;

}
