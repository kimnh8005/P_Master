package kr.co.pulmuone.v1.order.order.service;


import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.mapper.order.delivery.ShippingZoneMapper;
import kr.co.pulmuone.v1.comm.mapper.order.order.OrderDetailMapper;
import kr.co.pulmuone.v1.comm.mapper.order.registration.OrderRegistrationMapper;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.order.claim.dto.vo.ClaimAttcVo;
import kr.co.pulmuone.v1.order.delivery.service.ShippingZoneBiz;
import kr.co.pulmuone.v1.order.order.dto.*;
import kr.co.pulmuone.v1.order.order.dto.mall.OrderDetailGoodsListMallDto;
import kr.co.pulmuone.v1.order.order.dto.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 상세 관련 Service
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
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderDetailService {

    private final OrderDetailMapper orderDetailMapper;

    private final ShippingZoneMapper shippingZoneMapper;

    private final OrderRegistrationMapper orderRegistrationMapper;

	@Autowired
	private ShippingZoneBiz shippingZoneBiz;

    /**
     * 주문상세 상품 리스트 조회
     * @param odOrderId
     * @return
     */
    protected List<OrderDetailGoodsListDto> getOrderDetailGoodsList(String odOrderId) {
        return orderDetailMapper.getOrderDetailGoodsList(odOrderId);
    }

    /**
     * 주문상세 클레임 상품 리스트 조회
     * @param odOrderId
     * @return
     */
    protected List<OrderDetailGoodsListDto> getOrderDetailClaimGoodsList(String odOrderId) {
        return orderDetailMapper.getOrderDetailClaimGoodsList(odOrderId);
    }

    /**
     * @Desc 주문 상세 클레임 상품정보 조회 > 첨부파일보기 조회
     * @param odClaimId
     * @return List<ClaimAttcVo>
     */
    protected List<ClaimAttcVo> getOrderClaimAttc(long odClaimId) {
		return orderDetailMapper.getOrderClaimAttc(odClaimId);
	}

    /**
     * 주문상세 결제정보(결제상세정보) 리스트 조회
     * @param odOrderId
     * @return
     */
    protected List<OrderDetailPayDetlListDto> getOrderDetailPayDetlList(String odOrderId) {
        return orderDetailMapper.getOrderDetailPayDetlList(odOrderId);
    }

    /**
     * 주문상세 결제정보(결제정보) 리스트 조회
     * @param odOrderId
     * @return
     */
    protected List<OrderDetailPayListDto> getOrderDetailPayList(String odOrderId) {
        return orderDetailMapper.getOrderDetailPayList(odOrderId);
    }

    /**
     * 주문상세 결제정보(환불정보) 리스트 조회
     * @param odOrderId
     * @return
     */
    protected List<OrderDetailRefundListDto> getOrderDetailRefundList(String odOrderId) {
        return orderDetailMapper.getOrderDetailRefundList(odOrderId);
    }

    /**
     * 주문상세 쿠폰할인 리스트 조회
     * @param odOrderId
     * @return
     */
    protected List<OrderDetailDiscountListDto> getOrderDetailDiscountList(String odOrderId) {
        return orderDetailMapper.getOrderDetailDiscountList(odOrderId);
    }

	/**
	 * 주문 상세 클레임 회수 정보 조회
	 * @param odOrderId
	 * @return
	 */
	protected List<OrderDetailClaimCollectionListDto> getOrderDetailClaimCollectionList(String odOrderId) {
		return orderDetailMapper.getOrderDetailClaimCollectionList(odOrderId);
	}

    /**
     * 주문상세 상담 리스트 조회
     * @param odOrderId
     * @return
     */
    protected List<OrderDetailConsultListDto> getOrderDetailConsultList(String odOrderId, long batchCreateUserId) {
        return orderDetailMapper.getOrderDetailConsultList(odOrderId, batchCreateUserId);
    }

    /**
     * 주문 상세 상담 등록
     * @param orderDetailConsultRequestDto
     * @return
     */
    protected int addOrderDetailConsult(OrderDetailConsultRequestDto orderDetailConsultRequestDto) {

    	if(StringUtil.isEmpty(orderDetailConsultRequestDto.getCreateId())) {
    		//세션 정보 불러오기
    		UserVo userVo = SessionUtil.getBosUserVO();
    		String userId = userVo.getUserId();
    		orderDetailConsultRequestDto.setCreateId(userId);
    	}

        return orderDetailMapper.addOrderDetailConsult(orderDetailConsultRequestDto);
    }

    /**
     * 주문 상세 상담 수정
     * @param orderDetailConsultRequestDto
     * @return
     */
    protected int putOrderDetailConsult(OrderDetailConsultRequestDto orderDetailConsultRequestDto) {
		//세션 정보 불러오기
		UserVo userVo = SessionUtil.getBosUserVO();
		String userId = userVo.getUserId();
		orderDetailConsultRequestDto.setCreateId(userId);
        return orderDetailMapper.putOrderDetailConsult(orderDetailConsultRequestDto);
    }

    /**
     * 주문 상세 상담 삭제
     * @param odConsultId
     * @return
     */
    protected int delOrderDetailConsult(String odConsultId) {
        return orderDetailMapper.delOrderDetailConsult(odConsultId);
    }

    /**
     * 주문상세 처리이력 리스트 조회
     * @param orderDetailHistoryRequestDto
     * @return
     */
    protected List<OrderDetailHistoryListDto> getOrderDetailHistoryList(OrderDetailHistoryRequestDto orderDetailHistoryRequestDto) {
        return orderDetailMapper.getOrderDetailHistoryList(orderDetailHistoryRequestDto);
    }

    /**
     * 주문상세 상품 리스트 조회
     * @param odOrderId
     * @return
     */
    protected List<OrderDetailGoodsListMallDto> getOrderDetailGoodsMallList(String odOrderId) {
        return orderDetailMapper.getOrderDetailGoodsMallList(odOrderId);
    }


	/**
	 * @Desc 주문상세 주문자정보 리스트 조회
	 * @param odid
	 * @return List<OrderDetlOrdererVo>
	 */
	protected OrderBuyerDto getOrderBuyer(String odid) {
		return orderDetailMapper.getOrderBuyer(odid);
	}

	/**
	 * @Desc 주문상세 배송정보 리스트 조회
	 * @param odOrderId
	 * @return List<OrderDetlShippingZoneVo>
	 */
	protected List<OrderDetlShippingZoneVo> getOrderDetailShippingZoneList(String odOrderId) {
		return orderDetailMapper.getOrderDetailShippingZoneList(odOrderId);
	}

	/**
	 * @Desc 주문 상세 수취 정보 조회 (by odShippingZoneId)
	 * @param odShippingZoneId
	 * @return List<OrderDetlShippingZoneVo>
	 */
	protected List<OrderDetlShippingZoneVo> getOrderShippingZoneByOdShippingZoneId(String odShippingZoneId) {
		return orderDetailMapper.getOrderShippingZoneByOdShippingZoneId(odShippingZoneId);
	}

	/**
	 * @Desc 주문상세 일일배송 스케쥴 배송정보 조회
	 * @param odShippingZoneId
	 * @return OrderDetlShippingZoneVo
	 */
	protected OrderDetlShippingZoneVo getOrderDetailDailySchShippingZone(Long odShippingZoneId) {
		return orderDetailMapper.getOrderDetailDailySchShippingZone(odShippingZoneId);
	}

	protected List<OrderDetlShippingZoneVo> getOrderDetailShippingZoneList(String odOrderId, String odShippingZoneId) {
		return orderDetailMapper.getOrderDetailShippingZoneList(odOrderId, odShippingZoneId);
	}

	/**
	 * @Desc 주문복사에서 배송정보 리스트 조회
	 * @param odOrderId
	 * @return
	 */
	protected List<OrderDetlCopyShippingZoneDto> getOrderDetailCopyShippingZoneList(String odOrderId) {
		return orderDetailMapper.getOrderDetailCopyShippingZoneList(odOrderId);
	}

    /**
     * 주문 배송지 수정
     * OD_SHIPPING_ZONE
     * @param orderShippingZoneVo
     * @return
     */
    protected int putShippingZone(OrderShippingZoneVo orderShippingZoneVo) {
        return shippingZoneMapper.putShippingZone(orderShippingZoneVo);
    }

	/**
	 * 주문 배송지 등록
	 * OD_SHIPPING_ZONE
	 * @param orderShippingZoneVo
	 * @return
	 */
	protected int addShippingZone(OrderShippingZoneVo orderShippingZoneVo) {
		return orderRegistrationMapper.addShippingZone(orderShippingZoneVo);
	}

    /**
     * 주문 배송지 이력 등록
     * OD_SHIPPING_ZONE_HIST
     * @param orderShippingZoneHistVo
     * @return
     */
    protected int addShippingZoneHist(OrderShippingZoneHistVo orderShippingZoneHistVo) {
        return orderRegistrationMapper.addShippingZoneHist(orderShippingZoneHistVo);
    }

	/**
	 * @Desc 주문 상세 변경내역 조회 (변경 정보)
	 * @param odShippingZoneId
	 * @return List<OrderDetlShippingZoneHistVo>
	 */
	protected List<OrderDetlShippingZoneHistVo> getOrderShippingZoneHistList(String odShippingZoneId) {
		return orderDetailMapper.getOrderShippingZoneHistList(odShippingZoneId);
	}

	/**
	 * @Desc 주문 상세 결제 정보 > 즉시할인내역 조회
	 * @param odOrderId
	 * @return List<OrderDetlDirectDiscountVo>
	 */
	protected List<OrderDetlDirectDiscountVo> getOrderDirectDiscountList(String odOrderId) {
		return orderDetailMapper.getOrderDirectDiscountList(odOrderId);
	}

	/**
	 * @Desc 주문PK, 쿠폰발급PK로 배송비쿠폰재발급가능한지여부 확인
	 * @param odOrderId
	 * @param pmCouponIssueId
	 * @return int
	 */
	protected int isPossibilityReissueCouponInOdshippingPrice(Long odOrderId, Long pmCouponIssueId) {
		return orderDetailMapper.isPossibilityReissueCouponInOdshippingPrice(odOrderId, pmCouponIssueId);
	}

	/**
	 * @Desc 주문PK, 쿠폰발급PK로 쿠폰재발급가능한지여부 확인
	 * @param odOrderId
	 * @param pmCouponIssueId
	 * @return int
	 */
	protected int isPossibilityReissueCouponInOdOrderDetl(Long odOrderId, Long pmCouponIssueId) {
		return orderDetailMapper.isPossibilityReissueCouponInOdOrderDetl(odOrderId, pmCouponIssueId);
	}

	/**
	 * @Desc 주문PK, 쿠폰발급PK로 장바구니쿠폰재발급가능한지여부 확인
	 * @param odOrderId
	 * @param pmCouponIssueId
	 * @return
	 */
	protected int isPossibilityReissueCartCouponInOdOrderDetl(Long odOrderId, Long pmCouponIssueId, List<Long> odClaimDetlIds) {
		return orderDetailMapper.isPossibilityReissueCartCouponInOdOrderDetl(odOrderId, pmCouponIssueId, odClaimDetlIds);
	}

	/**
	 * @Desc  주문 상세 주문상세 결제정보(증정품 내역) 리스트 조회
	 * @param odOrderId
	 * @return List<OrderGiftVo>
	 */
	protected List<OrderGiftVo> getOrderGiftList(Long odOrderId) {
		return orderDetailMapper.getOrderGiftList(odOrderId);
	}

	/**
	 * 주문상세 PK로 일일배송 스케쥴정보 조회
	 * @param odOrderDetlId
	 * @return OrderDetlDailyVo
	 */
	protected OrderDetlDailyVo getOrderDetlDailySchByOdOrderDetlId(Long odOrderDetlId){
		return orderDetailMapper.getOrderDetlDailySchByOdOrderDetlId(odOrderDetlId);
	}
	
	/**
	 * 하이톡 <--> FD-PHI 스위치 설정값 조회(하이톡 스위치)
	 */
	protected boolean isHitokSwitch() {
		return orderDetailMapper.getHitokSwitchFromPsConfig() > 0;
	}

	/**
	 * 주문번호 PK로 매장(배송/픽업) 정보 조회
	 * @param odOrderId
	 * @return OrderShopStoreVo
	 */
	protected OrderShopStoreVo getOrderShopStoreInfo(String odOrderId) throws Exception {
		return orderDetailMapper.getOrderShopStoreInfo(odOrderId);
	}

	protected OrderShippingZoneVo setPutShippingZoneRequestParam(Long odShippingZoneId, OrderDetailShippingZoneRequestDto orderDetailShippingZoneRequestDto) throws Exception{
		OrderShippingZoneVo orderShippingZoneVo = OrderShippingZoneVo.builder()
				.odShippingZoneId(odShippingZoneId)
				.odOrderId(orderDetailShippingZoneRequestDto.getOdOrderId())
				.deliveryType(orderDetailShippingZoneRequestDto.getDeliveryType())
				.shippingType(orderDetailShippingZoneRequestDto.getShippingType())
				.recvNm(orderDetailShippingZoneRequestDto.getRecvNm())
				.recvHp(orderDetailShippingZoneRequestDto.getRecvHp())
				.recvTel(orderDetailShippingZoneRequestDto.getRecvTel())
				.recvMail(orderDetailShippingZoneRequestDto.getRecvMail())
				.recvZipCd(orderDetailShippingZoneRequestDto.getRecvZipCd())
				.recvAddr1(orderDetailShippingZoneRequestDto.getRecvAddr1())
				.recvAddr2(orderDetailShippingZoneRequestDto.getRecvAddr2())
				.recvBldNo(orderDetailShippingZoneRequestDto.getRecvBldNo())
				.deliveryMsg(orderDetailShippingZoneRequestDto.getDeliveryMsg())
				.doorMsgCd(orderDetailShippingZoneRequestDto.getDoorMsgCd())
				.doorMsg(orderDetailShippingZoneRequestDto.getDoorMsg())
				.build();

		return orderShippingZoneVo;
	}

	protected OrderShippingZoneHistVo setAddShippingZoneHistRequestParam(Long odShippingZoneId, OrderDetailShippingZoneRequestDto orderDetailShippingZoneRequestDto) throws Exception{

		// 주문 배송 정보 조회
		OrderShippingZoneVo orderShipingZoneVo = shippingZoneBiz.getOrderShippingZone(odShippingZoneId);

		OrderShippingZoneHistVo orderShippingZoneHistVo = OrderShippingZoneHistVo.builder()
				.odShippingZoneId(odShippingZoneId)
				.odOrderId(orderDetailShippingZoneRequestDto.getOdOrderId())
				.deliveryType(orderShipingZoneVo.getDeliveryType())
				.shippingType(orderShipingZoneVo.getShippingType())
				.recvNm(orderDetailShippingZoneRequestDto.getRecvNm())
				.recvHp(orderDetailShippingZoneRequestDto.getRecvHp())
				.recvTel(orderDetailShippingZoneRequestDto.getRecvTel())
				.recvMail(orderDetailShippingZoneRequestDto.getRecvMail())
				.recvZipCd(orderDetailShippingZoneRequestDto.getRecvZipCd())
				.recvAddr1(orderDetailShippingZoneRequestDto.getRecvAddr1())
				.recvAddr2(orderDetailShippingZoneRequestDto.getRecvAddr2())
				.recvBldNo(orderDetailShippingZoneRequestDto.getRecvBldNo())
				.deliveryMsg(orderDetailShippingZoneRequestDto.getDeliveryMsg())
				.doorMsgCd(orderDetailShippingZoneRequestDto.getDoorMsgCd())
				.doorMsg(orderDetailShippingZoneRequestDto.getDoorMsg())
				.build();
		return orderShippingZoneHistVo;
	}

	protected int getOrderEmployeeDiscountCnt(String odOrderId){
		return orderDetailMapper.getOrderEmployeeDiscountCnt(odOrderId);
	}

	/**
	 * 주문 상세 선물정보 조회
	 * @param odOrderId
	 * @return OrderDetailPresentVo
	 */
	protected OrderDetailPresentVo getOrderPresentInfo(String odOrderId) {
		return orderDetailMapper.getOrderPresentInfo(odOrderId);
	}

	/**
	 * 주문 상세 선물정보 수정
	 * @param orderDetailPresentVo
	 * @return
	 */
	protected int putOrderPresentInfo(OrderDetailPresentVo orderDetailPresentVo) {
		return orderDetailMapper.putOrderPresentInfo(orderDetailPresentVo);
	}

}