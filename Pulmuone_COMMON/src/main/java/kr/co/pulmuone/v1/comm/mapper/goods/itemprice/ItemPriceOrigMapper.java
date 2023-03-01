package kr.co.pulmuone.v1.comm.mapper.goods.itemprice;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.pulmuone.v1.goods.itemprice.dto.vo.ItemPriceOrigVo;


@Mapper
public interface ItemPriceOrigMapper {

	int addItemPriceOrig(ItemPriceOrigVo itemPriceOrigVo);

	List<ItemPriceOrigVo> getItemPriceOrigListNoBatchDate();

	int putItemPriceOrigBatchDate(long ilItemPriceOriginalId);

	int getItemPriceOrigCount(String ilItemCode);

	ItemPriceOrigVo getItemPriceOrigLastly(String ilItemCode);

	ItemPriceOrigVo getItemPriceOrigLastlyByErpProductType(@Param("ilItemCode") String ilItemCode, @Param("erpProductType") String erpProductType);

}
