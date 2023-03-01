package kr.co.pulmuone.v1.goods.item.service;

import java.util.List;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.goods.item.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <PRE>
* Forbiz Korea
* 상품품목 매장 BizImpl
 * </PRE>
 *
 * <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2021. 06. 10.                홍진영          최초작성
* =======================================================================
 * </PRE>
 */
@Service
public class GoodsItemStoreBizImpl implements GoodsItemStoreBiz {

    @Autowired
    GoodsItemStoreService goodsItemStoreService;

    /**
     * 품목 매장 정보 리스트
     */
	@Override
	public List<ItemStoreInfoDto> getStoreList(String ilItemCode) throws Exception {
		return goodsItemStoreService.getStoreList(ilItemCode);
	}

	/**
	 * 품목 매장 가격 히스토리 정보
	 */
	@Override
	public ItemStorePriceLogResponseDto getStorePriceLogList(ItemStorePriceLogRequestDto reqDto) throws Exception {
		return goodsItemStoreService.getStorePriceLogList(reqDto);
	}

	/**
	 * 매장 재고 리스트
	 * @param itemStoreStockRequestDto
	 * @return
	 * @throws Exception
	 */
	@Override
	public ItemStoreStockResponseDto getItemStoreStockList(ItemStoreStockRequestDto itemStoreStockRequestDto) throws Exception {
		ItemStoreStockResponseDto itemStoreStockResponseDto = new ItemStoreStockResponseDto();
		Page<ItemStoreStockDto> itemStoreStockDto = goodsItemStoreService.getItemStoreStockList(itemStoreStockRequestDto);
		itemStoreStockResponseDto.setRows(itemStoreStockDto.getResult());
		itemStoreStockResponseDto.setTotal(itemStoreStockDto.getTotal());
		return itemStoreStockResponseDto;
	}

	@Override
	public ExcelDownloadDto getItemStoreStockListExcel(ItemStoreStockRequestDto itemStoreStockRequestDto) throws Exception {
		return goodsItemStoreService.getItemStoreStockListExcel(itemStoreStockRequestDto);
	}
}
