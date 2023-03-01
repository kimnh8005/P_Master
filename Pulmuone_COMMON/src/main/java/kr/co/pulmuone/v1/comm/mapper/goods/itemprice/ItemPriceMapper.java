package kr.co.pulmuone.v1.comm.mapper.goods.itemprice;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.pulmuone.v1.goods.itemprice.dto.vo.ItemPriceListVo;
import kr.co.pulmuone.v1.goods.itemprice.dto.vo.ItemPriceOrigVo;


@Mapper
public interface ItemPriceMapper {

	String getItemPriceMinEndDt(ItemPriceOrigVo itemPriceOrigVo);

	int addItemPriceByOrig(ItemPriceListVo itemPriceListVo);

	int putPastItemPrice(ItemPriceListVo itemPriceListVo);

	List<ItemPriceListVo> getItemPriceStartDateUnderList(ItemPriceOrigVo itemPriceOrigVo);

	int putItemBatchDate(String ilItemCode);

}
