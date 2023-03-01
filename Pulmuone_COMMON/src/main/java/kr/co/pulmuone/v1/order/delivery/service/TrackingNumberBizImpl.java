package kr.co.pulmuone.v1.order.delivery.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.order.delivery.dto.vo.ClaimNumberSearchVo;
import kr.co.pulmuone.v1.order.delivery.dto.vo.OrderTrackingNumberVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 송장 번호 관련 Implements
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 02. 18.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */
@Slf4j
@Service
public class TrackingNumberBizImpl implements TrackingNumberBiz {

	@Autowired
	private TrackingNumberService trackingNumberService;


	@Override
	public int addTrackingNumber(OrderTrackingNumberVo orderTrackingNumberVo) throws Exception {
		return trackingNumberService.addTrackingNumber(orderTrackingNumberVo);
	}

	@Override
	public int delTrackingNumber(OrderTrackingNumberVo orderTrackingNumberVo) throws Exception {
		return trackingNumberService.delTrackingNumber(orderTrackingNumberVo);
	}

	/**
	 * 취소요청에 대한 클레임 정보 있으면 취소거부로 업데이트
	 * @param odOrderDetlId
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@Override
	public void putCancelRequestClaimDenial(String odid, long odClaimId) throws Exception {
		trackingNumberService.putCancelRequestClaimDenial(odid, odClaimId);
	}

	/**
	 * 주문상세번호에 따른 클레임 번호 조회
	 * @param odOrderDetlId
	 * @return
	 */
	@Override
	public ClaimNumberSearchVo getOdClaimId(long odOrderDetlId) {
		return trackingNumberService.getOdClaimId(odOrderDetlId);
	}

}
