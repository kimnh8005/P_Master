package kr.co.pulmuone.v1.batch.goods.stock;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.pulmuone.v1.comm.api.dto.basic.ErpIfStock3PlSearchResponseDto;
import kr.co.pulmuone.v1.comm.api.dto.vo.BaseApiResponseVo;
import kr.co.pulmuone.v1.comm.api.service.ErpApiExchangeService;
import kr.co.pulmuone.v1.comm.exception.BaseException;

/**
 * <PRE>
 * Forbiz Korea
 * ERP 3PL재고수량 조회 API 조회 후 BOS 재고 계산 배치 Biz
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020.11.17  이성준         최초작성
 * =======================================================================
 * </PRE>
 */
@Service
public class BatchGoodsStock3PlSearchBizImpl implements BatchGoodsStock3PlSearchBiz {

	// ERP API 의 재고 수량 조회 인터페이스 ID
    private static final String STOCK_3PL_SEARCH_INTERFACE_ID = "IF_STOCK_3PL_SRCH";

    // runGoodsStock3PlSearchDailyJob 으로 실행하는 해당 배치 ID
    private static final long BATCH_ID = 0;

    @Autowired
    private ErpApiExchangeService erpApiExchangeService;

    @Autowired
    private BatchGoodsStock3PlSearchService batchGoodsStock3PlSearchService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = { BaseException.class, Exception.class })
	public void runGoodsStock3PlSearchJob() throws BaseException {
    	 /*
         * ERP 재고 Data 전체 조회 / BOS 상에 등록된 연동 품목인 경우 IL_ITEM_STOCK_EXPR, IL_ITEM_ERP_STOCK 테이블에 저장
         */
        List<ErpIfStock3PlSearchResponseDto> erpStock3PlSearchList = getErpStock3PlSearchList();
        batchGoodsStock3PlSearchService.runGoodsStock3PlSearchJob(erpStock3PlSearchList, BATCH_ID);
	}

    /*
     * ERP 재고 조회 API 출고처의 전체 재고 Data 조회
     *
     * @return List<ErpIfStock3PlSearchResponseDto> totalErpStock3PlSearchList : ERP 3PL재고조회 API를 통해 수집한 재고 Data
     */
	private List<ErpIfStock3PlSearchResponseDto> getErpStock3PlSearchList() throws BaseException {
		List<ErpIfStock3PlSearchResponseDto> totalErpStock3PlSearchList = new ArrayList<>(); // 재고 Data 전체 취합할 ArrayList

		LocalDate now = LocalDate.now();//현재일짜
        LocalDate agoDay = now.minusDays(1);//하루전 날짜

        String inpDat = agoDay.toString().replaceAll("-", "");

        Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put("inpDat", inpDat); //하루전 기준으로 조회 (API 검색조건에 추가)

        BaseApiResponseVo baseApiResponseVo = null;
        List<ErpIfStock3PlSearchResponseDto> eachPageList = null;

        try {

            // baseApiResponseVo : restTemplate 으로 조회한 응답 결과 객체
            baseApiResponseVo = erpApiExchangeService.get(parameterMap, STOCK_3PL_SEARCH_INTERFACE_ID);

            // baseApiResponseVo => List<T> 역직렬화
            eachPageList = baseApiResponseVo.deserialize(ErpIfStock3PlSearchResponseDto.class);

        } catch (Exception e) { // ERP API 통신 실패시 : BaseException 으로 변환 후 throw 처리
            throw new BaseException(e.getMessage());
        }

        totalErpStock3PlSearchList.addAll(eachPageList);

        int startPage = baseApiResponseVo.getCurrentPage(); // 최초 조회한 페이지 ( 1 페이지 )
        int totalPage = baseApiResponseVo.getTotalPage(); // 해당 검색조건으로 조회시 전체 페이지 수

        if (totalPage > 1) {

            // 최초 조회한 페이지의 다음 페이지부터 조회
            for (int page = startPage + 1; page <= totalPage; page++) {

                parameterMap.put("page", String.valueOf(page));

                try {

                    // baseApiResponseVo : restTemplate 으로 조회한 응답 결과 객체
                    baseApiResponseVo = erpApiExchangeService.get(parameterMap, STOCK_3PL_SEARCH_INTERFACE_ID);

                    // baseApiResponseVo => List<T> 역직렬화
                    eachPageList = baseApiResponseVo.deserialize(ErpIfStock3PlSearchResponseDto.class);

                } catch (Exception e) { // ERP API 통신 실패시 : BaseException 으로 변환 후 throw 처리
                    throw new BaseException(e.getMessage());
                }

                totalErpStock3PlSearchList.addAll(eachPageList);

            }

        }

        return totalErpStock3PlSearchList;
	}

}
