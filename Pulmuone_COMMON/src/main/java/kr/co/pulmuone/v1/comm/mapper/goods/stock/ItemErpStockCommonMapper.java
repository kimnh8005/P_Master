package kr.co.pulmuone.v1.comm.mapper.goods.stock;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.pulmuone.v1.goods.stock.dto.vo.ItemErpStockCommonVo;

@Mapper
public interface ItemErpStockCommonMapper {

	public int addItemErpStock(ItemErpStockCommonVo itemErpStockCommonVo);

	public int addItemErpStockHistory(@Param("ilItemErpStockId") long ilItemErpStockId);

	public int callSpItemStockCaculated(@Param("ilItemWarehouseId") long ilItemWarehouseId);

	public ItemErpStockCommonVo getItemErpStockId(ItemErpStockCommonVo itemErpStockCommonVo);

	public int putItemErpStockQty(ItemErpStockCommonVo itemErpStockCommonVo);

	public int putItemErpStockHistoryQty(@Param("ilItemErpStockId") long ilItemErpStockId);

}
