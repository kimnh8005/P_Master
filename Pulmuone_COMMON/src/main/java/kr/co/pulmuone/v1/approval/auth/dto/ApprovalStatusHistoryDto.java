package kr.co.pulmuone.v1.approval.auth.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.approval.auth.dto.vo.ApprovalStatusVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "업무별 승인내역 이력 Dto")
public class ApprovalStatusHistoryDto{

	@ApiModelProperty(value = "업무별 승인내역 이력 리스트")
	private	List<ApprovalStatusVo> rows;

}
