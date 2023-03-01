package kr.co.pulmuone.v1.batch.goods.po;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.pulmuone.v1.comm.api.dto.basic.ErpIfPoOffSearchResponseDto;
import kr.co.pulmuone.v1.comm.api.dto.vo.BaseApiResponseVo;
import kr.co.pulmuone.v1.comm.api.service.ErpApiExchangeService;
import kr.co.pulmuone.v1.comm.exception.BaseException;

/**
 * <PRE>
 * Forbiz Korea
 * 올가오프라인발주상태 조회 배치 Biz
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0   2021-02-16   이성준            최초작성
 * =======================================================================
 * </PRE>
 */
@Service
public class BatchGoodsPoOffSearchBizImpl implements BatchGoodsPoOffSearchBiz {

	// 올가오프라인발주상태 조회 인터페이스 ID
    private static final String PO_OFF_SEARCH_INTERFACE_ID = "IF_ORGAOFFLINE_SRCH";

    // runGoodsPoOffSearchJob 으로 실행하는 해당 배치 ID
    private static final long BATCH_ID = 0;

    @Autowired
    private ErpApiExchangeService erpApiExchangeService;

    @Autowired
    private BatchGoodsPoOffSearchService batchGoodsPoOffSearchService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = { BaseException.class, Exception.class })
	public void runGoodsPoOffSearchJob() throws BaseException {
    	 /*
         * 올가오프라인발주상태 조회
         */
        List<ErpIfPoOffSearchResponseDto> getErpPoOffSearchList = getErpPoOffSearchList();
        batchGoodsPoOffSearchService.runGoodsPoOffSearchJob(getErpPoOffSearchList, BATCH_ID);
	}

    /*
     * 올가오프라인발주상태 조회
     *
     * @return List<ErpIfPoOffSearchResponseDto> totalErpPoOffSearchList : 올가오프라인발주상태 조회 Data
     */
	private List<ErpIfPoOffSearchResponseDto> getErpPoOffSearchList() throws BaseException {
		List<ErpIfPoOffSearchResponseDto> totalErpPoOffSearchList = new ArrayList<>(); // 발주 Data 전체 취합할 ArrayList

		LocalDate now = LocalDate.now();//현재일짜
        LocalDate agoWeek = now.minusDays(6);//1주전 데이타 조회

        String updDat = agoWeek.toString().replaceAll("-", "")+"000000"+"~"+now.toString().replaceAll("-", "")+"235959";//조회기간

        //updDat = "20210106160440~20210106160445";//(샘플 조건)

        Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put("updDat", updDat); //API 검색조건에 추가

        BaseApiResponseVo baseApiResponseVo = null;
        List<ErpIfPoOffSearchResponseDto> eachPageList = null;

        try {

            // baseApiResponseVo : restTemplate 으로 조회한 응답 결과 객체
            baseApiResponseVo = erpApiExchangeService.get(parameterMap, PO_OFF_SEARCH_INTERFACE_ID);

            // baseApiResponseVo => List<T> 역직렬화
            eachPageList = baseApiResponseVo.deserialize(ErpIfPoOffSearchResponseDto.class);

        } catch (Exception e) { // ERP API 통신 실패시 : BaseException 으로 변환 후 throw 처리
            throw new BaseException(e.getMessage());
        }

        totalErpPoOffSearchList.addAll(eachPageList);

        int startPage = baseApiResponseVo.getCurrentPage(); // 최초 조회한 페이지 ( 1 페이지 )
        int totalPage = baseApiResponseVo.getTotalPage(); // 해당 검색조건으로 조회시 전체 페이지 수

        if (totalPage > 1) {

            // 최초 조회한 페이지의 다음 페이지부터 조회
            for (int page = startPage + 1; page <= totalPage; page++) {

                parameterMap.put("page", String.valueOf(page));

                try {

                    // baseApiResponseVo : restTemplate 으로 조회한 응답 결과 객체
                    baseApiResponseVo = erpApiExchangeService.get(parameterMap, PO_OFF_SEARCH_INTERFACE_ID);

                    // baseApiResponseVo => List<T> 역직렬화
                    eachPageList = baseApiResponseVo.deserialize(ErpIfPoOffSearchResponseDto.class);

                } catch (Exception e) { // ERP API 통신 실패시 : BaseException 으로 변환 후 throw 처리
                    throw new BaseException(e.getMessage());
                }

                totalErpPoOffSearchList.addAll(eachPageList);

            }

        }

        return totalErpPoOffSearchList;
	}

}
