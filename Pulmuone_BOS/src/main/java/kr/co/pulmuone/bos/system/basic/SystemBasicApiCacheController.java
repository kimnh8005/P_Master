package kr.co.pulmuone.bos.system.basic;

import javax.servlet.http.HttpServletRequest;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import kr.co.pulmuone.v1.system.basic.dto.GetApiCacheRequestDto;
import kr.co.pulmuone.v1.system.basic.dto.SaveApiCacheRequestDto;
import kr.co.pulmuone.v1.system.basic.dto.SaveApiCacheRequestSaveDto;
import kr.co.pulmuone.v1.system.basic.service.SystemBasicApiCacheBiz;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <PRE>
* Forbiz Korea
* Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
 * &#64; 2020.07.01		김경민          최초생성
* =======================================================================
 * </PRE>
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class SystemBasicApiCacheController {
	private final SystemBasicApiCacheBiz systemBasicApiCacheBiz;
	
	/**
	 * 시스템-기초설정-API데이타캐쉬관리 데이터를 조회
	 */
	
	@ApiOperation(value = "API 데이터캐시 데이터 조회")
	@PostMapping(value = "/admin/st/apiCache/getApiCacheList")
	public ApiResult<?> getApiCacheList(HttpServletRequest request, GetApiCacheRequestDto getApiCacheRequestDto) throws Exception {
		return ApiResult.success(systemBasicApiCacheBiz.getApiCacheList((GetApiCacheRequestDto) BindUtil.convertRequestToObject(request, GetApiCacheRequestDto.class)));
	}
	
	/**
	 * 시스템-기초설정-API데이타캐쉬관리 저장
	 */
	@ApiOperation(value = "API 데이터캐시 데이터 저장")
	@ApiResponses(value = {
			@ApiResponse(code = 0000, message = "성공"),
			@ApiResponse(code = 9999, message = "" +
					"[DUPLICATE_DATA] 777777777 - 중복된 데이터가 존재합니다. \n"
			)
	})
	@PostMapping(value = "/admin/st/apiCache/saveApiCache")
	public ApiResult<?> saveApiCache(SaveApiCacheRequestDto saveApiCacheRequestDto) throws Exception {
		saveApiCacheRequestDto.setInsertRequestDtoList(BindUtil.convertJsonArrayToDtoList(saveApiCacheRequestDto.getInsertData(), SaveApiCacheRequestSaveDto.class));
		saveApiCacheRequestDto.setUpdateRequestDtoList(BindUtil.convertJsonArrayToDtoList(saveApiCacheRequestDto.getUpdateData(), SaveApiCacheRequestSaveDto.class));
		saveApiCacheRequestDto.setDeleteRequestDtoList(BindUtil.convertJsonArrayToDtoList(saveApiCacheRequestDto.getDeleteData(), SaveApiCacheRequestSaveDto.class));
		
		return systemBasicApiCacheBiz.saveApiCache(saveApiCacheRequestDto);
	}
	
}
