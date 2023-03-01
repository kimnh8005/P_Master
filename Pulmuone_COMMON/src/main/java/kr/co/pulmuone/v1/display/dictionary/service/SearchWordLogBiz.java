package kr.co.pulmuone.v1.display.dictionary.service;

import kr.co.pulmuone.v1.comm.base.dto.PagingListDataDto;
import kr.co.pulmuone.v1.display.dictionary.dto.SearchWordLogConditionDto;
import kr.co.pulmuone.v1.display.dictionary.dto.vo.SearchWordLogVo;

public interface SearchWordLogBiz {

    /**
     * 인기검색어 수집 데이터 페이징 리스트
     * @param dto
     * @return
     */
    PagingListDataDto<SearchWordLogVo> searchWordLogPagingList(SearchWordLogConditionDto dto);

    /**
     * 검색어 추가
     * @param keyword
     * @param count
     * @return
     */
    int addSearchWordLog(String keyword, Long count);

    /**
     * 인기검색어 통계 daily
     * @param targetDate
     */
    int makeSearchWordDailyStatistics(String targetDate);

    /**
     *  검색어 raw 데이터 삭제
     * @return
     */
    int deleteOldSearchWordLog();
}
