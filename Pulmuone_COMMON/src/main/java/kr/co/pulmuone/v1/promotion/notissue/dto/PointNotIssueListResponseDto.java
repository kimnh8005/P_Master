package kr.co.pulmuone.v1.promotion.notissue.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.promotion.notissue.dto.vo.PointNotIssueListVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "PointNotIssueListResponseDto")
public class PointNotIssueListResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "적립금 미지급 내역  조회 리스트")
	private	List<PointNotIssueListVo> rows;

	@ApiModelProperty(value = "적립금 미지급 내역  총 Count")
	private long total;
}
