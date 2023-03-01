package kr.co.pulmuone.bos.approval.item;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.approval.auth.dto.ApprovalItemRegistRequestDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.goods.item.dto.ItemApprovalResponseDto;
import kr.co.pulmuone.v1.goods.item.service.GoodsItemBiz;
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
 *  1.0		20210217		박승현              최초작성
 *
 * =======================================================================
 * </PRE>
 *
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class ApprovalItemRegistController {

	@Autowired
	private GoodsItemBiz goodsItemBiz;

	@Autowired(required=true)
	private HttpServletRequest request;

	/**
	 * 품목등록 승인 목록 조회
	 * @param ApprovalItemRegistRequestDto
	 * @return ApiResult
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/approval/item/getApprovalItemRegistList")
	@ApiOperation(value = "품목등록 승인 목록 조회", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = ItemApprovalResponseDto.class),
	})
	public ApiResult<?> getApprovalItemRegistList(ApprovalItemRegistRequestDto requestDto) throws Exception {
		return goodsItemBiz.getApprovalItemRegistList((ApprovalItemRegistRequestDto) BindUtil.convertRequestToObject(request, ApprovalItemRegistRequestDto.class));
	}

	/**
	 * 품목등록 승인 요청철회
	 * @param ApprovalItemRegistRequestDto
	 * @return ApiResult
	 */
	@RequestMapping(value = "/admin/approval/item/putCancelRequestApprovalItemRegist")
	@ApiOperation(value = "품목등록 승인 요청철회", httpMethod = "POST")
	@ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : Integer.class"),
    })
	public ApiResult<?> putCancelRequestApprovalItemRegist(@RequestBody ApprovalItemRegistRequestDto dto) throws Exception {
		return goodsItemBiz.putCancelRequestApprovalItemRegist(dto);
	}
	/**
	 * 품목등록 승인 처리
	 * @param ApprovalItemRegistRequestDto
	 * @return ApiResult
	 */
	@RequestMapping(value = "/admin/approval/item/putApprovalProcessItemRegist")
	@ApiOperation(value = "품목등록 승인 처리", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class"),
	})
	public ApiResult<?> putApprovalProcessItemRegist(@RequestBody ApprovalItemRegistRequestDto dto) throws Exception {
		return goodsItemBiz.putApprovalProcessItemRegist(dto);
	}
}

