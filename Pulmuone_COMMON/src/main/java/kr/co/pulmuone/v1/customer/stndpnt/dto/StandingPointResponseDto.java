package kr.co.pulmuone.v1.customer.stndpnt.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.customer.stndpnt.dto.vo.GetStandingPointAttachResultVo;
import kr.co.pulmuone.v1.customer.stndpnt.dto.vo.GetStandingPointListResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StandingPointResponseDto {

	private long total;

	private List<GetStandingPointListResultVo> rows;

	@ApiModelProperty(value = "상품입점상담 관리 상세정보")
	private	GetStandingPointListResultVo row;


	@ApiModelProperty(value = "")
	private GetStandingPointAttachResultVo rowsFile;

}
