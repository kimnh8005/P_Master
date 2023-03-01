package kr.co.pulmuone.v1.order.regular.service;

import kr.co.pulmuone.v1.order.regular.dto.RegularResultCreateOrderListDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularResultCreateOrderResponseDto;

import java.util.List;
import java.util.Map;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송주문생성 Biz OrderRegularOrderCreateBiz
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020.09.23	  김명진           최초작성
 * =======================================================================
 * </PRE>
 */
public interface OrderRegularOrderCreateBiz {

	/**
	 * 정기배송 주문생성 대상 상품 목록 조회
	 * @param odRegularResultId
	 * @return
	 */
	List<RegularResultCreateOrderListDto> getRegularOrderResultCreateGoodsList(long odRegularResultId) throws Exception;

	/**
	 * 정기배송 주문 생성 처리
	 * @param regularGoodsList
	 * @param odRegularResultId
	 * @param entryData
	 * @return
	 */
	RegularResultCreateOrderResponseDto createOrderRegular(List<RegularResultCreateOrderListDto> regularGoodsList, long odRegularResultId, Map<String, List<RegularResultCreateOrderListDto>> entryData) throws Exception;
}
