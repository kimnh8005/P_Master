package kr.co.pulmuone.v1.goods.stock.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.goods.stock.dto.StockOrderRequestDto;
import kr.co.pulmuone.v1.goods.stock.dto.vo.StockOrderResultVo;

public interface GoodsStockOrderBiz {

	ApiResult<?> stockOrderHandle(StockOrderRequestDto dto) throws Exception;

	ApiResult<?> putOrderStock(Long odOrderId, String orderYn) throws Exception;

	ApiResult<?> putOrderStockByOdOrderDetlId(Long odOrderDetlId, String orderYn) throws Exception;

	ApiResult<?> validStockOrderHandle(StockOrderRequestDto dto) throws Exception;
}
