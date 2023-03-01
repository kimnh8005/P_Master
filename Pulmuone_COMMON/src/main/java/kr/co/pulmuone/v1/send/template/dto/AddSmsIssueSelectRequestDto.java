package kr.co.pulmuone.v1.send.template.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ApiModel(description = " AddSmsIssueSelectRequestDto")
public class AddSmsIssueSelectRequestDto extends BaseRequestDto {

	@ApiModelProperty(value = "내용")
	private String content;

	@ApiModelProperty(value = "발송일자")
	private String reserveDate;

	@ApiModelProperty(value = "보내는이전화번호")
	private String senderTelephone;

	@ApiModelProperty(value = "첨부파일")
	private String attachment;

	@ApiModelProperty(value = "받는이 user_id")
	private String urUserId;

	@ApiModelProperty(value = "받는이 전화번호")
	private String mobile;

	@ApiModelProperty(value = "수동발생키값")
	private String snManualSmsId;

	@ApiModelProperty(value = "자동발생키값")
	private String snAutoSendId;

	@ApiModelProperty(value = "N: 즉시발송 Y: 예약발송")
	private String reserveYn;

	@ApiModelProperty(value = "N: 발송대기 Y: 발송")
	@Builder.Default
	private String sendYn = "N";

}
