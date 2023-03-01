package kr.co.pulmuone.v1.comm.mapper.api;

import kr.co.pulmuone.v1.api.hitok.dto.vo.HitokDailyDeliveryCancelOrderListVo;
import kr.co.pulmuone.v1.api.hitok.dto.vo.HitokDailyDeliveryRedeliveryOrderListVo;
import kr.co.pulmuone.v1.api.hitok.dto.vo.HitokDailyDeliveryReturnOrderListVo;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 하이톡 API Mapper
 * </PRE>
 */

@Mapper
public interface HitokApiMapper {

    /**
     * 하이톡 반품 데이터 리스트 조회
     * @param odClaimId
     * @return List<HitokDailyDeliveryReturnOrderListVo>
     * @throws BaseException
     */
    List<HitokDailyDeliveryReturnOrderListVo> selectHitokDailyDeliveryReturnOrderList(@Param("odClaimId") long odClaimId) throws BaseException;

    /**
     * 하이톡 일일배송 반품 API 완료 업데이트
     * @param odClaimDetlIdList
     * @throws BaseException
     */
    void putHitokDailyDeliveryReturnOrderCompleteUpdate(@Param("odClaimDetlIdList") List<String> odClaimDetlIdList) throws BaseException;

    /**
     * 하이톡 일일배송 재배솧 원 배송일자 데이터 리스트 조회
     * @param odClaimId
     * @return List<HitokDailyDeliveryCancelOrderListVo>
     * @throws BaseException
     */
    List<HitokDailyDeliveryCancelOrderListVo> selectHitokDailyOriginalRedeliveryOrderList(@Param("odClaimId") long odClaimId) throws BaseException;

    /**
     * 원 주문 취소
     * @param hitokDailyDeliveryCancelOrderListVo
     * @throws BaseException
     */
    void putHitokDailyOriginalOrderCancel(@Param("odOrderDetlDailySchId") String odOrderDetlDailySchId) throws BaseException;

    /**
     * 재배송 클레임 주문에 원주문 수량 더하기
     * @param odOrderDetlDailySchId
     * @param orderCnt
     * @throws BaseException
     */
    void putHitokDailyDeliveryReturnClaimOrderProcess(@Param("odOrderDetlDailySchId") String odOrderDetlDailySchId, @Param("orderCnt") Integer orderCnt) throws BaseException;

    /**
     * 하이톡 재배송 데이터 리스트 조회
     * @param odClaimId
     * @return List<HitokDailyDeliveryRedeliveryOrderListVo>
     * @throws BaseException
     */
    List<HitokDailyDeliveryRedeliveryOrderListVo> selectHitokDailyDeliveryRedeliveryOrderList(@Param("odClaimId") long odClaimId) throws BaseException;

    /**
     * 하이톡 일일배송 재배송 API 완료 업데이트
     * @param odClaimDetlId
     * @throws BaseException
     */
    void putHitokDailyDeliveryRedeliveryOrderCompleteUpdate(@Param("odOrderDetlDailySchId") String odOrderDetlDailySchId) throws BaseException;

}
