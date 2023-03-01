package kr.co.pulmuone.v1.order.order.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kr.co.pulmuone.v1.comm.enums.*;
import org.apache.commons.collections.CollectionUtils;
import kr.co.pulmuone.v1.promotion.linkprice.service.LinkPriceBiz;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.GoodsDeliveryType;
import kr.co.pulmuone.v1.comm.enums.OrderEnums.PaymentType;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.util.MaskingUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.stock.dto.StockOrderRequestDto;
import kr.co.pulmuone.v1.goods.stock.service.GoodsStockOrderBiz;
import kr.co.pulmuone.v1.order.create.service.OrderCreateBiz;
import kr.co.pulmuone.v1.order.email.dto.OrderInfoForEmailResultDto;
import kr.co.pulmuone.v1.order.email.service.OrderEmailBiz;
import kr.co.pulmuone.v1.order.email.service.OrderEmailSendBiz;
import kr.co.pulmuone.v1.order.order.dto.PgApprovalOrderDataDto;
import kr.co.pulmuone.v1.order.order.dto.PgApprovalOrderPaymentDataDto;
import kr.co.pulmuone.v1.order.order.dto.PutOrderPaymentCompleteResDto;
import kr.co.pulmuone.v1.order.order.dto.StockCheckOrderDetailDto;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderCashReceiptVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderPaymentMasterVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderVo;
import kr.co.pulmuone.v1.order.registration.dto.OrderApprovalDto;
import kr.co.pulmuone.v1.order.registration.service.OrderRegistrationBiz;
import kr.co.pulmuone.v1.promotion.coupon.service.PromotionCouponBiz;
import kr.co.pulmuone.v1.promotion.point.dto.DepositPointDto;
import kr.co.pulmuone.v1.promotion.point.service.PointBiz;
import kr.co.pulmuone.v1.user.buyer.service.UserBuyerBiz;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderProcessBizImpl implements OrderProcessBiz {

  @Autowired
  private OrderOrderService orderOrderService;

  @Autowired
  private PromotionCouponBiz promotionCouponBiz;

  @Autowired
  private PointBiz pontBiz;

  @Autowired
  private OrderRegistrationBiz orderRegistrationBiz;

  @Autowired
  private GoodsStockOrderBiz goodsStockOrderBiz;

  @Autowired
  private UserBuyerBiz userBuyerBiz;

  @Autowired
  public OrderEmailBiz orderEmailBiz;

  @Autowired
  public OrderEmailSendBiz orderEmailSendBiz;

  @Autowired
  public OrderCreateBiz orderCreateBiz;

	@Autowired
	public LinkPriceBiz linkPriceBiz;


	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public PutOrderPaymentCompleteResDto putOrderPaymentComplete(PgApprovalOrderDataDto orderData, PgApprovalOrderPaymentDataDto paymentData) throws Exception {

		PutOrderPaymentCompleteResDto putOrderPaymentCompleteResDto = new PutOrderPaymentCompleteResDto();
		putOrderPaymentCompleteResDto.setResult(PgEnums.PgErrorType.SUCCESS);
		putOrderPaymentCompleteResDto.setOrderData(orderData);
		putOrderPaymentCompleteResDto.setPaymentData(paymentData);

		if (!PaymentType.DIRECT.getCode().equals(orderData.getOriginOrderPaymentType())) {

			// 쿠폰 사용 처리
			if (CollectionUtils.isNotEmpty(orderData.getPmCouponIssueIds())) {
				for (Long pmCouponIssueId : orderData.getPmCouponIssueIds()) {
					ApiResult<?> couponRes = promotionCouponBiz.useCoupon(orderData.getUrUserId(), pmCouponIssueId);
					if (!BaseEnums.Default.SUCCESS.getCode().equals(couponRes.getCode())) {
						throw new BaseException(couponRes.getMessageEnum());
					}
				}
			}

			// 적립금 사용 처리
			if (orderData.getPointPrice() > 0) {
				ApiResult<?> pointRes = pontBiz.redeemPoint(DepositPointDto.builder().urUserId(orderData.getUrUserId()).pmPointId(0L)
						.pointPaymentType(PointEnums.PointPayment.DEDUCTION)
						.amount(Long.valueOf((orderData.getPointPrice() * -1)))
						.pointProcessType(PointEnums.PointProcessType.WITHDRAW_POINT_PAYMENT).refNo1(orderData.getOdid())
						.build());
				if (!BaseEnums.Default.SUCCESS.getCode().equals(pointRes.getCode())) {
					throw new BaseException(pointRes.getMessageEnum());
				}
			}

			// 재고 차감
			List<StockOrderRequestDto> stockOrderReqDtoList = new ArrayList<StockOrderRequestDto>();
			StockOrderRequestDto stockOrderRequestDto = new StockOrderRequestDto();
			List<StockCheckOrderDetailDto> orderGoodsList = orderOrderService.getStockCheckOrderDetailList(orderData.getOdOrderId());
			for (StockCheckOrderDetailDto goods : orderGoodsList) {
				StockOrderRequestDto stockOrderReqDto = new StockOrderRequestDto();
				stockOrderReqDto.setIlGoodsId(goods.getIlGoodsId());
				stockOrderReqDto.setOrderQty(goods.getOrderCnt());
				stockOrderReqDto.setScheduleDt(StringUtil.nvl(goods.getShippingDt(), "2000-01-01")); // TODO : 홍진영리더 ==? NULL인지 경우 어떻게 처리 해야하는
				stockOrderReqDto.setOrderYn("Y");
				stockOrderReqDto.setStoreYn(GoodsDeliveryType.SHOP.getCode().equals(goods.getGoodsDeliveryType()) ? "Y" : "N");
				stockOrderReqDto.setMemo(String.valueOf(goods.getOdOrderDetlId()));
				stockOrderReqDtoList.add(stockOrderReqDto);
			}
			stockOrderRequestDto.setOrderList(stockOrderReqDtoList);
			ApiResult<?> stockRes = goodsStockOrderBiz.stockOrderHandle(stockOrderRequestDto);
			if (!BaseEnums.Default.SUCCESS.getCode().equals(stockRes.getCode())) {
				throw new BaseException(stockRes.getMessageEnum());
			}
		}

		// 선물하기는 최근 배송지 저장 안함
		if (!"Y".equals(orderData.getPresentYn())) {
			// 최근배송지 저장
			userBuyerBiz.saveLatelyShippingAddress(orderData.getUrUserId(), orderData.getOdOrderId());
		}

		String orderStatusCd = OrderEnums.OrderStatus.INCOM_READY.getCode();
		if (!OrderEnums.PaymentType.VIRTUAL_BANK.getCode().equals(orderData.getOrderPaymentType())) {
			orderStatusCd = OrderEnums.OrderStatus.INCOM_COMPLETE.getCode();
		}

		String pgAccountType = "";
		if(paymentData.getPgAccountType() != null){
			pgAccountType = paymentData.getPgAccountType().getCode();
		}

		OrderCashReceiptVo orderCashReceiptVo = null;
		// 현금 영수증 정보
		if ("Y".equals(paymentData.getCashReceiptYn())) {
			orderCashReceiptVo = OrderCashReceiptVo.builder()
					.odOrderId(orderData.getOdOrderId())
					.odPaymentMasterId(orderData.getOdPaymentMasterId())
					.cashReceiptType(paymentData.getCashReceiptType())
					.cashReceiptNo(paymentData.getCashReceiptNo())
					.cashReceiptAuthNo(paymentData.getCashReceiptAuthNo())
					.build();
		}


		// 결제 승인 처리
		orderRegistrationBiz.approvalOrder(OrderApprovalDto.builder()
				.order(OrderVo.builder()
						.odOrderId(orderData.getOdOrderId())
						.orderPaymentType(orderData.getOrderPaymentType())
						.orderYn("Y")
						.build())
				.orderDetlVo(OrderDetlVo.builder()
						.odOrderId(orderData.getOdOrderId())
						.orderStatusCd(orderStatusCd)
						.build())
				.orderPaymentMasterVo(OrderPaymentMasterVo.builder()
						.odPaymentMasterId(orderData.getOdPaymentMasterId())
						.status(orderStatusCd)
						.payTp(orderData.getOrderPaymentType())
						.pgService(pgAccountType)
						.tid(paymentData.getTid())
						.authCd(paymentData.getAuthCode())
						.cardNumber(MaskingUtil.cardNumber(paymentData.getCardNumber()))
						.cardQuotaInterest(paymentData.getCardQuotaInterest())
						.cardQuota(paymentData.getCardQuota())
						.virtualAccountNumber(paymentData.getVirtualAccountNumber())
						.bankNm(paymentData.getBankName())
						.info(paymentData.getInfo())
						.paidDueDt(paymentData.getPaidDueDate())
						.paidHolder(paymentData.getPaidHolder())
						.partCancelYn(StringUtil.nvl(paymentData.getPartCancelYn(), "Y"))
						.escrowYn(StringUtil.nvl(paymentData.getEscrowYn(), "N"))
						.approvalDt(paymentData.getApprovalDate())
						.responseData(paymentData.getResponseData())
						.build())
				.orderCashReceipt(orderCashReceiptVo)
				.build());

		// 비인증 결제 주문연동 매출만연동시 결제완료시 처리
		OrderVo orderVo = orderRegistrationBiz.getOrderCopySalIfYn(orderData.getOdOrderId());
		log.debug("주문복사 정보 :: <{}>", orderVo.toString());
		String orderCopySalIfYn = orderVo.getOrderCopySalIfYn();
		String orderCopyOdid	= orderVo.getOrderCopyOdid();

		if ("Y".equals(orderCopySalIfYn) && StringUtil.isNotEmpty(orderCopyOdid) && !PaymentType.VIRTUAL_BANK.getCode().equals(orderData.getOrderPaymentType())) {
			orderCreateBiz.orderCopySalIfExecute(orderData.getOdOrderId(), orderCopySalIfYn, orderCopyOdid);
		}

		// 출고처 일자별 출고수량 업데이트
		orderOrderService.putWarehouseDailyShippingCount(Arrays.asList(orderData.getOdOrderId()));

		// 주문 완료 자동메일 발송
		try {
			OrderInfoForEmailResultDto orderInfoForEmailResultDto = orderEmailBiz.getOrderInfoForEmail(orderData.getOdOrderId());

			// 렌탈상품은 주문자동메일 발송x
			if(ObjectUtils.isNotEmpty(orderInfoForEmailResultDto.getOrderInfoVo()) && orderInfoForEmailResultDto.getOrderInfoVo().getRentalCount() == 0){
				if (OrderEnums.PaymentType.VIRTUAL_BANK.getCode().equals(orderData.getOrderPaymentType())) {

					// 주문 접수 완료 자동메일(가상계좌)
					orderEmailSendBiz.orderReceivedComplete(orderInfoForEmailResultDto);
				}else {

					// 주문 결제 완료 자동메일(가상계좌 외)
					orderEmailSendBiz.orderPaymentComplete(orderInfoForEmailResultDto);
				}

				// 선물 받는사람 SMS 보내기
				if ("Y".equals(orderData.getPresentYn())) {
					orderEmailSendBiz.orderPresentMassegeSend(orderInfoForEmailResultDto, orderData);
				}
			}

			// 올가 식품안전팀 주의 주문 발생시 -> BOS 식품안전팀 자동메일 발송
			if(orderEmailBiz.checkBosOrgaCautionOrderNotification(orderData.getOdOrderId())){
				orderEmailSendBiz.bosOrgaCautionOrderNotification(orderInfoForEmailResultDto);
			}

		} catch (Exception e) {
			log.error("ERROR ====== 주문완료 자동메일 발송 오류 orderData ::{}", orderData);
			log.error("ERROR ====== 주문완료 자동메일 발송 오류 Exception", e);
		}

		// 링크프라이스 실적 결제정보 저장 및 전송
		try {
			//실결제 금액이 0원 이어도 적립금 금액이 0보다 크다면 링크프라이스에 전송
			if(orderStatusCd.equals(OrderEnums.OrderStatus.INCOM_COMPLETE.getCode()) && (orderData.getPaymentPrice() > 0 || orderData.getPointPrice() > 0)) {
				linkPriceBiz.saveAndSendLinkPrice(orderData);
			}
			//가상계좌 주문시 PM_AD_LINK_PRICE 에 쿠키와 주문번호를 먼저 넣어두고, 이니시스 가상계좌 입금통보(value = "/pg/inicis/virtualAccountReturn") 시 링크프라이스에 전송
            if (PaymentType.VIRTUAL_BANK.getCode().equals(orderData.getOrderPaymentType()) && orderStatusCd.equals(OrderEnums.OrderStatus.INCOM_READY.getCode())) {
                linkPriceBiz.virtualBankSaveLinkPrice(orderData);
            }
		} catch (Exception e) {
			log.warn("ERROR ====== 주문완료 링크프라이 실적 결제정보 저장 및 전송 오류 orderData ::{}", orderData);
			log.warn("ERROR ====== 주문완료 링크프라이 실적 결제정보 저장 및 전송 오류 Exception", e);
		}

		return putOrderPaymentCompleteResDto;
	}
}
