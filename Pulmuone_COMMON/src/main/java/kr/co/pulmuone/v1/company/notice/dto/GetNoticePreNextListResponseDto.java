package kr.co.pulmuone.v1.company.notice.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.company.notice.dto.vo.GetNoticePreNextListResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "GetNoticePreNextListResponseDto")
public class GetNoticePreNextListResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "")
	private	List<GetNoticePreNextListResultVo> rows;

}
