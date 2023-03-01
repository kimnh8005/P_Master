package kr.co.pulmuone.v1.user.buyer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.user.certification.dto.GetClauseArrayRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " AddBuyerRequestDto")
public class AddBuyerRequestDto
{
	@ApiModelProperty(value = "로그인아이디", required = true)
	private String loginId;

	@ApiModelProperty(value = "비밀번호", required = true)
	private String password;

	@ApiModelProperty(value = "메일", required = true)
	private String mail;

	@ApiModelProperty(value = "우편번호", required = true)
	private String zipCode;

	@ApiModelProperty(value = "주소", required = true)
	private String address1;

	@ApiModelProperty(value = "상세주소", required = true)
	private String address2;

	@ApiModelProperty(value = "건물관리번호", required = true)
	private String buildingCode;

	@ApiModelProperty(value = "추천인아이디")
	private String recommendLoginId;

	@ApiModelProperty(value = "이메일동의여부", required = true)
	private String smsYn;

	@ApiModelProperty(value = "메일동의여부", required = true)
	private String mailYn;

	@ApiModelProperty(value = "마케팅 활용 동의 여부", required = true)
	private String marketingYn;

	@ApiModelProperty(value = "약관동의리스트")
	private GetClauseArrayRequestDto[] clause;

	@ApiModelProperty(value="회원코드 pk", hidden = true)
	private String urUserId;

	@ApiModelProperty(value="유입사이트코드 (풀샵-0000000000, 올가-0000200000)", hidden = true)
	private String siteNo;

	@ApiModelProperty(value="PCID CD", hidden = true)
	private String urPcidCd;

}
