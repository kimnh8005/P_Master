package kr.co.pulmuone.bos.order.claim;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.order.claim.dto.OrderCSRefundRegisterRequestDto;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimRegisterRequestDto;
import kr.co.pulmuone.v1.order.claim.service.ClaimCompleteProcessBiz;
import kr.co.pulmuone.v1.order.claim.service.ClaimProcessBiz;
import kr.co.pulmuone.v1.order.create.dto.OrderClaimCardPayRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
* <PRE>
* Forbiz Korea
* 주문클레임 신청 Controller
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
public class OrderClaimProcessController {

	@Autowired
    private final ClaimProcessBiz claimProcessBiz;

	/**
	 * @param orderClaimRegisterRequestDto
	 * @return
	 * @throws Exception
	 */
    @ApiOperation(value = "주문클레임 생성")
    @PostMapping(value = "/admin/order/claim/addOrderClaimRegister")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class") })
	public ApiResult<?> addOrderClaimRegister(@RequestBody OrderClaimRegisterRequestDto orderClaimRegisterRequestDto) throws Exception {
    	log.debug("------------------------------------------------------------");
    	log.debug("orderClaimRegisterRequestDto :: <{}>", orderClaimRegisterRequestDto.toString());
    	log.debug("------------------------------------------------------------");
		return claimProcessBiz.addOrderClaim(orderClaimRegisterRequestDto);
	}

	@ApiOperation(value = "클레임 비인증 신용카드 결제")
	@PostMapping(value = "/admin/order/claim/addCardPayOrderCreate")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class") })
	public ApiResult<?> addCardPayOrderCreate(@RequestBody OrderClaimCardPayRequestDto orderClaimCardPayRequestDto) throws Exception {
		return claimProcessBiz.addCardPayOrderCreate(orderClaimCardPayRequestDto);
	}

	@ApiOperation(value = "클레임 무통장입금")
	@PostMapping(value = "/admin/order/claim/addBankBookOrderCreate")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class") })
	public ApiResult<?> addBankBookOrderCreate(@RequestBody OrderClaimCardPayRequestDto orderClaimCardPayRequestDto) throws Exception {
		return claimProcessBiz.addBankBookOrderCreate(orderClaimCardPayRequestDto);
	}

	/**
	 * @param orderClaimRegisterRequestDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "추가결제 후 주문클레임 생성")
	@PostMapping(value = "/admin/order/claim/addAddPaymentAfterOrderClaimRegister")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class") })
	public ApiResult<?> addAddPaymentAfterOrderClaimRegister(@RequestBody OrderClaimRegisterRequestDto orderClaimRegisterRequestDto) throws Exception {
		log.debug("------------------------------------------------------------");
		log.debug("orderClaimRegisterRequestDto :: <{}>", orderClaimRegisterRequestDto.toString());
		log.debug("------------------------------------------------------------");
		return claimProcessBiz.addAddPaymentAfterOrderClaimRegister(orderClaimRegisterRequestDto);
	}

	/**
	 * 주문 클레임 CS환불승인상태 변경
	 * @param orderCSRefundRegisterRequest
	 * @throws Exception
	 */
	@ApiOperation(value = "주문 클레임 CS환불승인상태 변경")
	@PostMapping(value = "/admin/order/claim/putOrderClaimCsRefundApproveCd")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class") })
	public ApiResult<?> putOrderClaimCsRefundApproveCd(OrderCSRefundRegisterRequestDto orderCSRefundRegisterRequest) throws Exception {
		return claimProcessBiz.putOrderClaimCsRefundApproveCd(orderCSRefundRegisterRequest);
	}

	/**
	 * @param orderClaimRegisterRequestDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "녹즙 주문클레임 생성")
	@PostMapping(value = "/admin/order/claim/addAddPaymentAfterOrderClaimGreenJuiceRegister")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class") })
	public ApiResult<?> addAddPaymentAfterOrderClaimGreenJuiceRegister(@RequestBody OrderClaimRegisterRequestDto orderClaimRegisterRequestDto) throws Exception {
		log.debug("------------------------------------------------------------");
		log.debug("addAddPaymentAfterOrderClaimGreenJuiceRegister :: <{}>", orderClaimRegisterRequestDto.toString());
		log.debug("------------------------------------------------------------");
		return claimProcessBiz.addOrderClaimGreenJuice(orderClaimRegisterRequestDto);
	}

	/**
	 * 주문 상세 클레임 상품 정보 > BOS 클레임 사유 변경
	 * @param orderClaimRegisterRequestDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "주문 상세 클레임 상품 정보 > BOS 클레임 사유 변경", httpMethod = "POST")
	@PostMapping(value = "/admin/order/claim/putOrderClaimDetlBosClaimReason")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class") })
	public ApiResult<?> putOrderClaimDetlBosClaimReason(@RequestBody OrderClaimRegisterRequestDto orderClaimRegisterRequestDto) throws Exception {
		return claimProcessBiz.putOrderClaimDetlBosClaimReason(orderClaimRegisterRequestDto);
	}

	/**
	 * CS 환불 정보 등록
	 * @param orderCSRefundRegisterRequestDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "CS 환불 정보 등록", httpMethod = "POST")
	@PostMapping(value = "/admin/order/claim/addOrderCSRefundRegister")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class") })
	public ApiResult<?> addOrderCSRefundRegister(@RequestBody OrderCSRefundRegisterRequestDto orderCSRefundRegisterRequestDto) throws Exception {
		return claimProcessBiz.addOrderCSRefundRegister(orderCSRefundRegisterRequestDto);
	}
}