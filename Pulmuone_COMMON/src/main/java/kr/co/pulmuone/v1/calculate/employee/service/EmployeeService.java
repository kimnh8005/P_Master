package kr.co.pulmuone.v1.calculate.employee.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.calculate.employee.dto.EmployeeCalculateHeaderDto;
import kr.co.pulmuone.v1.calculate.employee.dto.EmployeeExcelListRequestDto;
import kr.co.pulmuone.v1.calculate.employee.dto.EmployeeListDto;
import kr.co.pulmuone.v1.calculate.employee.dto.EmployeeListRequestDto;
import kr.co.pulmuone.v1.calculate.employee.dto.SettleEmployeeMasterConfirmRequestDto;
import kr.co.pulmuone.v1.calculate.employee.dto.vo.CalculateConfirmProcVo;
import kr.co.pulmuone.v1.calculate.employee.dto.vo.LimitUsePriceExceDownloadVo;
import kr.co.pulmuone.v1.calculate.employee.dto.vo.SettleOuMngVo;
import kr.co.pulmuone.v1.calculate.employee.dto.vo.SupportPriceExceDownloadlVo;
import kr.co.pulmuone.v1.comm.mapper.calculate.employee.EmployeeMapper;
import lombok.RequiredArgsConstructor;

/**
 * <PRE>
 * Forbiz Korea
 * 정산관리 > 임직원관리 > 임직원 지원금 정산 Service
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

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeMapper employeeMapper;


    /**
     * @Desc 검색키 -> 검색키 리스트 변환
     *       검색키가 빈값이 아니고, 검색키중에 ALL 이 없을 경우 실행
     * @param searchKey
     * @param splitKey
     * @return List<String>
     */
    protected List<String> getSearchKeyToSearchKeyList(String searchKey, String splitKey) {
        List<String> searchKeyList = new ArrayList<String>();

        if( StringUtils.isNotEmpty(searchKey) && searchKey.indexOf("ALL") < 0 ) {

            searchKeyList.addAll(Stream.of(searchKey.split(splitKey))
                    .map(String::trim)
                    .filter( x -> StringUtils.isNotEmpty(x) )
                    .collect(Collectors.toList()));
        }

        return searchKeyList;
    }

    /**
     * 부문 구문 전체 조회
     * @return List<SettleOuMngVo>
     */
    protected List<SettleOuMngVo> getOuIdAllList() throws Exception {
        return employeeMapper.getOuIdAllList();
    }

    /**
     * 임직원 지원금 정산 리스트 카운트 조회
     * @param employeeListRequestDto
     * @return long
     */
    protected long getEmployeeListCount(EmployeeListRequestDto employeeListRequestDto) throws Exception {
        return employeeMapper.getEmployeeListCount(employeeListRequestDto);
    }

    /**
     * 임직원 지원금 정산 리스트 조회
     * @param employeeListRequestDto
     * @return List<EmployeeListDto>
     */
    protected List<EmployeeListDto> getEmployeeList(EmployeeListRequestDto employeeListRequestDto) throws Exception {
        return employeeMapper.getEmployeeList(employeeListRequestDto);
    }

    /**
     * 임직원 지원금 한도 사용액 엑셀 다운로드
     * @param employeeListRequestDto
     * @return List<EmployeeListDto>
     */
    protected List<LimitUsePriceExceDownloadVo> selectLimitUsePriceExceDownloadlList(EmployeeExcelListRequestDto employeeExcelListRequestDto) throws Exception {
    	return employeeMapper.selectLimitUsePriceExceDownloadlList(employeeExcelListRequestDto);
    }

    /**
     * 임직원 지원금 지원금정산 엑셀 다운로드
     * @param employeeListRequestDto
     * @return List<EmployeeListDto>
     */
    protected List<SupportPriceExceDownloadlVo> selectSupportPriceExceDownloadlList(EmployeeExcelListRequestDto employeeExcelListRequestDto) throws Exception {
    	return employeeMapper.selectSupportPriceExceDownloadlList(employeeExcelListRequestDto);
    }

    /**
     * 임직원 지원금 정산 확정 대상목록 조회
     * @param settleMonth
     * @param ouId
     * @return List<CalculateConfirmProcVo>
     */
    protected List<CalculateConfirmProcVo> selectCalculateConfirmProcList(String settleMonth, String ouId, long sessionId) throws Exception {
        return employeeMapper.selectCalculateConfirmProcList(settleMonth, ouId, sessionId);
    }

    /**
     * 임직원정산 (일마감) 정산여부 업데이트
     * @param headerItem
     * @param sessionId
     * @return void
     */
    protected void putSettleEmployeeDayYn(CalculateConfirmProcVo headerItem, long sessionId) throws Exception {
        employeeMapper.putSettleEmployeeDayYn(headerItem, sessionId);
    }

    /**
     * 임직원정산 마스터 확정 업데이트
     * @param confirmRequestDto
     * @return void
     */
    protected void putSettleEmployeeMasterConfirm(SettleEmployeeMasterConfirmRequestDto confirmRequestDto) throws Exception {
        employeeMapper.putSettleEmployeeMasterConfirm(confirmRequestDto);
    }

    /**
     * 임직원정산 이력 저장
     * @param headerDto
     * @return void
     */
    protected void addSettleEmployeeHist(EmployeeCalculateHeaderDto headerDto) throws Exception {
        employeeMapper.addSettleEmployeeHist(headerDto);
    }

}