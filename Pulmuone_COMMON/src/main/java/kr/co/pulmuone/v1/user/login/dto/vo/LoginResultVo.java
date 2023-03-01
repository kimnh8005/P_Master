package kr.co.pulmuone.v1.user.login.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "관리자 회원 로그인 Result")
public class LoginResultVo {

	@ApiModelProperty(value = "발급회원코드")
	private String urUserId;

	@ApiModelProperty(value = "로그인 ID")
	private String	loginId;

	@ApiModelProperty(value = "회원명")
	private String loginName;

	@ApiModelProperty(value = "회원 타입")
	private String userType;

	@ApiModelProperty(value = "회원상태")
	private String statusType;

	@ApiModelProperty(value = "거래처명")
	private String companyName;

	@ApiModelProperty(value = "롤 ID")
	private String roleId;

	@ApiModelProperty(value = "마지막 로그인 일자")
	private String lastLoginDate;

	@ApiModelProperty(value = "비밀변호 변경 일자")
	private String passwordChangeDate;

	@ApiModelProperty(value = "비밀변호 변경 필요 여부")
	private String passwordChangeYn;

	@ApiModelProperty(value = "마지막 로그인 경과 일")
	private int lastLoginElapsedDay;

	@ApiModelProperty(value = "비밀번호 변경 경과 일")
	private int passwordChangeElapsedDay;

	@ApiModelProperty(value = "비밀번호")
	private String password;

	@ApiModelProperty(value = "비밀번호 실패 카운트")
	private int failCount;

	@ApiModelProperty(value="모바일 전화번호")
	private String mobile;

}
