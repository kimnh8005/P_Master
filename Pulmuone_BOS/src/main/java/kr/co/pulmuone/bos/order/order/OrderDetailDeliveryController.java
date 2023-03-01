package kr.co.pulmuone.bos.order.order;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.order.order.dto.OrderDetailDateUpdateRequestDto;
import kr.co.pulmuone.v1.order.order.dto.OrderDetailDeliveryStatusRequestDto;
import kr.co.pulmuone.v1.order.order.dto.OrderDetailIfDateListRequestDto;
import kr.co.pulmuone.v1.order.order.dto.OrderDetailIfDateListResponseDto;
import kr.co.pulmuone.v1.order.order.service.OrderDetailDeliveryBiz;
import kr.co.pulmuone.v1.store.delivery.service.StoreDeliveryBiz;
import kr.co.pulmuone.v1.user.buyer.dto.CommonGetShippingAddressListRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.CommonGetShippingAddressListResponseDto;
import kr.co.pulmuone.v1.user.buyer.service.UserBuyerBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 주문상세 배송 Controller
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일				:  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 01. 08.       이규한          	  최초작성
 * =======================================================================
 * </PRE>
 */

@RestController
public class OrderDetailDeliveryController {

    @Autowired
    private OrderDetailDeliveryBiz orderDetailDeliveryBiz;		// 주문상세 배송 Biz

    @Autowired
    private UserBuyerBiz userBuyerBiz;		// 주문상세 배송 Biz

    @Autowired
    private StoreDeliveryBiz storeDeliveryBiz;

	/**
	 * 주문상세 - 배송상태 업데이트
	 *
	 * @param OrderDetailDeliveryStatusRequestDto
	 * @return ApiResult<?>
	 */
	@ApiOperation(value = "주문상세 배송상태 업데이트", httpMethod = "POST")
	@RequestMapping(value = "/admin/order/putOrderDetailDeliveryStatus")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class") })
    public ApiResult<?> putOrderDetailDeliveryStatus(@RequestBody List<OrderDetailDeliveryStatusRequestDto> orderDetailDeliveryStatusRequestDtoList) throws Exception {
		return ApiResult.success(orderDetailDeliveryBiz.putOrderDetailDeliveryStatus(orderDetailDeliveryStatusRequestDtoList));
    }

    /**
     * 주문상세 - 주문I/F 출고지시일 목록 조회
     * @param OrderDetailIfDateListRequestDto
     * @return ApiResult<?>
     * @throws Exception
     */
    @ApiOperation(value = "주문I/F 출고지시일 목록 조회", httpMethod = "POST")
    @PostMapping(value = "/admin/order/getIfDayList")
    @ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = OrderDetailIfDateListResponseDto.class) })
    public ApiResult<?> getIfDayList(@RequestBody OrderDetailIfDateListRequestDto orderDetailIfDateListRequestDto) throws Exception {
        return orderDetailDeliveryBiz.getIfDayList(orderDetailIfDateListRequestDto);
    }

    /**
     * 주문상세 - 주문I/F 정보 업데이트
     * @param OrderDetailDateUpdateRequestDto
     * @return ApiResult<?>
     * @throws Exception
     */
    @ApiOperation(value = "주문I/F 정보 업데이트", httpMethod = "POST")
    @PostMapping(value = "/admin/order/putIfDay")
    @ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class") })
    public ApiResult<?> putIfDay(OrderDetailDateUpdateRequestDto orderDetailDateUpdateRequestDto) throws Exception {
        return ApiResult.success(orderDetailDeliveryBiz.putIfDay(orderDetailDateUpdateRequestDto));
    }

    /**
     * @Desc 배송정보 상세조회
     * @param odShippingZoneId
     * @throws Exception
     * @return ApiResult<?>
     */
    @ApiOperation(value = "배송정보 배송지목록 조회", httpMethod = "POST")
    @PostMapping(value = "/admin/order/getShippingZoneList")
    @ApiResponses(value = {
    		@ApiResponse(code = 900, message = "response data List<>", response = CommonGetShippingAddressListResponseDto.class) })
    public ApiResult<?> getShippingZoneList(CommonGetShippingAddressListRequestDto dto) throws Exception {
    	return ApiResult.success(userBuyerBiz.getShippingAddressList(dto));
    }

    /**
     * @Desc get 배송 주소로 배송 가능 정보 조회
     * @throws Exception
     * @return ApiResult<?>
     */
    @ApiOperation(value = " get 배송 주소로 배송 가능 정보 조회", httpMethod = "POST")
    @PostMapping(value = "/admin/order/getShippingAddressPossibleDeliveryInfo")
    public ApiResult<?> getShippingAddressPossibleDeliveryInfo(String zipCode, String buildingCode) throws Exception {
    	return ApiResult.success(storeDeliveryBiz.getShippingAddressPossibleDeliveryInfo(zipCode, buildingCode));
    }

    /**
     * 주문생성 - 주문I/F 출고지시일 목록 조회
     * @param OrderDetailIfDateListRequestDto
     * @return ApiResult<?>
     * @throws Exception
     */
    @ApiOperation(value = "주문생성 - 주문I/F 출고지시일 목록 조회", httpMethod = "POST")
    @PostMapping(value = "/admin/order/getIfDayListForOrderCreate")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data List<>", response = OrderDetailIfDateListResponseDto.class) })
    public ApiResult<?> getIfDayListForOrderCreate(@RequestBody OrderDetailIfDateListRequestDto orderDetailIfDateListRequestDto) throws Exception {
        return orderDetailDeliveryBiz.getIfDayListForOrderCreate(orderDetailIfDateListRequestDto);
    }

}