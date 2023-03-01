package kr.co.pulmuone.v1.comm.mapper.goods.stock;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.goods.stock.dto.StockUploadListRequestDto;
import kr.co.pulmuone.v1.goods.stock.dto.vo.StockUploadListResultVo;

@Mapper
public interface GoodsStockExcelUploadListMapper {

	Page<StockUploadListResultVo> getStockUploadList(StockUploadListRequestDto dto);

	List<StockUploadListResultVo> getStockUploadListExcelDownload(StockUploadListRequestDto dto);

}
