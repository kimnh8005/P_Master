package kr.co.pulmuone.v1.goods.stock.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.goods.stock.dto.StockListRequestDto;

public interface GoodsStockListBiz {

	ApiResult<?> getStockList(StockListRequestDto dto) throws Exception;

	ApiResult<?> getStockInfo(StockListRequestDto stockListRequestDto) throws Exception;

	ApiResult<?> getStockDetailList(StockListRequestDto stockListRequestDto) throws Exception;

	ApiResult<?> getStockPreOrderPopupInfo(StockListRequestDto stockListRequestDto) throws Exception;

	ApiResult<?> putStockPreOrder(StockListRequestDto stockListRequestDto) throws Exception;

	ExcelDownloadDto getStockListExportExcel(StockListRequestDto dto);

	ApiResult<?> getStockDeadlineDropDownList(StockListRequestDto dto) throws Exception;

	ApiResult<?> putStockDeadlineInfo(StockListRequestDto stockListRequestDto) throws Exception;

	ApiResult<?> putStockAdjustCount(StockListRequestDto stockListRequestDto) throws Exception;

}
