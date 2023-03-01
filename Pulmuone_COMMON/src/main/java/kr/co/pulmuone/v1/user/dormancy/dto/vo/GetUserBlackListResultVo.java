package kr.co.pulmuone.v1.user.dormancy.dto.vo;

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
@ApiModel(description = "GetUserBlackListResultVo")
public class GetUserBlackListResultVo {

	@ApiModelProperty(value = "")
	private int rowNumber;

	@ApiModelProperty(value = "")
	private long urUserId;

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
	private String accumulateCount;

	@ApiModelProperty(value = "")
	private String lastCreateDate;

	@ApiModelProperty(value = "이벤트 제한 여부")
	private String eventLimitTp;

}
