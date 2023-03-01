package kr.co.pulmuone.v1.approval.auth.dto;

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
@ApiModel(description = "ApprovalCsRefundRequestDto")
public class ApprovalCsRefundRequestDto extends BaseRequestPageDto{

	@ApiModelProperty(value = "단일조건 _ 복수조건 검색")
	private String selectConditionType;

	@ApiModelProperty(value = "기간검색유형")
    private String dateSearchType;

    @ApiModelProperty(value = "기간검색 시작일자")
    private String dateSearchStart;

    @ApiModelProperty(value = "기간검색 종료일자")
    private String dateSearchEnd;

	@ApiModelProperty(value = "승인요청자 아이디/회원 타입")
	private String searchApprReqUserType;

	@ApiModelProperty(value = "승인요청자 아이디/회원 조회값")
	private String searchApprReqUser;

	@ApiModelProperty(value = "승인처리자 아이디/회원 타입")
	private String searchApprChgUserType;

	@ApiModelProperty(value = "승인처리자 아이디/회원 조회값")
	private String searchApprChgUser;

	@ApiModelProperty(value = "승인상태")
	private String searchApprovalStatus;

	@ApiModelProperty(value = "승인상태Array", required = false)
    private List<String> approvalStatusArray;

    @ApiModelProperty(value = "CS환불 구분")
    private String csRefundTp;

    @ApiModelProperty(value = "CS환불 구분 리스트")
    private List<String> csRefundTpList;

    @ApiModelProperty(value = "복수조건 검색조건")
	private String searchMultiType;

	@ApiModelProperty(value = "복수조건 검색어")
	private String findKeyword;

    @ApiModelProperty(value = "단일조건 검색조건")
	private String searchSingleType;

	@ApiModelProperty(value = "단일조건 검색어")
	private String codeSearch;

    @ApiModelProperty(value = "단일조건 검색어 리스트")
    private ArrayList<String> codeSearchList;

	@ApiModelProperty(value = "승인상태")
	private String apprStat;

	@ApiModelProperty(value = "클래임 환불 PK 목록")
	private List<String> odCsIdList;

	@ApiModelProperty(value = "승인처리 메세지")
	private String statusComment;
}
