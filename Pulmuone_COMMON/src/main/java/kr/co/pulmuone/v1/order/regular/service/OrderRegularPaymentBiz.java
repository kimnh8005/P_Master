package kr.co.pulmuone.v1.order.regular.service;

import kr.co.pulmuone.v1.order.regular.dto.RegularResultPaymentListDto;
import kr.co.pulmuone.v1.order.regular.dto.vo.OrderRegularResultVo;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송주문결제 Biz OrderRegularPaymentBiz
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020.09.24	  김명진           최초작성
 * =======================================================================
 * </PRE>
 */
public interface OrderRegularPaymentBiz {

	/**
	 * 정기배송 결제 대상목록 조회
	 * @return
	 * @throws Exception
	 */
	List<RegularResultPaymentListDto> getRegularOrderResultPaymentList() throws Exception;

	/**
	 * 정기배송 주문 결제 처리
	 * @param regularResultPaymentItem
	 * @throws Exception
	 */
	OrderRegularResultVo procOdRegularPayment(RegularResultPaymentListDto regularResultPaymentItem) throws Exception;
}
