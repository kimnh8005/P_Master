package kr.co.pulmuone.v1.goods.item.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemPoRequestDto;

public interface GoodsItemPoRequestBiz {

	ApiResult<?> getPoRequestList(ItemPoRequestDto paramDto);

	ApiResult<?> addItemPoRequest(ItemPoRequestDto paramDto) throws Exception;

	ApiResult<?> getPoRequest(String ilPoEventId);

	ApiResult<?> putPoRequest(ItemPoRequestDto paramDto) throws Exception;

	ApiResult<?> delPoRequest(ItemPoRequestDto paramDto) throws Exception;

	ApiResult<?> addPoRequestExcelUpload(ItemPoRequestDto paramDto) throws Exception;

	ApiResult<?> getPoRequestUploadList(ItemPoRequestDto paramDto);

	ExcelDownloadDto createPoRequestUplodFailList(ItemPoRequestDto paramDto);

	ExcelDownloadDto createPoRequestList(ItemPoRequestDto paramDto);


}
