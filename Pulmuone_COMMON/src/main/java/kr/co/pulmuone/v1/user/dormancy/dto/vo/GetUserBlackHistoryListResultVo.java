package kr.co.pulmuone.v1.user.dormancy.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@Setter
@ToString
@ApiModel(description = "GetUserBlackHistoryListResultVo")
public class GetUserBlackHistoryListResultVo {

	@ApiModelProperty(value = "")
	private int rowNumber;

	@ApiModelProperty(value = "")
	private long urBuyerBlacklistId;

	@ApiModelProperty(value = "")
	private String reason;

	@ApiModelProperty(value = "")
	private String createDate;

	@ApiModelProperty(value = "")
	private String registerUserName;

	@ApiModelProperty(value = "")
	private String registerUserId;

	@ApiModelProperty(value = "이벤트 제한 여부")
	private String eventLimitTp;

	public String getRegisterUserName() {
		if(!Objects.isNull(registerUserId)){
			return registerUserName + "(" + registerUserId + ")";
		}
		return registerUserName;
	}
}
