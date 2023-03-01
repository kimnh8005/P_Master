package kr.co.pulmuone.v1.comm.mapper.goods.stock;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.pulmuone.v1.goods.stock.dto.StockOrderRequestDto;
import kr.co.pulmuone.v1.goods.stock.dto.vo.StockOrderResultVo;

@Mapper
public interface GoodsStockOrderMapper {

	StockOrderResultVo getIlItemWarehouseIdInfo(StockOrderRequestDto dto);

	int addErpStockOrder(StockOrderResultVo vo);

	int addErpStockOrderHistory(@Param("ilItemErpStockId") long ilItemErpStockId);

	int spItemStockCaculated(StockOrderResultVo vo);

	int getNotIfStockCnt(StockOrderResultVo vo);

	int putNotIfStockCnt(StockOrderResultVo vo);

}
