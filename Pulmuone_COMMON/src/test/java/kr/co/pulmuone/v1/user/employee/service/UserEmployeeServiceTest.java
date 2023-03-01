package kr.co.pulmuone.v1.user.employee.service;

import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.user.employee.dto.EmployeeInfoRequestDto;
import kr.co.pulmuone.v1.user.employee.dto.EmployeeRequestDto;
import kr.co.pulmuone.v1.user.employee.dto.vo.EmployeeAuthVo;
import kr.co.pulmuone.v1.user.employee.dto.vo.EmployeeVo;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserEmployeeServiceTest extends CommonServiceTestBaseForJunit5{

    @Autowired
    private UserEmployeeService userEmployeeService;

    @Test
    void BOS계정관리_조회_성공() {

        UserVo userVO = new UserVo();
        userVO.setLangCode("1");
        List<String> listRoleId = new ArrayList<>();
        listRoleId.add("1");
        userVO.setListRoleId(listRoleId);
        SessionUtil.setUserVO(userVO);

        EmployeeRequestDto employeeRequestDto = new EmployeeRequestDto();
        employeeRequestDto.setPage(1);
        employeeRequestDto.setPageSize(30);
        employeeRequestDto.setFindKeyword("01608");
        employeeRequestDto.setSearchCondition("ID");

        Page<EmployeeVo> employeeVoList = userEmployeeService.getEmployeeList(employeeRequestDto);

        assertTrue( CollectionUtils.isNotEmpty(employeeVoList.getResult()) );

        SessionUtil.setUserVO(null);
    }

    @Test
    void BOS계정관리_조회_실패() {

        EmployeeRequestDto employeeRequestDto = new EmployeeRequestDto();
        employeeRequestDto.setPage(1);
        employeeRequestDto.setPageSize(30);
        employeeRequestDto.setFindKeyword("222222");
        employeeRequestDto.setSearchCondition("ID");

        Page<EmployeeVo> employeeVoList = userEmployeeService.getEmployeeList(employeeRequestDto);

        assertFalse( CollectionUtils.isNotEmpty(employeeVoList.getResult()) );
    }

    @Test
    void 관리자_회원정보_조회_성공() {
        EmployeeVo employeeVo = userEmployeeService.getEmployeeInfo("A123456456");

        assertEquals("A123456456", employeeVo.getEmployeeNumber());
    }

    @Test
    void 관리자_회원정보_조회_실패() {
        EmployeeVo employeeVo = userEmployeeService.getEmployeeInfo("1");

        assertFalse( !ObjectUtils.isEmpty(employeeVo) );
    }

    @Test
    void ERP_임직원_정보_조회_성공() {
        EmployeeVo employeeVo = userEmployeeService.getErpEmployeeInfo("A123456456");

        assertEquals("A123456456", employeeVo.getEmployeeNumber());
    }

    @Test
    void ERP_임직원_정보_조회_실패() {
        EmployeeVo employeeVo = userEmployeeService.getErpEmployeeInfo("1");

        assertFalse( !ObjectUtils.isEmpty(employeeVo) );
    }

    @Test
    void 관리자회원_이메일_중복체크_중복안함() {
        EmployeeInfoRequestDto employeeInfoRequestDto = new EmployeeInfoRequestDto();
        employeeInfoRequestDto.setEmail("vnfandnjs0901111@forbiz.co.kr");

        boolean emailDuplocate = userEmployeeService.getEmailDuplocateCheck(employeeInfoRequestDto);

        assertFalse(emailDuplocate);
    }

    @Test
    void 관리자회원_이메일_중복체크_중복() {
        EmployeeInfoRequestDto employeeInfoRequestDto = new EmployeeInfoRequestDto();
        employeeInfoRequestDto.setEmail("vnfandnjs0907@forbiz.co.kr");

        boolean emailDuplocate = userEmployeeService.getEmailDuplocateCheck(employeeInfoRequestDto);

        assertTrue(emailDuplocate);
    }

    @Test
    void 관리자회원_등록_성공() {
        int count = 0;

        EmployeeVo employeeVo = new EmployeeVo();
        employeeVo.setEmployeeNumber("e5555555");
        employeeVo.setUserId(419936L);
        employeeVo.setCompanyId(1L);
        employeeVo.setUserStatus(UserEnums.StatusCode.NORMAL.getCode());
        employeeVo.setTeamLeaderYn("N");
        employeeVo.setPersonalInfoAccessYn("N");
        employeeVo.setGrantAuthStopYn("N");
        employeeVo.setAuthSupplierId(1L);
        employeeVo.setCreateId("1");

        try {
            count = userEmployeeService.addEmployee(employeeVo);
        } catch (Exception e) {

        }

        assertEquals(1, count);

    }

    @Test
    void 관리자회원_등록_실패() {
        int count = 0;

        EmployeeVo employeeVo = new EmployeeVo();
        employeeVo.setUserId(0L);
        employeeVo.setCompanyId(1L);
        employeeVo.setUserStatus(UserEnums.StatusCode.NORMAL.getCode());
        employeeVo.setTeamLeaderYn("N");
        employeeVo.setPersonalInfoAccessYn("N");
        employeeVo.setGrantAuthStopYn("N");
        employeeVo.setAuthSupplierId(1L);
        employeeVo.setCreateId("1");

        try {
            count = userEmployeeService.addEmployee(employeeVo);
        } catch (Exception e) {

        }

        assertNotEquals(1, count);
    }

    @Test
    void 관리자회원_수정_성공() {
        int count = 0;

        EmployeeVo employeeVo = new EmployeeVo();
        employeeVo.setEmployeeNumber("A123456457");
        employeeVo.setCreateId("1");

        try {
            count = userEmployeeService.putEmployee(employeeVo);
        } catch (Exception e) {

        }

        assertEquals(1, count);
    }

    @Test
    void 관리자회원_수정_실패() {
        int count = 0;

        EmployeeVo employeeVo = new EmployeeVo();
        employeeVo.setEmployeeNumber("");
        employeeVo.setCreateId("1");

        try {
            count = userEmployeeService.putEmployee(employeeVo);
        } catch (Exception e) {

        }

        assertNotEquals(1, count);
    }

    @Test
    void 관리자회원_권한_성공() {
        int count = 0;

        try {
            EmployeeAuthVo employeeAuthVo = new EmployeeAuthVo();
        	employeeAuthVo.setUrEmployeeCd("forbiz1234");
        	employeeAuthVo.setAuthIdTp(UserEnums.EmployeeAuthIdType.SUPPLIER.getCode());
        	employeeAuthVo.setAuthId("1");
        	employeeAuthVo.setCreateId("1");
            count = userEmployeeService.addEmployeeAuth(employeeAuthVo);
        } catch (Exception e) {

        }

        assertEquals(1, count);

        try {
        	count = 0;
            EmployeeAuthVo employeeAuthVo = new EmployeeAuthVo();
        	employeeAuthVo.setUrEmployeeCd("forbiz1234");
        	employeeAuthVo.setAuthIdTp(UserEnums.EmployeeAuthIdType.WAREHOUSE.getCode());
        	employeeAuthVo.setAuthId("85");
        	employeeAuthVo.setCreateId("1");
            count = userEmployeeService.addEmployeeAuth(employeeAuthVo);
        } catch (Exception e) {

        }

        assertEquals(1, count);

        try {
        	count = 0;
            count = userEmployeeService.delEmployeeAuth("forbiz1234", null, null);
        } catch (Exception e) {

        }

        assertEquals(2, count);

    }

}
