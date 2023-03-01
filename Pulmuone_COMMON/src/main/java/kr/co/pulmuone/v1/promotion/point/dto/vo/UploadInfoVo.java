package kr.co.pulmuone.v1.promotion.point.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@ApiModel(description = "UploadInfoVo")
public class UploadInfoVo {

	@ApiModelProperty(value = "회원 ID")
	private String urUserId;

	@ApiModelProperty(value = "난수번호")
	private String serialNumber;

	@ApiModelProperty(value = "회원 Login ID")
	private String loginId;

	@ApiModelProperty(value = "총적립금")
	private int amount;

	@ApiModelProperty(value = "회원명")
	private String userName;

	@ApiModelProperty(value = "역할 총적립금")
	private int roleValidityAmount;

	@ApiModelProperty(value = "사용 적립금")
	private int useAmount;

	@ApiModelProperty(value = "승인 적립금")
	private int issueVal;

	@ApiModelProperty(value = "업로드한 포인트")
	private String uploadIssueValue;

}
