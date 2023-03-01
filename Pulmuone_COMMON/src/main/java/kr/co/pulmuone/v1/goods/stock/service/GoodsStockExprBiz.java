package kr.co.pulmuone.v1.goods.stock.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.goods.stock.dto.StockExprRequestDto;

public interface GoodsStockExprBiz {

	ApiResult<?> getStockExprList(StockExprRequestDto dto) throws Exception;

	ApiResult<?> getStockErpList(StockExprRequestDto dto) throws Exception;

	ApiResult<?> getStockNonErpList(StockExprRequestDto dto) throws Exception;

	ApiResult<?> putStockNonErp(StockExprRequestDto dto) throws Exception;

	ExcelDownloadDto getStockExprExportExcel(StockExprRequestDto dto);

	ExcelDownloadDto getStockErpExportExcel(StockExprRequestDto dto);

	ExcelDownloadDto getStockNonErpExportExcel(StockExprRequestDto dto);

}
