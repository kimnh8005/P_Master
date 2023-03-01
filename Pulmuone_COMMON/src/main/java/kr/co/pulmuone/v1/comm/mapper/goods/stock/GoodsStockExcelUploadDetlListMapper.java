package kr.co.pulmuone.v1.comm.mapper.goods.stock;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.goods.stock.dto.StockExcelUploadDetlListRequestDto;
import kr.co.pulmuone.v1.goods.stock.dto.vo.StockExcelUploadDetlListResultVo;

@Mapper
public interface GoodsStockExcelUploadDetlListMapper {

	Page<StockExcelUploadDetlListResultVo> getStockUploadDetlList(StockExcelUploadDetlListRequestDto stockListRequestDto);

}
