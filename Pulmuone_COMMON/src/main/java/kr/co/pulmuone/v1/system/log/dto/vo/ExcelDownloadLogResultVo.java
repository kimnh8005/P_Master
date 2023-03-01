package kr.co.pulmuone.v1.system.log.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "엑셀다운로드 로그 Result Vo")
public class ExcelDownloadLogResultVo {

	@ApiModelProperty(value = "엑셀 다운로드 유형 공통코드(EXCEL_DOWN_TP : EMPLOYEE(임직원정보), MALL_MEMBER(회원정보), ORDER(주문정보), EVENT_PARTICIPANT(이벤트참여자정보))")
	private String excelDownloadType;

	@ApiModelProperty(value = "접속 IP")
	private String ip;

	@ApiModelProperty(value = "다운로드 사유")
	private String downloadReason;

	@ApiModelProperty(value = "처리자 ID")
	private long handleUserId;

	@ApiModelProperty(value = "등록일")
	private String createDate;

	@ApiModelProperty(value = "처리자 명")
	private String userName;

	@ApiModelProperty(value = "엑셀 다운로드 유형 공통코드 명")
	private String excelDownloadTypeName;

	@ApiModelProperty(value = "아이디")
	private String loginId;
}
