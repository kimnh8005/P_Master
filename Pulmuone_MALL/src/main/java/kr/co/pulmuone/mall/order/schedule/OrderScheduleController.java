package kr.co.pulmuone.mall.order.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleDeliverableListResponseDto;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleListResponseDto;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleUpdateRequestDto;
import kr.co.pulmuone.v1.order.schedule.service.mall.MallOrderScheduleBiz;

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
 *  1.0    2021. 02. 26.            석세동         최초작성
 * =======================================================================
 * </PRE>
 */
@RestController
public class OrderScheduleController {

    @Autowired
    private MallOrderScheduleBiz mallOrderScheduleBiz;


    /**
     * 주문 녹즙/잇슬림/베이비밀 스케줄 리스트 조회
     * @param odOrderDetlId(주문상세 PK)
     * @return OrderDetailScheduleListResponseDto(주문 상세 스케줄 리스트 Response Dto)
     * @throws Exception
     */
	@PostMapping(value = "/order/schedule/getOrderScheduleList")
	@ApiOperation(value = "주문 녹즙/잇슬림/베이비밀 스케줄 리스트 조회", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "odOrderDetlId", value = "주문상세 PK", required = true, dataType = "String")})
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = OrderDetailScheduleListResponseDto.class),
			@ApiResponse(code = 901, message = "" + "[NEED_LOGIN] NEED_LOGIN - 로그인 필요 \n")
	})
    public ApiResult<?> getOrderScheduleList(@RequestParam(value = "odOrderDetlId", required = true) String odOrderDetlId) throws Exception {
        // 로그인 체크
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
        }
        return mallOrderScheduleBiz.getOrderScheduleList(Long.parseLong(odOrderDetlId));
    }

	/**
	 * 주문 녹즙/잇슬림/베이비밀 스케줄 배송일자,수량 변경
	 * @param orderDetailScheduleUpdateRequestDto(주문 상세 스케줄 변경 Request Dto)
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/order/schedule/putScheduleArrivalDate")
	@ApiOperation(value = "주문 녹즙/잇슬림/베이비밀 스케줄 배송일자,수량 변경", httpMethod =  "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class"),
			@ApiResponse(code = 901, message = "" + "[NEED_LOGIN] NEED_LOGIN - 로그인 필요 \n")
	})
	public ApiResult<?> putScheduleArrivalDate(OrderDetailScheduleUpdateRequestDto orderDetailScheduleUpdateRequestDto) throws Exception {
        // 로그인 체크
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
        }
		return mallOrderScheduleBiz.putScheduleArrivalDate(orderDetailScheduleUpdateRequestDto);
	}

	/**
	 * 주문 녹즙/잇슬림/베이비밀 스케줄 배송요일 변경
	 * @param orderDetailScheduleUpdateRequestDto(주문 상세 스케줄 변경 Request Dto)
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/order/schedule/putScheduleArrivalDay")
	@ApiOperation(value = "주문 녹즙/잇슬림/베이비밀 스케줄 배송요일 변경", httpMethod =  "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class"),
			@ApiResponse(code = 901, message = "" + "[NEED_LOGIN] NEED_LOGIN - 로그인 필요 \n")
	})
	public ApiResult<?> putScheduleArrivalDay(OrderDetailScheduleUpdateRequestDto orderDetailScheduleUpdateRequestDto) throws Exception {
        // 로그인 체크
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
        }
		return mallOrderScheduleBiz.putScheduleArrivalDay(orderDetailScheduleUpdateRequestDto);
	}

	/**
     * 주문 녹즙/잇슬림/베이비밀 배송가능 스케줄 리스트 조회
     * @param odOrderDetlId(주문상세 PK)
     * @return OrderDetailScheduleDeliverableListResponseDto(주문상세 배송가능 스케줄 리스트 조회 Response Dto)
     * @throws Exception
     */
	@PostMapping(value = "/order/schedule/getOrderDeliverableScheduleList")
	@ApiOperation(value = "주문 녹즙/잇슬림/베이비밀 배송가능 스케줄 리스트 조회", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "odOrderDetlId", value = "주문상세 PK", required = true, dataType = "String")})
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = OrderDetailScheduleDeliverableListResponseDto.class),
			@ApiResponse(code = 901, message = "" + "[NEED_LOGIN] NEED_LOGIN - 로그인 필요 \n")
	})
    public ApiResult<?> getOrderDeliverableScheduleList(@RequestParam(value = "odOrderDetlId", required = true) String odOrderDetlId) throws Exception {
        // 로그인 체크
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
        }
        return mallOrderScheduleBiz.getOrderDeliverableScheduleList(Long.parseLong(odOrderDetlId));
    }
}