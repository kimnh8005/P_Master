package kr.co.pulmuone.mall.order.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.order.order.dto.mall.MallOrderDailyListRequestDto;
import kr.co.pulmuone.v1.order.order.dto.mall.MallOrderDailyListResponseDto;
import kr.co.pulmuone.v1.order.order.dto.mall.MallOrderListRequestDto;
import kr.co.pulmuone.v1.order.order.dto.mall.MallOrderListResponseDto;
import kr.co.pulmuone.v1.order.order.service.MallOrderListBiz;

/**
 * <PRE>
 * Forbiz Korea
 * Mall 주문리스트 관련 Controller
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 01. 12.	이규한		최초작성
 * =======================================================================
 * </PRE>
 */

@RestController
public class OrderListController {

    @Autowired
    private MallOrderListBiz mallOrderListBiz;	// Mall 주문리스트 관련 Biz

    /**
     * 주문/배송 리스트 조회
     *
     * @param mallOrderListRequestDto
     * @return ApiResult<?>
     */
	@PostMapping(value = "/order/getOrderList")
	@ApiOperation(value = "주문/배송 리스트 조회", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = MallOrderListResponseDto.class),
			@ApiResponse(code = 901, message = "" + "[NEED_LOGIN] NEED_LOGIN - 로그인 필요 \n") })
    public ApiResult<?> getOrderList(MallOrderListRequestDto mallOrderListRequestDto) throws Exception {
        // 로그인 체크
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();

        if (StringUtil.isEmpty(buyerVo.getUrUserId()) && StringUtil.isEmpty(buyerVo.getNonMemberCiCd())) {
            return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
        }
        if (StringUtil.isNotEmpty(buyerVo.getUrUserId())){
        	mallOrderListRequestDto.setUrUserId(Long.parseLong(buyerVo.getUrUserId()));
        } else{
        	mallOrderListRequestDto.setGuestCi(buyerVo.getNonMemberCiCd());
        }


        return mallOrderListBiz.getOrderList(mallOrderListRequestDto);
    }

    /**
     * 취소/반품 리스트 조회
     *
     * @param mallOrderListRequestDto
     * @return ApiResult<?>
     */
	@PostMapping(value = "/order/getOrderClaimList")
	@ApiOperation(value = "취소/반품 리스트 조회", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = MallOrderListResponseDto.class),
			@ApiResponse(code = 901, message = "" + "[NEED_LOGIN] NEED_LOGIN - 로그인 필요 \n") })
    public ApiResult<?> getOrderClaimList(MallOrderListRequestDto mallOrderListRequestDto) throws Exception {
        // 로그인 체크
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (StringUtil.isEmpty(buyerVo.getUrUserId()) && StringUtil.isEmpty(buyerVo.getNonMemberCiCd())) {
            return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
        }
        if (StringUtil.isNotEmpty(buyerVo.getUrUserId())){
            mallOrderListRequestDto.setUrUserId(Long.parseLong(buyerVo.getUrUserId()));
        } else{
            mallOrderListRequestDto.setGuestCi(buyerVo.getNonMemberCiCd());
        }

        return mallOrderListBiz.getOrderClaimList(mallOrderListRequestDto);
    }

    /**
     * 일일배송 리스트 조회
     *
     * @param mallOrderListRequestDto
     * @return ApiResult<?>
     */
	@PostMapping(value = "/order/getOrderDetailDailyList")
	@ApiOperation(value = "일일배송 리스트 조회", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = MallOrderDailyListResponseDto.class),
			@ApiResponse(code = 901, message = "" + "[NEED_LOGIN] NEED_LOGIN - 로그인 필요 \n") })
    public ApiResult<?> getOrderDetailDailyList(MallOrderDailyListRequestDto mallOrderDailyListRequestDto) throws Exception {
        // 로그인 체크
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
        }
        return mallOrderListBiz.getOrderDetailDailyList(mallOrderDailyListRequestDto);
    }

	/**
     * 주문/배송 리스트 조회
     *
     * @param mallOrderListRequestDto
     * @return ApiResult<?>
     */
	@PostMapping(value = "/order/getOrderPresentList")
	@ApiOperation(value = "보낸선물함 리스트 조회", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = MallOrderListResponseDto.class),
			@ApiResponse(code = 901, message = "" + "[NEED_LOGIN] NEED_LOGIN - 로그인 필요 \n") })
    public ApiResult<?> getOrderPresentList(MallOrderListRequestDto mallOrderListRequestDto) throws Exception {
        // 로그인 체크
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();

        if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
        }
        if (StringUtil.isNotEmpty(buyerVo.getUrUserId())){
        	mallOrderListRequestDto.setUrUserId(Long.parseLong(buyerVo.getUrUserId()));
        }
        return mallOrderListBiz.getOrderPresentList(mallOrderListRequestDto);
    }
}