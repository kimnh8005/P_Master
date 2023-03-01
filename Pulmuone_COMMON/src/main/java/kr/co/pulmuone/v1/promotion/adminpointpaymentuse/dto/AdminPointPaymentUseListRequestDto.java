package kr.co.pulmuone.v1.promotion.adminpointpaymentuse.dto;

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
@ApiModel(description = "AdminPointPaymentUseListRequestDto")
public class AdminPointPaymentUseListRequestDto extends BaseRequestPageDto{

	@ApiModelProperty(value = "아이디/회원 조회값")
	private String condiValue;

	@ApiModelProperty(value = "아이디/회원 타입")
	private String condiType;

	@ApiModelProperty(value = "휴대폰/email 타입")
	private String searchType;

	@ApiModelProperty(value = "조회값")
	private String searchValue;

	@ApiModelProperty(value = "처리자")
	private String grantAuthEmployeeNumber;

	@ApiModelProperty(value = "역할")
	private String roleGroup;

	@ApiModelProperty(value = "상세구분")
	private String searchPointType;

	@ApiModelProperty(value = "사유")
	private String pointUsedMsg;

	@ApiModelProperty(value = "검색 시작일")
	private String startDate;

	@ApiModelProperty(value = "검색 종료일")
	private String endDate;

	@ApiModelProperty(value = "")
	private ArrayList<String> condiValueArray;

	@ApiModelProperty(value = "조회 데이터 리스트")
	private List<String> searchValueList;

	@ApiModelProperty(value = "조회 데이터")
	private String inputSearchValue;

	@ApiModelProperty(value = "적립금 세부타입")
	private String pointAdminIssueType;

	@ApiModelProperty(value = "적립금 타입")
	private String pointType;

}
