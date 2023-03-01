package kr.co.pulmuone.v1.promotion.manage.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.ExhibitApprovalResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "ExhibitApprovalResponseDto")
public class ExhibitApprovalResponseDto  extends BaseResponseDto {

	@ApiModelProperty(value = "기획전 승인 목록 조회 리스트")
	private	List<ExhibitApprovalResultVo> rows;

	@ApiModelProperty(value = "기획전 승인 목록 조회 총 Count")
	private long total;

}
