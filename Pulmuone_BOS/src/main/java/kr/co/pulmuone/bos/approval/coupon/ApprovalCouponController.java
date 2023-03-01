package kr.co.pulmuone.bos.approval.coupon;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.promotion.coupon.dto.CouponApprovalRequestDto;
import kr.co.pulmuone.v1.promotion.coupon.dto.CouponApprovalResponseDto;
import kr.co.pulmuone.v1.promotion.coupon.service.PromotionCouponBiz;
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
 *  1.0		20201228		박승현              최초작성
 *
 * =======================================================================
 * </PRE>
 *
 */
@RestController
@RequiredArgsConstructor
public class ApprovalCouponController {

	@Autowired
	private PromotionCouponBiz promotionCouponBiz;

	@Autowired(required=true)
	private HttpServletRequest request;

	/**
	 * 쿠폰승인 목록 조회
	 * @param CouponApprovalRequestDto
	 * @return ApiResult
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/approval/coupon/getApprovalCouponList")
	@ApiOperation(value = "쿠폰승인 목록 조회", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = CouponApprovalResponseDto.class)
	})
	public ApiResult<?> getApprovalCouponList(CouponApprovalRequestDto requestDto) throws Exception {
		return promotionCouponBiz.getApprovalCouponList((CouponApprovalRequestDto)BindUtil.convertRequestToObject(request, CouponApprovalRequestDto.class));
	}

	/**
	 * 쿠폰승인 요청철회
	 * @param CouponApprovalRequestDto
	 * @return ApiResult
	 */
	@RequestMapping(value = "/admin/approval/coupon/putCancelRequestApprovalCoupon")
	@ApiOperation(value = "쿠폰승인 요청철회", httpMethod = "POST")
	@ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : Integer.class"),
    })
	public ApiResult<?> putCancelRequestApprovalCoupon(@RequestBody CouponApprovalRequestDto dto) throws Exception {
		return promotionCouponBiz.putCancelRequestApprovalCoupon(dto);
	}

	/**
	 * 쿠폰승인 폐기처리
	 * @param CouponApprovalRequestDto
	 * @return ApiResult
	 */
	@RequestMapping(value = "/admin/approval/coupon/putDisposalApprovalCoupon")
	@ApiOperation(value = "쿠폰승인 폐기처리", httpMethod = "POST")
	@ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : Integer.class"),
    })
	public ApiResult<?> putDisposalApprovalCoupon(@RequestBody CouponApprovalRequestDto dto) throws Exception {
		return promotionCouponBiz.putDisposalApprovalCoupon(dto);
	}

	/**
	 * 쿠폰승인 처리
	 * @param CouponApprovalRequestDto
	 * @return ApiResult
	 */
	@RequestMapping(value = "/admin/approval/coupon/putApprovalProcessCoupon")
	@ApiOperation(value = "쿠폰승인 처리", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class"),
	})
	public ApiResult<?> putApprovalProcessCoupon(@RequestBody CouponApprovalRequestDto dto) throws Exception {
		return promotionCouponBiz.putApprovalProcessCoupon(dto);
	}
}

