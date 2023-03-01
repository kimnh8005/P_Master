package kr.co.pulmuone.v1.promotion.pointhistory.dto;

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
@ApiModel(description = "PointHistoryListRequestDto")
public class PointHistoryListRequestDto extends BaseRequestPageDto{

	@ApiModelProperty(value = "아이디/회원 조회값")
	private String condiValue;

	@ApiModelProperty(value = "아이디/회원 타입")
	private String condiType;

	@ApiModelProperty(value = "구분")
	private String searchPaymentType;

	@ApiModelProperty(value = "상세구분")
	private String searchPointDetailType;

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

	@ApiModelProperty(value = "엑셀 다운로드 타입")
	private String excelDownType;
}
