package kr.co.pulmuone.v1.user.buyer.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingLoginId;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@Setter
@ToString
@ApiModel(description = "GetBuyerStatusHistoryListResultVo")
public class GetBuyerStatusHistoryListResultVo {

	@ApiModelProperty(value = "")
	private String status;

	@ApiModelProperty(value = "")
	private String reason;

	@ApiModelProperty(value = "")
	private String createDate;

	@ApiModelProperty(value = "")
	@UserMaskingLoginId
	private String createLoginId;

	@ApiModelProperty(value = "")
	@UserMaskingUserName
	private String createLoginName;

	public String getCreateLoginId() {
		if(!Objects.isNull(createLoginName)){
			return createLoginName + "(" + createLoginId + ")";
		}
		return createLoginId;
	}
}
