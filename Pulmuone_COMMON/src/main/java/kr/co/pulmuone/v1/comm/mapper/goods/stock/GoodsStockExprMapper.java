package kr.co.pulmuone.v1.comm.mapper.goods.stock;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.goods.stock.dto.StockExprRequestDto;
import kr.co.pulmuone.v1.goods.stock.dto.vo.StockExprResultVo;

@Mapper
public interface GoodsStockExprMapper {

	Page<StockExprResultVo> getStockExprList(StockExprRequestDto dto);

	Page<StockExprResultVo> getStockErpList(StockExprRequestDto dto);

	Page<StockExprResultVo> getStockNonErpList(StockExprRequestDto dto);

	int putStockNonErp(StockExprRequestDto dto);

}
