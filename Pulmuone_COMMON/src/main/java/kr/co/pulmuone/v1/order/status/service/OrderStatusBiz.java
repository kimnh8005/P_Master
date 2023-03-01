package kr.co.pulmuone.v1.order.status.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.order.claim.dto.vo.ClaimDetlVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlVo;
import kr.co.pulmuone.v1.order.status.dto.*;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 주문상태 관련 Interface
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 14.     이명수         최초작성
 *  1.1    2021. 01. 11.     김명진         파라미터 변경
 * =======================================================================
 * </PRE>
 */
public interface OrderStatusBiz {

	ApiResult<?>  getOrderDetailStatusInfo(OrderStatusSelectRequestDto orderStatusSelectRequestDto);

	ApiResult<?> putOrderDetailListStatus(OrderStatusUpdateRequestDto orderStatusUpdateRequestDto) throws Exception;

	int putOrderDetailStatus(List<OrderStatusUpdateDto> orderStatusUpdateList);

	int putClaimDetailStatus(ClaimDetlVo claimDetlVo, long userId);

	/**
	 * 취소요청 -> 취소완료 일괄 변경
	 * @param orderClaimStatusRequestDto
	 * @return
	 */
	OrderStatusUpdateResponseDto putClaimCancelReqeustToCancelComplete(OrderClaimStatusRequestDto orderClaimStatusRequestDto) throws Exception;

	void sendOrderGoodsDelivery(List<Long> odOrderDetlIdList, boolean isSend) throws Exception;


	/**
	 * 배송중 업데이트를 위한 주문상세 조회
	 * @param odOrderId
	 * @return
	 */
    //List<String> selectOrderDetailDcList(long odOrderId);
}
