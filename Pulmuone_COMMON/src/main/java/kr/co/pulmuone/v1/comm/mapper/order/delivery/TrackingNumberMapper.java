package kr.co.pulmuone.v1.comm.mapper.order.delivery;

import kr.co.pulmuone.v1.order.delivery.dto.vo.OrderTrackingNumberVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 송장번호 관련 Mapper
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
@Mapper
public interface TrackingNumberMapper {

    /**
     * 송장번호 등록
     * @param orderTrackingNumberVo
     * @return
     */
    int addTrackingNumber(OrderTrackingNumberVo orderTrackingNumberVo);


    /**
     * 송장번호 삭제
     * @param orderTrackingNumberVo
     * @return
     */
    int delTrackingNumber(OrderTrackingNumberVo orderTrackingNumberVo);

}
