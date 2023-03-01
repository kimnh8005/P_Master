package kr.co.pulmuone.v1.goods.stock.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.goods.stock.dto.vo.ItemErpStockCommonVo;

public interface GoodsStockCommonBiz {

	ApiResult<?> addErpStock(ItemErpStockCommonVo itemErpStockCommonVo) throws Exception;

	ApiResult<?> addErpStockHistory(long ilItemErpStockId) throws Exception;

	ApiResult<?> callSpItemStockCaculated(long ilItemWarehouseId) throws Exception;

	ApiResult<?> getItemErpStockId(ItemErpStockCommonVo itemErpStockCommonVo) throws Exception;

	ApiResult<?> putItemErpStockQty(ItemErpStockCommonVo itemErpStockCommonVo) throws Exception;

	ApiResult<?> putItemErpStockHistoryQty(long ilItemErpStockId) throws Exception;

	ApiResult<?> processMergeErpStock(ItemErpStockCommonVo itemErpStockCommonVo, boolean runProcedure) throws Exception;

}
