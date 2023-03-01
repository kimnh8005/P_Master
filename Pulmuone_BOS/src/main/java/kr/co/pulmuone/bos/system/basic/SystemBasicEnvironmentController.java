package kr.co.pulmuone.bos.system.basic;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.system.basic.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.annotations.ApiOperation;
import kr.co.pulmuone.bos.comm.config.EnvConfig;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.system.basic.dto.vo.GetEnvironmentListResultVo;
import kr.co.pulmuone.v1.system.basic.service.SystemBasicEnvironmentBiz;

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
 *  1.0    20200529    오영민              최초작성
 * =======================================================================
 * </PRE>
 */
@RequiredArgsConstructor
@Controller
public class SystemBasicEnvironmentController {
	private final SystemBasicEnvironmentBiz systemBasicEnvironmentBiz;

	/**
	 * 환경설정 리스트 조회
	 */
	@PostMapping(value = "/admin/st/basic/getEnvironmentList")
	@ResponseBody
	@ApiOperation(value = "환경설정 리스트 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 0000, message = "성공", response = GetEnvironmentListResponseDto.class)
	})
	public ApiResult<?> getEnvironmentList(HttpServletRequest request, GetEnvironmentListRequestDto dto) throws Exception{
		return ApiResult.success(systemBasicEnvironmentBiz.getEnvironmentList((GetEnvironmentListRequestDto) BindUtil.convertRequestToObject(request, GetEnvironmentListRequestDto.class)));
	}

	/**
	 * 환경설정 저장
	 */
	@PostMapping(value = "/admin/st/basic/saveEnvironment")
	@ResponseBody
	@ApiOperation(value = "환경설정 리스트 저장")
	@ApiResponses(value = {
			@ApiResponse(code = 0000, message = "성공"),
			@ApiResponse(code = 9999, message = "" +
					"[DUPLICATE_DATA] 777777777 - 중복된 데이터가 존재합니다. \n"
			)
	})
	public ApiResult<?> saveEnvironment(SaveEnvironmentRequestDto dto)throws Exception{

		//binding data
		dto.setInsertRequestDtoList((List<SaveEnvironmentRequestSaveDto>) BindUtil.convertJsonArrayToDtoList(dto.getInsertData(), SaveEnvironmentRequestSaveDto.class));
		dto.setUpdateRequestDtoList((List<SaveEnvironmentRequestSaveDto>) BindUtil.convertJsonArrayToDtoList(dto.getUpdateData(), SaveEnvironmentRequestSaveDto.class));
		dto.setDeleteRequestDtoList((List<SaveEnvironmentRequestSaveDto>) BindUtil.convertJsonArrayToDtoList(dto.getDeleteData(), SaveEnvironmentRequestSaveDto.class));
		
		ApiResult apiResult = systemBasicEnvironmentBiz.saveEnvironment(dto);

		// 캐쉬를 갱신한다.
		EnvConfig.reload();

		return apiResult;
	}


	/**
	 * 환경설정 조회
	 */
	@PostMapping(value = "/admin/st/basic/getEnvironment")
	@ResponseBody
	@ApiOperation(value = "환경설정  조회")
	@ApiResponses(value = {
			@ApiResponse(code = 0000, message = "성공", response = GetEnvironmentListResultVo.class)
	})
	public ApiResult<?> getEnvironment(GetEnvironmentListRequestDto dto) throws Exception{
		return ApiResult.success(systemBasicEnvironmentBiz.getEnvironment(dto));
	}


	/**
	 * 환경설정 정보 저장
	 */
	@PostMapping(value = "/admin/st/basic/putEnvironmentEnvVal")
	@ResponseBody
	@ApiOperation(value = "환경설정 수정")
	@ApiResponses(value = {
			@ApiResponse(code = 0000, message = "성공", response = GetEnvironmentListResponseDto.class),
	})
	public ApiResult<?> putEnvironmentEnvVal(SaveEnvironmentRequestSaveDto dto) throws Exception{
		return ApiResult.success(systemBasicEnvironmentBiz.putEnvironmentEnvVal(dto));

	}



}

