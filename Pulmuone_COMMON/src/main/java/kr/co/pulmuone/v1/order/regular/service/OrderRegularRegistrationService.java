package kr.co.pulmuone.v1.order.regular.service;

import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.mapper.order.regular.OrderRegularRegistrationMapper;
import kr.co.pulmuone.v1.comm.mapper.order.regular.OrderRegularRegistrationSeqMapper;
import kr.co.pulmuone.v1.order.regular.dto.vo.OrderRegularReqHistoryVo;
import kr.co.pulmuone.v1.order.regular.dto.vo.OrderRegularReqMemoVo;
import kr.co.pulmuone.v1.order.regular.dto.vo.OrderRegularReqOrderDetlVo;
import kr.co.pulmuone.v1.order.regular.dto.vo.OrderRegularReqShippingZoneVo;
import kr.co.pulmuone.v1.order.regular.dto.vo.OrderRegularReqVo;
import kr.co.pulmuone.v1.order.regular.dto.vo.OrderRegularResultDetlVo;
import kr.co.pulmuone.v1.order.regular.dto.vo.OrderRegularResultVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021.02.07	김명진		최초작성
 * =======================================================================
 * </PRE>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderRegularRegistrationService {

	private final OrderRegularRegistrationSeqMapper orderRegularRegistrationSeqMapper;

	private final OrderRegularRegistrationMapper orderRegularRegistrationMapper;

	/**
	 * 정기배송 주문신청 SEQ
	 *
	 * @return
	 * @throws Exception
	 */
	protected long getOdRegularReqIdSeq() throws Exception {
		return orderRegularRegistrationSeqMapper.getOdRegularReqIdSeq();
	}

	/**
	 * 정기배송 신청정보 생성
	 *
	 * @param odRegularReqId
	 * @return
	 * @throws Exception
	 */
	protected String getOdRegularReqIdSeq(long odRegularReqId) throws Exception {
		return orderRegularRegistrationSeqMapper.getOdRegularReqId(odRegularReqId);
	}

	/**
	 * 정기배송 주문 신청 등록
	 *
	 * @param orderRegularReqVo
	 * @return
	 * @throws Exception
	 */
	protected int addOrderRegularReq(OrderRegularReqVo orderRegularReqVo) throws Exception {
		return orderRegularRegistrationMapper.addOrderRegularReq(orderRegularReqVo);
	}

	/**
	 * 정기배송 주문 신청 주문상세 SEQ
	 *
	 * @param
	 * @return
	 * @throws Exception
	 */
	protected long getOdRegularReqOrderDetlIdSeq() throws Exception {
		return orderRegularRegistrationSeqMapper.getOdRegularReqOrderDetlIdSeq();
	}

	/**
	 * 정기배송 주문 신청 주문 상세 등록
	 *
	 * @param orderRegularReqOrderDetlVo
	 * @return
	 * @throws Exception
	 */
	protected int addOrderRegularReqOrderDetl(OrderRegularReqOrderDetlVo orderRegularReqOrderDetlVo) throws Exception {
		return orderRegularRegistrationMapper.addOrderRegularReqOrderDetl(orderRegularReqOrderDetlVo);
	}

	/**
	 * 정기배송 주문 신청 주문 상세 중복 조회
     * @param orderRegularReqVo
	 * @return
	 * @throws Exception
	 */
	protected OrderRegularReqOrderDetlVo getOverlapOrderRegularReqOrderDetl(long odRegularReqId, long ilGoodsId, String reqDetailStatusCd) throws Exception {
		return orderRegularRegistrationMapper.getOverlapOrderRegularReqOrderDetl(odRegularReqId, ilGoodsId, reqDetailStatusCd);
	}

	/**
	 * 정기배송 주문 신청 주문 상세 수량 수정
	 * @param orderRegularReqOrderDetlVo
	 * @return
	 * @throws Exception
	 */
	protected int putOrderRegularReqOrderDetlOrderCnt(OrderRegularReqOrderDetlVo orderRegularReqOrderDetlVo) throws Exception {
		return orderRegularRegistrationMapper.putOrderRegularReqOrderDetlOrderCnt(orderRegularReqOrderDetlVo);
	}

	/**
	 * 정기배송 정기배송 주문 신청 배송지 SEQ
	 *
	 * @param
	 * @return
	 * @throws Exception
	 */
	protected long getOdRegularReqShippingZoneIdSeq() throws Exception {
		return orderRegularRegistrationSeqMapper.getOdRegularReqShippingZoneIdSeq();
	}

	/**
	 * 정기배송 주문 신청 배송지 등록
	 *
	 * @param orderRegularReqShippingZoneVo
	 * @return
	 * @throws Exception
	 */
	protected int addOrderRegularReqShippingZone(OrderRegularReqShippingZoneVo orderRegularReqShippingZoneVo) throws Exception {
		return orderRegularRegistrationMapper.addOrderRegularReqShippingZone(orderRegularReqShippingZoneVo);
	}

	/**
	 * 정기배송 주문 신청 히스토리 SEQ
	 *
	 * @param
	 * @return
	 * @throws Exception
	 */
	protected long getOdRegularReqHistoryIdSeq() throws Exception {
		return orderRegularRegistrationSeqMapper.getOdRegularReqHistoryIdSeq();
	}

	/**
	 * 정기배송 주문 신청 히스토리 등록
	 *
	 * @param orderRegularReqHistoryVo
	 * @return
	 * @throws Exception
	 */
	protected int addOrderRegularReqHistory(OrderRegularReqHistoryVo orderRegularReqHistoryVo) throws Exception {
		return orderRegularRegistrationMapper.addOrderRegularReqHistory(orderRegularReqHistoryVo);
	}

	/**
	 * 정기배송 주문 신청 메모 SEQ
	 *
	 * @param
	 * @return
	 * @throws Exception
	 */
	protected long getOdRegularReqMemoIdSeq() throws Exception {
		return orderRegularRegistrationSeqMapper.getOdRegularReqMemoIdSeq();
	}

	/**
	 * 정기배송 주문 신청 메모 등록
     * @param orderRegularReqVo
	 * @return
	 * @throws Exception
	 */
	protected int addOrderRegularReqMemo(OrderRegularReqMemoVo orderRegularReqMemoVo) throws Exception {
		return orderRegularRegistrationMapper.addOrderRegularReqMemo(orderRegularReqMemoVo);
	}

	/**
	 * 정기배송 주문 결과 SEQ
	 *
	 * @param
	 * @return
	 * @throws Exception
	 */
	protected long getOdRegularResultIdSeq() throws Exception {
		return orderRegularRegistrationSeqMapper.getOdRegularResultIdSeq();
	}

	/**
	 * 정기배송 주문 결과 등록
     * @param orderRegularReqVo
	 * @return
	 * @throws Exception
	 */
	protected int addOrderRegularResult(OrderRegularResultVo orderRegularResultVo) throws Exception {
		return orderRegularRegistrationMapper.addOrderRegularResult(orderRegularResultVo);
	}

	/**
	 * 정기배송 주문 결과 상세 SEQ
	 *
	 * @param
	 * @return
	 * @throws Exception
	 */
	protected long getOdRegularResultDetlIdSeq() throws Exception {
		return orderRegularRegistrationSeqMapper.getOdRegularResultDetlIdSeq();
	}

	/**
	 * 정기배송 주문 결과 상세 등록
     * @param orderRegularResultDetlVo
	 * @return
	 * @throws	Exception
	 */
	protected int addOrderRegularResultDetl(OrderRegularResultDetlVo orderRegularResultDetlVo) throws Exception {
		return orderRegularRegistrationMapper.addOrderRegularResultDetl(orderRegularResultDetlVo);
	}
}
