package kr.co.pulmuone.v1.goods.stock.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.goods.stock.dto.StockExcelUploadDetlListRequestDto;

public interface GoodsStockExcelUploadDetlListBiz {

	ApiResult<?> getStockUploadDetlList(StockExcelUploadDetlListRequestDto dto) throws Exception;

	ExcelDownloadDto getStockUploadDetlExcel(StockExcelUploadDetlListRequestDto dto);

}
