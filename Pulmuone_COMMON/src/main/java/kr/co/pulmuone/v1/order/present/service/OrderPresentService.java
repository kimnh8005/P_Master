package kr.co.pulmuone.v1.order.present.service;

import java.util.List;

import kr.co.pulmuone.v1.order.present.dto.OrderPresentExpiredResponseDto;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.enums.OrderEnums.OrderPresentErrorCode;
import kr.co.pulmuone.v1.comm.enums.OrderEnums.PresentOrderStatus;
import kr.co.pulmuone.v1.comm.mapper.order.present.OrderPresentMapper;
import kr.co.pulmuone.v1.comm.util.SHA256Util;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimGoodsInfoDto;
import kr.co.pulmuone.v1.order.present.dto.OrderPresentDto;
import lombok.RequiredArgsConstructor;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 선물하기 Service
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

@Service
@RequiredArgsConstructor
public class OrderPresentService {

	private final OrderPresentMapper orderPresentMapper;

	private String ORDER_GIFT_ID_ENCOD_SALT = "present";

	/**
	 * 선물하기 presentId 생성
	 */
	protected String makePresentId(Long odOrderId) throws Exception {
		return SHA256Util.get(Long.toString(odOrderId), ORDER_GIFT_ID_ENCOD_SALT);
	}

	/**
	 * 선물하기 데이터 presentId로 조회
	 *
	 * @param presentId
	 * @return
	 */
	protected OrderPresentDto getOrderPresentByPresentId(String presentId) throws Exception {
		return orderPresentMapper.getOrderPresentByPresentId(presentId);
	}

	/**
	 * 선물하기 데이터 presentId, presentAuthCd 로 조회
	 *
	 * @param presentId
	 * @param presentAuthCd
	 * @return
	 */
	protected OrderPresentDto getOrderPresentByPresentIdAndAuthCode(String presentId, String presentAuthCd)
			throws Exception {
		return orderPresentMapper.getOrderPresentByPresentIdAndAuthCode(presentId, presentAuthCd);
	}

