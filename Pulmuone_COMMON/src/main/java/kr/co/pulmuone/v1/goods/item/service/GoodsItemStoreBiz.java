package kr.co.pulmuone.v1.goods.item.service;

import java.util.List;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.goods.item.dto.*;

public interface GoodsItemStoreBiz {

    public List<ItemStoreInfoDto> getStoreList(String ilItemCode) throws Exception;

    public ItemStorePriceLogResponseDto getStorePriceLogList(ItemStorePriceLogRequestDto reqDto) throws Exception;

    ItemStoreStockResponseDto getItemStoreStockList(ItemStoreStockRequestDto itemStoreStockRequestDto) throws Exception;

    ExcelDownloadDto getItemStoreStockListExcel(ItemStoreStockRequestDto itemStoreStockRequestDto) throws Exception;

}
