package kr.co.pulmuone.v1.calculate.collation.service;


import kr.co.pulmuone.v1.calculate.collation.dto.CalPgDetlListDto;
import kr.co.pulmuone.v1.calculate.collation.dto.CalPgDetlListRequestDto;
import kr.co.pulmuone.v1.calculate.collation.dto.CalPgDetlListResponseDto;
import kr.co.pulmuone.v1.calculate.collation.dto.CalPgUploadResponseDto;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.mapper.calculate.collation.CalPgDetlMapper;
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
 * 정산관리 > 대사관리 > PG 대사 상세내역 Service
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 * 버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 * 1.0		2021. 04. 26.	이원호		최초작성
 * =======================================================================
 * </PRE>
 */

@Service
@RequiredArgsConstructor
public class CalPgDetlService {

    private final CalPgDetlMapper calPgDetlMapper;

//    /**
//     * @param searchKey
//     * @param splitKey
//     * @return List<String>
//     * @Desc 검색키 -> 검색키 리스트 변환
//     * 검색키가 빈값이 아니고, 검색키중에 ALL 이 없을 경우 실행
//     */
    protected List<String> getSearchKeyToSearchKeyList(String searchKey, String splitKey) {
        List<String> searchKeyList = new ArrayList<String>();

        if (StringUtils.isNotEmpty(searchKey) && searchKey.indexOf("ALL") < 0) {

            searchKeyList.addAll(Stream.of(searchKey.split(splitKey))
                    .map(String::trim)
                    .filter(x -> StringUtils.isNotEmpty(x))
                    .collect(Collectors.toList()));
        }

        return searchKeyList;
    }

    /**
     * PG 거래 내역 대사 리스트 조회
     *
     * @param dto CalPgDetlListRequestDto
     * @return CalPgDetlListResponseDto
     */
    protected CalPgDetlListResponseDto getPgDetailList(CalPgDetlListRequestDto dto) {
        dto.setDateSearchStart(DateUtil.convertFormatNew(dto.getDateSearchStart(), "yyyyMMdd", "yyyy-MM-dd"));
        dto.setDateSearchEnd(DateUtil.convertFormatNew(dto.getDateSearchEnd(), "yyyyMMdd", "yyyy-MM-dd"));
        dto.setSalesOrderGubunList(getSearchKeyToSearchKeyList(dto.getSalesOrderGubun(), Constants.ARRAY_SEPARATORS));
        dto.setPaymentMethodCodeList(getSearchKeyToSearchKeyList(dto.getPaymentMethodCode(), Constants.ARRAY_SEPARATORS));

        CalPgUploadResponseDto totalVo = calPgDetlMapper.getPgDetailListCount(dto);
        List<CalPgDetlListDto> calPgDetlList = new ArrayList<>();
        if (totalVo.getTotalCnt() > 0) {
            calPgDetlList = calPgDetlMapper.getPgDetailList(dto);
        }
//        PageMethod.startPage(dto.getPage(), dto.getPageSize());
//        Page<CalPgDetlListDto> result = calPgDetlMapper.getPgDetailList(dto);

        return CalPgDetlListResponseDto.builder()
                .total(totalVo.getTotalCnt())
                .emSumPrice(totalVo.getTotalAmt())
                .rows(calPgDetlList)
                .build();
    }

}