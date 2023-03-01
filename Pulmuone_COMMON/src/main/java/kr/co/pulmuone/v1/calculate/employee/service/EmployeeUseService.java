package kr.co.pulmuone.v1.calculate.employee.service;


import kr.co.pulmuone.v1.calculate.employee.dto.EmployeeUseListDto;
import kr.co.pulmuone.v1.calculate.employee.dto.EmployeeUseListRequestDto;
import kr.co.pulmuone.v1.comm.mapper.calculate.employee.EmployeeUseMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <PRE>
 * Forbiz Korea
 * 정산관리 > 임직원관리 > 임직원 포인트 사용 현황 Service
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

@Service
@RequiredArgsConstructor
public class EmployeeUseService {

    private final EmployeeUseMapper employeeUseMapper;


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
     * 임직원 포인트 사용 현황 리스트 카운트 조회
     * @param employeeUseListRequestDto
     * @return
     */
    protected long getEmployeeUseListCount(EmployeeUseListRequestDto employeeUseListRequestDto) {
        return employeeUseMapper.getEmployeeUseListCount(employeeUseListRequestDto);
    }

    /**
     * 임직원 포인트 사용 현황 리스트 조회
     * @param employeeUseListRequestDto
     * @return
     */
    protected List<EmployeeUseListDto> getEmployeeUseList(EmployeeUseListRequestDto employeeUseListRequestDto) {
        return employeeUseMapper.getEmployeeUseList(employeeUseListRequestDto);
    }

}