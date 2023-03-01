package kr.co.pulmuone.v1.customer.qna.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.customer.qna.dto.vo.QnaBosDetailVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "QnaBosDetailResponseDto")
public class QnaBosDetailResponseDto {

	@ApiModelProperty(value = "통합몰문의관리 상세정보")
	private	QnaBosDetailVo row;

	@ApiModelProperty(value = "통합몰문의관리 리스트")
	private	List<QnaBosDetailVo> rows;
}
