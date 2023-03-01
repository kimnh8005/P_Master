package kr.co.pulmuone.v1.order.delivery.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderShippingZoneHistVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderShippingZoneVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 생성 관련 Implements
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 15.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */
@Slf4j
@Service
public class ShippingZoneBizImpl implements ShippingZoneBiz {

	@Autowired
	private ShippingZoneService shippingZoneService;

	/**
	 * 배송지 정보 수정
	 * @param orderShippingZoneVo
	 * @return
	 */
	@Override
	public ApiResult<?> putShippingZone(OrderShippingZoneVo orderShippingZoneVo) {


		shippingZoneService.putShippingZone(orderShippingZoneVo);

		OrderShippingZoneHistVo orderShippingZoneHistVo = OrderShippingZoneHistVo.builder()
				.odShippingZoneId(orderShippingZoneVo.getOdShippingZoneId())
				.odOrderId(orderShippingZoneVo.getOdOrderId())
				.deliveryType(orderShippingZoneVo.getDeliveryType())
				.shippingType(orderShippingZoneVo.getShippingType())
				.recvNm(orderShippingZoneVo.getRecvNm())
				.recvHp(orderShippingZoneVo.getRecvHp())
				.recvTel(orderShippingZoneVo.getRecvTel())
				.recvMail(orderShippingZoneVo.getRecvMail())
				.recvZipCd(orderShippingZoneVo.getRecvZipCd())
				.recvAddr1(orderShippingZoneVo.getRecvAddr1())
				.recvAddr2(orderShippingZoneVo.getRecvAddr2())
				.recvBldNo(orderShippingZoneVo.getRecvBldNo())
				.deliveryMsg(orderShippingZoneVo.getDeliveryMsg())
				.doorMsgCd(orderShippingZoneVo.getDoorMsgCd())
				.doorMsg(orderShippingZoneVo.getDoorMsg())
				.build();
		shippingZoneService.addShippingZoneHist(orderShippingZoneHistVo);

		return ApiResult.success();
	}

    /**
     * 주문 배송지 조회
     * @param odShippingZoneId
     * @return OrderShippingZoneVo
     */
	@Override
    public OrderShippingZoneVo getOrderShippingZone(Long odShippingZoneId) {
        return shippingZoneService.getOrderShippingZone(odShippingZoneId);
    }
}
