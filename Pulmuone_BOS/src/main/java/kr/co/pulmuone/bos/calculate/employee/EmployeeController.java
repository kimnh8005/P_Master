package kr.co.pulmuone.bos.calculate.employee;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.calculate.employee.dto.EmployeeExcelListRequestDto;
import kr.co.pulmuone.v1.calculate.employee.dto.EmployeeListRequestDto;
import kr.co.pulmuone.v1.calculate.employee.dto.EmployeeListResponseDto;
import kr.co.pulmuone.v1.calculate.employee.dto.OuIdListResponseDto;
import kr.co.pulmuone.v1.calculate.employee.service.EmployeeBiz;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.comm.util.SessionUtil;

/**
 * <PRE>
 * Forbiz Korea
 * 정산관리 > 임직원관리 > 임직원 지원금 정산 Controller
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 03. 04.	이명수		최초작성
 * =======================================================================
 * </PRE>
 */
@RestController
public class EmployeeController {

    @Autowired
    private EmployeeBiz employeeBiz;

    @Autowired
    private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

    /**
     * 부문 구분 전체 조회
     * @return ApiResult<?>
     * @throws Exception
     */
    @GetMapping(value = "/admin/calculate/employee/getOuIdAllList")
    @ApiOperation(value = "부문 구분 리스트 조회", httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data List<>", response = OuIdListResponseDto.class)
    })
    public ApiResult<?> getOuIdAllList() throws Exception {
        return employeeBiz.getOuIdAllList();
    }

    /**
     * 임직원 지원금 정산 리스트 조회
     * @param request
     * @param employeeListRequestDto
     * @return ApiResult<?>
     * @throws Exception
     */
    @PostMapping(value = "/admin/calculate/employee/getEmployeeList")
    @ApiOperation(value = "임직원 지원금 정산 리스트 조회", httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data List<>", response = EmployeeListResponseDto.class)
    })
    public ApiResult<?> getEmployeeList(HttpServletRequest request, EmployeeListRequestDto employeeListRequestDto) throws Exception {
        return employeeBiz.getEmployeeList(BindUtil.bindDto(request, EmployeeListRequestDto.class));
    }

    /**
     * 임직원 지원금 한도 사용액 엑셀 다운로드
     * @param request
     * @param employeeListRequestDto
     * @return ApiResult<?>
     * @throws Exception
     */
    @ApiOperation(value = "임직원 지원금 한도 사용액 엑셀 다운로드", httpMethod = "POST")
    @PostMapping(value = "/admin/calculate/employee/getEmployeeLimitUsePriceExcelList")
    public ModelAndView getEmployeeLimitUsePriceExcelList(@RequestBody EmployeeExcelListRequestDto employeeExcelListRequestDto) throws Exception {

        ExcelDownloadDto excelDownloadDto = employeeBiz.selectLimitUsePriceExceDownloadlList(employeeExcelListRequestDto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }

    /**
     * 임직원 지원금 지원금정산 엑셀 다운로드
     * @param request
     * @param employeeListRequestDto
     * @return ApiResult<?>
     * @throws Exception
     */
    @ApiOperation(value = "임직원 지원금 지원금정산 엑셀 다운로드", httpMethod = "POST")
    @PostMapping(value = "/admin/calculate/employee/getEmployeeSupportPriceExcelList")
    public ModelAndView getEmployeeSupportPriceExcelList(@RequestBody EmployeeExcelListRequestDto employeeExcelListRequestDto) throws Exception {

    	ExcelDownloadDto excelDownloadDto = employeeBiz.selectSupportPriceExceDownloadlList(employeeExcelListRequestDto);

    	ModelAndView modelAndView = new ModelAndView(excelDownloadView);
    	modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

    	return modelAndView;
    }

    /**
     * 임직원 지원금 정산 확정 완료
     * @return ApiResult<?>
     * @throws Exception
     */
    @PostMapping(value = "/admin/calculate/employee/putCalculateConfirmProc")
    @ApiOperation(value = "임직원 지원금 정산 확정 완료")
    public ApiResult<?> putCalculateConfirmProc(
            @RequestParam(value = "settleMonth[]") List<String> settleMonthList
            , @RequestParam(value = "ouId[]") List<String> ouIdList
            , @RequestParam(value = "sessionId[]") List<Long> sessionIdList
    ) throws Exception {
        return employeeBiz.putCalculateConfirmProc(settleMonthList, ouIdList, sessionIdList, SessionUtil.getBosUserVO().getUserId());
    }

}
