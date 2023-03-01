package kr.co.pulmuone.bos.approval.csrefund;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.approval.auth.dto.ApprovalCsRefundRequestDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.order.claim.service.ClaimCompleteProcessBiz;
import kr.co.pulmuone.v1.promotion.manage.dto.ExhibitApprovalResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
 *  1.0		20210514		홍진영              최초작성
 *
 * =======================================================================
 * </PRE>
 *
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class ApprovalCsRefundController {

	@Autowired
	private ClaimCompleteProcessBiz claimCompleteProcessBiz;

	@Autowired(required = true)
	private HttpServletRequest request;

	/**
	 * CS환불 승인 목록 조회
	 *
	 * @param ApprovalCsRefundRequestDto
	 * @return ApiResult
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/approval/csrefund/getApprovalCsRefundList")
	@ApiOperation(value = "CS환불 승인 목록 조회", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = ExhibitApprovalResponseDto.class)
	})
	public ApiResult<?> getApprovalCsRefundList() throws Exception {
		ApprovalCsRefundRequestDto requestDto = BindUtil.bindDto(request, ApprovalCsRefundRequestDto.class);
		return claimCompleteProcessBiz.getApprovalCsRefundList(requestDto);
	}

	/**
	 * CS환불 승인 요청철회
	 *
	 * @param ApprovalCsRefundRequestDto
	 * @return ApiResult
	 */
	@PostMapping(value = "/admin/approval/csrefund/putCancelRequestApprovalCsRefund")
	@ApiOperation(value = "CS환불 승인 요청철회", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class")
	})
	public ApiResult<?> putCancelRequestApprovalCsRefund(@RequestBody ApprovalCsRefundRequestDto dto) throws Exception {
		return claimCompleteProcessBiz.putCancelRequestApprovalCsRefund(dto.getOdCsIdList());
	}

	/**
	 * CS환불 승인 처리
	 *
	 * @param ApprovalCsRefundRequestDto
	 * @return ApiResult
	 */
	@PostMapping(value = "/admin/approval/csrefund/putApprovalProcessCsRefund")
	@ApiOperation(value = "CS환불 승인 처리", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class")
	})
	public ApiResult<?> putApprovalProcessCsRefund(@RequestBody ApprovalCsRefundRequestDto dto) throws Exception {
		return claimCompleteProcessBiz.putApprovalProcessCsRefund(dto.getApprStat(), dto.getOdCsIdList(), dto.getStatusComment());
	}

	/**
	 * CS환불 승인 폐기처리
	 * @param ApprovalCsRefundRequestDto
	 * @return ApiResult
	 */
	@RequestMapping(value = "/admin/approval/csrefund/putDisposalApprovalCsRefund")
	@ApiOperation(value = "CS환불 승인 폐기처리", httpMethod = "POST")
	@ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : Integer.class"),
    })
	public ApiResult<?> putDisposalApprovalCsRefund(@RequestBody ApprovalCsRefundRequestDto dto) throws Exception {
		return claimCompleteProcessBiz.putDisposalApprovalCsRefund(dto);
	}

}
