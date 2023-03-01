package kr.co.pulmuone.bos.system.help;

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
import kr.co.pulmuone.v1.system.help.dto.HelpRequestDto;
import kr.co.pulmuone.v1.system.help.dto.GetHelpListRequestDto;
import kr.co.pulmuone.v1.system.help.dto.GetHelpListResponseDto;
import kr.co.pulmuone.v1.system.help.dto.GetHelpResponseDto;
import kr.co.pulmuone.v1.system.help.service.SystemHelpBiz;

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
 *  1.0    20200602		   	천혜현            최초작성
 *  1.0    20201105		   	ykk       현행화
 * =======================================================================
 * </PRE>
 */
@RequiredArgsConstructor
@Controller
public class SystemHelpController {


	private final SystemHelpBiz systemHelpBiz;

	@Autowired(required=true)
	private HttpServletRequest request;


	/**
	 * 도움말 리스트조회
	 */
	@PostMapping(value = "/admin/st/help/getHelpList")
	@ResponseBody
	@ApiOperation(value = "도움말 리스트조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetHelpListResponseDto.class),
			@ApiResponse(code = 901, message = "" +
					"[MANDATORY_MISSING] 333333333 - 필수값을 입력해주세요. \n"
			)
	})
	public ApiResult<?> getHelpList(GetHelpListRequestDto dto) throws Exception {
		return systemHelpBiz.getHelpList(BindUtil.bindDto(request, GetHelpListRequestDto.class));
	}

	/**
	 * 도움말 상세조회
	 */
	@PostMapping(value = "/admin/st/help/getHelp")
	@ResponseBody
	@ApiOperation(value = "도움말 상세조회")
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "id", value = "도움말 PK", dataType = "Long")
	})
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetHelpResponseDto.class),
			@ApiResponse(code = 901, message = "" +
					"[MANDATORY_MISSING] 333333333 - 필수값을 입력해주세요. \n"
			)
	})
	public ApiResult<?> getHelp(@RequestParam(value="id")Long id) {
		return ApiResult.success(systemHelpBiz.getHelp(id));
	}

	/**
	 * 도움말 추가
	 */
	@PostMapping(value = "/admin/st/help/addHelp")
	@ResponseBody
	@ApiOperation(value = "도움말 추가")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data"),
			@ApiResponse(code = 901, message = "" +
					"[MANDATORY_MISSING] 333333333 - 필수값을 입력해주세요. \n"
			)
	})
	public ApiResult<?> addHelp(HelpRequestDto dto) {
		return systemHelpBiz.addHelp(dto);
	}

	/**
	 * 도움말 수정
	 */
	@PostMapping(value = "/admin/st/help/putHelp")
	@ResponseBody
	@ApiOperation(value = "도움말 수정")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data"),
			@ApiResponse(code = 901, message = "" +
					"[MANDATORY_MISSING] 333333333 - 필수값을 입력해주세요. \n"
			)
	})
	public ApiResult<?> putHelp(HelpRequestDto dto) {
		return systemHelpBiz.putHelp(dto);
	}

	/**
	 * 도움말 삭제
	 */
	@PostMapping(value = "/admin/st/help/delHelp")
	@ResponseBody
	@ApiOperation(value = "도움말 삭제")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data"),
			@ApiResponse(code = 901, message = "" +
					"[MANDATORY_MISSING] 333333333 - 필수값을 입력해주세요. \n"
			)
	})
	public ApiResult<?> delHelp(@RequestParam(value="id")String id) {
		return ApiResult.success(systemHelpBiz.delHelp(id));
	}



	/**
	 * 해당 도움말 목록
	 */
	@PostMapping(value = "/admin/comn/help/getHelpListByArray")
	@ResponseBody
	@ApiOperation(value = "해당 도움말 리스트조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetHelpListResponseDto.class),
			@ApiResponse(code = 901, message = "" +
					"[MANDATORY_MISSING] 333333333 - 필수값을 입력해주세요. \n"
			)
	})
	public ApiResult<?> getHelpListByArray(@RequestParam String systemHelpId) throws Exception {
		return systemHelpBiz.getHelpListByArray(systemHelpId);
	}
}



