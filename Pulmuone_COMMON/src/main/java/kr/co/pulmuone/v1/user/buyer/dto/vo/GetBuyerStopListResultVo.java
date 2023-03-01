package kr.co.pulmuone.v1.user.buyer.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingEmail;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingLoginId;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingMobile;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetBuyerStopListResultVo")
public class GetBuyerStopListResultVo {

	@ApiModelProperty(value = "")
	private int rowNumber;

	@ApiModelProperty(value = "")
	private String employeeYn;

	@ApiModelProperty(value = "")
	@UserMaskingUserName
	private String userName;

	@ApiModelProperty(value = "")
	@UserMaskingLoginId
	private String loginId;

	@ApiModelProperty(value = "")
	@UserMaskingMobile
	private String mobile;

	@ApiModelProperty(value = "")
	@UserMaskingEmail
	private String mail;

	@ApiModelProperty(value = "")
	private String lastLoginDate;

	@ApiModelProperty(value = "")
	private String reason;

	@ApiModelProperty(value = "")
	private String createDate;

	@ApiModelProperty(value = "")
	private String urUserId;

	@ApiModelProperty(value = "")
	private String urBuyerStatusLogId;

}
