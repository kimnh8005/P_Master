package kr.co.pulmuone.v1.order.date.service;


import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.order.claim.dto.vo.ClaimDetlDtVo;
import kr.co.pulmuone.v1.order.claim.dto.vo.ClaimVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDtVo;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 일자 관련 Biz
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
public interface OrderDateBiz {
	/** 주문 상태별 일자 수정 */
	public ApiResult<?> putOrderDt(OrderDtVo orderDtVo);
	/** 주문 상세 상태별 일자 수정 */

	public ApiResult<?> putOrderDetailDt(OrderDetlVo orderDetlVo);

	/** 클레임 상세 상태별 일자 수정 */
	public ApiResult<?> putClaimDetailDt(ClaimDetlDtVo claimDetlDtVo);
}