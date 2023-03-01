package kr.co.pulmuone.bos.order.claim;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimReShippingPaymentInfoRequestDto;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimViewRequestDto;
import kr.co.pulmuone.v1.order.claim.service.ClaimRequestBiz;
import kr.co.pulmuone.v1.policy.claim.dto.PolicyClaimMallRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
* <PRE>
* Forbiz Korea
* 주문클레임 상세정보 Controller
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0  2021. 01. 19.     강상국          최초작성
* =======================================================================
* </PRE>
*/
@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderClaimRequestController {

	@Autowired
    private final ClaimRequestBiz claimRequestBiz;

	/**
     * 주문클레임 신청 화면 정보 조회
     *
     * @param orderClaimViewRequestDto
     * @return ApiResult<?>
     */
    @ApiOperation(value = "주문클레임 신청 화면 정보 조회")
    @PostMapping(value = "/admin/order/claim/getOrderClaimView")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class") })
    public ApiResult<?> getOrderClaimView(@RequestBody OrderClaimViewRequestDto orderClaimViewRequestDto) throws Exception {
    	log.debug("전체 조회 parameter :: <{}>", orderClaimViewRequestDto);
		return claimRequestBiz.getOrderClaimInfo(orderClaimViewRequestDto);
	}

	/**
     * 주문 녹즙 클레임 신청 화면 정보 조회
     *
     * @param orderClaimViewRequestDto
     * @return ApiResult<?>
     */
    @ApiOperation(value = "주문 녹즙 클레임 신청 화면 정보 조회")
    @PostMapping(value = "/admin/order/claim/getOrderGreenJuiceClaimView")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class") })
    public ApiResult<?> getOrderGreenJuiceClaimView(@RequestBody OrderClaimViewRequestDto orderClaimViewRequestDto) throws Exception {
    	log.debug("주문 녹즙 클레임 신청 화면 정보 조회 조회 parameter :: <{}>", orderClaimViewRequestDto);
		return claimRequestBiz.getOrderGreenJuiceClaimView(orderClaimViewRequestDto);
	}

    /**
     * 주문 녹즙 반품 대상 상품 조회
     *
     * @param orderClaimViewRequestDto
     * @return ApiResult<?>
     */
    @ApiOperation(value = "주문 녹즙 반품 대상 상품 조회")
    @PostMapping(value = "/admin/order/claim/getOrderGreenJuiceClaimReturnsScheduleView")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : Integer.class") })
    public ApiResult<?> getOrderGreenJuiceClaimReturnsScheduleView(@RequestBody OrderClaimViewRequestDto orderClaimViewRequestDto) throws Exception {
        log.debug("주문 녹즙 반품 대상 상품 조회 parameter :: <{}>", orderClaimViewRequestDto);
        return claimRequestBiz.getOrderGreenJuiceClaimReturnsScheduleView(orderClaimViewRequestDto);
    }

    /**
     * 주문 녹즙 재배송 상품 도착예정일 조회
     *
     * @param orderClaimViewRequestDto
     * @return ApiResult<?>
     */
    @ApiOperation(value = "주문 녹즙 재배송 상품 도착예정일 조회")
    @PostMapping(value = "/admin/order/claim/getOrderGreenJuiceClaimExchangeScheduleView")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : Integer.class") })
    public ApiResult<?> getOrderGreenJuiceClaimExchangeScheduleView(@RequestBody OrderClaimViewRequestDto orderClaimViewRequestDto) throws Exception {
        log.debug("주문 녹즙 재배송 상품 도착예정일 조회 parameter :: <{}>", orderClaimViewRequestDto);
         return claimRequestBiz.getOrderGreenJuiceClaimExchangeScheduleView(orderClaimViewRequestDto);
    }

    /**
     * 주문클레임 재배송 상품 금액 조회
     *
     * @param orderClaimReShippingPaymentInfoRequestDto
     * @return ApiResult<?>
     */
    @ApiOperation(value = "주문클레임 재배송 상품 금액 조회")
    @PostMapping(value = "/admin/order/claim/getOrderClaimReShippingGoodsPriceInfo")
    @ApiResponses(value = {
    		@ApiResponse(code = 900, message = "response data : Integer.class") })
    public ApiResult<?> getOrderClaimReShippingGoodsPriceInfo(@RequestBody OrderClaimReShippingPaymentInfoRequestDto orderClaimReShippingPaymentInfoRequestDto) throws Exception {
    	log.debug("주문클레임 재배송 상품 금액 조회  START");
    	return claimRequestBiz.getOrderClaimReShippingGoodsPriceInfo(orderClaimReShippingPaymentInfoRequestDto);
    }

    /**
     * 주문클레임 클레임수량 변경 할 때
     *
     * @param orderClaimViewRequestDto
     * @return ApiResult<?>
     */
    @ApiOperation(value = "주문클레임 상품 클레임 수량 변경시 정보 조회")
    @PostMapping(value = "/admin/order/claim/getOrderClaimGoodsAmountInfo")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class") })
    public ApiResult<?> getOrderClaimGoodsAmountInfo(@RequestBody OrderClaimViewRequestDto orderClaimViewRequestDto) throws Exception {
    	log.debug("수량 조회 parameter :: <{}>", orderClaimViewRequestDto);
		return claimRequestBiz.getOrderClaimInfo(orderClaimViewRequestDto);
	}

    /**
     * 주문클레임 클레임상세팝업 > 클레임정보 > 사유(쇼핑몰 사유) 셀렉트박스 조회
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "주문클레임 클레임상세팝업 > 클레임정보 > 사유(쇼핑몰 사유) 셀렉트박스 조회")
    @PostMapping(value = "/admin/order/claim/getOrderClaimReasonList")
    @ApiResponses(value = {
    		@ApiResponse(code = 900, message = "response data : Integer.class") })
    public ApiResult<?> getOrderClaimReasonList(@RequestBody PolicyClaimMallRequestDto dto) throws Exception {
    	return claimRequestBiz.getOrderClaimReasonList(dto);
    }

    /**
     * 클레임정보 클레임번호에 의한 BOS클레임사유 조회
     *
     * @param orderClaimViewRequestDto
     * @return ApiResult<?>
     */
    @ApiOperation(value = "클레임정보 클레임번호에 의한 BOS클레임사유 조회")
    @PostMapping(value = "/admin/order/claim/getOrderClaimBosClaimReasonView")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : Integer.class") })
    public ApiResult<?> getOrderClaimBosClaimReasonView(@RequestBody OrderClaimViewRequestDto orderClaimViewRequestDto) throws Exception {
        log.debug("클레임정보 클레임번호에 의한 BOS클레임사유 조회 parameter :: <{}>", orderClaimViewRequestDto);
        return claimRequestBiz.getOrderClaimBosClaimReasonView(orderClaimViewRequestDto);
    }
}