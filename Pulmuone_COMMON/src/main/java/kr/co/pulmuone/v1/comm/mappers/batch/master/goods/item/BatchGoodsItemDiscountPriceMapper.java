package kr.co.pulmuone.v1.comm.mappers.batch.master.goods.item;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.pulmuone.v1.goods.discount.dto.vo.DiscountInfoVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemInfoVo;
import kr.co.pulmuone.v1.goods.itemprice.dto.vo.ItemPriceDiscountIfTempVo;
import kr.co.pulmuone.v1.goods.itemprice.dto.vo.ItemPriceOrigVo;

@Mapper
public interface BatchGoodsItemDiscountPriceMapper {

	int addItemPriceOrig(ItemPriceOrigVo itemPriceOrigVo);

	int addItemDiscount(DiscountInfoVo discountInfoVo);

    List<ItemInfoVo> getGoodsIdListByItemCd(String ilItemCode);

    /**
     * @Desc 상품할인 등록/수정
     * @param discountInfoVo
     * @return string
     */
    int addGoodsDiscountByBatch(DiscountInfoVo discountInfoVo);

    int putGoodsDiscountByBatch(DiscountInfoVo discountInfoVo);

    Long checkDuplicateErpDiscount(DiscountInfoVo discountInfoVo);


    /**
     * @Desc 상품할인 배치, 상품가격변경처리여부 수정
     * @param discountInfoVo
     * @return string
     */
	int putGoodsBatchChange(@Param("ilGoodsId") Long ilGoodsId);

	void spGoodsPriceUpdateWhenItemPriceChanges(@Param("ilItemCode") String ilItemCode, @Param("inDebugFlag") boolean inDebugFlag);

	void spPackageGoodsPriceUpdateWhenItemPriceChanges(@Param("inDebugFlag") boolean inDebugFlag);

	int addItemPriceDiscountIfTemp(ItemPriceDiscountIfTempVo priceDiscountIfTempVo);
	void delItemPriceDiscountIfTemp();
	public List<ItemPriceDiscountIfTempVo> getItemPriceDiscountIfTempList();

}
