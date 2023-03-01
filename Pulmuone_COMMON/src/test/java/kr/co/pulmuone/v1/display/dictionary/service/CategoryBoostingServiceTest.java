package kr.co.pulmuone.v1.display.dictionary.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.SearchEnums;
import kr.co.pulmuone.v1.display.dictionary.dto.CategoryBoostingGridDto;
import kr.co.pulmuone.v1.display.dictionary.dto.CategoryBoostingSearchRequestDto;
import kr.co.pulmuone.v1.display.dictionary.dto.vo.CategoryBoostingVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class CategoryBoostingServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    CategoryBoostingService categoryBoostingService;

    @BeforeEach
    void beforeEach() {
        preLogin();
    }


    @Test
    public void test_카테고리부스팅_데이터_조회() {
        CategoryBoostingSearchRequestDto dto = new CategoryBoostingSearchRequestDto();
        dto.setUseYn("Y");
        dto.setSearchWord("심콩");
        List<CategoryBoostingGridDto> list = categoryBoostingService.getCategoryBoostingList(dto);
        list.stream().forEach(l->log.info(l.toString()));

        assertTrue(list.size() >= 0);
    }

    @Test
    public void test_카테고리부스팅_데이터_조회_pageLimit() {
        //given
        CategoryBoostingSearchRequestDto dto = new CategoryBoostingSearchRequestDto();
        dto.setUseYn("Y");
        dto.setSearchWord("나또");
        dto.setPage(2);
        dto.setPageSize(2);

        //when
        List<CategoryBoostingGridDto> list = categoryBoostingService.getCategoryBoostingList(dto);

        //then
        assertTrue(list.size() > 0);
    }

    @Test
    public void test_카테고리부스팅_데이터_카운트_조회() {
        CategoryBoostingSearchRequestDto dto = new CategoryBoostingSearchRequestDto();
        dto.setUseYn("Y");
        int count = categoryBoostingService.getCategoryBoostingListCount(dto);

        assertTrue(count >= 0);
    }

    @Test
    public void test_부스팅_적용할_카테고리목록_조회(){
        assertNotNull(categoryBoostingService.getBoostingCategoryList());
    }


    @Nested
    public class NestedTest1 {

        String keyword = "테스트키워드1";
        String lev1CategoryId = "60";
        int score = 1;

        @BeforeEach
        public void init() {

            List<CategoryBoostingVo> insertRequestList = new ArrayList<>();
            CategoryBoostingVo data = new CategoryBoostingVo();
            data.setIlCtgryId(lev1CategoryId);
            data.setSearchWord(keyword);
            data.setBoostingScore(score);
            data.setUseYn("Y");
            insertRequestList.add(data);
            categoryBoostingService.addCategoryBoosting(insertRequestList);
        }

        @Test
        public void test_카테고리부스팅_데이터_중복체크() {
            List<CategoryBoostingVo> list = new ArrayList<>();
            CategoryBoostingVo data = new CategoryBoostingVo();
            data.setSearchWord(keyword);
            data.setIlCtgryId(lev1CategoryId);
            data.setBoostingScore(score);
            data.setUseYn("Y");
            list.add(data);

            ApiResult result = categoryBoostingService.addCategoryBoosting(list);
            assertTrue(SearchEnums.DictionaryMessage.DUPLICATE_DATA.getCode() == result.getCode());
        }


        @Test
        public void test_사용자정의사전_데이터_삭제() {
            CategoryBoostingSearchRequestDto searchDto = new CategoryBoostingSearchRequestDto();
            searchDto.setIlCtgryId(lev1CategoryId);
            searchDto.setSearchWord(keyword);
            List<CategoryBoostingGridDto> result = categoryBoostingService.getCategoryBoostingList(searchDto);
            String boostingId = result.get(0).getDpCtgryBoostingId();

            List<CategoryBoostingVo> deleteRequestList = new ArrayList<>();
            CategoryBoostingVo data = new CategoryBoostingVo();
            data.setDpCtgryBoostingId(boostingId);
            deleteRequestList.add(data);

            categoryBoostingService.deleteCategoryBoosting(deleteRequestList);

            result = categoryBoostingService.getCategoryBoostingList(searchDto);

            assertTrue(result.isEmpty());
        }
    }

    @Nested
    public class NestedTest2 {

        String keyword = "테스트키워드2";
        String lev1CategoryId = "60";
        int score = 1;


        @Test
        public void test_사용자정의사전_데이터_추가() {
            List<CategoryBoostingVo> insertRequestList = new ArrayList<>();
            CategoryBoostingVo data = new CategoryBoostingVo();
            data.setSearchWord(keyword);
            data.setIlCtgryId(lev1CategoryId);
            data.setBoostingScore(score);
            data.setUseYn("Y");
            insertRequestList.add(data);

            ApiResult insertResult = categoryBoostingService.addCategoryBoosting(insertRequestList);
            assertTrue(BaseEnums.Default.SUCCESS.getCode() == insertResult.getCode());

            CategoryBoostingSearchRequestDto searchDto = new CategoryBoostingSearchRequestDto();
            searchDto.setIlCtgryId(lev1CategoryId);
            searchDto.setSearchWord(keyword);
            List<CategoryBoostingGridDto> result = categoryBoostingService.getCategoryBoostingList(searchDto);
            result.stream().forEach(r -> log.info(r.toString()));

            assertNotNull(result);
        }

        @AfterEach
        public void delete() {
            CategoryBoostingSearchRequestDto searchDto = new CategoryBoostingSearchRequestDto();
            searchDto.setIlCtgryId(lev1CategoryId);
            searchDto.setSearchWord(keyword);
            List<CategoryBoostingGridDto> result = categoryBoostingService.getCategoryBoostingList(searchDto);
            String boostingId = result.get(0).getDpCtgryBoostingId();

            List<CategoryBoostingVo> deleteRequestList = new ArrayList<>();
            CategoryBoostingVo data = new CategoryBoostingVo();
            data.setDpCtgryBoostingId(boostingId);
            deleteRequestList.add(data);

            categoryBoostingService.deleteCategoryBoosting(deleteRequestList);
        }

    }

}