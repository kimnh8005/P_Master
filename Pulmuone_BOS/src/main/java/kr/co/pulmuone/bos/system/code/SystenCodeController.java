package kr.co.pulmuone.bos.system.code;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.system.code.dto.*;
import kr.co.pulmuone.v1.system.code.service.SystemCodeBiz;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class SystenCodeController {

	private final SystemCodeBiz systemCodeBiz;

	@Autowired(required=true)
	private HttpServletRequest request;

	/**
	 * 공통코드 마스터 리스트 조회
	 */
	@PostMapping(value = "/admin/st/code/getCodeMasterList")
	@ResponseBody
	@ApiOperation(value = "공통코드 마스터 리스트 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetCodeMasterListResponseDto.class),
			@ApiResponse(code = 901, message = "" +
					"[MANDATORY_MISSING] 333333333 - 필수값을 입력해주세요. \n"
			)
	})
	public ApiResult<?> getCodeMasterList(GetCodeMasterListRequestDto dto) throws Exception {
		return ApiResult.success(systemCodeBiz.getCodeMasterList((GetCodeMasterListRequestDto) BindUtil.convertRequestToObject(request, GetCodeMasterListRequestDto.class)));
	}

	/**
	 * 공통코드 마스터 등록
	 */
	@PostMapping(value = "/admin/st/code/addCodeMaster")
	@ResponseBody
	@ApiOperation(value = "공통코드 마스터 등록")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data"),
			@ApiResponse(code = 901, message = "" +
					"[MANDATORY_MISSING] 333333333 - 필수값을 입력해주세요. \n"
			)
	})
	public ApiResult<?> addCodeMaster(CodeMasterRequestDto dto) throws Exception {
		return systemCodeBiz.addCodeMaster(dto);
	}

	/**
	 * 공통코드 마스터 수정
	 */
	@PostMapping(value = "/admin/st/code/putCodeMaster")
	@ResponseBody
	@ApiOperation(value = "공통코드 마스터 수정")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data"),
			@ApiResponse(code = 901, message = "" +
					"[MANDATORY_MISSING] 333333333 - 필수값을 입력해주세요. \n"
			)
	})
	public ApiResult<?> putCodeMaster(CodeMasterRequestDto dto) throws Exception {
		return systemCodeBiz.putCodeMaster(dto);
	}

	/**
	 * 공통코드 마스터 삭제
	 */
	@PostMapping(value = "/admin/st/code/delCodeMaster")
	@ResponseBody
	@ApiOperation(value = "공통코드 마스터 삭제")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data"),
			@ApiResponse(code = 901, message = "" +
					"[MANDATORY_MISSING] 333333333 - 필수값을 입력해주세요. \n"
			)
	})
	public ApiResult<?> delCodeMaster(@RequestParam(value="stComnCodeMstId")String stComnCodeMstId) throws Exception {
		return systemCodeBiz.delCodeMaster(stComnCodeMstId);
	}

	/**
	 * 공통코드 마스터명 조회
	 */
	@PostMapping(value = "/admin/st/code/getCodeMasterNameList")
	@ResponseBody
	@ApiOperation(value = "공통코드 마스터명 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetCodeMasterNameListResponseDto.class),
			@ApiResponse(code = 901, message = "" +
					"[MANDATORY_MISSING] 333333333 - 필수값을 입력해주세요. \n"
			)
	})
	public ApiResult<?> getCodeMasterNameList(GetCodeMasterNameListRequestDto dto) throws Exception {
		return ApiResult.success(systemCodeBiz.getCodeMasterNameList((GetCodeMasterNameListRequestDto) BindUtil.convertRequestToObject(request, GetCodeMasterNameListRequestDto.class)));
	}

	/**
	 * 공통코드 리스트 조회
	 */
	@PostMapping(value = "/admin/st/code/getCodeList")
	@ResponseBody
	@ApiOperation(value = "공통코드 리스트 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetCodeListResponseDto.class),
			@ApiResponse(code = 901, message = "" +
					"[MANDATORY_MISSING] 333333333 - 필수값을 입력해주세요. \n"
			)
	})
	public ApiResult<?> getCodeList(GetCodeListRequestDto dto) throws Exception {
		return ApiResult.success(systemCodeBiz.getCodeList(dto));
	}

	/**
	 * 공통코드 저장
	 */
	@PostMapping(value = "/admin/st/code/saveCode")
	@ResponseBody
	@ApiOperation(value = "공통코드 저장")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data"),
			@ApiResponse(code = 901, message = "" +
					"[MANDATORY_MISSING] 333333333 - 필수값을 입력해주세요. \n"
			)
	})
	public ApiResult<?> saveCode(SaveCodeRequestDto dto) throws Exception {
		dto.setInsertRequestDtoList((List<SaveCodeRequestSaveDto>) BindUtil.convertJsonArrayToDtoList(dto.getInsertData(), SaveCodeRequestSaveDto.class));
		dto.setUpdateRequestDtoList((List<SaveCodeRequestSaveDto>) BindUtil.convertJsonArrayToDtoList(dto.getUpdateData(), SaveCodeRequestSaveDto.class));
		dto.setDeleteRequestDtoList((List<SaveCodeRequestSaveDto>) BindUtil.convertJsonArrayToDtoList(dto.getDeleteData(), SaveCodeRequestSaveDto.class));

		return ApiResult.success(systemCodeBiz.saveCode(dto));
	}
}


