package kr.co.pulmuone.v1.comm.mappers.batch.master.goods.stock;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.pulmuone.v1.batch.goods.stock.dto.vo.ItemErpStockReCalQtyResultVo;
import kr.co.pulmuone.v1.comm.api.dto.basic.ErpStockReCalQtyResponseDto;

@Mapper
public interface BatchItemErpStockReCalQtyMapper {

	List<ErpStockReCalQtyResponseDto> getErpStockOrgList();

	int getStockExprSum(ErpStockReCalQtyResponseDto dto);

	Integer getStockExprExcessSum(ErpStockReCalQtyResponseDto dto);

	int getStockExprCount(ErpStockReCalQtyResponseDto dto);

	int putStockExprTotalSum(ItemErpStockReCalQtyResultVo vo);

	int putItemErpStock(ErpStockReCalQtyResponseDto dto);

	int putItemErpStockHistory(ErpStockReCalQtyResponseDto dto);

	int addItemStockExpr(ItemErpStockReCalQtyResultVo vo);

	ItemErpStockReCalQtyResultVo getStockExprSearch(ErpStockReCalQtyResponseDto dto);

	List<ItemErpStockReCalQtyResultVo> getStockExprSearchList(ErpStockReCalQtyResponseDto dto);

	ItemErpStockReCalQtyResultVo getStockExprSearchByExpirationDate(ItemErpStockReCalQtyResultVo dto);
}
