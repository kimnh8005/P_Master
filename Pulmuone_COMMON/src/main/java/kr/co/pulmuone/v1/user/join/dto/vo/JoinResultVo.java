package kr.co.pulmuone.v1.user.join.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(description = "회원가입 완료 정보 Vo")
public class JoinResultVo extends BaseRequestDto{

	@ApiModelProperty(value = "회원코드")
	private String urUserId;

	@ApiModelProperty(value = "이메일")
	private String mail;

	@ApiModelProperty(value = "모바일 전화번호")
	private String mobile;

	@ApiModelProperty(value = "가입일시")
	private String createDate;

	@ApiModelProperty(value = "회원아이디")
	private String loginId;

	@ApiModelProperty(value = "이메일수신여부")
	private String mailYn;

	@ApiModelProperty(value = "SMS수신여부")
	private String smsYn;

	@ApiModelProperty(value = "탈퇴시점의 회원상태 공통코드")
	private String status;


}
