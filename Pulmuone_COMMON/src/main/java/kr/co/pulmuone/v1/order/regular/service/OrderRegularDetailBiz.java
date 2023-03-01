package kr.co.pulmuone.v1.order.regular.service;

import java.util.List;

import kr.co.pulmuone.v1.order.regular.dto.*;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.order.regular.dto.vo.OrderRegularReqHistoryVo;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송주문상세 OrderRegularDetailBiz
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020.02.07	  김명진           최초작성
 * =======================================================================
 * </PRE>
 */

public interface OrderRegularDetailBiz {

	/**
	 * 정기배송주문신청 내역 상품 리스트 조회
	 * @param odRegularReqId
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> getOrderRegularReqDetailGoodsList(long odRegularReqId) throws Exception;

	/**
	 * 정기배송주문신청 상품 구독해지
	 * @param odRegularReqId
	 * @param ilGoodsIdList
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> putOrderRegularReqGoodsCancel(long odRegularReqId, List<Long> ilGoodsIdList) throws Exception;

	/**
	 * 정기배송주문신청 상품 추가
	 * @param odRegularReqId
	 * @param ilGoodsId
	 * @return
	 * @throws Exception
	 */
    ApiResult<?> addOrderRegularReqGoods(long odRegularReqId, List<Long> ilGoodsIdList, List<String> ilItemCdList, List<String> goodsNmList, List<Integer> orderCntList) throws Exception;

    /**
     * 정기배송 주문 신청 기간 연장
     * @param odRegularReqId
     * @return
     * @throws Exception
     */
    ApiResult<?> putOrderRegularGoodsCycleTermExt(long odRegularReqId) throws Exception;

	/**
	 * 정기배송주문신청 내역 신청 정보 조회
	 * @param odRegularReqId
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> getOrderRegularReqDetailBuyer(long odRegularReqId) throws Exception;

	/**
	 * 정기배송주문신청 내역 신청 변경 정보 조회
	 * @param regularReqBuyerChangeRequestDto
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> getOrderRegularReqDetailChangeBuyerInfo(RegularReqBuyerChangeRequestDto regularReqBuyerChangeRequestDto) throws Exception;

	/**
	 * 정기배송 주문 신청 내역 신청정보 변경
	 * @param regularReqBuyerChangeRequestDto
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> putOrderRegularReqDetailChangeBuyerInfo(RegularReqBuyerChangeRequestDto regularReqBuyerChangeRequestDto) throws Exception;

	/**
	 * 정기배송주문신청 내역 배송지 조회
	 * @param odRegularReqId
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> getOrderRegularReqDetailShippingZone(long odRegularReqId) throws Exception;

	/**
	 * 정기배송주문신청 팝업 배송지 정보 조회
	 * @param odRegularReqId
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> getOrderRegularReqDetailShippingZonePopup(long odRegularReqId) throws Exception;

	/**
	 * 정기배송 배송지 변경 - BOS
	 * @param regularReqShippingZoneDto
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> putOrderRegularReqShippingZone(RegularReqShippingZoneDto regularReqShippingZoneDto) throws Exception;

	/**
	 * 정기배송 결제정보 얻기
	 * @param regularResultReqRoundGoodsList
	 * @param regularReqPayment
	 * @throws Exception
	 */
	void getOrderRegularPaymentInfo(List<RegularResultReqRoundGoodsListDto> regularResultReqRoundGoodsList, RegularReqPaymentListDto regularReqPayment, long urWarehouseId, int addDiscountStdReqRound) throws Exception;

	/**
	 * 정기배송주문신청 내역 결제정보 조회
	 * @param odRegularReqId
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> getOrderRegularReqDetailPayInfo(long odRegularReqId) throws Exception;

	/**
	 * 정기배송 주문 신청 상담 등록
	 * @param regularReqReqConsultDto
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> addOrderRegularReqDetailConsult(RegularReqReqConsultDto regularReqReqConsultDto) throws Exception;

	/**
	 * 정기배송 주문 신청 상담 수정
	 * @param regularReqReqConsultDto
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> putOrderRegularReqDetailConsult(RegularReqReqConsultDto regularReqReqConsultDto) throws Exception;

	/**
	 * 정기배송 주문 신청 상담 삭제
	 * @param regularReqReqConsultDto
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> delOrderRegularReqDetailConsult(RegularReqReqConsultDto regularReqReqConsultDto) throws Exception;

	/**
	 * 정기배송주문신청 신청상담 목록 조회
	 * @param odRegularReqId
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> getOrderRegularReqDetailConsultList(long odRegularReqId) throws Exception;

	/**
	 * 정기배송주문신청 내역 처리 이력 목록 조회
	 * @param odRegularReqId
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> getOrderRegularReqDetailHistoryList(long odRegularReqId) throws Exception;

	/**
	 * 정기배송 주문 신청 내역 회차별 상품 정보 배송예정내역조회
	 * @param odRegularReqId
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> getOrderRegularReqDetailShippingExpect(long odRegularReqId) throws Exception;

	/**
	 * 정기배송 주문 신청 내역 회차별 상품 건너뛰기
	 * @param odRegularReqId
	 * @param odRegularResultDetlIdList
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> putOrderRegularReqDetailGoodsSkip(RegularReqRoundGoodsSkipListRequestDto RegularReqRoundGoodsSkipListDtoList) throws Exception;

	/**
	 * 정기배송 주문 신청 내역 회차별 상품 건너뛰기 철회
	 * @param odRegularReqId
	 * @param odRegularResultDetlIdList
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> putOrderRegularReqDetailGoodsSkipCancel(RegularReqRoundGoodsSkipListRequestDto RegularReqRoundGoodsSkipListDtoList) throws Exception;

	/**
	 * 정기배송 주문 신청 내역 회차별 상품 정보 건너뛰기내역조회
	 * @param odRegularReqId
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> getOrderRegularReqDetailShippingSkip(long odRegularReqId) throws Exception;

	/**
	 * 정기배송 주문 신청 내역 회차별 상품 정보 배송내역조회
	 * @param odRegularReqId
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> getOrderRegularReqDetailShippingHistory(long odRegularReqId) throws Exception;

	/**
	 * 정기배송 주기 요일 변경 정보 조회
	 * @param odRegularReqId
	 * @return
	 * @throws Exception
	 */
	RegularReqCycleDaysInfoResponseDto getOrderRegularDaysInfo(long odRegularReqId) throws Exception;

