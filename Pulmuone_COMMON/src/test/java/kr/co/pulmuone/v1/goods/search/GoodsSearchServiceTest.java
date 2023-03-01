package kr.co.pulmuone.v1.goods.search;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.constants.GoodsConstants;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsSearchByGoodsIdRequestDto;
import kr.co.pulmuone.v1.goods.search.dto.GoodsSearchNewGoodsRequestDto;
import kr.co.pulmuone.v1.goods.search.dto.GoodsSearchStoreGoodsRequestDto;
import kr.co.pulmuone.v1.goods.search.dto.vo.GoodsSearchAdditionalVo;
import kr.co.pulmuone.v1.goods.search.dto.vo.GoodsSearchExperienceVo;
import kr.co.pulmuone.v1.goods.search.dto.vo.GoodsSearchHmrVo;
import kr.co.pulmuone.v1.goods.search.dto.vo.GoodsSearchOutMallVo;
import kr.co.pulmuone.v1.search.searcher.dto.GoodsSearchResultDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GoodsSearchServiceTest extends CommonServiceTestBaseForJunit5 {
    @Autowired
    private GoodsSearchService goodsSearchService;

    @Test
    void getGoodsSearchExperience_조회_정상() throws Exception {
        //given
        Long ilGoodsId = 175L;

        //when
        GoodsSearchExperienceVo result = goodsSearchService.getGoodsSearchExperience(ilGoodsId);

        //then
        assertEquals(ilGoodsId, result.getGoodsId());
    }

    @Test
    void searchGoodsByGoodsIdList_조회_정상() throws Exception {
        //given
        GoodsSearchByGoodsIdRequestDto reqDto = new GoodsSearchByGoodsIdRequestDto();
        reqDto.setGoodsIdList(Collections.singletonList(15340L));

        //when
        List<GoodsSearchResultDto> result = goodsSearchService.searchGoodsByGoodsIdList(reqDto);

        //then
        assertTrue(result.size() > 0);
    }

    @Test
    void searchGoodsByGoodsIdList_조회_정상_page_limit() throws Exception {
        //given
        GoodsSearchByGoodsIdRequestDto reqDto = new GoodsSearchByGoodsIdRequestDto();
        reqDto.setGoodsIdList(Arrays.asList(15340L, 90018265L, 90018260L, 90018258L, 90018257L, 90018245L));
        reqDto.setPage(2);
        reqDto.setLimit(2);

        //when
        List<GoodsSearchResultDto> result = goodsSearchService.searchGoodsByGoodsIdList(reqDto);

        //then
        assertTrue(result.size() > 0);
    }

    @Test
    void getDailyGoods_조회_정상() throws Exception {
        //given
        String getDailyGoods = "GOODS_DAILY_TP.BABYMEAL";

        //when
        List<Long> result = goodsSearchService.getDailyGoods(getDailyGoods);

        //then
        assertTrue(result.size() > 0);
    }

    @Test
    void getGoodsFromOutMall_조회_정상() throws Exception {
        //given
        List<String> goodsIdList = Arrays.asList("15460", "15463");

        //when
        List<GoodsSearchOutMallVo> result = goodsSearchService.getGoodsFromOutMall(goodsIdList);

        //then
        assertTrue(result.size() > 0);
    }

    @Test
    void getNewGoods_조회_정상() throws Exception {
        //given
        GoodsSearchNewGoodsRequestDto dto = GoodsSearchNewGoodsRequestDto.builder()
                .mallDiv(GoodsEnums.MallDiv.PULMUONE.getCode())
                .monthSub(GoodsConstants.NEW_GOODS_MONTH_INTERVAL_3)
                .build();

        //when
        List<Long> result = goodsSearchService.getNewGoods(dto);

        //then
        assertTrue(result.size() > 0);
    }

    @Test
    void getGoodsAdditional_조회_정상() throws Exception {
        //given
        List<Long> goodsIdList = new ArrayList<>();
        goodsIdList.add(900106L);

        //when
        List<GoodsSearchAdditionalVo> result = goodsSearchService.getGoodsAdditional(goodsIdList);

        //then
        assertTrue(result.size() > 0);
    }

    @Test
    void getGoodsCouponCoverageByUrWareHouseId() throws Exception {
        //given
        List<Long> wareHouseIdList = Collections.singletonList(85L);

        //when
        List<String> result = goodsSearchService.getGoodsCouponCoverageByUrWareHouseId(wareHouseIdList);

        //then
        assertTrue(result.size() > 0);
    }

    @Test
    void getGoodsSearchHmr_조회_성공() throws Exception {
        //given
        GoodsSearchStoreGoodsRequestDto requestDto = GoodsSearchStoreGoodsRequestDto.builder()
                .mallDiv(GoodsEnums.MallDiv.PULMUONE.getCode())
                .dpCtgryIdList(Arrays.asList(50L, 60L))
                .build();

        //when
        List<GoodsSearchHmrVo> result = goodsSearchService.getGoodsSearchHmr(requestDto);

        //then
        assertTrue(result.size() > 0);
    }

    @Test
    void getGoodsSearchShopOnly_조회_성공() throws Exception {
        //given
        GoodsSearchStoreGoodsRequestDto requestDto = GoodsSearchStoreGoodsRequestDto.builder()
                .mallDiv(GoodsEnums.MallDiv.ORGA.getCode())
                .dpCtgryIdList(Arrays.asList(5080L, 60L))
                .build();

        //when
        Page<Long> result = goodsSearchService.getGoodsSearchShopOnly(requestDto);

        //then
        assertTrue(result.getTotal() > 0);
    }
}