package kr.co.pulmuone.v1.batch.goods.stock;

import kr.co.pulmuone.v1.comm.api.dto.basic.ErpIfOrgaShopOrderSearchResponseDto;
import kr.co.pulmuone.v1.comm.api.dto.basic.ErpIfOrgaShopStockSearchResponseDto;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ItemErpStoreInfoVo;
import kr.co.pulmuone.v1.goods.goods.service.GoodsStoreStockBiz;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * ERP 올가 매장 상품 정보 조회 API 호출
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0   2021-06-08          천혜현            최초작성
 *  
 * =======================================================================
 * </PRE>
 */

@Slf4j
@Service
public class BatchGoodsStockOrgaShopBizImpl implements BatchGoodsStockOrgaShopBiz {

    @Autowired
    private GoodsStoreStockBiz goodsStoreStockBiz;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void runGoodsStockOrgaShopJob() throws BaseException {

        //1. 중계서버 재고조회 API 호출
        List<ErpIfOrgaShopStockSearchResponseDto> erpIfOrgaShopStockList = goodsStoreStockBiz.getErpIfOrgaShopStock();

        //2. 중계서버 미처리된 주문조회 API 호출
        List<ErpIfOrgaShopOrderSearchResponseDto> erpIfOrgaShopOrderCntList = goodsStoreStockBiz.getErpIfOrgaShopOrder();

        //3. 샵풀무원 미연동된 매장 주문 정보 조회
        List<ErpIfOrgaShopOrderSearchResponseDto> mallOrgaShopNonIfOrderList = goodsStoreStockBiz.getOrgaShopNonIfOrderCount(null,null);

        //4. 재고업데이트위한 requestDto 세팅
        HashMap<String, ItemErpStoreInfoVo> stockMap = goodsStoreStockBiz.setStoreStockReqestDto(erpIfOrgaShopStockList,erpIfOrgaShopOrderCntList,mallOrgaShopNonIfOrderList);

        //5. 재고,가격 업데이트
        List<ItemErpStoreInfoVo> storeInfoVoList = new ArrayList<>();
        stockMap.entrySet().forEach(stockVo -> {
            ItemErpStoreInfoVo itemStoreInfoVo = stockVo.getValue();
            storeInfoVoList.add(itemStoreInfoVo);

            //5-1. 재고,가격 업데이트 & 매장품목가격 로그 추가
            try {
                if(itemStoreInfoVo.getIlItemCd() != null && itemStoreInfoVo.getUrStoreId() != null){
                    goodsStoreStockBiz.putIlItemStoreInfoAndPriceLog(itemStoreInfoVo);
                }
            } catch (BaseException e) {
                log.error(e.getMessage());
            }

        });

        //6. 중계서버 미처리주문,샵풀무원 미연동된 매장주문이 없는 품목에 대한 재고 0으로 업데이트 처리
        if(CollectionUtils.isNotEmpty(storeInfoVoList)){
            goodsStoreStockBiz.putIlItemStoreInfoForStock(storeInfoVoList);
        }

        //7. 올가매장상품정보 조회 완료 API 호출
        goodsStoreStockBiz.putErpIfOrgaShopStockFlag(erpIfOrgaShopStockList);

    }

}
