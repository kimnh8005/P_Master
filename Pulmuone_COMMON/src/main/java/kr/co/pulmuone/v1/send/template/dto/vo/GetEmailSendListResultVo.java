package kr.co.pulmuone.v1.send.template.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetEmailSendListResultVo {

	@ApiModelProperty(value = "RowNum")
	private long rnum;

	@ApiModelProperty(value = "자동발송키값")
	private String snAutoSendId;

	@ApiModelProperty(value = "템플릿코드")
	private String templateCode;

	@ApiModelProperty(value = "템플릿명")
	private String templateName;

	@ApiModelProperty(value = "사용자 메일 발송여부")
	private String mailSendYn;

	@ApiModelProperty(value = "사용자 SMS 발송여부")
	private String smsSendYn;


}
