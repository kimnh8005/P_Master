package kr.co.pulmuone.v1.approval.auth.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "업무별 승인 관리 Dto")
public class ApprovalAuthByTaskDto{

	@ApiModelProperty(value = "업무별 승인 관리 대상 업무 목록 리스트")
	private	List<ApprovalAuthByTaskInfoDto> rows;

}
