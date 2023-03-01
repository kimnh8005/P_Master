package kr.co.pulmuone.v1.goods.store.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.goods.store.dto.OrgaStoreGoodsRequestDto;
import kr.co.pulmuone.v1.goods.store.dto.OrgaStoreGoodsResponseDto;
import kr.co.pulmuone.v1.goods.store.dto.OrgaStorePageInfoResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertTrue;

class GoodsStoreServiceTest extends CommonServiceTestBaseForJunit5 {
    @Autowired
    private GoodsStoreService goodsStoreService;

    @BeforeEach
    void setUp(){
        buyerLogin();
    }

    @Test
    void getOrgaStorePageInfo() throws Exception {
        //given, when
        OrgaStorePageInfoResponseDto result = goodsStoreService.getOrgaStorePageInfo();

        //then
        assertTrue(result.getCategory().size() > 0);
    }

    @Test
    void getOrgaStoreGoods() throws Exception {
        //given
        OrgaStoreGoodsRequestDto dto = new OrgaStoreGoodsRequestDto();
        dto.setUrStoreId("1");
        dto.setIlCtgryId(5080L);

        //when
        OrgaStoreGoodsResponseDto result = goodsStoreService.getOrgaStoreGoods(dto);

        //then
        assertTrue(result.getTotal() > 0);
    }

}