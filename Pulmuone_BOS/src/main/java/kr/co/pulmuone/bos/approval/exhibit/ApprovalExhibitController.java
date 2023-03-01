package kr.co.pulmuone.bos.approval.exhibit;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.approval.auth.dto.ApprovalExhibitRequestDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.ExhibitEnums.ExhibitMessage;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.promotion.manage.dto.ExhibitApprovalResponseDto;
import kr.co.pulmuone.v1.promotion.manage.service.ExhibitManageBiz;
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
 *  1.0		20210208		박승현              최초작성
 *
 * =======================================================================
 * </PRE>
 *
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class ApprovalExhibitController {

	@Autowired
	private ExhibitManageBiz exhibitManageBiz;

	@Autowired(required=true)
	private HttpServletRequest request;

	/**
	 * 기획전 승인 목록 조회
	 * @param ApprovalExhibitRequestDto
	 * @return ApiResult
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/approval/exhibit/getApprovalExhibitList")
	@ApiOperation(value = "기획전 승인 목록 조회", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = ExhibitApprovalResponseDto.class),
            @ApiResponse(code = 901, message = ""
                    + "EXHIBIT_MANAGE_PARAM_NO_INPUT - 입력정보가 존재하지 않습니다."
            		)
	})
	public ApiResult<?> getApprovalExhibitList(ApprovalExhibitRequestDto requestDto) throws Exception {
		if (requestDto == null) {
			//입력정보가 존재하지 않습니다.
			return ApiResult.result(ExhibitMessage.EXHIBIT_MANAGE_PARAM_NO_INPUT);
		}
		requestDto = (ApprovalExhibitRequestDto) BindUtil.convertRequestToObject(request, ApprovalExhibitRequestDto.class);
		return exhibitManageBiz.getApprovalExhibitList(requestDto);
	}

	/**
	 * 기획전 승인 요청철회
	 * @param ApprovalExhibitRequestDto
	 * @return ApiResult
	 */
	@RequestMapping(value = "/admin/approval/exhibit/putCancelRequestApprovalExhibit")
	@ApiOperation(value = "기획전 승인 요청철회", httpMethod = "POST")
	@ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : Integer.class"),
    })
	public ApiResult<?> putCancelRequestApprovalExhibit(@RequestBody ApprovalExhibitRequestDto dto) throws Exception {
		return exhibitManageBiz.putCancelRequestApprovalExhibit(dto);
	}
	/**
	 * 기획전 승인 처리
	 * @param ApprovalExhibitRequestDto
	 * @return ApiResult
	 */
	@RequestMapping(value = "/admin/approval/exhibit/putApprovalProcessExhibit")
	@ApiOperation(value = "기획전 승인 처리", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class"),
	})
	public ApiResult<?> putApprovalProcessExhibit(@RequestBody ApprovalExhibitRequestDto dto) throws Exception {
		return exhibitManageBiz.putApprovalProcessExhibit(dto);
	}

	/**
	 * 기획전 승인 폐기처리
	 * @param ApprovalExhibitRequestDto
	 * @return ApiResult
	 */
	@RequestMapping(value = "/admin/approval/exhibit/putDisposalApprovalExhibit")
	@ApiOperation(value = "기획전 승인 폐기처리", httpMethod = "POST")
	@ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : Integer.class"),
    })
	public ApiResult<?> putDisposalApprovalExhibit(@RequestBody ApprovalExhibitRequestDto dto) throws Exception {
		return exhibitManageBiz.putDisposalApprovalExhibit(dto);
	}

}

