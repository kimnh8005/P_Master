package kr.co.pulmuone.v1.batch.user.dormancy.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "정상회원 휴면전환 RequestDto")
public class ActiveUserDormantRequestDto {

	@ApiModelProperty(value = "회원 PK")
	private Long urUserId;

	@ApiModelProperty(value = "휴면회원정보 PK")
	private Long urUserMoveId;

}
