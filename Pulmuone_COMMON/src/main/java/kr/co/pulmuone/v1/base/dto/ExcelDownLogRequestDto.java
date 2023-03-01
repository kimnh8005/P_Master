package kr.co.pulmuone.v1.base.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "ExcelDownLogRequestDto")
public class ExcelDownLogRequestDto extends BaseRequestPageDto{


	@ApiModelProperty(value = "엑셀 다운로드 유형 공통코드(EXCEL_DOWN_TP : EMPLOYEE(임직원정보), MALL_MEMBER(회원정보), ORDER(주문정보), EVENT_PARTICIPANT(이벤트참여자정보))")
	String excelDownloadType;

	@ApiModelProperty(value = "접속 IP")
	String ip;

	@ApiModelProperty(value = "다운로드 사유")
	String downloadReason;

	@ApiModelProperty(value = "검색어 타입")
	String searchType;

	@ApiModelProperty(value = "검색 내용")
	String searchValue;

	@ApiModelProperty(value = "다운로드 데이터")
	String excelDownType;

	@ApiModelProperty(value = "시작일")
	String startCreateDate;

	@ApiModelProperty(value = "종료일")
	String endCreateDate;


}
