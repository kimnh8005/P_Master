package kr.co.pulmuone.v1.order.present.service;

import java.util.List;
import java.util.stream.Collectors;

import kr.co.pulmuone.v1.order.present.dto.OrderPresentExpiredResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.util.StringUtil;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.OrderClaimEnums;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.enums.OrderEnums.OrderPresentErrorCode;
import kr.co.pulmuone.v1.comm.enums.OrderEnums.OrderRegistrationResult;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.goods.goods.dto.ArrivalScheduledDateDto;
import kr.co.pulmuone.v1.goods.goods.service.GoodsGoodsBiz;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimRegisterRequestDto;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimRegisterResponseDto;
import kr.co.pulmuone.v1.order.claim.service.ClaimProcessBiz;
import kr.co.pulmuone.v1.order.delivery.service.ShippingZoneBiz;
import kr.co.pulmuone.v1.order.email.dto.OrderInfoForEmailResultDto;
import kr.co.pulmuone.v1.order.email.service.OrderEmailBiz;
import kr.co.pulmuone.v1.order.email.service.OrderEmailSendBiz;
import kr.co.pulmuone.v1.order.order.dto.OrderDetailDateUpdateRequestDto;
import kr.co.pulmuone.v1.order.order.dto.PgApprovalOrderDataDto;
import kr.co.pulmuone.v1.order.order.dto.StockCheckOrderDetailDto;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderShippingZoneVo;
import kr.co.pulmuone.v1.order.order.service.OrderDetailDeliveryBiz;
import kr.co.pulmuone.v1.order.order.service.OrderOrderBiz;
import kr.co.pulmuone.v1.order.present.dto.OrderPresentArrivalScheduledDto;
import kr.co.pulmuone.v1.order.present.dto.OrderPresentDto;
import kr.co.pulmuone.v1.order.present.dto.OrderPresentReceiveRequestDto;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 선물하기 BizImpl
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		20210715   	 	홍진영            최초작성
 * =======================================================================
 * </PRE>
 */

@Service
public class OrderPresentBizImpl implements OrderPresentBiz {

	@Autowired
	OrderPresentService orderPresentService;

	@Autowired
	ClaimProcessBiz claimProcessBiz;

	@Autowired
	ShippingZoneBiz shippingZoneBiz;

	@Autowired
	OrderDetailDeliveryBiz orderDetailDeliveryBiz;

	@Autowired
	OrderOrderBiz orderOrderBiz;

	@Autowired
	GoodsGoodsBiz goodsGoodsBiz;

	@Autowired
	OrderEmailBiz orderEmailBiz;

	@Autowired
	OrderEmailSendBiz orderEmailSendBiz;

	/**
	 * 선물하기 presentId 생성
	 */
	@Override
	public String makePresentId(Long odOrderId) throws Exception {
		return orderPresentService.makePresentId(odOrderId);
	}

	/**
	 * 선물하기 데이터 presentId로 조회
	 */
	@Override
	public OrderPresentDto getOrderPresentByPresentId(String presentId) throws Exception {
		return orderPresentService.getOrderPresentByPresentId(presentId);
	}

	/**
	 * 선물하기 데이터 presentId, presentAuthCd 로 조회
	 */
	@Override
	public OrderPresentDto getOrderPresentByPresentIdAndAuthCode(String presentId, String presentAuthCd)
			throws Exception {
		return orderPresentService.getOrderPresentByPresentIdAndAuthCode(presentId, presentAuthCd);
	}

	/**
	 * 선물하기 데이터 presentId, odOrderId 검증
	 */
	@Override
	public boolean isOrderPresentAuth(String presentId, Long odOrderId) throws Exception {
		return orderPresentService.isOrderPresentAuth(presentId, odOrderId);
	}

	/**
	 * 선물 거절
	 */
	@Override
	public OrderPresentErrorCode reject(Long odOrderId, String rejectUrUserId) throws Exception {

		// 선물 거절하기 가능 상태 체크
		OrderPresentErrorCode reusltCode = orderPresentService.isCheckPresentOrderStatusReject(odOrderId);
		if (!OrderPresentErrorCode.SUCCESS.equals(reusltCode)) {
			return reusltCode;
		}

		// 취소 처리
		if (doOrderCancel(odOrderId, rejectUrUserId, "고객 선물받기 거절")) {
			// 거절 상태 변경
			orderPresentService.putPresentOrderStatusReject(odOrderId);

			// 선물 거절 SMS 발송 보내기
			OrderPresentDto orderPresentDto = orderPresentService.getOrderPresentByOdOrderId(odOrderId);
			OrderInfoForEmailResultDto emailDto = orderEmailBiz.getOrderInfoForEmail(odOrderId);
			orderEmailSendBiz.orderPresentRejectMassegeSend(emailDto, orderPresentDto);

			return OrderPresentErrorCode.SUCCESS;
		} else {
			return OrderPresentErrorCode.FAIL_REJECT;
		}
	}

