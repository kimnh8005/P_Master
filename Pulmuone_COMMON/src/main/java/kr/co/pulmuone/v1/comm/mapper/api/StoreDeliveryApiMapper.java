package kr.co.pulmuone.v1.comm.mapper.api;

import kr.co.pulmuone.v1.api.storedelivery.dto.vo.StoreDeliveryApiListVo;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 매장배송 API Mapper
 * </PRE>
 */

@Mapper
public interface StoreDeliveryApiMapper {

    /**
     * 매장배송 클레임 데이터 확인
     * @param odClaimId
     * @return int
     * @throws BaseException
     */
     int storeDeliveryClaimCheck(@Param("odClaimId") long odClaimId, @Param("orderStatusCd") String orderStatusCd, @Param("claimStatusCd") String claimStatusCd, @Param("claimStatusTp") String claimStatusTp) throws BaseException;

    /**
     * 매장배송 전체주문 클레임 데이터 리스트 조회
     * @param odOrderId
     * @return List<StoreDeliveryListVo>
     * @throws BaseException
     */
    List<StoreDeliveryApiListVo> selectStoreDeliveryAllOrderClaimList(@Param("odOrderId") long odOrderId, @Param("odClaimId") long odClaimId) throws BaseException;

    /**
     * 매장배송 클레임 제외 주문 데이터 리스트 조회
     * @param odOrderId
     * @return List<StoreDeliveryListVo>
     * @throws BaseException
     */
    List<StoreDeliveryApiListVo> selectStoreDeliveryNotClaimOrderList(@Param("odOrderId") long odOrderId, @Param("odClaimId") long odClaimId) throws BaseException;

    /**
     * 매장배송 취소 API 완료 업데이트
     * @param odClaimId
     * @throws BaseException
     */
    void putStoreDeliveryCancelCompleteUpdate(@Param("odClaimId") long odClaimId) throws BaseException;

    /**
     * 매장배송 재배송 주문 API 송신 완료
     * @param odOrderId
     * @throws BaseException
     */
    void putOrderApiCompleteUpdate(@Param("odOrderId") long odOrderId) throws BaseException;

}
