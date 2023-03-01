package kr.co.pulmuone.bos.user.privacy;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.user.dormancy.service.UserDormancyBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.pulmuone.v1.user.dormancy.dto.CommonPutUserDormantRequestDto;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.user.dormancy.dto.GetUserDormantHistoryListRequestDto;
import kr.co.pulmuone.v1.user.dormancy.dto.GetUserDormantHistoryListResponseDto;
import kr.co.pulmuone.v1.user.dormancy.dto.GetUserDormantListRequestDto;
import kr.co.pulmuone.v1.user.dormancy.dto.GetUserDormantListResponseDto;
import kr.co.pulmuone.v1.user.dormancy.dto.PutUserDormantResponseDto;
import kr.co.pulmuone.bos.user.privacy.service.UserDormantService;
import org.springframework.web.bind.annotation.RestController;

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
 *  1.0    20200612		   	천혜현            최초작성
 * =======================================================================
 * </PRE>
 */
@RestController
public class UserDormantController {

	@Autowired
	private UserDormancyBiz userDormancyBiz;

	@Autowired
	private UserDormantService userDormantService;

	@Autowired(required=true)
	private HttpServletRequest request;


	/**
	 * 휴면회원 리스트조회
	 * @param dto
	 * @return GetUserDormantListResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/ur/UserDormant/getUserDormantList")
	@ApiOperation(value = "휴면회원 리스트조회", httpMethod = "POST", notes = "휴면회원 리스트조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = ApiResult.class),
			@ApiResponse(code = 901, message = "" +
					"0000 - 성공 \n" +
					"9999 - 관리자에게 문의하세요."
			)
	})
	public ApiResult<?> getUserDormantList(GetUserDormantListRequestDto dto) throws Exception {

		return ApiResult.success(userDormantService.getUserDormantList((GetUserDormantListRequestDto) BindUtil.convertRequestToObject(request, GetUserDormantListRequestDto.class)));
	}

	/**
	 * 휴면회원 정상전환
	 * @param dto
	 * @return PutUserDormantResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/ur/UserDormant/putUserDormant")
	@ApiOperation(value = "휴면회원 정상전환", httpMethod = "POST", notes = "휴면회원 정상전환")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = ApiResult.class),
			@ApiResponse(code = 901, message = "" +
					"0000 - 성공 \n" +
					"9999 - 관리자에게 문의하세요."
			)
	})
	public ApiResult<?> putUserDormant(CommonPutUserDormantRequestDto dto) throws Exception {

		userDormancyBiz.putUserDormant(dto);

		return ApiResult.success();
	}

	/**
	 * 휴면회원 이력리스트조회
	 * @param dto
	 * @return GetUserDormantHistoryListResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/ur/UserDormant/getUserDormantHistoryList")
	@ApiOperation(value = "탈퇴회원 목록 조회", httpMethod = "POST", notes = "휴면회원 리스트조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = ApiResult.class),
			@ApiResponse(code = 901, message = "" +
					"0000 - 성공 \n" +
					"9999 - 관리자에게 문의하세요."
			)
	})
	public ApiResult<?> getUserDormantHistoryList(GetUserDormantHistoryListRequestDto dto) throws Exception {

		return ApiResult.success(userDormantService.getUserDormantHistoryList((GetUserDormantHistoryListRequestDto) BindUtil.convertRequestToObject(request, GetUserDormantHistoryListRequestDto.class)));
	}

}



