package kr.co.pulmuone.v1.goods.item.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemPoListRequestDto;

public interface GoodsItemPoListBiz {

	ApiResult<?> getPoList(ItemPoListRequestDto dto);

	ApiResult<?> getPoInfoList(ItemPoListRequestDto dto);

	ApiResult<?> getPoResultList(ItemPoListRequestDto dto);

	ApiResult<?> getPoTpList(ItemPoListRequestDto dto);

	ApiResult<?> getOnChangePoTpList(ItemPoListRequestDto dto);

	ApiResult<?> getErpCtgryList(ItemPoListRequestDto dto);

	ApiResult<?> putItemPo(ItemPoListRequestDto dto) throws Exception;

	ExcelDownloadDto getPfPoListExportExcel(ItemPoListRequestDto dto);

	ExcelDownloadDto getOgPoListExportExcel(ItemPoListRequestDto dto);

	ExcelDownloadDto getPfPoResultListExportExcel(ItemPoListRequestDto dto);

	ExcelDownloadDto getOgPoResultListExportExcel(ItemPoListRequestDto dto);

}
