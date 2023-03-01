package kr.co.pulmuone.v1.send.template.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = " PutEmailSendRequestDto")
public class PutEmailSendRequestDto extends BaseRequestDto {

	@ApiModelProperty(value = "자동발송키값")
	private String snAutoSendId;

	@ApiModelProperty(value = "템플릿코드")
	private String templateCode;

	@ApiModelProperty(value = "템플릿명")
	private String templateName;

	@ApiModelProperty(value = "메일제목")
	private String mailTitle;

	@ApiModelProperty(value = "메일내용")
	private String mailBody;

	@ApiModelProperty(value = "문자내용")
	private String smsBody;

	@ApiModelProperty(value = "사용자 메일 발송여부")
	private String mailSendYn;

	@ApiModelProperty(value = "사용자 SMS 발송여부")
	private String smsSendYn;

}
