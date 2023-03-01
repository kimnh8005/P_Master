package kr.co.pulmuone.v1.user.certification.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " GetFindPasswordRequestDto")
public class GetFindPasswordRequestDto extends BaseRequestDto
{
	@ApiModelProperty(value = "회원이름", required = true)
	private String userName;

	@ApiModelProperty(value = "회원명", required = true)
	private String mobile;

	@ApiModelProperty(value = "로그인 아이디", required = true)
	private String LoginId;
}
