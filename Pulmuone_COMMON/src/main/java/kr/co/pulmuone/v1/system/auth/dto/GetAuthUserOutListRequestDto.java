package kr.co.pulmuone.v1.system.auth.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetAuthUserOutListRequestDto")
public class GetAuthUserOutListRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "로그인 아이디")
	private String loginId;

	@ApiModelProperty(value = "사용자명")
	private String userName;

	@ApiModelProperty(value = "법인명")
	private String regalName;

	@ApiModelProperty(value = "조직명")
	private String organizationName;

	@ApiModelProperty(value = "이메일")
	private String email;

	@ApiModelProperty(value = "역활타입 시퀀스 아이디")
	private String stRoleTypeId;

	@ApiModelProperty(value = "예외 관리자 PK")
	private String notUrUserIds;

	public String[] getNotUrUserIdList() {
		return this.notUrUserIds.split(",");
	}
}
