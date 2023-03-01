package kr.co.pulmuone.v1.comm.mapper.order.order;

import java.util.List;


import kr.co.pulmuone.v1.order.order.dto.*;
import kr.co.pulmuone.v1.order.order.dto.mall.OrderDetailGoodsListMallDto;
import kr.co.pulmuone.v1.order.claim.dto.vo.ClaimAttcVo;
import kr.co.pulmuone.v1.order.order.dto.vo.*;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 상세 관련 Mapper
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 28.            이명수         최초작성
 *  1.1    2021. 01. 12.            이규한         주문상세 동일 출고처 주문상세ID 리스트 조회 추가
 * =======================================================================
 * </PRE>
 */
@Mapper
public interface OrderDetailMapper {

    /**
     * 주문 상세 상품 리스트 조회
     * @param odOrderId
     * @return
     */
    List<OrderDetailGoodsListDto> getOrderDetailGoodsList(String odOrderId);

    /**
     * 주문 상세 클레임 상품 리스트 조회
     * @param odOrderId
     * @return
     */
    List<OrderDetailGoodsListDto> getOrderDetailClaimGoodsList(String odOrderId);

    /**
     * @Desc 주문 상세 클레임 상품정보 조회 > 첨부파일보기 조회
     * @param odClaimId
     * @return List<ClaimAttcVo>
     */
    List<ClaimAttcVo> getOrderClaimAttc(long odClaimId);
    /**
     * 주문 상세 주문상세 결제정보(결제상세정보) 리스트 조회
     * @param odOrderId
     * @return
     */
    List<OrderDetailPayDetlListDto> getOrderDetailPayDetlList(String odOrderId);

    /**
     * 주문 상세 주문상세 결제정보(결제 정보) 리스트 조회
     * @param odOrderId
     * @return
     */
    List<OrderDetailPayListDto> getOrderDetailPayList(String odOrderId);

    /**
     * 주문 상세 주문상세 결제정보(환불 정보) 리스트 조회
     * @param odOrderId
     * @return
     */
    List<OrderDetailRefundListDto> getOrderDetailRefundList(String odOrderId);

    /**
     * 주문 상세 쿠폰할인 리스트 조회
     * @param odOrderId
     * @return
     */
    List<OrderDetailDiscountListDto> getOrderDetailDiscountList(String odOrderId);

	/**
	 * 주문 상세 클레임 회수 정보 조회
	 * @param odOrderId
	 * @return
	 */
	List<OrderDetailClaimCollectionListDto> getOrderDetailClaimCollectionList(String odOrderId);

    /**
     * 주문 상세 상담 리스트 조회
     * @param odOrderId
     * @return
     */
    List<OrderDetailConsultListDto> getOrderDetailConsultList(@Param("odOrderId") String odOrderId, @Param("batchCreateUserId") long batchCreateUserId);

    /**
     * 주문 상세 상담 등록
     * @param orderDetailConsultRequestDto
     * @return
     */
    int addOrderDetailConsult(OrderDetailConsultRequestDto orderDetailConsultRequestDto);

    /**
     * 주문 상세 상담 수정
     * @param orderDetailConsultRequestDto
     * @return
     */
    int putOrderDetailConsult(OrderDetailConsultRequestDto orderDetailConsultRequestDto);

    /**
     * 주문 상세 상담 삭제
     * @param odConsultId
     * @return
     */
    int delOrderDetailConsult(String odConsultId);

    /**
     * 주문 상세 처리이력 리스트 조회
     *
     * @param orderDetailHistoryRequestDto
     * @return
     */
    List<OrderDetailHistoryListDto> getOrderDetailHistoryList(OrderDetailHistoryRequestDto orderDetailHistoryRequestDto);

    /**
     * 주문 상세 상품 리스트 Mall 조회
     * @param odOrderId
     * @return
     */
    List<OrderDetailGoodsListMallDto> getOrderDetailGoodsMallList(String odOrderId);

    /**
     * 주문상세 동일 출고처 주문상세ID 리스트 조회
     * @param odOrderDetlId (주문상세 PK)
     * @return List<Long> (주문상세PK List)
     */
	List<Long> getUrWarehouseOdOrderDetlIdList(Long odOrderDetlId);

	/**
	 * @Desc 주문 상세 주문자정보 리스트 조회
	 * @param odOrderId
	 * @return OrderBuyerDto
	 */
    OrderBuyerDto getOrderBuyer(String odOrderId);

	/**
	 * @Desc 주문상세 배송정보 리스트 조회
	 * @param odOrderId
	 * @return List<OrderDetlShippingZoneVo>
	 */
	List<OrderDetlShippingZoneVo> getOrderDetailShippingZoneList(@Param("odOrderId") String odOrderId);

