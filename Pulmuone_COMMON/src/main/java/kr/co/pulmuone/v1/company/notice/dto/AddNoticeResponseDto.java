package kr.co.pulmuone.v1.company.notice.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.company.notice.dto.vo.AddNoticeResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "AddNoticeResponseDto")
public class AddNoticeResponseDto {
	@ApiModelProperty(value = "")
	private AddNoticeResultVo rows;
}
