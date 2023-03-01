package kr.co.pulmuone.v1.api.cjlogistics.service;

import kr.co.pulmuone.v1.api.cjlogistics.dto.CJLogisticsOrderAcceptDto;
import kr.co.pulmuone.v1.api.cjlogistics.dto.CJLogisticsTrackingResponseDto;

public interface CJLogisticsBiz {

	/**
	 * CJ 택배 송장조회
	 * @param waybillNumber
	 * @return
	 */
    CJLogisticsTrackingResponseDto getCJLogisticsTrackingList(String waybillNumber);

    /**
     * CJ 택배 주문접수
     * @param vo
     * @return
     * @throws Exception
     */
    int addCJLogisticsOrderAccept(CJLogisticsOrderAcceptDto vo);
}
