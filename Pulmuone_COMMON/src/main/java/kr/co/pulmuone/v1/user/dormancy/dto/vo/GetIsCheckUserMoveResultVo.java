package kr.co.pulmuone.v1.user.dormancy.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetIsCheckUserMoveResultVo")
public class GetIsCheckUserMoveResultVo
{
	@ApiModelProperty(value = "이동유저아이디")
	private long urUserMoveId;
}