	/**
	 * 유효기간 만료 선물하기 리스트
	 */
	@Override
	public List<OrderPresentExpiredResponseDto> getExpiredCancelOrderList() throws Exception {
		return orderPresentService.getexpiredCancelOrderList();
	}

	/**
	 * 선물 유효기간 만료 취소
	 */
	@Override
	public OrderPresentErrorCode expiredCancel(Long odOrderId) throws Exception {

		// 선물 유효기간 만료 취소 가능 상태 체크
		OrderPresentErrorCode reusltCode = orderPresentService.isCheckPresentOrderStatusExpired(odOrderId);
		if (!OrderPresentErrorCode.SUCCESS.equals(reusltCode)) {
			return reusltCode;
		}

		// 취소 처리
		if (doOrderCancel(odOrderId, String.valueOf(Constants.BATCH_CREATE_USER_ID), "선물 유효기간 만료 취소")) {
			// 유효기간 만료 상태 변경
			orderPresentService.putPresentOrderStatusExpired(odOrderId);
			return OrderPresentErrorCode.SUCCESS;
		} else {
			return OrderPresentErrorCode.FAIL_REJECT;
		}
	}

	private boolean doOrderCancel(Long odOrderId, String cancelUrUserId, String cancelMsg) throws Exception {

		// 주문 데이터 조회
		OrderPresentDto orderPresentDto = orderPresentService.getOrderPresentByOdOrderId(odOrderId);

		OrderClaimRegisterRequestDto reqDto = new OrderClaimRegisterRequestDto();
		reqDto.setOdOrderId(orderPresentDto.getOdOrderId());
		reqDto.setOdid(orderPresentDto.getOdid());
		reqDto.setClaimStatusTp(OrderClaimEnums.ClaimStatusTp.CANCEL.getCode()); // 클레임상태구분 취소
		reqDto.setClaimReasonMsg(cancelMsg);

		// 클레임상태코드 취소완료
		reqDto.setClaimStatusCd(OrderEnums.OrderStatus.CANCEL_COMPLETE.getCode());

		// 취소 상품 리스트
		reqDto.setGoodsInfoList(orderPresentService.getOrderPresentCancelGoodsList(odOrderId));
		reqDto.setUrUserId(StringUtil.isNotEmpty(cancelUrUserId) ? cancelUrUserId
				: String.valueOf(Constants.GUEST_CREATE_USER_ID));
		reqDto.setCustomUrUserId(String.valueOf(orderPresentDto.getBuyerUrUserId()));

		reqDto.setFrontTp(OrderClaimEnums.OrderClaimFrontTpCd.FRONT_TP_FRONT.getCodeValue());

		// 클레임 취소 처리
		try {
			ApiResult<OrderClaimRegisterResponseDto> result = (ApiResult<OrderClaimRegisterResponseDto>) claimProcessBiz
					.addOrderClaim(reqDto);
			OrderClaimRegisterResponseDto data = result.getData();
			if (BaseEnums.Default.SUCCESS.getCode().equals(result.getCode())
					&& OrderRegistrationResult.SUCCESS.getCode().equals(data.getOrderRegistrationResult().getCode())) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}

	}

