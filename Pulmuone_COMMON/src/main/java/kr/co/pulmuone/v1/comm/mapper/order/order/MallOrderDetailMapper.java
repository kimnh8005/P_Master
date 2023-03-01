package kr.co.pulmuone.v1.comm.mapper.order.order;

import kr.co.pulmuone.v1.goods.goods.dto.vo.ShippingInfoByOdOrderIdResultVo;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimAttcInfoDto;
import kr.co.pulmuone.v1.order.order.dto.mall.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <PRE>
 * Forbiz Korea
 * 프론트 주문상세 리스트 관련 Mapper
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 01. 12.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */
@Mapper
public interface MallOrderDetailMapper {
	/**
	 * @Desc 주문 조회
	 * @param odOrderId(주문 PK)
	 * @return OrderVo
	 */
	MallOrderDto getOrder(@Param("odOrderId") long odOrderId, @Param("urUserId") String urUserId, @Param("guestCi") String guestCi);


	Map<String, Object> getOrderClaim(@Param("odClaimId") long odClaimId, @Param("urUserId") String urUserId, @Param("guestCi") String guestCi);

	/**
	 * @Desc 주문 정기배송 조회
	 * @param odOrderId(주문 PK)
	 * @return MallOrderRegularDto
	 */
	MallOrderRegularDto getRegular(long odOrderId);


	/**
     * @Desc 주문상세 목록  조회
     * @param odOrderId(주문 PK)
     * @return List<MallOrderDetailListDto>
     */
	List<MallOrderDetailGoodsDto> getOrderDetailGoodsList(long odOrderId);

	/**
	 * 주문 클레임 상세 목록 조회
	 * @param odClaimId
	 * @return
	 */
	List<MallOrderDetailGoodsDto> getClaimDetailGoodsList(@Param("odOrderId") long odOrderId,@Param("odClaimId") long odClaimId);



	/**
	 * @Desc 주문 상세 배송지 정보
	 * @param odOrderId
	 * @return MallOrderDetailShippingZoneDto
	 */
	MallOrderDetailShippingZoneDto getOrderDetailShippingInfo(long odOrderId);

	/**
	 * @Desc 주문 상세 결제 정보
	 * @param odOrderId
	 * @return MallOrderDetailPayDto
	 */
	MallOrderDetailPayResultDto getOrderDetailPayInfo(long odOrderId);



	/**
	 * @Desc 주문 상세 주문 취소 / 반품 신청 내역
	 * @param odOrderId
	 * @return List<MallOrderDetailClaimListDto>
	 */
	List<MallOrderDetailPayDiscountDto> getOrderDetailDiscountList(long odOrderId);

	/**
	 * @Desc 주문 상세 주문 취소 / 반품 신청 내역
	 * @param odOrderId
	 * @return List<MallOrderDetailClaimListDto>
	 */
	List<MallOrderDetailClaimListDto> getOrderDetailClaimList(long odOrderId);

	/**
	 * 주문 클레임 상세 반품 수거지
	 * @param ocClaimId
	 * @return
	 */
	MallClaimSendShippingZoneDto getClaimShippingZone(long ocClaimId);

	/**
	 * 주문 클레임 정보
	 * @param ocClaimId
	 * @return
	 */
	MallClaimInfoDto getClaimInfo(long ocClaimId);

	/**
	 * 클레임 첨부파일 목록 정보
	 * @param odClaimId
	 * @return
	 */
	List<OrderClaimAttcInfoDto> getClaimAttcList(long odClaimId);


	/**
	 * 클레임 결제정보
	 * @param odClaimId
	 * @return
	 */
	MallOrderDetailPayResultDto getClaimDetailPayInfo(@Param("odOrderId")long odOrderId, @Param("odClaimId") long odClaimId);

	/**
	 * 주문배송지PK로 주문정보 조회
	 * @param odShippingZoneId
	 * @return List<OrderDetailByOdShippingZondIdResultDto>
	 */
	List<OrderDetailByOdShippingZondIdResultDto> getOrderDetailInfoByOdShippingZoneId(Long odShippingZoneId);

	/**
	 * 주문PK로 배송정책별 주문정보 조회
	 * @param odOrderId
	 * @return List<ShippingInfoByOdOrderIdResultVo>
	 */
	List<ShippingInfoByOdOrderIdResultVo> getShippingInfoByOdOrderId(Long odOrderId);

	/**
	 * 주문PK로 상품PK별 주문정보 조회
	 * @param odOrderId
	 * @return List<ShippingInfoByOdOrderIdResultVo>
	 */
	List<ShippingInfoByOdOrderIdResultVo> getOrderDetailInfoByOdOrderId(Long odOrderId);

	/**
	 * @Desc 주문상세PK로 주문정보 조회
	 * @param odOrderDetlId
	 * @return MallOrderDto
	 */
	MallOrderDto getOrderInfoByOdOrderDetlId(Long odOrderDetlId);

	/**
	 * @Desc 배송가능여부 체크 위한 주문정보 조회
	 * @param odOrderId
	 * @return List<HashMap>
	 */
	List<HashMap> getOrderInfoForShippingPossibility(@Param("odOrderId")Long odOrderId, @Param("odOrderDetlId")Long odOrderDetlId);

}