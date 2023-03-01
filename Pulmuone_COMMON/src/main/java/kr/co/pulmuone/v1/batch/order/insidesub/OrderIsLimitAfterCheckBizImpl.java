package kr.co.pulmuone.v1.batch.order.insidesub;

import kr.co.pulmuone.v1.batch.order.insidesub.dto.LimitIsOrderCountSearchRequestDto;
import kr.co.pulmuone.v1.batch.order.insidesub.dto.vo.OrderLimitInfoVo;
import kr.co.pulmuone.v1.comm.enums.ErpApiEnums;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <PRE>
 * Forbiz Korea
 * 용인물류 일반배송 주문체크 후 제한 배치 BizImpl
 * </PRE>
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderIsLimitAfterCheckBizImpl implements OrderIsLimitAfterCheckBiz {

	private final OrderIsLimitAfterCheckService orderIsLimitAfterCheckService;

	/**
	 * 용인물류 일반배송 주문체크 후 제한
	 * @param
	 * @return void
	 * @throws BaseException
	 */
	@Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = { BaseException.class, Exception.class })
	public void normalDeliveryOrderIsLimitAfterCheck() throws BaseException {

		// 주문제한정보 조회
		OrderLimitInfoVo orderLimitInfo = orderIsLimitAfterCheckService.selectOrderLimitInfo();
		int nowHour = StringUtil.nvlInt(DateUtil.getCurrentDate("HH")); // 현재시간
		int orderLimitStartTime = orderLimitInfo.getOrderLimitStartTime(); // 주문제한 시작시간
		int orderLimitEndTime = orderLimitInfo.getOrderLimitEndTime(); // 주문제한 종료시간
		int orderLimitCount = orderLimitInfo.getOrderLimitCount(); // 주문제한 수량

		log.info("===================nowHour:"+nowHour+"|orderLimitStartTime:"+orderLimitStartTime+"|orderLimitEndTime:"+orderLimitEndTime);

		if(orderLimitStartTime <= nowHour && nowHour < orderLimitEndTime) {
			String strOrderLimitStartTime = "0"+orderLimitStartTime;
			String strOrderLimitEndTime = "0"+orderLimitEndTime;

			// 제한시간내 주문 제한수량 체크
			limitOrderCountCheck(strOrderLimitStartTime, strOrderLimitEndTime, orderLimitCount);
		}

	}

	/**
	 * 제한시간내 주문 제한수량 체크
	 * @param strOrderLimitStartTime
	 * @param strOrderLimitEndTime
	 * @param orderLimitCount
	 * @return void
	 * @throws BaseException
	 */
	protected void limitOrderCountCheck(String strOrderLimitStartTime, String strOrderLimitEndTime, int orderLimitCount) throws BaseException {

		String orderLimitStartTime = strOrderLimitStartTime.substring(strOrderLimitStartTime.length()-2);
		String orderLimitEndTime = strOrderLimitEndTime.substring(strOrderLimitEndTime.length()-2);

		// 제한시간내 주문건수 조회
		String[] orderStatusCds = { OrderEnums.OrderStatus.INCOM_COMPLETE.getCode(), OrderEnums.OrderStatus.DELIVERY_READY.getCode(), OrderEnums.OrderStatus.DELIVERY_ING.getCode() };
		LimitIsOrderCountSearchRequestDto params =  LimitIsOrderCountSearchRequestDto.builder()
									.orderLimitStartTime(orderLimitStartTime) // 주문제한 시작시간
									.orderLimitEndTime(orderLimitEndTime) // 주문제한 종료시간
									.urWarehouseId(ErpApiEnums.UrWarehouseId.YONGIN_LOGISTICS.getCode()) // 출고처: 용인물류
									.goodsDeliveryType(GoodsEnums.GoodsDeliveryType.NORMAL.getCode()) // 배송유형: 일반
									.orderStatusCd(orderStatusCds) // 주문상태: 결제완료, 배송준비중, 배송완료
									.build();
		int orderCount = orderIsLimitAfterCheckService.selectLimitIsOrderCount(params);

		log.info("===================orderCount:"+orderCount+"|orderLimitCount:"+orderLimitCount);

		// 제한 주문건수 넘으면 SMS 발송 및 이력저장
		if(orderCount >= orderLimitCount) sendSmsAndPutSendHist();

	}

	/**
	 * SMS 발송 및 이력저장
	 * @param
	 * @return void
	 * @throws BaseException
	 */
	protected void sendSmsAndPutSendHist() throws BaseException {

		// 오늘 SMS 발송이력 카운트 조회
		int todaySmsSendHistCount = orderIsLimitAfterCheckService.selectTodaySmsSendHistCount();

		log.info("===================todaySmsSendHistCount:"+todaySmsSendHistCount);

		// 오늘 SMS 발송이력 없을때 SMS 발송 및 이력저장
		if(todaySmsSendHistCount <= 0) {
			// TODO SMS 발송 개발 필요

			// SMS 발송 이력저장
			orderIsLimitAfterCheckService.putSmsSendHist();
		}

	}


}