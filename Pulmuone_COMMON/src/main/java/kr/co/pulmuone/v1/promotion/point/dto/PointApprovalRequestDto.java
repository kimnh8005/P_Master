package kr.co.pulmuone.v1.promotion.point.dto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "PointApprovalRequestDto")
public class PointApprovalRequestDto extends BaseRequestPageDto{

	@ApiModelProperty(value = "적립금명")
	private String searchPointName;

	@ApiModelProperty(value = "설정구분")
	private String searchPointType;

	@ApiModelProperty(value = "승인상태")
	private String searchApprovalStatus;

	@ApiModelProperty(value = "승인상태Array", required = false)
    private ArrayList<String> approvalStatusArray;

	@ApiModelProperty(value = "승인요청일자 검색 시작일")
	private String approvalReqStartDate;

	@ApiModelProperty(value = "승인요청일자 검색 종료일")
	private String approvalReqEndDate;

	@ApiModelProperty(value = "승인처리일자 검색 시작일")
	private String approvalChgStartDate;

	@ApiModelProperty(value = "승인처리일자 검색 종료일")
	private String approvalChgEndDate;

	@ApiModelProperty(value = "적립금 마스터 상태")
	private String pointMasterStat;

	@ApiModelProperty(value = "승인상태")
	private String apprStat;

	@ApiModelProperty(value = "승인처리자 단계")
	private String apprStep;

	@ApiModelProperty(value = "승인요청자 아이디/회원 타입")
	private String searchApprReqUserType;

	@ApiModelProperty(value = "승인요청자 아이디/회원 조회값")
	private String searchApprReqUser;

	@ApiModelProperty(value = "승인처리자 아이디/회원 타입")
	private String searchApprChgUserType;

	@ApiModelProperty(value = "승인처리자 아이디/회원 조회값")
	private String searchApprChgUser;

	@ApiModelProperty(value = "적립금 ID")
	private String pmPointId;

	@ApiModelProperty(value = "적립금승인.적립금 ID 목록")
	private List<String> pmPointIdList;

	@ApiModelProperty(value = "승인처리 메세지")
	private String statusComment;

}
