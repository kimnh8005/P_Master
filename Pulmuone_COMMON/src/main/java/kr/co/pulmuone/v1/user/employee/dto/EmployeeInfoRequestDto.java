package kr.co.pulmuone.v1.user.employee.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import kr.co.pulmuone.v1.system.auth.dto.vo.AuthUserRoleTypeVo;
import kr.co.pulmuone.v1.user.employee.dto.vo.EmployeeAuthVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "BOS 계정관리 등록 & 수정 Request")
public class EmployeeInfoRequestDto extends BaseRequestDto{

    @ApiModelProperty(value = "관리자유형")
    private String adminType;

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

    @ApiModelProperty(value = "이름")
    private String userName;

    @ApiModelProperty(value = "이메일")
    private String email;

    @ApiModelProperty(value = "법인정보명")
    private String regalName;

    @ApiModelProperty(value = "휴대폰번호")
    private String mobile;

    @ApiModelProperty(value = "직책정보명")
    private String positionName;

    @ApiModelProperty(value = "조직정보명")
    private String organizationName;

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

    @ApiModelProperty(value = "권한설정 공급업체 Id 리스트")
    private List<EmployeeAuthVo> authSupplierIdList;

    @ApiModelProperty(value = "권한설정 출고처 Id 리스트")
    private List<EmployeeAuthVo> authWarehouseIdList;

    @ApiModelProperty(value = "권한설정 공급업체/출고처 변경여부")
    private String isAuthListChanged;

    @ApiModelProperty(value = "공급업체 권한 부여일시")
    private String authSupplierDate;

    @ApiModelProperty(value = "권한 역할 리스트")
    List<AuthUserRoleTypeVo> roleList;

    @ApiModelProperty(value = "itsm ID")
    private String itsmId;

    @ApiModelProperty(value = "itsm ID - 개인정보접근권한")
    private String privacyItsmId;

}
