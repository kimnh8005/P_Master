package kr.co.pulmuone.v1.user.login.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "LoginRequestDto")
public class LoginRequestDto extends BaseRequestDto {

	@ApiModelProperty(value = "로그인 ID")
	private String loginId;

	@ApiModelProperty(value = "비밀번호")
	private String password;

    @ApiModelProperty(value = "발급회원코드")
    private String urUserId;

    @ApiModelProperty(value = "접속 IP")
    private String addressIp;

    @ApiModelProperty(value = "접속서비스")
    private String service;

	@ApiModelProperty(value = "회원상태")
	private String statusType;

	@ApiModelProperty(value = "회원명")
	private String userName;

	@ApiModelProperty(value = "이메일")
	private String email;

	@ApiModelProperty(value = "새 비밀번호")
	private String newPassword;

	@ApiModelProperty(value = "임시 여부")
	private String temporaryYn;

	@ApiModelProperty(value = "수정자 ID")
	private String modifyId;

	@ApiModelProperty(value = "접속 로그 일련 ID")
	private long urConnectLogId;

	@ApiModelProperty(value = "로그인 성공여부(Y:성공)")
	private String successYn;

	@ApiModelProperty(value = "사용자 환경 정보 PK")
	private String userPcidCd;





}


