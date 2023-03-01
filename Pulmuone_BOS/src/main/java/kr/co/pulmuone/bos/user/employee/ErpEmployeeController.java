package kr.co.pulmuone.bos.user.employee;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.user.employee.dto.ErpEmployeeRequestDto;
import kr.co.pulmuone.v1.user.employee.service.ErpEmployeeBiz;
import lombok.RequiredArgsConstructor;

/**
* <PRE>
* Forbiz Korea
*  ERP 임직원 Controller
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 9. 16.                손진구          최초작성
* =======================================================================
* </PRE>
*/
@RestController
@RequiredArgsConstructor
public class ErpEmployeeController {
    private final ErpEmployeeBiz erpEmployeeBiz;

    /**
     * @Desc ERP 임직원 정보 조회
     * @param erpEmployeeRequestDto
     * @throws Exception
     * @return ApiResult<?>
     */
    @PostMapping(value = "/admin/user/employee/getPulmuoneEmployeeList")
    public ApiResult<?> getPulmuoneEmployeeList(HttpServletRequest request, ErpEmployeeRequestDto erpEmployeeRequestDto) throws Exception{

        return erpEmployeeBiz.getPulmuoneEmployeeList(BindUtil.bindDto(request, ErpEmployeeRequestDto.class));
    }

    /**
     * @Desc ERP 임직원정보 AND 조직정보 연동
     * @throws Exception
     * @return ApiResult<?>
     */
    @GetMapping(value = "/admin/user/employee/getErpEmployeeAndErpOrganization")
    public ApiResult<?> getErpEmployeeAndErpOrganization() throws Exception{

        return erpEmployeeBiz.getErpEmployeeAndErpOrganization();
    }
}