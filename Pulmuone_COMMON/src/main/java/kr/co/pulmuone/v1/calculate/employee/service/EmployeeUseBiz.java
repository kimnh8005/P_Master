package kr.co.pulmuone.v1.calculate.employee.service;

import kr.co.pulmuone.v1.calculate.employee.dto.EmployeeUseListRequestDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;

/**
 * <PRE>
 * Forbiz Korea
 * 정산관리 > 임직원관리 > 임직원 포인트 사용 현황 Interface
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 03. 03.	이명수		최초작성
 * =======================================================================
 * </PRE>
 */

public interface EmployeeUseBiz {

	/**
	 * 임직원 포인트 사용 현황 리스트 조회
	 * @param employeeUseListRequestDto
	 * @return
	 */
	public ApiResult<?> getEmployeeUseList(EmployeeUseListRequestDto employeeUseListRequestDto);

	/**
	 * 임직원 포인트 사용 현황 엑셀 리스트 조회
	 * @param employeeExcelListRequestDto
	 * @return
	 * @throws Exception
	 */
	public ExcelDownloadDto getEmployeeUseExcelList(EmployeeUseListRequestDto employeeUseListRequestDto) throws Exception;
}