package kr.co.pulmuone.v1.goods.goods.service;

import kr.co.pulmuone.v1.comm.api.dto.basic.ErpIfOrgaShopOrderSearchResponseDto;
import kr.co.pulmuone.v1.comm.api.dto.basic.ErpIfOrgaShopStockSearchResponseDto;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ItemErpStoreInfoVo;

import java.util.HashMap;
import java.util.List;

public interface GoodsStoreStockBiz {

    List<ErpIfOrgaShopStockSearchResponseDto> getErpIfOrgaShopStock() throws BaseException;

    List<ErpIfOrgaShopStockSearchResponseDto> getErpIfOrgaShopStock(String urStoreId, String ilItemCd) throws BaseException;

    List<ErpIfOrgaShopOrderSearchResponseDto> getErpIfOrgaShopOrder() throws BaseException;

    List<ErpIfOrgaShopOrderSearchResponseDto> getErpIfOrgaShopOrder(String urStoreId, String ilItemCd) throws BaseException;

    List<ErpIfOrgaShopOrderSearchResponseDto> getOrgaShopNonIfOrderCount(String urStoreId, String ilItemCd);

    HashMap<String, ItemErpStoreInfoVo> setStoreStockReqestDto(List<ErpIfOrgaShopStockSearchResponseDto> erpIfOrgaShopStockList, List<ErpIfOrgaShopOrderSearchResponseDto> erpIfOrgaShopOrderCntList, List<ErpIfOrgaShopOrderSearchResponseDto> mallOrgaShopNonIfOrderList);

    int putIlItemStoreInfo(ItemErpStoreInfoVo itemStoreInfoVo);

    void saveIlItemStorePriceLog(ItemErpStoreInfoVo itemStoreInfoVo);

    int putIlItemStoreInfoForStock(List<ItemErpStoreInfoVo> itemStoreInfoList);

    void putErpIfOrgaShopStockFlag(List<ErpIfOrgaShopStockSearchResponseDto> erpIfOrgaShopStockList);

    void putIlItemStoreInfoAndPriceLog(ItemErpStoreInfoVo itemStoreInfoVo) throws BaseException;

    void getGoodsStockOrgaShop(String urStoreId, String ilItemCd) throws Exception;
    
    boolean isStockPreOrderWithGoodsId(long ilGoodsId);
}
