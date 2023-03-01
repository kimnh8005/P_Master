package kr.co.pulmuone.v1.comm.mapper.calculate.employee;

import kr.co.pulmuone.v1.calculate.employee.dto.EmployeeUseListDto;
import kr.co.pulmuone.v1.calculate.employee.dto.EmployeeUseListRequestDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 정산관리 > 임직원관리 > 임직원 포인트 사용 현황 Mapper
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
@Mapper
public interface EmployeeUseMapper {

	/**
	 * 임직원 포인트 사용 현황 리스트 카운트 조회
	 * @param employeeUseListRequestDto
	 * @return
	 */
	long getEmployeeUseListCount(EmployeeUseListRequestDto employeeUseListRequestDto);

	/**
	 * 임직원 포인트 사용 현황 리스트 조회
	 * @param employeeUseListRequestDto
	 * @return
	 */
	List<EmployeeUseListDto> getEmployeeUseList(EmployeeUseListRequestDto employeeUseListRequestDto);
}
