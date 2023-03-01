package kr.co.pulmuone.v1.approval.auth.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.approval.auth.dto.vo.ApprovalAuthManagerVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "업무별 승인 관리 대상 업무 Dto")
public class ApprovalAuthByTaskInfoDto{

	@ApiModelProperty(value = "승인권한관리 대상 업무 코드")
	private String taskCode;

	@ApiModelProperty(value = "승인권한관리 대상 업무 코드 명")
	private String taskName;

	@ApiModelProperty(value = "승인요청 로그인 아이디")
	private String apprReqLoginId;

	@ApiModelProperty(value = "승인요청 아이디")
	private String apprReqUsrId;

	@ApiModelProperty(value = "업무별 1차 승인 관리자 필수 여부")
	private boolean subRequired;

	@ApiModelProperty(value = "업무별 1차 승인관리자 목록")
	private	List<ApprovalAuthManagerVo> authManager1stList;

	@ApiModelProperty(value = "업무별 2차 승인관리자 목록")
	private	List<ApprovalAuthManagerVo> authManager2ndList;

}
