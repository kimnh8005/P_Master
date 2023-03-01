package kr.co.pulmuone.v1.order.present.service;

import java.util.List;

import kr.co.pulmuone.v1.comm.enums.OrderEnums.OrderPresentErrorCode;
import kr.co.pulmuone.v1.order.present.dto.OrderPresentDto;
import kr.co.pulmuone.v1.order.present.dto.OrderPresentExpiredResponseDto;
import kr.co.pulmuone.v1.order.present.dto.OrderPresentReceiveRequestDto;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 선물하기 Interface
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		20210715   	 	홍진영            최초작성
 * =======================================================================
 * </PRE>
 */

public interface OrderPresentBiz {

	/**
	 * 선물하기 presentId 생성
	 */
	public String makePresentId(Long odOrderId) throws Exception;

	/**
	 * 선물하기 데이터 presentId로 조회
	 *
	 * @param presentId
	 * @return
	 */
	public OrderPresentDto getOrderPresentByPresentId(String presentId) throws Exception;

	/**
	 * 선물하기 데이터 presentId, presentAuthCd 로 조회
	 *
	 * @param presentId
	 * @param presentAuthCd
	 * @return
	 */
	public OrderPresentDto getOrderPresentByPresentIdAndAuthCode(String presentId, String presentAuthCd)
			throws Exception;

	/**
	 * 선물하기 데이터 presentId, odOrderId 검증
	 *
	 * @param presentId
	 * @param odOrderId
	 * @return
	 */
	public boolean isOrderPresentAuth(String presentId, Long odOrderId) throws Exception;

	/**
	 * 선물 거절
	 *
	 * @param odOrderId
	 * @return
	 * @throws Exception
	 */
	public OrderPresentErrorCode reject(Long odOrderId, String rejectUrUserId) throws Exception;

	/**
	 * 유효기간 만료 선물하기 리스트
	 *
	 * @return
	 * @throws Exception
	 */
	List<OrderPresentExpiredResponseDto> getExpiredCancelOrderList() throws Exception;

	/**
	 * 유효기간 만료 취소
	 *
	 * @param odOrderId
	 * @return
	 * @throws Exception
	 */
	public OrderPresentErrorCode expiredCancel(Long odOrderId) throws Exception;

	/**
	 * 선물 받기
	 *
	 * @param odOrderId
	 * @return
	 * @throws Exception
	 */
	public OrderPresentErrorCode receive(OrderPresentReceiveRequestDto reqDto, Long receiveUrUserId) throws Exception;

	/**
	 * 선물하기 메세지 재발송
	 *
	 * @param odid
	 * @param urlType
	 * @return
	 * @throws Exception
	 */
	public OrderPresentErrorCode reSendMessage(String odid, String urlType) throws Exception;

	/**
	 * 선물하기 취소
	 *
	 * @param odOrderId
	 * @return
	 * @throws Exception
	 */
	public OrderPresentErrorCode buyerCancel(Long odOrderId) throws Exception;

	/**
	 * 선물하기 조회
	 *
	 * @param odOrderId
	 * @return
	 * @throws Exception
	 */
	public OrderPresentDto getOrderPresentByOdOrderId(Long odOrderId) throws Exception;
}