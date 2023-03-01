package kr.co.pulmuone.v1.comm.mapper.display;

import kr.co.pulmuone.v1.display.dictionary.dto.SearchWordLogConditionDto;
import kr.co.pulmuone.v1.display.dictionary.dto.vo.SearchWordLogVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SearchWordLogMapper {
    List<SearchWordLogVo> getSearchWordLogList(SearchWordLogConditionDto dto);

    int getSearchWordLogListCount(SearchWordLogConditionDto dto);

    int addSearchWordLog(@Param("keyword") String keyword, @Param("count") Long count);

    int insertSearchWordLogSummary(@Param("targetDate") String targetDate);

    int deleteSearchWordLogSummary(@Param("targetDate") String targetDate);

    int deleteOldSearchWordLog();
}
