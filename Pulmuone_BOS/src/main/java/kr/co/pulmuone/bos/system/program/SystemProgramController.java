package kr.co.pulmuone.bos.system.program;

import javax.servlet.http.HttpServletRequest;

import io.swagger.annotations.*;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.system.program.dto.ProgramRequestDto;
import kr.co.pulmuone.v1.system.program.dto.GetProgramListRequestDto;
import kr.co.pulmuone.v1.system.program.dto.GetProgramListResponseDto;
import kr.co.pulmuone.v1.system.program.dto.GetProgramNameListRequestDto;
import kr.co.pulmuone.v1.system.program.dto.GetProgramNameListResponseDto;
import kr.co.pulmuone.v1.system.program.dto.GetProgramResponseDto;
import kr.co.pulmuone.v1.system.program.service.SystemProgramBiz;

@RequiredArgsConstructor
@Controller
public class SystemProgramController {

	private final SystemProgramBiz systemProgramBiz;

	@Autowired(required=true)
	private HttpServletRequest request;


	/**
	 * 프로그램 리스트 조회
	 * @param dto
	 * @return GetProgramListResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/st/pgm/getProgramList")
	@ResponseBody
	@ApiOperation(value = "프로그램 리스트 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetProgramListResponseDto.class),
			@ApiResponse(code = 901, message = "" +
					"[MANDATORY_MISSING] 333333333 - 필수값을 입력해주세요. \n"
			)
	})
	public ApiResult<?> getProgramList(GetProgramListRequestDto dto) throws Exception {
		return ApiResult.success(systemProgramBiz.getProgramList((GetProgramListRequestDto) BindUtil.convertRequestToObject(request, GetProgramListRequestDto.class)));
	}

	/**
	 * 프로그램 팝업 조회
	 * @param dto
	 * @return GetProgramNameListResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/st/pgm/getProgramNameList")
	@ResponseBody
	@ApiOperation(value = "프로그램 팝업 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetProgramNameListResponseDto.class),
			@ApiResponse(code = 901, message = "" +
					"[MANDATORY_MISSING] 333333333 - 필수값을 입력해주세요. \n"
			)
	})
	public ApiResult<?> getProgramNameList(GetProgramNameListRequestDto dto) throws Exception {
		return ApiResult.success(systemProgramBiz.getProgramNameList((GetProgramNameListRequestDto) BindUtil.convertRequestToObject(request, GetProgramNameListRequestDto.class)));
	}

	/**
	 * 프로그램 상세조회
	 * @param dto
	 * @return GetProgramResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/st/pgm/getProgram")
	@ResponseBody
	@ApiOperation(value = "프로그램 상세 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetProgramResponseDto.class),
			@ApiResponse(code = 901, message = "" +
					"[MANDATORY_MISSING] 333333333 - 필수값을 입력해주세요. \n"
			)
	})
	public ApiResult<?> getProgram(ProgramRequestDto dto) throws Exception {
		return ApiResult.success(systemProgramBiz.getProgram(dto));
	}

	/**
	 * 프로그램 등록
	 * @param dto
	 * @return BaseResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/st/pgm/addProgram")
	@ResponseBody
	@ApiOperation(value = "프로그램 등록")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data"),
			@ApiResponse(code = 901, message = "" +
					"[MANDATORY_MISSING] 333333333 - 필수값을 입력해주세요. \n"
			)
	})
	public ApiResult<?> addProgram(ProgramRequestDto dto) throws Exception {
		return systemProgramBiz.addProgram(dto);
	}

	/**
	 * 프로그램 수정
	 * @param dto
	 * @return BaseResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/st/pgm/putProgram")
	@ResponseBody
	@ApiOperation(value = "프로그램 수정")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data"),
			@ApiResponse(code = 901, message = "" +
					"[MANDATORY_MISSING] 333333333 - 필수값을 입력해주세요. \n"
			)
	})
	public ApiResult<?> putProgram(ProgramRequestDto dto) throws Exception {
		return systemProgramBiz.putProgram(dto);
	}

	/**
	 * 프로그램 삭제
	 * @param stProgramId
	 * @return BaseResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/st/pgm/delProgram")
	@ResponseBody
	@ApiOperation(value = "프로그램 삭제")
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "stProgramId", value = "프로그램 PK", required = true, dataType = "Long")
	})
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data"),
			@ApiResponse(code = 901, message = "" +
					"[MANDATORY_MISSING] 333333333 - 필수값을 입력해주세요. \n"
			)
	})
	public ApiResult<?> delProgram(@RequestParam(value="stProgramId", required = true) Long stProgramId) throws Exception {
		return systemProgramBiz.delProgram(stProgramId);
	}

	/**
	 * 프로그램 권한 사용 리스트
	 * @param stProgramId
	 * @return BaseResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/st/pgm/getProgramAuthUseList")
	@ResponseBody
	@ApiOperation(value = "프로그램 권한 사용 리스트")
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "stProgramId", value = "프로그램 PK", required = true, dataType = "Long")
	})
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data"),
			@ApiResponse(code = 901, message = "" +
					"[MANDATORY_MISSING] 333333333 - 필수값을 입력해주세요. \n"
			)
	})
	public ApiResult<?> getProgramAuthUseList(@RequestParam(value="stProgramId", required = true) Long stProgramId) throws Exception {
		return ApiResult.success(systemProgramBiz.getProgramAuthUseList(stProgramId));
	}
}
