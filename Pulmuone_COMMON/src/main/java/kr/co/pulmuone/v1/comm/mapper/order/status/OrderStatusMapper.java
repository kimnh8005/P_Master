package kr.co.pulmuone.v1.comm.mapper.order.status;

import kr.co.pulmuone.v1.order.claim.dto.vo.ClaimDetlHistVo;
import kr.co.pulmuone.v1.order.claim.dto.vo.ClaimDetlVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlHistVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlVo;
import kr.co.pulmuone.v1.order.status.dto.OrderStatusSelectRequestDto;
import kr.co.pulmuone.v1.order.status.dto.OrderStatusSelectResponseDto;
import kr.co.pulmuone.v1.order.status.dto.OrderStatusUpdateDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 상태 관련 Mapper
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 29.            이명수         최초작성
 *  1.2    2021. 01. 11.            김명진         주문상세 상태 이력 등록 추가
 * =======================================================================
 * </PRE>
 */
@Mapper
public interface OrderStatusMapper {

    /**
     * 주문상태 변경 전에 현재 주문상태 조회
     * @param orderStatusSelectRequestDto
     * @return
     */
    List<OrderStatusSelectResponseDto> getOrderDetailStatusInfo(OrderStatusSelectRequestDto orderStatusSelectRequestDto);

	/**
	 * 주문상세 상태 이력 등록
	 * @param orderDetlHistVo
	 * @return
	 */
	int putOrderDetailStatusHist(OrderDetlHistVo orderDetlHistVo);

	/**
	 * 주문 상세 상태 업데이트
	 * @param orderStatusUpdateDto
	 * @return
	 */
	int putOrderDetailStatus(OrderStatusUpdateDto orderStatusUpdateDto);

	/**
	 * 주문 클레임 상세 상태 이력 등록
	 * @param claimDetlHistVo
	 * @return
	 */
	int putClaimDetailStatusHist(ClaimDetlHistVo claimDetlHistVo);

	/**
	 * 주문 거부 클레임 상세 상태 이력 등록
	 * @param claimDetlHistVo
	 * @return
	 */
	int putDenyDefeClaimDetailStatusHist(ClaimDetlHistVo claimDetlHistVo);

	/**
	 * 주문 클레임 상세 상태 업데이트
	 * @param claimDetlVo
	 * @return
	 */
	int putClaimDetailStatus(ClaimDetlVo claimDetlVo);


	/**
	 * 주문상세 상태 값 조회
	 * @param orderStatusCd
	 * @param orderDetlIdList
	 * @return
	 */
	List<String> selectTargetOverlapOrderStatusList(@Param("orderStatusCd") String orderStatusCd, @Param("orderDetlIdList") List<Long> orderDetlIdList);

	/**
	 * 클레임 상세 상태 값 조회
	 * @param claimStatusCd
	 * @param orderDetlIdList
	 * @return
	 */
	List<String> selectTargetOverlapClaimStatusList(@Param("claimStatusCd") String claimStatusCd, @Param("orderDetlIdList") List<Long> orderDetlIdList);

	/**
	 * 주문IF대상 체크
	 * @param orderDetlIdList
	 * @return
	 */
	int selectOrderInterfaceTargetCheck(@Param("orderDetlIdList") List<Long> orderDetlIdList, @Param("urWarehouseIds")  String[] urWarehouseIds);

	/**
	 * 배송중 업데이트를 위한 주문상세 조회
	 * @param odOrderId
	 * @return
	 */
    //List<String> selectOrderDetailDcList(@Param("odOrderId") long odOrderId);
}
