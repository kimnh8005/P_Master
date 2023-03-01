package kr.co.pulmuone.v1.order.order.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.*;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.GoodsDeliveryType;
import kr.co.pulmuone.v1.comm.enums.OrderEnums.PaymentType;
import kr.co.pulmuone.v1.comm.enums.PgEnums.InicisCardCode;
import kr.co.pulmuone.v1.comm.enums.PgEnums.InicisCode;
import kr.co.pulmuone.v1.comm.enums.PgEnums.PgServiceType;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.goods.goods.dto.ArrivalScheduledDateDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsReserveOptionVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ItemStoreInfoVo;
import kr.co.pulmuone.v1.goods.goods.service.GoodsGoodsBiz;
import kr.co.pulmuone.v1.order.create.service.OrderCreateBiz;
import kr.co.pulmuone.v1.order.email.dto.OrderInfoForEmailResultDto;
import kr.co.pulmuone.v1.order.email.service.OrderEmailBiz;
import kr.co.pulmuone.v1.order.email.service.OrderEmailSendBiz;
import kr.co.pulmuone.v1.order.order.dto.*;
import kr.co.pulmuone.v1.order.order.dto.mall.MallOrderDto;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderCashReceiptVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderPaymentMasterVo;
import kr.co.pulmuone.v1.order.registration.dto.OrderApprovalDto;
import kr.co.pulmuone.v1.order.registration.service.OrderRegistrationBiz;
import kr.co.pulmuone.v1.pg.dto.EtcDataCartDto;
import kr.co.pulmuone.v1.pg.dto.ReceiptIssueRequestDto;
import kr.co.pulmuone.v1.pg.dto.ReceiptIssueResponseDto;
import kr.co.pulmuone.v1.pg.dto.VirtualAccountDataResponseDto;
import kr.co.pulmuone.v1.pg.service.PgAbstractService;
import kr.co.pulmuone.v1.pg.service.PgBiz;
import kr.co.pulmuone.v1.pg.service.inicis.dto.*;
import kr.co.pulmuone.v1.pg.service.kcp.dto.KcpApprovalResponseDto;
import kr.co.pulmuone.v1.pg.service.kcp.dto.KcpVirtualAccountReturnRequestDto;
import kr.co.pulmuone.v1.promotion.coupon.service.PromotionCouponBiz;
import kr.co.pulmuone.v1.promotion.linkprice.service.LinkPriceBiz;
import kr.co.pulmuone.v1.promotion.point.service.PromotionPointBiz;
import kr.co.pulmuone.v1.store.delivery.dto.StoreDeliveryScheduleDto;
import kr.co.pulmuone.v1.store.delivery.service.StoreDeliveryBiz;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderOrderBizImpl implements OrderOrderBiz {

	@Autowired
	private OrderOrderService orderOrderService;

	@Autowired
	private PromotionPointBiz promotionPointBiz;

	@Autowired
	private PromotionCouponBiz promotionCouponBiz;

	@Autowired
	private PgBiz pgBiz;

	@Autowired
	private OrderRegistrationBiz orderRegistrationBiz;

	@Autowired
	private GoodsGoodsBiz goodsGoodsBiz;

	@Autowired
	public OrderEmailBiz orderEmailBiz;

	@Autowired
	public OrderEmailSendBiz orderEmailSendBiz;

	@Autowired
	public OrderCreateBiz orderCreateBiz;

	@Autowired
	private MallOrderDetailBiz mallOrderDetailBiz;

	@Autowired
	private StoreDeliveryBiz storeDeliveryBiz;

	@Autowired
	private OrderProcessBiz orderProcessBiz;

    @Autowired
    private LinkPriceBiz linkPriceBiz;

	/**
	 * 상품 구매수량
	 *
	 * @param ilGoodsId,urUserId
	 * @return Integer
	 * @throws Exception
	 */

	@Override
	public int getOrderGoodsBuyQty(Long ilGoodsId, String urUserId) throws Exception {
		return orderOrderService.getOrderGoodsBuyQty(ilGoodsId, urUserId);
	}

	/**
	 * 일별 출고 한도 체크 가능 출고일자 리턴
	 */
	@Override
	public List<ArrivalScheduledDateDto> removeDailyDeliveryLimitCnt(Long urWarehouseId,
																	 List<ArrivalScheduledDateDto> scheduledDateList,
																	 int limitCnt, boolean isDawnDelivery) throws Exception {
		return orderOrderService.removeDailyDeliveryLimitCnt(urWarehouseId, scheduledDateList, limitCnt, isDawnDelivery);
	}

	@Override
	public int getOrderCountByUser(String start, String end) throws Exception {
		return orderOrderService.getOrderCountByUser(start, end);
	}

	@Override
	public int getOrderPriceByUser(String start, String end) throws Exception {
		return orderOrderService.getOrderPriceByUser(start, end);
	}

	@Override
	public VirtualAccountResponseDto getVirtualAccount(String odid, String urUserId, String guestCi) throws Exception {
		return orderOrderService.getVirtualAccount(odid, urUserId, guestCi);
	}

	@Override
	public int getOrderOdidCount(String odid, String urUserId, String guestCi) throws Exception {
		return orderOrderService.getOrderOdidCount(odid, urUserId, guestCi);
	}

	@Override
	public PgApprovalOrderDataDto getPgApprovalOrderDataByOdid(String odid) throws Exception {

		PgApprovalOrderDataDto resDto = orderOrderService.getPgApprovalOrderDataByOdid(odid);
		if (resDto == null) {
			return null;
		}
		resDto.setPmCouponIssueIds(orderOrderService.getOrderUsePmCouponIssueIdList(resDto.getOdOrderId()));
		return resDto;
	}

	@Override
	public List<PgApprovalOrderDataDto> getPgApprovalOrderDataByOdPaymentMasterId(String odPaymentMasterId) throws Exception {
		return orderOrderService.getPgApprovalOrderDataByOdPaymentMasterId(odPaymentMasterId);
	}

	@Override
	public PgApprovalOrderDataDto getPgApprovalOrderCreateDataByOdid(List<String> odIdList) throws Exception {

		PgApprovalOrderDataDto resDto = orderOrderService.getPgApprovalOrderCreateDataByOdid(odIdList);
		if (resDto == null) {
			return null;
		}
		resDto.setPmCouponIssueIds(orderOrderService.getOrderUsePmCouponIssueIdList(resDto.getOdOrderId()));
		return resDto;
	}

	@Override
	public void convertPgApprovalOrderDataDto(PgApprovalOrderDataDto dto, EtcDataCartDto etcDto) throws Exception {
		// 상담원 직접 결제시 orderPaymnetType 을 etc 에 있는것으로 대체
		if (PaymentType.DIRECT.getCode().equals(dto.getOrderPaymentType())) {
			dto.setOrderPaymentType(etcDto.getPaymentType());
		}

		// 링크프라이스 전송 정보를 받기 위한 전송값 세팅
		try {
			if(StringUtils.isNotEmpty(etcDto.getLpinfo())) {
				dto.setLpinfo(etcDto.getLpinfo());
				if (StringUtils.isNotEmpty(etcDto.getUserAgent())) {
					dto.setUserAgent(etcDto.getUserAgent());
				}
				if (StringUtils.isNotEmpty(etcDto.getIp())) {
					dto.setIp(etcDto.getIp());
				}
				if (StringUtils.isNotEmpty(etcDto.getDeviceType())) {
					dto.setDeviceType(etcDto.getDeviceType());
				}
			}
		} catch (Exception e) {
			log.warn("", e);
		}
	}

	@Override
	public PgEnums.PgErrorType isPgApprovalOrderValidation(PgApprovalOrderDataDto orderData) throws Exception {
		if (orderData == null) {
			return PgEnums.PgErrorType.FAIL_NULL_ORDER_DATA;
		}
		if (!PaymentType.DIRECT.getCode().equals(orderData.getOriginOrderPaymentType())) {

			// 쿠폰 사용 조건 체크
			if (!orderData.getPmCouponIssueIds().isEmpty()) {
				if (!promotionCouponBiz.isUseCouponListValidation(orderData.getUrUserId(), orderData.getPmCouponIssueIds())) {
					return PgEnums.PgErrorType.FAIL_APPROVAL_VALIDATION_COUPON;
				}
			}

			// 적립금
			if (orderData.getPointPrice() > 0) {
				// 사용가능 적립금 조회
				int availablePoint = promotionPointBiz.getPointUsable(orderData.getUrUserId());
				if (availablePoint < orderData.getPointPrice()) {
					return PgEnums.PgErrorType.FAIL_APPROVAL_VALIDATION_POINT;
				}
			}

			// 상품 재고 체크
			List<StockCheckOrderDetailDto> orderGoodsList = orderOrderService.getStockCheckOrderDetailList(orderData.getOdOrderId());
			List<Long> checkOdShippingPriceIds = new ArrayList<Long>();
			for (StockCheckOrderDetailDto goodsDto : orderGoodsList) {
				if (GoodsDeliveryType.SHOP.getCode().equals(goodsDto.getGoodsDeliveryType())) {
					// 매장배송의 경우
					ItemStoreInfoVo itemStoreInfo = goodsGoodsBiz.getItemStoreInfo(goodsDto.getUrStoreId(), goodsDto.getIlItemCd());
					if (itemStoreInfo == null || itemStoreInfo.getStoreStock() < goodsDto.getOrderCnt()) {
						return PgEnums.PgErrorType.FAIL_APPROVAL_VALIDATION_STOCK;
					}
					// 매장 회차 체크
					StoreDeliveryScheduleDto storeDeliveryScheduleDto = storeDeliveryBiz.getStoreScheduleByUrStoreScheduleId(goodsDto.getUrStoreScheduleId(), goodsDto.getDeliveryDt());
					if (storeDeliveryScheduleDto == null || !storeDeliveryScheduleDto.isPossibleSelect()) {
						return PgEnums.PgErrorType.FAIL_STORE_SCHEDULE;
					}
				} else if (GoodsEnums.SaleType.RESERVATION.getCode().equals(goodsDto.getSaleType())) {
					GoodsReserveOptionVo goodsReserveOptionVo = goodsGoodsBiz.getGoodsReserveOption(goodsDto.getIlGoodsReserveOptionId());
					// 예약 상품의 경우 하나라도 재고 없을때는 변경 프로세스 타면 안됨
					if (goodsReserveOptionVo == null || goodsReserveOptionVo.getStockQty() < goodsDto.getOrderCnt()) {
						return PgEnums.PgErrorType.FAIL_APPROVAL_VALIDATION_STOCK;
					}
				} else {
					// 상품별 재고 스케줄
					List<ArrivalScheduledDateDto> arrivalScheduledDateDtoList = goodsGoodsBiz.getArrivalScheduledDateDtoList(
							goodsDto.getUrWarehouseId(), goodsDto.getIlGoodsId(),
							GoodsEnums.GoodsDeliveryType.DAWN.getCode().equals(goodsDto.getGoodsDeliveryType()) ? true : false,
							goodsDto.getOrderCnt(), goodsDto.getGoodsCycleTp());

					// 일일배송 주1일일 경우 (내맘대로 녹즙 제외)
					if (GoodsEnums.SaleType.DAILY.getCode().equals(goodsDto.getSaleType())
						&& !ShoppingEnums.CartPromotionType.GREENJUICE_SELECT.getCode().equals(goodsDto.getPromotionTp())) {
						// 배송주기에 따라 선택일자 요일별로 선택 되어야함
						if(!"Y".equals(goodsDto.getGoodsDailyBulkYn())) {
							String weekCode = null;
							if (GoodsEnums.GoodsCycleType.DAY1_PER_WEEK.getCode().equals(goodsDto.getGoodsCycleTp())) {
								if (goodsDto.getMonCnt() > 0){
									weekCode = GoodsEnums.WeekCodeByGreenJuice.MON.getCode();
								}
								if (goodsDto.getTueCnt() > 0){
									weekCode = GoodsEnums.WeekCodeByGreenJuice.TUE.getCode();
								}
								if (goodsDto.getWedCnt() > 0){
									weekCode = GoodsEnums.WeekCodeByGreenJuice.WED.getCode();
								}
								if (goodsDto.getThuCnt() > 0){
									weekCode = GoodsEnums.WeekCodeByGreenJuice.THU.getCode();
								}
								if (goodsDto.getFriCnt() > 0){
									weekCode = GoodsEnums.WeekCodeByGreenJuice.FRI.getCode();
								}
							}
							arrivalScheduledDateDtoList = goodsGoodsBiz.getArrivalScheduledDateDtoListByWeekCode(arrivalScheduledDateDtoList, goodsDto.getUrWarehouseId(), goodsDto.getGoodsCycleTp(),weekCode);
						}
					}


					goodsDto.setArrivalScheduledDateDtoList(arrivalScheduledDateDtoList);

					//상품별 재고 스케줄에 고객이 선택한 일자가 있는지 확인
					ArrivalScheduledDateDto arrivalScheduledDateDto = goodsGoodsBiz.getArrivalScheduledDateDtoByArrivalScheduledDate(arrivalScheduledDateDtoList, goodsDto.getDeliveryDt());
					if (arrivalScheduledDateDto == null || arrivalScheduledDateDto.getArrivalScheduledDate() == null) {
						if (!checkOdShippingPriceIds.contains(goodsDto.getOdShippingPriceId())) {
							checkOdShippingPriceIds.add(goodsDto.getOdShippingPriceId());
						}
					}
				}
			}

			if (!checkOdShippingPriceIds.isEmpty()) {
				// 배송 정책별로 상품정보 조회하여 교집함 일자 처리하기
				for (Long odShippingPriceId : checkOdShippingPriceIds) {
					List<StockCheckOrderDetailDto> changeGoodsList = orderGoodsList.stream()
							.filter(goodsDto -> (goodsDto.getOdShippingPriceId() == odShippingPriceId && !GoodsEnums.SaleType.RESERVATION.getCode().equals(goodsDto.getSaleType())))
							.collect(Collectors.toList());
					List<LocalDate> intersectionArrivalScheduledDateList = goodsGoodsBiz.intersectionArrivalScheduledDateListByDto(changeGoodsList.stream().map(StockCheckOrderDetailDto::getArrivalScheduledDateDtoList).collect(Collectors.toList()));
					LocalDate afterArrivalScheduledDate = goodsGoodsBiz.getNextArrivalScheduledDate(intersectionArrivalScheduledDateList, changeGoodsList.get(0).getDeliveryDt());
					if (afterArrivalScheduledDate == null) {
						return PgEnums.PgErrorType.FAIL_APPROVAL_VALIDATION_STOCK;
					} else {

						// 아직 정상주문으로 처리되지 않은 주문건만 업데이트
						if("N".equals(orderData.getOrderYn())){
							for (StockCheckOrderDetailDto changeGoods : changeGoodsList) {
								ArrivalScheduledDateDto changeDateDto = goodsGoodsBiz.getArrivalScheduledDateDtoByArrivalScheduledDate(changeGoods.getArrivalScheduledDateDtoList(), afterArrivalScheduledDate);

								orderOrderService.putOrderDetailArrivalScheduledDate(OrderDetlVo.builder()
										.odOrderDetlId(changeGoods.getOdOrderDetlId())
										.orderIfDt(changeDateDto.getOrderDate())
										.shippingDt(changeDateDto.getForwardingScheduledDate())
										.deliveryDt(changeDateDto.getArrivalScheduledDate())
										.build());
							}
						}
					}
				}
			}
		}
		return PgEnums.PgErrorType.SUCCESS;
	}

	@Override
	public PutOrderPaymentCompleteResDto putOrderPaymentComplete(PgApprovalOrderDataDto orderData, InicisApprovalResponseDto approvalResDto)
			throws Exception {

		PgApprovalOrderPaymentDataDto paymentData = convertPgApprovalOrderPaymentData(orderData.getOrderPaymentType(), approvalResDto);

		try {
			return orderProcessBiz.putOrderPaymentComplete(orderData, paymentData);
		} catch (Exception e) {
			log.error("ERROR ====== 이니시스 주문 승인 putOrderPaymentComplete - InicisApprovalResponseDto 에러 ");
			log.error("ERROR ====== 이니시스 주문 승인 orderData ::{}", orderData);
			log.error("ERROR ====== 이니시스 주문 승인 approvalResDto ::{}", approvalResDto);
			log.error("ERROR ====== 이니시스 주문 승인 Exception", e);

			PutOrderPaymentCompleteResDto putOrderPaymentCompleteResDto = new PutOrderPaymentCompleteResDto();
			putOrderPaymentCompleteResDto.setResult(PgEnums.PgErrorType.FAIL_UPDATE_ERROR);
			putOrderPaymentCompleteResDto.setOrderData(orderData);
			putOrderPaymentCompleteResDto.setPaymentData(paymentData);
			return putOrderPaymentCompleteResDto;
		}
	}

	@Override
	public PgApprovalOrderPaymentDataDto convertPgApprovalOrderPaymentData(String paymentType, InicisApprovalResponseDto approvalResDto) throws Exception {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		ObjectMapper objectMapper = new ObjectMapper();
		PgEnums.PgServiceType pgServiceType = PgEnums.PgServiceType.INICIS;
		PgEnums.PgAccountType pgAccountType = PgEnums.PgAccountType.INICIS_BASIC;
		String cardNumber = "";
		String cardQuotaInterest = "";
		String cardQuota = "";
		String virtualAccountNumber = "";
		String info = "";
		String bankName = "";
		LocalDateTime paidDueDate = null;
		String partCancelYn = "Y";
		String escrowYn = "Y";
		String paidHolder = "";
		String cashReceiptYn = "N";
		String cashReceiptType = "";
		String cashReceiptNo = "";
		String cashReceiptAuthNo = "";
		LocalDateTime approvalDate = null;
		if (OrderEnums.PaymentType.VIRTUAL_BANK.getCode().equals(paymentType)) {
			bankName = approvalResDto.getVactBankName();
			virtualAccountNumber = approvalResDto.getVACT_Num();
			paidDueDate = LocalDateTime.parse(approvalResDto.getVACT_Date() + approvalResDto.getVACT_Time(), dateTimeFormatter);
			paidHolder = approvalResDto.getVACT_Name();
		} else if (OrderEnums.PaymentType.BANK.getCode().equals(paymentType)) {
			info = PgEnums.InicisBankCode.findByCode(approvalResDto.getBankCode()).getCodeName();
			approvalDate = LocalDateTime.now();
			cashReceiptYn = InicisCode.CASH_RECEIPT_SUCCESS_PC.getCode().equals(approvalResDto.getCSHR_ResultCode()) ? "Y" : "N";
			cashReceiptType = OrderEnums.CashReceipt.USER.getCode(); //"1".equals(approvalResDto.getCSHR_Type()) ? OrderEnums.CashReceipt.PROOF.getCode() : OrderEnums.CashReceipt.DEDUCTION.getCode();
			cashReceiptNo = "***********";
			cashReceiptAuthNo = approvalResDto.getTid();
		} else {
			cardNumber = approvalResDto.getCardNum();
			info = pgBiz.getPgBankName(pgServiceType.getCode(), paymentType, approvalResDto.getCardCode());
			cardQuotaInterest = "1".equals(approvalResDto.getCARD_Interest()) ? "Y" : "N";
			cardQuota = approvalResDto.getCARD_Quota();
			partCancelYn = "1".equals(approvalResDto.getCARD_PRTC_CODE()) ? "Y" : "N";
			escrowYn = "N";
			approvalDate = LocalDateTime.now();
		}

		PgApprovalOrderPaymentDataDto paymentData = new PgApprovalOrderPaymentDataDto();
		paymentData.setPgAccountType(pgAccountType);
		paymentData.setTid(approvalResDto.getTid());
		paymentData.setAuthCode(approvalResDto.getApplNum());
		paymentData.setCardNumber(cardNumber);
		paymentData.setCardQuotaInterest(cardQuotaInterest);
		paymentData.setCardQuota(cardQuota);
		paymentData.setVirtualAccountNumber(virtualAccountNumber);
		paymentData.setBankName(bankName);
		paymentData.setInfo(info);
		paymentData.setPaidDueDate(paidDueDate);
		paymentData.setPaidHolder(paidHolder);
		paymentData.setPartCancelYn(partCancelYn);
		paymentData.setEscrowYn(escrowYn);
		paymentData.setApprovalDate(approvalDate);
		paymentData.setCashReceiptYn(cashReceiptYn);
		paymentData.setCashReceiptType(cashReceiptType);
		paymentData.setCashReceiptNo(cashReceiptNo);
		paymentData.setCashReceiptAuthNo(cashReceiptAuthNo);
		paymentData.setResponseData(objectMapper.writeValueAsString(approvalResDto));
		return paymentData;
	}

	@Override
	public PutOrderPaymentCompleteResDto putOrderPaymentComplete(PgApprovalOrderDataDto orderData, InicisMobileApprovalResponseDto approvalResDto) throws Exception {

		PgApprovalOrderPaymentDataDto paymentData = convertPgApprovalOrderPaymentData(orderData.getOrderPaymentType(), approvalResDto);

		try {
			return orderProcessBiz.putOrderPaymentComplete(orderData, paymentData);
		} catch (Exception e) {
			log.error("ERROR ====== 이니시스 주문 승인 putOrderPaymentComplete - InicisMobileApprovalResponseDto 에러 ");
			log.error("ERROR ====== 이니시스 주문 승인 orderData ::{}", orderData);
			log.error("ERROR ====== 이니시스 주문 승인 approvalResDto ::{}", approvalResDto);
			log.error("ERROR ====== 이니시스 주문 승인 Exception", e);

			PutOrderPaymentCompleteResDto putOrderPaymentCompleteResDto = new PutOrderPaymentCompleteResDto();
			putOrderPaymentCompleteResDto.setResult(PgEnums.PgErrorType.FAIL_UPDATE_ERROR);
			putOrderPaymentCompleteResDto.setOrderData(orderData);
			putOrderPaymentCompleteResDto.setPaymentData(paymentData);
			return putOrderPaymentCompleteResDto;
		}
	}

	@Override
	public PgApprovalOrderPaymentDataDto convertPgApprovalOrderPaymentData(String paymentType, InicisMobileApprovalResponseDto approvalResDto) throws Exception {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		ObjectMapper objectMapper = new ObjectMapper();
		PgEnums.PgAccountType pgAccountType = PgEnums.PgAccountType.INICIS_BASIC;
		String cardNumber = "";
		String cardQuotaInterest = "";
		String cardQuota = "";
		String virtualAccountNumber = "";
		String info = "";
		String bankName = "";
		LocalDateTime paidDueDate = null;
		String partCancelYn = "Y";
		String escrowYn = "Y";
		String paidHolder = "";
		LocalDateTime approvalDate = null;
		String cashReceiptYn = "N";
		String cashReceiptType = "";
		String cashReceiptNo = "";
		String cashReceiptAuthNo = "";
		if (OrderEnums.PaymentType.VIRTUAL_BANK.getCode().equals(paymentType)) {
			bankName = approvalResDto.getP_FN_NM();
			virtualAccountNumber = approvalResDto.getP_VACT_NUM();
			paidDueDate = LocalDateTime.parse(approvalResDto.getP_VACT_DATE() + approvalResDto.getP_VACT_TIME(),
					dateTimeFormatter);
			paidHolder = approvalResDto.getP_VACT_NAME();
		} else if (OrderEnums.PaymentType.BANK.getCode().equals(paymentType)) {
			info = approvalResDto.getP_FN_NM();
			approvalDate = LocalDateTime.now();
			cashReceiptYn = InicisCode.CASH_RECEIPT_SUCCESS_MO.getCode().equals(approvalResDto.getP_CSHR_CODE()) ? "Y" : "N";
			cashReceiptType = OrderEnums.CashReceipt.USER.getCode();
			//"1".equals(approvalResDto.getP_CSHR_TYPE()) ? OrderEnums.CashReceipt.PROOF.getCode() : OrderEnums.CashReceipt.DEDUCTION.getCode();
			cashReceiptNo = approvalResDto.getP_CSHR_AUTH_NO();
			cashReceiptAuthNo = approvalResDto.getP_TID();
		} else {
			cardNumber = approvalResDto.getP_CARD_NUM();
			info = approvalResDto.getP_FN_NM();
			cardQuotaInterest = "1".equals(approvalResDto.getP_CARD_INTEREST()) ? "Y" : "N";
			cardQuota = approvalResDto.getP_RMESG2();
			partCancelYn = "1".equals(approvalResDto.getP_CARD_PRTC_CODE()) ? "Y" : "N";
			escrowYn = "N";
			approvalDate = LocalDateTime.now();
		}

		PgApprovalOrderPaymentDataDto paymentData = new PgApprovalOrderPaymentDataDto();
		paymentData.setPgAccountType(pgAccountType);
		paymentData.setTid(approvalResDto.getP_TID());
		paymentData.setAuthCode(approvalResDto.getP_AUTH_NO());
		paymentData.setCardNumber(cardNumber);
		paymentData.setCardQuotaInterest(cardQuotaInterest);
		paymentData.setCardQuota(cardQuota);
		paymentData.setVirtualAccountNumber(virtualAccountNumber);
		paymentData.setBankName(bankName);
		paymentData.setInfo(info);
		paymentData.setPaidDueDate(paidDueDate);
		paymentData.setPaidHolder(paidHolder);
		paymentData.setPartCancelYn(partCancelYn);
		paymentData.setEscrowYn(escrowYn);
		paymentData.setApprovalDate(approvalDate);
		paymentData.setCashReceiptYn(cashReceiptYn);
		paymentData.setCashReceiptNo(cashReceiptNo);
		paymentData.setCashReceiptType(cashReceiptType);
		paymentData.setCashReceiptAuthNo(cashReceiptAuthNo);
		paymentData.setResponseData(objectMapper.writeValueAsString(approvalResDto));
		return paymentData;
	}

	@Override
	public PutOrderPaymentCompleteResDto putOrderPaymentComplete(PgApprovalOrderDataDto orderData, KcpApprovalResponseDto approvalResDto) throws Exception {

		PgApprovalOrderPaymentDataDto paymentData = convertPgApprovalOrderPaymentData(orderData.getOrderPaymentType(), approvalResDto);

		try {
			return orderProcessBiz.putOrderPaymentComplete(orderData, paymentData);
		} catch (Exception e) {
			log.error("ERROR ====== KCP 주문 승인 putOrderPaymentComplete - KcpApprovalResponseDto 에러 ");
			log.error("ERROR ====== KCP 주문 승인 orderData ::{}", orderData);
			log.error("ERROR ====== KCP 주문 승인 approvalResDto ::{}", approvalResDto);
			log.error("ERROR ====== KCP 주문 승인 Exception", e);

			PutOrderPaymentCompleteResDto putOrderPaymentCompleteResDto = new PutOrderPaymentCompleteResDto();
			putOrderPaymentCompleteResDto.setResult(PgEnums.PgErrorType.FAIL_UPDATE_ERROR);
			putOrderPaymentCompleteResDto.setOrderData(orderData);
			putOrderPaymentCompleteResDto.setPaymentData(paymentData);
			return putOrderPaymentCompleteResDto;
		}
	}

	@Override
	public PgApprovalOrderPaymentDataDto convertPgApprovalOrderPaymentData(String paymentType, KcpApprovalResponseDto approvalResDto) throws Exception {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		ObjectMapper objectMapper = new ObjectMapper();
		PgEnums.PgAccountType pgAccountType = null;
		if (PaymentType.isSimplePay(paymentType)) {
			pgAccountType = PgEnums.PgAccountType.KCP_SIMPLE;
		} else {
			pgAccountType = PgEnums.PgAccountType.KCP_BASIC;
		}
		String cardNumber = "";
		String cardQuotaInterest = "";
		String cardQuota = "";
		String virtualAccountNumber = "";
		String info = "";
		String bankName = "";
		LocalDateTime paidDueDate = null;
		String paidHolder = "";
		LocalDateTime approvalDate = null;
		String partCancelYn = "Y";
		String cashReceiptYn = "N";
		String cashReceiptType = "";
		String cashReceiptNo = "";
		String cashReceiptAuthNo = "";
		if (OrderEnums.PaymentType.VIRTUAL_BANK.getCode().equals(paymentType)) {
			bankName = approvalResDto.getBankname();
			virtualAccountNumber = approvalResDto.getAccount();
			paidDueDate = LocalDateTime.parse(approvalResDto.getVa_date(), dateTimeFormatter);
			paidHolder = approvalResDto.getDepositor();
		} else if (OrderEnums.PaymentType.BANK.getCode().equals(paymentType)) {
			info = approvalResDto.getBank_name();
			approvalDate = LocalDateTime.now();
			cashReceiptNo = approvalResDto.getCash_no();
			cashReceiptAuthNo = approvalResDto.getCash_authno();
			cashReceiptType = OrderEnums.CashReceipt.USER.getCode();// "1".equals(approvalResDto.getCash_tr_code()) ? OrderEnums.CashReceipt.PROOF.getCode() : OrderEnums.CashReceipt.DEDUCTION.getCode();
			if (!cashReceiptNo.isEmpty() && !cashReceiptAuthNo.isEmpty()) {
				cashReceiptYn = "Y";
			}
		} else {
			info = approvalResDto.getCard_name();
			cardNumber = approvalResDto.getCard_no();
			cardQuotaInterest = approvalResDto.getNoinf();
			cardQuota = approvalResDto.getQuota();
			approvalDate = LocalDateTime.now();
			partCancelYn = approvalResDto.getPartcanc_yn();
		}

		PgApprovalOrderPaymentDataDto paymentData = new PgApprovalOrderPaymentDataDto();
		paymentData.setPgAccountType(pgAccountType);
		paymentData.setTid(approvalResDto.getTno());
		paymentData.setAuthCode(approvalResDto.getApp_no());
		paymentData.setCardNumber(cardNumber);
		paymentData.setCardQuotaInterest(cardQuotaInterest);
		paymentData.setCardQuota(cardQuota);
		paymentData.setVirtualAccountNumber(virtualAccountNumber);
		paymentData.setBankName(bankName);
		paymentData.setInfo(info);
		paymentData.setPaidDueDate(paidDueDate);
		paymentData.setPaidHolder(paidHolder);
		paymentData.setPartCancelYn(partCancelYn);
		paymentData.setEscrowYn(approvalResDto.getEscw_yn());
		paymentData.setApprovalDate(approvalDate);
		paymentData.setCashReceiptYn(cashReceiptYn);
		paymentData.setCashReceiptNo(cashReceiptNo);
		paymentData.setCashReceiptType(cashReceiptType);
		paymentData.setCashReceiptAuthNo(cashReceiptAuthNo);
		paymentData.setResponseData(objectMapper.writeValueAsString(approvalResDto));
		return paymentData;
	}

	@Override
	public PutOrderPaymentCompleteResDto putOrderPaymentComplete(PgApprovalOrderDataDto orderData, InicisNonAuthenticationCartPayRequestDto payReqDto, InicisNonAuthenticationCartPayResponseDto approvalResDto) throws Exception {

		ObjectMapper objectMapper = new ObjectMapper();
		PgEnums.PgAccountType pgAccountType = PgEnums.PgAccountType.INICIS_ADMIN;

		PgApprovalOrderPaymentDataDto paymentData = new PgApprovalOrderPaymentDataDto();
		paymentData.setPgAccountType(pgAccountType);
		paymentData.setTid(approvalResDto.getTid());
		paymentData.setAuthCode(approvalResDto.getPayAuthCode());
		paymentData.setCardNumber(payReqDto.getCardNumber());
		paymentData.setCardQuotaInterest("N");
		paymentData.setCardQuota(approvalResDto.getPayAuthQuota());
		paymentData.setVirtualAccountNumber("");
		paymentData.setBankName("");
		paymentData.setInfo(InicisCardCode.findByCode(approvalResDto.getCardCode()).getCodeName());
//		paymentData.setPaidDueDate(null);
		paymentData.setPaidHolder("");
		paymentData.setPartCancelYn("1".equals(approvalResDto.getPrtcCode()) ? "Y" : "N");
		paymentData.setEscrowYn("N");
		paymentData.setApprovalDate(LocalDateTime.now());
		paymentData.setCashReceiptYn("N");
		paymentData.setCashReceiptType("");
		paymentData.setCashReceiptNo("");
		paymentData.setCashReceiptAuthNo("");
		paymentData.setResponseData(objectMapper.writeValueAsString(approvalResDto));

		try {
			return orderProcessBiz.putOrderPaymentComplete(orderData, paymentData);
		} catch (Exception e) {
			log.error("ERROR ====== 이니시스 주문 승인 putOrderPaymentComplete - InicisNonAuthenticationCartPayResponseDto 에러 ");
			log.error("ERROR ====== 이니시스 주문 승인 orderData ::{}", orderData);
			log.error("ERROR ====== 이니시스 주문 승인 approvalResDto ::{}", approvalResDto);
			log.error("ERROR ====== 이니시스 주문 승인 Exception", e);

			PutOrderPaymentCompleteResDto putOrderPaymentCompleteResDto = new PutOrderPaymentCompleteResDto();
			putOrderPaymentCompleteResDto.setResult(PgEnums.PgErrorType.FAIL_UPDATE_ERROR);
			putOrderPaymentCompleteResDto.setOrderData(orderData);
			putOrderPaymentCompleteResDto.setPaymentData(paymentData);
			return putOrderPaymentCompleteResDto;
		}
	}

	@Override
	public PutOrderPaymentCompleteResDto putOrderPaymentComplete(PgApprovalOrderDataDto orderData, VirtualAccountDataResponseDto virtualAccountResDto) throws Exception{

		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		ObjectMapper objectMapper = new ObjectMapper();
		PgEnums.PgServiceType pgServiceType = PgEnums.PgServiceType.INICIS;
		PgEnums.PgAccountType pgAccountType = PgEnums.PgAccountType.INICIS_BASIC;
		String cardNumber = "";
		String cardQuotaInterest = "";
		String cardQuota = "";
		String virtualAccountNumber = "";
		String info = "";
		String bankName = "";
		LocalDateTime paidDueDate = null;
		String partCancelYn = "Y";
		String escrowYn = "Y";
		String paidHolder = "";
		String cashReceiptYn = "N";
		String cashReceiptType = "";
		String cashReceiptNo = "";
		String cashReceiptAuthNo = "";
		LocalDateTime approvalDate = null;

		bankName = virtualAccountResDto.getBankName();
		virtualAccountNumber = virtualAccountResDto.getAccount();
		paidDueDate = LocalDateTime.parse(virtualAccountResDto.getValidDate().substring(0, 4) + "-" + virtualAccountResDto.getValidDate().substring(4, 6) + "-" + virtualAccountResDto.getValidDate().substring(6, 8) + " " +
				virtualAccountResDto.getValidDate().substring(8, 10) + ":" + virtualAccountResDto.getValidDate().substring(10, 12) + ":" + virtualAccountResDto.getValidDate().substring(12, 14), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		paidHolder = virtualAccountResDto.getDepositor();

		PgApprovalOrderPaymentDataDto paymentData = new PgApprovalOrderPaymentDataDto();
		paymentData.setPgAccountType(pgAccountType);
		paymentData.setTid(virtualAccountResDto.getTid());
		paymentData.setAuthCode("");
		paymentData.setCardNumber(cardNumber);
		paymentData.setCardQuotaInterest(cardQuotaInterest);
		paymentData.setCardQuota(cardQuota);
		paymentData.setVirtualAccountNumber(virtualAccountNumber);
		paymentData.setBankName(bankName);
		paymentData.setInfo(info);
		paymentData.setPaidDueDate(paidDueDate);
		paymentData.setPaidHolder(paidHolder);
		paymentData.setPartCancelYn(partCancelYn);
		paymentData.setEscrowYn(escrowYn);
		paymentData.setApprovalDate(approvalDate);
		paymentData.setCashReceiptYn(cashReceiptYn);
		paymentData.setCashReceiptType(cashReceiptType);
		paymentData.setCashReceiptNo(cashReceiptNo);
		paymentData.setCashReceiptAuthNo(cashReceiptAuthNo);
		paymentData.setResponseData(objectMapper.writeValueAsString(virtualAccountResDto));

		return orderProcessBiz.putOrderPaymentComplete(orderData, paymentData);

	}

	@Override
	public void putVirtualBankOrderPaymentComplete(PgApprovalOrderDataDto orderData, KcpVirtualAccountReturnRequestDto reqDto) throws Exception {
		putVirtualBankOrderPaymentDataDto paymentData = new putVirtualBankOrderPaymentDataDto();
		ObjectMapper objectMapper = new ObjectMapper();
		paymentData.setAuthCode("");
		paymentData.setApprovalDate(LocalDateTime.now());
		paymentData.setResponseData(objectMapper.writeValueAsString(reqDto));

		String cashReceiptYn = "N";
		String cashReceiptNo = reqDto.getCash_no();
		String cashReceiptAuthNo = reqDto.getCash_a_no();
		if (!cashReceiptNo.isEmpty() && !cashReceiptAuthNo.isEmpty()) {
			cashReceiptYn = "Y";
		}
		paymentData.setCashReceiptYn(cashReceiptYn);
		paymentData.setCashReceiptType(OrderEnums.CashReceipt.USER.getCode());
		paymentData.setCashReceiptNo(cashReceiptNo);
		paymentData.setCashReceiptAuthNo(cashReceiptAuthNo);

		putVirtualBankOrderPaymentComplete(orderData, paymentData);
	}

	@Override
	public void putVirtualBankOrderPaymentComplete(PgApprovalOrderDataDto orderData, InicisVirtualAccountReturnRequestDto reqDto) throws Exception {
		putVirtualBankOrderPaymentDataDto paymentData = new putVirtualBankOrderPaymentDataDto();
		ObjectMapper objectMapper = new ObjectMapper();
		paymentData.setAuthCode("");
		paymentData.setApprovalDate(LocalDateTime.now());
		paymentData.setResponseData(objectMapper.writeValueAsString(reqDto));

		String cashReceiptYn = "N";
		String cashReceiptNo = reqDto.getNo_cshr_appl();
		String cashReceiptAuthNo = reqDto.getNo_cshr_tid();
		if (cashReceiptNo == null || cashReceiptAuthNo == null) {
			cashReceiptNo = "";
		} else if (!cashReceiptNo.isEmpty() && !cashReceiptAuthNo.isEmpty()) {
			cashReceiptYn = "Y";
		}
		paymentData.setCashReceiptYn(cashReceiptYn);
		paymentData.setCashReceiptType(OrderEnums.CashReceipt.USER.getCode());
		paymentData.setCashReceiptNo(cashReceiptNo);
		paymentData.setCashReceiptAuthNo(cashReceiptAuthNo);

		putVirtualBankOrderPaymentComplete(orderData, paymentData);
	}

	@Override
	public void putVirtualBankOrderPaymentComplete(PgApprovalOrderDataDto orderData, InicisNotiRequestDto reqDto) throws Exception {
		putVirtualBankOrderPaymentDataDto paymentData = new putVirtualBankOrderPaymentDataDto();
		ObjectMapper objectMapper = new ObjectMapper();
		paymentData.setAuthCode(reqDto.getP_AUTH_NO());
		paymentData.setApprovalDate(LocalDateTime.now());
		paymentData.setResponseData(objectMapper.writeValueAsString(reqDto));

		String cashReceiptYn = "N";
		String cashReceiptNo = "";
		String cashReceiptAuthNo = reqDto.getP_TID();
		if (cashReceiptAuthNo != null && !cashReceiptAuthNo.isEmpty()) {
			cashReceiptNo = reqDto.getP_CSHR_AUTH_NO();
			cashReceiptYn = "Y";
		}
		paymentData.setCashReceiptYn(cashReceiptYn);
		//paymentData.setCashReceiptType("1".equals(reqDto.getP_CSHR_TYPE()) ? OrderEnums.CashReceipt.PROOF.getCode() : OrderEnums.CashReceipt.DEDUCTION.getCode());
		paymentData.setCashReceiptType(OrderEnums.CashReceipt.USER.getCode());
		paymentData.setCashReceiptNo(cashReceiptNo);
		paymentData.setCashReceiptAuthNo(cashReceiptAuthNo);

		putVirtualBankOrderPaymentComplete(orderData, paymentData);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
	public void putVirtualBankOrderPaymentComplete(PgApprovalOrderDataDto orderData, putVirtualBankOrderPaymentDataDto paymentData) throws Exception {

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

		// 가상계좌 입금 처리
		orderRegistrationBiz.payApprovalOrder(OrderApprovalDto.builder()
				.orderDetlVo(
						OrderDetlVo.builder().odOrderId(orderData.getOdOrderId()).orderStatusCd(OrderEnums.OrderStatus.INCOM_COMPLETE.getCode()).build())
				.orderPaymentMasterVo(OrderPaymentMasterVo.builder().odPaymentMasterId(orderData.getOdPaymentMasterId())
						.status(OrderEnums.OrderStatus.INCOM_COMPLETE.getCode())
						.authCd(paymentData.getAuthCode())
						.approvalDt(paymentData.getApprovalDate())
						.responseData(paymentData.getResponseData()).build())
				.orderCashReceipt(orderCashReceiptVo)
				.build());

		// 가상계좌 입금완료 자동메일 발송
		try {
			OrderInfoForEmailResultDto orderInfoForEmailResultDto = orderEmailBiz.getOrderInfoForEmail(orderData.getOdOrderId());
			orderEmailSendBiz.orderDepositComplete(orderInfoForEmailResultDto);
		} catch (Exception e) {
			log.error("ERROR ====== 가상계좌 입금완료 자동메일 발송 오류 orderData ::{}", orderData);
			log.error("ERROR ====== 가상계좌 입금완료 자동메일 발송 오류 Exception", e);
		}

		// 가상계좌 입금완료 링크프라이스 실적 결제정보 전송
		try {
			//실결제 금액이 0원 이어도 적립금 금액이 0보다 크다면 링크프라이스에 전송
			if(orderData.getPaymentPrice() > 0 || orderData.getPointPrice() > 0) {
				linkPriceBiz.virtualBankSendLinkPrice(orderData);
			}
		} catch (Exception e) {
			log.warn("ERROR ====== 가상계좌 입금완료 링크프라이 실적 결제정보 전송 오류 orderData ::{}", orderData);
			log.warn("ERROR ====== 가상계좌 입금완료 링크프라이 실적 결제정보 전송 오류 Exception", e);
		}
	}


	@Override
	public int putOrderDetailArrivalScheduledDate(OrderDetlVo orderDetlVo) throws Exception {
		return orderOrderService.putOrderDetailArrivalScheduledDate(orderDetlVo);
	}

	@Override
	public List<StockCheckOrderDetailDto> getStockCheckOrderDetailList(Long odOrderId) throws Exception {
		return orderOrderService.getStockCheckOrderDetailList(odOrderId);
	}

	@Override
	public int putOrderDetailGoodsDeliveryType(Long odShippingZoneId, String goodsDeliveryType, String orderStatusDetailType) throws Exception {
		return orderOrderService.putOrderDetailGoodsDeliveryType(odShippingZoneId, goodsDeliveryType, orderStatusDetailType);
	}

	@Override
	public int putOrderDetailGoodsDeliveryTypeByOdOrderDetlId(Long odOrderDetlId, String goodsDeliveryType, String orderStatusDetailType) throws Exception {
		return orderOrderService.putOrderDetailGoodsDeliveryTypeByOdOrderDetlId(odOrderDetlId, goodsDeliveryType, orderStatusDetailType);
	}

	@Override
	public List<ArrivalScheduledDateDto> getOrderDetailDisposalGoodsArrivalScheduledList(List<Long> odOrderDetlIds) throws Exception {
		return orderOrderService.getOrderDetailDisposalGoodsArrivalScheduledList(odOrderDetlIds);
	}

	@Override
	public List<StockCheckOrderDetailDto> getStockCheckOrderDetailListByOdOrderDetlId(Long odOrderDetlId) throws Exception {
		return orderOrderService.getStockCheckOrderDetailListByOdOrderDetlId(odOrderDetlId);
	}

	/**
	 * 일일상품택배배송 여부
	 */
	@Override
	public boolean isOrderDetailDailyDelivery(Long odOrderDetlId) throws Exception {
		return orderOrderService.isOrderDetailDailyDelivery(odOrderDetlId);
	}

	/**
	 * 현금영수증 발급내역 조회
	 */
	@Override
	public CashReceiptIssuedListResponseDto getCashReceiptIssuedList(CashReceiptIssuedListRequestDto cashReceiptIssuedListRequestDto) throws Exception {
		return orderOrderService.getCashReceiptIssuedList(cashReceiptIssuedListRequestDto);
	}

	/**
	 * 현금영수증 발급
	 */
	@Override
	public ApiResult<?> cashReceiptIssue(CashReceiptIssueRequestDto cashReceiptIssueRequestDto) throws Exception {

		// PG 서비스 조회
		String pgServiceTypeString = PgEnums.PgAccountType.findByCode(cashReceiptIssueRequestDto.getPgService()).getPgServiceType();
		PgServiceType pgServiceType = PgEnums.PgServiceType.findByCode(pgServiceTypeString);
		PgAbstractService pgService = pgBiz.getService(pgServiceType);

		// 주문정보 조회
		MallOrderDto orderDto = mallOrderDetailBiz.getOrder(cashReceiptIssueRequestDto.getOdOrderId(), cashReceiptIssueRequestDto.getUrUserId(), cashReceiptIssueRequestDto.getGuestCi());

		// 현금영수증 발급 요청 파라미터 세팅
		ReceiptIssueRequestDto receiptIssueRequestDto = orderOrderService.setReceiptIssueRequestDto(cashReceiptIssueRequestDto, orderDto);

		// 현금영수증 발급
		ReceiptIssueResponseDto receiptIssueResponseDto = pgService.receiptIssue(receiptIssueRequestDto);
		if (!receiptIssueResponseDto.isSuccess()) {
			return ApiResult.result(receiptIssueResponseDto, BaseEnums.Default.FAIL);
		}

		// 현금영수증 발급 정보 저장
		OrderCashReceiptVo orderCashReceiptVo = OrderCashReceiptVo.builder()
				.odOrderId(cashReceiptIssueRequestDto.getOdOrderId())
				.odPaymentMasterId(cashReceiptIssueRequestDto.getOdPaymentMasterId())
				.cashReceiptNo(receiptIssueResponseDto.getReceiptNo())
				.cashReceiptAuthNo(receiptIssueResponseDto.getTid())
				.cashReceiptType(cashReceiptIssueRequestDto.getReceiptType())
				.cashPrice(cashReceiptIssueRequestDto.getPaymentPrice())
				.cashNo(cashReceiptIssueRequestDto.getRegNumber())
				.build();
		orderRegistrationBiz.addOrderCashReceipt(orderCashReceiptVo);

		return ApiResult.success();
	}

	/**
	 * 현금영수증 발급내역 엑셀다운로드
	 */
	@Override
	public ExcelDownloadDto getCashReceiptIssuedListExportExcel(CashReceiptIssuedListRequestDto dto) throws Exception {
		return orderOrderService.getCashReceiptIssuedListExportExcel(dto);
	}

	@Override
	public boolean isOdidValuePaymentMasterId(String odid) throws Exception {
		return orderOrderService.isOdidValuePaymentMasterId(odid);
	}

	@Override
	public boolean isOdidValueAddPaymentMasterId(String odid) throws Exception {
		return orderOrderService.isOdidValueAddPaymentMasterId(odid);
	}

	@Override
	public String getPaymentMasterIdByOdid(String odid) throws Exception {
		return orderOrderService.getPaymentMasterIdByOdid(odid);
	}

	@Override
	public void putWarehouseDailyShippingCount(String odOrderIds) throws Exception {
		List<Long> list = Arrays.asList(odOrderIds.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
		if (!list.isEmpty()) {
			orderOrderService.putWarehouseDailyShippingCount(list);
		}
	}

	@Override
	public void putWarehouseDailyShippingCount(List<Long> odOrderIds) throws Exception {
		if (!odOrderIds.isEmpty()) {
			orderOrderService.putWarehouseDailyShippingCount(odOrderIds);
		}
	}

	@Override
	public PaymentInfoResponseDto getPaymentInfo(String odid, String urUserId, String guestCi) throws Exception{
		return orderOrderService.getPaymentInfo(odid, urUserId, guestCi);
	}
}
