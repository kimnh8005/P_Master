package kr.co.pulmuone.bos.approval.item;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.approval.auth.dto.ApprovalItemRegistRequestDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.goods.item.dto.ItemApprovalResponseDto;
import kr.co.pulmuone.v1.goods.item.service.GoodsItemBiz;
import kr.co.pulmuone.v1.goods.item.service.GoodsItemModifyBiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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
public class ApprovalItemClientController {

	private final GoodsItemBiz goodsItemBiz;

	private final GoodsItemModifyBiz goodsItemModifyBiz;

	private final HttpServletRequest request;

	/**
	 * 품목등록 승인 목록 조회
	 * @param ApprovalItemRegistRequestDto
	 * @return ApiResult
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/approval/item/getApprovalItemClientList")
	@ApiOperation(value = "품목등록 승인 목록 조회", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = ItemApprovalResponseDto.class),
	})
	public ApiResult<?> getApprovalItemClientList(ApprovalItemRegistRequestDto requestDto) throws Exception {
		return goodsItemBiz.getApprovalItemClientList((ApprovalItemRegistRequestDto) BindUtil.convertRequestToObject(request, ApprovalItemRegistRequestDto.class));
	}

	/**
	 * 품목등록 승인 요청철회
	 * @param ApprovalItemClientRequestDto
	 * @return ApiResult
	 */
	@RequestMapping(value = "/admin/approval/item/putCancelRequestApprovalItemClient")
	@ApiOperation(value = "품목등록 승인 요청철회", httpMethod = "POST")
	@ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : Integer.class"),
    })
	public ApiResult<?> putCancelRequestApprovalItemClient(@RequestBody ApprovalItemRegistRequestDto dto) throws Exception {
		return goodsItemBiz.putCancelRequestApprovalItemClient(dto);
	}

	/**
	 * 거래처 품목수정 승인 폐기처리
	 * @param ApprovalItemRegistRequestDto
	 * @return ApiResult
	 */
	@RequestMapping(value = "/admin/approval/item/putDisposalApprovalItemClient")
	@ApiOperation(value = "거래처 품목수정 승인 폐기처리", httpMethod = "POST")
	@ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : Integer.class"),
    })
	public ApiResult<?> putDisposalApprovalItemClient(@RequestBody ApprovalItemRegistRequestDto dto) throws Exception {
		return goodsItemBiz.putDisposalApprovalItemClient(dto);
	}

	/**
	 * 품목등록 승인 처리
	 * @param ApprovalItemClientRequestDto
	 * @return ApiResult
	 */
	@RequestMapping(value = "/admin/approval/item/putApprovalProcessItemClient")
	@ApiOperation(value = "품목등록 승인 처리", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class"),
	})
	public ApiResult<?> putApprovalProcessItemClient(@RequestBody ApprovalItemRegistRequestDto dto) throws Exception {
		return goodsItemBiz.putApprovalProcessItemClient(dto);
	}

}

