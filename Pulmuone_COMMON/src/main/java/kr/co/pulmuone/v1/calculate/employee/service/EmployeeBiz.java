package kr.co.pulmuone.v1.calculate.employee.service;

import kr.co.pulmuone.v1.calculate.employee.dto.EmployeeCalculateHeaderDto;
import kr.co.pulmuone.v1.calculate.employee.dto.EmployeeExcelListRequestDto;
import kr.co.pulmuone.v1.calculate.employee.dto.EmployeeListRequestDto;
import kr.co.pulmuone.v1.calculate.employee.dto.SettleEmployeeMasterConfirmRequestDto;
import kr.co.pulmuone.v1.calculate.employee.dto.vo.CalculateConfirmProcVo;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 정산관리 > 임직원관리 > 임직원 지원금 정산 Interface
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

public interface EmployeeBiz {

	/**
	 * 부문 구문 전체 조회
	 * @return ApiResult<?>
	 */
	ApiResult<?> getOuIdAllList() throws Exception ;

	/**
	 * 임직원 지원금 정산 리스트 조회
	 * @param employeeListRequestDto
	 * @return ApiResult<?>
	 */
	ApiResult<?> getEmployeeList(EmployeeListRequestDto employeeListRequestDto) throws Exception;

	/**
	 * 임직원 지원금 한도 사용액 엑셀 다운로드
	 * @param employeeExcelListRequestDto
	 * @return
	 * @throws Exception
	 */
	ExcelDownloadDto selectLimitUsePriceExceDownloadlList(EmployeeExcelListRequestDto employeeExcelListRequestDto) throws Exception;

	/**
	 * 임직원 지원금 지원금정산 엑셀 다운로드
	 * @param employeeExcelListRequestDto
	 * @return
	 * @throws Exception
	 */
	ExcelDownloadDto selectSupportPriceExceDownloadlList(EmployeeExcelListRequestDto employeeExcelListRequestDto) throws Exception;

	/**
	 * 임직원 지원금 정산 확정 완료
	 * @param settleMonthList
	 * @param ouIdList
	 * @param sessionIdList
	 * @return ApiResult<?>
	 */
	ApiResult<?> putCalculateConfirmProc(List<String> settleMonthList, List<String> ouIdList, List<Long> sessionIdList, String userId) throws Exception;

	/**
	 * 임직원정산 (일마감) 정산여부 업데이트
	 * @param headerItem
	 * @param sessionId
	 * @return void
	 */
	void putSettleEmployeeDayYn(CalculateConfirmProcVo headerItem, long sessionId) throws Exception;

	/**
	 * 임직원정산 마스터 확정 업데이트
	 * @param confirmRequestDto
	 * @return void
	 */
	void putSettleEmployeeMasterConfirm(SettleEmployeeMasterConfirmRequestDto confirmRequestDto) throws Exception;

	/**
	 * 임직원정산 이력 저장
	 * @param headerDto
	 * @return void
	 */
	void addSettleEmployeeHist(EmployeeCalculateHeaderDto headerDto) throws Exception;
}