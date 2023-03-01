package kr.co.pulmuone.v1.company.notice.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetNoticeAttachResultVo")
public class GetNoticeAttachResultVo {

	@ApiModelProperty(value = "")
	String csCompanyBbsAttachId;

	@ApiModelProperty(value = "")
	String filePath;

	@ApiModelProperty(value = "")
	String csCompanyBbsId;

	@ApiModelProperty(value = "")
	String physicalAttached;

	@ApiModelProperty(value = "")
	String realAttached;

}
