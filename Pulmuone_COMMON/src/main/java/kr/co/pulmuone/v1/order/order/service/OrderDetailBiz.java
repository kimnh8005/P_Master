package kr.co.pulmuone.v1.order.order.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.order.order.dto.OrderDetailConsultRequestDto;
import kr.co.pulmuone.v1.order.order.dto.OrderDetailShippingZoneRequestDto;
import kr.co.pulmuone.v1.order.order.dto.OrderDetlCopyShippingZoneDto;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetailPresentVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlDailyVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderShippingZoneHistVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderShippingZoneVo;
import kr.co.pulmuone.v1.user.buyer.dto.CommonSaveShippingAddressRequestDto;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 상세 관련 Interface
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
public interface OrderDetailBiz {


	/**
	 * 주문상세 상품 리스트 조회
	 * @param odOrderId
	 * @return
	 */
	ApiResult<?> getOrderDetailGoodsList(String odOrderId);

	/**
	 * 주문상세 클레임 상품 리스트 조회
	 * @param odOrderId
	 * @return
	 */
	ApiResult<?> getOrderDetailClaimGoodsList(String odOrderId);

	/**
	 * @Desc 주문 상세 클레임 상품정보 조회 > 첨부파일보기 조회
	 * @param odClaimId
	 * @return ApiResult<?>
	 */
	ApiResult<?> getOrderClaimAttc(long odClaimId);

	/**
	 * 주문상세 결제 리스트 조회
	 * @param odOrderId
	 * @return
	 */
	ApiResult<?> getOrderDetailPayList(String odOrderId);

	/**
	 * 주문상세 쿠폰할인 리스트 조회
	 * @param odOrderId
	 * @return
	 */
	ApiResult<?> getOrderDetailDiscountList(String odOrderId);

	/**
	 * 주문 상세 클레임 회수 정보 조회
	 * @param odOrderId
	 * @return
	 */
	ApiResult<?> getOrderDetailClaimCollectionList(String odOrderId);

	/**
	 * 주문상세 상담 리스트 조회
	 * @param odOrderId
	 * @return
	 */
	ApiResult<?> getOrderDetailConsultList(String odOrderId);

	/**
	 * 주문 상세 상담 등록
	 * @param orderDetailConsultRequestDto
	 * @return
	 */
	ApiResult<?> addOrderDetailConsult(OrderDetailConsultRequestDto orderDetailConsultRequestDto);

	/**
	 * 주문 상세 상담 수정
	 * @param orderDetailConsultRequestDto
	 * @return
	 */
	ApiResult<?> putOrderDetailConsult(OrderDetailConsultRequestDto orderDetailConsultRequestDto);

	/**
	 * 주문 상세 상담 삭제
	 * @param odConsultId
	 * @return
	 */
	ApiResult<?> delOrderDetailConsult(String odConsultId);

	/**
	 * 주문상세 처리이력 리스트 조회
	 * @param odOrderId
	 * @return
	 */
	ApiResult<?> getOrderDetailHistoryList(String odOrderId);

	/**
	 * 주문상세 수취정보 변경
	 * @param orderDetailShippingZoneRequestDto
	 * @return
	 */
	ApiResult<?> putOrderDetailShippingZone(OrderDetailShippingZoneRequestDto orderDetailShippingZoneRequestDto) throws Exception;

	/**
	 * @Desc 주문상세 주문자정보 리스트 조회
	 * @param odid
	 * @return ApiResult<?>
	 */
	ApiResult<?> getOrderBuyer(String odid);

	/**
	 * @Desc 주문상세 배송정보 리스트 조회
	 * @param odOrderId
	 * @return ApiResult<?>
	 */
	ApiResult<?> getOrderDetailShippingZoneList(String odOrderId);


	ApiResult<?> getOrderDetailShippingZoneList(String odOrderId, String odShippingZoneId);

	/**
	 * @Desc 주문복사에서 배송정보 리스트 조회
	 * @param odOrderId
	 * @return
	 */
	List<OrderDetlCopyShippingZoneDto> getOrderDetailCopyShippingZoneList(String odOrderId);

