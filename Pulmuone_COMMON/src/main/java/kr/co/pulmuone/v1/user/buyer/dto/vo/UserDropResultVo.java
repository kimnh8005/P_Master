package kr.co.pulmuone.v1.user.buyer.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "회원탈퇴 결과 VO")
public class UserDropResultVo extends BaseRequestDto{

	@ApiModelProperty(value = "탈퇴회원정보 PK", hidden = true)
    private Long urUserDropId;

	@ApiModelProperty(value="회원코드")
	private Long urUserId;

	@ApiModelProperty(value = "회원아이디")
	private String loginId;

	@ApiModelProperty(value = "이메일")
	private String mail;

	@ApiModelProperty(value = "탈퇴일시")
	private String createDate;

}
