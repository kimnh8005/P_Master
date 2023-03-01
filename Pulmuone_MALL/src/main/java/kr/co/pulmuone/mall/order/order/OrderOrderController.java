package kr.co.pulmuone.mall.order.order;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.mall.order.order.service.OrderOrderMallService;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.shopping.cart.dto.CartSummaryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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
 *  1.0    20200911   	 	김만환            최초작성
 * =======================================================================
 * </PRE>
 */
@RestController
public class OrderOrderController {

	@Autowired
	public OrderOrderMallService orderOrderMallService;

	/**
	 * 주문 완료 가상계좌 정보 조회
	 *
	 * @param	odid
	 * @throws	Exception
	 */
	@PostMapping(value = "/order/order/getVirtualAccount")
	@ApiOperation(value = "주문 완료 가상계좌 정보 조회", httpMethod = "POST")
	@ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = CartSummaryDto.class) ,
							@ApiResponse(code = 901, message = "" + "NOT_SAME_USER - 본인 주문 아님 ")})
	public ApiResult<?> getVirtualAccount(String odid) throws Exception {
		return orderOrderMallService.getVirtualAccount(odid);
	}

	/**
	 * 주문 완료 결제 정보 조회
	 *
	 * @param odid
	 * @throws Exception
	 */
	@PostMapping(value = "/order/order/getPaymentInfo")
	@ApiOperation(value = "주문 완료 결제 정보 조회", httpMethod = "POST")
	@ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = CartSummaryDto.class) ,
			@ApiResponse(code = 901, message = "" + "NOT_SAME_USER - 본인 주문 아님 ")})
	public ApiResult<?> getPaymentInfo(String odid) throws Exception {
		return orderOrderMallService.getPaymentInfo(odid);
	}


}
