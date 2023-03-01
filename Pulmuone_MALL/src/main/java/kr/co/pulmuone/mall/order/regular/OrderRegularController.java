package kr.co.pulmuone.mall.order.regular;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.mall.order.regular.service.OrderRegularService;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.order.regular.dto.ApplyRegularBatchKeyResponseDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularReqGoodsListRequestDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularReqShippingZoneDto;

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
 *  1.0    20200929   	 	홍진영            최초작성
 * =======================================================================
 * </PRE>
 */
@RestController
public class OrderRegularController {

	@Autowired
	public OrderRegularService orderRegularService;

	/**
	 * 정기 결제 카드 등록
	 *
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/order/regular/applyRegularBatchKey")
	@ApiOperation(value = "정기 결제 카드 등록")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = ApplyRegularBatchKeyResponseDto.class) })
	public ApiResult<?> applyRegularBatchKey(@RequestParam(value = "paymentPrice") int paymentPrice,
			@RequestParam(value = "orderInputUrl") String orderInputUrl) throws Exception {
		// 로그인 체크
		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
		if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
			return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
		}
		return orderRegularService.applyRegularBatchKey(paymentPrice, orderInputUrl);
	}

	/**
	 * 정기 결제 카드 조회
	 *
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/order/regular/getRegularPaymentCardInfo")
	@ApiOperation(value = "정기 결제 카드 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = ApplyRegularBatchKeyResponseDto.class) })
	public ApiResult<?> getRegularPaymentCardInfo() throws Exception {
		// 로그인 체크
		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
		if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
			return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
		}
		return orderRegularService.getRegularPaymentCardInfo(Long.parseLong(buyerVo.getUrUserId()));
	}

	/**
	 * 정기 결제 카드 삭제
	 *
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/order/regular/delOrderRegularPaymentCardInfo")
	@ApiOperation(value = "정기 결제 카드 삭제")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = ApplyRegularBatchKeyResponseDto.class) })
	public ApiResult<?> delOrderRegularPaymentCardInfo(@RequestParam(value = "odRegularPaymentKeyId") long odRegularPaymentKeyId) throws Exception {
		// 로그인 체크
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
		if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
			return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
		}
		return orderRegularService.delOrderRegularPaymentCardInfo(odRegularPaymentKeyId, Long.parseLong(buyerVo.getUrUserId()));
	}

	/**
	 * 정기배송 내역 리스트
	 *
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/order/regular/getOrderRegularList")
	@ApiOperation(value = "정기배송 내역 리스트")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = ApplyRegularBatchKeyResponseDto.class) })
	public ApiResult<?> getOrderRegularList(RegularReqGoodsListRequestDto regularReqGoodsListRequestDto) throws Exception {
        // 로그인 체크
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
        }

        regularReqGoodsListRequestDto.setUrUserId(Long.parseLong(buyerVo.getUrUserId()));
		return orderRegularService.getOrderRegularList(regularReqGoodsListRequestDto);
	}

	/**
	 * 정기배송 주기 요일 변경 정보 조회
	 *
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/order/regular/getOrderRegularDaysInfo")
	@ApiOperation(value = "정기배송 주기 요일 변경 정보 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = ApplyRegularBatchKeyResponseDto.class) })
	public ApiResult<?> getOrderRegularDaysInfo(@RequestParam(value = "odRegularReqId") long odRegularReqId) throws Exception {
		// 로그인 체크
		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
		if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
			return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
		}
		return orderRegularService.getOrderRegularDaysInfo(odRegularReqId);
	}

	/**
	 * 정기배송 주기 요일 변경 도착일 목록 조회
	 *
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/order/regular/getOrderRegularArriveDtList")
	@ApiOperation(value = "정기배송 주기 요일 변경 도착일 목록 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = ApplyRegularBatchKeyResponseDto.class) })
	public ApiResult<?> getOrderRegularArriveDtList(@RequestParam(value = "odRegularReqId") long odRegularReqId, @RequestParam(value = "goodsCycleTp") String goodsCycleTp, @RequestParam(value = "weekCd") String weekCd) throws Exception {
		// 로그인 체크
		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
		if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
			return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
		}
		return orderRegularService.getOrderRegularArriveDtList(odRegularReqId, goodsCycleTp, weekCd);
	}

	/**
	 * 정기배송 주기 요일 변경
	 *
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/order/regular/putOrderRegularCycleDays")
	@ApiOperation(value = "정기배송 주기 요일 변경")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = ApplyRegularBatchKeyResponseDto.class) })
	public ApiResult<?> putOrderRegularCycleDays(@RequestParam(value = "odRegularReqId") long odRegularReqId, @RequestParam(value = "goodsCycleTp") String goodsCycleTp, @RequestParam(value = "weekCd") String weekCd) throws Exception {
		// 로그인 체크
		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
		if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
			return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
		}
		return orderRegularService.putOrderRegularCycleDays(odRegularReqId, goodsCycleTp, weekCd, Long.parseLong(buyerVo.getUrUserId()));
	}

	/**
	 * 정기배송 배송지 변경
	 *
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/order/regular/putOrderRegularShippingZone")
	@ApiOperation(value = "정기배송 배송지 변경")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = ApplyRegularBatchKeyResponseDto.class) })
	public ApiResult<?> putOrderRegularShippingZone(RegularReqShippingZoneDto regularReqShippingZoneDto) throws Exception {
		// 로그인 체크
		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
		if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
			return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
		}

		return orderRegularService.putOrderRegularShippingZone(regularReqShippingZoneDto, Long.parseLong(buyerVo.getUrUserId()));
	}

	/**
	 * 정기배송 기간연장
	 *
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/order/regular/putOrderRegularGoodsCycleTermExtension")
	@ApiOperation(value = "정기배송 기간연장")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = ApplyRegularBatchKeyResponseDto.class) })
	public ApiResult<?> putOrderRegularGoodsCycleTermExtension(@RequestParam(value = "odRegularReqId") long odRegularReqId) throws Exception {
		// 로그인 체크
		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
		if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
			return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
		}

		return orderRegularService.putOrderRegularGoodsCycleTermExtension(odRegularReqId, Long.parseLong(buyerVo.getUrUserId()));
	}

	/**
	 * 정기배송 상품 취소/건너뛰기 정보 조회
	 *
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/order/regular/getOrderRegularGoodsSkipCancelInfo")
	@ApiOperation(value = "정기배송 상품 취소/건너뛰기 정보 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = ApplyRegularBatchKeyResponseDto.class) })
	public ApiResult<?> getOrderRegularGoodsSkipCancelInfo(@RequestParam(value = "odRegularResultDetlId") long odRegularResultDetlId) throws Exception {
		// 로그인 체크
		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
		if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
			return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
		}

		return orderRegularService.getOrderRegularGoodsSkipCancelInfo(odRegularResultDetlId);
	}

	/**
	 * 정기배송 회차 건너뛰기
	 *
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/order/regular/putOrderRegularReqRoundSkip")
	@ApiOperation(value = "정기배송 회차 건너뛰기")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = ApplyRegularBatchKeyResponseDto.class) })
	public ApiResult<?> putOrderRegularReqRoundSkip(@RequestParam(value = "odRegularResultId") long odRegularResultId) throws Exception {
		// 로그인 체크
		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
		if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
			return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
		}

		return orderRegularService.putOrderRegularReqRoundSkip(odRegularResultId, Long.parseLong(buyerVo.getUrUserId()));
	}

	/**
	 * 정기배송 회차 건너뛰기 철회
	 *
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/order/regular/putOrderRegularReqRoundSkipCancel")
	@ApiOperation(value = "정기배송 회차 건너뛰기 철회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = ApplyRegularBatchKeyResponseDto.class) })
	public ApiResult<?> putOrderRegularReqRoundSkipCancel(@RequestParam(value = "odRegularResultId") long odRegularResultId) throws Exception {
		// 로그인 체크
		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
		if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
			return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
		}

		return orderRegularService.putOrderRegularReqRoundSkipCancel(odRegularResultId, Long.parseLong(buyerVo.getUrUserId()));
	}

	/**
	 * 정기배송 상품 건너뛰기
	 *
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/order/regular/putOrderRegularGoodsSkip")
	@ApiOperation(value = "정기배송 상품 건너뛰기")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = ApplyRegularBatchKeyResponseDto.class) })
	public ApiResult<?> putOrderRegularGoodsSkip(@RequestParam(value = "odRegularResultDetlId") long odRegularResultDetlId) throws Exception {
		// 로그인 체크
		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
		if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
			return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
		}

		return orderRegularService.putOrderRegularGoodsSkip(odRegularResultDetlId, Long.parseLong(buyerVo.getUrUserId()));
	}

	/**
	 * 정기배송 상품 건너뛰기 철회
	 *
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/order/regular/putOrderRegularGoodsSkipCancel")
	@ApiOperation(value = "정기배송 상품 건너뛰기 철회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = ApplyRegularBatchKeyResponseDto.class) })
	public ApiResult<?> putOrderRegularGoodsSkipCancel(@RequestParam(value = "odRegularResultDetlId") long odRegularResultDetlId) throws Exception {
		// 로그인 체크
		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
		if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
			return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
		}

		return orderRegularService.putOrderRegularGoodsSkipCancel(odRegularResultDetlId, Long.parseLong(buyerVo.getUrUserId()));
	}

	/**
	 * 정기배송 상품 취소
	 *
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/order/regular/putOrderRegularGoodsCancel")
	@ApiOperation(value = "정기배송 상품 취소")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = ApplyRegularBatchKeyResponseDto.class) })
	public ApiResult<?> putOrderRegularGoodsCancel(@RequestParam(value = "odRegularResultDetlId") long odRegularResultDetlId) throws Exception {
		// 로그인 체크
		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
		if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
			return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
		}

		return orderRegularService.putOrderRegularGoodsCancel(odRegularResultDetlId, Long.parseLong(buyerVo.getUrUserId()));
	}
}
