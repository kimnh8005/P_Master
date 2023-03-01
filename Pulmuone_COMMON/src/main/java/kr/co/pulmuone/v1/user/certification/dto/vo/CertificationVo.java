package kr.co.pulmuone.v1.user.certification.dto.vo;

import java.sql.Timestamp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "로그인 인증 정보 ParamVo")
public class CertificationVo extends BaseRequestDto{

	@ApiModelProperty(value = "로그인 인증 정보 ID")
	private Long certificationId;

	@ApiModelProperty(value = "회원 ID")
	private Long userId;

	@ApiModelProperty(value = "로그인 ID")
	private String loginId;

	@ApiModelProperty(value = "비밀번호")
	private String password;

	@ApiModelProperty(value = "비밀번호 변경을 위한 보안코드")
	private String passwordChangeCode;

	@ApiModelProperty(value = "자동로그인을 위한 인증키 ")
	private String autoLoginKey;

	@ApiModelProperty(value = "비밀번호 실패 횟수")
	private int failCount;

	@ApiModelProperty(value = "비밀번호 변경 일자")
	private Timestamp passwordChangeDate;

	@ApiModelProperty(value = "임시비밀번호 여부")
	private String tempPasswordYn;

}
