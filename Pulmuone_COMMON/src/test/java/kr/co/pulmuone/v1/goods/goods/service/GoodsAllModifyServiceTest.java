package kr.co.pulmuone.v1.goods.goods.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsAllModifyRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsAllModifyVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GoodsAllModifyServiceTest extends CommonServiceTestBaseForJunit5{

	@Autowired
	private GoodsAllModifyService goodsAllModifyService;

	@Test
    void 상품일괄조회_테스트() {

		GoodsAllModifyRequestDto paramDto = new GoodsAllModifyRequestDto();

		List<String> list = new ArrayList<>();

		list.add("15485");
		list.add("15484");

		paramDto.setIlGoodsIds(list);
		List<GoodsAllModifyVo> result = goodsAllModifyService.getGoodsAllModifyList(paramDto);

		// then
		assertTrue(result.size() > 0);
    }

	@Test
	void 상품일괄수정_프로모션명수정_테스트() {
		GoodsAllModifyRequestDto paramDto = new GoodsAllModifyRequestDto();
		List<GoodsAllModifyVo> goodsList = new ArrayList<>();
		GoodsAllModifyVo vo = new GoodsAllModifyVo();
		vo.setIlItemCd("0934578");
		vo.setIlGoodsId("15641");
		goodsList.add(vo);
		paramDto.setGoodsGridList(goodsList);
		paramDto.setPromotionNm("test");
		paramDto.setPromotionNameStartDate("2021-02-02 00:00");
		paramDto.setPromotionNameEndDate("2021-02-18 00:00");

		int putResult = goodsAllModifyService.putPromotionInfoModify(paramDto);

		assertTrue(putResult > 0);
	}

}
