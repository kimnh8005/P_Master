package kr.co.pulmuone.v1.customer.notice.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.customer.notice.dto.vo.GetNoticeListByUserResultVo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
@ApiModel(description = "공지사항 리스트 ResponseDto")
public class GetNoticeListByUserResponseDto {

	@ApiModelProperty(value = "공지사항 리스트 ResultList")
	private List<GetNoticeListByUserResultVo> notice;

	@ApiModelProperty(value = "total")
	private long total;

}
