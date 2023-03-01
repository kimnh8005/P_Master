package kr.co.pulmuone.v1.goods.stock.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.goods.stock.dto.StockExcelUploadRequestDto;

public interface GoodsStockExcelUploadBiz {

	ApiResult<?> addExcelUpload(StockExcelUploadRequestDto stockExcelUploadRequestDto) throws Exception;

	ExcelDownloadDto getStockExprListExportExcel();

}
