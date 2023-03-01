package kr.co.pulmuone.v1.comm.mapper.order.claim;

import kr.co.pulmuone.v1.order.claim.dto.vo.ClaimOrderStatusVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 클레임 validation Mapper
 * </PRE>
 */
@Mapper
public interface ClaimValidationMapper {

    /**
     * 주문 상태값 조회
     * @param odOrderDetlIds
     * @return List<ClaimOrderStatusVo>
     * @throws Exception
     */
    List<ClaimOrderStatusVo> selectOrderStatusList(@Param("odOrderDetlIds") List<Long> odOrderDetlIds) throws Exception;

    /**
     * 클레임 상태값 조회
     * @param odOrderDetlIds
     * @param odClaimId
     * @return List<ClaimOrderStatusVo>
     * @throws Exception
     */
    List<ClaimOrderStatusVo> selectClaimStatusList(@Param("odClaimDetlIds") List<Long> odClaimDetlIds, @Param("odClaimId") long odClaimId) throws Exception;

    /**
     * 클레임 수량 조회
     * @param odOrderDetlId
     * @param claimCnt
     * @return int
     * @throws Exception
     */
    int selectClaimCnt(@Param("odOrderDetlId") long odOrderDetlId, @Param("claimCnt") int claimCnt) throws Exception;

}
