package kr.co.pulmuone.bos.base.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.base.dto.GetCodeListRequestDto;
import kr.co.pulmuone.v1.base.dto.GetMenuListRequestDto;
import kr.co.pulmuone.v1.base.dto.GetMenuListResponseDto;
import kr.co.pulmuone.v1.base.dto.GetPageInfoRequestDto;
import kr.co.pulmuone.v1.base.dto.GetPageInfoResponseDto;
import kr.co.pulmuone.v1.base.dto.GetProgramListRequestDto;
import kr.co.pulmuone.v1.base.dto.GetShopInfoRequestDto;
import kr.co.pulmuone.v1.base.dto.GetShopInfoResponseDto;
import kr.co.pulmuone.v1.base.dto.vo.GetCodeListResultVo;
import kr.co.pulmuone.v1.base.dto.vo.GetProgramListResultVo;
import kr.co.pulmuone.v1.base.service.StComnBiz;
import kr.co.pulmuone.v1.comm.base.ApiResult;

@Controller("stCommonControllerHangaram")
public class StCommonController {

	@Resource(name = "stComnBizHangaram")
	private StComnBiz stComnBiz;

	@GetMapping(value = "/admin/comn/getCodeList")
	@ResponseBody
	@ApiOperation(value = "코드 검색")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetCodeListResultVo.class)
	})
	public ApiResult<?> getCodeList(GetCodeListRequestDto dto) {
		return stComnBiz.getCodeList(dto);
	}

	@PostMapping(value = "/admin/comn/getPostCodeList")
	@ResponseBody
	@ApiOperation(value = "코드 검색")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetCodeListResultVo.class)
	})
	public ApiResult<?> getPostCodeList(GetCodeListRequestDto dto) {
		return stComnBiz.getCodeList(dto);
	}

	@GetMapping(value = "/admin/comn/getProgramList")
	@ResponseBody
	@ApiOperation(value = "프로그램 검색")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetProgramListResultVo.class)
	})
	public ApiResult<?> getProgramList(GetProgramListRequestDto dto) {
		return stComnBiz.getProgramList(dto);
	}

	@GetMapping(value = "/admin/comn/getMenuList")
	@ResponseBody
	@ApiOperation(value = "메뉴 검색")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetMenuListResponseDto.class)
	})
	public ApiResult<?> getMenuList(GetMenuListRequestDto dto) {
		return stComnBiz.getMenuList(dto);
	}

	@GetMapping(value = "/admin/comn/getShopInfo")
	@ResponseBody
	@ApiOperation(value = "샵정보 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetShopInfoResponseDto.class)
	})
	public ApiResult<?> getShopInfo(GetShopInfoRequestDto dto) {
		return stComnBiz.getShopInfo(dto);
	}

	@PostMapping(value = "/admin/comn/getPageInfo")
	@ResponseBody
	@ApiOperation(value = "페이지 정보 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetPageInfoResponseDto.class)
	})
	public ApiResult<?> getPageInfo(GetPageInfoRequestDto dto) throws Exception{
		return stComnBiz.getPageInfo(dto);
	}
}
