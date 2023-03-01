package kr.co.pulmuone.bos.approval.goods;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
public class ApprovalGoodsRegistController {

	@Autowired
	private GoodsListBiz goodsListBiz;

	@Autowired(required=true)
	private HttpServletRequest request;

	/**
	 * 상품등록 승인 목록 조회
	 * @param ApprovalGoodsRegistRequestDto
	 * @return ApiResult
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/approval/goods/getApprovalGoodsRegistList")
	@ApiOperation(value = "상품등록 승인 목록 조회", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = GoodsApprovalResponseDto.class),
	})
	public ApiResult<?> getApprovalGoodsRegistList(ApprovalGoodsRequestDto requestDto) throws Exception {
		return goodsListBiz.getApprovalGoodsRegistList((ApprovalGoodsRequestDto) BindUtil.convertRequestToObject(request, ApprovalGoodsRequestDto.class));
	}

	/**
	 * 상품등록 승인 요청철회
	 * @param ApprovalGoodsRegistRequestDto
	 * @return ApiResult
	 */
	@RequestMapping(value = "/admin/approval/goods/putCancelRequestApprovalGoodsRegist")
	@ApiOperation(value = "상품등록 승인 요청철회", httpMethod = "POST")
	@ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : Integer.class"),
    })
	public ApiResult<?> putCancelRequestApprovalGoodsRegist(@RequestBody ApprovalGoodsRequestDto dto) throws Exception {
		return goodsListBiz.putCancelRequestApprovalGoodsRegist(dto);
	}

	/**
	 * 상품등록 승인 처리
	 * @param ApprovalGoodsRegistRequestDto
	 * @return ApiResult
	 */
	@RequestMapping(value = "/admin/approval/goods/putApprovalProcessGoodsRegist")
	@ApiOperation(value = "상품등록 승인 처리", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class"),
	})
	public ApiResult<?> putApprovalProcessGoodsRegist(@RequestBody ApprovalGoodsRequestDto dto) throws Exception {
		return goodsListBiz.putApprovalProcessGoodsRegist(dto);
	}

}

