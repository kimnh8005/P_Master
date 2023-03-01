package kr.co.pulmuone.v1.user.certification.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetBuyerSessionDataResultVo")
public class GetBuyerSessionDataResultVo
{
	@ApiModelProperty(value = "유저아이디")
	private String urUserId;

	@ApiModelProperty(value = "로그인아이디")
	private String loginId;

	@ApiModelProperty(value = "회원이름")
	private String userName;

	@ApiModelProperty(value = "임직원 사번 PK")
	private String urErpEmployeeCode;

	@ApiModelProperty(value = "회원 모바일")
	private String userMobile;

	@ApiModelProperty(value = "회원 이메일")
	private String userEmail;

	@ApiModelProperty(value = "그룹 PK")
	private Long urGroupId;

}
