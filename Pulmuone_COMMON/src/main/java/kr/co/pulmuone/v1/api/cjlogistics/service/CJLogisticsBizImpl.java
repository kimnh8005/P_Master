package kr.co.pulmuone.v1.api.cjlogistics.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.pulmuone.v1.api.cjlogistics.dto.CJLogisticsOrderAcceptDto;
import kr.co.pulmuone.v1.api.cjlogistics.dto.CJLogisticsTrackingResponseDto;


/**
* <PRE>
* Forbiz Korea
* CJ택배 BizImpl
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 9. 22.                손진구          최초작성
* =======================================================================
* </PRE>
*/
@Service
public class CJLogisticsBizImpl implements CJLogisticsBiz {

    @Autowired
    CJLogisticsService cJLogisticsService;

    /**
     * @Desc CJ 송장번호 트래킹 API
     * @param waybillNumber
     * @return CJLogisticsTrackingResponseDto
     */
    @Override
    public CJLogisticsTrackingResponseDto getCJLogisticsTrackingList(String waybillNumber){
        return cJLogisticsService.getCJLogisticsTrackingList(waybillNumber);
    }

    /**
     * @Desc CJ 주문접수 API
     * @param dto
     * @return int
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int addCJLogisticsOrderAccept(CJLogisticsOrderAcceptDto dto){
    	return cJLogisticsService.addCJLogisticsOrderAccept(dto);
    }
}
