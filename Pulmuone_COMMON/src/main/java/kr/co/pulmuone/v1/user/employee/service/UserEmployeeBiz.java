package kr.co.pulmuone.v1.user.employee.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.user.employee.dto.EmployeeInfoRequestDto;
import kr.co.pulmuone.v1.user.employee.dto.EmployeeRequestDto;
import kr.co.pulmuone.v1.user.employee.dto.vo.EmployeeAuthVo;
import kr.co.pulmuone.v1.user.employee.dto.vo.EmployeeVo;

public interface UserEmployeeBiz {

    ApiResult<?> getEmployeeList(EmployeeRequestDto employeeRequestDto);
    ApiResult<?> getEmployeeInfo(String employeeNumber);
    ApiResult<?> getErpEmployeeInfo(String employeeNumber);
    ApiResult<?> addEmployeeInfo(EmployeeInfoRequestDto employeeInfoRequestDto) throws Exception;
    ApiResult<?> putEmployeeInfo(EmployeeInfoRequestDto employeeInfoRequestDto) throws Exception;
    ApiResult<?> getUserInfo(String loginId);
    ApiResult<?> getEmailDuplocateCheck(EmployeeInfoRequestDto employeeInfoRequestDto);
    ApiResult<?> delAuthUserMapping(EmployeeRequestDto employeeRequestDto) throws Exception;
    int putEmployee(EmployeeVo employeeInfoVo) throws Exception;
    int addEmployeeAuth(EmployeeAuthVo employeeAuthVo) throws Exception;
    int delEmployeeAuth(String urEmployeeCd, String authIdTp, List<String> listExcludeAuthId) throws Exception;
    List<EmployeeAuthVo> getEmployeeAuthList(EmployeeAuthVo employeeAuthVo);
}
