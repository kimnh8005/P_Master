package kr.co.pulmuone.bos.user.psuserbasic;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.user.movereason.dto.MoveReasonResponseDto;
import kr.co.pulmuone.v1.user.movereason.service.UserMoveReasonBiz;
import kr.co.pulmuone.v1.user.psuserbasic.dto.PsUserBasicRequestDto;
import kr.co.pulmuone.v1.user.psuserbasic.service.UserPsUserBasicBiz;
import lombok.RequiredArgsConstructor;


/**
 * <PRE>
 * Forbiz Korea
 * 기초설정 관리
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20200820    강윤경              최초작성
 * =======================================================================
 * </PRE>
 */
@RestController
@RequiredArgsConstructor
public class UserPsUserBasicController {

	@Autowired(required=true)
	private HttpServletRequest request;

	private final UserPsUserBasicBiz psUserBasicBiz;


	@ApiOperation(value = "기초설정  조회")
	@PostMapping(value = "/admin/ur/psUserBasic/getPsUserBasic")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = MoveReasonResponseDto.class)
	})
	public ApiResult<?> getPsUserBasic() throws Exception{
		return psUserBasicBiz.getPsUserBasic();
	}


	@ApiOperation(value = "기초설정  수정")
	@PostMapping(value = "/admin/ur/psUserBasic/putPsUserBasic")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = MoveReasonResponseDto.class)
	})
	public ApiResult<?> putPsUserBasic(PsUserBasicRequestDto psUserBasicRequestDto) throws Exception {
		return psUserBasicBiz.putPsUserBasic(psUserBasicRequestDto);
	}

}

