package kr.co.pulmuone.bos.user.noti;

import java.util.Arrays;

import kr.co.pulmuone.v1.user.noti.dto.UserNotiDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.bos.user.noti.service.UserNotiBosService;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.user.movereason.dto.MoveReasonResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <PRE>
* Forbiz Korea
* 회원 알림
 * </PRE>
 *
 * <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2021. 03. 16.    홍진영            최초작성
* =======================================================================
 * </PRE>
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class UserNotiController {
	@Autowired
	private UserNotiBosService userNotiBosService;

	@ApiOperation(value = "회원 알림 안읽은 정보 여부")
	@PostMapping(value = "/admin/comn/user/noti/isNotReadNoti")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = MoveReasonResponseDto.class) })
	public ApiResult<?> isNotReadNoti() throws Exception {
		return userNotiBosService.isNotReadNoti();
	}

	@ApiOperation(value = "회원 알림 내역 조회")
	@PostMapping(value = "/admin/comn/user/noti/getNotiListByUser")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = UserNotiDto.class) })
	public ApiResult<?> getNotiListByUser() throws Exception {
		return userNotiBosService.getNotiListByUser();
	}

	@ApiOperation(value = "회원 알림 읽음처리 - 알림")
	@PostMapping(value = "/admin/comn/user/noti/putNotiRead")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = MoveReasonResponseDto.class) })
	public ApiResult<?> putNotiRead(Long[] urNotiIds) throws Exception {
		return userNotiBosService.putNotiRead(Arrays.asList(urNotiIds));
	}

	@ApiOperation(value = "회원 알림 읽음처리 - 내용")
	@PostMapping(value = "/admin/comn/user/noti/putNotiReadClick")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data") })
	public ApiResult<?> putNotiReadClick(Long urNotiId) throws Exception {
		return userNotiBosService.putNotiReadClick(urNotiId);
	}

}