package kr.co.pulmuone.v1.comm.mapper.shopping.favorites;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.shopping.favorites.dto.CommonGetFavoritesGoodsListByUserRequestDto;
import kr.co.pulmuone.v1.shopping.favorites.dto.ShoppingFavoritesParamDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ShoppingFavoritesMapper {
    Long getGoodsFavorites(ShoppingFavoritesParamDto shoppingFavoritesParamDto) throws Exception;

    Long addGoodsFavorites(ShoppingFavoritesParamDto shoppingFavoritesParamDto) throws Exception;

    int delGoodsFavorites(ShoppingFavoritesParamDto shoppingFavoritesParamDto) throws Exception;

    Page<Long> getFavoritesGoodsListByUser(CommonGetFavoritesGoodsListByUserRequestDto dto) throws Exception;

    void delFavoritesGoodsByGoodsId(@Param("ilGoodsId")Long ilGoodsId, @Param("urUserId")Long urUserId) throws Exception;

}
