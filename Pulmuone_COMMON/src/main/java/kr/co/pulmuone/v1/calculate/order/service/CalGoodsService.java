package kr.co.pulmuone.v1.calculate.order.service;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.calculate.employee.dto.vo.SettleOuMngVo;
import kr.co.pulmuone.v1.calculate.order.dto.CalGoodsListDto;
import kr.co.pulmuone.v1.calculate.order.dto.CalGoodsListRequestDto;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.mapper.calculate.order.CalGoodsMapper;
import lombok.RequiredArgsConstructor;

/**
 * <PRE>
 * Forbiz Korea
 * 정산관리 > 주문정산 > 상품정산 Service
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
public class CalGoodsService {

    private final CalGoodsMapper calGoodsMapper;


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
    	return calGoodsMapper.getOuIdAllList();
    }

    /**
     * 상품 정산 리스트 카운트 조회
     * @param calGoodsListRequestDto
     * @return
     */
    protected long getGoodsListCount(CalGoodsListRequestDto calGoodsListRequestDto) {
        return calGoodsMapper.getGoodsListCount(calGoodsListRequestDto);
    }

    /**
     * 상품 정산 리스트 조회
     * @param calGoodsListRequestDto
     * @return
     */
    protected List<CalGoodsListDto> getGoodsList(CalGoodsListRequestDto calGoodsListRequestDto) {
        return calGoodsMapper.getGoodsList(calGoodsListRequestDto);
    }

    /**
     * @Desc 상품 정산 리스트 엑셀 다운로드 목록 조회
     * @param CalGoodsListRequestDto : 상품 정산 리스트 검색 조건 request dto
     * @return List<CalGoodsListDto> : 상품 정산 리스트 엑셀 다운로드 목록
     */
 	@UserMaskingRun(system="BOS")
    public List<CalGoodsListDto> getGoodsListExportExcel(CalGoodsListRequestDto calGoodsListRequestDto) {


        List<CalGoodsListDto> itemList = calGoodsMapper.getGoodsListExportExcel(calGoodsListRequestDto);

        // 화면과 동일하게 역순으로 no 지정
        for (int i = itemList.size() - 1; i >= 0; i--) {
//            itemList.get(i).setRowNumber(String.valueOf(itemList.size() - i));
        }

        return itemList;
    }

    /**
     * 상품 정산 (IF 아닌) 리스트 카운트 조회
     * @param calGoodsListRequestDto
     * @return
     */
    protected long getGoodsNotIfListCount(CalGoodsListRequestDto calGoodsListRequestDto) {
        return calGoodsMapper.getGoodsNotIfListCount(calGoodsListRequestDto);
    }

    /**
     * 상품 정산 (IF 아닌) 리스트 조회
     * @param calGoodsListRequestDto
     * @return
     */
    protected List<CalGoodsListDto> getGoodsNotIfList(CalGoodsListRequestDto calGoodsListRequestDto) {
        return calGoodsMapper.getGoodsNotIfList(calGoodsListRequestDto);
    }

    /**
     * 매장 상품 정산 리스트 카운트 조회
     * @param calGoodsListRequestDto
     * @return
     */
    protected long getStoreGoodsListCount(CalGoodsListRequestDto calGoodsListRequestDto) {
        return calGoodsMapper.getStoreGoodsListCount(calGoodsListRequestDto);
    }

    /**
     * 매장 상품 정산 리스트 조회
     * @param calGoodsListRequestDto
     * @return
     */
    protected List<CalGoodsListDto> getStoreGoodsList(CalGoodsListRequestDto calGoodsListRequestDto) {
        return calGoodsMapper.getStoreGoodsList(calGoodsListRequestDto);
    }

}