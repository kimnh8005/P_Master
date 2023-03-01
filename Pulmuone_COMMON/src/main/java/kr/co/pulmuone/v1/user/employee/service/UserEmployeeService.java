package kr.co.pulmuone.v1.user.employee.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.mapper.user.employee.UserEmployeeMapper;
import kr.co.pulmuone.v1.user.employee.dto.EmployeeInfoRequestDto;
import kr.co.pulmuone.v1.user.employee.dto.EmployeeRequestDto;
import kr.co.pulmuone.v1.user.employee.dto.vo.EmployeeAuthVo;
import kr.co.pulmuone.v1.user.employee.dto.vo.EmployeeVo;
import lombok.RequiredArgsConstructor;


/**
* <PRE>
* Forbiz Korea
* 관리자회원 Service
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
@Service
@RequiredArgsConstructor
public class UserEmployeeService {

    @Autowired
    private final UserEmployeeMapper userEmployeeMapper;

    /**
     * @Desc BOS계정관리 조회
     * @param employeeRequestDto
     * @return Page<EmployeeVo>
     */
    protected Page<EmployeeVo> getEmployeeList(EmployeeRequestDto employeeRequestDto) {
        PageMethod.startPage(employeeRequestDto.getPage(), employeeRequestDto.getPageSize());
        return userEmployeeMapper.getEmployeeList(employeeRequestDto);
    }

    /**
     * @Desc 관리자 회원정보 조회
     * @param employeeNumber
     * @return EmployeeVo
     */
 	@UserMaskingRun(system="BOS")
    protected EmployeeVo getEmployeeInfo(String employeeNumber) {
        return userEmployeeMapper.getEmployeeInfo(employeeNumber);
    }

    /**
     * @Desc ERP 임직원 정보 조회
     * @param employeeNumber
     * @return EmployeeVo
     */
    protected EmployeeVo getErpEmployeeInfo(String employeeNumber) {
        return userEmployeeMapper.getErpEmployeeInfo(employeeNumber);
    }

    /**
     * @Desc 관리자회원 이메일 중복체크
     * @param employeeInfoRequestDto
     * @return boolean
     */
    protected boolean getEmailDuplocateCheck(EmployeeInfoRequestDto employeeInfoRequestDto) {
        return userEmployeeMapper.getEmailDuplocateCheck(employeeInfoRequestDto);
    }

    /**
     * @Desc 관리자회원 등록
     * @param employeeInfoVo
     * @throws Exception
     * @return int
     */
    protected int addEmployee(EmployeeVo employeeInfoVo) throws Exception{
        return userEmployeeMapper.addEmployee(employeeInfoVo);
    }

    /**
     * @Desc 관리자회원 수정
     * @param employeeInfoVo
     * @throws Exception
     * @return int
     */
    protected int putEmployee(EmployeeVo employeeInfoVo) throws Exception{
        return userEmployeeMapper.putEmployee(employeeInfoVo);
    }

    /**
     * @Desc 관리자 회원 공급처/출고처 권한 등록
     * @param EmployeeAuthVo
     * @throws Exception
     * @return int
     */
    protected int addEmployeeAuth(EmployeeAuthVo employeeAuthVo) throws Exception{
        return userEmployeeMapper.addEmployeeAuth(employeeAuthVo);
    }

    /**
     * @Desc 관리자 회원 공급처/출고처 권한 삭제
     * @param String
     * @throws Exception
     * @return int
     */
    protected int delEmployeeAuth(String urEmployeeCd, String authIdTp, List<String> listExcludeAuthId) throws Exception{
        return userEmployeeMapper.delEmployeeAuth(urEmployeeCd, authIdTp, listExcludeAuthId);
    }

    /**
     * @Desc 관리자 회원 공급처/출고처 권한 조회 List
     * @param EmployeeAuthVo
     * @throws Exception
     * @return List<EmployeeAuthVo>
     */
    protected List<EmployeeAuthVo> getEmployeeAuthList(EmployeeAuthVo employeeAuthVo) {
        return userEmployeeMapper.getEmployeeAuthList(employeeAuthVo);
    }

}
