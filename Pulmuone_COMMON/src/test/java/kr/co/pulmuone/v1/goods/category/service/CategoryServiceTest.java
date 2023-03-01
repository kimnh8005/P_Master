package kr.co.pulmuone.v1.goods.category.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.mapper.goods.category.CategoryMapper;
import kr.co.pulmuone.v1.goods.category.dto.CategoryRequestDto;
import kr.co.pulmuone.v1.goods.category.dto.CategoryResponseDto;
import kr.co.pulmuone.v1.goods.category.dto.CategoryStdRequestDto;
import kr.co.pulmuone.v1.goods.category.dto.CategoryStdResponseDto;
import kr.co.pulmuone.v1.goods.category.dto.vo.CategoryExcelVo;
import kr.co.pulmuone.v1.goods.category.dto.vo.CategoryStdVo;
import kr.co.pulmuone.v1.goods.category.dto.vo.CategoryVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class CategoryServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private CategoryService categoryService;

    @InjectMocks
    private CategoryService mockCategoryService;

    @Mock
    CategoryMapper mockCategoryMapper;

    @BeforeEach
    void beforeEach() {
        preLogin();
    }

    @Test
    void getCategoryList() throws Exception {
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto();
        categoryRequestDto.setMallDivision("MALL_DIV.PULMUONE");
        categoryRequestDto.setCategoryId("2044");
        CategoryResponseDto categoryResponseDto = categoryService.getCategoryList(categoryRequestDto);
        assertTrue(categoryResponseDto.getRows().size() > 0);
    }

    @Test
    void getCategory() throws Exception {
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto();
        categoryRequestDto.setCategoryId("50");
        CategoryResponseDto categoryResponseDto = categoryService.getCategory(categoryRequestDto);
        assertTrue(categoryResponseDto.getDetail() != null);
    }

    @Test
    void addCategory() throws Exception {
        // [TDD 의거 임시작성] 향후 개발 완료후 개선 요망.
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto();
        given(mockCategoryMapper.addCategory(any())).willReturn(1);
        given(mockCategoryMapper.addCategoryPrntsInfo(any())).willReturn(1);
        given(mockCategoryMapper.getCategory(any())).willReturn(new CategoryVo());
        mockCategoryService.addCategory(categoryRequestDto);
    }

    @Test
    void putCategory() throws Exception {
        // [TDD 의거 임시작성] 향후 개발 완료후 개선 요망.
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto();
        given(mockCategoryMapper.putCategory(any())).willReturn(1);
        given(mockCategoryMapper.getCategory(any())).willReturn(new CategoryVo());
        mockCategoryService.putCategory(categoryRequestDto);
    }

    @Test
    void putCategorySort() throws Exception {
        // [TDD 의거 임시작성] 향후 개발 완료후 개선 요망.
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto();
        given(mockCategoryMapper.putCategorySort(any())).willReturn(1);
        given(mockCategoryMapper.getCategory(any())).willReturn(new CategoryVo());
        mockCategoryService.putCategorySort(categoryRequestDto);
    }

    @Test
    void delCategory() throws Exception {
        // [TDD 의거 임시작성] 향후 개발 완료후 개선 요망.
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto();
        given(mockCategoryMapper.delCategory(any())).willReturn(1);
        given(mockCategoryMapper.getCategory(any())).willReturn(new CategoryVo());
        mockCategoryService.delCategory(categoryRequestDto);
    }

    @Test
    void getCategoryListForExcel() throws Exception {
        // [TDD 의거 임시작성] 향후 개발 완료후 개선 요망.
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto();
        categoryRequestDto.setMallDivision("MALL_DIV.PULMUONE");
        List<CategoryExcelVo> result = categoryService.getCategoryListForExcel(categoryRequestDto);
        assertTrue(result.size() > 0);
    }

    @Test
    void getCategoryStdList() throws Exception {
        //given
        CategoryStdRequestDto categoryStdRequestDto = new CategoryStdRequestDto();
        categoryStdRequestDto.setStandardCategoryId("0");

        //when
        CategoryStdResponseDto categoryStdResponseDto = categoryService.getCategoryStdList(categoryStdRequestDto);

        //then
        assertTrue(categoryStdResponseDto.getRows().size() > 0);
    }

    @Test
    void getCategoryStd() throws Exception {
        //given
        CategoryStdRequestDto categoryStdRequestDto = new CategoryStdRequestDto();
        categoryStdRequestDto.setStandardCategoryId("0");

        //when
        CategoryStdResponseDto categoryStdResponseDto = categoryService.getCategoryStd(categoryStdRequestDto);

        //then
        assertNotNull(categoryStdResponseDto.getRows());
    }

    @Test
    void addCategoryStd() throws Exception {
        // [TDD 의거 임시작성] 향후 개발 완료후 개선 요망.
        CategoryStdRequestDto categoryStdRequestDto = new CategoryStdRequestDto();
        given(mockCategoryMapper.addCategoryStd(any())).willReturn(1);
        given(mockCategoryMapper.addCategoryStdPrntsInfo(any())).willReturn(1);
        given(mockCategoryMapper.getCategoryStd(any())).willReturn(new CategoryStdVo());
        mockCategoryService.addCategoryStd(categoryStdRequestDto);
    }

    @Test
    void putCategoryStd() throws Exception {
        // [TDD 의거 임시작성] 향후 개발 완료후 개선 요망.
        CategoryStdRequestDto categoryStdRequestDto = new CategoryStdRequestDto();
        given(mockCategoryMapper.putCategoryStd(any())).willReturn(1);
        given(mockCategoryMapper.getCategoryStd(any())).willReturn(new CategoryStdVo());
        mockCategoryService.putCategoryStd(categoryStdRequestDto);
    }

    @Test
    void putCategoryStdSort() throws Exception {
        // [TDD 의거 임시작성] 향후 개발 완료후 개선 요망.
        CategoryStdRequestDto categoryStdRequestDto = new CategoryStdRequestDto();
        given(mockCategoryMapper.putCategoryStdSort(any())).willReturn(1);
        given(mockCategoryMapper.getCategoryStd(any())).willReturn(new CategoryStdVo());
        mockCategoryService.putCategoryStdSort(categoryStdRequestDto);
    }

    @Test
    void delCategoryStd() throws Exception {
        // [TDD 의거 임시작성] 향후 개발 완료후 개선 요망.
        CategoryStdRequestDto categoryStdRequestDto = new CategoryStdRequestDto();
        given(mockCategoryMapper.delCategoryStd(any())).willReturn(1);
        given(mockCategoryMapper.getCategoryStd(any())).willReturn(new CategoryStdVo());
        mockCategoryService.delCategoryStd(categoryStdRequestDto);
    }

    @Test
    void getCategoryStdListForExcel() throws Exception {
        // [TDD 의거 임시작성] 향후 개발 완료후 개선 요망.
        List<CategoryExcelVo> result = categoryService.getCategoryStdListForExcel(null);
        assertTrue(result.size() > 0);
    }
}