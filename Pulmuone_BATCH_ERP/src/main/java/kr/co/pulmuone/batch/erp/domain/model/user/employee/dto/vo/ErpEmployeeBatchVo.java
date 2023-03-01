package kr.co.pulmuone.batch.erp.domain.model.user.employee.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
@ApiModel(description = "ERP 임직원 정보")
public class ErpEmployeeBatchVo {

	@ApiModelProperty(value = "사번")
	private String erpEmployeeNumber;

	@ApiModelProperty(value = "직원명")
	private String erpEmployeeName;

    @ApiModelProperty(value = "휴대폰")
    private String erpCellPhone;

    @ApiModelProperty(value = "이메일")
    private String erpEmail;

	@ApiModelProperty(value = "법인코드")
	private String erpRegalCode;

	@ApiModelProperty(value = "법인명")
	private String erpRegalName;

	@ApiModelProperty(value = "조직코드")
	private String erpOrganizationCode;

	@ApiModelProperty(value = "조직명")
	private String erpOrganizationName;

	@ApiModelProperty(value = "직급코드")
	private String erpGradeCode;

	@ApiModelProperty(value = "직급명")
	private String erpGradeName;

	@ApiModelProperty(value = "직책명")
	private String erpPositionName;

    @ApiModelProperty(value = "ERP 상태명")
    private String erpStatusName;

    @ApiModelProperty(value = "사용유무")
    private String useYn;

	@ApiModelProperty(value = "상태코드")
	private String status;

	@ApiModelProperty(value = "상태명")
	private String statusName;

	@ApiModelProperty(value = "등록자")
	private long createId;

	@ApiModelProperty(value = "회계법인코드")
	private String finRegalCd;

	@ApiModelProperty(value = "회계조직코드")
	private String finOrganizationCd;

	@ApiModelProperty(value = "계정과목코드")
	private String finAccountCd;

	@ApiModelProperty(value = "ERP장부ID")
	private String sob;

	@ApiModelProperty(value = "OU코드")
	private String ouId;

	@ApiModelProperty(value = "OU명")
	private String ouName;

	@ApiModelProperty(value = "수동관리여부")
	private String manualYn;

	@ApiModelProperty(value = "수동관리시작일")
	private Date manualStartDt;

	@ApiModelProperty(value = "수동관리종료일")
	private Date manualEndDt;

}
