package kr.co.pulmuone.v1.goods.stock.service;


import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.goods.stock.dto.StockDeadlineRequestDto;

public interface GoodsStockDeadlineBiz {

	ApiResult<?> getStockDeadlineList(StockDeadlineRequestDto stockDeadlineRequestDto) throws Exception;

	ApiResult<?> addStockDeadline(StockDeadlineRequestDto stockDeadlineRequestDto) throws Exception;

	ApiResult<?> getStockDeadline(StockDeadlineRequestDto stockDeadlineRequestDto) throws Exception;

	ApiResult<?> putStockDeadline(StockDeadlineRequestDto stockDeadlineRequestDto) throws Exception;

	ApiResult<?> getStockDeadlineHistList(StockDeadlineRequestDto stockDeadlineRequestDto) throws Exception;

}
