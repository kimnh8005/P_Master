package kr.co.pulmuone.v1.comm.mapper.order.registration;

import kr.co.pulmuone.v1.order.order.dto.vo.*;
import org.apache.ibatis.annotations.Mapper;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 데이터 생성 시퀀스 조회 관련 Mapper
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 01. 13.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */
@Mapper
public interface OrderRegistrationSeqMapper {

    long getOrderIdSeq();

    String getOrderNumber(long odOrderId);

    long getOrderDetlSeq();

    long getOrderShippingZoneSeq();

    long getOrderShippingPriceSeq();

    long getPaymentMasterSeq();

    long getOrderDetlDailySeq();



}
