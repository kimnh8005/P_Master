package kr.co.pulmuone.v1.order.regular.service;

import kr.co.pulmuone.v1.comm.mapper.order.regular.OrderRegularPaymentMapper;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDtVo;
import kr.co.pulmuone.v1.order.regular.dto.RegularResultPaymentListDto;
import kr.co.pulmuone.v1.order.regular.dto.vo.OrderRegularReqOrderDetlVo;
import kr.co.pulmuone.v1.order.regular.dto.vo.OrderRegularReqVo;
import kr.co.pulmuone.v1.order.regular.dto.vo.OrderRegularResultDetlVo;
import kr.co.pulmuone.v1.order.regular.dto.vo.OrderRegularResultVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송 주문결제 Service
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

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderRegularPaymentService {

	private final OrderRegularPaymentMapper orderRegularPaymentMapper;

	/**
	 * 정기배송 주문 결제 대상 목록 조회
	 * @return
	 * @throws Exception
	 */
    protected List<RegularResultPaymentListDto> getRegularOrderResultPaymentList(String regularStatusCd) throws Exception {
    	return orderRegularPaymentMapper.getRegularOrderResultPaymentList(regularStatusCd);
    }

	/**
	 * 주문날짜 정보 업데이트
	 * @param orderDtVo
	 * @return
	 * @throws Exception
	 */
	protected int putOrderDtInfo(OrderDtVo orderDtVo) throws Exception {
		return orderRegularPaymentMapper.putOrderDtInfo(orderDtVo);
	}

	/**
	 * 주문 상세 주문상태코드 업데이트
	 * @param orderDetlVo
	 * @return
	 * @throws Exception
	 */
	protected int putOrderDetlInfo(OrderDetlVo orderDetlVo) throws Exception {
		return orderRegularPaymentMapper.putOrderDetlInfo(orderDetlVo);
	}

	/**
	 * 정기배송 결과 테이블 회차 완료 여부 업데이트
	 * @param orderRegularResultVo
	 * @return
	 * @throws Exception
	 */
	protected int putOrderRegularResultReqRoundYnInfo(OrderRegularResultVo orderRegularResultVo) throws Exception {
		return orderRegularPaymentMapper.putOrderRegularResultReqRoundYnInfo(orderRegularResultVo);
	}

	/**
	 * 정기배송 결과 상세 테이블 해지 처리
	 * @param orderRegularResultDetlVo
	 * @return
	 * @throws Exception
	 */
	protected int putOrderRegularDetlStatusCancel(OrderRegularResultDetlVo orderRegularResultDetlVo) throws Exception {
		return orderRegularPaymentMapper.putOrderRegularDetlStatusCancel(orderRegularResultDetlVo);
	}

	/**
	 * 정기배송 결과 테이블 해지 처리
	 * @param orderRegularResultVo
	 * @return
	 * @throws Exception
	 */
	protected int putOrderRegularStatusCancel(OrderRegularResultVo orderRegularResultVo) throws Exception {
		return orderRegularPaymentMapper.putOrderRegularStatusCancel(orderRegularResultVo);
	}

	/**
	 * 정기배송 신청 상세 테이블 해지 처리
	 * @param orderRegularReqOrderDetlVo
	 * @return
	 * @throws Exception
	 */
	protected int putOrderRegularReqDetlStatusCancel(OrderRegularReqOrderDetlVo orderRegularReqOrderDetlVo) throws Exception {
		return orderRegularPaymentMapper.putOrderRegularReqDetlStatusCancel(orderRegularReqOrderDetlVo);
	}

	/**
	 * 정기배송 신청 테이블 해지 처리
	 * @param orderRegularReqVo
	 * @return
	 * @throws Exception
	 */
	protected int putOrderRegularReqStatusCancel(OrderRegularReqVo orderRegularReqVo) throws Exception {
		return orderRegularPaymentMapper.putOrderRegularReqStatusCancel(orderRegularReqVo);
	}

	/**
	 * 정기배송 주문 상세 상품PK 목록 조회
	 * @param odRegularResultId
	 * @return
	 * @throws Exception
	 */
	protected List<Long> getOdRegularOrderDetlGoodsList(long odRegularResultId) {
		return orderRegularPaymentMapper.getOdRegularOrderDetlGoodsList(odRegularResultId);
	}

	/**
	 * 정기배송 결과 상세 테이블 건너뛰기 처리
	 * @param orderRegularResultDetlVo
	 * @return
	 * @throws Exception
	 */
	protected int putOrderRegularDetlStatusSkip(OrderRegularResultDetlVo orderRegularResultDetlVo) throws Exception {
		return orderRegularPaymentMapper.putOrderRegularDetlStatusSkip(orderRegularResultDetlVo);
	}
}
