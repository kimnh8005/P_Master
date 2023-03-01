package kr.co.pulmuone.v1.send.template.dto;


import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@ApiModel(description = " AddSmsManualRequestDto")
public class AddSmsManualRequestDto extends BaseRequestDto {

	@ApiModelProperty(value = "내용")
	private String content;

	@ApiModelProperty(value = "발송일자")
	private String reserveDate;

	@ApiModelProperty(value = "보내는이전화번호")
	private String senderTelephone;

	@ApiModelProperty(value = "첨부파일")
	private String attachment;

	@ApiModelProperty(value = "수동발생키값")
	private String snManualSmsId;

}
