package kr.co.pulmuone.v1.display.dictionary.service;

import kr.co.pulmuone.v1.comm.base.dto.PagingListDataDto;
import kr.co.pulmuone.v1.display.dictionary.dto.SearchWordLogConditionDto;
import kr.co.pulmuone.v1.display.dictionary.dto.vo.SearchWordLogVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SearchWordLogBizImpl implements SearchWordLogBiz {

    @Autowired
    SearchWordLogService searchWordLogService;


    @Override
    public PagingListDataDto<SearchWordLogVo> searchWordLogPagingList(SearchWordLogConditionDto dto) {
        List<SearchWordLogVo> list = searchWordLogService.getSearchWordLogList(dto);
        int totalCount = searchWordLogService.getSearchWordLogListCount(dto);

        return new PagingListDataDto<>(dto.getPage()
                , dto.getPageSize()
                , totalCount
                , list);
    }

    @Override
    public int addSearchWordLog(String keyword, Long count) {
        return searchWordLogService.addSearchWordLog(keyword, count);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public int makeSearchWordDailyStatistics(String targetDate) {
        return searchWordLogService.makeSearchWordDailyStatistics(targetDate);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public int deleteOldSearchWordLog() {
        return searchWordLogService.deleteOldSearchWordLog();
    }
}
