package kr.co.pulmuone.bos.calculate.employee;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.calculate.employee.dto.EmployeeExcelListRequestDto;
import kr.co.pulmuone.v1.calculate.employee.dto.EmployeeListResponseDto;
import kr.co.pulmuone.v1.calculate.employee.dto.EmployeeUseListRequestDto;
import kr.co.pulmuone.v1.calculate.employee.service.EmployeeUseBiz;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * <PRE>
 * Forbiz Korea
 * 정산관리 > 임직원관리 > 임직원 포인트 사용 현황 Controller
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 03. 05.	이명수		최초작성
 * =======================================================================
 * </PRE>
 */
@RestController
public class EmployeeUseController {


    @Autowired
    private EmployeeUseBiz employeeUseBiz;

    @Autowired
    private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰


    /**
     * 임직원 포인트 사용 현황 리스트 조회
     * @param request
     * @param employeeUseListRequestDto
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/admin/calculate/employee/getEmployeeUseList")
    @ApiOperation(value = "임직원 포인트 사용 현황 리스트 조회", httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data List<>", response = EmployeeListResponseDto.class)
    })
    public ApiResult<?> getEmployeeUseList(HttpServletRequest request, EmployeeUseListRequestDto employeeUseListRequestDto) throws Exception {
        return employeeUseBiz.getEmployeeUseList(BindUtil.bindDto(request, EmployeeUseListRequestDto.class));
    }

    /**
     * 임직원 포인트 사용 현황 엑셀 다운로드
     * @param employeeUseListRequestDto
     * @return ModelAndView
     * @throws Exception
     */
    @ApiOperation(value = "임직원 포인트 사용 현황 엑셀 다운로드", httpMethod = "POST")
    @PostMapping(value = "/admin/calculate/employee/getEmployeeUseExcelList")
    public ModelAndView getEmployeeUseExcelList(@RequestBody EmployeeUseListRequestDto employeeUseListRequestDto) throws Exception {

    	ExcelDownloadDto excelDownloadDto = employeeUseBiz.getEmployeeUseExcelList(employeeUseListRequestDto);

    	ModelAndView modelAndView = new ModelAndView(excelDownloadView);
    	modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

    	return modelAndView;
    }

}
