package kr.co.pulmuone.v1.user.buyer.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingLoginId;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetUserChangeHistoryResultVo")
public class GetUserChangeHistoryResultVo {

	@ApiModelProperty(value = "")
	private int rowNumber;

	@ApiModelProperty(value = "")
	private long urChangeLogId;

	@ApiModelProperty(value = "")
	private long urUserId;

	@ApiModelProperty(value = "")
	@UserMaskingUserName
	private String userName;

	@ApiModelProperty(value = "")
	@UserMaskingLoginId
	private String loginId;

	@ApiModelProperty(value = "")
	private String columnLabel;

	@ApiModelProperty(value = "")
	private String beforeData;

	@ApiModelProperty(value = "")
	private String afterData;

	@ApiModelProperty(value = "")
	private String columnName;

	@ApiModelProperty(value = "")
	private String changeUserName;

	@ApiModelProperty(value = "")
	private String changedDate;

}
