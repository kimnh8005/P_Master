package kr.co.pulmuone.v1.comm.mapper.calculate.employee;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.pulmuone.v1.calculate.employee.dto.EmployeeCalculateHeaderDto;
import kr.co.pulmuone.v1.calculate.employee.dto.EmployeeExcelListRequestDto;
import kr.co.pulmuone.v1.calculate.employee.dto.EmployeeListDto;
import kr.co.pulmuone.v1.calculate.employee.dto.EmployeeListRequestDto;
import kr.co.pulmuone.v1.calculate.employee.dto.SettleEmployeeMasterConfirmRequestDto;
import kr.co.pulmuone.v1.calculate.employee.dto.vo.CalculateConfirmProcVo;
import kr.co.pulmuone.v1.calculate.employee.dto.vo.LimitUsePriceExceDownloadVo;
import kr.co.pulmuone.v1.calculate.employee.dto.vo.SettleOuMngVo;
import kr.co.pulmuone.v1.calculate.employee.dto.vo.SupportPriceExceDownloadlVo;

/**
 * <PRE>
 * Forbiz Korea
 * 정산관리 > 임직원관리 > 임직원 지원금 정산 Mapper
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
@Mapper
public interface EmployeeMapper {

	/**
	 * 부문 구문 전체 조회
	 * @return List<SettleOuMngVo>
	 */
	List<SettleOuMngVo> getOuIdAllList() throws Exception;

	/**
	 * 임직원 지원금 정산 리스트 카운트 조회
	 * @param employeeListRequestDto
	 * @return long
	 */
	long getEmployeeListCount(EmployeeListRequestDto employeeListRequestDto) throws Exception;

	/**
	 * 임직원 지원금 정산 리스트 조회
	 * @param employeeListRequestDto
	 * @return List<EmployeeListDto>
	 */
	List<EmployeeListDto> getEmployeeList(EmployeeListRequestDto employeeListRequestDto) throws Exception;

	/**
	 * 임직원 지원금 한도 사용액 엑셀 다운로드
	 * @param employeeListRequestDto
	 * @return List<LimitUsePriceExceDownloadVo>
	 */
	List<LimitUsePriceExceDownloadVo> selectLimitUsePriceExceDownloadlList(EmployeeExcelListRequestDto employeeExcelListRequestDto) throws Exception;

	/**
	 * 임직원 지원금 지원금정산 엑셀 다운로드
	 * @param employeeListRequestDto
	 * @return List<LimitUsePriceExceDownloadVo>
	 */
	List<SupportPriceExceDownloadlVo> selectSupportPriceExceDownloadlList(EmployeeExcelListRequestDto employeeExcelListRequestDto) throws Exception;

	/**
	 * 임직원 지원금 정산 확정 대상목록 조회
	 * @param settleMonth
	 * @param ouId
	 * @return List<CalculateConfirmProcVo>
	 */
	List<CalculateConfirmProcVo> selectCalculateConfirmProcList(@Param("settleMonth") String settleMonth, @Param("ouId") String ouId, @Param("sessionId") long sessionId) throws Exception;

	/**
	 * 임직원정산 (일마감) 정산여부 업데이트
	 * @param headerItem
	 * @return void
	 */
	void putSettleEmployeeDayYn(@Param("headerItem") CalculateConfirmProcVo headerItem, @Param("sessionId") long sessionId) throws Exception;

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
