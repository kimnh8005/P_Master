package kr.co.pulmuone.v1.comm.mapper.goods.item;

import java.util.List;

import kr.co.pulmuone.v1.goods.item.dto.*;
import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;

@Mapper
public interface GoodsItemStoreMapper {

	/**
	 * 품목 매장 정보 리스트
	 *
	 * @param ilItemCode
	 * @return
	 */
	List<ItemStoreInfoDto> getStoreList(String ilItemCode);

	/**
	 * 품목 매장 가격 리스트 목록
	 *
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	Page<ItemStorePriceLogDto> getStorePriceLogList(ItemStorePriceLogRequestDto reqDto) throws Exception;

	/**
	 * 매장 재고 리스트
	 *
	 * @param itemStoreStockRequestDto
	 * @return
	 * @throws Exception
	 */
	Page<ItemStoreStockDto> getItemStoreStockList(ItemStoreStockRequestDto itemStoreStockRequestDto) throws Exception;
}
