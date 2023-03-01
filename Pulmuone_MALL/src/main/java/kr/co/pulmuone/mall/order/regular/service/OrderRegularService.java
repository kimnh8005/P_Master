package kr.co.pulmuone.mall.order.regular.service;

import org.springframework.web.bind.annotation.RequestParam;

import kr.co.pulmuone.v1.comm.base.ApiResult;
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
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20200929		 	홍진영            최초작성
 * =======================================================================
 * </PRE>
 */

public interface OrderRegularService {

	/**
	 * 정기결제 카드 등록
	 * @param paymentPrice
	 * @param orderInputUrl
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> applyRegularBatchKey(int paymentPrice, String orderInputUrl) throws Exception;

	/**
	 * 정기결제 카드 조회
	 * @param urUserId
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> getRegularPaymentCardInfo(long urUserId) throws Exception;

	/**
	 * 정기결제 카드 삭제
	 * @param odRegularPaymentKeyId
	 * @param urUserId
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> delOrderRegularPaymentCardInfo(long odRegularPaymentKeyId, long urUserId) throws Exception;

	/**
	 * 정기배송 주문 목록 조회
	 * @param urUserId
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> getOrderRegularList(RegularReqGoodsListRequestDto regularReqGoodsListRequestDto) throws Exception;

	/**
	 * 정기배송 주기 요일 변경 정보 조회
	 * @param urUserId
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> getOrderRegularDaysInfo(long odRegularReqId) throws Exception;

	/**
	 * 정기배송 주기 요일 변경 도착일 목록 조회
	 * @param odRegularReqId
	 * @param goodsCycleTp
	 * @param weekCd
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> getOrderRegularArriveDtList(long odRegularReqId, String goodsCycleTp, String weekCd) throws Exception;

	/**
	 * 정기배송 주기 요일 변경
	 * @param odRegularReqId
	 * @param goodsCycleTp
	 * @param weekCd
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> putOrderRegularCycleDays(long odRegularReqId, String goodsCycleTp, String weekCd, long urUserId) throws Exception;

	/**
	 * 정기배송 배송지 변경
	 * @param regularReqShippingZoneDto
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> putOrderRegularShippingZone(RegularReqShippingZoneDto regularReqShippingZoneDto, long urUserId) throws Exception;

	/**
	 * 정기배송 기간연장
	 * @param odRegularReqId
	 * @param urUserId
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> putOrderRegularGoodsCycleTermExtension(long odRegularReqId, long urUserId) throws Exception;

	/**
	 * 정기배송 상품 취소/건너뛰기 정보 조회
	 * @param odRegularResultId
	 * @param ilGoodsId
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> getOrderRegularGoodsSkipCancelInfo(long odRegularResultDetlId) throws Exception;

	/**
	 * 정기배송 회차 건너뛰기
	 * @param odRegularResultId
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> putOrderRegularReqRoundSkip(long odRegularResultId, long urUserId) throws Exception;

	/**
	 * 정기배송 회차 건너뛰기 철회
	 * @param odRegularResultId
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> putOrderRegularReqRoundSkipCancel(long odRegularResultId, long urUserId) throws Exception;

	/**
	 * 정기배송 상품 건너뛰기
	 * @param odRegularResultId
	 * @param ilGoodsId
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> putOrderRegularGoodsSkip(long odRegularResultDetlId, long urUserId) throws Exception;

	/**
	 * 정기배송 상품 건너뛰기 철회
	 * @param odRegularResultId
	 * @param ilGoodsId
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> putOrderRegularGoodsSkipCancel(long odRegularResultDetlId, long urUserId) throws Exception;

	/**
	 * 정기배송 상품 취소
	 * @param odRegularResultId
	 * @param ilGoodsId
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> putOrderRegularGoodsCancel(long odRegularResultDetlId, long urUserId) throws Exception;
}
