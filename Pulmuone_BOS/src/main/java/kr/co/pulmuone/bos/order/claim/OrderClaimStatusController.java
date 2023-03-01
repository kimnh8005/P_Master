package kr.co.pulmuone.bos.order.claim;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.order.claim.dto.OrderChangeClaimStatusList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
* <PRE>
* Forbiz Korea
* 주문클레임 상태 변경 Controller
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* ----------------------------------------------------------------------
*  1.0  2021. 03. 15.     천혜현          최초작성
* =======================================================================
* </PRE>
*/
@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderClaimStatusController {

	/**
     * 미출 주문 상세리스트 > 일괄 취소완료 팝업 저장
     *
     * @param orderListRequestDto
     * @return
     */
	 @ApiOperation(value = "미출 주문 상세리스트 > 일괄 취소완료 팝업 저장")
	 @PostMapping(value = "/admin/order/putChangeClaimStatusCC")
	 @ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class") })
	 public ApiResult<?> putChangeClaimStatusCC(@RequestBody OrderChangeClaimStatusList orderChangeClaimStatusList) throws Exception {

		 // TODO :: 상태값 업데이트


		 return ApiResult.success();
	}

	/**
     * 미출 주문 상세리스트 > 일괄 재배송 팝업 저장
     *
     * @param orderListRequestDto
     * @return
     */
	 @ApiOperation(value = "미출 주문 상세리스트 > 일괄 재배송 팝업 저장")
	 @PostMapping(value = "/admin/order/putChangeClaimStatusEC")
	 @ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class") })
	 public ApiResult<?> putChangeClaimStatusEC(@RequestBody OrderChangeClaimStatusList orderChangeClaimStatusList) throws Exception {

		 // TODO :: 상태값 업데이트


		 return ApiResult.success();
	}
}