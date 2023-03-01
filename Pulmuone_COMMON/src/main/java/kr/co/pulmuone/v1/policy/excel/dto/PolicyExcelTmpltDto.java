package kr.co.pulmuone.v1.policy.excel.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import kr.co.pulmuone.v1.policy.excel.dto.vo.PolicyExcelTmpltVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "엑셀양식관리 Dto")
public class PolicyExcelTmpltDto extends BaseRequestPageDto{

	@ApiModelProperty(value = "엑셀양식관리 조회 리스트")
	private	List<PolicyExcelTmpltVo> rows;

	@ApiModelProperty(value = "엑셀양식관리 조회 카운트")
	private	int	total;

	@ApiModelProperty(value = "엑셀양식관리.SEQ")
	private String psExcelTemplateId;

	@ApiModelProperty(value = "엑셀양식관리.엑셀 양식 유형 공통코드(EXCEL_TEMPLATE_TP)")
	private String excelTemplateTp;

	@ApiModelProperty(value = "엑셀양식관리.엑셀 양식 사용 유형 공통코드(EXCEL_TEMPLATE_USE_TP)")
	private String excelTemplateUseTp;

	@ApiModelProperty(value = "엑셀양식관리.템플릿명")
	private String templateNm;

	@ApiModelProperty(value = "엑셀양식관리.엑셀설정값")
	private String templateData;

	/*
	 * 11.26 SPEC OUT
	@ApiModelProperty(value = "엑셀양식관리.엑셀데이터 시작행")
	private String startLine;
	 */

	@ApiModelProperty(value = "엑셀양식관리.회원 PK")
	private String urUserId;

	@ApiModelProperty(value = "엑셀양식관리.등록 회사")
	private String urCompanyId;

	@ApiModelProperty(value = "엑셀양식관리.개인 사용 유무(Y: 개인사용)")
	private String personalUseYn;

	@ApiModelProperty(value = "엑셀양식관리.개인정보유여부(Y: 사용)")
	private String privacyIncludeYn;

	@ApiModelProperty(value = "등록자ID")
	private String createId;

	@ApiModelProperty(value = "등록일시")
	private String createDt;

	@ApiModelProperty(value = "수정자ID")
	private String modifyId;

	@ApiModelProperty(value = "수정일시")
	private String modifyDt;


}
