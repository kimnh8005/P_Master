package kr.co.pulmuone.v1.system.auth.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetAuthMenuSystemUrlListResultVo")
public class GetRoleMenuAuthListResultVo {

	@ApiModelProperty(value = "역활별 매뉴별 권한 관리 PK")
	private String stRoleMenuAuthMappingId;

	@ApiModelProperty(value = "프로그램 권한 PK")
	private String stProgramAuthId;

	@ApiModelProperty(value = "프로그램 권한 코드")
	private String programAuthCode;

	@ApiModelProperty(value = "프로그램 권한 명")
	private String programAuthCodeName;
}
