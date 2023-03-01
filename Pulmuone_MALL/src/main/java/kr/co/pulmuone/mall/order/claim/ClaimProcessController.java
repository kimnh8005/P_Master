package kr.co.pulmuone.mall.order.claim;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.enums.ClaimEnums;
import kr.co.pulmuone.v1.comm.enums.OrderClaimEnums;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.util.DeviceUtil;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.order.claim.dto.MallOrderClaimAddPaymentRequest;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimRegisterRequestDto;
import kr.co.pulmuone.v1.order.claim.service.*;
import kr.co.pulmuone.v1.pg.dto.BasicDataResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
* <PRE>
* Forbiz Korea
* 주문클레임 관련 Controller
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0  2021. 03. 02.     강상국          최초작성
* =======================================================================
* </PRE>
*/
@Slf4j
@RestController
@RequiredArgsConstructor
public class ClaimProcessController {

	@Autowired
    private final ClaimProcessBiz claimProcessBiz;

    /**
     * 주문취소 화면에서 전체 주문취소 또는 부분취소 할 때
     *
     * @param orderClaimRegisterRequestDto
     * @return ApiResult<?>
     */
    @ApiOperation(value = "전체 주문취소 또는 부분취소")
    @PostMapping(value = "/order/claim/addOrderClaimCancel")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class") })
	public ApiResult<?> addOrderClaimCancel(OrderClaimRegisterRequestDto orderClaimRegisterRequestDto) throws Exception {
		// 로그인 체크
//		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
//		if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
//			return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
//		}

    	orderClaimRegisterRequestDto.setFrontTp(1);
		orderClaimRegisterRequestDto.setClaimStatusTp(OrderClaimEnums.ClaimStatusTp.CANCEL.getCode());
		return claimProcessBiz.addOrderClaim(orderClaimRegisterRequestDto);
	}

    /**
     * 반품신청 화면에서 반품 신청 할 때
     *
     * @param orderClaimRegisterRequestDto
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "반품신청")
    @PostMapping(value = "/order/claim/addOrderClaimRefund")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class") })
	public ApiResult<?> addOrderClaimRefund(OrderClaimRegisterRequestDto orderClaimRegisterRequestDto) throws Exception {
		// 로그인 체크
//		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
//		if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
//			return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
//		}

    	orderClaimRegisterRequestDto.setFrontTp(1);
		// 몰에서 반품 신청은 무조건 회수 조건
		orderClaimRegisterRequestDto.setReturnsYn(ClaimEnums.ReturnsYn.RETURNS_YN_Y.getCode());
		orderClaimRegisterRequestDto.setClaimStatusCd(OrderEnums.OrderStatus.RETURN_APPLY.getCode());
		orderClaimRegisterRequestDto.setClaimStatusTp(OrderClaimEnums.ClaimStatusTp.RETURN.getCode());
		return claimProcessBiz.addOrderClaim(orderClaimRegisterRequestDto);
	}

	/**
	 * 주문취소 또는 반품 신청 할 때 추가 결제
	 * @param orderClaimRegisterRequestDto
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/order/claim/addPayment")
	@ApiOperation(value = "추가결제", httpMethod = "POST")
	@ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = BasicDataResponseDto.class),
			@ApiResponse(code = 901, message = ""
					+ "[GOODS_LACK_STOCK] GOODS_LACK_STOCK - 상품 재고 수량 보다 장바구니 수량이 더 큰 경우 \n"
					+ "[NON_MEMBER_NOT_POINT] NON_MEMBER_NOT_POINT - 비회원 포인트 사용 불가 \n"
					+ "[NON_MEMBER_NOT_REGULAR] NON_MEMBER_NOT_REGULAR - 비회원 정기결제 정보 이용 안됨 \n"
					+ "[NOT_BUY_GOODS] NOT_BUY_GOODS - 구매 불가 상품 \n"
					+ "[ORVER_AVAILABLE_POINT] ORVER_AVAILABLE_POINT - 가용 포인트 보다 많은 포인트 사용 \n"
					+ "[REQUIRED_NON_MEMBER_CERTIFICATION] REQUIRED_NON_MEMBER_CERTIFICATION - 비회원 본인인증 정보 없음 \n"
					+ "[REQUIRED_NON_MEMBER_EMAIL] REQUIRED_NON_MEMBER_EMAIL - 비회원 이메일 정보 없음 \n"
					+ "[REQUIRED_REFUND_ACCOUNT] REQUIRED_REFUND_ACCOUNT - 환불계좌 정보 없음 \n"
					+ "[REQUIRED_REGULAR_PAYMENT] REQUIRED_REGULAR_PAYMENT - 정기배송 결제 정보 없음 \n"
					+ "[USE_DUPLICATE_COUPON] USE_DUPLICATE_COUPON - 중복 쿠폰 사용 \n"
					+ "[FAIL_VALIDATION_GIFT] FAIL_VALIDATION_GIFT - 증정품 검증 실패") })
	public ApiResult<?> addPayment(OrderClaimRegisterRequestDto orderClaimRegisterRequestDto) throws Exception {
		// 로그인 체크
//		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
//		if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
//			return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
//		}

		orderClaimRegisterRequestDto.setFrontTp(1);
		return claimProcessBiz.addPayment(orderClaimRegisterRequestDto);
	}

	/**
	 * MALL > 상담접수상태의 렌탈상품 취소
	 *
	 * @param orderClaimRegisterRequestDto
	 * @return ApiResult<?>
	 */
	@ApiOperation(value = "MALL > 상담접수상태의 렌탈상품 취소")
	@PostMapping(value = "/order/claim/addOrderClaimCancelForRental")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class") })
	public ApiResult<?> addOrderClaimCancelForRental(OrderClaimRegisterRequestDto orderClaimRegisterRequestDto) throws Exception {

		// 로그인 체크
		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
		if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
			return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
		}

		return claimProcessBiz.addOrderClaimRental(orderClaimRegisterRequestDto);
	}
}