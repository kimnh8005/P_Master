package kr.co.pulmuone.v1.display.dictionary.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.SearchEnums;
import kr.co.pulmuone.v1.display.dictionary.dto.CustomDictionaryDto;
import kr.co.pulmuone.v1.display.dictionary.dto.CustomDictionarySearchDto;
import kr.co.pulmuone.v1.display.dictionary.dto.vo.CustomDictionaryVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class CustomDictionaryServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    CustomDictionaryService customDictionaryService;

    @BeforeEach
    void beforeEach() {
        preLogin();
    }

    @Test
    public void test_사용자정의사전_조회() {
        CustomDictionarySearchDto dto = CustomDictionarySearchDto.builder().useYn("Y").build();
        List<CustomDictionaryVo> list = customDictionaryService.getCustomDictionary(dto);
        list.stream().forEach(l->log.info(l.toString()));

        assertTrue(list.size() >= 0);
    }


    @Test
    public void test_사용자정의사전_카운트_조회() {
        CustomDictionarySearchDto dto = CustomDictionarySearchDto.builder().useYn("Y").build();
        int count = customDictionaryService.getCustomDictionaryCount(dto);

        assertTrue(count >= 0);
    }


    @Nested
    public class NestedTest1 {

        String keyword = "신조어1";

        @BeforeEach
        public void init() {
            List<CustomDictionaryDto> insertRequestList = new ArrayList<>();
            CustomDictionaryDto data = new CustomDictionaryDto();
            data.setCustomizeWord(keyword);
            data.setUseYn("Y");
            insertRequestList.add(data);
            customDictionaryService.addCustomDictionary(insertRequestList);
        }

        @Test
        public void test_사용자정의사전_데이터_중복체크() {
            List<CustomDictionaryDto> list = new ArrayList<>();
            CustomDictionaryDto data = new CustomDictionaryDto();
            data.setCustomizeWord(keyword);
            data.setUseYn("Y");
            list.add(data);

            ApiResult result = customDictionaryService.addCustomDictionary(list);
            assertTrue(SearchEnums.DictionaryMessage.DUPLICATE_DATA.getCode() == result.getCode());
        }


        @Test
        public void test_사용자정의사전_데이터_삭제() {
            CustomDictionarySearchDto searchDto = CustomDictionarySearchDto.builder().customizeWord(keyword).build();
            List<CustomDictionaryVo> result = customDictionaryService.getCustomDictionary(searchDto);
            String customizeDicId = result.get(0).getDpCustomizeDicId();

            List<CustomDictionaryDto> deleteRequestList = new ArrayList<>();
            CustomDictionaryDto data = new CustomDictionaryDto();
            data.setDpCustomizeDicId(customizeDicId);
            deleteRequestList.add(data);

            customDictionaryService.deleteCustomDictionary(deleteRequestList);

            result = customDictionaryService.getCustomDictionary(searchDto);

            assertTrue(result.isEmpty());
        }
    }

    @Nested
    public class NestedTest2 {

        String keyword = "신조어2";

        @Test
        public void test_사용자정의사전_데이터_추가() {
            List<CustomDictionaryDto> insertRequestList = new ArrayList<>();
            CustomDictionaryDto data = new CustomDictionaryDto();
            data.setCustomizeWord(keyword);
            data.setUseYn("Y");
            insertRequestList.add(data);

            ApiResult insertResult = customDictionaryService.addCustomDictionary(insertRequestList);
            assertTrue(BaseEnums.Default.SUCCESS.getCode() == insertResult.getCode());

            CustomDictionarySearchDto searchDto = CustomDictionarySearchDto.builder().customizeWord(keyword).build();
            List<CustomDictionaryVo> searchRresult = customDictionaryService.getCustomDictionary(searchDto);
            searchRresult.stream().forEach(r -> log.info(r.toString()));

            assertNotNull(searchRresult);
        }

        @AfterEach
        public void delete() {
            CustomDictionarySearchDto searchDto = CustomDictionarySearchDto.builder().customizeWord(keyword).build();
            List<CustomDictionaryVo> result = customDictionaryService.getCustomDictionary(searchDto);
            String customizeDicId = result.get(0).getDpCustomizeDicId();

            List<CustomDictionaryDto> deleteRequestList = new ArrayList<>();
            CustomDictionaryDto data = new CustomDictionaryDto();
            data.setDpCustomizeDicId(customizeDicId);
            deleteRequestList.add(data);

            customDictionaryService.deleteCustomDictionary(deleteRequestList);
        }

    }

}