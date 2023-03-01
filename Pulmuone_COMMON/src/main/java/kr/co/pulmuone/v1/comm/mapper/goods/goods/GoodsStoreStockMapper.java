package kr.co.pulmuone.v1.comm.mapper.goods.goods;

import kr.co.pulmuone.v1.comm.api.dto.basic.ErpIfOrgaShopOrderSearchResponseDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ItemErpStoreInfoVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ItemStoreInfoIfTempVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ItemStorePriceLogVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GoodsStoreStockMapper {

    int addIlItemStoreInfoIfTemp(ItemStoreInfoIfTempVo itemStoreInfoIfTempVo);

    int delIlItemStoreInfoIfTemp();

    int putIlItemStoreInfo(ItemErpStoreInfoVo itemStoreInfoVo);

    int addIlItemStorePriceLog(ItemStorePriceLogVo itemStorePriceLogVo);

    int putIlItemStorePriceLog(ItemStorePriceLogVo itemStorePriceLogVo);

    List<ErpIfOrgaShopOrderSearchResponseDto> getOrgaShopNonIfOrderCount(@Param("urStoreId")String urStoreId, @Param("ilItemCd")String ilItemCd);

    int putIlItemStoreInfoForStock(@Param("itemStoreInfoList") List<ItemErpStoreInfoVo> itemStoreInfoList);
    
    int getStockPreOrderWithGoodsId(long ilGoodsId);
}