	/**
	 * 정기배송 주기 요일 변경 도착일 목록 조회
	 * @param odRegularReqId
	 * @param goodsCycleTp
	 * @param weekCd
	 * @return
	 * @throws Exception
	 */
	RegularReqCycleDaysInfoResponseDto getOrderRegularArriveDtList(long odRegularReqId, String goodsCycleTp, String weekCd) throws Exception;

	/**
	 * 정기배송 주기 요일 변경
	 * @param odRegularReqId
	 * @param goodsCycleTp
	 * @param weekCd
	 * @return
	 * @throws Exception
	 */
	int putOrderRegularCycleDays(long odRegularReqId, String goodsCycleTp, String weekCd, long urUserId) throws Exception;

	/**
	 * 정기배송 배송지 변경
	 * @param regularReqShippingZoneDto
	 * @throws Exception
	 */
	void putOrderRegularShippingZone(RegularReqShippingZoneDto regularReqShippingZoneDto, long urUserId) throws Exception;

	/**
	 * 정기배송 기간연장
	 * @param odRegularReqId
	 * @param urUserId
	 * @throws Exception
	 */
	int putOrderRegularGoodsCycleTermExtension(long odRegularReqId, long urUserId) throws Exception;

	/**
	 * 정기배송 상품 취소/건너뛰기 정보 조회
	 * @param odRegularResultId
	 * @param ilGoodsId
	 * @return
	 * @throws Exception
	 */
	RegularReqGoodsSkipCancelResponseDto getOrderRegularGoodsSkipCancelInfo(long odRegularResultDetlId) throws Exception;

	/**
	 * 정기배송 회차 건너뛰기
	 * @param odRegularResultId
	 * @throws Exception
	 */
	void putOrderRegularReqRoundSkip(long odRegularResultId, long urUserId) throws Exception;

	/**
	 * 정기배송 회차 건너뛰기 철회
	 * @param odRegularResultId
	 * @throws Exception
	 */
	void putOrderRegularReqRoundSkipCancel(long odRegularResultId, long urUserId) throws Exception;

	/**
	 * 정기배송 상품 건너뛰기
	 * @param odRegularResultId
	 * @param ilGoodsId
	 * @throws Exception
	 */
	int putOrderRegularGoodsSkip(long odRegularResultDetlId, long urUserId) throws Exception;

	/**
	 * 정기배송 상품 건너뛰기 철회
	 * @param odRegularResultId
	 * @param ilGoodsId
	 * @throws Exception
	 */
	void putOrderRegularGoodsSkipCancel(long odRegularResultDetlId, long urUserId) throws Exception;

	/**
	 * 정기배송 상품 취소
	 * @param odRegularResultId
	 * @param ilGoodsId
	 * @throws Exception
	 */
	void putOrderRegularGoodsCancel(long odRegularResultDetlId, long urUserId) throws Exception;

	/**
	 * 정기결제 카드 정보 조회
	 * @param urUserId
	 * @return
	 * @throws Exception
	 */
	RegularReqPaymentCardResponseDto getRegularPaymentCardInfo(long urUserId) throws Exception;

	/**
	 * 정기결제 카드 삭제
	 * @param odRegularPaymentKeyId
	 * @param urUserId
	 * @throws Exception
	 */
	void delOrderRegularPaymentCardInfo(long odRegularPaymentKeyId, long urUserId) throws Exception;

	/**
	 * 배송비 얻기
	 * @param ilGoodsShippingTemplateIdList
	 * @param goodsPrice
	 * @param orderCnt
	 * @param recvZipCd
	 * @return
	 * @throws Exception
	 */
	RegularShippingPriceInfoDto getShippingPrice(List<Long> ilGoodsShippingTemplateIdList, int goodsPrice, int orderCnt, String recvZipCd) throws Exception;

	/**
	 * 정기배송 주문 히스토리 등록
	 * @param orderRegularReqHistoryVo
	 * @throws Exception
	 */
	void putRegularOrderReqHistory(OrderRegularReqHistoryVo orderRegularReqHistoryVo) throws Exception;
}
