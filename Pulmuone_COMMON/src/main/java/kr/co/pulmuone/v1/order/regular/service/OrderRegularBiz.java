package kr.co.pulmuone.v1.order.regular.service;

import java.util.List;

import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.order.regular.dto.ApplyRegularResponseDto;
import kr.co.pulmuone.v1.order.regular.dto.MallRegularReqInfoResponseDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularReqGoodsListRequestDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularReqListRequestDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularReqListResponseDto;
import kr.co.pulmuone.v1.order.regular.dto.vo.OrderRegularPaymentKeyVo;
import kr.co.pulmuone.v1.order.regular.dto.vo.RegularPaymentKeyVo;
import kr.co.pulmuone.v1.shopping.cart.dto.CartRegularShippingDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CreateOrderCartDto;
import kr.co.pulmuone.v1.shopping.cart.dto.vo.ShippingTemplateGroupByVo;
import kr.co.pulmuone.v1.shopping.cart.dto.vo.SpCartVo;
import kr.co.pulmuone.v1.user.certification.dto.GetSessionShippingResponseDto;

public interface OrderRegularBiz {

	CartRegularShippingDto getRegularInfoByCart(Long urUserId) throws Exception;

	List<SpCartVo> getGoodsListByShippingPolicy(ShippingTemplateGroupByVo shippingTemplateData, Long urUserId)
			throws Exception;

	GetSessionShippingResponseDto getRegularShippingZone(Long odRegularReqId) throws Exception;

	int addRegularPaymentKey(OrderRegularPaymentKeyVo orderRegularPaymentKeyVo) throws Exception;

	RegularPaymentKeyVo getRegularPaymentKey(Long urUserId) throws Exception;

	ApplyRegularResponseDto applyRegular(CreateOrderCartDto createOrderCartDto) throws Exception;

	/**
	 * 정기배송주문신청 리스트 조회
	 * @param regularReqListRequestDto
	 * @return
	 * @throws Exception
	 */
	RegularReqListResponseDto getOrderRegularReqList(RegularReqListRequestDto regularReqListRequestDto) throws Exception;

	/**
	 * 정기배송주문신청 리스트 엑셀다운로드
	 * @param regularReqListRequestDto
	 * @return
	 * @throws Exception
	 */
	ExcelDownloadDto getOrderRegularReqListExcel(RegularReqListRequestDto regularReqListRequestDto) throws Exception;

	/**
	 * 정기배송 신청 내역 정보 조회
	 * @param urUserId
	 * @return
	 * @throws Exception
	 */
	MallRegularReqInfoResponseDto getOrderRegularReqInfo(RegularReqGoodsListRequestDto regularReqGoodsListRequestDto) throws Exception;
}
