package kr.co.pulmuone.v1.system.basic.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.system.basic.dto.SaveApiCacheRequestSaveDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.pulmuone.v1.system.basic.dto.GetApiCacheListResponseDto;
import kr.co.pulmuone.v1.system.basic.dto.GetApiCacheRequestDto;
import kr.co.pulmuone.v1.system.basic.dto.SaveApiCacheRequestDto;
import kr.co.pulmuone.v1.comm.exception.UserException;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SystemBasicApiCacheBizImpl implements SystemBasicApiCacheBiz {
	private final SystemBasicApiCacheService service;
	
	/**
	 * API데이타캐쉬관리 조회
	 */
	@Override
	public GetApiCacheListResponseDto getApiCacheList(GetApiCacheRequestDto dto) {
		return service.getApiCacheList(dto);
	}
	
	/**
	 * API데이타캐쉬관리 등록
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = UserException.class)
	public ApiResult<?> saveApiCache(SaveApiCacheRequestDto dto) {
		List<SaveApiCacheRequestSaveDto> insertRequestDtoList = dto.getInsertRequestDtoList();
		List<SaveApiCacheRequestSaveDto> updateRequestDtoList = dto.getUpdateRequestDtoList();
		List<SaveApiCacheRequestSaveDto> deleteRequestDtoList = dto.getDeleteRequestDtoList();

		MessageCommEnum returnCode = service.checkApiUrlDuplicate(insertRequestDtoList);

		if (BaseEnums.Default.SUCCESS == returnCode) {
			// 데이터 삭제
			if (!deleteRequestDtoList.isEmpty()) {
				service.delApiCache(deleteRequestDtoList);
			}

			// 데이터 저장
			if (!insertRequestDtoList.isEmpty()) {
				service.addApiCache(insertRequestDtoList);
			}

			// 데이터 수정
			if (!updateRequestDtoList.isEmpty()) {
				service.putApiCache(updateRequestDtoList);
			}
		} else {
			ApiResult.result(returnCode);
		}

		return ApiResult.success();
	}
}
