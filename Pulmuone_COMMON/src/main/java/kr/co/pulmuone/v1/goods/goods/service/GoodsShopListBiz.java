package kr.co.pulmuone.v1.goods.goods.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsShopListRequestDto;

public interface GoodsShopListBiz {

	ApiResult<?> getGoodsShopList(GoodsShopListRequestDto paramDto);

	ExcelDownloadDto getGoodsShopListExcel(GoodsShopListRequestDto paramDto);
}
