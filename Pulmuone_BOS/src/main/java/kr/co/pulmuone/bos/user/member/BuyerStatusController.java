package kr.co.pulmuone.bos.user.member;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.bos.user.member.service.BuyerStatusBosService;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.FileVo;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.user.buyer.dto.GetBuyerStatusHistoryListRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.GetBuyerStatusHistoryListResponseDto;
import kr.co.pulmuone.v1.user.buyer.dto.GetUserStopHistoryListRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.GetUserStopHistoryListResponseDto;
import kr.co.pulmuone.v1.user.buyer.dto.GetUserStopHistoryRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.GetUserStopHistoryResponseDto;
import kr.co.pulmuone.v1.user.buyer.dto.GetUserStopListRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.GetUserStopListResponseDto;
import kr.co.pulmuone.v1.user.buyer.dto.PutBuyerNormalRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.PutBuyerStopRequestDto;
import kr.co.pulmuone.v1.user.buyer.service.UserBuyerStatusBiz;

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
 *  1.0    20200623		   	천혜현            최초작성
 * =======================================================================
 * </PRE>
 */
@RestController
public class BuyerStatusController {

	@Autowired
	private UserBuyerStatusBiz userBuyerStatusBiz;

	@Autowired(required=true)
	private HttpServletRequest request;

	@Autowired
	private BuyerStatusBosService buyerStatusBosService;

	/**
	 * 정지회원 리스트조회
	 * @param getUserStopListRequestDto
	 * @return GetUserStopListResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/ur/buyerStatus/getBuyerStopList")
	@ApiOperation(value = "정지회원 목록 조회", httpMethod = "POST", notes = "정지회원 목록 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetUserStopListResponseDto.class),
			@ApiResponse(code = 901, message = "" +
					"0000 - 성공 \n" +
					"9999 - 관리자에게 문의하세요."
			)
	})
	public ApiResult<?> getUserStopList(GetUserStopListRequestDto getUserStopListRequestDto) throws Exception {

		return ApiResult.success(buyerStatusBosService.getBuyerStopList((GetUserStopListRequestDto) BindUtil.convertRequestToObject(request, GetUserStopListRequestDto.class)));
	}


	/**
	 * 정지회원 이력 상세조회
	 * @param getUserStopHistoryRequestDto
	 * @return GetUserStopHistoryResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/ur/buyerStatus/getBuyerStopLog")
	@ApiOperation(value = "회원 정지 이력 조회", httpMethod = "POST", notes = "회원 정지 이력 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetUserStopHistoryResponseDto.class),
			@ApiResponse(code = 901, message = "" +
					"0000 - 성공 \n" +
					"9999 - 관리자에게 문의하세요."
			)
	})
	public ApiResult<?> getUserStopHistory(GetUserStopHistoryRequestDto getUserStopHistoryRequestDto) throws Exception {

		return ApiResult.success(buyerStatusBosService.getBuyerStopLog(getUserStopHistoryRequestDto));
	}


	/**
	 * 정지회원 이력 리스트조회
	 * @param getUserStopHistoryListRequestDto
	 * @return GetUserStopHistoryListResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/ur/buyerStatus/getBuyerStopHistoryList")
	@ApiOperation(value = "정지회원 이력 목록 조회", httpMethod = "POST", notes = "정지회원 이력 목록 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetUserStopHistoryListResponseDto.class),
			@ApiResponse(code = 901, message = "" +
					"0000 - 성공 \n" +
					"9999 - 관리자에게 문의하세요."
			)
	})
	public ApiResult<?> getUserStopHistoryList(GetUserStopHistoryListRequestDto getUserStopHistoryListRequestDto) throws Exception {

		return ApiResult.success(buyerStatusBosService.getBuyerStopHistoryList((GetUserStopHistoryListRequestDto) BindUtil.convertRequestToObject(request, GetUserStopHistoryListRequestDto.class)));
	}

	/**
	 * 회원 상태변경 이력 리스트조회
	 * @param getBuyerStatusHistoryListRequestDto
	 * @return GetBuyerStatusHistoryListResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/ur/buyerStatus/getBuyerStatusHistoryList")
	@ApiOperation(value = "회원상태 변경이력 목록 조회", httpMethod = "POST", notes = "회원상태 변경이력 목록 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetBuyerStatusHistoryListResponseDto.class),
			@ApiResponse(code = 901, message = "" +
					"0000 - 성공 \n" +
					"9999 - 관리자에게 문의하세요."
			)
	})
	public ApiResult<?> getBuyerStatusHistoryList(GetBuyerStatusHistoryListRequestDto getBuyerStatusHistoryListRequestDto) throws Exception {

		return ApiResult.success(userBuyerStatusBiz.getBuyerStatusHistoryList(getBuyerStatusHistoryListRequestDto));
	}

	/**
	 * 회원상태 정지 설정
	 * @param putBuyerStopRequestDto
	 * @return PutBuyerStopResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/ur/buyerStatus/putBuyerStop")
	@ApiOperation(value = "회원상태 정지 설정", httpMethod = "POST", notes = "회원상태를 정지상태로 변경")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = ApiResult.class),
			@ApiResponse(code = 901, message = "" +
					"0000 - 성공 \n" +
					"9999 - 관리자에게 문의하세요."
			)
	})
	public ApiResult<?> putBuyerStop(PutBuyerStopRequestDto putBuyerStopRequestDto) throws Exception {

		return ApiResult.success(userBuyerStatusBiz.putBuyerStop(putBuyerStopRequestDto));
	}

	/**
	 * 회원상태 정상 설정
	 * @param putBuyerNormalRequestDto
	 * @return PutBuyerNormalResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/ur/buyerStatus/putBuyerNormal")
	@ApiOperation(value = "회원상태 정상 설정", httpMethod = "POST", notes = "회원상태를 정상상태로 변경")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = ApiResult.class),
			@ApiResponse(code = 901, message = "" +
					"0000 - 성공 \n" +
					"9999 - 관리자에게 문의하세요."
			)
	})
	public ApiResult<?> putBuyerNormal(PutBuyerNormalRequestDto putBuyerNormalRequestDto) throws Exception {

		if(!putBuyerNormalRequestDto.getUploadFileDeleteYn().equals("Y")) {
			putBuyerNormalRequestDto.setAddFileList(BindUtil.convertJsonArrayToDtoList(putBuyerNormalRequestDto.getAddFile(), FileVo.class));
		}

		return ApiResult.success(userBuyerStatusBiz.putBuyerNormal(putBuyerNormalRequestDto));
	}

}



