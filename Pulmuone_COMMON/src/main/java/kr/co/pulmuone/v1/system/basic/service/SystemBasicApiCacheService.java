package kr.co.pulmuone.v1.system.basic.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.comm.mapper.system.basic.SystemBasicApiCacheMapper;
import kr.co.pulmuone.v1.system.basic.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SystemBasicApiCacheService {
	private final SystemBasicApiCacheMapper mapper;
	
	/**
	 * API데이타캐쉬관리 조회
	 */
	
	protected GetApiCacheListResponseDto getApiCacheList(GetApiCacheRequestDto dto) {
		GetApiCacheListResponseDto result = new GetApiCacheListResponseDto();

		PageMethod.startPage(dto.getPage(), dto.getPageSize());
		Page<GetApiCacheListResultVo> rows = mapper.getApiCacheList(dto); // rows

		result.setTotal((int)rows.getTotal());
		result.setRows(rows.getResult());
		
		return result;
	}
	
	/**
	 * API데이타캐쉬관리 등록
	 */
	protected int addApiCache(List<SaveApiCacheRequestSaveDto> dto) {
		return mapper.addApiCache(dto);
	}

	protected int putApiCache(List<SaveApiCacheRequestSaveDto> dto) {
		return mapper.putApiCache(dto);
	}

	protected int delApiCache(List<SaveApiCacheRequestSaveDto> dto) {
		return mapper.delApiCache(dto);
	}
	
	/**
	 * 중복 데이터 체크
	 */
	protected MessageCommEnum checkApiUrlDuplicate(List<SaveApiCacheRequestSaveDto> saveApiCacheRequestSaveDto) {
		MessageCommEnum returnCode = BaseEnums.Default.SUCCESS;
		
		List<String> apiUrlKeyList = new ArrayList<String>();
		for (int i = 0; i < saveApiCacheRequestSaveDto.size(); i++) {
			String apiUrlkey = saveApiCacheRequestSaveDto.get(i).getApiUrl();
			if (i != 0 && apiUrlKeyList.contains(apiUrlkey)) {
				returnCode = BaseEnums.CommBase.DUPLICATE_DATA;
				return returnCode;
			}
			apiUrlKeyList.add(apiUrlkey);
		}
		
		int count = 0;
		if (!apiUrlKeyList.isEmpty()) {
			count = mapper.checkApiUrlDuplicate(apiUrlKeyList);
			
			if (count > 0) {
				returnCode = BaseEnums.CommBase.DUPLICATE_DATA;
				return returnCode;
			}
		}
		
		return returnCode;
	}
	
}
