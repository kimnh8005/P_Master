package kr.co.pulmuone.v1.customer.notice.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.customer.notice.dto.vo.NoticeBosDetailVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "NoticeBosDetailResponseDto")
public class NoticeBosDetailResponseDto {

	@ApiModelProperty(value = "공지사항 상세정보")
	private	NoticeBosDetailVo row;

	@ApiModelProperty(value = "공지사항 상세정보 첨부파일 리스트")
	private	List<NoticeBosDetailVo> rows;
}
