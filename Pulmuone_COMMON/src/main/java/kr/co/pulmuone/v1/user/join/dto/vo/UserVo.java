package kr.co.pulmuone.v1.user.join.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "회원기본정보 Vo")
public class UserVo extends BaseRequestDto{

	@ApiModelProperty(value = "회원 ID")
	private Long userId;

	@ApiModelProperty(value = "로그인 ID")
	private String loginId;

	@ApiModelProperty(value = "이름")
	private String userName;

	@ApiModelProperty(value = "회원구분")
	private String userType;

	@ApiModelProperty(value = "회원상태")
	private String userStatus;

}
