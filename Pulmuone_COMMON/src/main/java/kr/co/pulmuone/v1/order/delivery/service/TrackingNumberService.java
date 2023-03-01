package kr.co.pulmuone.v1.order.delivery.service;


import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.ClaimEnums;
import kr.co.pulmuone.v1.comm.enums.OrderClaimEnums;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.mapper.order.delivery.OrderBulkTrackingNumberMapper;
import kr.co.pulmuone.v1.comm.mapper.order.delivery.TrackingNumberMapper;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimGoodsInfoDto;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimRegisterRequestDto;
import kr.co.pulmuone.v1.order.claim.service.ClaimProcessBiz;
import kr.co.pulmuone.v1.order.delivery.dto.vo.ClaimNumberSearchVo;
import kr.co.pulmuone.v1.order.delivery.dto.vo.OrderTrackingNumberVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlHistVo;
import kr.co.pulmuone.v1.order.status.service.OrderStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 상세 관련 Service
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 28.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TrackingNumberService {

    private final OrderBulkTrackingNumberMapper orderBulkTrackingNumberMapper;

    @Autowired
    private ClaimProcessBiz claimProcessBiz;

    /**
     * 송장번호 등록
     * @param orderTrackingNumberVo
     * @return
     */
    protected int addTrackingNumber(OrderTrackingNumberVo orderTrackingNumberVo) {
        return orderBulkTrackingNumberMapper.addOrderTrackingNumber(orderTrackingNumberVo);
    }

    /**
     * 송장번호 삭제
     * @param orderTrackingNumberVo
     * @return
     */
    protected int delTrackingNumber(OrderTrackingNumberVo orderTrackingNumberVo) {
        return orderBulkTrackingNumberMapper.delOrderTrackingNumber(orderTrackingNumberVo);
    }

    /**
     * 취소요청에 대한 클레임 정보 있으면 취소거부로 업데이트
     * @param odOrderDetlId
     * @param userId
     * @return
     * @throws Exception
     */
    protected void putCancelRequestClaimDenial(String odid, long odClaimId) throws Exception {

        // odid로 주문정보 조회
        OrderClaimRegisterRequestDto claimReqInfo = orderBulkTrackingNumberMapper.getOrderInfo(odid, odClaimId);

        // 클레임상태구분 취소
        claimReqInfo.setClaimStatusTp(OrderClaimEnums.ClaimStatusTp.CANCEL.getCode());

        // 클레임상태코드 취소거부
        claimReqInfo.setClaimStatusCd(OrderEnums.OrderStatus.CANCEL_DENY_DEFE.getCode());

        List<OrderClaimGoodsInfoDto> goodsInfoList = orderBulkTrackingNumberMapper.getOrderDetlInfoList(odid, odClaimId);
        claimReqInfo.setGoodsInfoList(goodsInfoList);
        claimReqInfo.setUrUserId(String.valueOf(Constants.BATCH_CREATE_USER_ID));
        claimReqInfo.setCustomUrUserId(String.valueOf(Constants.BATCH_CREATE_USER_ID));
        claimReqInfo.setOdClaimId(odClaimId);
        claimReqInfo.setFrontTp(OrderClaimEnums.OrderClaimFrontTpCd.FRONT_TP_BATCH.getCodeValue());
        claimReqInfo.setRejectReasonMsg(ClaimEnums.ClaimReasonMsg.CLAIM_REASON_DI.getCodeName());

        claimProcessBiz.procClaimDenyDefer(OrderClaimEnums.ClaimStatusTp.CANCEL.getCode(), OrderEnums.OrderStatus.CANCEL_DENY_DEFE.getCode(), claimReqInfo, true);

    }

    /**
     * 주문상세번호에 따른 클레임 번호 조회
     * @param odOrderDetlId
     * @return
     */
    protected ClaimNumberSearchVo getOdClaimId(long odOrderDetlId) {
        return orderBulkTrackingNumberMapper.getOdClaimId(odOrderDetlId);
    }

}
