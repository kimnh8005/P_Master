package kr.co.pulmuone.bos.user.employee;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.user.employee.dto.EmployeeInfoRequestDto;
import kr.co.pulmuone.v1.user.employee.dto.EmployeeRequestDto;
import kr.co.pulmuone.v1.user.employee.service.UserEmployeeBiz;
import lombok.RequiredArgsConstructor;

/**
* <PRE>
* Forbiz Korea
* 관리자회원 Controller
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 9. 14.                손진구          최초작성
* =======================================================================
* </PRE>
*/
@RestController
@RequiredArgsConstructor
public class UserEmployeeController {
    private final UserEmployeeBiz userEmployeeBiz;

    @ApiOperation(value = "BOS 계정관리 조회")
    @PostMapping(value = "/admin/user/employee/getEmployeeList")
    public ApiResult<?> getEmployeeList(HttpServletRequest request, EmployeeRequestDto employeeRequestDto) throws Exception{

        return userEmployeeBiz.getEmployeeList(BindUtil.bindDto(request, EmployeeRequestDto.class));
    }

    @ApiOperation(value = "BOS 회원정보 조회")
    @GetMapping(value = "/admin/user/employee/getEmployeeInfo")
    @ApiImplicitParams({ @ApiImplicitParam(name = "goodsDiscountId", value = "관리자회원 PK", required = true, dataType = "string") })
    public ApiResult<?> getEmployeeInfo(@RequestParam(value = "employeeNumber", required = true) String employeeNumber) {
        return userEmployeeBiz.getEmployeeInfo(employeeNumber);
    }

    @ApiOperation(value = "ERP 임직원 정보 조회")
    @GetMapping(value = "/admin/user/employee/getErpEmployeeInfo")
    @ApiImplicitParams({ @ApiImplicitParam(name = "employeeNumber", value = "관리자회원 PK", required = true, dataType = "string") })
    public ApiResult<?> getErpEmployeeInfo(@RequestParam(value = "employeeNumber", required = true) String employeeNumber){
        return userEmployeeBiz.getErpEmployeeInfo(employeeNumber);
    }

    @ApiOperation(value = "회원정보 등록")
    @PostMapping(value = "/admin/user/employee/addEmployeeInfo")
    public ApiResult<?> addEmployeeInfo(@RequestBody EmployeeInfoRequestDto employeeInfoRequestDto) throws Exception{

        return userEmployeeBiz.addEmployeeInfo(employeeInfoRequestDto);
    }

    @ApiOperation(value = "회원정보 수정")
    @PostMapping(value = "/admin/user/employee/putEmployeeInfo")
    public ApiResult<?> putEmployeeInfo(@RequestBody EmployeeInfoRequestDto employeeInfoRequestDto) throws Exception{

        return userEmployeeBiz.putEmployeeInfo(employeeInfoRequestDto);
    }

    @ApiOperation(value = "회원기본정보 조회")
    @GetMapping(value = "/admin/user/employee/getUserInfo")
    @ApiImplicitParams({ @ApiImplicitParam(name = "loginId", value = "관리자회원 PK", required = true, dataType = "string") })
    public ApiResult<?> getUserInfo(String loginId){

        return userEmployeeBiz.getUserInfo(loginId);
    }

    @ApiOperation(value = "이메일 중복검사")
    @PostMapping(value = "/admin/user/employee/getEmailDuplocateCheck")
    public ApiResult<?> getEmailDuplocateCheck(EmployeeInfoRequestDto employeeInfoRequestDto){

        return userEmployeeBiz.getEmailDuplocateCheck(employeeInfoRequestDto);
    }

    @ApiOperation(value = "권한설정 사용자권한 매핑 삭제")
    @PostMapping(value = "/admin/user/employee/delAuthUserMapping")
    public ApiResult<?> delAuthUserMapping(EmployeeRequestDto employeeRequestDto) throws Exception{

        return userEmployeeBiz.delAuthUserMapping(employeeRequestDto);
    }
}