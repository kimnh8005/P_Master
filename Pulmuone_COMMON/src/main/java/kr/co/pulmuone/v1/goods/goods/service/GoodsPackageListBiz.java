package kr.co.pulmuone.v1.goods.goods.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsPackageListRequestDto;

public interface GoodsPackageListBiz {

	ApiResult<?> getGoodsPackageList(GoodsPackageListRequestDto paramDto);

	ApiResult<?> getGoodsPackageDetailList(GoodsPackageListRequestDto paramDto);

	ExcelDownloadDto getGoodsPackageExcelList(GoodsPackageListRequestDto paramDto);


}
