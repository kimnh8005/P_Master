package kr.co.pulmuone.v1.shopping.favorites.service;

import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.shopping.favorites.dto.CommonGetFavoritesGoodsListByUserRequestDto;
import kr.co.pulmuone.v1.shopping.favorites.dto.CommonGetFavoritesGoodsListByUserResponseDto;
import kr.co.pulmuone.v1.shopping.favorites.dto.ShoppingFavoritesDto;
import kr.co.pulmuone.v1.shopping.favorites.dto.ShoppingFavoritesParamDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShoppingFavoritesBizImpl implements ShoppingFavoritesBiz {

    @Autowired
    private ShoppingFavoritesService shoppingFavoritesService;


    /**
     * 찜 추가
     *
     * @param ilGoodsId, urUserId
     * @return ShoppingFavoritesDto
     * @throws Exception
     */
    @Override
    public ShoppingFavoritesDto addGoodsFavorites(Long ilGoodsId, String urUserId) throws Exception {

        ShoppingFavoritesParamDto shoppingFavoritesParamDto = new ShoppingFavoritesParamDto();
        shoppingFavoritesParamDto.setIlGoodsId(ilGoodsId);
        shoppingFavoritesParamDto.setUrUserId(urUserId);

        Long spFavoritesGoodsId = shoppingFavoritesService.getGoodsFavorites(shoppingFavoritesParamDto);

        if (StringUtil.isEmpty(spFavoritesGoodsId)) {
            spFavoritesGoodsId = shoppingFavoritesService.addGoodsFavorites(shoppingFavoritesParamDto);
        }

        ShoppingFavoritesDto shoppingFavoritesDto = new ShoppingFavoritesDto();
        shoppingFavoritesDto.setSpFavoritesGoodsId(spFavoritesGoodsId);

        return shoppingFavoritesDto;
    }


    /**
     * 찜 삭제
     *
     * @param spFavoritesGoodsId, urUserId
     * @throws Exception
     */
    @Override
    public void delGoodsFavorites(Long spFavoritesGoodsId, String urUserId) throws Exception {

        ShoppingFavoritesParamDto shoppingFavoritesParamDto = new ShoppingFavoritesParamDto();
        shoppingFavoritesParamDto.setSpFavoritesGoodsId(spFavoritesGoodsId);
        shoppingFavoritesParamDto.setUrUserId(urUserId);

        shoppingFavoritesService.delGoodsFavorites(shoppingFavoritesParamDto);
    }


    /**
     * 찜 정보 조회
     *
     * @param urUserId, ilGoodsId
     * @return Long
     * @throws Exception
     */
    @Override
    public Long getGoodsFavorites(String urUserId, Long ilGoodsId) throws Exception {
        ShoppingFavoritesParamDto shoppingFavoritesParamDto = new ShoppingFavoritesParamDto();
        shoppingFavoritesParamDto.setIlGoodsId(ilGoodsId);
        shoppingFavoritesParamDto.setUrUserId(urUserId);

        return shoppingFavoritesService.getGoodsFavorites(shoppingFavoritesParamDto);
    }

    /**
     * 찜 상품 조회
     *
     * @param dto CommonGetFavoritesGoodsListByUserRequestDto
     * @return CommonGetFavoritesGoodsListByUserResponseDto
     * @throws Exception exception
     */
    @Override
    public CommonGetFavoritesGoodsListByUserResponseDto getFavoritesGoodsListByUser(CommonGetFavoritesGoodsListByUserRequestDto dto) throws Exception {
        return shoppingFavoritesService.getFavoritesGoodsListByUser(dto);
    }

    /**
     * 찜 상품 삭제
     *
     * @param ilGoodsId Long
     * @param urUserId  Long
     * @return CommonGetFavoritesGoodsListByUserResponseDto
     * @throws Exception exception
     */
    @Override
    public void delFavoritesGoodsByGoodsId(Long ilGoodsId, Long urUserId) throws Exception {
        shoppingFavoritesService.delFavoritesGoodsByGoodsId(ilGoodsId, urUserId);
    }
}
