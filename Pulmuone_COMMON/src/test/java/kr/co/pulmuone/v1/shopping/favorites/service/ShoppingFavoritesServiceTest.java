package kr.co.pulmuone.v1.shopping.favorites.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.shopping.favorites.dto.CommonGetFavoritesGoodsListByUserRequestDto;
import kr.co.pulmuone.v1.shopping.favorites.dto.CommonGetFavoritesGoodsListByUserResponseDto;
import kr.co.pulmuone.v1.shopping.favorites.dto.ShoppingFavoritesParamDto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ShoppingFavoritesServiceTest extends CommonServiceTestBaseForJunit5 {
    @Autowired
    ShoppingFavoritesService shoppingFavoritesService;

    @Test
    void getFavoritesGoodsListByUser_조회_정상() throws Exception{
        //given
        CommonGetFavoritesGoodsListByUserRequestDto dto = new CommonGetFavoritesGoodsListByUserRequestDto();
        dto.setUrUserId(100L);
        dto.setPage(0);
        dto.setLimit(20);

        //when
        CommonGetFavoritesGoodsListByUserResponseDto result = shoppingFavoritesService.getFavoritesGoodsListByUser(dto);

        //then
        Assertions.assertTrue(result.getGoodsIdList().size() > 0);
    }

    @Test
    void getFavoritesGoodsListByUser_조회내역없음() throws Exception{
        //given
        CommonGetFavoritesGoodsListByUserRequestDto dto = new CommonGetFavoritesGoodsListByUserRequestDto();
        dto.setUrUserId(null);
        dto.setPage(0);
        dto.setLimit(20);

        //when
        CommonGetFavoritesGoodsListByUserResponseDto result = shoppingFavoritesService.getFavoritesGoodsListByUser(dto);

        //then
        Assertions.assertTrue(result.getGoodsIdList().size() == 0);
    }

    @Test
    void delFavoritesGoodsByGoodsId() throws Exception {
        //given
        Long ilGoodsId = 1359L;
        Long urUserId = 1646893L;

        //when, then
        shoppingFavoritesService.delFavoritesGoodsByGoodsId(ilGoodsId, urUserId);
    }


    @Test
    void getGoodsFavorites_성공() throws Exception{
    	ShoppingFavoritesParamDto shoppingFavoritesParamDto = new ShoppingFavoritesParamDto();
    	shoppingFavoritesParamDto.setIlGoodsId(175L);
    	shoppingFavoritesParamDto.setUrUserId("100");

    	Long goodsFavorites = shoppingFavoritesService.getGoodsFavorites(shoppingFavoritesParamDto);

    	Assertions.assertNotNull(goodsFavorites);
    }

    @Test
    void getGoodsFavorites_조회결과없음() throws Exception{
    	ShoppingFavoritesParamDto shoppingFavoritesParamDto = new ShoppingFavoritesParamDto();
    	shoppingFavoritesParamDto.setIlGoodsId(175L);
    	shoppingFavoritesParamDto.setUrUserId("1");

    	Long goodsFavorites = shoppingFavoritesService.getGoodsFavorites(shoppingFavoritesParamDto);

    	Assertions.assertNull(goodsFavorites);
    }

    @Test
    void addGoodsFavorites_성공() throws Exception{
    	ShoppingFavoritesParamDto shoppingFavoritesParamDto = new ShoppingFavoritesParamDto();
    	shoppingFavoritesParamDto.setIlGoodsId(177L);
    	shoppingFavoritesParamDto.setUrUserId("100");

    	Long goodsFavoritesGoodsId = shoppingFavoritesService.addGoodsFavorites(shoppingFavoritesParamDto);

    	Assertions.assertNotNull(goodsFavoritesGoodsId);
    }

    @Test
    void addGoodsFavorites_실패() throws Exception{
    	ShoppingFavoritesParamDto shoppingFavoritesParamDto = new ShoppingFavoritesParamDto();
    	shoppingFavoritesParamDto.setIlGoodsId(177L);

    	Assertions.assertThrows(Exception.class,() ->{
    		shoppingFavoritesService.addGoodsFavorites(shoppingFavoritesParamDto);
    	});
    }

    @Test
    void delGoodsFavorites_성공() throws Exception{
    	ShoppingFavoritesParamDto shoppingFavoritesParamDto = new ShoppingFavoritesParamDto();
    	shoppingFavoritesParamDto.setIlGoodsId(177L);
    	shoppingFavoritesParamDto.setUrUserId("100");

    	shoppingFavoritesService.delGoodsFavorites(shoppingFavoritesParamDto);
    }
}