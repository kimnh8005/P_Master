package kr.co.pulmuone.v1.shopping.favorites.service;


import kr.co.pulmuone.v1.shopping.favorites.dto.CommonGetFavoritesGoodsListByUserRequestDto;
import kr.co.pulmuone.v1.shopping.favorites.dto.CommonGetFavoritesGoodsListByUserResponseDto;
import kr.co.pulmuone.v1.shopping.favorites.dto.ShoppingFavoritesDto;

public interface ShoppingFavoritesBiz {

    ShoppingFavoritesDto addGoodsFavorites(Long ilGoodsId, String urUserId) throws Exception;

    void delGoodsFavorites(Long spFavoritesGoodsId, String urUserId) throws Exception;

    Long getGoodsFavorites(String urUserId, Long ilGoodsId) throws Exception;

    CommonGetFavoritesGoodsListByUserResponseDto getFavoritesGoodsListByUser(CommonGetFavoritesGoodsListByUserRequestDto dto) throws Exception;

    void delFavoritesGoodsByGoodsId(Long ilGoodsId, Long urUserId) throws Exception;
}
