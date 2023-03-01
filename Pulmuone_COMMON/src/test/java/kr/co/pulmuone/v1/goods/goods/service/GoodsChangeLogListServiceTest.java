package kr.co.pulmuone.v1.goods.goods.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsChangeLogListRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsChangeLogListResponseDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsChangeLogListVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class GoodsChangeLogListServiceTest extends CommonServiceTestBaseForJunit5{

	@Autowired
	GoodsChangeLogListService goodsChangeLogListService;

	@BeforeEach
	void setUp() {
		preLogin();
	}

	@Test
	void 상품업데이트_내역() {
		//given
		GoodsChangeLogListRequestDto requestDto = new GoodsChangeLogListRequestDto();
		requestDto.setSelectConditionType("singleSection");
		requestDto.setSearchCondition("ALL");
		requestDto.setFindKeyword("900288");
		requestDto.setFindKeywordList(Collections.singletonList("900288")); // 검색어
		requestDto.setPage(1);
		requestDto.setPageSize(20);
		requestDto.setFindKeywordStrFlag("Y");
		requestDto.setDateSearchStart("2021-04-01");
		requestDto.setDateSearchEnd("2021-04-23");
		requestDto.setGoodsName("test");
		requestDto.setChargeType("ALL");

		//when
		Page<GoodsChangeLogListVo> goodsList = goodsChangeLogListService.getGoodsChangeLogList(requestDto);

		//then
		assertTrue(goodsList.getTotal() > 0);
	}

	@Test
	void 상품업데이트_상세내역팝업() throws Exception {
		GoodsChangeLogListRequestDto goodsChangeLogListRequestDto = new GoodsChangeLogListRequestDto();

		goodsChangeLogListRequestDto.setIlGoodsId("90018101");
		goodsChangeLogListRequestDto.setCreateDate("2021-04-23 13:47:57");

		List<GoodsChangeLogListVo> goodsChangeLogPopup = goodsChangeLogListService.getGoodsChangeLogPopup(goodsChangeLogListRequestDto);

		GoodsChangeLogListResponseDto goodsChangeLogListResponseDto = new GoodsChangeLogListResponseDto();

		goodsChangeLogListResponseDto.setRows(goodsChangeLogPopup);

		assertTrue(goodsChangeLogListResponseDto.getRows().size() > 0);
	}
}
