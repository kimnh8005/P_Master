package kr.co.pulmuone.v1.system.auth.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetAuthUsersResultVo")
public class GetAuthUserResultVo {

	@ApiModelProperty(value = "로그인 ID")
	private String loginId;

	@ApiModelProperty(value = "관리자명")
	private String userName;

	@ApiModelProperty(value = "법인명")
	private String regalName;

	@ApiModelProperty(value = "부서명")
	private String organizationName;

	@ApiModelProperty(value = "Email")
	private String email;

	@ApiModelProperty(value = "회원  PK")
	private Long urUserId;

	@ApiModelProperty(value = "역할 그룹 PK")
	private Long stRoleTypeId;

	@ApiModelProperty(value = "회사유형")
	private String compTp;

	@ApiModelProperty(value = "회사유형명")
	private String compTpNm;

	@ApiModelProperty(value = "회사명")
	private String compNm;

	@ApiModelProperty(value = "거래처타입명")
	private String clientTpNm;

}
