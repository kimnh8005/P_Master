package kr.co.pulmuone.bos.display.popup;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.FileVo;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.display.popup.dto.AddDisplayPopupRequestDto;
import kr.co.pulmuone.v1.display.popup.dto.GetDisplayPopupListRequestDto;
import kr.co.pulmuone.v1.display.popup.dto.PutDisplayPopupRequestDto;
import kr.co.pulmuone.v1.display.popup.dto.vo.GetDisplayPopupListResultVo;
import kr.co.pulmuone.v1.display.popup.service.DisplayPopupBiz;
import lombok.extern.slf4j.Slf4j;

/**
 * <PRE>
 * Forbiz Korea
 * 팝업관리 BOS Controller
 *
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020.09.24.               최성현         최초작성
 * =======================================================================
 * </PRE>
 */

@Slf4j
@RestController
public class DisplayPopupController {

	@Autowired
	private DisplayPopupBiz displayPopupBiz;

	@ApiOperation(value = "BOS 팝업 목록 조회")
	@PostMapping(value = "/admin/display/popup/getPopupList")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetDisplayPopupListResultVo.class) })
	public ApiResult<?> getPopupList(HttpServletRequest request) throws Exception {

		return displayPopupBiz.getPopupList(BindUtil.bindDto(request, GetDisplayPopupListRequestDto.class));

	}

	@ApiOperation(value = "BOS 팝업 목록 등록")
	@PostMapping(value= "/admin/display/popup/addPopup")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = AddDisplayPopupRequestDto.class) })
	public ApiResult<?> addPopupList(AddDisplayPopupRequestDto addDisplayPopupRequestDto) throws Exception {

		addDisplayPopupRequestDto.setCreateId((SessionUtil.getBosUserVO()).getUserId());

		if (!addDisplayPopupRequestDto.getAddFile().isEmpty()) {
			addDisplayPopupRequestDto.setAddFileList(
					BindUtil.convertJsonArrayToDtoList(addDisplayPopupRequestDto.getAddFile(), FileVo.class));
		}

		return displayPopupBiz.addPopup(addDisplayPopupRequestDto);

	}

	@ApiOperation(value = "BOS 팝업 등록 상세조회")
	@PostMapping(value = "/admin/display/popup/putPopupDetail")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data") })
	public ApiResult<?> putPopupList(String displayFrontPopupId) throws Exception {

		return displayPopupBiz.putPopupDetail(displayFrontPopupId);

	}

	@ApiOperation(value = "BOS 팝업 목록 수정")
	@PostMapping(value= "/admin/display/popup/putPopup")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = PutDisplayPopupRequestDto.class) })
	public ApiResult<?> putPopup(PutDisplayPopupRequestDto putDisplayPopupRequestDto) throws Exception {

		putDisplayPopupRequestDto.setCreateId((SessionUtil.getBosUserVO()).getUserId());

		if (!putDisplayPopupRequestDto.getAddFile().isEmpty()) {
			putDisplayPopupRequestDto.setAddFileList(
					BindUtil.convertJsonArrayToDtoList(putDisplayPopupRequestDto.getAddFile(), FileVo.class));
		}

		return displayPopupBiz.putPopup(putDisplayPopupRequestDto);

	}

	@ApiOperation(value = "BOS 팝업 삭제")
	@PostMapping(value = "/admin/display/popup/delPopup")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data") })
	public ApiResult<?> delPopup(String displayFrontPopupId) throws Exception {

		return displayPopupBiz.delPopup(displayFrontPopupId);

	}

	@ApiOperation(value = "BOS 팝업 미리보기")
	@PostMapping(value = "/admin/display/popup/previewPopup")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data") })
	public ApiResult<?> previewPopup(String displayFrontPopupId) throws Exception {

		return displayPopupBiz.previewPopup(displayFrontPopupId);

	}
}
