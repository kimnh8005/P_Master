package kr.co.pulmuone.v1.system.auth.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "getAuthUserInListRequestDto")
public class GetAuthUserInListRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "사용자명")
	private String userName;

	@ApiModelProperty(value = "회사명")
	private String companyName;

	@ApiModelProperty(value = "로그인아이디")
	private String loginId;

	@ApiModelProperty(value = "검색일자")
	private String searchData;

	@ApiModelProperty(value = "역활타입아이디")
	private String stRoleTypeId;

	@ApiModelProperty(value = "이메일")
	private String email;
}
