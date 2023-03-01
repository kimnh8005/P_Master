package kr.co.pulmuone.bos.approval.point;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.promotion.point.dto.PointApprovalRequestDto;
import kr.co.pulmuone.v1.promotion.point.dto.PointApprovalResponseDto;
import kr.co.pulmuone.v1.promotion.point.service.PromotionPointBiz;
import lombok.RequiredArgsConstructor;

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
 *  1.0		20201228		박승현              최초작성
 *
 * =======================================================================
 * </PRE>
 *
 */
@RestController
@RequiredArgsConstructor
public class ApprovalPointController {

	@Autowired
	private PromotionPointBiz promotionPointBiz;

	@Autowired(required=true)
	private HttpServletRequest request;

	/**
	 * 적립금승인 목록 조회
	 * @param PointApprovalRequestDto
	 * @return ApiResult
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/approval/point/getApprovalPointList")
	@ApiOperation(value = "적립금승인 목록 조회", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = PointApprovalResponseDto.class)
	})
	public ApiResult<?> getApprovalPointList(PointApprovalRequestDto requestDto) throws Exception {
		return promotionPointBiz.getApprovalPointList((PointApprovalRequestDto)BindUtil.convertRequestToObject(request, PointApprovalRequestDto.class));
	}

	/**
	 * 적립금승인 요청철회
	 * @param PointApprovalRequestDto
	 * @return ApiResult
	 */
	@RequestMapping(value = "/admin/approval/point/putCancelRequestApprovalPoint")
	@ApiOperation(value = "적립금승인 요청철회", httpMethod = "POST")
	@ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : Integer.class"),
    })
	public ApiResult<?> putCancelRequestApprovalPoint(@RequestBody PointApprovalRequestDto dto) throws Exception {
		return promotionPointBiz.putCancelRequestApprovalPoint(dto);
	}

	/**
	 * 적립금승인 폐기처리
	 * @param PointApprovalRequestDto
	 * @return ApiResult
	 */
	@RequestMapping(value = "/admin/approval/point/putDisposalApprovalPoint")
	@ApiOperation(value = "적립금승인 폐기처리", httpMethod = "POST")
	@ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : Integer.class"),
    })
	public ApiResult<?> putDisposalApprovalPoint(@RequestBody PointApprovalRequestDto dto) throws Exception {
		return promotionPointBiz.putDisposalApprovalPoint(dto);
	}

	/**
	 * 적립금승인 처리
	 * @param PointApprovalRequestDto
	 * @return ApiResult
	 */
	@RequestMapping(value = "/admin/approval/point/putApprovalProcessPoint")
	@ApiOperation(value = "적립금승인 처리", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class"),
	})
	public ApiResult<?> putApprovalProcessPoint(@RequestBody PointApprovalRequestDto dto) throws Exception {
		return promotionPointBiz.putApprovalProcessPoint(dto);
	}

}

