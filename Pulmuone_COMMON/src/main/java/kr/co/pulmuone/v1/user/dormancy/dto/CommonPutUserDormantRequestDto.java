package kr.co.pulmuone.v1.user.dormancy.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "공통 휴면회원 전환 RequestDto")
public class CommonPutUserDormantRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "회원 ID")
	private String urUserId;

	@ApiModelProperty(value = "휴면회원정보 PK")
	private String urUserMoveId;

}
