package kr.co.pulmuone.v1.send.template.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@ApiModel(description = " AddEmailManualRequestDto")
public class AddEmailManualRequestDto extends BaseRequestDto {

	@ApiModelProperty(value = "보내는이(이름)")
	private String senderName;

	@ApiModelProperty(value = "보내는이(이메일)")
	private String senderMail;

	@ApiModelProperty(value = "발송일자")
	private String reserveDate;

	@ApiModelProperty(value = "제목")
	private String title;

	@ApiModelProperty(value = "내용")
	private String content;

	@ApiModelProperty(value = "첨부파일")
	private String attachment;

	@ApiModelProperty(value = "원본첨부파일명")
	private String originFileName;

	@ApiModelProperty(value = "참조인들")
	private String bcc;

	@ApiModelProperty(value = "수동메일발송아이디")
	private Long snManualEmailId;

}
