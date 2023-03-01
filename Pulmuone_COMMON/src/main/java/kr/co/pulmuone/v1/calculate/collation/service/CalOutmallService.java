package kr.co.pulmuone.v1.calculate.collation.service;


import kr.co.pulmuone.v1.calculate.collation.dto.CalOutmallDetlListDto;
import kr.co.pulmuone.v1.calculate.collation.dto.CalOutmallListDto;
import kr.co.pulmuone.v1.calculate.collation.dto.CalOutmallListRequestDto;
import kr.co.pulmuone.v1.calculate.collation.dto.CalOutmallUploadDto;
import kr.co.pulmuone.v1.comm.mapper.calculate.collation.CalOutmallMapper;
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
 * 정산관리 > 대사관리 > 외부몰 주문 대사 Service
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
public class CalOutmallService {

    private final CalOutmallMapper calOutmallMapper;


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
//    protected List<SettleOuMngVo> getOuIdAllList() {
//    	return calOutmallMapper.getOuIdAllList();
//    }

    /**
     * 외부몰 주문 대사 리스트 카운트 조회
     * @param calOutmallListRequestDto
     * @return
     */
    protected long getOutmallListCount(CalOutmallListRequestDto calOutmallListRequestDto) {
        return calOutmallMapper.getOutmallListCount(calOutmallListRequestDto);
    }

    /**
     * 외부몰 주문 대사 리스트 조회
     * @param calOutmallListRequestDto
     * @return
     */
    protected List<CalOutmallListDto> getOutmallList(CalOutmallListRequestDto calOutmallListRequestDto) {
        return calOutmallMapper.getOutmallList(calOutmallListRequestDto);
    }

    /**
     * @Desc
     * @param calOutmallListRequestDto
     * @throws Exception
     * @return
     */
   	@Transactional(rollbackFor = Exception.class)
   	protected int addOdOrderMaster(CalOutmallListRequestDto calOutmallListRequestDto) {
   		return calOutmallMapper.addOdOrderMaster(calOutmallListRequestDto);
   	}


    protected CalOutmallUploadDto getSellerInfo(String sellersNm) {
    	CalOutmallUploadDto dto = new CalOutmallUploadDto();
    	dto = calOutmallMapper.getSellerInfo(sellersNm);
    	return dto;
    }


    /**
     * @Desc
     * @param calOutmallUploadDto
     * @throws Exception
     * @return
     */
   	@Transactional(rollbackFor = Exception.class)
   	protected int addOdOrderUploadDetail(CalOutmallUploadDto calOutmallUploadDto) {
   		return calOutmallMapper.addOdOrderUploadDetail(calOutmallUploadDto);
   	}

   	/**
     * @Desc
     * @param calOutmallListRequestDto
     * @throws Exception
     * @return
     */
   	@Transactional(rollbackFor = Exception.class)
   	protected int putOutmallCountInfo(CalOutmallListRequestDto calOutmallListRequestDto) {
   		return calOutmallMapper.putOutmallCountInfo(calOutmallListRequestDto);
   	}


   	/**
     * 외부몰 주문 대사 상세내역 리스트 카운트 조회
     * @param calOutmallListRequestDto
     * @return
     */
    protected CalOutmallDetlListDto getOutmallDetlListCount(CalOutmallListRequestDto calOutmallListRequestDto) {
        return calOutmallMapper.getOutmallDetlListCount(calOutmallListRequestDto);
    }

    /**
     * 외부몰 주문 대사 상세내역 리스트 조회
     * @param calOutmallListRequestDto
     * @return
     */
    protected List<CalOutmallDetlListDto> getOutmallDetlList(CalOutmallListRequestDto calOutmallListRequestDto) {
        return calOutmallMapper.getOutmallDetlList(calOutmallListRequestDto);
    }

    /**
     * BOS 클레임 사유 건수 조회
     * @param psClaimBosId
     * @return
     */
    protected int getPsClaimBosCount(long psClaimBosId) {
        return calOutmallMapper.getPsClaimBosCount(psClaimBosId);
    }


    /**
     * 외부몰 주문 대사 상세내역 리스트 조회
     * @param calOutmallListRequestDto
     * @return
     */
    protected List<CalOutmallDetlListDto> getCalOutmallUploadFailList(CalOutmallListRequestDto calOutmallListRequestDto) {
        return calOutmallMapper.getCalOutmallUploadFailList(calOutmallListRequestDto);
    }
}