	/**
	 * 선물하기 데이터 presentId, odOrderId 검증
	 *
	 * @param presentId
	 * @param odOrderId
	 * @return
	 * @throws Exception
	 */
	protected boolean isOrderPresentAuth(String presentId, Long odOrderId) throws Exception {
		try {
			return makePresentId(odOrderId).equals(presentId);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 선물하기 데이터 odOrderId로 조회
	 *
	 * @param odOrderId
	 * @return
	 * @throws Exception
	 */
	protected OrderPresentDto getOrderPresentByOdOrderId(Long odOrderId) throws Exception {
		return orderPresentMapper.getOrderPresentByOdOrderId(odOrderId);
	}

	/**
	 * 선물하기 거절 가능한 상태 체크
	 *
	 * @param odOrderId
	 * @return
	 */
	protected OrderPresentErrorCode isCheckPresentOrderStatusReject(Long odOrderId) throws Exception {
		OrderPresentDto orderPresentDto = getOrderPresentByOdOrderId(odOrderId);
		if (PresentOrderStatus.WAIT.getCode().equals(orderPresentDto.getPresentOrderStatus())) {
			return OrderPresentErrorCode.SUCCESS;
		} else {
			return returnOrderPresentErrorCodeByPresentOrderStatus(orderPresentDto.getPresentOrderStatus());
		}
	}

	private OrderPresentErrorCode returnOrderPresentErrorCodeByPresentOrderStatus(String presentOrderStatus) {
		if (PresentOrderStatus.REJECT.getCode().equals(presentOrderStatus)) {
			return OrderPresentErrorCode.FAIL_IMPOSSIBLE_STATUS_REJECT;
		} else if (PresentOrderStatus.EXPIRED.getCode().equals(presentOrderStatus)) {
			return OrderPresentErrorCode.FAIL_IMPOSSIBLE_STATUS_EXPIRED;
		} else if (PresentOrderStatus.RECEIVE_COMPLET.getCode().equals(presentOrderStatus)) {
			return OrderPresentErrorCode.FAIL_IMPOSSIBLE_STATUS_RECEIVE_COMPLET;
		} else if (PresentOrderStatus.CANCEL.getCode().equals(presentOrderStatus)) {
			return OrderPresentErrorCode.FAIL_IMPOSSIBLE_STATUS_CANCEL;
		}
		return null;
	}

	/**
	 * 선물하기 거절 상태 변경
	 *
	 * @param odOrderId
	 * @return
	 */
	protected int putPresentOrderStatusReject(Long odOrderId) throws Exception {
		return orderPresentMapper.putPresentOrderStatus(odOrderId, PresentOrderStatus.REJECT.getCode());
	}

	/**
	 * 유효기간 만료 선물하기 리스트
	 *
	 * @return
	 * @throws Exception
	 */
	protected List<OrderPresentExpiredResponseDto> getexpiredCancelOrderList() throws Exception {
		return orderPresentMapper.getExpiredCancelOrderList();
	}

	/**
	 * 선물 유효기간 만료 취소 가능 상태 체크
	 *
	 * @param odOrderId
	 * @return
	 */
	protected OrderPresentErrorCode isCheckPresentOrderStatusExpired(Long odOrderId) throws Exception {
		OrderPresentDto orderPresentDto = getOrderPresentByOdOrderId(odOrderId);
		if (PresentOrderStatus.WAIT.getCode().equals(orderPresentDto.getPresentOrderStatus())) {
			return OrderPresentErrorCode.SUCCESS;
		} else {
			return returnOrderPresentErrorCodeByPresentOrderStatus(orderPresentDto.getPresentOrderStatus());
		}
	}

	/**
	 * 선물하기 유효기간 만료 상태 변경
	 *
	 * @param odOrderId
	 * @return
	 */
	protected int putPresentOrderStatusExpired(Long odOrderId) throws Exception {
		return orderPresentMapper.putPresentOrderStatus(odOrderId, PresentOrderStatus.EXPIRED.getCode());
	}

	/**
	 * 선물하기 취소 상품 리스트
	 *
	 * @param odOrderId
	 * @return
	 * @throws Exception
	 */
	protected List<OrderClaimGoodsInfoDto> getOrderPresentCancelGoodsList(Long odOrderId) throws Exception {
		return orderPresentMapper.getOrderPresentCancelGoodsList(odOrderId);
	}

	/**
	 * 선물 받기 가능 상태 체크
	 *
	 * @param odOrderId
	 * @return
	 */
	protected OrderPresentErrorCode isCheckPresentOrderStatusReceive(Long odOrderId) throws Exception {
		OrderPresentDto orderPresentDto = getOrderPresentByOdOrderId(odOrderId);
		if (PresentOrderStatus.WAIT.getCode().equals(orderPresentDto.getPresentOrderStatus())) {
			return OrderPresentErrorCode.SUCCESS;
		} else {
			return returnOrderPresentErrorCodeByPresentOrderStatus(orderPresentDto.getPresentOrderStatus());
		}
	}

	/**
	 * 선물 하기 odShippingZoneId 리스트 조회
	 *
	 * @param odOrderId
	 * @return
	 * @throws Exception
	 */
	protected List<Long> getOrderPresentOdShippingZoneIds(Long odOrderId) throws Exception {
		return orderPresentMapper.getOrderPresentOdShippingZoneIds(odOrderId);
	}

	/**
	 * 선물하기 유효기간 만료 상태 변경
	 *
	 * @param odOrderId
	 * @return
	 */
	protected int putPresentOrderStatusReceive(Long odOrderId) throws Exception {
		return orderPresentMapper.putPresentOrderStatus(odOrderId, PresentOrderStatus.RECEIVE_COMPLET.getCode());
	}

	/**
	 * 선물하기 메세지 재발송 가능 상태 체크
	 *
	 * @param odOrderId
	 * @return
	 * @throws Exception
	 */
	protected boolean isCheckPresentOrderStatusReSendMessage(Long odOrderId) throws Exception {
		OrderPresentDto orderPresentDto = getOrderPresentByOdOrderId(odOrderId);
		return PresentOrderStatus.WAIT.getCode().equals(orderPresentDto.getPresentOrderStatus());
	}

	/**
	 * 메시지 발송 count 업데이트
	 *
	 * @param odOrderId
	 * @return
	 * @throws Exception
	 */
	protected int putPresentMsgSendCnt(Long odOrderId) throws Exception {
		return orderPresentMapper.putPresentMsgSendCnt(odOrderId);
	}

	/**
	 * 선물하기 취소 가능 상태 체크
	 *
	 * @param odOrderId
	 * @return
	 * @throws Exception
	 */
	protected boolean isCheckPresentOrderStatusCancel(Long odOrderId) throws Exception {
		OrderPresentDto orderPresentDto = getOrderPresentByOdOrderId(odOrderId);
		return PresentOrderStatus.WAIT.getCode().equals(orderPresentDto.getPresentOrderStatus())
				|| PresentOrderStatus.RECEIVE_COMPLET.getCode().equals(orderPresentDto.getPresentOrderStatus());
	}

	/**
	 * 선물하기 취소 상태 변경
	 *
	 * @param odOrderId
	 * @return
	 */
	protected int putPresentOrderStatusCancel(Long odOrderId) throws Exception {
		return orderPresentMapper.putPresentOrderStatus(odOrderId, PresentOrderStatus.CANCEL.getCode());
	}
}