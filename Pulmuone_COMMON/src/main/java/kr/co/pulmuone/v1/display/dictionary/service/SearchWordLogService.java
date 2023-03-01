package kr.co.pulmuone.v1.display.dictionary.service;

import kr.co.pulmuone.v1.comm.mapper.display.SearchWordLogMapper;
import kr.co.pulmuone.v1.display.dictionary.dto.SearchWordLogConditionDto;
import kr.co.pulmuone.v1.display.dictionary.dto.vo.SearchWordLogVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchWordLogService {

    @Autowired
    private SearchWordLogMapper searchWordLogMapper;

    /**
     * 인기검색어 수집 데이터 리스트
     * @param dto
     * @return
     */
    protected List<SearchWordLogVo> getSearchWordLogList(SearchWordLogConditionDto dto) {
        return searchWordLogMapper.getSearchWordLogList(dto);
    }

    /**
     * 인기검색어 수집 데이터 리스트 카운트
     * @param dto
     * @return
     */
    protected int getSearchWordLogListCount(SearchWordLogConditionDto dto) {
        return searchWordLogMapper.getSearchWordLogListCount(dto);
    }

    /**
     * 검색어 추가
     * @param keyword
     * @param count
     * @return
     */
    protected int addSearchWordLog(String keyword, Long count) {
        return searchWordLogMapper.addSearchWordLog(keyword, count);
    }


    /**
     * 인기검색어 daily 통계
     * @param targetDate
     */
    protected int makeSearchWordDailyStatistics(String targetDate) {
        searchWordLogMapper.deleteSearchWordLogSummary(targetDate);
        return searchWordLogMapper.insertSearchWordLogSummary(targetDate);
    }

    /**
     * 검색어 raw 데이터 삭제; (7일 지난 데이터)
     */
    protected int deleteOldSearchWordLog() {
        return searchWordLogMapper.deleteOldSearchWordLog();
    }
}
