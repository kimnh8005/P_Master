package kr.co.pulmuone.v1.comm.mapper.order.present;

import java.util.List;

import kr.co.pulmuone.v1.order.present.dto.OrderPresentExpiredResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.pulmuone.v1.order.claim.dto.OrderClaimGoodsInfoDto;
import kr.co.pulmuone.v1.order.present.dto.OrderPresentDto;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 선물하기 Mapper
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20210715   	 	홍진영            최초작성
 * =======================================================================
 * </PRE>
 */
@Mapper
public interface OrderPresentMapper {

	/**
	 * 선물하기 데이터 presentId로 조회
	 *
	 * @param odOrderId
	 * @return
	 */
	OrderPresentDto getOrderPresentByPresentId(String presentId) throws Exception;

	/**
	 * 선물하기 데이터 odOrderId로 조회
	 *
	 * @param odOrderId
	 * @return
	 * @throws Exception
	 */
	OrderPresentDto getOrderPresentByOdOrderId(Long odOrderId) throws Exception;

	/**
	 * 선물하기 데이터 presentId, presentAuthCd 로 조회
	 *
	 * @param presentId
	 * @param presentAuthCd
	 * @return
	 */
	OrderPresentDto getOrderPresentByPresentIdAndAuthCode(@Param("presentId") String presentId, @Param("presentAuthCd") String presentAuthCd) throws Exception;

	/**
	 * 선물하기 상태 변경
	 *
	 * @param odOrderId
	 * @param presentOrderStatus
	 * @return
	 * @throws Exception
	 */
	int putPresentOrderStatus(@Param("odOrderId") Long odOrderId, @Param("presentOrderStatus") String presentOrderStatus) throws Exception;

	/**
	 * 선물하기 취소 상품 리스트
	 *
	 * @param odOrderId
	 * @return
	 * @throws Exception
	 */
	List<OrderClaimGoodsInfoDto> getOrderPresentCancelGoodsList(Long odOrderId) throws Exception;

	/**
	 * 유효기간 만료 선물하기 리스트
	 *
	 * @return
	 * @throws Exception
	 */
	List<OrderPresentExpiredResponseDto> getExpiredCancelOrderList() throws Exception;

	/**
	 * 선물 하기 odShippingZoneId 리스트 조회
	 *
	 * @param odOrderId
	 * @return
	 * @throws Exception
	 */
	List<Long> getOrderPresentOdShippingZoneIds(Long odOrderId) throws Exception;

	/**
	 * 메시지 발송 count 업데이트
	 *
	 * @param odOrderId
	 * @return
	 * @throws Exception
	 */
	int putPresentMsgSendCnt(Long odOrderId) throws Exception;
}