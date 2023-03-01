package kr.co.pulmuone.bos.approval.auth;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.approval.auth.dto.ApprovalAuthByTaskDto;
import kr.co.pulmuone.v1.approval.auth.dto.ApprovalAuthByTaskInfoDto;
import kr.co.pulmuone.v1.approval.auth.dto.ApprovalAuthManagerHistoryByTaskDto;
import kr.co.pulmuone.v1.approval.auth.dto.ApprovalStatusHistoryDto;
import kr.co.pulmuone.v1.approval.auth.service.ApprovalAuthBiz;
import kr.co.pulmuone.v1.comm.base.ApiResult;
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
 *  1.0		20210114		박승현              최초작성
 *
 * =======================================================================
 * </PRE>
 *
 */
@RestController
@RequiredArgsConstructor
public class ApprovalAuthController {

	@Autowired
	private ApprovalAuthBiz approvalAuthBiz;

	@Autowired(required=true)
	private HttpServletRequest request;

	/**
	 * 업무별 승인권한 관리 목록 조회
	 * @param masterApprovalKindType
	 * @return ApiResult
	 */
	@RequestMapping(value = "/admin/approval/auth/getApprovalAuthByTaskList")
	@ApiOperation(value = "업무별 승인권한 관리 목록 조회", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "masterApprovalKindType", value = "승인종류유형 마스터코드", required = true, dataType = "String")})
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = ApprovalAuthByTaskDto.class)
	})
	public ApiResult<?> getApprovalAuthByTaskList(@RequestParam(value = "masterApprovalKindType", required = true) String masterApprovalKindType) {
		return approvalAuthBiz.getApprovalAuthByTaskList(masterApprovalKindType);
	}

	/**
	 * 업무별 승인관리자 정보 조회
	 * @param taskCode
	 * @return ApiResult
	 */
	@RequestMapping(value = "/admin/approval/auth/getApprovalAuthByTaskInfo")
	@ApiOperation(value = "업무별 승인관리자 정보 조회", httpMethod = "GET")
    @ApiImplicitParams({ @ApiImplicitParam(name = "taskCode", value = "승인권한관리 대상 업무 코드", required = true, dataType = "String")})
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = ApprovalAuthByTaskInfoDto.class)
	})
	public ApiResult<?> getApprovalAuthByTaskInfo(@RequestParam(value = "taskCode", required = true) String taskCode) {
		return approvalAuthBiz.getApprovalAuthByTaskInfo(taskCode);
	}

	/**
     * 업무별 승인관리자 정보 저장
     *
     * @param ApprovalAuthByTaskInfoDto
     * @return int
     */
	@RequestMapping(value = "/admin/approval/auth/putApprovalAuthByTaskInfo")
	@ApiOperation(value = "업무별 승인관리자 정보 저장", httpMethod = "POST")
	@ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : Integer.class"),
    })
	public ApiResult<?> putApprovalAuthByTaskInfo(@RequestBody ApprovalAuthByTaskInfoDto reqData) throws Exception{
		return approvalAuthBiz.putApprovalAuthByTaskInfo(reqData);
	}

	/**
	 * 업무별 직전 승인관리자 이력 조회
	 * @param taskCode
	 * @return ApiResult
	 */
	@RequestMapping(value = "/admin/approval/auth/getApprovalAuthManagerHistoryByTask")
	@ApiOperation(value = "업무별 직전 승인관리자 이력 조회", httpMethod = "GET")
    @ApiImplicitParams({ @ApiImplicitParam(name = "taskCode", value = "승인권한관리 대상 업무 코드", required = true, dataType = "String")})
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = ApprovalAuthManagerHistoryByTaskDto.class)
	})
	public ApiResult<?> getApprovalAuthManagerHistoryByTask(@RequestParam(value = "taskCode", required = true) String taskCode) {
		return approvalAuthBiz.getApprovalAuthManagerHistoryByTask(taskCode);
	}

	/**
	 * 업무별 승인내역 이력 조회
	 * @param taskCode, taskPk
	 * @return ApiResult
	 */
	@PostMapping(value = "/admin/approval/auth/getApprovalHistory")
	@ApiOperation(value = "업무별 승인내역 이력 조회", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "taskCode", value = "승인권한관리 대상 업무 코드", required = true, dataType = "String")})
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = ApprovalStatusHistoryDto.class)
	})
	public ApiResult<?> getApprovalHistory(@RequestParam(value = "taskCode", required = true) String taskCode,
													@RequestParam(value = "taskPk", required = true) String taskPk) {
	    return approvalAuthBiz.getApprovalHistory(taskCode, taskPk);
	}

	/**
     * 승인 진행 상태 정보 조회
     *
     * @param taskCode, taskPk
     * @return ApiResult
     */
    @RequestMapping(value = "/admin/approval/auth/getApprovalProcessBizInfo")
    @ApiOperation(value = "승인 진행 상태 정보 조회", httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : Integer.class"),
    })
    public ApiResult<?> getApprovalProcessBizInfo(@RequestParam(value = "taskCode", required = true) String taskCode,
														@RequestParam(value = "taskPk", required = true) String taskPk) throws Exception {
        return approvalAuthBiz.getApprovailProcessBizInfo(taskCode, taskPk);
    }

	/**
	 * 품목 가격 승인, 상품 할인 승인 중복 체크
	 *
	 * @param taskCode, taskPk
	 * @return ApiResult
	 */
	@RequestMapping(value = "/admin/approval/auth/checkDuplicatePriceApproval")
	@ApiOperation(value = "품목 가격 승인, 상품 할인 승인 중복 체크", httpMethod = "GET")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class"),
	})
	public ApiResult<?> checkDuplicatePriceApproval(@RequestParam(value = "taskCode", required = true) String taskCode,
												  @RequestParam(value = "itemCode", required = true) String itemCode) throws Exception {
		return approvalAuthBiz.checkDuplicatePriceApproval(taskCode, itemCode);
	}

}