	/*
	* @Desc 주문 상세 변경내역 조회 (수취 정보)
	 * @param odShippingZoneId
	 * @return ApiResult<?>
	 */
	ApiResult<?> getOrderShippingZoneByOdShippingZoneId(String odShippingZoneId);

	/**
 	 * @Desc 주문 상세 변경내역 조회 (변경 정보)
	 * @param odShippingZoneId
	 * @param odOrderDetlId
	 * @return ApiResult<?>
	 */
	ApiResult<?> getOrderShippingZoneHistList(String odShippingZoneId, String odOrderDetlId);

	/**
	 * @Desc 주문 상세 결제 정보 > 즉시할인내역 조회
	 * @param odOrderId
	 * @return ApiResult<?>
	 */
	ApiResult<?> getOrderDirectDiscountList(String odOrderId);

	/**
	 * @Desc 주문PK,쿠폰발급PK로 배송비쿠폰재발급가능한지여부 확인
	 * @param odOrderId
	 * @param pmCouponIssueId
	 * @return int
	 */
	int isPossibilityReissueCouponInOdshippingPrice(Long odOrderId, Long pmCouponIssueId);

	/**
	 * @Desc 주문PK, 쿠폰발급PK로 쿠폰재발급가능한지여부 확인
	 * @param odOrderId
	 * @param pmCouponIssueId
	 * @return int
	 */
	int isPossibilityReissueCouponInOdOrderDetl(Long odOrderId, Long pmCouponIssueId);

	/**
	 * @Desc 주문PK, 쿠폰발급PK로 장바구니쿠폰재발급가능한지여부 확인
	 * @param odOrderId
	 * @param pmCouponIssueId
	 * @param odClaimDetlIds
	 * @return
	 */
	int isPossibilityReissueCartCouponInOdOrderDetl(Long odOrderId, Long pmCouponIssueId, List<Long> odClaimDetlIds);

    /**
     * @Desc 주문 배송지 수정
     * @param orderShippingZoneVo
     * @return int
     */
	int putShippingZone(OrderShippingZoneVo orderShippingZoneVo);

	/**
	 * @Desc 주문 배송지 등록
	 * @param orderShippingZoneVo
	 * @return int
	 */
	int addShippingZone(OrderShippingZoneVo orderShippingZoneVo);

    /**
     * @Desc 주문 배송지 이력 등록
     * @param orderShippingZoneHistVo
     * @return int
     */
    int addShippingZoneHist(OrderShippingZoneHistVo orderShippingZoneHistVo);

	/**
	 * @Desc 주문 상세 결제 정보 > 증정품 내역 조회
	 * @param odOrderId
	 * @return ApiResult<?>
	 */
	ApiResult<?> getOrderGiftList(Long odOrderId);

	/**
	 * @Desc  배송지 변경 가능 여부 체크
	 * @param commonSaveShippingAddressRequestDto
	 * @return ApiResult<?>
	 */
	ApiResult<?> isPossibleChangeDeliveryAddress(CommonSaveShippingAddressRequestDto commonSaveShippingAddressRequestDto) throws Exception;

	/**
	 * @Desc 주문상세 PK로 일일배송 스케쥴정보 조회
	 * @param odOrderDetlId
	 * @return OrderDetlDailyVo
	 */
	OrderDetlDailyVo getOrderDetlDailySchByOdOrderDetlId(Long odOrderDetlId);

	ApiResult<?> getOrderShopStoreInfo(String odOrderId) throws Exception;
	
	/**
	 * 하이톡 <--> FD-PHI 스위치 설정값 조회(하이톡 스위치)
	 */
	ApiResult<?> getHitokSwitch();


	/**
	 * 임직원 할인사용 내역 카운트
	 */
	int getOrderEmployeeDiscountCnt(String odOrderId);

	/**
	 * 주문 상세 선물정보 조회
	 */
	ApiResult<?> getOrderPresentInfo(String odOrderId) throws Exception ;
	
	/**
	 * 주문 상세 선물정보 수정
	 */
	ApiResult<?> putOrderPresentInfo(OrderDetailPresentVo orderDetailPresentVo) throws Exception ;
	
	/**
	 * 주문 상세 선물정보 메세지 재발송
	 */
	ApiResult<?> reSendMessage(String odid) throws Exception;
}
