package kr.co.pulmuone.v1.company.notice.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.company.notice.dto.vo.GetNoticeListResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "GetNoticeListResponseDto")
public class GetNoticeListResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "")
	private	List<GetNoticeListResultVo> rows;

	@ApiModelProperty(value = "")
	private int total;
}
