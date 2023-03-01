package kr.co.pulmuone.v1.order.email.service;

import kr.co.pulmuone.v1.base.dto.vo.GetCodeListResultVo;
import kr.co.pulmuone.v1.order.email.dto.OrderInfoForEmailResultDto;
import kr.co.pulmuone.v1.order.order.dto.PgApprovalOrderDataDto;
import kr.co.pulmuone.v1.order.present.dto.OrderPresentDto;

import java.util.List;

public interface OrderEmailSendBiz {

	/**
	 * 주문 접수 완료
	 */
	void orderReceivedComplete(OrderInfoForEmailResultDto orderInfoDto) throws Exception;

	/**
	 * 주문 결제 완료
	 */
	void orderPaymentComplete(OrderInfoForEmailResultDto orderInfoForEmailResultDto) throws Exception;

	/**
	 * 선물하기 받는사람 보내기
	 */
	void orderPresentMassegeSend(OrderInfoForEmailResultDto orderInfoForEmailResultDto, PgApprovalOrderDataDto orderData) throws Exception;

	/**
	 * 선물하기 거절 보낸사람에게 보내기
	 */
	void orderPresentRejectMassegeSend(OrderInfoForEmailResultDto orderInfoForEmailResultDto, OrderPresentDto orderPresentDto) throws Exception;

	/**
	 * 주문 입금 완료
	 */
	void orderDepositComplete(OrderInfoForEmailResultDto orderInfoForEmailResultDto) throws Exception;

	/**
	 * 정기 주문 결제 완료
	 */
	void orderRegularPaymentComplete(OrderInfoForEmailResultDto orderInfoForEmailResultDto) throws Exception;

	/**
	 * 상품 발송
	 */
	void orderGoodsDelivery(OrderInfoForEmailResultDto orderInfoForEmailResultDto) throws Exception;

	/**
	 * 주문 취소 완료
	 */
	void orderCancelComplete(OrderInfoForEmailResultDto orderInfoForEmailResultDto, List<Long> odOrderDetlIdList) throws Exception;

	/**
	 * 주문 취소(입금 전 취소)
	 */
	void orderCancelBeforeDeposit(OrderInfoForEmailResultDto orderInfoForEmailResultDto)  throws Exception;

	/**
	 * 주문 반품 완료
	 */
	void orderReturnCompleted(OrderInfoForEmailResultDto orderInfoForEmailResultDto, List<Long> odOrderDetlIdList) throws Exception;

	/**
	 * 정기배송 신청 완료
	 */
	void orderRegularApplyCompleted(OrderInfoForEmailResultDto orderInfoForEmailResultDto, String firstOrderYn) throws Exception;

	/**
	 * 정기배송 결제 실패(1차)
	 */
	void orderRegularPaymentFailFirst(OrderInfoForEmailResultDto orderInfoForEmailResultDto) throws Exception;

	/**
	 * 정기배송 결제 실패(2차)
	 */
	void orderRegularPaymentFailSecond(OrderInfoForEmailResultDto orderInfoForEmailResultDto) throws Exception;

	/**
	 * 정기배송 결제 실패(4차)
	 */
	void orderRegularPaymentFailFourth(OrderInfoForEmailResultDto orderInfoForEmailResultDto) throws Exception;

	/**
	 * 정기배송 취소 완료
	 */
	void orderRegularCancelCompleted(OrderInfoForEmailResultDto orderInfoForEmailResultDto) throws Exception;

	/**
	 * 정기배송 상품 건너뛰기 완료
	 */
	void orderRegularGoodsSkipCompleted(OrderInfoForEmailResultDto orderInfoForEmailResultDto) throws Exception;

	/**
	 * 정기배송 회차 건너뛰기 완료
	 */
	void orderRegularReqRoundSkipCompleted(OrderInfoForEmailResultDto orderInfoForEmailResultDto) throws Exception;

	/**
	 * 정기배송 주문 생성 완료
	 */
	void orderRegularCreationCompleted(OrderInfoForEmailResultDto orderInfoForEmailResultDto) throws Exception;

	/**
	 * 정기배송 만료 예정
	 */
	void orderRegularExpireExpected(OrderInfoForEmailResultDto orderInfoForEmailResultDto) throws Exception;

	/**
	 * 정기배송 만료
	 */
	void orderRegularExpired(OrderInfoForEmailResultDto orderInfoForEmailResultDto) throws Exception;

	/**
	 * 녹즙 일일배송 종료
	 */
	void orderDailyGreenJuiceEnd(OrderInfoForEmailResultDto orderInfoForEmailResultDto) throws Exception;

	/**
	 * 정기배송 상품금액 변동 안내
	 */
	void orderRegularGoodsPriceChange(OrderInfoForEmailResultDto orderInfoForEmailResultDto) throws Exception;

	/**
	 * BOS 주문 상태 알림
	 */
	void bosOrderStatusNotification(OrderInfoForEmailResultDto orderInfoForEmailResultDto) throws Exception;

	/**
	 * 직접배송 미등록 송장 알림
	 * @throws Exception
	 */
	void directShippingUnregisteredInvoiceNotification() throws Exception;

	/**
	 * BOS 수집몰 연동 실패 알림
	 * @throws Exception
	 */
	void bosCollectionMallInterfaceFailNotification() throws Exception;

	/**
	 * BOS 올가 식품안전팀 주의주문 발생 알림
	 * @throws Exception
	 */
	void bosOrgaCautionOrderNotification(OrderInfoForEmailResultDto orderInfoForEmailResultDto) throws Exception;

	/**
	 * 부분취소 배송비 추가결제 가상계좌 발급
	 * */
	void payAdditionalShippingPrice(OrderInfoForEmailResultDto orderInfoForEmailResultDto) throws Exception;

	/**
	 * 매장픽업 상품 발송
	 */
	void orderShopPickupGoodsDelivery(OrderInfoForEmailResultDto orderInfoForEmailResultDto) throws Exception;
}
