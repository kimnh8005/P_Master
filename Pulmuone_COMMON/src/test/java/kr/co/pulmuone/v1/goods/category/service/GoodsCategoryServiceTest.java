package kr.co.pulmuone.v1.goods.category.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.mapper.goods.category.GoodsCategoryMapper;
import kr.co.pulmuone.v1.goods.category.dto.GetCategoryResultDto;
import kr.co.pulmuone.v1.goods.category.dto.OrgaStoreCategoryRequestDto;
import kr.co.pulmuone.v1.goods.category.dto.vo.CategoryDepth1Vo;
import kr.co.pulmuone.v1.goods.category.dto.vo.GetCategoryListResultVo;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class GoodsCategoryServiceTest extends CommonServiceTestBaseForJunit5 {

	@Autowired
	private GoodsCategoryService goodsCategoryService;

	@InjectMocks
	private GoodsCategoryService mockGoodsCategoryService;

	@Mock
	GoodsCategoryMapper mockGoodsCategoryMapper;

	@Test
	void getCategory_성공() throws Exception{
		Long ilCategoryId = 50L;

		GetCategoryResultDto categoryResult = goodsCategoryService.getCategory(ilCategoryId);

		assertNotNull(categoryResult);
	}

	@Test
	void getCategory_조회결과없음() throws Exception{
		Long ilCategoryId = 1L;

		GetCategoryResultDto categoryResult = goodsCategoryService.getCategory(ilCategoryId);

		assertNull(categoryResult);
	}

	@Test
	void getCategoryList_성공() throws Exception{
		String mallDiv = "MALL_DIV.PULMUONE";
		Long categoryIdDepth0 = 2044L;

		List<GetCategoryListResultVo> categoryList = goodsCategoryService.getCategoryList(mallDiv);

		assertTrue(categoryList.size() > 0);
	}

	@Test
	void getCategoryList_조회결과없음() throws Exception{
		String mallDiv = "MALL_DIV";
		Long categoryIdDepth0 = 1L;

		List<GetCategoryListResultVo> categoryList = goodsCategoryService.getCategoryList(mallDiv);

		assertTrue(categoryList.size() == 0);
	}

    @Test
    void putCategoryDispGoodsCnt() throws Exception{

		// [TDD 의거 임시작성] 향후 개발 완료후 개선 요망.
		given(mockGoodsCategoryMapper.putCategoryDispGoodsCnt3(any())).willReturn(1);
		given(mockGoodsCategoryMapper.putCategoryDispGoodsCnt2(any())).willReturn(1);
		given(mockGoodsCategoryMapper.putCategoryDispGoodsCnt1(any())).willReturn(1);

		int n = mockGoodsCategoryService.putCategoryDispGoodsCnt(0L);
		assertTrue(n == 0);

    }

	@Test
	void getOrgaStoreCategoryDepth1_조회_성공() throws Exception {
		//given
		OrgaStoreCategoryRequestDto dto = OrgaStoreCategoryRequestDto.builder()
				.mallDiv(GoodsEnums.MallDiv.ORGA.getCode())
				.dpBrandIdList(Arrays.asList("31","39"))
				.build();

		//when
		List<CategoryDepth1Vo> result = goodsCategoryService.getOrgaStoreCategoryDepth1(dto);

		//then
		assertTrue(result.size() > 0);
	}

}
