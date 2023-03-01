package kr.co.pulmuone.v1.goods.stock.service;

import java.util.List;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.goods.stock.dto.StockUploadListRequestDto;
import kr.co.pulmuone.v1.goods.stock.dto.vo.StockUploadListResultVo;

public interface GoodsStockExcelUploadListBiz {

	ApiResult<?> getStockUploadList(StockUploadListRequestDto stockUploadListRequestDto) throws Exception;

	List<StockUploadListResultVo> getStockUploadListExcelDownload(StockUploadListRequestDto stockUploadListRequestDto) throws Exception;

}
