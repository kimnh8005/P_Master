package kr.co.pulmuone.v1.comm.mapper.order.regular;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.pulmuone.v1.order.regular.dto.vo.OrderRegularReqHistoryVo;
import kr.co.pulmuone.v1.order.regular.dto.vo.OrderRegularReqMemoVo;
import kr.co.pulmuone.v1.order.regular.dto.vo.OrderRegularReqOrderDetlVo;
import kr.co.pulmuone.v1.order.regular.dto.vo.OrderRegularReqShippingZoneVo;
import kr.co.pulmuone.v1.order.regular.dto.vo.OrderRegularReqVo;
import kr.co.pulmuone.v1.order.regular.dto.vo.OrderRegularResultDetlVo;
import kr.co.pulmuone.v1.order.regular.dto.vo.OrderRegularResultVo;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송 주문 등록 Mapper
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 02. 07.	김명진 		최초작성
 * =======================================================================
 * </PRE>
 */
@Mapper
public interface OrderRegularRegistrationMapper {

	/**
	 * 정기배송 주문 신청 등록
	 * @param orderRegularReqVo
	 * @return
	 * @throws Exception
	 */
    int addOrderRegularReq(OrderRegularReqVo orderRegularReqVo) throws Exception;

    /**
     * 정기배송 주문 신청 주문 상세 등록
     * @param orderRegularReqVo
     * @return
     * @throws Exception
     */
    int addOrderRegularReqOrderDetl(OrderRegularReqOrderDetlVo orderRegularReqOrderDetlVo) throws Exception;

    /**
     * 정기배송 주문 신청 주문 상세 중복 조회
     * @param odRegularReqId
     * @param ilGoodsId
     * @return
     */
    OrderRegularReqOrderDetlVo getOverlapOrderRegularReqOrderDetl(@Param("odRegularReqId") long odRegularReqId, @Param("ilGoodsId") long ilGoodsId, @Param("reqDetailStatusCd") String reqDetailStatusCd);

    /**
     * 정기배송 주문 신청 주문 상세 수량 수정
     * @param orderRegularReqOrderDetlVo
     * @return
     */
    int putOrderRegularReqOrderDetlOrderCnt(OrderRegularReqOrderDetlVo orderRegularReqOrderDetlVo);

    /**
     * 정기배송 주문 신청 배송지 등록
     * @param orderRegularReqVo
     * @return
     * @throws Exception
     */
    int addOrderRegularReqShippingZone(OrderRegularReqShippingZoneVo orderRegularReqShippingZoneVo) throws Exception;

    /**
     * 정기배송 주문 신청 히스토리 등록
     * @param orderRegularReqVo
     * @return
     * @throws Exception
     */
    int addOrderRegularReqHistory(OrderRegularReqHistoryVo orderRegularReqHistoryVo) throws Exception;

    /**
     * 정기배송 주문 신청 메모 등록
     * @param orderRegularReqVo
     * @return
     * @throws Exception
     */
    int addOrderRegularReqMemo(OrderRegularReqMemoVo orderRegularReqMemoVo) throws Exception;

    /**
     * 정기배송 주문 결과 등록
     * @param orderRegularReqVo
     * @return
     * @throws Exception
     */
    int addOrderRegularResult(OrderRegularResultVo orderRegularResultVo) throws Exception;

    /**
     * 정기배송 주문 결과 상세 등록
     * @param orderRegularResultDetlVo
     * @return
     * @throws Exception
     */
    int addOrderRegularResultDetl(OrderRegularResultDetlVo orderRegularResultDetlVo) throws Exception;
}
