package kr.co.pulmuone.v1.goods.goods.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsShopListRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsShopListVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GoodsShopListServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    GoodsShopListService godsShopListService;

    @BeforeEach
    void setUp() {
        preLogin();
    }

    @Test
    void 매장상품조회_테스트() {
        //given
        GoodsShopListRequestDto paramDto = new GoodsShopListRequestDto();
        paramDto.setSearchType("single");

        //when
        Page<GoodsShopListVo> result = godsShopListService.getGoodsShopList(paramDto);

        // then
        assertNotNull(result.getResult());
    }
}
