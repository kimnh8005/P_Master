package kr.co.pulmuone.v1.display.dictionary.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.display.dictionary.dto.SearchWordLogConditionDto;
import kr.co.pulmuone.v1.display.dictionary.dto.vo.SearchWordLogVo;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class SearchWordLogServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    SearchWordLogService searchWordLogService;

    @Test
    public void test_인기검색어_조회() {
        SearchWordLogConditionDto dto = new SearchWordLogConditionDto();
        dto.setEndCreateDate("20200701");
        dto.setStartCreateDate("20200701");

        List<SearchWordLogVo> list = searchWordLogService.getSearchWordLogList(dto);
        list.stream().forEach(l->log.info(l.toString()));

        assertTrue(list.size() >= 0);
    }


    @Test
    public void test_인기검색어_카운트_조회() {
        SearchWordLogConditionDto dto = dto = new SearchWordLogConditionDto();
        dto.setEndCreateDate("20200701");
        dto.setStartCreateDate("20200701");
        int count = searchWordLogService.getSearchWordLogListCount(dto);

        assertTrue(count >= 0);
    }

    @Test
    public void test_검색어_추가() {
        String keyword = "얇은피만두";
        Long count = 1L;
        int insertCount = searchWordLogService.addSearchWordLog(keyword, count);
        assertTrue(insertCount >= 0);
    }


}