package kr.co.pulmuone.v1.comm.mapper.system.basic;

import java.util.List;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

import kr.co.pulmuone.v1.system.basic.dto.GetApiCacheRequestDto;
import kr.co.pulmuone.v1.system.basic.dto.SaveApiCacheRequestSaveDto;
import kr.co.pulmuone.v1.system.basic.dto.GetApiCacheListResultVo;

@Mapper
public interface SystemBasicApiCacheMapper {
	int getApiCacheListCount(GetApiCacheRequestDto dto);
	Page<GetApiCacheListResultVo> getApiCacheList(GetApiCacheRequestDto dto);
	int addApiCache(List<SaveApiCacheRequestSaveDto> voList);
	int putApiCache(List<SaveApiCacheRequestSaveDto> voList);
	int delApiCache(List<SaveApiCacheRequestSaveDto> voList);
	int checkApiUrlDuplicate(List<String> voList);
	
}