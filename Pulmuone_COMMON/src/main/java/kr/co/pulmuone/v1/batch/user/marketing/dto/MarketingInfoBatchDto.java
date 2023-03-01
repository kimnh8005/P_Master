package kr.co.pulmuone.v1.batch.user.marketing.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "마케팅 정보 수신동의 대상자 DTO")
public class MarketingInfoBatchDto{

	@ApiModelProperty(value = "회원 ID")
	private Long urUserId;

	@ApiModelProperty(value = "이름")
	private String userName;

	@ApiModelProperty(value = "이메일")
	private String mail;

	@ApiModelProperty(value = "휴대폰번호")
	private String mobile;

	@ApiModelProperty(value = "메일 수신동의여부")
    private String mailYn;

    @ApiModelProperty(value = "sms 수신동의여부")
    private String smsYn;

    @ApiModelProperty(value = "메일 수신동의일자")
    private String mailYnDate;

    @ApiModelProperty(value = "sms 수신동의일자")
    private String smsYnDate;

    @ApiModelProperty(value = "mail 수신동의정보")
    private String mailInfo;

    @ApiModelProperty(value = "sms 수신동의정보")
    private String smsInfo;

}
