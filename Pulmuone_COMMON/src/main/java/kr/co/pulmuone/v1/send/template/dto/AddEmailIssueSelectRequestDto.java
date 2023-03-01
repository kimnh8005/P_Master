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
@ApiModel(description = " AddEmailIssueSelectRequestDto")
public class AddEmailIssueSelectRequestDto extends BaseRequestDto {

	@ApiModelProperty(value = "보내는이(이름)")
	private String senderName;

	@ApiModelProperty(value = "보내는이(이메일)")
	private String senderMail;

	@ApiModelProperty(value = "발송일자")
	private String reserveDate;

	@ApiModelProperty(value = "메일제목")
	private String title;

	@ApiModelProperty(value = "메일내용")
	private String content;

	@ApiModelProperty(value = "첨부파일")
	private String attachment;

	@ApiModelProperty(value = "원본첨부파일명")
	private String originFileName;

	@ApiModelProperty(value = "참조인들")
	private String bcc;

	@ApiModelProperty(value = "수동메일아이디")
	private Long snManualEmailId;

	@ApiModelProperty(value = "메일보관함")
	private String snMailTemplateId;

	@ApiModelProperty(value = "주소록인덱스값")
	private String snAddressId;

	@ApiModelProperty(value = "자동발송인덱스값")
	private String snAutoSendId;

	@ApiModelProperty(value = "N: 즉시발송 Y: 예약발송")
	private String reserveYn;

	@ApiModelProperty(value = "회원코드")
	private String urUserId;

	@ApiModelProperty(value = "받는사람메일주소")
	private String mail;

}
