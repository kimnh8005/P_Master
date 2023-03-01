package kr.co.pulmuone.batch.erp.domain.model.user.employee.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.batch.erp.domain.model.base.dto.vo.AuthUserRoleTypeVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "회원정보 Vo")
public class EmployeeBatchVo {

    @ApiModelProperty(value = "관리자유형")
    private String adminType;

    @ApiModelProperty(value = "관리자유형명")
    private String adminTypeName;

	@ApiModelProperty(value = "사번")
	private String employeeNumber;

    @ApiModelProperty(value = "로그인 ID")
    private String loginId;

	@ApiModelProperty(value = "회원 ID")
	private Long userId;

	@ApiModelProperty(value = "회사 ID")
	private Long companyId;

    @ApiModelProperty(value = "거래처명")
    private String clientName;

    @ApiModelProperty(value = "최초 가입일시")
    private String firstJoinDate;

    @ApiModelProperty(value = "최근 접속일시")
    private String lastVisitDate;

    @ApiModelProperty(value = "회원상태")
    private String userStatus;

    @ApiModelProperty(value = "회원상태명")
    private String userStatusName;

	@ApiModelProperty(value = "이름")
	private String userName;

	@ApiModelProperty(value = "이메일")
	private String email;

    @ApiModelProperty(value = "법인정보명")
    private String regalName;

    @ApiModelProperty(value = "법인정보코드")
    private String regalCode;

	@ApiModelProperty(value = "휴대폰번호")
	private String mobile;

    @ApiModelProperty(value = "직책정보명")
    private String positionName;

    @ApiModelProperty(value = "조직정보명")
    private String organizationName;

    @ApiModelProperty(value = "조직정보코드")
    private String organizationCode;

    @ApiModelProperty(value = "조직장 여부")
    private String teamLeaderYn;

    @ApiModelProperty(value = "개인정보 열람권한유무")
    private String personalInfoAccessYn;

    @ApiModelProperty(value = "열람권한 부여일시")
    private String accessAuthGrantDate;

    @ApiModelProperty(value = "권한 위임정보 담당자명")
    private String grantAuthEmployeeName;

    @ApiModelProperty(value = "권한 위임정보 담당자 사번")
    private String grantAuthEmployeeNumber;

    @ApiModelProperty(value = "권한 위임기간 시작일자")
    private String grantAuthDateStart;

    @ApiModelProperty(value = "권한 위임기간 종료일자")
    private String grantAuthDateEnd;

    @ApiModelProperty(value = "권한 위임 중지 여부")
    private String grantAuthStopYn;

    @ApiModelProperty(value = "공급업체 Id")
    private Long authSupplierId;

    @ApiModelProperty(value = "공급업체 권한 부여일시")
    private String authSupplierDate;

    @ApiModelProperty(value = "등록자 ID")
    private String createId;

    @ApiModelProperty(value = "등록일")
    private String createDate;

    @ApiModelProperty(value = "회원구분")
    private String userType;

    @ApiModelProperty(value = "ERP 임직원 재직상태")
    private String erpUserStatus;

    @ApiModelProperty(value = "권한 역할 리스트")
    List<AuthUserRoleTypeVo> roleList;

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

}
