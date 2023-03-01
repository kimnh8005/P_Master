package kr.co.pulmuone.v1.customer.feedback.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.customer.feedback.dto.vo.FeedbackBosDetailVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "FeedbackBosDetailResponseDto")
public class FeedbackBosDetailResponseDto extends BaseResponseDto{

	@ApiModelProperty(value = "후기관리 상세정보")
	private	FeedbackBosDetailVo row;

	@ApiModelProperty(value = "후기관리 상세정보 첨부파일 리스트")
	private	List<FeedbackBosDetailVo> rows;

}
