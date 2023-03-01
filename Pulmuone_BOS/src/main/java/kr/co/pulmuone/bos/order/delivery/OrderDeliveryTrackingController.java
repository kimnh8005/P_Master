package kr.co.pulmuone.bos.order.delivery;


import kr.co.pulmuone.v1.comm.enums.OrderClaimEnums;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.order.delivery.dto.OrderDeliveryTrackingRequestDto;
import kr.co.pulmuone.v1.order.delivery.dto.OrderDeliveryTrackingResponseDto;
import kr.co.pulmuone.v1.order.delivery.service.OrderDeliveryTrackingBiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
* <PRE>
* Forbiz Korea
* 배송추적 Controller
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0  2021. 01. 18.     강상국          최초작성
* =======================================================================
* </PRE>
*/
@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderDeliveryTrackingController {

	@Autowired
    private final OrderDeliveryTrackingBiz orderDeliveryTrackingBiz;

    /**
     * 배송추적
     * js 파일에서 함수 호출 할 때 아래와 같이 호출 하시면 됩니다.
     *
     * function fnTrackingSearch() {
     *		let form = $("#searchForm").formSerialize(true);
     *		form['logisticsCd'] = '90';				//택배사코드 롯데 : 90, CJ대한통운 : 93
     *		form['trackingNo'] = '306641893246';	//운송장번호
     *
     *		fnAjax({
     *			url     : "/admin/order/delivery/getDeliveryTrackingList",
     *			params  : form,
     *			success : function( data ){
     *				var trackingList = data.tracking;	// OrderDeliveryTrackingResponseDto.java 변수명을 확인 하시면 됩니다.
     *				}
     *			},
     *			isAction : "select"
     *		})
     *	};
     * controller.js 에서 위에 함수 같이 호출 하시면 됩니다.
     * @param GoodsDisExcelUploadRequestDto
     * @return ApiResult<?>
     */
    @ApiOperation(value = "배송추적조회")
    @PostMapping(value = "/admin/order/delivery/getDeliveryTrackingList")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class") })
	public ApiResult<?> getDeliveryTrackingList(OrderDeliveryTrackingRequestDto orderDeliveryTrackingRequestDto) throws Exception {

		return orderDeliveryTrackingBiz.getDeliveryTrackingList(orderDeliveryTrackingRequestDto);
	}

	/**
	 * 반품 회수 배송추적
	 * @param orderDeliveryTrackingRequestDto
	 * @return ApiResult<?>
	 */
	@ApiOperation(value = "반품 회수 배송추적 조회")
	@PostMapping(value = "/admin/order/delivery/getOrderClaimReturnDeliveryTrackingList")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class") })
	public ApiResult<?> getOrderClaimReturnDeliveryTrackingList(OrderDeliveryTrackingRequestDto orderDeliveryTrackingRequestDto) throws Exception {

		// 반품 회수 배송추적여부
		orderDeliveryTrackingRequestDto.setReturnsYn(OrderClaimEnums.AllTypeYn.ALL_TYPE_Y.getCode());

		return orderDeliveryTrackingBiz.getDeliveryTrackingList(orderDeliveryTrackingRequestDto);
	}
}