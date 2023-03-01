package kr.co.pulmuone.v1.comm.mapper.goods.stock;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.goods.stock.dto.StockListRequestDto;
import kr.co.pulmuone.v1.goods.stock.dto.vo.StockListResultVo;

@Mapper
public interface GoodsStockListMapper {

	Page<StockListResultVo> getStockList(StockListRequestDto dto);

	StockListResultVo getStockInfo(StockListRequestDto stockListRequestDto);

	Page<StockListResultVo> getStockDetailList(StockListRequestDto stockListRequestDto);

	StockListResultVo getStockPreOrderPopupInfo(StockListRequestDto stockListRequestDto);

	int putStockPreOrder(StockListRequestDto stockListRequestDto);

	List<StockListResultVo> getStockDeadlineDropDownList(StockListRequestDto dto);

	int putStockDeadlineInfo(StockListRequestDto stockListRequestDto);

	int putStockDeadlineInfoBasicYn(StockListRequestDto stockListRequestDto);

	StockListResultVo getPackageGoodsStockInfo(StockListRequestDto stockListRequestDto);

}
