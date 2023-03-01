package kr.co.pulmuone.v1.calculate.collation.service;

import kr.co.pulmuone.v1.calculate.collation.dto.CalSalesListDto;
import kr.co.pulmuone.v1.calculate.collation.dto.CalSalesListRequestDto;
import kr.co.pulmuone.v1.calculate.collation.dto.CalSalesListResponseDto;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.mapper.calculate.collation.CalSalesMapper;
import kr.co.pulmuone.v1.comm.util.DateUtil;
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
 * 정산관리 > 대사관리 > 통합몰 매출 대사 Service
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 * 버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 * 1.0		2021. 04. 28.	이원호		최초작성
 * =======================================================================
 * </PRE>
 */

@Service
@RequiredArgsConstructor
public class CalSalesService {

    private final CalSalesMapper calSalesMapper;

    /**
     * @param searchKey
     * @param splitKey
     * @return List<String>
     * @Desc 검색키 -> 검색키 리스트 변환
     * 검색키가 빈값이 아니고, 검색키중에 ALL 이 없을 경우 실행
     */
    protected List<String> getSearchKeyToSearchKeyList(String searchKey, String splitKey, boolean containsAll) {
        List<String> searchKeyList = new ArrayList<>();

        if(containsAll){
            if (StringUtils.isNotEmpty(searchKey)) {
                searchKeyList.addAll(Stream.of(searchKey.split(splitKey))
                        .map(String::trim)
                        .filter(StringUtils::isNotEmpty)
                        .collect(Collectors.toList()));
            }
        }else{
            if (StringUtils.isNotEmpty(searchKey) && !searchKey.contains("ALL")) {
                searchKeyList.addAll(Stream.of(searchKey.split(splitKey))
                        .map(String::trim)
                        .filter(StringUtils::isNotEmpty)
                        .collect(Collectors.toList()));
            }
        }

        return searchKeyList;
    }

    /**
     * 통합몰 매출 대사 리스트 조회
     *
     * @param dto CalSalesListRequestDto
     * @return CalSalesListResponseDto
     */
    protected CalSalesListResponseDto getSalesList(CalSalesListRequestDto dto) {
        dto.setErpDateSearchStart(DateUtil.convertFormatNew(dto.getErpDateSearchStart(), "yyyyMMdd", "yyyy-MM-dd"));
        dto.setErpDateSearchEnd(DateUtil.convertFormatNew(dto.getErpDateSearchEnd(), "yyyyMMdd", "yyyy-MM-dd"));
        dto.setDateSearchStart(DateUtil.convertFormatNew(dto.getDateSearchStart(), "yyyyMMdd", "yyyy-MM-dd"));
        dto.setDateSearchEnd(DateUtil.convertFormatNew(dto.getDateSearchEnd(), "yyyyMMdd", "yyyy-MM-dd"));
        dto.setSalesGubunList(getSearchKeyToSearchKeyList(dto.getSalesGubun(), Constants.ARRAY_SEPARATORS, true));
        dto.setOrderStateList(getSearchKeyToSearchKeyList(dto.getOrderState(), Constants.ARRAY_SEPARATORS, false)); // BOS 주문상태
        dto.setClaimStateList(getSearchKeyToSearchKeyList(dto.getClaimState(), Constants.ARRAY_SEPARATORS, false)); // BOS 클레임상태

//        PageMethod.startPage(dto.getPage(), dto.getPageSize());
        List<CalSalesListDto> result = calSalesMapper.getSalesList(dto);

        CalSalesListDto totalDto = calSalesMapper.getSalesListCount(dto);

        return  CalSalesListResponseDto.builder()
                        .rows(result)
                        .total(totalDto.getTotalCnt())
                        .erpSumPrice(totalDto.getSettlePrice())
                        .bosSumPrice(totalDto.getPaidPrice())
                        .build()
        ;

    }




    /**
     * 통합몰 매출 대사 리스트 엑셀 다운로드
     *
     * @param dto
     * @return List<CalSalesListDto>
     * @throws Exception
     */
    protected CalSalesListResponseDto getSalesExcelList(CalSalesListRequestDto dto) throws Exception
    {
        dto.setErpDateSearchStart(DateUtil.convertFormatNew(dto.getErpDateSearchStart(), "yyyyMMdd", "yyyy-MM-dd"));
        dto.setErpDateSearchEnd(DateUtil.convertFormatNew(dto.getErpDateSearchEnd(), "yyyyMMdd", "yyyy-MM-dd"));
        dto.setDateSearchStart(DateUtil.convertFormatNew(dto.getDateSearchStart(), "yyyyMMdd", "yyyy-MM-dd"));
        dto.setDateSearchEnd(DateUtil.convertFormatNew(dto.getDateSearchEnd(), "yyyyMMdd", "yyyy-MM-dd"));
        dto.setSalesGubunList(getSearchKeyToSearchKeyList(dto.getSalesGubun(), Constants.ARRAY_SEPARATORS, true));
        dto.setOrderStateList(getSearchKeyToSearchKeyList(dto.getOrderState(), Constants.ARRAY_SEPARATORS, false)); // BOS 주문상태
        dto.setClaimStateList(getSearchKeyToSearchKeyList(dto.getClaimState(), Constants.ARRAY_SEPARATORS, false)); // BOS 클레임상태

        List<CalSalesListDto> result = new ArrayList<>();
        dto.setExcelYn("Y");
        result = calSalesMapper.getSalesList(dto);

        return  CalSalesListResponseDto.builder()
                .rows(result)
                .build()
                ;
    }

}