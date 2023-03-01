package kr.co.pulmuone.v1.goods.price.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.goods.itemprice.service.GoodsItemPriceService;
import kr.co.pulmuone.v1.goods.price.dto.GoodsPackageEmployeeDiscountResponseDto;
import kr.co.pulmuone.v1.goods.price.dto.GoodsPriceRequestDto;
import kr.co.pulmuone.v1.goods.price.dto.ItemPriceRequestDto;
import kr.co.pulmuone.v1.goods.price.dto.vo.GoodsPriceVo;
import kr.co.pulmuone.v1.goods.price.dto.vo.GoodsVo;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GoodsPriceServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private GoodsPriceService goodsPriceService;

    @Test
    void getTargetGoodsListByItemCd() {
        // given
        List<String> ilItemCdList = null;
        String baseDe = null;
        List<Map<String, String>> targetGoodsListUnPackage = null;
        List<Map<String, String>> targetGoodsListPackage = null;

        // when, then
        assertThrows(Exception.class, () -> {
            goodsPriceService.getTargetGoodsListByItemCd(ilItemCdList
                                                                , baseDe
                                                                , targetGoodsListUnPackage
                                                                , targetGoodsListPackage);
        });
    }

    @Test
    void getTargetGoodsListByGoodsId() {
        // given
        GoodsVo inGoodsVo = null;
        String baseDe = null;
        List<Map<String, String>> targetGoodsListUnPackage = null;
        List<Map<String, String>> targetGoodsListPackage = null;
        // when, then
        assertThrows(Exception.class, () -> {
            goodsPriceService.getTargetGoodsListByGoodsId(inGoodsVo
                                                                , baseDe
                                                                , targetGoodsListUnPackage
                                                                , targetGoodsListPackage);
        });
    }

    @Test
    void genGoodsPriceInfo() {
        // given
        Map<String, String> targetGoodsMap = null;
        List<GoodsPriceVo > listScheduleFinal = null;
        String goodsPriceCallType = null;

        // when, then
        assertThrows(Exception.class, () -> {
            goodsPriceService.genGoodsPriceInfo(targetGoodsMap
                                                    , listScheduleFinal
                                                    , goodsPriceCallType);
        });
    }

    @Test
    void setSalePriceByDiscountInfo() {
        // given
        List<GoodsPriceVo> inListScheduleFinal = null;

        // when, then
        assertThrows(Exception.class, () -> {
            goodsPriceService.setSalePriceByDiscountInfo(inListScheduleFinal);
        });
    }

    @Test
    void getGoodsPricePagingList() {
        // given
        GoodsPriceRequestDto goodsPriceRequestDto = null;
        // when, then
        assertThrows(Exception.class, () -> {
            goodsPriceService.getGoodsPricePagingList(goodsPriceRequestDto);
        });
    }

    @Test
    void getItemPricePagingList() {
        // given
        ItemPriceRequestDto itemPriceRequestDto = null;

        // when, then
        assertThrows(Exception.class, () -> {
            goodsPriceService.getItemPricePagingList(itemPriceRequestDto);
        });
    }

    @Test
    void getGoodsDiscountPagingList() {
        // given
        GoodsPriceRequestDto goodsPriceRequestDto = null;

        // when, then
        assertThrows(Exception.class, () -> {
            goodsPriceService.getGoodsDiscountPagingList(goodsPriceRequestDto);
        });
    }

	@Test
	void 임직원개별할인정보_변경이력() {
		// given
		GoodsPriceRequestDto goodsPriceRequestDto = null;

		// when, then
		assertThrows(Exception.class, () -> {
			goodsPriceService.goodsPackageEmployeeDiscountHistoryGridList(goodsPriceRequestDto);
		});
	}

    @Test
    void 임직원개별할인정보_변경이력_조회() throws Exception {
        // given
        GoodsPriceRequestDto requestDto = new GoodsPriceRequestDto();
        requestDto.setHistoryKind("price");
        requestDto.setPage(1);
        requestDto.setPageSize(2);
        requestDto.setBaseRowCount(1);

        // when
        GoodsPackageEmployeeDiscountResponseDto result = goodsPriceService.goodsPackageEmployeeDiscountHistoryGridList(requestDto);

        // then
        assertNotNull(result.getRows());
        assertTrue(result.getRows().size() > 0);
    }

}