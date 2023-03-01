package kr.co.pulmuone.bos.approval.goods;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.approval.auth.dto.ApprovalGoodsRequestDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsApprovalResponseDto;
import kr.co.pulmuone.v1.goods.goods.service.GoodsListBiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
public class ApprovalGoodsDiscountController {

	private final GoodsListBiz goodsListBiz;

	private final HttpServletRequest request;

	/**
	 * 상품할인 승인 목록 조회
	 * @param ApprovalGoodsRequestDto
	 * @return ApiResult
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/approval/goods/getApprovalGoodsDiscountList")
	@ApiOperation(value = "상품할인 승인 목록 조회", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = GoodsApprovalResponseDto.class),
	})
	public ApiResult<?> getApprovalGoodsDiscountList(ApprovalGoodsRequestDto requestDto) throws Exception {
		return goodsListBiz.getApprovalGoodsDiscountList((ApprovalGoodsRequestDto) BindUtil.convertRequestToObject(request, ApprovalGoodsRequestDto.class));
	}

	/**
	 * 상품할인 승인 요청철회
	 * @param ApprovalGoodsRequestDto
	 * @return ApiResult
	 */
	@RequestMapping(value = "/admin/approval/goods/putCancelRequestApprovalGoodsDiscount")
	@ApiOperation(value = "상품할인 승인 요청철회", httpMethod = "POST")
	@ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : Integer.class"),
    })
	public ApiResult<?> putCancelRequestApprovalGoodsDiscount(@RequestBody ApprovalGoodsRequestDto dto) throws Exception {
		return goodsListBiz.putCancelRequestApprovalGoodsDiscount(dto);
	}

	/**
	 * 상품할인 승인 처리
	 * @param ApprovalGoodsRequestDto
	 * @return ApiResult
	 */
	@RequestMapping(value = "/admin/approval/goods/putApprovalProcessGoodsDiscount")
	@ApiOperation(value = "상품할인 승인 처리", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class"),
	})
	public ApiResult<?> putApprovalProcessGoodsDiscount(@RequestBody ApprovalGoodsRequestDto dto) throws Exception {
		return goodsListBiz.putApprovalProcessGoodsDiscount(dto);
	}

	/**
	 * 상품할인 승인 폐기처리
	 * @param ApprovalGoodsRequestDto
	 * @return ApiResult
	 */
	@RequestMapping(value = "/admin/approval/goods/putDisposalApprovalGoodsDiscount")
	@ApiOperation(value = "상품할인 승인 폐기처리", httpMethod = "POST")
	@ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : Integer.class"),
    })
	public ApiResult<?> putDisposalApprovalGoodsDiscount(@RequestBody ApprovalGoodsRequestDto dto) throws Exception {
		return goodsListBiz.putDisposalApprovalGoodsDiscount(dto);
	}

}

