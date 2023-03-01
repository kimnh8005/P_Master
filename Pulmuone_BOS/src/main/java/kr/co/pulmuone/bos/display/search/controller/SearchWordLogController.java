package kr.co.pulmuone.bos.display.search.controller;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.dto.PagingListDataDto;
import kr.co.pulmuone.v1.display.dictionary.dto.SearchWordLogConditionDto;
import kr.co.pulmuone.v1.display.dictionary.dto.vo.SearchWordLogVo;
import kr.co.pulmuone.v1.display.dictionary.service.SearchWordLogBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchWordLogController {

    @Autowired
    SearchWordLogBiz searchWordLogBiz;


    /**
     * 전시관리 > 인기검색어 조회
     * @param searchWordLogConditionDto
     * @return
     */
    @PostMapping(value = "/admin/dp/searchWordLog/getSearchWordLogList")
    public ApiResult<PagingListDataDto<SearchWordLogVo>> searchWordLogVoPagingList(SearchWordLogConditionDto searchWordLogConditionDto) {
        return ApiResult.success(searchWordLogBiz.searchWordLogPagingList(searchWordLogConditionDto));
    }


    /**
     * 전시관리 > 인기검색어 > 검색어 raw 데이터 삭제
     * @return
     */
    @PostMapping(value = "/admin/dp/searchWordLog/deleteOldSearchWordLog")
    public ApiResult<?> deleteOldSearchWordLog() {
        //fixme: 추후 배치로 변경 예정.
        if (searchWordLogBiz.deleteOldSearchWordLog() >= 0)
            return ApiResult.success();

        return ApiResult.fail();
    }

}