	/**
	 * @Desc 주문상세 일일배송 스케쥴 배송정보 리스트 조회
	 * @param odShippingZoneId
	 * @return OrderDetlShippingZoneVo
	 */
	OrderDetlShippingZoneVo getOrderDetailDailySchShippingZone(@Param("odShippingZoneId")Long odShippingZoneId);

	List<OrderDetlShippingZoneVo> getOrderDetailShippingZoneList(@Param("odOrderId") String odOrderId, @Param("odShippingZoneId") String odShippingZoneId);

	/**
	 * @Desc 주문복사에서 배송정보 리스트 조회
	 * @param odOrderId
	 * @return
	 */
	List<OrderDetlCopyShippingZoneDto> getOrderDetailCopyShippingZoneList(@Param("odOrderId") String odOrderId);

	/**
	 * @Desc 주문 상세 변경내역 조회 (변경 정보)
	 * @param odShippingZoneId
	 * @return List<OrderDetlShippingZoneVo>
	 */
	List<OrderDetlShippingZoneHistVo> getOrderShippingZoneHistList(String odShippingZoneId);

	/**
	 * @Desc 주문 상세 변경내역 조회 (수취 정보)
	 * @param odShippingZoneId
	 * @return List<OrderDetlShippingZoneVo>
	 */
	List<OrderDetlShippingZoneVo> getOrderShippingZoneByOdShippingZoneId(String odShippingZoneId);

	/**
	 * @Desc 주문 상세 결제 정보 > 즉시할인내역 조회
	 * @param odOrderId
	 * @return List<OrderDetlDirectDiscountVo>
	 */
	List<OrderDetlDirectDiscountVo> getOrderDirectDiscountList(String odOrderId);

	/**
	 * @Desc 주문PK,쿠폰발급PK로 배송비쿠폰재발급가능한지여부 확인
	 * @param odOrderId
	 * @param pmCouponIssueId
	 * @return int
	 */
	int isPossibilityReissueCouponInOdshippingPrice(@Param("odOrderId")Long odOrderId, @Param("pmCouponIssueId")Long pmCouponIssueId);

	/**
	 * @Desc 주문PK, 쿠폰발급PK로 쿠폰재발급가능한지여부 확인
	 * @param odOrderId
	 * @param pmCouponIssueId
	 * @return int
	 */
	int isPossibilityReissueCouponInOdOrderDetl(@Param("odOrderId")Long odOrderId, @Param("pmCouponIssueId")Long pmCouponIssueId);

	/**
	 * @Desc 주문PK, 쿠폰발급PK로 장바구니쿠폰재발급가능한지여부 확인
	 * @param odOrderId
	 * @param pmCouponIssueId
	 * @return
	 */
	int isPossibilityReissueCartCouponInOdOrderDetl(@Param("odOrderId")Long odOrderId, @Param("pmCouponIssueId")Long pmCouponIssueId, @Param("odClaimDetlIds")List<Long> odClaimDetlIds);

    /**
     * 주문 상세 주문상세 결제정보(증정품 내역) 리스트 조회
     * @param odOrderId
     * @return
     */
    List<OrderGiftVo> getOrderGiftList(Long odOrderId);

	/**
	 * 주문상세 PK로 일일배송 스케쥴정보 조회
	 * @param odOrderDetlId
	 * @return OrderDetlDailyVo
	 */
	OrderDetlDailyVo getOrderDetlDailySchByOdOrderDetlId(Long odOrderDetlId);

	/**
	 * 주문번호 PK로 매장(배송/픽업) 정보 조회
	 * @param odOrderId
	 * @return OrderShopStoreVo
	 */
    OrderShopStoreVo getOrderShopStoreInfo(String odOrderId) throws Exception;
	
	/**
	 * 하이톡 <--> FD-PHI 스위치 설정값 조회(하이톡 스위치)
	 */
	int getHitokSwitchFromPsConfig();

	/**
	 * 임직원 지원금 사용여부
	 * @param odOrderId
	 * @return
	 */
	int getOrderEmployeeDiscountCnt(@Param("odOrderId") String odOrderId);

	/**
     * 주문상세 처리이력 메세지 정보 주문상세ID 리스트 조회
     * @param odOrderDetlId
     * @return
     */
    OrderDetlVo getHistMsgOdOrderDetlId(@Param("odOrderDetlId") Long odOrderDetlId, @Param("type")String type);

    /**
	 * 주문상세 선물정보 조회
	 * @param odOrderId
	 * @return
	 */
    OrderDetailPresentVo getOrderPresentInfo(String odOrderId);

     /**
	 * 주문상세 선물정보 수정
	 * @param orderDetailPresentVo
	 * @return
	 */
	int putOrderPresentInfo(OrderDetailPresentVo orderDetailPresentVo);
}
