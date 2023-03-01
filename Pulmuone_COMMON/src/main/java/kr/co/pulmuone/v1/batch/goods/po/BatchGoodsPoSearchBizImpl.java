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

import kr.co.pulmuone.v1.comm.api.dto.basic.ErpIfPoSearchResponseDto;
import kr.co.pulmuone.v1.comm.api.dto.vo.BaseApiResponseVo;
import kr.co.pulmuone.v1.comm.api.service.ErpApiExchangeService;
import kr.co.pulmuone.v1.comm.exception.BaseException;

/**
 * <PRE>
 * Forbiz Korea
 * 올가R2발주스케쥴 조회 배치 Biz
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
public class BatchGoodsPoSearchBizImpl implements BatchGoodsPoSearchBiz {

	// 올가R2발주스케쥴 조회 인터페이스 ID
    private static final String PO_SEARCH_INTERFACE_ID = "IF_PURCHASESCH_SRCH";

    // runGoodsPoSearchJob 으로 실행하는 해당 배치 ID
    private static final long BATCH_ID = 0;

    @Autowired
    private ErpApiExchangeService erpApiExchangeService;

    @Autowired
    private BatchGoodsPoSearchService batchGoodsPoSearchService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = { BaseException.class, Exception.class })
	public void runGoodsPoSearchJob() throws BaseException {
    	 /*
         * ERP 올가R2발주스케쥴 조회
         */
        List<ErpIfPoSearchResponseDto> getErpPoSearchList = getErpPoSearchList();
        batchGoodsPoSearchService.runGoodsPoSearchJob(getErpPoSearchList, BATCH_ID);
	}

    /*
     * ERP 올가R2발주스케쥴 조회
     *
     * @return List<ErpIfPoSearchResponseDto> totalErpPoSearchList : ERP 올가R2발주스케쥴 조회  Data
     */
	private List<ErpIfPoSearchResponseDto> getErpPoSearchList() throws BaseException {
		List<ErpIfPoSearchResponseDto> totalErpPoSearchList = new ArrayList<>(); // 발주 Data 전체 취합할 ArrayList

		LocalDate now = LocalDate.now();//현재일짜
        LocalDate agoWeek = now.minusDays(6);//1주전 데이타 조회

        String updDat = agoWeek.toString().replaceAll("-", "")+"000000"+"~"+now.toString().replaceAll("-", "")+"235959";//조회기간

        //updDat = "20200818000000~20200818235959";(샘플 조건)

        Map<String, String> parameterMap = new HashMap<>();
//        parameterMap.put("itmNo", "0934248"); // 테스트용 품목코드
//        parameterMap.put("updDat", updDat); //API 검색조건에 추가

        BaseApiResponseVo baseApiResponseVo = null;
        List<ErpIfPoSearchResponseDto> eachPageList = null;

        try {

            // baseApiResponseVo : restTemplate 으로 조회한 응답 결과 객체
            baseApiResponseVo = erpApiExchangeService.get(parameterMap, PO_SEARCH_INTERFACE_ID);

            // baseApiResponseVo => List<T> 역직렬화
            eachPageList = baseApiResponseVo.deserialize(ErpIfPoSearchResponseDto.class);

        } catch (Exception e) { // ERP API 통신 실패시 : BaseException 으로 변환 후 throw 처리
            throw new BaseException(e.getMessage());
        }

        totalErpPoSearchList.addAll(eachPageList);

        int startPage = baseApiResponseVo.getCurrentPage(); // 최초 조회한 페이지 ( 1 페이지 )
        int totalPage = baseApiResponseVo.getTotalPage(); // 해당 검색조건으로 조회시 전체 페이지 수

        if (totalPage > 1) {

            // 최초 조회한 페이지의 다음 페이지부터 조회
            for (int page = startPage + 1; page <= totalPage; page++) {

                parameterMap.put("page", String.valueOf(page));

                try {

                    // baseApiResponseVo : restTemplate 으로 조회한 응답 결과 객체
                    baseApiResponseVo = erpApiExchangeService.get(parameterMap, PO_SEARCH_INTERFACE_ID);

                    // baseApiResponseVo => List<T> 역직렬화
                    eachPageList = baseApiResponseVo.deserialize(ErpIfPoSearchResponseDto.class);

                } catch (Exception e) { // ERP API 통신 실패시 : BaseException 으로 변환 후 throw 처리
                    throw new BaseException(e.getMessage());
                }

                totalErpPoSearchList.addAll(eachPageList);

            }

        }

        return totalErpPoSearchList;
	}

}
