package kr.co.pulmuone.v1.goods.goods.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsRankingRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.search.searcher.dto.GoodsSearchResultDto;

class GoodsEtcServiceTest extends CommonServiceTestBaseForJunit5{

	@Autowired
	private GoodsEtcService goodsEtcService;


	@Test
	void getRecommendedGoodsList_성공() throws Exception{
		String categoryIdDepth2 = "55";

		List<Long> recommendedGoodsList =  goodsEtcService.getRecommendedGoodsList(categoryIdDepth2);

		assertTrue(recommendedGoodsList.size() > 0);
	}

	@Test
	void getRecommendedGoodsList_조회결과없음() throws Exception{
		String categoryIdDepth2 = "1";

		List<Long> recommendedGoodsList =  goodsEtcService.getRecommendedGoodsList(categoryIdDepth2);

		assertTrue(recommendedGoodsList.size() == 0);
	}

	@Test
	void goodsListSortByGoodsId_성공() throws Exception{
		List<Long> goodsIdList = new ArrayList<>();
		goodsIdList.add(175L);
		goodsIdList.add(177L);
		goodsIdList.add(179L);
		List<GoodsSearchResultDto> searchResultDtoList = new ArrayList<>();
		GoodsSearchResultDto goodsSearchDto1 = new GoodsSearchResultDto();
		goodsSearchDto1.setGoodsId(177L);
		goodsSearchDto1.setGoodsName("상품명177");
		GoodsSearchResultDto goodsSearchDto2 = new GoodsSearchResultDto();
		goodsSearchDto2.setGoodsId(179L);
		goodsSearchDto2.setGoodsName("상품명179");
		GoodsSearchResultDto goodsSearchDto3 = new GoodsSearchResultDto();
		goodsSearchDto3.setGoodsId(175L);
		goodsSearchDto3.setGoodsName("상품명175");

		searchResultDtoList.add(goodsSearchDto1);
		searchResultDtoList.add(goodsSearchDto2);
		searchResultDtoList.add(goodsSearchDto3);

		goodsEtcService.goodsListSortByGoodsId(goodsIdList, searchResultDtoList);

		assertEquals("상품명175",searchResultDtoList.get(0).getGoodsName());
	}

	@Test
	void goodsListSortByGoodsId_실패() throws Exception{
		List<Long> goodsIdList = new ArrayList<>();
		goodsIdList.add(175L);
		goodsIdList.add(177L);
		goodsIdList.add(179L);
		List<GoodsSearchResultDto> searchResultDtoList = new ArrayList<>();
		GoodsSearchResultDto goodsSearchDto1 = new GoodsSearchResultDto();
		goodsSearchDto1.setGoodsId(177L);
		goodsSearchDto1.setGoodsName("상품명177");
		GoodsSearchResultDto goodsSearchDto2 = new GoodsSearchResultDto();
		goodsSearchDto2.setGoodsId(179L);
		goodsSearchDto2.setGoodsName("상품명179");
		GoodsSearchResultDto goodsSearchDto3 = new GoodsSearchResultDto();
		goodsSearchDto3.setGoodsId(175L);
		goodsSearchDto3.setGoodsName("상품명175");

		searchResultDtoList.add(goodsSearchDto1);
		searchResultDtoList.add(goodsSearchDto2);
		searchResultDtoList.add(goodsSearchDto3);

		goodsEtcService.goodsListSortByGoodsId(goodsIdList, searchResultDtoList);

		assertNotEquals("상품명177",searchResultDtoList.get(0).getGoodsName());
	}

    @Test
    void getGoodsRankingByCategoryIdDepth1_조회_성공() throws Exception {
		//given
		GoodsRankingRequestDto dto = GoodsRankingRequestDto.builder()
				.mallDiv(GoodsEnums.MallDiv.PULMUONE.getCode())
				.categoryIdDepth1(0L)
				.total(40)
				.bestYn("Y")
				.exceptShopOnly(true)
				.build();

		//when
		List<Long> result = goodsEtcService.getGoodsRankingByCategoryIdDepth1(dto);

		//then
		assertTrue(result.size() > 0);
    }

	@Test
	void getGoodsRankingByDpBrandId_조회_성공() throws Exception {
		//given
		String mallDiv = "MALL_DIV.PULMUONE";
		Long dpBrandId = 1L;
		int total = 40;

		//when
		List<Long> result = goodsEtcService.getGoodsRankingByDpBrandId(mallDiv, dpBrandId, total);

		//then
		assertTrue(result.size() > 0);
	}
}

