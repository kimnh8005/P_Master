package kr.co.pulmuone.v1.user.buyer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "PutBuyerRequestDto")
public class PutBuyerRequestDto extends BaseRequestPageDto
{

	@ApiModelProperty(value = "")
	private String urUserId;

	@ApiModelProperty(value = "")
	private String bday;

	@ApiModelProperty(value = "")
	private String gender;

	@ApiModelProperty(value = "")
	private String mobile;

	@ApiModelProperty(value = "")
	private String mail;

	@ApiModelProperty(value = "")
	private String smsYn;

	@ApiModelProperty(value = "")
	private String mailYn;

	@ApiModelProperty(value = "마케팅활용동의여부")
	private String marketingYn;

	@ApiModelProperty(value = "")
	private String pushYn;

	@ApiModelProperty(value = "")
	private String eventJoinYn;

	@ApiModelProperty(value = "")
	private String changeLogArray;

	@ApiModelProperty(value = "")
	private String personalInformationAccessYn;

}
