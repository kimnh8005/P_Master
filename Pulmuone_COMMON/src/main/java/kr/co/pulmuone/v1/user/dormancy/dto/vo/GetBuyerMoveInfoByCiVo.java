package kr.co.pulmuone.v1.user.dormancy.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetBuyerMoveInfoByCiVo")
public class GetBuyerMoveInfoByCiVo
{

	@ApiModelProperty(value = "회원 ID")
	private String urUserId;

	@ApiModelProperty(value = "휴면회원정보 PK")
	private String urUserMoveId;

	@ApiModelProperty(value = "비밀번호 변경 코드")
	private String passwordChangeCd;
}
