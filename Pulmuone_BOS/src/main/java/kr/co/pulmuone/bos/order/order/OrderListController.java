package kr.co.pulmuone.bos.order.order;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.base.dto.vo.GetCodeListResultVo;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.enums.OrderEnums.OrderExcelNm;
import kr.co.pulmuone.v1.comm.enums.OrderEnums.OrderStatus;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.order.order.dto.OrderListRequestDto;
import kr.co.pulmuone.v1.order.order.dto.OrderListResponseDto;
import kr.co.pulmuone.v1.order.order.service.OrderListBiz;
import kr.co.pulmuone.v1.system.log.dto.ExcelDownloadAsyncRequestDto;
import kr.co.pulmuone.v1.system.log.dto.ExcelDownloadAsyncResponseDto;
import kr.co.pulmuone.v1.system.log.service.SystemLogBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * <PRE>
 * Forbiz Korea
 * 주문리스트 관련 Controller
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 14.            이명수         최초작성
 *  1.1    2020. 12. 15.            석세동         수정
 * =======================================================================
 * </PRE>
 */
@RestController
public class OrderListController {


    @Autowired
    private OrderListBiz orderListBiz;

	@Autowired
	private SystemLogBiz systemLogBiz;

    @Autowired
    private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

    /**
     * 주문 리스트 조회
     *
     * @param orderListRequestDto
     * @return ApiResult<?>
     */
	@PostMapping(value = "/admin/order/getOrderList")
	@ApiOperation(value = "주문 리스트 조회", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = OrderListResponseDto.class)
	})
    public ApiResult<?> getOrderList(HttpServletRequest request, OrderListRequestDto orderListRequestDto) throws Exception {

        return orderListBiz.getOrderList(BindUtil.bindDto(request, OrderListRequestDto.class));
    }

    /**
     * 주문 리스트 엑셀다운로드
     *
     * @param orderListRequestDto
     * @return ApiResult<?>
     */
    @ApiOperation(value = "주문 리스트 엑셀다운로드")
    @PostMapping(value = "/admin/order/getOrderExcelList")
    public ModelAndView getOrderExcelList(@RequestBody OrderListRequestDto orderListRequestDto) throws Exception {

        ExcelDownloadDto excelDownloadDto = orderListBiz.getOrderExcelList(orderListRequestDto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }

	/**
	 * 매장 주문 리스트 엑셀다운로드
	 *
	 * @param orderListRequestDto
	 * @return ModelAndView
	 */
	@ApiOperation(value = "매장 주문  리스트 엑셀다운로드")
	@PostMapping(value = "/admin/order/getShopOrderExcelList")
	public ModelAndView getShopOrderExcelList(@RequestBody OrderListRequestDto orderListRequestDto) {

		orderListRequestDto.setShopStockStatus("shopStockStatus");

		ExcelDownloadDto excelDownloadDto = orderListBiz.getShopOrderExcelList(orderListRequestDto);

		ModelAndView modelAndView = new ModelAndView(excelDownloadView);
		modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

		return modelAndView;
	}

    /**
     * 주문 상세 리스트 조회
     *
     * @param orderListRequestDto
     * @return ApiResult<?>
     */
	@PostMapping(value = "/admin/order/getOrderDetailList")
	@ApiOperation(value = "주문상세 리스트 조회", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = OrderListResponseDto.class)
	})
    public ApiResult<?> getOrderDetailList(HttpServletRequest request, OrderListRequestDto orderListRequestDto) throws Exception {

		orderListRequestDto = BindUtil.bindDto(request, OrderListRequestDto.class);
		orderListRequestDto.setOrderByType(OrderEnums.OrderOrderByType.ORDER_BY_ORDER.getCode());
        return orderListBiz.getOrderDetailList(orderListRequestDto);
    }

	/**
     * 주문 상세 리스트 엑셀다운로드
     *
     * @param orderListRequestDto
     * @return ModelAndView
     */
    @ApiOperation(value = "주문 상세 리스트 엑셀다운로드")
    @PostMapping(value = "/admin/order/getOrderDetailExcelList")
    public ModelAndView getOrderDetailExcelList(@RequestBody OrderListRequestDto orderListRequestDto) {

    	orderListRequestDto.setPsExcelNm(OrderExcelNm.ODD.getCodeName());
		orderListRequestDto.setOrderByType(OrderEnums.OrderOrderByType.ORDER_BY_ORDER.getCode());

        ExcelDownloadDto excelDownloadDto = orderListBiz.getOrderDetailExcelList(orderListRequestDto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }

	@ApiOperation(value = "주문 상세 리스트 - 엑셀 생성 실행")
	@PostMapping(value = "/admin/order/getOrderDetailExcelListMake")
	public ApiResult<?> getOrderDetailExcelListMake(@RequestBody OrderListRequestDto orderListRequestDto) throws Exception {
		orderListRequestDto.setOrderByType(OrderEnums.OrderOrderByType.ORDER_BY_ORDER.getCode());

		return ApiResult.success(orderListBiz.getOrderDetailExcelListMake(orderListRequestDto));
	}

	@ApiOperation(value = "주문 상세 리스트 - 엑셀 생성 여부 조회")
	@PostMapping(value = "/admin/order/getExcelDownloadAsyncUseYn")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = ExcelDownloadAsyncResponseDto.class),
	})
	public ApiResult<?> getExcelDownloadAsyncUseYn(@RequestBody ExcelDownloadAsyncRequestDto requestDto) {
		return ApiResult.success(systemLogBiz.getExcelDownloadAsyncUseYn(requestDto.getStExcelDownloadAsyncId()));
	}

    /**
     * 결제완료 주문 상세 리스트 조회
     *
     * @param request
     * @return ApiResult<?>
     */
	@PostMapping(value = "/admin/order/getPayCompleteOrderDetailList")
	@ApiOperation(value = "결제완료 주문상세 리스트 조회", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = OrderListResponseDto.class)
	})
    public ApiResult<?> getPayCompleteOrderDetailList(HttpServletRequest request) throws Exception {

		OrderListRequestDto orderListRequestDto = BindUtil.bindDto(request, OrderListRequestDto.class);

		orderListRequestDto.setOrderState(OrderStatus.INCOM_COMPLETE.getCode());
		orderListRequestDto.setOrderByType(OrderEnums.OrderOrderByType.ORDER_BY_ORDER.getCode());
		orderListRequestDto.setOrderCntYn("Y");
        return orderListBiz.getOrderDetailList(orderListRequestDto);
    }

	/**
     * 결제완료 주문 상세 리스트 엑셀다운로드
     *
     * @param orderListRequestDto
     * @return ModelAndView
     */
    @ApiOperation(value = "결제완료 주문 상세 리스트 엑셀다운로드")
    @PostMapping(value = "/admin/order/getPayCompleteOrderDetailExcelList")
    public ModelAndView getPayCompleteOrderDetailExcelList(@RequestBody OrderListRequestDto orderListRequestDto) throws Exception {

		orderListRequestDto.setOrderState(OrderStatus.INCOM_COMPLETE.getCode());
		orderListRequestDto.setPsExcelNm(OrderExcelNm.IC.getCodeName());
		orderListRequestDto.setOrderByType(OrderEnums.OrderOrderByType.ORDER_BY_ORDER.getCode());
		orderListRequestDto.setOrderCntYn("Y");
        ExcelDownloadDto excelDownloadDto = orderListBiz.getOrderDetailExcelList(orderListRequestDto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }

	/**
	 * 취소요청 주문 상세 리스트 조회
	 *
	 * @param request
	 * @return ApiResult<?>
	 */
	@PostMapping(value = "/admin/order/getCancelReqOrderDetailList")
	@ApiOperation(value = "취소요청 주문상세 리스트 조회", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = OrderListResponseDto.class)
	})
	public ApiResult<?> getCancelReqOrderDetailList(HttpServletRequest request) throws Exception {

		OrderListRequestDto orderListRequestDto = BindUtil.bindDto(request, OrderListRequestDto.class);

		orderListRequestDto.setClaimState(OrderStatus.CANCEL_APPLY.getCode());
		orderListRequestDto.setOrderByType(OrderEnums.OrderOrderByType.ORDER_BY_CLAIM.getCode());

		return orderListBiz.getOrderDetailList(orderListRequestDto);
	}

	/**
     * 취소요청 주문 상세 리스트 엑셀다운로드
     *
     * @param orderListRequestDto
     * @return ModelAndView
     */
    @ApiOperation(value = "취소요청 주문 상세 리스트 엑셀다운로드")
    @PostMapping(value = "/admin/order/getCancelReqOrderDetailExcelList")
    public ModelAndView getCancelReqOrderDetailExcelList(@RequestBody OrderListRequestDto orderListRequestDto) throws Exception {

		orderListRequestDto.setClaimState(OrderStatus.CANCEL_APPLY.getCode());
		orderListRequestDto.setPsExcelNm(OrderExcelNm.CA.getCodeName());
		orderListRequestDto.setOrderByType(OrderEnums.OrderOrderByType.ORDER_BY_ORDER.getCode());

        ExcelDownloadDto excelDownloadDto = orderListBiz.getOrderDetailExcelList(orderListRequestDto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }

	/**
	 * 출고예정 주문 상세 리스트 조회
	 *
	 * @param request
	 * @return ApiResult<?>
	 */
	@PostMapping(value = "/admin/order/getDeliveryReadyOrderDetailList")
	@ApiOperation(value = "출고예정 주문상세 리스트 조회", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = OrderListResponseDto.class)
	})
	public ApiResult<?> getDeliveryReadyOrderDetailList(HttpServletRequest request) throws Exception {

		OrderListRequestDto orderListRequestDto = BindUtil.bindDto(request, OrderListRequestDto.class);

		//orderListRequestDto.setOrderState(OrderStatus.DELIVERY_READY.getCode());
		orderListRequestDto.setOrderByType(OrderEnums.OrderOrderByType.ORDER_BY_ORDER.getCode());
		//orderListRequestDto.setOrderCntYn("Y");
		return orderListBiz.getOrderDetailList(orderListRequestDto);
	}

	/**
     * 출고예정 주문 상세 리스트 엑셀다운로드
     *
     * @param orderListRequestDto
     * @return ModelAndView
     */
    @ApiOperation(value = "출고예정 주문 상세 리스트 엑셀다운로드")
    @PostMapping(value = "/admin/order/getDeliveryReadyOrderDetailExcelList")
    public ModelAndView getDeliveryReadyOrderDetailExcelList(@RequestBody OrderListRequestDto orderListRequestDto) throws Exception {

		orderListRequestDto.setOrderState(OrderStatus.DELIVERY_READY.getCode());
		orderListRequestDto.setPsExcelNm(OrderExcelNm.DR.getCodeName());
		orderListRequestDto.setOrderByType(OrderEnums.OrderOrderByType.ORDER_BY_ORDER.getCode());

        ExcelDownloadDto excelDownloadDto = orderListBiz.getOrderDetailExcelList(orderListRequestDto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }

	/**
	 * 배송중 주문 상세 리스트 조회
	 *
	 * @param request
	 * @return ApiResult<?>
	 */
	@PostMapping(value = "/admin/order/getDeliveryIngOrderDetailList")
	@ApiOperation(value = "배송중 주문상세 리스트 조회", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = OrderListResponseDto.class)
	})
	public ApiResult<?> getDeliveryIngOrderDetailList(HttpServletRequest request) throws Exception {

		OrderListRequestDto orderListRequestDto = BindUtil.bindDto(request, OrderListRequestDto.class);

		orderListRequestDto.setOrderState(OrderStatus.DELIVERY_ING.getCode());
		orderListRequestDto.setOrderByType(OrderEnums.OrderOrderByType.ORDER_BY_ORDER.getCode());
		orderListRequestDto.setOrderCntYn("Y");
		return orderListBiz.getOrderDetailList(orderListRequestDto);
	}

	/**
     * 배송중 주문 상세 리스트 엑셀다운로드
     *
     * @param orderListRequestDto
     * @return ModelAndView
     */
    @ApiOperation(value = "배송중 주문 상세 리스트 엑셀다운로드")
    @PostMapping(value = "/admin/order/getDeliveryIngOrderDetailExcelList")
    public ModelAndView getDeliveryIngOrderDetailExcelList(@RequestBody OrderListRequestDto orderListRequestDto) throws Exception {

		orderListRequestDto.setOrderState(OrderStatus.DELIVERY_ING.getCode());
		orderListRequestDto.setPsExcelNm(OrderExcelNm.DI.getCodeName());
		orderListRequestDto.setOrderByType(OrderEnums.OrderOrderByType.ORDER_BY_ORDER.getCode());

        ExcelDownloadDto excelDownloadDto = orderListBiz.getOrderDetailExcelList(orderListRequestDto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }

	/**
	 * 미출 주문 상세 리스트 조회
	 *
	 * @param request
	 * @return ApiResult<?>
	 */
	@PostMapping(value = "/admin/order/getUnreleasedOrderDetailList")
	@ApiOperation(value = "미출 주문상세 리스트 조회", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = OrderListResponseDto.class)
	})
	public ApiResult<?> getUnreleasedOrderDetailList(HttpServletRequest request, OrderListRequestDto orderListRequestDto) throws Exception {

		orderListRequestDto = BindUtil.bindDto(request, OrderListRequestDto.class);
		orderListRequestDto.setOrderByType(OrderEnums.OrderOrderByType.ORDER_BY_UNDELIVERED.getCode());

		return orderListBiz.getUnreleasedOrderDetailList(orderListRequestDto);
	}

	/**
     * 미출 주문 상세 리스트 엑셀다운로드
     *
     * @param orderListRequestDto
     * @return ModelAndView
     */
    @ApiOperation(value = "미출 주문 상세 리스트 엑셀다운로드")
    @PostMapping(value = "/admin/order/getUnreleasedOrderDetailExcelList")
    public ModelAndView getUnreleasedOrderDetailExcelList(@RequestBody OrderListRequestDto orderListRequestDto) throws Exception {

		orderListRequestDto.setPsExcelNm(OrderExcelNm.MI.getCodeName());
		/* 조회 데이터와 엑셀 다운로드 데이터 Order by값이 달라서 출력되는 순서의 차이가 있어서 주석처리 */
		// orderListRequestDto.setOrderByType(OrderEnums.OrderOrderByType.ORDER_BY_CLAIM.getCode());

        ExcelDownloadDto excelDownloadDto = orderListBiz.getUnreleasedOrderDetailExcelList(orderListRequestDto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);
        return modelAndView;
    }

    /**
	 * 반품 사유 코드 목록 조회
	 *
	 * @param
	 * @return ApiResult<?>
	 */
    @GetMapping(value = "/admin/order/getReturnReasonList")
	@ApiOperation(value = "반품 사유 코드 목록 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = GetCodeListResultVo.class)
	})
	public ApiResult<?> getReturnReasonList() throws Exception {
		return orderListBiz.getReturnReasonList();
	}

	/**
	 * 반품 주문 상세 리스트 조회
	 *
	 * @param request
	 * @return ApiResult<?>
	 */
	@PostMapping(value = "/admin/order/getReturnOrderDetailList")
	@ApiOperation(value = "반품 주문상세 리스트 조회", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = OrderListResponseDto.class)
	})
	public ApiResult<?> getReturnOrderDetailList(HttpServletRequest request, OrderListRequestDto orderListRequestDto) throws Exception {
		orderListRequestDto = BindUtil.bindDto(request, OrderListRequestDto.class);
		orderListRequestDto.setOrderByType(OrderEnums.OrderOrderByType.ORDER_BY_CLAIM.getCode());

		return orderListBiz.getReturnOrderDetailList(orderListRequestDto);
	}

	/**
     * 반품 주문 상세 리스트 엑셀다운로드
     *
     * @param orderListRequestDto
     * @return ModelAndView
     */
    @ApiOperation(value = "반품 주문 상세 리스트 엑셀다운로드")
    @PostMapping(value = "/admin/order/getReturnOrderDetailExcelList")
    public ModelAndView getReturnOrderDetailExcelList(@RequestBody OrderListRequestDto orderListRequestDto) throws Exception {

		orderListRequestDto.setPsExcelNm(OrderExcelNm.RA.getCodeName());
		orderListRequestDto.setOrderByType(OrderEnums.OrderOrderByType.ORDER_BY_CLAIM.getCode());

        ExcelDownloadDto excelDownloadDto = orderListBiz.getReturnOrderDetailExcelList(orderListRequestDto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }

	/**
	 * 환불 주문 상세 리스트 조회
	 *
	 * @param request
	 * @return ApiResult<?>
	 */
	@PostMapping(value = "/admin/order/getRefundOrderDetailList")
	@ApiOperation(value = "환불 주문상세 리스트 조회", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = OrderListResponseDto.class)
	})
	public ApiResult<?> getRefundOrderDetailList(HttpServletRequest request, OrderListRequestDto orderListRequestDto) throws Exception {

		orderListRequestDto = BindUtil.bindDto(request, OrderListRequestDto.class);
		orderListRequestDto.setOrderByType(OrderEnums.OrderOrderByType.ORDER_BY_CLAIM.getCode());

		return orderListBiz.getRefundOrderDetailList(orderListRequestDto);
	}

	/**
     * 환불 주문 상세 리스트 엑셀다운로드
     *
     * @param orderListRequestDto
     * @return ModelAndView
     */
    @ApiOperation(value = "환불 주문 상세 리스트 엑셀다운로드")
    @PostMapping(value = "/admin/order/getRefundOrderDetailExcelList")
    public ModelAndView getRefundOrderDetailExcelList(@RequestBody OrderListRequestDto orderListRequestDto) throws Exception {

		orderListRequestDto.setPsExcelNm(OrderExcelNm.FA.getCodeName());
		orderListRequestDto.setOrderByType(OrderEnums.OrderOrderByType.ORDER_BY_CLAIM.getCode());

        ExcelDownloadDto excelDownloadDto = orderListBiz.getRefundOrderDetailExcelList(orderListRequestDto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }

	/**
	 * CS환불 승인리스트 조회
	 *
	 * @param request
	 * @return ApiResult<?>
	 */
    @ApiOperation(value = "CS환불 승인리스트 조회", httpMethod = "POST")
	@PostMapping(value = "/admin/order/getCsRefundApprovalList")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = OrderListResponseDto.class)
	})
	public ApiResult<?> getCsRefundApprovalList(HttpServletRequest request, OrderListRequestDto csRefundRequest) throws Exception {
		csRefundRequest = BindUtil.bindDto(request, OrderListRequestDto.class);
//    	orderListRequestDto.setClaimState(OrderStatus.CUSTOMER_SERVICE_REFUND.getCode());
//		orderListRequestDto.setOrderByType(OrderEnums.OrderOrderByType.ORDER_BY_CLAIM.getCode());
//		orderListRequestDto.setClaimYn("Y");

		return orderListBiz.getCsRefundApprovalList(csRefundRequest);
	}

	/**
     * CS환불 승인리스트 엑셀다운로드
     *
     * @param orderListRequestDto
     * @return ModelAndView
     */
    @ApiOperation(value = "CS환불 주문 상세 리스트 엑셀다운로드")
    @PostMapping(value = "/admin/order/getCsRefundApprovalExcelList")
    public ModelAndView getCsRefundApprovalExcelList(@RequestBody OrderListRequestDto orderListRequestDto) throws Exception {

		orderListRequestDto.setExcelYn("Y");

        ExcelDownloadDto excelDownloadDto = orderListBiz.getCsRefundApprovalExcelList(orderListRequestDto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);
        return modelAndView;
    }

	@PostMapping(value = "/admin/order/getOrderTotalCountInfo")
	@ApiOperation(value = "상세리스트 카운트 요약정보", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = OrderListResponseDto.class)
	})
	public ApiResult<?> getOrderTotalCountInfo(HttpServletRequest request, OrderListRequestDto orderListRequestDto) throws Exception {

		return orderListBiz.getOrderTotalCountInfo(BindUtil.bindDto(request, OrderListRequestDto.class));
	}

}
