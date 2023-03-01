package kr.co.pulmuone.v1.company.notice.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "AddNoticeAttachParamDto")
public class AddNoticeAttachParamDto extends BaseRequestDto {

	@ApiModelProperty(value = "")
	String filePath;

	@ApiModelProperty(value = "")
	String physicalAttachment;

	@ApiModelProperty(value = "")
	String realAttachment;

	@ApiModelProperty(value = "")
	String csCompanyBbsId;

}
