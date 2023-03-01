package kr.co.pulmuone.mall.order.present;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import kr.co.pulmuone.mall.order.present.service.OrderPresentMallService;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.order.present.dto.IsShippingPossibilityRequestDto;
import kr.co.pulmuone.v1.order.present.dto.OrderPresentReceiveRequestDto;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자       :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20210715   	 	홍진영            최초작성
 * =======================================================================
 * </PRE>
 */
@RestController
public class OrderPresentController {

	@Autowired
	public OrderPresentMallService orderPresentMallService;

	@PostMapping(value = "/order/present/getOrderPresentConfirm")
	@ApiOperation(value = "선물확인", httpMethod = "POST")
	public ApiResult<?> getOrderPresentConfirm(String presentId) throws Exception {
		return orderPresentMallService.getOrderPresentConfirm(presentId);
	}

	@PostMapping(value = "/order/present/getOrderPresentAuth")
	@ApiOperation(value = "선물인증하기", httpMethod = "POST")
	public ApiResult<?> getOrderPresentAuth(@RequestParam(value = "presentId", required = true) String presentId,
			@RequestParam(value = "presentAuthCd", required = true) String presentAuthCd,
			@RequestParam(value = "captcha", required = false) String captcha) throws Exception {
		return orderPresentMallService.getOrderPresentAuth(presentId, presentAuthCd, captcha);
	}

	@PostMapping(value = "/order/present/isShippingPossibility")
	@ApiOperation(value = "선물하기 배송 가능 체크", httpMethod = "POST")
	public ApiResult<?> getOrderPresentAuth(IsShippingPossibilityRequestDto reqDto) throws Exception {
		return orderPresentMallService.isShippingPossibility(reqDto);
	}

	@PostMapping(value = "/order/present/reject")
	@ApiOperation(value = "선물 거절", httpMethod = "POST")
	public ApiResult<?> reject(@RequestParam(value = "presentId", required = true) String presentId,
			@RequestParam(value = "odOrderId", required = true) Long odOrderId) throws Exception {
		return orderPresentMallService.reject(presentId, odOrderId);
	}

	@PostMapping(value = "/order/present/receive")
	@ApiOperation(value = "선물 받기", httpMethod = "POST")
	public ApiResult<?> receive(OrderPresentReceiveRequestDto reqDto) throws Exception {
		return orderPresentMallService.receive(reqDto);
	}

	@PostMapping(value = "/order/present/reSendMessage")
	@ApiOperation(value = "메세지 재발송", httpMethod = "POST")
	public ApiResult<?> reSendMessage(@RequestParam(value = "odid", required = true) String odid) throws Exception {
		return orderPresentMallService.reSendMessage(odid);
	}
}
