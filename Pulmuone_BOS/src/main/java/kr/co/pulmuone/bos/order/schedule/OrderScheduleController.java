package kr.co.pulmuone.bos.order.schedule;

import java.time.LocalDate;

import kr.co.pulmuone.v1.order.schedule.service.mall.MallOrderScheduleBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleDeliverableListResponseDto;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleListResponseDto;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleUpdateDto;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleUpdateRequestDto;
import kr.co.pulmuone.v1.order.schedule.service.OrderScheduleBiz;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 녹즙/잇슬림/베이비밀 스케줄 관련 Controller
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 01. 20.            석세동         최초작성
 * =======================================================================
 * </PRE>
 */
@RestController
public class OrderScheduleController {

    @Autowired
    private OrderScheduleBiz orderScheduleBiz;

    @Autowired
    private MallOrderScheduleBiz mallOrderScheduleBiz;


    /**
     * 주문 녹즙/잇슬림/베이비밀 스케줄 리스트 조회
     * @param odOrderDetlId(주문상세 PK)
     * @return OrderDetailScheduleListResponseDto(주문 상세 스케줄 리스트 Response Dto)
     * @throws Exception
     */
	@PostMapping(value = "/admin/order/schedule/getOrderScheduleList")
	@ApiOperation(value = "주문 녹즙/잇슬림/베이비밀 스케줄 리스트 조회", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "odOrderDetlId", value = "주문상세 PK", required = true, dataType = "String")})
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = OrderDetailScheduleListResponseDto.class)
	})
    public ApiResult<?> getOrderScheduleList(@RequestParam(value = "odOrderDetlId", required = true) String odOrderDetlId) throws Exception {
        return orderScheduleBiz.getOrderScheduleList(Long.parseLong(odOrderDetlId));
    }

	/**
	 * 주문 녹즙/잇슬림/베이비밀 스케줄 배송일자,수량 변경
	 * @param orderDetailScheduleUpdateRequestDto(주문 상세 스케줄 변경 Request Dto)
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/order/schedule/putScheduleArrivalDate")
	@ApiOperation(value = "주문 녹즙/잇슬림/베이비밀 스케줄 배송일자,수량 변경", httpMethod =  "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class") })
	public ApiResult<?> putScheduleArrivalDate(OrderDetailScheduleUpdateRequestDto orderDetailScheduleUpdateRequestDto) throws Exception {
		orderDetailScheduleUpdateRequestDto.setScheduleUpdateList(BindUtil.convertJsonArrayToDtoList(orderDetailScheduleUpdateRequestDto.getScheduleUpdateListData() , OrderDetailScheduleUpdateDto.class));
		return orderScheduleBiz.putScheduleArrivalDate(orderDetailScheduleUpdateRequestDto);
	}

	/**
	 * 주문 녹즙/잇슬림/베이비밀 스케줄 배송요일 변경
	 * @param orderDetailScheduleUpdateRequestDto(주문 상세 스케줄 변경 Request Dto)
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/order/schedule/putScheduleArrivalDay")
	@ApiOperation(value = "주문 녹즙/잇슬림/베이비밀 스케줄 배송요일 변경", httpMethod =  "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class") })
	public ApiResult<?> putScheduleArrivalDay(OrderDetailScheduleUpdateRequestDto orderDetailScheduleUpdateRequestDto) throws Exception {
		return orderScheduleBiz.putScheduleArrivalDay(orderDetailScheduleUpdateRequestDto);
	}

	/**
	 * 주문 녹즙/잇슬림/베이비밀 스케줄 건너뛰기
	 * @param orderDetailScheduleUpdateRequestDto(주문 상세 스케줄 변경 Request Dto)
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/order/schedule/putScheduleSkip")
	@ApiOperation(value = "주문 녹즙/잇슬림/베이비밀 스케줄 건너뛰기", httpMethod =  "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class") })
	public ApiResult<?> putScheduleSkip(OrderDetailScheduleUpdateRequestDto orderDetailScheduleUpdateRequestDto) throws Exception {
		orderDetailScheduleUpdateRequestDto.setScheduleUpdateList(BindUtil.convertJsonArrayToDtoList(orderDetailScheduleUpdateRequestDto.getScheduleUpdateListData() , OrderDetailScheduleUpdateDto.class));
		return orderScheduleBiz.putScheduleSkip(orderDetailScheduleUpdateRequestDto);
	}

	/**
     * 주문 녹즙/잇슬림/베이비밀 배송가능 스케줄 리스트 조회
     * @param odOrderDetlId(주문상세 PK)
     * @return OrderDetailScheduleDeliverableListResponseDto(주문상세 배송가능 스케줄 리스트 조회 Response Dto)
     * @throws Exception
     */
	@PostMapping(value = "/admin/order/schedule/getOrderDeliverableScheduleList")
	@ApiOperation(value = "주문 녹즙/잇슬림/베이비밀 배송가능 스케줄 리스트 조회", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "odOrderDetlId", value = "주문상세 PK", required = true, dataType = "String")})
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = OrderDetailScheduleDeliverableListResponseDto.class)
	})
    public ApiResult<?> getOrderDeliverableScheduleList(@RequestParam(value = "odOrderDetlId", required = true) String odOrderDetlId) throws Exception {
        return mallOrderScheduleBiz.getOrderDeliverableScheduleList(Long.parseLong(odOrderDetlId));
    }

	/**
	 * 주문 녹즙/잇슬림/베이비밀 배송가능 스케줄 주소변경 리스트 조회
	 * @param odOrderDetlId(주문상세 PK)
	 * @return OrderDetailScheduleDeliverableListResponseDto(주문상세 배송가능 스케줄 리스트 조회 Response Dto)
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/order/schedule/getOrderDeliverableScheduleAddressList")
	@ApiOperation(value = "주문 녹즙/잇슬림/베이비밀 배송가능 스케줄 리스트 조회", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "odOrderDetlId", value = "주문상세 PK", required = true, dataType = "String")})
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = OrderDetailScheduleDeliverableListResponseDto.class)
	})
	public ApiResult<?> getOrderDeliverableScheduleAddressList(@RequestParam(value = "odOrderDetlId", required = true) String odOrderDetlId) throws Exception {
		return orderScheduleBiz.getOrderDeliverableScheduleList(Long.parseLong(odOrderDetlId));
	}

	/**
	 * @Desc 주문 녹즙/잇슬림/베이비밀 배송가능 스케줄 > 주문I/F일자, 출고예정일자, 도착예정일자 업데이트
	 * @param orderDetlVo
	 * @throws Exception
	 * @return ApiResult<?>
	 */
	@PostMapping(value = "/admin/order/schedule/putOrderArrivalScheduledDate")
	@ApiOperation(value = "주문 녹즙/잇슬림/베이비밀 배송가능 스케줄 > 주문I/F일자, 출고예정일자, 도착예정일자 업데이트", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class")
	})
    public ApiResult<?> putOrderArrivalScheduledDate(@RequestParam(value = "odOrderDetlId", required = true) long odOrderDetlId,
    		@RequestParam(value = "ilGoodsId", required = true) long ilGoodsId,
    		@RequestParam(value = "deliveryDt", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate deliveryDt) throws Exception {
        return orderScheduleBiz.putOrderArrivalScheduledDate(odOrderDetlId, ilGoodsId, deliveryDt);
    }


}