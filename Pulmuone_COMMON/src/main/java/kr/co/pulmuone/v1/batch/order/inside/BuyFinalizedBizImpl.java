package kr.co.pulmuone.v1.batch.order.inside;

import java.util.List;

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
 * 구매확정배치 BizImpl
 * </PRE>
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class BuyFinalizedBizImpl implements BuyFinalizedBiz {

    @Autowired
    private BuyFinalizedService buyFinalizedService;	// 구매확정 배치 Service

	/**
	 * 구매확정 대상 조회 및 저장 배치
	 * @param
	 * @return void
	 * @throws BaseException
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = { BaseException.class, Exception.class })
	public void runBuyFinalizedSetUp() throws BaseException {
		// 구매확정 대상 조회
		List<OrderStatusInfoVo> buyFinalizedList = buyFinalizedService.getBuyFinalizedList();
		buyFinalizedService.putBuyFinalizedList(buyFinalizedList);
	}
}