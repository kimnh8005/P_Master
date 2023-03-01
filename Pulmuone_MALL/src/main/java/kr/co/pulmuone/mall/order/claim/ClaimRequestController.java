package kr.co.pulmuone.mall.order.claim;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.enums.ClaimEnums;
import kr.co.pulmuone.v1.comm.enums.OrderClaimEnums.ClaimStatusTp;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.order.claim.dto.MallOrderClaimAddPaymentRequest;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimViewRequestDto;
import kr.co.pulmuone.v1.order.claim.service.ClaimRequestBiz;
import kr.co.pulmuone.v1.policy.claim.dto.PolicyClaimMallRequestDto;
import kr.co.pulmuone.v1.policy.claim.dto.vo.PolicyClaimMallVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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
public class ClaimRequestController {

	@Autowired
	private final ClaimRequestBiz claimRequestBiz;


    /**
     * 마이페이지 주문취소 정보 조회
     *
     * @param orderClaimViewRequestDto
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "마이페이지 주문취소 정보 조회")
    @PostMapping(value = "/order/claim/getOrderCanCelList")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class") })
    public ApiResult<?> getOrderCanCelList(OrderClaimViewRequestDto orderClaimViewRequestDto) throws Exception {
		// 로그인 체크
		// 비회원도 마이페이지에서 주문취소 가능 -> 주석처리
		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
//		if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
//			return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
//		}
		if (StringUtil.isEmpty(buyerVo.getUrUserId()) && StringUtil.isEmpty(buyerVo.getNonMemberCiCd())) {
			return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
		}
		if (StringUtil.isNotEmpty(buyerVo.getUrUserId())){
			orderClaimViewRequestDto.setUrUserId(buyerVo.getUrUserId());
		} else{
			orderClaimViewRequestDto.setGuestCi(buyerVo.getNonMemberCiCd());
		}

		orderClaimViewRequestDto.setClaimStatusTp(ClaimStatusTp.CANCEL.getCode());
    	orderClaimViewRequestDto.setFrontTp(1);
		orderClaimViewRequestDto.setDbStatusCheckYn("N");
    	log.debug("전체 조회 parameter :: <{}>", orderClaimViewRequestDto);
		return claimRequestBiz.getOrderClaimInfo(orderClaimViewRequestDto);
    }

    /**
     * 마이페이지 클레임수량 변경 시 주문취소 정보 조회
     *
     * @param orderClaimViewRequestDto
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "마이페이지 클레임수량 변경 시 주문취소 정보 조회")
    @PostMapping(value = "/order/claim/getOrderCanCelChangeList")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class") })
    public ApiResult<?> getOrderCanCelChangeList(OrderClaimViewRequestDto orderClaimViewRequestDto) throws Exception {
		// 로그인 체크
		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
//		if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
//			return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
//		}
		if (StringUtil.isEmpty(buyerVo.getUrUserId()) && StringUtil.isEmpty(buyerVo.getNonMemberCiCd())) {
			return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
		}
		if (StringUtil.isNotEmpty(buyerVo.getUrUserId())){
			orderClaimViewRequestDto.setUrUserId(buyerVo.getUrUserId());
		} else{
			orderClaimViewRequestDto.setGuestCi(buyerVo.getNonMemberCiCd());
		}

		orderClaimViewRequestDto.setClaimStatusTp(ClaimStatusTp.CANCEL.getCode());
    	orderClaimViewRequestDto.setFrontTp(1);
    	orderClaimViewRequestDto.setDbStatusCheckYn("N");
    	log.debug("클레임수량 변경 시 전체 조회 parameter :: <{}>", orderClaimViewRequestDto);
		return claimRequestBiz.getOrderClaimInfo(orderClaimViewRequestDto);
    }



    /**
     * 마이페이지 반품신청 정보 조회
     *
     * @param orderClaimViewRequestDto
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "마이페이지 반품신청 정보 조회")
    @PostMapping(value = "/order/claim/getOrderRefundList")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class") })
    public ApiResult<?> getOrderRefundList(OrderClaimViewRequestDto orderClaimViewRequestDto) throws Exception {
		// 로그인 체크
		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
//		if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
//			return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
//		}
		if (StringUtil.isEmpty(buyerVo.getUrUserId()) && StringUtil.isEmpty(buyerVo.getNonMemberCiCd())) {
			return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
		}
		if (StringUtil.isNotEmpty(buyerVo.getUrUserId())){
			orderClaimViewRequestDto.setUrUserId(buyerVo.getUrUserId());
		} else{
			orderClaimViewRequestDto.setGuestCi(buyerVo.getNonMemberCiCd());
		}

		orderClaimViewRequestDto.setClaimStatusTp(ClaimStatusTp.RETURN.getCode());
    	// 몰에서 반품 신청은 무조건 회수 조건
		orderClaimViewRequestDto.setReturnsYn(ClaimEnums.ReturnsYn.RETURNS_YN_Y.getCode());
		orderClaimViewRequestDto.setClaimStatusCd(OrderEnums.OrderStatus.RETURN_APPLY.getCode());
    	orderClaimViewRequestDto.setFrontTp(1);
		orderClaimViewRequestDto.setDbStatusCheckYn("N");
    	log.debug("반품신청 조회 parameter :: <{}>", orderClaimViewRequestDto);
		return claimRequestBiz.getOrderClaimInfo(orderClaimViewRequestDto);
    }

    /**
     * 마이페이지 클레임수량 변경 시 반품신청 정보 조회
     *
     * @param orderClaimViewRequestDto
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "마이페이지 클레임수량 변경 시 반품신청 정보 조회")
    @PostMapping(value = "/order/claim/getOrderRefundChangeList")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class") })
    public ApiResult<?> getOrderRefundChangeList(OrderClaimViewRequestDto orderClaimViewRequestDto) throws Exception {
		// 로그인 체크
		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
//		if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
//			return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
//		}
		if (StringUtil.isEmpty(buyerVo.getUrUserId()) && StringUtil.isEmpty(buyerVo.getNonMemberCiCd())) {
			return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
		}
		if (StringUtil.isNotEmpty(buyerVo.getUrUserId())){
			orderClaimViewRequestDto.setUrUserId(buyerVo.getUrUserId());
		} else{
			orderClaimViewRequestDto.setGuestCi(buyerVo.getNonMemberCiCd());
		}

    	orderClaimViewRequestDto.setClaimStatusTp(ClaimStatusTp.RETURN.getCode());
		orderClaimViewRequestDto.setReturnsYn(ClaimEnums.ReturnsYn.RETURNS_YN_Y.getCode());
		orderClaimViewRequestDto.setClaimStatusCd(OrderEnums.OrderStatus.RETURN_APPLY.getCode());
    	orderClaimViewRequestDto.setFrontTp(1);
		orderClaimViewRequestDto.setDbStatusCheckYn("N");
    	log.debug("클레임수량변경 반품신청 조회 parameter :: <{}>", orderClaimViewRequestDto);
		return claimRequestBiz.getOrderClaimInfo(orderClaimViewRequestDto);
    }

	/**
	 * 추가 배송비 결제 금액 조회
	 *
	 * @param mallOrderClaimAddPaymentRequest
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "추가 배송비 결제 금액 조회")
	@PostMapping(value = "/order/claim/getOrderClaimAddShippingPrice")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class") })
	public ApiResult<?> getOrderClaimAddShippingPrice(MallOrderClaimAddPaymentRequest mallOrderClaimAddPaymentRequest) throws Exception {
		// 로그인 체크
		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
		if (StringUtil.isEmpty(buyerVo.getUrUserId()) && StringUtil.isEmpty(buyerVo.getNonMemberCiCd())) {
			return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
		}
		if (StringUtil.isNotEmpty(buyerVo.getUrUserId())){
			mallOrderClaimAddPaymentRequest.setLoginId(buyerVo.getLoginId());
			mallOrderClaimAddPaymentRequest.setUrUserId(buyerVo.getUrUserId());
			mallOrderClaimAddPaymentRequest.setCustomUrUserId(buyerVo.getUrUserId());
		} else{
			mallOrderClaimAddPaymentRequest.setGuestCi(buyerVo.getNonMemberCiCd());
		}

		log.debug("추가 배송비 결제 금액 조회 parameter :: <{}>", mallOrderClaimAddPaymentRequest.getOdClaimId());
		return claimRequestBiz.getOrderClaimAddShippingPrice(mallOrderClaimAddPaymentRequest);
	}

	/**
	 * 추가 배송비 결제 요청
	 *
	 * @param mallOrderClaimAddPaymentRequest
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "추가 배송비 결제 요청")
	@PostMapping(value = "/order/claim/applyOrderClaimAddShippingPrice")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class") })
	public ApiResult<?> applyOrderClaimAddShippingPrice(MallOrderClaimAddPaymentRequest mallOrderClaimAddPaymentRequest) throws Exception {
		// 로그인 체크
		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
		if (StringUtil.isEmpty(buyerVo.getUrUserId()) && StringUtil.isEmpty(buyerVo.getNonMemberCiCd())) {
			return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
		}
		if (StringUtil.isNotEmpty(buyerVo.getUrUserId())){
			mallOrderClaimAddPaymentRequest.setLoginId(buyerVo.getLoginId());
			mallOrderClaimAddPaymentRequest.setUrUserId(buyerVo.getUrUserId());
			mallOrderClaimAddPaymentRequest.setCustomUrUserId(buyerVo.getUrUserId());
		} else{
			mallOrderClaimAddPaymentRequest.setGuestCi(buyerVo.getNonMemberCiCd());
		}

		log.debug("추가 배송비 결제 금액 조회 parameter :: <{}>", mallOrderClaimAddPaymentRequest.getOdClaimId());
		return claimRequestBiz.applyOrderClaimAddShippingPrice(mallOrderClaimAddPaymentRequest);
	}

	/**
	 * 쇼핑몰 사유 목록 조회
	 *
	 * @param policyClaimMallRequestDto
	 * @return ApiResult<?>
	 */
	@PostMapping(value = "/order/getPolicyClaimMallListForMall")
	@ApiOperation(value = "쇼핑몰 사유 목록 조회", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = PolicyClaimMallVo.class)})
	public ApiResult<?> getPolicyClaimMallListForMall(PolicyClaimMallRequestDto policyClaimMallRequestDto) throws Exception {

		// 로그인 체크
		// 비회원도 주문취소 가능 -> 주석처리
//        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
//        if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
//            return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
//        }
		return claimRequestBiz.getPolicyClaimMallListForMall(policyClaimMallRequestDto);
	}
}