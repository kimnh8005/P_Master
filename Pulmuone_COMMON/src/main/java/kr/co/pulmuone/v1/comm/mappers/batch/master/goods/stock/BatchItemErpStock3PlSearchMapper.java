package kr.co.pulmuone.v1.comm.mappers.batch.master.goods.stock;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.pulmuone.v1.batch.goods.stock.dto.vo.ItemErpStock3PlSearchResultVo;
import kr.co.pulmuone.v1.comm.api.dto.basic.ErpIfStock3PlSearchResponseDto;

@Mapper
public interface BatchItemErpStock3PlSearchMapper {

	int addStock3PlSearch(ItemErpStock3PlSearchResultVo vo);

	String getConfigValue(String psKey);

	List<ItemErpStock3PlSearchResultVo> getStockInfoList(ErpIfStock3PlSearchResponseDto dto);

	List<ItemErpStock3PlSearchResultVo> getStockOrgList(ErpIfStock3PlSearchResponseDto dto);

	List<ItemErpStock3PlSearchResultVo> getStockSubOrgList(ErpIfStock3PlSearchResponseDto dto);

	List<ItemErpStock3PlSearchResultVo> getStockSubNotOrgList(ErpIfStock3PlSearchResponseDto dto);

	int getSearchTypeCnt(ErpIfStock3PlSearchResponseDto dto);

	int getSubSearchTypeCnt(ErpIfStock3PlSearchResponseDto dto);

	int addErpStock(ItemErpStock3PlSearchResultVo vo);

	int putIlItemWarehouse(ItemErpStock3PlSearchResultVo vo);

}
