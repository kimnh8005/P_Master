package kr.co.pulmuone.v1.user.employee.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "관리자 회원 공급처/출고처 권한 Vo")
public class EmployeeAuthVo {

// [S] UR_EMPLOYEE_AUTH 컬럼
	@ApiModelProperty(value = "관리자 회원 공급처/출고처 권한 ID")
    private String urEmployeeAuthId;

    @ApiModelProperty(value = "회원 코드")
    private String urEmployeeCd;

	@ApiModelProperty(value = "S: 공급업체, W: 출고처")
	private String authIdTp;

    @ApiModelProperty(value = "S: 공급업체 ID, W: 출고처 ID")
    private String authId;

	@ApiModelProperty(value = "등록자 ID")
	private String createId;

	@ApiModelProperty(value = "등록일")
	private String createDt;
// [E] UR_EMPLOYEE_AUTH 컬럼

// 추가 정보
    @ApiModelProperty(value = "S: 공급업체명, W: 출고처명")
	private String authName;

}
