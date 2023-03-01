package kr.co.pulmuone.v1.user.employee.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.system.auth.dto.vo.AuthUserRoleTypeVo;
import kr.co.pulmuone.v1.user.employee.dto.vo.EmployeeAuthVo;
import kr.co.pulmuone.v1.user.employee.dto.vo.EmployeeVo;
import kr.co.pulmuone.v1.user.join.dto.vo.UserVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "BOS계정관리 Response")
public class EmployeeResponseDto{

	@ApiModelProperty(value = "BOS계정관리 리스트")
	private	List<EmployeeVo> rows;

	@ApiModelProperty(value = "BOS계정관리 총 갯수")
	private long total;

    @ApiModelProperty(value = "회원정보")
    private EmployeeVo employeeInfo;

    @ApiModelProperty(value = "ERP 임직원 정보")
    private EmployeeVo erpEmployeeInfo;

    @ApiModelProperty(value = "회원기본정보")
    private UserVo userInfo;

    @ApiModelProperty(value = "이메일 중복등록여부")
    private boolean emailDuplocate;

    @ApiModelProperty(value = "권한역할 리스트")
    private List<AuthUserRoleTypeVo> authRoleList;

    @ApiModelProperty(value = "공급업체 권한 리스트")
    private List<EmployeeAuthVo> authSupplierList;

    @ApiModelProperty(value = "출고처 권한 리스트")
    private List<EmployeeAuthVo> authWarehouseList;

}
