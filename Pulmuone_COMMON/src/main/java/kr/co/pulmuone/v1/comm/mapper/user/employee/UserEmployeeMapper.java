package kr.co.pulmuone.v1.comm.mapper.user.employee;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.user.employee.dto.EmployeeInfoRequestDto;
import kr.co.pulmuone.v1.user.employee.dto.EmployeeRequestDto;
import kr.co.pulmuone.v1.user.employee.dto.vo.EmployeeAuthVo;
import kr.co.pulmuone.v1.user.employee.dto.vo.EmployeeVo;

@Mapper
public interface UserEmployeeMapper {

    /**
     * @Desc BOS계정관리 조회
     * @param employeeRequestDto
     * @return Page<EmployeeVo>
     */
    Page<EmployeeVo> getEmployeeList(EmployeeRequestDto employeeRequestDto);

    /**
     * @Desc 관리자 회원정보 조회
     * @param employeeNumber
     * @return EmployeeVo
     */
    EmployeeVo getEmployeeInfo(@Param("employeeNumber") String employeeNumber);

    /**
     * @Desc ERP 임직원 정보 조회
     * @param employeeNumber
     * @return EmployeeVo
     */
    EmployeeVo getErpEmployeeInfo(@Param("employeeNumber") String employeeNumber);

    /**
     * @Desc 관리자회원 이메일 중복검사
     * @param employeeInfoRequestDto
     * @return boolean
     */
    boolean getEmailDuplocateCheck(EmployeeInfoRequestDto employeeInfoRequestDto);

    /**
     * @Desc 관리자회원 등록
     * @param employeeInfoVo
     * @throws Exception
     * @return int
     */
    int addEmployee(EmployeeVo employeeInfoVo) throws Exception;

    /**
     * @Desc 관리자회원 수정
     * @param employeeInfoVo
     * @throws Exception
     * @return int
     */
    int putEmployee(EmployeeVo employeeInfoVo) throws Exception;

    /**
     * @Desc 관리자 회원 공급처/출고처 권한 등록
     * @param EmployeeAuthVo
     * @throws Exception
     * @return int
     */
    int addEmployeeAuth(EmployeeAuthVo employeeAuthVo) throws Exception;

    /**
     * @Desc 관리자 회원 공급처/출고처 권한 삭제
     * @param urEmployeeCd
     * @param authIdTp
     * @param listExcludeAuthId
     * @throws Exception
     * @return int
     */
    int delEmployeeAuth(@Param("urEmployeeCd") String urEmployeeCd, @Param("authIdTp") String authIdTp, @Param("listExcludeAuthId") List<String> listExcludeAuthId) throws Exception;

    /**
     * @Desc 관리자 회원 공급처/출고처 권한 조회 List
     * @param EmployeeAuthVo
     * @return List<EmployeeAuthVo>
     */
    List<EmployeeAuthVo> getEmployeeAuthList(EmployeeAuthVo employeeAuthVo);

}
