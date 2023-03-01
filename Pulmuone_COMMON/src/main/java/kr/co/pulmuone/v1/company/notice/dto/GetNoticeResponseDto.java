package kr.co.pulmuone.v1.company.notice.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.company.notice.dto.vo.GetNoticeAttachResultVo;
import kr.co.pulmuone.v1.company.notice.dto.vo.GetNoticeResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetNoticeResponseDto")
public class GetNoticeResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "")
	private GetNoticeResultVo rows;

	@ApiModelProperty(value = "")
	private GetNoticeAttachResultVo rowsFile;
}
