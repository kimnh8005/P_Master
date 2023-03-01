package kr.co.pulmuone.v1.company.notice.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.company.notice.dto.vo.PutNoticeResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "PutNoticeResponseDto")
public class PutNoticeResponseDto extends BaseResponseDto {
	@ApiModelProperty(value = "")
	private PutNoticeResultVo rows;
}
