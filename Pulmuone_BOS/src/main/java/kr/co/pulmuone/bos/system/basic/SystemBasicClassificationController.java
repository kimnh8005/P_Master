package kr.co.pulmuone.bos.system.basic;

import javax.servlet.http.HttpServletRequest;

import io.swagger.annotations.*;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.system.basic.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.system.basic.service.SystemBasicClassificationBiz;

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
public class SystemBasicClassificationController {
	private final SystemBasicClassificationBiz systemBasicClassificationBiz;

	/**
	 * 분류관리
	 */
	@PostMapping(value = "/admin/st/basic/getClassificationList")
	@ResponseBody
	@ApiOperation(value = "분류관리 리스트 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 0000, message = "성공", response = GetClassificationListResponseDto.class)
	})
	public ApiResult<?> getClassificationList(HttpServletRequest request, GetClassificationListParamDto dto) throws Exception{
		return ApiResult.success(systemBasicClassificationBiz.getClassificationList((GetClassificationListParamDto) BindUtil.convertRequestToObject(request, GetClassificationListParamDto.class)));
	}

	/**
	 * 분류관리 상세조회
	 */
	@PostMapping(value = "/admin/st/basic/getClassification")
	@ResponseBody
	@ApiOperation(value = "분류관리 상세 조회")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "stClassificationId", value = "분류 PK", required = true, dataType = "Long")
    })
	@ApiResponses(value = {
			@ApiResponse(code = 0000, message = "성공", response = GetClassificationResponseDto.class)
	})
	public ApiResult<?> getClassification(@RequestParam(value="stClassificationId", required = true) Long stClassificationId) throws Exception{
		return ApiResult.success(systemBasicClassificationBiz.getClassification(stClassificationId));
	}

	/**
	 * 분류관리 등록
	 */
	@PostMapping(value = "/admin/st/basic/addClassification")
	@ResponseBody
	@ApiOperation(value = "분류관리 등록")
	@ApiResponses(value = {
			@ApiResponse(code = 0000, message = "성공"),
			@ApiResponse(code = 9999, message = "" +
					"[DUPLICATE_DATA] 777777777 - 중복된 데이터가 존재합니다. \n"
			)
	})
	public ApiResult<?> addClassification(SaveClassificationRequestDto dto) throws Exception {
		return systemBasicClassificationBiz.addClassification(dto);
	}

	/**
	 * 분류관리 수정
	 */
	@PostMapping(value = "/admin/st/basic/putClassification")
	@ResponseBody
	@ApiOperation(value = "분류관리 수정")
	@ApiResponses(value = {
			@ApiResponse(code = 0000, message = "성공"),
			@ApiResponse(code = 9999, message = "" +
					"[DUPLICATE_DATA] 777777777 - 중복된 데이터가 존재합니다. \n"
			)
	})
	public ApiResult<?> putClassification(SaveClassificationRequestDto dto) throws Exception {
		return systemBasicClassificationBiz.putClassification(dto);
	}

	/**
	 * 분류관리 삭제
	 */
	@PostMapping(value = "/admin/st/basic/delClassification")
	@ResponseBody
	@ApiOperation(value = "분류관리 삭제")
	@ApiResponses(value = {
			@ApiResponse(code = 0000, message = "성공"),
			@ApiResponse(code = 9999, message = "" +
					"[VALID_ERROR] 888888888 - 데이터가 유효하지 않습니다. \n"
			)
	})
	public ApiResult<?> delClassification(@RequestParam(value="id") Long id) throws Exception {
		return systemBasicClassificationBiz.delClassification(id);
	}

	/**
	 * 분류코드 리스트 조회
	 */
	@GetMapping(value = "/admin/st/basic/getTypeList")
	@ResponseBody
	@ApiOperation(value = "분류코드 리스트 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 0000, message = "성공", response = GetTypeListResponseDto.class)
	})
	public ApiResult<?> getTypeList() throws Exception{
		return ApiResult.success(systemBasicClassificationBiz.getTypeList());
	}


}