	/**
	 * 선물 받기
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class, BaseException.class })
	public OrderPresentErrorCode receive(OrderPresentReceiveRequestDto reqDto, Long receiveUrUserId) throws Exception {

		// 선물 받기 가능 상태 체크
		OrderPresentErrorCode reusltCode = orderPresentService.isCheckPresentOrderStatusReceive(reqDto.getOdOrderId());
		if (!OrderPresentErrorCode.SUCCESS.equals(reusltCode)) {
			return reusltCode;
		}

		// 주소
		List<Long> odShippingZoneIds = orderPresentService.getOrderPresentOdShippingZoneIds(reqDto.getOdOrderId());

		for (Long odShippingZoneId : odShippingZoneIds) {
			// 주소 변경
			shippingZoneBiz.putShippingZone(OrderShippingZoneVo.builder().recvNm(reqDto.getShippingZone().getRecvNm())
					.recvHp(reqDto.getShippingZone().getRecvHp()).recvZipCd(reqDto.getShippingZone().getRecvZipCd())
					.recvAddr1(reqDto.getShippingZone().getRecvAddr1())
					.recvAddr2(reqDto.getShippingZone().getRecvAddr2())
					.recvBldNo(reqDto.getShippingZone().getRecvBldNo())
					.deliveryMsg(reqDto.getShippingZone().getDeliveryMsg())
					.doorMsgCd(reqDto.getShippingZone().getDoorMsgCd()).doorMsg(reqDto.getShippingZone().getDoorMsg())
					.odShippingZoneId(odShippingZoneId).build());
		}

		// 재고 일자 변경 상품 목록
		List<StockCheckOrderDetailDto> goodsList = orderOrderBiz.getStockCheckOrderDetailList(reqDto.getOdOrderId());

		// 상품 재고 일자 변경
		for (OrderPresentArrivalScheduledDto arrivalScheduled : reqDto.getArrivalScheduled()) {
			for (StockCheckOrderDetailDto goods : goodsList.stream()
					.filter(dto -> arrivalScheduled.getOdShippingPriceId().equals(dto.getOdShippingPriceId()))
					.collect(Collectors.toList())) {

				// 도착 예정이로 ArrivalScheduledDateDto 찾기
				ArrivalScheduledDateDto dateDto = goodsGoodsBiz
						.getArrivalScheduledDateDtoByArrivalScheduledDate(
								goodsGoodsBiz.getArrivalScheduledDateDtoList(goods.getUrWarehouseId(),
										Long.valueOf(goods.getIlGoodsId()),
										"Y".equals(arrivalScheduled.getDawnDeliveryYn()), goods.getOrderCnt(), null),
								arrivalScheduled.getArrivalScheduledDate());

				OrderDetailDateUpdateRequestDto putReqDto = new OrderDetailDateUpdateRequestDto();
				putReqDto.setOdShippingZoneId(arrivalScheduled.getOdShippingPriceId());
				putReqDto.setOrderIfDawnYn(arrivalScheduled.getDawnDeliveryYn());
				putReqDto.setUrWarehouseId(goods.getUrWarehouseId());
				putReqDto.setOdOrderDetlId(goods.getOdOrderDetlId());
				putReqDto.setOrderIfDt(dateDto.getOrderDate().toString());
				putReqDto.setShippingDt(dateDto.getForwardingScheduledDate().toString());
				putReqDto.setDeliveryDt(dateDto.getArrivalScheduledDate().toString());
				putReqDto.setAllChangeYn("N");

				ApiResult<?> result = orderDetailDeliveryBiz.putIfDay(putReqDto, receiveUrUserId);
				if (!BaseEnums.Default.SUCCESS.getCode().equals(result.getCode())) {
					throw new BaseException(OrderPresentErrorCode.FAIL_RECEIVE);
				}
			}
		}

		// 선물 받기 상태 변경
		orderPresentService.putPresentOrderStatusReceive(reqDto.getOdOrderId());

		return OrderPresentErrorCode.SUCCESS;

	}

	/**
	 * 선물하기 메세지 재발송
	 */
	@Override
	public OrderPresentErrorCode reSendMessage(String odid, String urlType) throws Exception {

		PgApprovalOrderDataDto orderData = orderOrderBiz.getPgApprovalOrderDataByOdid(odid);

		// 선물하기 메세지 재발송 가능 상태 체크
		if (!orderPresentService.isCheckPresentOrderStatusReSendMessage(orderData.getOdOrderId())) {
			return OrderPresentErrorCode.FAIL_RE_SEND_MESSAGE_IMPOSSIBLE_STATUS;
		}

		if ("Y".equals(orderData.getPresentYn())) {
			OrderInfoForEmailResultDto emailDto = orderEmailBiz.getOrderInfoForEmail(orderData.getOdOrderId());
			orderEmailSendBiz.orderPresentMassegeSend(emailDto, orderData);

			// 메시지 발송 count 업데이트
			if(BaseEnums.EnumSiteType.MALL.getCode().equals(urlType)){
				orderPresentService.putPresentMsgSendCnt(orderData.getOdOrderId());
			}
		}

		return OrderPresentErrorCode.SUCCESS;
	}

	/**
	 * 선물 취소
	 */
	@Override
	public OrderPresentErrorCode buyerCancel(Long odOrderId) throws Exception {

		// 선물 유효기간 만료 취소 가능 상태 체크
		if (!orderPresentService.isCheckPresentOrderStatusCancel(odOrderId)) {
			return OrderPresentErrorCode.FAIL_CANCEL_IMPOSSIBLE_STATUS;
		}

		// 상태 취소 변경
		orderPresentService.putPresentOrderStatusCancel(odOrderId);
		return OrderPresentErrorCode.SUCCESS;
	}

	/**
	 * 선물하기 조회
	 */
	@Override
	public OrderPresentDto getOrderPresentByOdOrderId(Long odOrderId) throws Exception {
		return orderPresentService.getOrderPresentByOdOrderId(odOrderId);
	}
}