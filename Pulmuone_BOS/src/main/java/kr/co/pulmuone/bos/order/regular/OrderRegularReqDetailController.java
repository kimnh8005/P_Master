package kr.co.pulmuone.bos.order.regular;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.order.regular.dto.RegularReqBuyerChangeRequestDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularReqConsultListResponseDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularReqCycleDaysInfoResponseDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularReqDetailGoodsListResponseDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularReqHistoryListResponseDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularReqListDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularReqPaymentListResponseDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularReqReqConsultDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularReqRoundGoodsListResponseDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularReqRoundGoodsSkipListRequestDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularReqShippingZoneDto;
import kr.co.pulmuone.v1.order.regular.service.OrderRegularDetailBiz;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송주문신청 상세 관련 Controller
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 02. 05.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */
@RestController
public class OrderRegularReqDetailController {

    @Autowired
    private OrderRegularDetailBiz orderRegularDetailBiz;

    /**
     * 정기배송 주문 신청 내역 상품 리스트 조회
     *
     * @param odRegularReqId
     * @return ApiResult<?>
     */
    @ApiOperation(value = "정기배송 주문 신청 내역 상품 리스트 조회", httpMethod = "POST")
	@PostMapping(value = "/admin/order/getOrderRegularReqDetailGoodsList")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = RegularReqDetailGoodsListResponseDto.class)
	})
    public ApiResult<?> getOrderRegularReqList(@RequestParam(value = "odRegularReqId", required = true) long odRegularReqId) throws Exception {
        return orderRegularDetailBiz.getOrderRegularReqDetailGoodsList(odRegularReqId);
    }

    /**
     * 정기배송 주문 신청 내역 상품 구독 해지
     *
     * @param odRegularReqId
     * @return ApiResult<?>
     */
    @ApiOperation(value = "정기배송 주문 신청 내역 상품 구독 해지", httpMethod = "POST")
    @PostMapping(value = "/admin/order/putOrderRegularReqGoodsCancel")
    @ApiResponses(value = {
    		@ApiResponse(code = 900, message = "response data List<>", response = RegularReqDetailGoodsListResponseDto.class)
    })
    public ApiResult<?> putOrderRegularReqGoodsCancel(@RequestParam(value = "odRegularReqId", required = true) long odRegularReqId,
    													@RequestParam(value = "ilGoodsIdList[]", required = true) List<Long> ilGoodsIdList) throws Exception {
    	return orderRegularDetailBiz.putOrderRegularReqGoodsCancel(odRegularReqId, ilGoodsIdList);
    }

    /**
     * 정기배송 주문 신청 내역 상품 추가
     *
     * @param odRegularReqId
     * @return ApiResult<?>
     */
    @ApiOperation(value = "정기배송 주문 신청 내역 상품 추가", httpMethod = "POST")
    @PostMapping(value = "/admin/order/addOrderRegularReqGoods")
    @ApiResponses(value = {
    		@ApiResponse(code = 900, message = "response data List<>")
    })
    public ApiResult<?> addOrderRegularReqGoods(@RequestParam(value = "odRegularReqId", required = true) long odRegularReqId,
									    		@RequestParam(value = "ilItemCdList[]", required = true) List<String> ilItemCdList,
    											@RequestParam(value = "ilGoodsIdList[]", required = true) List<Long> ilGoodsIdList,
    											@RequestParam(value = "goodsNmList[]", required = true) List<String> goodsNmList,
    											@RequestParam(value = "orderCntList[]", required = true) List<Integer> orderCntList) throws Exception {
    	return orderRegularDetailBiz.addOrderRegularReqGoods(odRegularReqId, ilGoodsIdList, ilItemCdList, goodsNmList, orderCntList);
    }

    /**
     * 정기배송 주문 신청 기간연장
     *
     * @param odRegularReqId
     * @return ApiResult<?>
     */
    @ApiOperation(value = "정기배송 주문 신청 기간연장", httpMethod = "POST")
    @PostMapping(value = "/admin/order/putOrderRegularGoodsCycleTermExtension")
    @ApiResponses(value = {
    		@ApiResponse(code = 900, message = "response data List<>")
    })
    public ApiResult<?> putOrderRegularGoodsCycleTermExt(@RequestParam(value = "odRegularReqId", required = true) long odRegularReqId) throws Exception {
    	return orderRegularDetailBiz.putOrderRegularGoodsCycleTermExt(odRegularReqId);
    }

    /**
     * 정기배송 주문 신청 내역 신청정보 조회
     *
     * @param odRegularReqId
     * @return ApiResult<?>
     */
    @ApiOperation(value = "정기배송 주문 신청 내역 신청정보 조회", httpMethod = "POST")
	@PostMapping(value = "/admin/order/getOrderRegularReqDetailBuyer")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = RegularReqListDto.class)
	})
    public ApiResult<?> getOrderRegularReqDetailBuyer(@RequestParam(value = "odRegularReqId", required = true) long odRegularReqId) throws Exception {
        return orderRegularDetailBiz.getOrderRegularReqDetailBuyer(odRegularReqId);
    }

    /**
     * 정기배송 주문 신청 내역 변경 신청정보 조회
     *
     * @param regularReqBuyerChangeRequestDto
     * @return ApiResult<?>
     */
    @ApiOperation(value = "정기배송 주문 신청 내역 변경 신청정보 조회", httpMethod = "POST")
    @PostMapping(value = "/admin/order/getOrderRegularReqDetailChangeBuyerInfo")
    @ApiResponses(value = {
    		@ApiResponse(code = 900, message = "response data List<>", response = RegularReqCycleDaysInfoResponseDto.class)
    })
    public ApiResult<?> getOrderRegularReqDetailChangeBuyerInfo(RegularReqBuyerChangeRequestDto regularReqBuyerChangeRequestDto) throws Exception {
    	return orderRegularDetailBiz.getOrderRegularReqDetailChangeBuyerInfo(regularReqBuyerChangeRequestDto);
    }

    /**
     * 정기배송 주문 신청 내역 신청정보 변경
     *
     * @param regularReqBuyerChangeRequestDto
     * @return ApiResult<?>
     */
    @ApiOperation(value = "정기배송 주문 신청 내역 신청정보 변경", httpMethod = "POST")
    @PostMapping(value = "/admin/order/putOrderRegularReqDetailChangeBuyerInfo")
    @ApiResponses(value = {
    		@ApiResponse(code = 900, message = "response data List<>", response = RegularReqCycleDaysInfoResponseDto.class)
    })
    public ApiResult<?> putOrderRegularReqDetailChangeBuyerInfo(RegularReqBuyerChangeRequestDto regularReqBuyerChangeRequestDto) throws Exception {
    	return orderRegularDetailBiz.putOrderRegularReqDetailChangeBuyerInfo(regularReqBuyerChangeRequestDto);
    }

    /**
     * 정기배송 주문 신청 내역 배송정보 조회
     *
     * @param odRegularReqId
     * @return ApiResult<?>
     */
    @ApiOperation(value = "정기배송 주문 신청 내역 배송정보 조회", httpMethod = "POST")
    @PostMapping(value = "/admin/order/getOrderRegularReqDetailShippingZone")
    @ApiResponses(value = {
    		@ApiResponse(code = 900, message = "response data List<>", response = RegularReqShippingZoneDto.class)
    })
    public ApiResult<?> getOrderRegularReqDetailShippingZone(@RequestParam(value = "odRegularReqId", required = true) long odRegularReqId) throws Exception {
    	return orderRegularDetailBiz.getOrderRegularReqDetailShippingZone(odRegularReqId);
    }

    /**
     * 정기배송 주문 신청 팝업 배송정보 조회
     *
     * @param odRegularReqId
     * @return ApiResult<?>
     */
    @ApiOperation(value = "정기배송 주문 신청 팝업 배송정보 조회", httpMethod = "POST")
    @PostMapping(value = "/admin/order/getOrderRegularReqDetailShippingZonePopup")
    @ApiResponses(value = {
    		@ApiResponse(code = 900, message = "response data List<>", response = RegularReqShippingZoneDto.class)
    })
    public ApiResult<?> getOrderRegularReqDetailShippingZonePopup(@RequestParam(value = "odRegularReqId", required = true) long odRegularReqId) throws Exception {
    	return orderRegularDetailBiz.getOrderRegularReqDetailShippingZonePopup(odRegularReqId);
    }

    /**
     * 정기배송 주문 신청 내역 배송정보 수정
     *
     * @param odRegularReqId
     * @return ApiResult<?>
     */
    @ApiOperation(value = "정기배송 주문 신청 내역 배송정보 수정", httpMethod = "POST")
    @PostMapping(value = "/admin/order/putOrderRegularReqShippingZone")
    @ApiResponses(value = {
    		@ApiResponse(code = 900, message = "response data List<>")
    })
    public ApiResult<?> putOrderRegularReqShippingZone(RegularReqShippingZoneDto regularReqShippingZoneDto) throws Exception {
    	return orderRegularDetailBiz.putOrderRegularReqShippingZone(regularReqShippingZoneDto);
    }

    /**
     * 정기배송 주문 신청 내역 결제정보 조회
     *
     * @param odRegularReqId
     * @return ApiResult<?>
     */
    @ApiOperation(value = "정기배송 주문 신청 내역 결제정보 조회", httpMethod = "POST")
    @PostMapping(value = "/admin/order/getOrderRegularReqDetailPayInfo")
    @ApiResponses(value = {
    		@ApiResponse(code = 900, message = "response data List<>", response = RegularReqPaymentListResponseDto.class)
    })
    public ApiResult<?> getOrderRegularReqDetailPayInfo(@RequestParam(value = "odRegularReqId", required = true) long odRegularReqId) throws Exception {
    	return orderRegularDetailBiz.getOrderRegularReqDetailPayInfo(odRegularReqId);
    }

    /**
     * 정기배송 주문 신청 내역 신청 상담 목록 조회
     *
     * @param odRegularReqId
     * @return ApiResult<?>
     */
    @ApiOperation(value = "정기배송 주문 신청 내역 신청 상담 목록 조회", httpMethod = "POST")
    @PostMapping(value = "/admin/order/getOrderRegularReqDetailConsultList")
    @ApiResponses(value = {
    		@ApiResponse(code = 900, message = "response data List<>", response = RegularReqConsultListResponseDto.class)
    })
    public ApiResult<?> getOrderRegularReqDetailConsultList(@RequestParam(value = "odRegularReqId", required = true) long odRegularReqId) throws Exception {
    	return orderRegularDetailBiz.getOrderRegularReqDetailConsultList(odRegularReqId);
    }

    /**
     * 정기배송 주문 신청 상담 등록
     *
     * @param odRegularReqId
     * @return ApiResult<?>
     */
    @ApiOperation(value = "정기배송 주문 신청 상담 등록", httpMethod = "POST")
    @PostMapping(value = "/admin/order/addOrderRegularReqDetailConsult")
    @ApiResponses(value = {
    		@ApiResponse(code = 900, message = "response data List<>")
    })
    public ApiResult<?> addOrderRegularReqDetailConsult(RegularReqReqConsultDto regularReqReqConsultDto) throws Exception {
    	return orderRegularDetailBiz.addOrderRegularReqDetailConsult(regularReqReqConsultDto);
    }

    /**
     * 정기배송 주문 신청 상담 수정
     *
     * @param odRegularReqId
     * @return ApiResult<?>
     */
    @ApiOperation(value = "정기배송 주문 신청 상담 수정", httpMethod = "POST")
    @PostMapping(value = "/admin/order/putOrderRegularReqDetailConsult")
    @ApiResponses(value = {
    		@ApiResponse(code = 900, message = "response data List<>")
    })
    public ApiResult<?> putOrderRegularReqDetailConsult(RegularReqReqConsultDto regularReqReqConsultDto) throws Exception {
    	return orderRegularDetailBiz.putOrderRegularReqDetailConsult(regularReqReqConsultDto);
    }

    /**
     * 정기배송 주문 신청 상담 삭제
     *
     * @param odRegularReqId
     * @return ApiResult<?>
     */
    @ApiOperation(value = "정기배송 주문 신청 상담 삭제", httpMethod = "POST")
    @PostMapping(value = "/admin/order/delOrderRegularReqDetailConsult")
    @ApiResponses(value = {
    		@ApiResponse(code = 900, message = "response data List<>")
    })
    public ApiResult<?> delOrderRegularReqDetailConsult(RegularReqReqConsultDto regularReqReqConsultDto) throws Exception {
    	return orderRegularDetailBiz.delOrderRegularReqDetailConsult(regularReqReqConsultDto);
    }

    /**
     * 정기배송 주문 신청 내역 처리 이력 목록 조회
     *
     * @param odRegularReqId
     * @return ApiResult<?>
     */
    @ApiOperation(value = "정기배송 주문 신청 내역 처리 이력 목록 조회", httpMethod = "POST")
    @PostMapping(value = "/admin/order/getOrderRegularReqDetailHistoryList")
    @ApiResponses(value = {
    		@ApiResponse(code = 900, message = "response data List<>", response = RegularReqHistoryListResponseDto.class)
    })
    public ApiResult<?> getOrderRegularReqDetailHistoryList(@RequestParam(value = "odRegularReqId", required = true) long odRegularReqId) throws Exception {
    	return orderRegularDetailBiz.getOrderRegularReqDetailHistoryList(odRegularReqId);
    }

    /**
     * 정기배송 주문 신청 내역 회차별 상품 정보 배송예정내역조회
     *
     * @param odRegularReqId
     * @return ApiResult<?>
     */
    @ApiOperation(value = "정기배송 주문 신청 내역 회차별 상품 정보 배송예정내역조회", httpMethod = "POST")
    @PostMapping(value = "/admin/order/getOrderRegularReqDetailShippingExpect")
    @ApiResponses(value = {
    		@ApiResponse(code = 900, message = "response data List<>", response = RegularReqRoundGoodsListResponseDto.class)
    })
    public ApiResult<?> getOrderRegularReqDetailShippingExpect(@RequestParam(value = "odRegularReqId", required = true) long odRegularReqId) throws Exception {
    	return orderRegularDetailBiz.getOrderRegularReqDetailShippingExpect(odRegularReqId);
    }

    /**
     * 정기배송 주문 신청 내역 회차별 상품 정보 건너뛰기내역조회
     *
     * @param odRegularReqId
     * @return ApiResult<?>
     */
    @ApiOperation(value = "정기배송 주문 신청 내역 회차별 상품 정보 건너뛰기내역조회", httpMethod = "POST")
    @PostMapping(value = "/admin/order/getOrderRegularReqDetailShippingSkip")
    @ApiResponses(value = {
    		@ApiResponse(code = 900, message = "response data List<>", response = RegularReqRoundGoodsListResponseDto.class)
    })
    public ApiResult<?> getOrderRegularReqDetailShippingSkip(@RequestParam(value = "odRegularReqId", required = true) long odRegularReqId) throws Exception {
    	return orderRegularDetailBiz.getOrderRegularReqDetailShippingSkip(odRegularReqId);
    }

    /**
     * 정기배송 주문 신청 내역 회차별 상품 건너뛰기
     *
     * @param odRegularReqId
     * @return ApiResult<?>
     */
    @ApiOperation(value = "정기배송 주문 신청 내역 회차별 상품 건너뛰기", httpMethod = "POST")
    @PostMapping(value = "/admin/order/getOrderRegularReqDetailGoodsSkip")
    @ApiResponses(value = {
    		@ApiResponse(code = 900, message = "response data List<>", response = RegularReqRoundGoodsListResponseDto.class)
    })
    public ApiResult<?> getOrderRegularReqDetailGoodsSkip(@RequestBody RegularReqRoundGoodsSkipListRequestDto regularReqRoundGoodsSkipListRequestDto) throws Exception {
    	return orderRegularDetailBiz.putOrderRegularReqDetailGoodsSkip(regularReqRoundGoodsSkipListRequestDto);
    }

    /**
     * 정기배송 주문 신청 내역 회차별 상품 건너뛰기 철회
     *
     * @param odRegularReqId
     * @return ApiResult<?>
     */
    @ApiOperation(value = "정기배송 주문 신청 내역 회차별 상품 건너뛰기 철회", httpMethod = "POST")
    @PostMapping(value = "/admin/order/getOrderRegularReqDetailGoodsSkipCancel")
    @ApiResponses(value = {
    		@ApiResponse(code = 900, message = "response data List<>", response = RegularReqRoundGoodsListResponseDto.class)
    })
    public ApiResult<?> getOrderRegularReqDetailGoodsSkipCancel(@RequestBody RegularReqRoundGoodsSkipListRequestDto regularReqRoundGoodsSkipListRequestDto) throws Exception {
    	return orderRegularDetailBiz.putOrderRegularReqDetailGoodsSkipCancel(regularReqRoundGoodsSkipListRequestDto);
    }

    /**
     * 정기배송 주문 신청 내역 회차별 상품 정보 배송내역조회
     *
     * @param odRegularReqId
     * @return ApiResult<?>
     */
    @ApiOperation(value = "정기배송 주문 신청 내역 회차별 상품 정보 배송내역조회", httpMethod = "POST")
    @PostMapping(value = "/admin/order/getOrderRegularReqDetailShippingHistory")
    @ApiResponses(value = {
    		@ApiResponse(code = 900, message = "response data List<>", response = RegularReqRoundGoodsListResponseDto.class)
    })
    public ApiResult<?> getOrderRegularReqDetailShippingHistory(@RequestParam(value = "odRegularReqId", required = true) long odRegularReqId) throws Exception {
    	return orderRegularDetailBiz.getOrderRegularReqDetailShippingHistory(odRegularReqId);
    }
}
