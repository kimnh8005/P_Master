package kr.co.pulmuone.v1.order.delivery.service;

import java.util.List;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.order.create.dto.OrderCardPayRequestDto;
import kr.co.pulmuone.v1.order.create.dto.GoodsInfoDto;
import kr.co.pulmuone.v1.order.create.dto.OrderCreateListRequestDto;
import kr.co.pulmuone.v1.order.create.dto.OrderCreateRequestDto;
import kr.co.pulmuone.v1.order.create.dto.OrderExcelRequestDto;
import kr.co.pulmuone.v1.order.create.dto.OrderInfoDto;
import kr.co.pulmuone.v1.order.create.dto.PaymentInfoDto;
import kr.co.pulmuone.v1.order.create.dto.UserGroupInfoDto;
import kr.co.pulmuone.v1.order.create.dto.vo.CreateInfoVo;
import kr.co.pulmuone.v1.order.delivery.dto.OrderDeliveryTrackingRequestDto;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderPaymentMasterVo;
import kr.co.pulmuone.v1.order.registration.dto.OrderRegistrationResponseDto;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 생성 관련 Interface
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
public interface OrderDeliveryTrackingBiz {
    ApiResult<?> getDeliveryTrackingList(OrderDeliveryTrackingRequestDto orderDeliveryTrackingRequestDto) throws Exception;
}
