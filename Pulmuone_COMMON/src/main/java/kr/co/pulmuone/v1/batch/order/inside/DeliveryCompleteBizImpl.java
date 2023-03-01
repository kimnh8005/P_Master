package kr.co.pulmuone.v1.batch.order.inside;

import java.util.List;

import kr.co.pulmuone.v1.comm.constants.BatchConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.pulmuone.v1.batch.order.inside.dto.vo.OrderStatusInfoVo;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <PRE>
 * Forbiz Korea
 * 배송완료배치 BizImpl
 * </PRE>
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryCompleteBizImpl implements DeliveryCompleteBiz {

    @Autowired
    private DeliveryCompleteService deliveryCompleteService;	// 배송완료 배치 Service

	/**
	 * 배송완료 대상 조회 및 저장 배치
	 * @param
	 * @return void
	 * @throws BaseException
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = { BaseException.class, Exception.class })
	public void runDeliveryCompleteSetUp() throws BaseException {
		// 배송완료 대상 조회
		List<OrderStatusInfoVo> deliveryCompleteList = deliveryCompleteService.getDeliveryCompleteList();
		deliveryCompleteService.putDeliveryCompleteList(deliveryCompleteList);
	}

	/**
	 * 매장배송 배송완료 대상 조회 및 저장 배치
	 * @param
	 * @return void
	 * @throws BaseException
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = { BaseException.class, Exception.class })
	public void runStoreDeliveryCompleteSetUp() throws BaseException {
		deliveryCompleteService.putStoreDeliveryCompleteList();
	}

	/**
	 * 배송완료 대상(일일:녹즙, 베이비밀, 잇슬림) 조회 및 저장 배치
	 * @param
	 * @return void
	 * @throws BaseException
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = { BaseException.class, Exception.class })
	public void runDeliveryCompleteDailySetUp() throws BaseException {
		// 배송완료 대상(일일) 조회
		List<OrderStatusInfoVo> deliveryCompleteList = deliveryCompleteService.getDeliveryCompleteDailyList();
		deliveryCompleteService.putDeliveryCompleteList(deliveryCompleteList);

		// 하이톡(녹즙) 스케줄 배송완료 저장
		deliveryCompleteService.putHitokScheduleDeliveryComplete();
	}

	/**
	 * 배송완료 대상(CJ/롯데 트래킹) 조회 및 저장 배치
	 * @param
	 * @return void
	 * @throws BaseException
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = { BaseException.class, Exception.class })
	public void runDeliveryCompleteTrackingSetUp() throws BaseException {
		// 배송완료 대상(CJ/롯데 트래킹) 조회
		List<OrderStatusInfoVo> deliveryCompleteTrackingList = deliveryCompleteService.getDeliveryCompleteTrackingList();
		if (deliveryCompleteTrackingList == null || deliveryCompleteTrackingList.isEmpty()) return;

		int page = 0;
		int BATCH_COUNT = BatchConstants.BATCH_DELIVERY_COMPLETE_COUNT;
		int maxCount = deliveryCompleteTrackingList.size();

		while (page <= maxCount) {
			int limit = page + BATCH_COUNT;
			if (limit > maxCount) limit = maxCount;
			List<OrderStatusInfoVo> targetList = deliveryCompleteTrackingList.subList(page, limit);
			List<OrderStatusInfoVo> putList = deliveryCompleteService.apiDeliveryCompleteTrackingList(targetList);
			deliveryCompleteService.putDeliveryCompleteListTransaction(putList);

			page += BATCH_COUNT;
		}
	}

	/**
	 * 잇슬림 일일배송 배송중 배치
	 * @throws BaseException
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = { BaseException.class, Exception.class })
	public void runEatsslimDailyDeliveryIngSetUp() throws BaseException {
		deliveryCompleteService.putEatsslimDailyDeliveryIng();
	}
}