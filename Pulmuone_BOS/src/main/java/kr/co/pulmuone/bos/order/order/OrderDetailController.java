package kr.co.pulmuone.bos.order.order;

import io.swagger.annotations.*;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimAttcListDto;
import kr.co.pulmuone.v1.order.order.dto.*;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetailPresentVo;
import kr.co.pulmuone.v1.order.order.service.OrderDetailBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 상세 관련 Controller
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 28.    세        이명수         최초작성
 * =======================================================================
 * </PRE>
 */
@RestController
public class OrderDetailController {

    @Autowired
    @Qualifier("orderDetailBizImpl")
    private OrderDetailBiz orderDetailBiz;

    /**
     * 주문 상세 상품정보 조회
     * @param odOrderId
     * @return
     * @throws Exception
     */
	@PostMapping(value = "/admin/order/getOrderDetailGoodsList")
	@ApiOperation(value = "주문 상세 상품정보 조회")
	@ApiImplicitParams({ @ApiImplicitParam(name = "odOrderId", value = "주문 PK", required = true, dataType = "String")})
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = OrderDetailGoodsListResponseDto.class)
	})
    public ApiResult<?> getOrderDetailGoodsList(@RequestParam(value = "odOrderId", required = true) String odOrderId) throws Exception {
        return orderDetailBiz.getOrderDetailGoodsList(odOrderId);
    }

    /**
     * 주문 상세 클레임 상품정보 조회
     * @param odOrderId
     * @return
     * @throws Exception
     */
	@PostMapping(value = "/admin/order/getOrderDetailClaimGoodsList")
	@ApiOperation(value = "주문 상세 클레임 상품정보 조회", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "odOrderId", value = "주문 PK", required = true, dataType = "String")})
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = OrderDetailGoodsListResponseDto.class)
	})
    public ApiResult<?> getOrderDetailClaimGoodsList(@RequestParam(value = "odOrderId", required = true) String odOrderId) throws Exception {
        return orderDetailBiz.getOrderDetailClaimGoodsList(odOrderId);
    }

	/**
	 * 주문 상세 클레임 상품정보 조회 > 첨부파일보기 조회
	 * @param odClaimId
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/order/getOrderClaimAttc")
	@ApiOperation(value = "주문 상세 클레임 상품정보 조회", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "odOrderId", value = "주문 PK", required = true, dataType = "String")})
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = OrderClaimAttcListDto.class)
	})
	public ApiResult<?> getOrderClaimAttc(@RequestParam(value = "odClaimId", required = true) long odClaimId) throws Exception {
		return orderDetailBiz.getOrderClaimAttc(odClaimId);
	}

    /**
     * 주문 상세 결제 정보 조회
     * @param odOrderId
     * @return
     * @throws Exception
     */
	@PostMapping(value = "/admin/order/getOrderDetailPayList")
	@ApiOperation(value = "주문 상세 결제 정보 조회", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "odOrderId", value = "주문 PK", required = true, dataType = "String")})
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = OrderDetailPayListResponseDto.class)
	})
    public ApiResult<?> getOrderDetailPayList(@RequestParam(value = "odOrderId", required = true) String odOrderId) throws Exception {
		return orderDetailBiz.getOrderDetailPayList(odOrderId);
    }

    /**
     * 주문 상세 쿠폰할인 정보 조회
     * @param odOrderId
     * @return
     * @throws Exception
     */
	@PostMapping(value = "/admin/order/getOrderDetailDiscountList")
	@ApiOperation(value = "주문 상세 쿠폰할인 정보 조회", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "odOrderId", value = "주문 PK", required = true, dataType = "String")})
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = OrderDetailDiscountListResponseDto.class)
	})
    public ApiResult<?> getOrderDetailDiscountList(@RequestParam(value = "odOrderId", required = true) String odOrderId) throws Exception {
        return orderDetailBiz.getOrderDetailDiscountList(odOrderId);
    }

	/**
	 * 주문 상세 클레임 회수 정보 조회
	 * @param odOrderId
	 * @return
	 */
	@PostMapping(value = "/admin/order/getOrderDetailClaimCollectionList")
	@ApiOperation(value = "주문 상세 클레임 회수 정보 조회", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "odOrderId", value = "주문 PK", required = true, dataType = "String")})
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = OrderDetailClaimCollectionListResponseDto.class)
	})
	public ApiResult<?> getOrderDetailClaimCollectionList(@RequestParam(value = "odOrderId") String odOrderId) {
		return orderDetailBiz.getOrderDetailClaimCollectionList(odOrderId);
	}

    /**
     * 주문 상세 상담 조회
     * @param odOrderId
     * @return
     * @throws Exception
     */
	@PostMapping(value = "/admin/order/getOrderDetailConsultList")
	@ApiOperation(value = "주문 상세 상담 조회", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "odOrderId", value = "주문 PK", required = true, dataType = "String")})
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = OrderDetailGoodsListResponseDto.class)
	})
    public ApiResult<?> getOrderDetailConsultList(@RequestParam(value = "odOrderId", required = true) String odOrderId) throws Exception {
        return orderDetailBiz.getOrderDetailConsultList(odOrderId);
    }

    /**
     * 주문 상세 상담 등록
     * @param orderDetailConsultRequestDto
     * @return
     * @throws Exception
     */
	@PostMapping(value = "/admin/order/addOrderDetailConsult")
	@ApiOperation(value = "주문 상세 상담 등록", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class")
	})
    public ApiResult<?> addOrderDetailConsult(OrderDetailConsultRequestDto orderDetailConsultRequestDto) throws Exception {
        return orderDetailBiz.addOrderDetailConsult(orderDetailConsultRequestDto);
    }

    /**
     * 주문 상세 상담 수정
     * @param orderDetailConsultRequestDto
     * @return
     * @throws Exception
     */
	@PostMapping(value = "/admin/order/putOrderDetailConsult")
	@ApiOperation(value = "주문 상세 상담 수정", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class")
	})
    public ApiResult<?> putOrderDetailConsult(OrderDetailConsultRequestDto orderDetailConsultRequestDto) throws Exception {
        return orderDetailBiz.putOrderDetailConsult(orderDetailConsultRequestDto);
    }

    /**
     * 주문 상세 상담 삭제
     * @param odConsultId
     * @return
     * @throws Exception
     */
	@PostMapping(value = "/admin/order/delOrderDetailConsult")
	@ApiOperation(value = "주문 상세 상담 삭제", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "odConsultId", value = "주문상세 상담 PK", required = true, dataType = "String")})
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class")
	})
    public ApiResult<?> delOrderDetailConsult(@RequestParam(value = "odConsultId", required = true) String odConsultId) throws Exception {
        return orderDetailBiz.delOrderDetailConsult(odConsultId);
    }

    /**
     * 주문 상세 처리이력 정보 조회
     * @param odOrderId
     * @return
     * @throws Exception
     */
	@PostMapping(value = "/admin/order/getOrderDetailHistoryList")
	@ApiOperation(value = "주문 상세 처리이력 정보 조회", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "odOrderId", value = "주문 PK", required = true, dataType = "String")})
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = OrderDetailHistoryListResponseDto.class)
	})
    public ApiResult<?> getOrderDetailHistoryList(@RequestParam(value = "odOrderId", required = true) String odOrderId) throws Exception {
        return orderDetailBiz.getOrderDetailHistoryList(odOrderId);
    }


	/**
     * 주문 상세 주문자 정보 조회
     * @param odid
     * @return
     * @throws Exception
     */
	@PostMapping(value = "/admin/order/getOrderBuyer")
	@ApiOperation(value = "주문 상세 주문자 정보 조회", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "odid", value = "주문 PK", required = true, dataType = "String")})
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = OrderBuyerResponseDto.class)
	})
    public ApiResult<?> getOrderBuyer(@RequestParam(value = "odid", required = true) String odid) throws Exception {
        return orderDetailBiz.getOrderBuyer(odid);
    }

	/**
     * 주문 상세 배송정보 리스트 조회
     * @param odOrderId
     * @return
     * @throws Exception
     */
	@PostMapping(value = "/admin/order/getOrderDetailShippingZoneList")
	@ApiOperation(value = "주문 상세 배송정보 리스트 조회 (수취정보)", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "odOrderId", value = "주문 PK", required = true, dataType = "String")})
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = OrderDetailShippingZoneListResponseDto.class)
	})
    public ApiResult<?> getOrderDetailShippingZoneList(@RequestParam(value = "odOrderId", required = true) String odOrderId, @RequestParam(value = "odShippingZoneId", required = false) String odShippingZoneId) throws Exception {
        return orderDetailBiz.getOrderDetailShippingZoneList(odOrderId, odShippingZoneId);
    }

	/**
	 * 주문 상세 수취정보 변경
	 * @param orderDetailShippingZoneRequestDto
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/order/putOrderDetailShippingZone")
	@ApiOperation(value = "주문 상세 수취정보 변경", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class")
	})
	public ApiResult<?> putOrderDetailShippingZone(OrderDetailShippingZoneRequestDto orderDetailShippingZoneRequestDto) throws Exception {
		return orderDetailBiz.putOrderDetailShippingZone(orderDetailShippingZoneRequestDto);
	}

	/**
	 * 주문 상세 수취정보 변경내역 조회
	 * @param odShippingZoneId
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/order/getOrderShippingZoneHistList")
	@ApiOperation(value = "주문 상세 변경내역 조회 (변경 정보)", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = OrderDetailShippingZoneListResponseDto.class)
	})
	public ApiResult<?> getOrderShippingZoneHistList(@RequestParam(value = "odShippingZoneId")String odShippingZoneId, @RequestParam(value = "odOrderDetlId")String odOrderDetlId) throws Exception {
		return orderDetailBiz.getOrderShippingZoneHistList(odShippingZoneId, odOrderDetlId);
	}

	/**
	 * 주문 상세 수취정보 조회
	 * @param odShippingZoneId
	 * @return
	 * @throws Exception-
	 */
	@PostMapping(value = "/admin/order/getOrderShippingZoneByOdShippingZoneId")
	@ApiOperation(value = "주문 상세 배송정보 리스트 조회 (수취정보)", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "odOrderId", value = "주문 PK", required = true, dataType = "String")})
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = OrderDetailShippingZoneListResponseDto.class)
	})
	public ApiResult<?> getOrderShippingZoneByOdShippingZoneId(String odShippingZoneId) throws Exception {
		return orderDetailBiz.getOrderShippingZoneByOdShippingZoneId(odShippingZoneId);
	}

	/**
	 * 주문 상세 결제 정보 > 즉시할인내역 조회
	 * @param odOrderId
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/order/getOrderDirectDiscountList")
	@ApiOperation(value = "주문 상세 결제 정보 > 즉시할인내역 조회", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = OrderDetailShippingZoneListResponseDto.class)
	})
	public ApiResult<?> getOrderDirectDiscountList(String odOrderId) throws Exception {
		return orderDetailBiz.getOrderDirectDiscountList(odOrderId);
	}

	/**
	 * 주문 상세 결제 정보 > 증정품 내역 조회
	 * @param odOrderId
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/order/getOrderGiftList")
	@ApiOperation(value = "주문 상세 결제 정보 > 증정품 내역 조회", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = OrderGiftListResponseDto.class)
	})
	public ApiResult<?> getOrderGiftList(Long odOrderId) throws Exception {
		return orderDetailBiz.getOrderGiftList(odOrderId);
	}

	/**
	 * 주문 상세 매장(배송/픽업) 매장정보 조회
	 * @param odOrderId
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/order/getOrderShopStoreInfo")
	@ApiOperation(value = "주문 상세 매장(배송/픽업) 매장정보 조회", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = OrderShopStoreResponseDto.class)
	})
	public ApiResult<?> getOrderShopStoreInfo(@RequestParam(value = "odOrderId", required = true) String odOrderId) throws Exception {
		return orderDetailBiz.getOrderShopStoreInfo(odOrderId);
	}



	/**
	 * 하이톡 <--> FD-PHI 스위치 여부 조회 (하이톡 스위치)
	 */
	@PostMapping(value = "/admin/order/getHitokSwitch")
	@ApiOperation(value = "하이톡 <--> FD-PHI 스위치 여부 조회", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = Boolean.class)
	})
	public ApiResult<?> getHitokSwitch() {
		return orderDetailBiz.getHitokSwitch();
	}

	/**
	 * 주문 상세 선물정보 조회
	 * @param odOrderId
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/order/getOrderPresentInfo")
	@ApiOperation(value = "주문 상세 선물정보 조회", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = OrderDetailPresentVo.class)
	})
	public ApiResult<?> getOrderPresentInfo(@RequestParam(value = "odOrderId", required = true) String odOrderId) throws Exception {
		return orderDetailBiz.getOrderPresentInfo(odOrderId);
	}

	/**
	 * 주문 상세 선물정보 수정
	 * @param orderDetailPresentVo
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/order/putOrderPresentInfo")
	@ApiOperation(value = "주문 상세 선물정보 수정", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class")
	})
    public ApiResult<?> putOrderPresentInfo(OrderDetailPresentVo orderDetailPresentVo) throws Exception {
        return orderDetailBiz.putOrderPresentInfo(orderDetailPresentVo);
    }

    /**
	 * 주문 상세 메세지 재발송
	 * @param odid
	 * @return
	 * @throws Exception
	 */
    @PostMapping(value = "/admin/order/reSendMessage")
	@ApiOperation(value = "메세지 재발송", httpMethod = "POST")
	public ApiResult<?> reSendMessage(@RequestParam(value = "odid", required = true) String odid) throws Exception {
		return orderDetailBiz.reSendMessage(odid);
	}
}
