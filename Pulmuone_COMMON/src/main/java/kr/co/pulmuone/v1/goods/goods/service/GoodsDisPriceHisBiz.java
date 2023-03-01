package kr.co.pulmuone.v1.goods.goods.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsDisPriceHisRequestDto;

public interface GoodsDisPriceHisBiz {

	ApiResult<?> getGoodsDisPriceHisList(GoodsDisPriceHisRequestDto paramDto);

	ExcelDownloadDto getGoodsDisPriceExcelList(GoodsDisPriceHisRequestDto paramDto);


}
