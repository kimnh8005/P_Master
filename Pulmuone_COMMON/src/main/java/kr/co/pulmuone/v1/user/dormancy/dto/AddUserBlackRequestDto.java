package kr.co.pulmuone.v1.user.dormancy.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "AddUserBlackRequestDto")
public class AddUserBlackRequestDto extends BaseRequestPageDto {


	@ApiModelProperty(value = "")
	private String urUserId;

	@ApiModelProperty(value = "")
	private String userBlackReason;

	@ApiModelProperty(value = "로그인아이디")
	private String loginId;

	@ApiModelProperty(value = "이벤트 제한 여부")
	private String eventLimitTp;

	@Builder
	public AddUserBlackRequestDto(String urUserId, String userBlackReason, String loginId, String eventLimitTp) {
		this.urUserId = urUserId;
		this.userBlackReason = userBlackReason;
		this.loginId = loginId;
		this.eventLimitTp = eventLimitTp;
	}
}
