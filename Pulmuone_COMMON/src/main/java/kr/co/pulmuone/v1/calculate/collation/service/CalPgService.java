package kr.co.pulmuone.v1.calculate.collation.service;


import kr.co.pulmuone.v1.calculate.collation.dto.*;
import kr.co.pulmuone.v1.calculate.employee.dto.vo.SettleOuMngVo;
import kr.co.pulmuone.v1.comm.mapper.calculate.collation.CalPgMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <PRE>
 * Forbiz Korea
 * 정산관리 > 대사관리 > PG 거래 내역 대사 Service
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
public class CalPgService {

    private final CalPgMapper calPgMapper;


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
     * @return
     */
    protected List<SettleOuMngVo> getOuIdAllList() {
    	return calPgMapper.getOuIdAllList();
    }

    /**
     * PG 거래 내역 대사 리스트 카운트 조회
     * @param calPgListRequestDto
     * @return
     */
    protected long getPgListCount(CalPgListRequestDto calPgListRequestDto) {
        return calPgMapper.getPgListCount(calPgListRequestDto);
    }

    /**
     * PG 거래 내역 대사 리스트 조회
     * @param calPgListRequestDto
     * @return
     */
    protected List<CalPgListDto> getPgList(CalPgListRequestDto calPgListRequestDto) {
        return calPgMapper.getPgList(calPgListRequestDto);
    }


    /**
     * @Desc
     * @param calPgListRequestDto
     * @throws Exception
     * @return
     */
   	@Transactional(rollbackFor = Exception.class)
   	protected int addOdOrderMaster(CalPgListRequestDto calPgListRequestDto) {
   		return calPgMapper.addOdOrderMaster(calPgListRequestDto);
   	}

   	/**
     * @Desc
     * @param calPgUploadDto
     * @throws Exception
     * @return
     */
   	@Transactional(rollbackFor = Exception.class)
   	protected int addOdPgCompareUploadDetail(CalPgUploadDto calPgUploadDto) {
   		return calPgMapper.addOdPgCompareUploadDetail(calPgUploadDto);
   	}

   	/**
     * @Desc
     * @param calPgUploadDto
     * @throws Exception
     * @return
     */
   	@Transactional(rollbackFor = Exception.class)
   	protected int addOdPgCompareUploadDetailInfo(CalPgUploadDto calPgUploadDto) {
   		return calPgMapper.addOdPgCompareUploadDetailInfo(calPgUploadDto);
   	}

   	/**
     * @Desc
     * @param calPgListRequestDto
     * @throws Exception
     * @return
     */
   	@Transactional(rollbackFor = Exception.class)
   	protected int putPgCountInfo(CalPgListRequestDto calPgListRequestDto) {
   		return calPgMapper.putPgCountInfo(calPgListRequestDto);
   	}


    /**
     * @Desc
     * @param calPgListRequestDto
     * @return
     */
    protected List<CalPgFailListDto> getCalPgUploadFailList(CalPgListRequestDto calPgListRequestDto) {
        return calPgMapper.getCalPgUploadFailList(calPgListRequestDto);
    }

}