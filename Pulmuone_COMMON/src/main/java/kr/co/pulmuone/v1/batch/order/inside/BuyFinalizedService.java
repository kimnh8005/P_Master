package kr.co.pulmuone.v1.batch.order.inside;

import java.text.MessageFormat;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.pulmuone.v1.batch.order.inside.dto.vo.OrderStatusInfoVo;
import kr.co.pulmuone.v1.comm.constants.BatchConstants;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mappers.batch.master.inside.BuyFinalizedMapper;
import kr.co.pulmuone.v1.comm.mappers.batch.master.order.order.OrderErpMapper;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlHistVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <PRE>
 * Forbiz Korea
 * 구매확정배치 Service
 * </PRE>
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class BuyFinalizedService {

	private final BuyFinalizedMapper buyFinalizedMapper;					// 구매확정 배치 Mapper

	private final OrderErpMapper orderErpMapper;							// 주문 API 배치 Mapper

	/**
     * @Desc 구매확정대상 리스트 조회
     * @return List<OrderStatusInfoVo>
     * @throws BaseException
     */
	protected List<OrderStatusInfoVo> getBuyFinalizedList() throws BaseException {
		// 자동 구매확정 설정기간 조회
		int buyFinalizedDay = buyFinalizedMapper.getBuyFinalizedDay(BatchConstants.BUY_FINALIZED_DAY_KEY);
		return buyFinalizedDay >= 1 ? buyFinalizedMapper.getBuyFinalizedList(OrderEnums.OrderStatus.DELIVERY_COMPLETE.getCode(), buyFinalizedDay) : null;
    }

	/**
     * @Desc 구매확정대상 리스트 구매확정으로 변경
     * @param buyFinalizedList
     * @return
     * @throws BaseException
     */
	protected void putBuyFinalizedList(List<OrderStatusInfoVo> buyFinalizedList) throws BaseException {
		if (buyFinalizedList != null && buyFinalizedList.size() > 0) {
			buyFinalizedList.forEach(orderStatusInfoVo -> {
				orderStatusInfoVo.setOrderStatusCd(OrderEnums.OrderStatus.BUY_FINALIZED.getCode());	// 주문상태(구매확정)
				orderStatusInfoVo.setCreateId(Constants.BATCH_CREATE_USER_ID);						// 등록자(Batch)
				saveBuyFinalizedInfo(orderStatusInfoVo);
	  		});
		}
	}

	/**
  	 * @Desc  구매확정 개별저장 (트랜잭션 단위) 배송완료 -> 구매확정
  	 * @param buyFinalizedInfoVo
  	 * @return
  	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = { BaseException.class, Exception.class })
	protected void saveBuyFinalizedInfo(OrderStatusInfoVo orderStatusInfoVo) {
		try {
			buyFinalizedMapper.putBuyFinalizedInfo(orderStatusInfoVo);
			OrderEnums.OrderDetailStatusHistMsg orderDetailStatusHistMsg = OrderEnums.OrderDetailStatusHistMsg.findByCode(orderStatusInfoVo.getOrderStatusCd());

			OrderDetlHistVo orderDetlHistVo = OrderDetlHistVo.builder()
					.odOrderDetlId(orderStatusInfoVo.getOdOrderDetlId())
					.statusCd(OrderEnums.OrderStatus.DELIVERY_COMPLETE.getCode())
					.histMsg(MessageFormat.format(orderDetailStatusHistMsg.getMessage(), "배치", orderStatusInfoVo.getOdOrderDetlId()))
					.createId(orderStatusInfoVo.getCreateId())
					.build();
			orderErpMapper.addOrderDetailStatusHist(orderDetlHistVo);
		} catch (Exception e) {
			// TODO : SMS 개발 추가
			log.error(e.getMessage());
			new BaseException(e.getMessage());
		}
  	}
}