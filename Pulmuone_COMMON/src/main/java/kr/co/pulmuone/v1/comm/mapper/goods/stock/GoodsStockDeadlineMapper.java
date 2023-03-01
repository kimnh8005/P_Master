package kr.co.pulmuone.v1.comm.mapper.goods.stock;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.goods.stock.dto.StockDeadlineHistParamDto;
import kr.co.pulmuone.v1.goods.stock.dto.StockDeadlineRequestDto;
import kr.co.pulmuone.v1.goods.stock.dto.vo.StockDeadlineHistResultVo;
import kr.co.pulmuone.v1.goods.stock.dto.vo.StockDeadlineResultVo;


@Mapper
public interface GoodsStockDeadlineMapper {

	int getStockDeadlineListCount(StockDeadlineRequestDto stockDeadlineRequestDto);

	Page<StockDeadlineResultVo> getStockDeadlineList(StockDeadlineRequestDto stockDeadlineRequestDto);

	StockDeadlineResultVo getStockDeadline(StockDeadlineRequestDto stockDeadlineRequestDto);

	int addStockDeadline(StockDeadlineRequestDto stockDeadlineRequestDto);

	int addStockDeadlineHist(StockDeadlineHistParamDto stockDeadlineHistParamDto);

	List<StockDeadlineResultVo> getStockDeadlineForCheck(StockDeadlineRequestDto stockDeadlineRequestDto);

	HashMap getStockDeadlineCheckCountByPeriod(StockDeadlineRequestDto stockDeadlineRequestDto);

	int getStockDeadlineBasicYnCount(StockDeadlineRequestDto stockDeadlineRequestDto);

	String getStockDeadlineBasicYnCheck(StockDeadlineRequestDto stockDeadlineRequestDto);

	int putStockDeadline(StockDeadlineRequestDto stockDeadlineRequestDto);

	int putStockDeadlineBasicYn(StockDeadlineRequestDto stockDeadlineRequestDto);

	int getStockDeadlineHistListCount(StockDeadlineRequestDto stockDeadlineRequestDto);

	Page<StockDeadlineHistResultVo> getStockDeadlineHistList(StockDeadlineRequestDto stockDeadlineRequestDto);

}
