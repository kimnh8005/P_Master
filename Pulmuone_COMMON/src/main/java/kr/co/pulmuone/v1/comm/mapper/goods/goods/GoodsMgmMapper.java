package kr.co.pulmuone.v1.comm.mapper.goods.goods;

import kr.co.pulmuone.v1.goods.goods.dto.GoodsDiscountRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsPriceInfoResultVo;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GoodsMgmMapper {
	List<GoodsPriceInfoResultVo> goodsPrice(@Param("ilGoodsId") String ilGoodsId,@Param("taxYn") String taxYn) throws Exception;

	List<GoodsPriceInfoResultVo> goodsDiscountList(@Param("ilGoodsId") String ilGoodsId,@Param("discountTypeCode") String discountTypeCode) throws Exception;

	List<GoodsPriceInfoResultVo> itemPriceList(String ilItemCode) throws Exception;

	int addGoodsDiscount(GoodsDiscountRequestDto goodsDiscountRequestDto) throws Exception;

	int modifyGoodsDiscount(GoodsDiscountRequestDto goodsDiscountRequestDto) throws Exception;
}
