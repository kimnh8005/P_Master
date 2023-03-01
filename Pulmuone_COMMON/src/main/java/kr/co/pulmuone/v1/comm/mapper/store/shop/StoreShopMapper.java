package kr.co.pulmuone.v1.comm.mapper.store.shop;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.store.shop.dto.ShopListRequestDto;
import kr.co.pulmuone.v1.store.shop.dto.vo.PickUpShopListVo;
import kr.co.pulmuone.v1.store.shop.dto.vo.ShopListVo;
import kr.co.pulmuone.v1.store.shop.dto.vo.ShopVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StoreShopMapper {

    Page<ShopListVo> getShopList(ShopListRequestDto dto);

    ShopVo getShop(@Param("urStoreId") String urStoreId, @Param("deviceType") String deviceType);

    List<PickUpShopListVo> getPickUpShopList();

}
