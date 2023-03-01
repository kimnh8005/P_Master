package kr.co.pulmuone.v1.system.basic.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.system.basic.dto.GetApiCacheListResponseDto;
import kr.co.pulmuone.v1.system.basic.dto.GetApiCacheRequestDto;
import kr.co.pulmuone.v1.system.basic.dto.SaveApiCacheRequestDto;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;

public interface SystemBasicApiCacheBiz {
	GetApiCacheListResponseDto getApiCacheList(GetApiCacheRequestDto dto) throws Exception;
	ApiResult<?> saveApiCache(SaveApiCacheRequestDto dto) throws Exception;
}
