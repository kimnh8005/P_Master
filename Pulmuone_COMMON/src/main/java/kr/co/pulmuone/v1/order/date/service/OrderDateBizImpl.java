package kr.co.pulmuone.v1.order.date.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.order.claim.dto.vo.ClaimDetlDtVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDtVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

 /**
 * <PRE>
 * Forbiz Korea
 * 주문 일자 관련 Implements
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일				:  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 01. 06.       이명수          	  최초작성
 *  1.1	   2021. 01. 08.       이규한             주문, 주문 상세 상태별 일자 수정 추가
 *  1.2    2021. 01. 13.       김명진             클레임, 클레임 상세 상태별 일자 수정 추가
 * =======================================================================
 * </PRE>
 */

@Service
public class OrderDateBizImpl implements OrderDateBiz {

	@Autowired
    private OrderDateService orderDateService;

    /**
     * @Desc 주문 상태별 일자 수정
     * @param orderDtVo
     * @return ApiResult
     * @throws Exception
     */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> putOrderDt(OrderDtVo orderDtVo) {
		orderDateService.putOrderDt(orderDtVo);
        return ApiResult.success();
	}

    /**
     * @Desc 주문 상세 상태별 일자 수정
     * @param orderDetlVo
     * @return ApiResult
     * @throws Exception
     */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> putOrderDetailDt(OrderDetlVo orderDetlVo) {
		orderDateService.putOrderDetailDt(orderDetlVo);
		return ApiResult.success();
	}


	 /**
	 * @Desc 클레임 상세 상태별 일자 수정
	 * @param claimDetlDtVo
	 * @return ApiResult
	 * @throws Exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> putClaimDetailDt(ClaimDetlDtVo claimDetlDtVo) {
		orderDateService.putClaimDetailDt(claimDetlDtVo);
		return ApiResult.success();
	}
}