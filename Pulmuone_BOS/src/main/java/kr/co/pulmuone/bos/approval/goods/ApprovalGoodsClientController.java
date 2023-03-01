package kr.co.pulmuone.bos.approval.goods;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.approval.auth.dto.ApprovalGoodsRequestDto;
import kr.co.pulmuone.v1.approval.auth.service.ApprovalAuthBiz;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsApprovalResponseDto;
import kr.co.pulmuone.v1.goods.goods.service.GoodsListBiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
 * 버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 * 1.0		20210409		최윤석              최초작성
 * <p>
 * =======================================================================
 * </PRE>
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class ApprovalGoodsClientController {

    private final GoodsListBiz goodsListBiz;

    private final ApprovalAuthBiz approvalAuthBiz;

    private final HttpServletRequest request;

    /**
     * 상품 수정 승인 목록 조회
     *
     * @param ApprovalGoodsRequestDto
     * @return ApiResult
     * @throws Exception
     */
    @PostMapping(value = "/admin/approval/goods/getApprovalGoodsClientList")
    @ApiOperation(value = "상품 수정 승인 목록 조회", httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data List<>", response = GoodsApprovalResponseDto.class),
    })
    public ApiResult<?> getApprovalGoodsClientList(ApprovalGoodsRequestDto requestDto) throws Exception {
        return goodsListBiz.getApprovalGoodsClientList((ApprovalGoodsRequestDto) BindUtil.convertRequestToObject(request, ApprovalGoodsRequestDto.class));
    }

    /**
     * 상품 수정 승인 요청철회
     *
     * @param ApprovalGoodsRequestDto
     * @return ApiResult
     */
    @RequestMapping(value = "/admin/approval/goods/putCancelRequestApprovalGoodsClient")
    @ApiOperation(value = "상품 수정 승인 요청철회", httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : Integer.class"),
    })
    public ApiResult<?> putCancelRequestApprovalGoodsClient(@RequestBody ApprovalGoodsRequestDto dto) throws Exception {
        return goodsListBiz.putCancelRequestApprovalGoodsClient(dto);
    }

	/**
	 * 거래처 상품수정 승인 폐기처리
	 * @param ApprovalGoodsRequestDto
	 * @return ApiResult
	 */
	@RequestMapping(value = "/admin/approval/goods/putDisposalApprovalGoodsClient")
	@ApiOperation(value = "거래처 상품수정 승인 폐기처리", httpMethod = "POST")
	@ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : Integer.class"),
    })
	public ApiResult<?> putDisposalApprovalGoodsClient(@RequestBody ApprovalGoodsRequestDto dto) throws Exception {
		return goodsListBiz.putDisposalApprovalGoodsClient(dto);
	}

    /**
     * 상품 수정 승인 처리
     *
     * @param ApprovalGoodsRequestDto
     * @return ApiResult
     */
    @RequestMapping(value = "/admin/approval/goods/putApprovalProcessGoodsClient")
    @ApiOperation(value = "상품 수정 승인 처리", httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : Integer.class"),
    })
    public ApiResult<?> putApprovalProcessGoodsClient(@RequestBody ApprovalGoodsRequestDto dto) throws Exception {
        return goodsListBiz.putApprovalProcessGoodsClient(dto);
    }

     /**
     * 상품 수정 승인 정보 조회
     *
     * @param String
     * @return ApiResult
     */
    @RequestMapping(value = "/admin/approval/goods/getGoodsClientDetail")
    @ApiOperation(value = "상품 수정 승인 정보 조회", httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : ApiResult.class"),
    })
    public ApiResult<?> getGoodsClientDetail(@RequestParam String ilGoodsApprId) throws Exception {
        return goodsListBiz.getGoodsClientDetail(ilGoodsApprId);
    }

}