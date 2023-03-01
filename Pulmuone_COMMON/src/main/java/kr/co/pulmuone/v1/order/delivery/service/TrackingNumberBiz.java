package kr.co.pulmuone.v1.order.delivery.service;

import kr.co.pulmuone.v1.order.delivery.dto.vo.ClaimNumberSearchVo;
import kr.co.pulmuone.v1.order.delivery.dto.vo.OrderTrackingNumberVo;

public interface TrackingNumberBiz {


    /**
     * 송장번호 등록
     * @param orderTrackingNumberVo
     * @return
     */
    int addTrackingNumber(OrderTrackingNumberVo orderTrackingNumberVo) throws Exception;

    /**
     * 송장번호 삭제
     * @param orderTrackingNumberVo
     * @return
     */
    int delTrackingNumber(OrderTrackingNumberVo orderTrackingNumberVo) throws Exception;

    /**
     * 취소요청에 대한 클레임 정보 있으면 취소거부로 업데이트
     * @param odOrderDetlId
     * @param userId
     * @return
     * @throws Exception
     */
    void putCancelRequestClaimDenial(String odid, long odClaimId) throws Exception ;

    /**
     * 주문상세번호에 따른 클레임 번호 조회
     * @param odOrderDetlId
     * @return
     */
    ClaimNumberSearchVo getOdClaimId(long odOrderDetlId);

}
