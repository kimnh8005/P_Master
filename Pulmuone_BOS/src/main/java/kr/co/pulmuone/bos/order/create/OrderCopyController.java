package kr.co.pulmuone.bos.order.create;

import kr.co.pulmuone.v1.order.create.dto.OrderCopyValidationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.order.create.dto.OrderCopyDetailInfoRequestDto;
import kr.co.pulmuone.v1.order.create.dto.OrderCopySaveRequestDto;
import kr.co.pulmuone.v1.order.create.service.OrderCopyBiz;
import kr.co.pulmuone.v1.order.order.dto.OrderDetlCopyShippingZoneDto;
import kr.co.pulmuone.v1.order.order.service.OrderDetailBiz;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 복사 Controller
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일				:  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 02. 23.       이규한          	  최초작성
 * =======================================================================
 * </PRE>
 */

@RestController
@RequiredArgsConstructor
public class OrderCopyController {

	@Autowired
    private final OrderCopyBiz orderCopyBiz;

	@Autowired
	private final OrderDetailBiz orderDetailBiz;
    /**
     * 주문복사 주문 상세내역 조회
     *
     * @param orderCopyDetailInfoRequestDto
     * @return ApiResult<?>
     */
    @ApiOperation(value = "주문복사 주문 상세내역 조회")
    @PostMapping(value = "/admin/order/copy/getOrderCopyDetailInfo")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : OrderCopyDetailInfoResponseDto.class") })
	public ApiResult<?> getOrderCopyDetailInfo(OrderCopyDetailInfoRequestDto orderCopyDetailInfoRequestDto) throws Exception {
    	return orderCopyBiz.getOrderCopyDetailInfo(orderCopyDetailInfoRequestDto);
	}

    /**
     * 주문복사 주문 상세내역 조회
     *
     * @param orderCopyDetailInfoRequestDto
     * @return ApiResult<?>
     */
    @ApiOperation(value = "주문복사 주문 상세내역 주문수량 변경 조회")
    @PostMapping(value = "/admin/order/copy/getOrderCopyCntChangeInfo")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : OrderCopyDetailInfoResponseDto.class") })
	public ApiResult<?> getOrderCopyCntChangeInfo(@RequestBody OrderCopyDetailInfoRequestDto orderCopyDetailInfoRequestDto) throws Exception {
    	return orderCopyBiz.getOrderCopyCntChangeInfo(orderCopyDetailInfoRequestDto);
	}

	/**
     * 주문 상세 배송정보 리스트 조회
     * @param odOrderId
     * @return
     * @throws Exception
     */
	@PostMapping(value = "/admin/order/copy/getOrderDetailCopyShippingZoneList")
	@ApiOperation(value = "주문 복사 배송정보 리스트 조회 (수취정보)", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "odOrderId", value = "주문 PK", required = true, dataType = "String")})
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = OrderDetlCopyShippingZoneDto.class)
	})
    public ApiResult<?> getOrderDetailShippingZoneList(@RequestParam(value = "odOrderId", required = true) String odOrderId) throws Exception {
        return ApiResult.success(orderDetailBiz.getOrderDetailCopyShippingZoneList(odOrderId));
    }

    /**
     * 주문복사 주문 상세내역 복사
     *
     * @param orderCopySaveRequestDto
     * @return ApiResult<?>
     */
    @ApiOperation(value = "주문복사 주문 상세내역 복사")
    @PostMapping(value = "/admin/order/copy/saveOrderCopyDetailInfo")
    @ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class") })
	public ApiResult<?> saveOrderCopyDetailInfo(@RequestBody OrderCopySaveRequestDto orderCopySaveRequestDto) throws Exception {
        UserVo userVo = SessionUtil.getBosUserVO();
        long userId = Long.parseLong(userVo.getUserId());
    	return orderCopyBiz.addOrderCopy(orderCopySaveRequestDto, userId);
	}

    /**
     * 주문복사 비인증 결제
     *
     * @param orderCopySaveRequestDto
     * @return ApiResult<?>
     */
    @ApiOperation(value = "주문복사 비인증 결제")
    @PostMapping(value = "/admin/order/copy/addNonCardPayment")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : OrderCopyDetailInfoResponseDto.class") })
	public ApiResult<?> addNonCardPayment(@RequestBody OrderCopySaveRequestDto orderCopySaveRequestDto) throws Exception {
        UserVo userVo = SessionUtil.getBosUserVO();
        long userId = Long.parseLong(userVo.getUserId());
    	return orderCopyBiz.addNonCardPayment(orderCopySaveRequestDto, userId);
	}

	/**
	 * 주문상품 정보 조회
	 *
	 * @param odOrderDetlIdList
	 * @return ApiResult<?>
	 */
	@ApiOperation(value = "주문상품 정보 조회")
	@PostMapping(value = "/admin/order/copy/getOrderDetlGoodsSaleStatus")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : GoodsVo.class") })
	public ApiResult<?> getOrderDetlGoodsSaleStatus(@RequestParam(value = "orderDetlId[]" , required = true) List<Long> odOrderDetlIdList) throws Exception {
		return orderCopyBiz.getOrderDetlGoodsSaleStatus(odOrderDetlIdList);
	}

	/**
	 * 주문복사 유효성체크
	 *
	 * @param orderCopyValidationDtoList
	 * @return ApiResult<?>
	 */
	@ApiOperation(value = "주문복사 유효성체크")
	@PostMapping(value = "/admin/order/copy/checkOrderCopyValidation")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : GoodsVo.class") })
	public ApiResult<?> checkOrderCopyValidation(@RequestBody List<OrderCopyValidationDto> orderCopyValidationDtoList) throws Exception {
		return orderCopyBiz.checkOrderCopyValidation(orderCopyValidationDtoList);
	}

	/**
	 * 주문상품 출고처 조회
	 *
	 * @param odOrderDetlIdList
	 * @return ApiResult<?>
	 */
	@ApiOperation(value = "주문상품 출고처 조회")
	@PostMapping(value = "/admin/order/copy/getOrderDetlGoodsWarehouseCode")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : GoodsVo.class") })
	public ApiResult<?> getOrderDetlGoodsWarehouseCode(@RequestParam(value = "orderDetlId[]" , required = true) List<Long> odOrderDetlIdList) throws Exception {
		return orderCopyBiz.getOrderDetlGoodsWarehouseCode(odOrderDetlIdList);
	}
}