package kr.co.pulmuone.v1.comm.mapper.order.order;

import org.apache.ibatis.annotations.Mapper;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 상세 상태 관련 Mapper
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
@Mapper
public interface OrderDetailStatusMapper {

    /**
     * 주문 상세 상태 값 업데이트
     * @param odOrderDetlId
     * @param orderStatusCd
     * @return
     */
    int putOrderDetailStatusChange(long odOrderDetlId, String orderStatusCd);


}
