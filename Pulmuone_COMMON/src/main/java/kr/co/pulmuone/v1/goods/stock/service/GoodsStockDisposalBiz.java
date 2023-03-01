package kr.co.pulmuone.v1.goods.stock.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.goods.stock.dto.GoodsStockDisposalRequestDto;

public interface GoodsStockDisposalBiz {

    ApiResult<?> getGoodsStockDisposalList(GoodsStockDisposalRequestDto goodsNutritionRequestDto);
    ExcelDownloadDto getGoodsStockDisposalExcelList(GoodsStockDisposalRequestDto goodsNutritionRequestDto);

}
