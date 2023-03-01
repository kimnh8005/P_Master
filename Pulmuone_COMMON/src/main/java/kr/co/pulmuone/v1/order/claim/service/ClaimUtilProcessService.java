package kr.co.pulmuone.v1.order.claim.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import kr.co.pulmuone.v1.api.cjlogistics.dto.CJLogisticsOrderAcceptDto;
import kr.co.pulmuone.v1.api.lotteglogis.dto.LotteGlogisClientOrderRequestDto;
import kr.co.pulmuone.v1.api.lotteglogis.dto.LotteGlogisClientOrderResponseDto;
import kr.co.pulmuone.v1.api.lotteglogis.service.LotteGlogisBiz;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.*;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.GoodsDeliveryType;
import kr.co.pulmuone.v1.comm.enums.PgEnums.InicisCardCode;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mapper.order.claim.ClaimProcessMapper;
import kr.co.pulmuone.v1.comm.mapper.order.claim.ClaimRequestMapper;
import kr.co.pulmuone.v1.comm.mapper.order.status.OrderStatusMapper;
import kr.co.pulmuone.v1.comm.util.*;
import kr.co.pulmuone.v1.goods.stock.dto.StockOrderRequestDto;
import kr.co.pulmuone.v1.goods.stock.service.GoodsStockOrderBiz;
import kr.co.pulmuone.v1.order.claim.dto.*;
import kr.co.pulmuone.v1.order.claim.dto.vo.*;
import kr.co.pulmuone.v1.order.create.dto.GoodsInfoDto;
import kr.co.pulmuone.v1.order.create.dto.OrderClaimCardPayRequestDto;
import kr.co.pulmuone.v1.order.create.dto.OrderClaimCardPayResponseDto;
import kr.co.pulmuone.v1.order.create.service.OrderCreateBiz;
import kr.co.pulmuone.v1.order.delivery.dto.vo.OrderTrackingNumberVo;
import kr.co.pulmuone.v1.order.delivery.service.OrderBulkTrackingNumberBiz;
import kr.co.pulmuone.v1.order.email.dto.OrderInfoForEmailResultDto;
import kr.co.pulmuone.v1.order.email.service.OrderEmailBiz;
import kr.co.pulmuone.v1.order.email.service.OrderEmailSendBiz;
import kr.co.pulmuone.v1.order.order.dto.PgApprovalOrderPaymentDataDto;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlHistVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderPaymentMasterVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderPaymentVo;
import kr.co.pulmuone.v1.order.registration.dto.OrderDetlDiscountInfoDto;
import kr.co.pulmuone.v1.order.registration.service.OrderRegistrationBiz;
import kr.co.pulmuone.v1.pg.dto.BasicDataRequestDto;
import kr.co.pulmuone.v1.pg.dto.VirtualAccountDataResponseDto;
import kr.co.pulmuone.v1.pg.service.PgAbstractService;
import kr.co.pulmuone.v1.pg.service.PgBiz;
import kr.co.pulmuone.v1.pg.service.inicis.dto.InicisNonAuthenticationCartPayRequestDto;
import kr.co.pulmuone.v1.pg.service.inicis.dto.InicisNonAuthenticationCartPayResponseDto;
import kr.co.pulmuone.v1.pg.service.inicis.service.InicisPgService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ClaimUtilProcessService {

	@Autowired
	private ClaimUtilRefundService claimUtilRefundService;

	@Autowired
	private OrderRegistrationBiz orderRegistrationBiz;

	@Autowired
	private OrderCreateBiz orderCreateBiz;

	@Autowired
	private OrderBulkTrackingNumberBiz orderBulkTrackingNumberBiz;

	@Autowired
	private OrderEmailBiz orderEmailBiz;

	@Autowired
	private OrderEmailSendBiz orderEmailSendBiz;

	@Autowired
	private LotteGlogisBiz lotteGlogisBiz;

	@Autowired
	private GoodsStockOrderBiz goodsStockOrderBiz;

	@Autowired
	private InicisPgService inicisPgService;

	@Autowired
	private PgBiz pgBiz;

	@Autowired
	private ClaimProcessMapper claimProcessMapper;

	@Autowired
	private OrderStatusMapper orderStatusMapper;

	@Autowired
	private ClaimRequestMapper claimRequestMapper;

	ObjectMapper objectMapper = new ObjectMapper();
	/**
	 * 로그인 정보 가져오는 함수
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	public void getUserInfo(OrderClaimRegisterRequestDto reqDto) throws Exception {
		// BOS 및 BATCH 실행 시 주문자ID Setting
		if (reqDto.getFrontTp() == OrderClaimEnums.OrderClaimFrontTpCd.FRONT_TP_BOS.getCodeValue() || reqDto.getFrontTp() == OrderClaimEnums.OrderClaimFrontTpCd.FRONT_TP_BATCH.getCodeValue()) {
			// BOS 일 경우만
			if(reqDto.getFrontTp() == OrderClaimEnums.OrderClaimFrontTpCd.FRONT_TP_BOS.getCodeValue()) {
				UserVo userVo = SessionUtil.getBosUserVO();
				reqDto.setUrUserId(userVo.getUserId());
			}
			// 주문 회원 PK 얻어온다
			reqDto.setCustomUrUserId(String.valueOf(claimRequestMapper.getOrderUrUserId(reqDto.getOdOrderId())));
		}
		else if(reqDto.getFrontTp() == OrderClaimEnums.OrderClaimFrontTpCd.FRONT_TP_FRONT.getCodeValue()){
			if(StringUtils.isEmpty(reqDto.getUrUserId())) {
				if (reqDto.isNonMember()) {
					reqDto.setUrUserId(String.valueOf(Constants.GUEST_CREATE_USER_ID));
					reqDto.setLoginId(String.valueOf(Constants.GUEST_CREATE_USER_ID));
					reqDto.setCustomUrUserId(String.valueOf(Constants.GUEST_CREATE_USER_ID));
				} else {
					BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
					log.debug("urUserId :: <{}>", buyerVo.getUrUserId());
					if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
						if (StringUtil.isEmpty(reqDto.getCustomUrUserId())) {
							throw new BaseException(UserEnums.Buyer.NEED_LOGIN);
						}
					} else {
						reqDto.setUrUserId(buyerVo.getUrUserId());
						reqDto.setLoginId(buyerVo.getLoginId());
						reqDto.setCustomUrUserId(buyerVo.getUrUserId());
					}
				}
			}
		}
	}

	/**
	 * orderClaimRegister 객체에서 필요한 값을 뽑아서 orderClaimView 로 만든다
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	public static OrderClaimViewRequestDto getOrderClaimViewReq(OrderClaimRegisterRequestDto reqDto) throws Exception {

		log.debug("-------getOrderClaimViewReq In------");
		OrderClaimViewRequestDto claimViewReqDto = new OrderClaimViewRequestDto();
		claimViewReqDto.setOdOrderId(reqDto.getOdOrderId());
		claimViewReqDto.setOdClaimId(reqDto.getOdClaimId());
		claimViewReqDto.setReturnsYn(reqDto.getReturnsYn());
		claimViewReqDto.setTargetTp(reqDto.getTargetTp());
		claimViewReqDto.setOrderStatusCd(reqDto.getOrderStatusCd());
		claimViewReqDto.setClaimStatusTp(reqDto.getClaimStatusTp());
		claimViewReqDto.setClaimStatusCd(reqDto.getClaimStatusCd());
		claimViewReqDto.setRecvZipCd(reqDto.getRecvZipCd());
		claimViewReqDto.setStatus(reqDto.getStatus());
		claimViewReqDto.setClaimType(reqDto.getClaimType());
		claimViewReqDto.setUndeliveredClaimYn(reqDto.getUndeliveredClaimYn());
		List<OrderClaimSearchGoodsDto> orderClaimSearchGoodsList = new ArrayList<>();
		List<OrderClaimGoodsInfoDto> goodsInfoList = reqDto.getGoodsInfoList();

		for(OrderClaimGoodsInfoDto goodsInfo : goodsInfoList) {

			OrderClaimSearchGoodsDto orderClaimSearchGoods = new OrderClaimSearchGoodsDto();
			orderClaimSearchGoods.setOdOrderId(goodsInfo.getOdOrderId());
			orderClaimSearchGoods.setOdOrderDetlId(goodsInfo.getOdOrderDetlId());
			orderClaimSearchGoods.setOdClaimDetlId(goodsInfo.getOdClaimDetlId());
			orderClaimSearchGoods.setClaimCnt(goodsInfo.getClaimCnt());
			orderClaimSearchGoods.setCancelCnt(goodsInfo.getCancelCnt());
			orderClaimSearchGoods.setOrderCnt(goodsInfo.getOrderCnt());
			orderClaimSearchGoods.setOrderPrice(goodsInfo.getOrderPrice());
			orderClaimSearchGoods.setGoodsCouponPrice(goodsInfo.getGoodsCouponPrice());
			orderClaimSearchGoods.setPaidPrice(goodsInfo.getPaidPrice());
			orderClaimSearchGoods.setUrWarehouseId(goodsInfo.getUrWarehouseId());
			orderClaimSearchGoods.setClaimGoodsYn(goodsInfo.getClaimGoodsYn());

			orderClaimSearchGoodsList.add(orderClaimSearchGoods);
		}

		claimViewReqDto.setGoodSearchList(orderClaimSearchGoodsList);

		log.debug("-------getOrderClaimViewReq Out------");
		return claimViewReqDto;
	}


	/**
	 * 클레임정보 세팅
	 * @return
	 */
	public static ClaimVo setClaimVo(OrderClaimRegisterRequestDto reqDto, List<OrderClaimGoodsInfoDto> goodsList) {
		long approvalId = OrderEnums.OrderStatus.RETURN_ING.getCode().equals(reqDto.getClaimStatusCd()) ? Long.parseLong(reqDto.getUrUserId()) : 0;

		String goodsNm = "";
		long ilGoodsId = 0;
		if(goodsList != null) {
			List<OrderClaimGoodsInfoDto> packItemList = goodsList.stream().filter(x -> (GoodsEnums.GoodsType.PACKAGE.getCode().equals(x.getGoodsTpCd()) 	||	// 묶음상품
																						GoodsEnums.GoodsType.DAILY.getCode().equals(x.getGoodsTpCd()) 		||	// 녹즙내맘대로
																						GoodsEnums.GoodsType.DISPOSAL.getCode().equals(x.getGoodsTpCd())	||	// 균일가행사
																						GoodsEnums.GoodsType.NORMAL.getCode().equals(x.getGoodsTpCd())) 	&&	// 균일가골라담기
																						x.getOdOrderDetlDepthId() == 1)											// Depth 정보가 1 인 것
																			.collect(Collectors.toList());
			if (!packItemList.isEmpty()) {
				// 취소 상품 정보가 존재할 경우
				goodsNm = packItemList.get(0).getGoodsNm();
				if (packItemList.size() > 1) {
					goodsNm = goodsNm + " 외 " + (packItemList.size() - 1) + "건";
				}
				ilGoodsId = packItemList.get(0).getIlGoodsId();
			}
		}
		if(ilGoodsId == 0) {
			List<OrderClaimGoodsInfoDto> goodsInfoList = reqDto.getGoodsInfoList();
			goodsNm = reqDto.getGoodsNm();
			ilGoodsId = 0;
			// 취소 상품 정보가 존재할 경우
			if (!goodsInfoList.isEmpty()) {
				goodsNm = goodsInfoList.get(0).getGoodsNm();
				if (goodsInfoList.size() > 1) {
					goodsNm = goodsNm + " 외 " + (goodsInfoList.size() - 1) + "건";
				}
				ilGoodsId = goodsInfoList.get(0).getIlGoodsId();
			}
		}

		ClaimVo claimVo = ClaimVo.builder()
				.odClaimId(reqDto.getOdClaimId()) 					//주문 클레임 PK
				.odOrderId(reqDto.getOdOrderId()) 					//주문 PK
				.claimStatusTp(reqDto.getClaimStatusTp())			//클레임상태구분 (CANCEL : 취소, RETURN : 반품, CS_REFUND: CS환불, RETURN_DELIVERY: 재배송 )
				.psClaimMallId(reqDto.getPsClaimMallId())			//클레임사유코드
				.claimReasonMsg(reqDto.getClaimReasonMsg())			//클레임상세사유
				.targetTp(reqDto.getTargetTp())						//귀책구분 B: 구매자, S: 판매자
				.directPaymentYn(reqDto.getDirectPaymentYn())		//직접결제여부
				.addPaymentTp(reqDto.getAddPaymentTp())				//추가결제방법
				.rejectReasonMsg(reqDto.getRejectReasonMsg()) 		//거부사유
				.returnsYn(reqDto.getReturnsYn()) 					//반품회수여부
				.refundType(StringUtil.nvl(reqDto.getRefundType(), OrderClaimEnums.RefundType.REFUND_TYPE_D.getCode()))	//환불수단 D: 원결제 내역 C : 무통장입금
				.goodsNm(goodsNm)									//대표상품명
				.refundGoodsPrice(reqDto.getRefundGoodsPrice())		//환불예정상품금액(환불 상품금액 - 할인금액)
				.goodsPrice(reqDto.getGoodsPrice())					//상품금액
				.goodsCouponPrice(reqDto.getGoodsCouponPrice())		//상품쿠폰금액
				.cartCouponPrice(reqDto.getCartCouponPrice()) 		//장바구니쿠폰금액
				.orderShippingPrice(reqDto.getOrderShippingPrice())	//주문 시 부과된 배송비
				.shippingPrice(reqDto.getShippingPrice())			//배송비
				.refundPrice(reqDto.getRefundPrice())				//환불금액
				.refundPointPrice(reqDto.getRefundPointPrice())		//환불적립금
				.odPaymentMasterId(reqDto.getOdPaymentMasterId())	//추가결제 결제마스터 PK
				.csRefundTp(reqDto.getCsRefundTp())					//CS환불구분
				.csRefundApproveCd(reqDto.getCsRefundApproveCd())	//CS환불승인상태
				.createId(Long.parseLong(reqDto.getUrUserId()))		//등록자
				.modifyId(Long.parseLong(reqDto.getUrUserId())) 	//수정자
				.approvalId(approvalId) 							//승인자
				.ilGoodsId(ilGoodsId)								//대표상품PK
				.build();

		return claimVo;
	}

	/**
	 * 클레임 상세 정보 세팅
	 * @param reqDto
	 * @return
	 */
	public static ClaimDetlVo setClaimDetl(OrderClaimRegisterRequestDto reqDto, OrderClaimGoodsInfoDto goodsInfo, OrderClaimGoodsPriceInfoDto orderClaimGoodsPriceInfoDto) {

		int salePrice = goodsInfo.getSalePrice();
		int totSalePrice = goodsInfo.getTotSalePrice();
		int directPrice = goodsInfo.getDirectPrice();
		int paidPrice = goodsInfo.getPaidPrice();
		int cartCouponPrice = goodsInfo.getCartCouponPrice();
		int goodsCouponPrice = goodsInfo.getGoodsCouponPrice();

		long urUserId = Long.parseLong(reqDto.getUrUserId());
		long odClaimId = reqDto.getOdClaimId();
		long odClaimDetlId = goodsInfo.getOdClaimDetlId();
		long caId = OrderEnums.OrderStatus.CANCEL_APPLY.getCode().equals(reqDto.getClaimStatusCd()) 			? urUserId : 0;		//취소요청
		long cwId = OrderEnums.OrderStatus.CANCEL_WITHDRAWAL.getCode().equals(reqDto.getClaimStatusCd())		? urUserId : 0;		//취소철회
		long ccId = OrderEnums.OrderStatus.CANCEL_COMPLETE.getCode().equals(reqDto.getClaimStatusCd())			? urUserId : 0;		//취소완료
		long raId = OrderEnums.OrderStatus.RETURN_APPLY.getCode().equals(reqDto.getClaimStatusCd())				? urUserId : 0;		//반품요청
		long rwId = OrderEnums.OrderStatus.RETURN_WITHDRAWAL.getCode().equals(reqDto.getClaimStatusCd())		? urUserId : 0;		//반품철회
		long riId = OrderEnums.OrderStatus.RETURN_ING.getCode().equals(reqDto.getClaimStatusCd())				? urUserId : 0;		//반품승인
		long rfId = OrderEnums.OrderStatus.RETURN_DEFER.getCode().equals(reqDto.getClaimStatusCd())				? urUserId : 0;		//반품보류
		long rcId = OrderEnums.OrderStatus.RETURN_COMPLETE.getCode().equals(reqDto.getClaimStatusCd())			? urUserId : 0;		//반품완료
		long ecId = OrderEnums.OrderStatus.EXCHANGE_COMPLETE.getCode().equals(reqDto.getClaimStatusCd())		? urUserId : 0;		//재배송
		long csId = OrderEnums.OrderStatus.CUSTOMER_SERVICE_REFUND.getCode().equals(reqDto.getClaimStatusCd())	? urUserId : 0;		//CS환불
		long faId = OrderEnums.OrderStatus.REFUND_APPLY.getCode().equals(reqDto.getClaimStatusCd()) 			? urUserId : 0;		//환불요청
		long fcId = OrderEnums.OrderStatus.REFUND_COMPLETE.getCode().equals(reqDto.getClaimStatusCd()) 			? urUserId : 0;		//환불완료
		long crId = urUserId;																										//클레임요청
		long ceId = OrderEnums.OrderStatus.RETURN_ING.getCode().equals(reqDto.getClaimStatusCd())
				||	OrderEnums.OrderStatus.RETURN_COMPLETE.getCode().equals(reqDto.getClaimStatusCd())
				||	OrderEnums.OrderStatus.CANCEL_COMPLETE.getCode().equals(reqDto.getClaimStatusCd())			? urUserId : 0;		//클레임승인
		long orderIfId = StringUtil.isNotEmpty(goodsInfo.getOrderIfDt())	? urUserId : 0;			//주문 I/F 등록자
		long shippingId = StringUtil.isNotEmpty(goodsInfo.getShippingDt())	? urUserId : 0;			//출고예정일 등록자
		long deliveryId = StringUtil.isNotEmpty(goodsInfo.getDeliveryDt())	? urUserId : 0;			//도착예정일 등록자

		String priorityUndelivered = StringUtil.nvl(reqDto.getPriorityUndelivered(),"N");

		LocalDateTime orderIfDt = StringUtil.isNotEmpty(goodsInfo.getOrderIfDt()) 	? LocalDate.parse(goodsInfo.getOrderIfDt(), DateTimeFormatter.ISO_DATE).atStartOfDay() : null;	//주문 I/F 일자
		LocalDateTime shippingDt = StringUtil.isNotEmpty(goodsInfo.getShippingDt()) ? LocalDate.parse(goodsInfo.getShippingDt(), DateTimeFormatter.ISO_DATE).atStartOfDay() : null;	//출고예정일 일자
		LocalDateTime deliveryDt = StringUtil.isNotEmpty(goodsInfo.getDeliveryDt()) ? LocalDate.parse(goodsInfo.getDeliveryDt(), DateTimeFormatter.ISO_DATE).atStartOfDay() : null;	//도착예정일 일자

		if(orderClaimGoodsPriceInfoDto != null) {
			salePrice = orderClaimGoodsPriceInfoDto.getSalePrice() ;
			totSalePrice = orderClaimGoodsPriceInfoDto.getTotSalePrice() ;
			directPrice = orderClaimGoodsPriceInfoDto.getDirectPrice();
			paidPrice = orderClaimGoodsPriceInfoDto.getPaidPrice();
			cartCouponPrice = orderClaimGoodsPriceInfoDto.getCartCouponPrice();
			goodsCouponPrice =orderClaimGoodsPriceInfoDto.getGoodsCouponPrice();
		}

		return ClaimDetlVo.builder()
				.odClaimDetlId(odClaimDetlId) 							//주문클레임 상세 PK
				.odClaimId(odClaimId)									//주문클레임 PK
				.odOrderDetlId(goodsInfo.getOdOrderDetlId()) 			//주문상세 PK
				.claimCnt(goodsInfo.getClaimCnt()) 						//클레임처리수량
				.orderStatusCd(goodsInfo.getOrderStatusCd()) 			//주문상태
				.claimStatusCd(reqDto.getClaimStatusCd()) 				//클레임상태
				.psClaimBosSupplyId(goodsInfo.getPsClaimBosSupplyId())	//BOS 클레임 사유 공급업체 PK
				.psClaimBosId(goodsInfo.getPsClaimBosId())				//BOS 클레임 사유 PK
				.bosClaimLargeId(goodsInfo.getBosClaimLargeId()) 		//BOS 클레임 대분류 ID
				.bosClaimMiddleId(goodsInfo.getBosClaimMiddleId()) 		//BOS 클레임 중분류 ID
				.bosClaimSmallId(goodsInfo.getBosClaimSmallId())		//BOS 클레임 소분류 ID
				.redeliveryType(goodsInfo.getRedeliveryType()) 			//재배송구분 재배송 : R, 대체상품 : S
				.ilGoodsShippingTemplateId(goodsInfo.getIlGoodsShippingTemplateId())	// 배송정책PK
				.urWarehouseId(goodsInfo.getUrWarehouseId())			// 출고처PK
				.addPaymentShippingPrice(goodsInfo.getAddPaymentShippingPrice())// 추가결제배송비
				.caId(caId)						//취소요청 등록자
				.cwId(cwId)						//취소철회 등록자
				.ccId(ccId)						//취소완료 등록자
				.raId(raId)						//반품요청 등록자
				.rwId(rwId)						//반품철회 등록자
				.riId(riId)						//반품승인 등록자
				.rfId(rfId)						//반품보류 등록자
				.rcId(rcId)						//반품완료 등록자
				.ecId(ecId)						//재배송 등록자
				.csId(csId)						//CS환불 등록자
				.faId(faId) 					//환불요청 등록자
				.fcId(fcId) 					//환불완료 등록자
				.crId(crId)						//클레임요청 등록자
				.ceId(ceId) 					//클레임승인 등록자
				.orderIfId(orderIfId) 			//주문 I/F 등록자
				.orderIfDt(orderIfDt)			//주문 I/F 일자
				.shippingId(shippingId)			//출고예정일 등록자
				.shippingDt(shippingDt)			//출고예정일 일자
				.deliveryId(deliveryId) 		//도착예정일 등록자
				.deliveryDt(deliveryDt)			//도착예정일 일자
				.salePrice(salePrice)					// 판매가
				.totSalePrice(totSalePrice)				// 판매가총합
				.directPrice(directPrice)				// 상품,장바구니쿠폰 할인 제외한 할인금액
				.paidPrice(paidPrice)					// 결제금액
				.cartCouponPrice(cartCouponPrice)		// 장바구니쿠폰할인금액
				.goodsCouponPrice(goodsCouponPrice)	// 상품쿠폰할인금액
				.priorityUndelivered(priorityUndelivered)	// 선미출여부
				.returnSalesExecFl(OrderClaimEnums.AllTypeYn.ALL_TYPE_N.getCode())	//반품매출연동여부
				.ifUnreleasedInfoId(StringUtil.nvlLong(reqDto.getIfUnreleasedInfoId()))	//반품매출연동여부
				.build();
	}

	public static ClaimDetlHistVo setClaimDetlHist(OrderClaimRegisterRequestDto reqDto, long odClaimDetlId, OrderClaimGoodsInfoDto goodsInfo) {

		String histMsg = OrderEnums.OrderStatus.findByCode(reqDto.getClaimStatusCd()).getCodeName();

		if(OrderClaimEnums.RedeliveryType.SUBSTITUTE_GOODS.getCode().equals(goodsInfo.getRedeliveryType())) {
			List<OrderClaimGoodsInfoDto> goodsInfoList = reqDto.getGoodsInfoList();
			List<OrderClaimGoodsInfoDto> idList = goodsInfoList.stream().filter(x -> x.getOdOrderDetlId() == 0 &&
																					 x.getRedeliveryIndex() == goodsInfo.getRedeliveryIndex())
																		.collect(Collectors.toList());
			if(!idList.isEmpty()) histMsg = "대체상품";
			for(OrderClaimGoodsInfoDto orderClaimGoodsInfoDto : idList){
				long ilGoodsId = orderClaimGoodsInfoDto.getIlGoodsId();
				String goodsNm = orderClaimGoodsInfoDto.getGoodsNm();
				int orderCnt = orderClaimGoodsInfoDto.getClaimCnt();
				histMsg += System.lineSeparator()+"상품코드: "+ilGoodsId+" / 상품명: "+goodsNm+" / 수량: "+orderCnt;
			}
		}

		return ClaimDetlHistVo.builder()
				.odOrderId(reqDto.getOdOrderId())				//주문 PK
				.odOrderDetlId(goodsInfo.getOdOrderDetlId()) 	//주문상세 PK
				.odOrderDetlSeq(goodsInfo.getOdOrderDetlSeq())	//주문상세 순번 주문번호에 대한 순번
				.odClaimId(reqDto.getOdClaimId())				//주문클레임PK
				.odClaimDetlId(odClaimDetlId)					//주문클레임상세PK
				.prevStatusCd(goodsInfo.getOrderStatusCd()) 	//이전상태값
				.statusCd(reqDto.getClaimStatusCd())			//변경상태값
				.histMsg(histMsg)
				.createId(Long.parseLong(reqDto.getUrUserId()))
				.build();
	}


	public static ClaimAccountVo setClaimAccount(OrderClaimRegisterRequestDto reqDto) throws Exception {
		ClaimAccountVo claimAccountVo = null;
		if (StringUtil.isNotEmpty(reqDto.getBankCd())
				&& StringUtil.isNotEmpty(reqDto.getAccountHolder())
				&& StringUtil.isNotEmpty(reqDto.getAccountNumber())) {

			claimAccountVo = ClaimAccountVo.builder()
					//.odClaimAccountId(odClaimAccountId)				//주문클레임 환불계좌 PK
					.odClaimId(reqDto.getOdClaimId())							//주문클레임 PK
					.bankCd(reqDto.getBankCd())					//은행코드
					.accountHolder(reqDto.getAccountHolder())	//예금주
					.accountNumber(reqDto.getAccountNumber())	//계좌번호
					.build();
		}
		return claimAccountVo;
	}

	/**
	 * 환불 결제 정보를 입력한다
	 *
	 * @param reqDto
	 * @throws Exception
	 */
	public long putOrderPaymentInfo(OrderClaimRegisterRequestDto reqDto) throws Exception {
		log.debug("환불결제 정보 입력 시작 !! <{}>", reqDto);
		//OrderRegistrationBiz orderRegistrationBiz = BeanUtils.getBeanByClass(OrderRegistrationBiz.class);
		long odPaymentMasterId = orderRegistrationBiz.getPaymentMasterSeq();

		//결제 정보 입력
		OrderPaymentVo orderPaymentVo = OrderPaymentVo.builder()
				.odOrderId(reqDto.getOdOrderId())				//주문PK
				.odClaimId(reqDto.getOdClaimId())				//주문클레임 PK
				.odPaymentMasterId(odPaymentMasterId)			//주문결제 PK
				.salePrice(reqDto.getGoodsPrice())				//판매가
				.cartCouponPrice(reqDto.getCartCouponPrice()) 	//장바구니 쿠폰 할인금액
				.goodsCouponPrice(reqDto.getGoodsCouponPrice()) //상품 쿠폰 할인금액
				.directPrice(reqDto.getEmployeePrice()) 		//즉시 할인금액
				.paidPrice(reqDto.getGoodsPrice() - reqDto.getCartCouponPrice() - reqDto.getGoodsCouponPrice()) //결제금액금액
				.shippingPrice(reqDto.getShippingPrice()) 		//배송비합
				.taxablePrice(reqDto.getTaxablePrice()) 		//과세결제금액
				.nonTaxablePrice(reqDto.getNonTaxablePrice())  	//비과세결제금액
				.paymentPrice(reqDto.getRefundPrice()) 			//결제금액
				.pointPrice(reqDto.getRefundPointPrice()) 		//사용적립금금액
				.build();

		log.debug("결제 테이블 변수 [OD_PAYMENT] <{}>", orderPaymentVo);
		orderRegistrationBiz.addPayment(orderPaymentVo);

		//결제 마스터 정보 입력
		OrderPaymentMasterVo orderPaymentMasterVo = OrderPaymentMasterVo.builder()
				.odPaymentMasterId(odPaymentMasterId)					//주문 결제 마스터 PK
				.type(reqDto.getType())  								//결제타입 (G : 결제, F : 환불 , A : 추가)
				.payTp(reqDto.getPayTp())								//결제방법 공통코드(PAY_TP)
				.pgService(reqDto.getPgService())						//PG Service
				.tid(reqDto.getTid())
				.authCd(reqDto.getAuthCode())
				.cardNumber(MaskingUtil.cardNumber(reqDto.getCardNumber()))
				.cardQuotaInterest(reqDto.getCardQuotaInterest())
				.cardQuota(reqDto.getCardQuota())
				.virtualAccountNumber(reqDto.getVirtualAccountNumber())
				.bankNm(reqDto.getBankName())
				.info(reqDto.getInfo())
				.paidDueDt(reqDto.getPaidDueDate())
				.paidHolder(reqDto.getPaidHolder())
				.partCancelYn(StringUtil.nvl(reqDto.getPartCancelYn(), "Y"))
				.escrowYn(StringUtil.nvl(reqDto.getEscrowYn(), "N"))	//에스크로결여부
				.status(OrderEnums.OrderStatus.INCOM_COMPLETE.getCode())//결제상태(IR:입금예정,IC:입금완료)
				.salePrice(reqDto.getGoodsPrice())						//판매가
				.cartCouponPrice(reqDto.getCartCouponPrice())			//장바구니 쿠폰 할인금액
				.goodsCouponPrice(reqDto.getGoodsCouponPrice())			//상품 쿠폰 할인금액
				.directPrice(reqDto.getEmployeePrice())					//즉시 할인금액 합
				.paidPrice(reqDto.getGoodsPrice() - reqDto.getCartCouponPrice() - reqDto.getGoodsCouponPrice())	//결제금액금액 합
				.shippingPrice(reqDto.getShippingPrice()) 				//배송비합
				.taxablePrice(reqDto.getTaxablePrice())					//과세결제금액
				.nonTaxablePrice(reqDto.getNonTaxablePrice())			//비과세결제금액
				.paymentPrice(reqDto.getRefundPrice())					//결제금액
				.pointPrice(reqDto.getRefundPointPrice())				//사용적립금금액
				.approvalDt(LocalDateTime.now())						//승인 일자
				.responseData(reqDto.getResponseData())					//취소응답메시지
				.build();

		log.debug("결제 마스터 테이블 변수 [OD_PAYMENT_MASTER] <{}>", orderPaymentVo);

		orderRegistrationBiz.addPaymentMaster(orderPaymentMasterVo);

		return odPaymentMasterId;
	}

	/**
	 * 추가 결제 정보를 입력한다
	 *
	 * @param requestDto
	 * @param claimPriceInfoDto
	 * @param pgPaymentDataDto
	 * @throws Exception
	 */
	public static long addClaimPaymentInfo(OrderClaimRegisterRequestDto requestDto, OrderClaimPriceInfoDto claimPriceInfoDto, PgApprovalOrderPaymentDataDto pgPaymentDataDto) throws Exception {
		log.debug("추가결제 정보 입력 시작 !! <{}>, <{}>", requestDto , pgPaymentDataDto);
		OrderRegistrationBiz orderRegistrationBiz = BeanUtils.getBeanByClass(OrderRegistrationBiz.class);
		long odPaymentMasterId = orderRegistrationBiz.getPaymentMasterSeq();

		//결제 정보 입력
		OrderPaymentVo orderPaymentVo = OrderPaymentVo.builder()
				.odOrderId(requestDto.getOdOrderId())				//주문PK
				.odClaimId(requestDto.getOdClaimId())				//주문클레임 PK
				.odPaymentMasterId(odPaymentMasterId)			//주문결제 PK
				.salePrice(0)				//판매가
				.cartCouponPrice(0) 	//장바구니 쿠폰 할인금액
				.goodsCouponPrice(0) //상품 쿠폰 할인금액
				.directPrice(0) 								//즉시 할인금액
				.paidPrice(0) //결제금액금액
				.shippingPrice(claimPriceInfoDto.getAddPaymentShippingPrice()) 		//배송비합
				.taxablePrice(claimPriceInfoDto.getAddPaymentShippingPrice()) 		//과세결제금액
				.nonTaxablePrice(0)  	//비과세결제금액
				.paymentPrice(claimPriceInfoDto.getAddPaymentShippingPrice()) 			//결제금액
				.pointPrice(0) 		//사용적립금금액
				.build();

		log.debug("결제 테이블 변수 [OD_PAYMENT] <{}>", orderPaymentVo);
		orderRegistrationBiz.addPayment(orderPaymentVo);

		//결제 마스터 정보 입력
		OrderPaymentMasterVo orderPaymentMasterVo = OrderPaymentMasterVo.builder()
				.odPaymentMasterId(odPaymentMasterId)					//주문 결제 마스터 PK
				.type(OrderEnums.PayType.A.getCode())  								//결제타입 (G : 결제, F : 환불 , A : 추가)
				.payTp(requestDto.getAddPaymentInfo().getPsPayCd())								//결제방법 공통코드(PAY_TP)
				.pgService(pgPaymentDataDto.getPgAccountType().getCode())						//PG Service
				.tid(pgPaymentDataDto.getTid())
				.authCd(pgPaymentDataDto.getAuthCode())
				.cardNumber(MaskingUtil.cardNumber(pgPaymentDataDto.getCardNumber()))
				.cardQuotaInterest(pgPaymentDataDto.getCardQuotaInterest())
				.cardQuota(pgPaymentDataDto.getCardQuota())
				.virtualAccountNumber(pgPaymentDataDto.getVirtualAccountNumber())
				.bankNm(pgPaymentDataDto.getBankName())
				.info(pgPaymentDataDto.getInfo())
				.paidDueDt(pgPaymentDataDto.getPaidDueDate())
				.paidHolder(pgPaymentDataDto.getPaidHolder())
				.partCancelYn(StringUtil.nvl(pgPaymentDataDto.getPartCancelYn(), "Y"))
				.escrowYn(pgPaymentDataDto.getEscrowYn())											//에스크로결여부
				.status(OrderEnums.OrderStatus.INCOM_COMPLETE.getCode())//결제상태(IR:입금예정,IC:입금완료)
				.salePrice(0)						//판매가
				.cartCouponPrice(0)			//장바구니 쿠폰 할인금액
				.goodsCouponPrice(0)			//상품 쿠폰 할인금액
				.directPrice(0)											//즉시 할인금액 합
				.paidPrice(0)	//결제금액금액 합
				.shippingPrice(claimPriceInfoDto.getAddPaymentShippingPrice()) 				//배송비합
				.taxablePrice(claimPriceInfoDto.getAddPaymentShippingPrice())					//과세결제금액
				.nonTaxablePrice(0)			//비과세결제금액
				.paymentPrice(claimPriceInfoDto.getAddPaymentShippingPrice())					//결제금액
				.pointPrice(0)				//사용적립금금액
				.approvalDt(LocalDateTime.now())						//승인 일자
				.responseData(pgPaymentDataDto.getResponseData())					//취소응답메시지
				.build();

		log.debug("결제 마스터 테이블 변수 [OD_PAYMENT_MASTER] <{}>", orderPaymentVo);

		orderRegistrationBiz.addPaymentMaster(orderPaymentMasterVo);

		return odPaymentMasterId;
	}

	/**
	 *  주문 클레임 첨부파일 관리 [OD_CLAIM_ATTC] Insert or Update
	 * @param reqDto
	 * @param odClaimId
	 * @return
	 */
	public static List<ClaimAttcVo> setOrderClaimAttc(OrderClaimRegisterRequestDto reqDto, long odClaimId) throws Exception {
		List<ClaimAttcVo> attcList = null;
		if (CollectionUtils.isNotEmpty(reqDto.getGoodsInfoList())) {
			if(CollectionUtils.isEmpty(reqDto.getAttcInfoList())) {
				return attcList;
			}
			attcList = new ArrayList<>();
			for (OrderClaimAttcInfoDto attcDto : reqDto.getAttcInfoList()) {

				ClaimAttcVo claimAttcVo = ClaimAttcVo.builder()
						.odClaimId(odClaimId)					//주문클레임 PK
						.originNm(attcDto.getOriginNm())		//업로드 원본 파일명
						.uploadNm(attcDto.getUploadNm())		//업로드 파일명
						.uploadPath(attcDto.getUploadPath())	//업로드 경로
						.build();
				attcList.add(claimAttcVo);
			}
		}
		return attcList;
	}


	/**
	 * 주문 클레임 받는 배송지 [OD_CLAIM_SHIPPING_ZONE] Insert or Update
	 * @param reqDto
	 * @return
	 */
	public static List<ClaimShippingZoneVo> setOrderClaimShippingZone(OrderClaimRegisterRequestDto reqDto, List<OrderClaimDetlInfoDto> claimDetlInfoList) throws Exception {

		List<ClaimShippingZoneVo> shippingZoneList = null;
		log.debug("받는배송지 claimList :: <{}>, recvShippList :: <{}>", CollectionUtils.isNotEmpty(claimDetlInfoList), CollectionUtils.isNotEmpty(reqDto.getRecvShippingList()));
		if (CollectionUtils.isNotEmpty(claimDetlInfoList) && CollectionUtils.isNotEmpty(reqDto.getRecvShippingList())) {
			shippingZoneList = new ArrayList<>();
			for (OrderClaimDetlInfoDto detlDto : claimDetlInfoList) {
				for (OrderClaimRecvShippingZoneInfoDto recvDto : reqDto.getRecvShippingList()) {
					log.debug("받는배송지 상품 출고지  <{}>==<{}>, <{}>==<{}>", detlDto.getOdOrderDetlId(), recvDto.getOdOrderDetlId(), detlDto.getUrWarehouseId(), recvDto.getUrWarehouseId());
					if (detlDto.getOdOrderDetlId() == recvDto.getOdOrderDetlId() && detlDto.getUrWarehouseId() == recvDto.getUrWarehouseId()) {
						log.debug("받는배송지 상품 출고지가 같다 <{}>==<{}>, <{}>==<{}>", detlDto.getOdOrderDetlId(), recvDto.getOdOrderDetlId(), detlDto.getUrWarehouseId(), recvDto.getUrWarehouseId());

						ClaimShippingZoneVo claimShippingZoneVo = ClaimShippingZoneVo.builder()
								.odClaimId(reqDto.getOdClaimId())				//주문클레임 PK
								.odClaimDetlId(detlDto.getOdClaimDetlId()) 		//주문 클레임 상세 PK
								.urWarehouseId(detlDto.getUrWarehouseId()) 		//출고처 PK : UR_WAREHOUSE.UR_WAREHOUSE_ID
								.recvNm(recvDto.getRecvNm())					//수령인명
								.recvHp(recvDto.getRecvHp())					//수령인핸드폰
								.recvTel(recvDto.getRecvTel())					//수령인연락처
								.recvZipCd(recvDto.getRecvZipCd())				//수령인우편번호
								.recvAddr1(recvDto.getRecvAddr1())				//수령인주소1
								.recvAddr2(recvDto.getRecvAddr2())				//수령인주소2
								.recvBldNo(recvDto.getRecvBldNo())				//건물번호
								.deliveryMsg(recvDto.getDeliveryMsg())			//배송요청사항
								.doorMsgCd(recvDto.getDoorMsgCd())				//출입정보타입 공통코드(DOOR_MSG_CD)
								.doorMsg(recvDto.getDoorMsg())					//배송출입 현관 비밀번호
								.build();
						log.debug("주문 클레임 받는 배송지 OD_CLAIM_SHIPPING_ZONE <{}>" , claimShippingZoneVo);
						shippingZoneList.add(claimShippingZoneVo);
					}
				}
			}
		}
		return shippingZoneList;
	}

	/**
	 * 주문 클레임 보내는 배송지 [OD_CLAIM_SEND_SHIPPING_ZONE] Insert or Update
	 * @param reqDto
	 * @param reqDto
	 * @return
	 */
	public static ClaimSendShippingZoneVo setOrderClaimSendShippingZone(OrderClaimRegisterRequestDto reqDto) throws Exception {
		return ClaimSendShippingZoneVo.builder()
									.odClaimId(reqDto.getOdClaimId())			//주문클레임 PK
									.recvNm(reqDto.getSendRecvNm())				//수령인명
									.recvHp(reqDto.getSendRecvHp())				//수령인핸드폰
									.recvTel(reqDto.getSendRecvTel())			//수령인연락처
									.recvZipCd(reqDto.getSendRecvZipCd())		//수령인우편번호
									.recvAddr1(reqDto.getSendRecvAddr1())		//수령인주소1
									.recvAddr2(reqDto.getSendRecvAddr2())		//수령인주소2
									.recvBldNo(reqDto.getSendRecvBldNo())		//건물번호
									.deliveryMsg(reqDto.getSendDeliveryMsg())	//배송요청사항
									.doorMsgCd(reqDto.getSendDoorMsgCd())		//출입정보타입 공통코드(DOOR_MSG_CD)
									.doorMsg(reqDto.getSendDoorMsg())			//배송출입 현관 비밀번호
									.build();
	}


	/**
	 * 입금전취소, 취소완료, 반품완료 자동메일 발송
	 * @return
	 */
	public void claimSendEmail(String status, long odOrderId, long odClaimId, String claimStatusCd, int frontTp,List<OrderClaimGoodsInfoDto> goodsInfoList){
		try {
			// 입금 전 취소 SMS 발송
			if(StringUtils.isNotEmpty(status) && OrderEnums.OrderStatus.INCOM_BEFORE_CANCEL_COMPLETE.getCode().equals(claimStatusCd)) { // 입금전취소
				OrderInfoForEmailResultDto orderInfoForsmsResultDto = orderEmailBiz.getOrderCancelBeforeDepositInfoForEmail(odOrderId);
				orderInfoForsmsResultDto.setFrontTp(frontTp);
				orderEmailSendBiz.orderCancelBeforeDeposit(orderInfoForsmsResultDto);

			} else {
				List<Long> odOrderDetlIdList = goodsInfoList.stream().map(m->m.getOdOrderDetlId()).collect(Collectors.toList());

				OrderInfoForEmailResultDto orderInfoForEmailResultDto = orderEmailBiz.getOrderClaimCompleteInfoForEmail(odClaimId,odOrderDetlIdList);

				// 외부몰 주문 BOS에서 취소,반품시 자동메일 발송 X (주문 자동메일/SMS발송은 통합몰 주문건에 한해서만 발송)
				if(OrderClaimEnums.OrderClaimFrontTpCd.FRONT_TP_FRONT.getCodeValue() != frontTp
					&& SystemEnums.AgentType.OUTMALL.getCode().equals(orderInfoForEmailResultDto.getOrderInfoVo().getAgentTypeCd())){
					return;
				}

				// 렌탈상품 주문자동메일 발송X
				if(orderInfoForEmailResultDto.getOrderInfoVo().getRentalCount() == 0){
					if(OrderEnums.OrderStatus.CANCEL_COMPLETE.getCode().equals(claimStatusCd)){
						// 주문 취소완료 자동메일 발송
						orderEmailSendBiz.orderCancelComplete(orderInfoForEmailResultDto, odOrderDetlIdList);
					}
					if(OrderEnums.OrderStatus.RETURN_COMPLETE.getCode().equals(claimStatusCd)){
						// 주문 반품완료 자동메일 발송
						orderEmailSendBiz.orderReturnCompleted(orderInfoForEmailResultDto, odOrderDetlIdList);
					}
				}
			}
		} catch (Exception e) {
			log.error("ERROR ====== 주문취소 자동메일 발송 오류 odClaimId ::" , odClaimId);
			log.error(e.getMessage());
		}
	}

	/**
	 * 취소, 반품 완료 처리 시 비지니스 로직
	 * @param requestDto
	 * @param claimVo
	 * @param claimDetlList
	 * @param isClaimSave
	 * @throws Exception
	 */
    public void setStatusCompleteProcess(OrderClaimRegisterRequestDto requestDto, OrderClaimInfoDto claimVo, List<OrderClaimDetlListInfoDto> claimDetlList, boolean isClaimSave) throws Exception {

		log.debug("=== 할인 쿠폰이 있으면 재발급을 시킨다. !!!===");
		claimUtilRefundService.putRefundCoupon(requestDto);

		log.debug("=== 클레임상태를 업데이트 !!!===");
		setClaimStatus(requestDto, isClaimSave);

		// 취소완료일 경우만
		if(OrderEnums.OrderStatus.CANCEL_COMPLETE.getCode().equals(requestDto.getClaimStatusCd())){
			log.debug("=== 재고를 업데이트 !!!===");
			setStockOrderUpdate(requestDto);

			log.debug("=== 추가 배송비 환불 처리 !!!===");
			// 추가 배송비 결제 내역 확인하여 추가 배송비 취소 처리
			claimUtilRefundService.putOrderClaimAddShippingPrice(requestDto);
		}

		boolean refundPriceFlag = false;
		//환불 금액이 있을 때
		if (requestDto.getRefundPrice() != 0) {
			log.debug("=== 환불금액이 있을때 5 !!!===");
			claimUtilRefundService.refundPrice(requestDto);
			refundPriceFlag = true;
		}

		//환불 적립금이 있을 때
		if (requestDto.getRefundPointPrice() != 0) {
			log.debug("=== 환불적립금이 있을때 6 !!!===");
			claimUtilRefundService.refundPointPrice(requestDto);
			// 금액 환불을 하지 않았을 경우
			if(!refundPriceFlag) {
				// 결제 타입 무료결제로 설정
				requestDto.setPayTp(OrderEnums.PaymentType.FREE.getCode());
			}
		}

		//환불적립금 또는 환불금액이 있을 때 환불상태 값을 환불완료로 바꾼다.
		if (requestDto.getRefundPointPrice() != 0 || requestDto.getRefundPrice() != 0) {
			log.debug("=== 환불상태를 환불완료로 업데이트 !!!===");
			List<?> claimList = isClaimSave ? requestDto.getGoodsInfoList() : claimDetlList;
			setClaimRefundPointPriceStatus(claimVo.getOdClaimId(), requestDto, OrderEnums.OrderStatus.REFUND_COMPLETE.getCode(), claimList, isClaimSave);

			log.debug("=== 환불 금액 또는 환불 포인트가 존재할 경우 환불 정보 payment 테이블 입력 ===");
			requestDto.setType(OrderEnums.PayType.F.getCode());
			this.putOrderPaymentInfo(requestDto);
		}

	}

	/**
	 * 추가 결제 클레임 상품 환불 정보 Set
	 * @param requestDto
	 * @param refundInfoDto
	 * @throws Exception
	 */
    public void setStatusAddPaymentCompleteProcess(OrderClaimRegisterRequestDto requestDto, OrderClaimPriceInfoDto refundInfoDto) throws Exception {

		requestDto.setGoodsPrice(refundInfoDto.getGoodsPrice());
		requestDto.setCartCouponPrice(refundInfoDto.getCartCouponPrice());
		requestDto.setGoodsCouponPrice(refundInfoDto.getGoodsCouponPrice());
		requestDto.setRefundGoodsPrice(refundInfoDto.getRefundGoodsPrice());
		requestDto.setOrderShippingPrice(refundInfoDto.getOrderShippingPrice());
		requestDto.setAddPaymentShippingPrice(refundInfoDto.getAddPaymentShippingPrice());
		requestDto.setPrevAddPaymentShippingPrice(refundInfoDto.getPrevAddPaymentShippingPrice());
		requestDto.setRefundPrice(refundInfoDto.getRefundPrice());
		requestDto.setShippingPrice(refundInfoDto.getShippingPrice());
		requestDto.setRemaindPrice(refundInfoDto.getRemainPaymentPrice());
		requestDto.setRefundPointPrice(refundInfoDto.getRefundPointPrice());
		requestDto.setRemainPointPrice(refundInfoDto.getRemainPointPrice());
		requestDto.setDeliveryCouponList(refundInfoDto.getDeliveryCouponList());
		requestDto.setAddPaymentList(refundInfoDto.getAddPaymentList());

		log.debug("=== 할인 쿠폰이 있으면 재발급을 시킨다. !!!===");
		claimUtilRefundService.putRefundCoupon(requestDto);

		// 취소완료일 경우만
		if(OrderEnums.OrderStatus.CANCEL_COMPLETE.getCode().equals(requestDto.getClaimStatusCd())){
			log.debug("=== 재고를 업데이트 !!!===");
			setStockOrderUpdate(requestDto);

			log.debug("=== 추가 배송비 환불 처리 !!!===");
			// 추가 배송비 결제 내역 확인하여 추가 배송비 취소 처리
			claimUtilRefundService.putOrderClaimAddShippingPrice(requestDto);
		}

		boolean refundPriceFlag = false;
		//환불 금액이 있을 때
		if (requestDto.getRefundPrice() != 0) {
			log.debug("=== 환불금액이 있을때 5 !!!===");
			claimUtilRefundService.refundPrice(requestDto);
			refundPriceFlag = true;
		}

		//환불 적립금이 있을 때
		if (requestDto.getRefundPointPrice() != 0) {
			log.debug("=== 환불적립금이 있을때 6 !!!===");
			claimUtilRefundService.refundPointPrice(requestDto);
			// 금액 환불을 하지 않았을 경우
			if(!refundPriceFlag) {
				// 결제 타입 무료결제로 설정
				requestDto.setPayTp(OrderEnums.PaymentType.FREE.getCode());
			}
		}

		//환불적립금 또는 환불금액이 있을 때 환불상태 값을 환불완료로 바꾼다.
		if (requestDto.getRefundPointPrice() != 0 || requestDto.getRefundPrice() != 0) {
			log.debug("=== 환불상태를 환불완료로 업데이트 !!!===");
			this.setClaimRefundPointPriceStatus(requestDto.getOdClaimId(), requestDto, OrderEnums.OrderStatus.REFUND_COMPLETE.getCode(), null, false);

			log.debug("=== 환불 금액 또는 환불 포인트가 존재할 경우 환불 정보 payment 테이블 입력 ===");
			requestDto.setType(OrderEnums.PayType.F.getCode());
			this.putOrderPaymentInfo(requestDto);
		}
	}

	/**
	 * 녹즙 취소, 반품 완료 처리 시 비지니스 로직
	 * @param requestDto
	 * @param isClaimSave
	 * @throws Exception
	 */
    public void setGreenJuiceStatusCompleteProcess(OrderClaimRegisterRequestDto requestDto, boolean isClaimSave) throws Exception {

		log.debug("=== 녹즙 할인 쿠폰이 있으면 재발급을 시킨다. !!!===");
		claimUtilRefundService.putRefundCoupon(requestDto);

    	boolean refundPriceFlag = false;
		//환불 금액이 있을 때
		if (requestDto.getRefundPrice() != 0) {
			log.debug("=== 녹즙 환불금액이 있을때 5 !!!===");
			claimUtilRefundService.refundPrice(requestDto);
			refundPriceFlag = true;
		}

		//환불 적립금이 있을 때
		if (requestDto.getRefundPointPrice() != 0) {
			log.debug("=== 녹즙 환불적립금이 있을때 6 !!!===");
			claimUtilRefundService.refundPointPrice(requestDto);
			// 금액 환불을 하지 않았을 경우
			if(!refundPriceFlag) {
				// 결제 타입 무료결제로 설정
				requestDto.setPayTp(OrderEnums.PaymentType.FREE.getCode());
			}
		}

		//환불적립금 또는 환불금액이 있을 때 환불상태 값을 환불완료로 바꾼다.
		if (requestDto.getRefundPointPrice() != 0 || requestDto.getRefundPrice() != 0) {
			log.debug("=== 녹즙 환불상태를 환불완료로 업데이트 !!!===");
			List<?> claimList = isClaimSave ? requestDto.getGoodsInfoList() : null;
			setClaimRefundPointPriceStatus(requestDto.getOdClaimId(), requestDto, OrderEnums.OrderStatus.REFUND_COMPLETE.getCode(), claimList, isClaimSave);

			log.debug("=== 녹즙 환불 금액 또는 환불 포인트가 존재할 경우 환불 정보 payment 테이블 입력 ===");
			requestDto.setType(OrderEnums.PayType.F.getCode());
			this.putOrderPaymentInfo(requestDto);
		}
	}

	/**
	 * 취소, 반품 완료 이외 처리 시 비지니스 로직
	 * @param requestDto
	 * @param isClaimSave
	 */
	public void setStatusCompleteOtherProcess(OrderClaimRegisterRequestDto requestDto, boolean isClaimSave) throws Exception {

		// 입금전취소일 경우
		if(OrderEnums.OrderStatus.INCOM_BEFORE_CANCEL_COMPLETE.getCode().equals(requestDto.getClaimStatusCd())){
			//환불 적립금이 있을 때
			if (requestDto.getRefundPointPrice() != 0) {
				log.debug("=== 환불적립금이 있을때 6 !!!===");
				claimUtilRefundService.refundPointPrice(requestDto);
			}

			log.debug("=== 할인 쿠폰이 있으면 재발급을 시킨다. !!!===");
			claimUtilRefundService.putRefundCoupon(requestDto);

			// 정기배송 주문서 생성 후 바로 입금전 취소할 경우에는 재고차감을 하지 않는다
			// - 정기배송 주문서 생성 시 해당 상품이 유효한 상태의 상품이 아닐 경우 (ex : 일시품절, 판매중상태아님 등), 주문서를 생성 후 입금전 취소 처리를 한다.
			//   정기배송 주문서 생성 후 바로 입금전 취소 처리를 할 경우 재고차감을 하고 오지 않기 때문에, 재고 업데이트 처리를 제외한다.
			String regularOrderIbFlag = StringUtil.nvl(requestDto.getRegularOrderIbFlag(), OrderClaimEnums.AllTypeYn.ALL_TYPE_N.getCode());
			if(OrderClaimEnums.AllTypeYn.ALL_TYPE_N.getCode().equals(regularOrderIbFlag)) {
				log.debug("=== 재고를 업데이트 !!!===");
				setStockOrderUpdate(requestDto);
			}

			// 가상계좌 망취소 처리
			claimUtilRefundService.putVirtualAccountCancel(requestDto);

			// OD_PAYMENT_MASTER 입금전 취소 업데이트
			claimUtilRefundService.putPaymentMasterStatus(requestDto);
		}

		log.debug("=== 클레임상태를 업데이트 !!!===");
		setClaimStatus(requestDto, isClaimSave);
	}

	/**
	 * 외부몰 취소, 반품 완료 처리 시 비지니스 로직
	 * @param requestDto
	 * @param isClaimSave
	 * @throws Exception
	 */
	public void setOutmallStatusCompleteProcess(OrderClaimRegisterRequestDto requestDto, boolean isClaimSave) throws Exception {

		if (requestDto.getRefundPrice() != 0) {
			log.debug("=== 환불 금액 존재할 경우 환불 처리 ===");
			//claimUtilRefundService.refundPrice(requestDto);

			log.debug("=== 환불 금액 존재할 경우 환불 정보 payment 테이블 입력 ===");
			requestDto.setType(OrderEnums.PayType.F.getCode());
			requestDto.setPayTp(OrderEnums.PaymentType.COLLECTION.getCode());
			this.putOrderPaymentInfo(requestDto);
		}

		log.debug("=== 클레임상태를 업데이트 !!!===");
		setClaimStatus(requestDto, isClaimSave);

		// 취소완료일 경우만
		if(OrderEnums.OrderStatus.CANCEL_COMPLETE.getCode().equals(requestDto.getClaimStatusCd())){
			log.debug("=== 재고를 업데이트 !!!===");
			setStockOrderUpdate(requestDto);
		}

	}

	/**
	 * 재고를 업데이트
	 * @param requestDto
	 */
	public void setStockOrderUpdate(OrderClaimRegisterRequestDto requestDto) throws Exception {

		List<StockOrderRequestDto> stockOrderReqDtoList = new ArrayList<StockOrderRequestDto>();
		StockOrderRequestDto stockOrderRequestDto = new StockOrderRequestDto();

		for(OrderClaimGoodsInfoDto orderClaimGoodsInfoDto : requestDto.getGoodsInfoList()) {
			log.debug("=====================재고업데이트 상품정보=======================: "+orderClaimGoodsInfoDto);

			// 주문 I/F여부 체크
			String batchExecFl = claimUtilRefundService.getOrderIsInterfaceCheck(orderClaimGoodsInfoDto.getOdOrderDetlId());
			// 주문 I/F안된 주문 상품만 재고 원복
			if("N".equals(batchExecFl)) {
				// 재고증감
				StockOrderRequestDto stockOrderReqDto = new StockOrderRequestDto();
				stockOrderReqDto.setIlGoodsId(orderClaimGoodsInfoDto.getIlGoodsId());
				stockOrderReqDto.setOrderQty(orderClaimGoodsInfoDto.getClaimCnt());
				stockOrderReqDto.setScheduleDt(StringUtil.nvl(orderClaimGoodsInfoDto.getShippingDt(), "2000-01-01"));
				stockOrderReqDto.setOrderYn("N");
				stockOrderReqDto.setStoreYn(GoodsDeliveryType.SHOP.getCode().equals(orderClaimGoodsInfoDto.getGoodsDeliveryType()) ? "Y" : "N");
				stockOrderReqDto.setMemo(orderClaimGoodsInfoDto.getOdOrderDetlId() + "-" + orderClaimGoodsInfoDto.getOdClaimDetlId());
				stockOrderReqDtoList.add(stockOrderReqDto);
			}

		}

		if(stockOrderReqDtoList.size() > 0){
			// 재고증감
			stockOrderRequestDto.setOrderList(stockOrderReqDtoList);
			ApiResult<?> stockRes = goodsStockOrderBiz.stockOrderHandle(stockOrderRequestDto);
			if (!stockRes.getCode().equals(BaseEnums.Default.SUCCESS.getCode())) {
				throw new Exception("=================주문취소 시 재고증감 실패===================");
			}
		}

	}

	/**
	 * 재배송 재고 업데이트 처리
	 * @param requestDto
	 */
	public void setRedeliveryStockOrderUpdate(OrderClaimRegisterRequestDto requestDto) throws Exception {

		List<StockOrderRequestDto> stockOrderReqDtoList = new ArrayList<StockOrderRequestDto>();
		StockOrderRequestDto stockOrderRequestDto = new StockOrderRequestDto();

		for(OrderClaimGoodsInfoDto orderClaimGoodsInfoDto : requestDto.getGoodsInfoList()) {
			log.debug("=====================재배송 재고업데이트 상품정보=======================: "+orderClaimGoodsInfoDto);

			// 주문 I/F여부 체크
			String batchExecFl = claimUtilRefundService.getOrderIsInterfaceCheck(orderClaimGoodsInfoDto.getOdOrderDetlId());
			// 주문 I/F안된 주문 상품만 재고 원복
			if(OrderClaimEnums.AllTypeYn.ALL_TYPE_N.getCode().equals(batchExecFl)) {
				// 재고증감
				StockOrderRequestDto stockOrderReqDto = new StockOrderRequestDto();
				stockOrderReqDto.setIlGoodsId(orderClaimGoodsInfoDto.getIlGoodsId());
				stockOrderReqDto.setOrderQty(orderClaimGoodsInfoDto.getClaimCnt());
				stockOrderReqDto.setScheduleDt(StringUtil.nvl(orderClaimGoodsInfoDto.getShippingDt(), "2000-01-01"));
				stockOrderReqDto.setOrderYn(orderClaimGoodsInfoDto.getOrderYn());
				stockOrderReqDto.setStoreYn(GoodsDeliveryType.SHOP.getCode().equals(orderClaimGoodsInfoDto.getGoodsDeliveryType()) ? "Y" : "N");
				stockOrderReqDto.setMemo(orderClaimGoodsInfoDto.getOdOrderDetlId() + "-" + orderClaimGoodsInfoDto.getOdClaimDetlId());
				stockOrderReqDtoList.add(stockOrderReqDto);
			}

		}

		if(stockOrderReqDtoList.size() > 0){
			// 재고증/차감
			stockOrderRequestDto.setOrderList(stockOrderReqDtoList);
			ApiResult<?> stockRes = goodsStockOrderBiz.stockOrderHandle(stockOrderRequestDto);
			if (!stockRes.getCode().equals(BaseEnums.Default.SUCCESS.getCode())) {
				throw new Exception("=================재배송 처리 시 재고증/차감 실패===================");
			}
		}

	}

	/**
	 * 반품승인 처리 시 비지니스 로직
	 * @param requestDto
	 * @param isClaimSave
	 */
	public void setStatusReturnApplyCompleteProcess(OrderClaimRegisterRequestDto requestDto, boolean isClaimSave, List<OrderClaimDetlListInfoDto> claimDetlList) throws Exception{

		if ("Y".equals(requestDto.getReturnsYn())) {

			log.debug("=== 받는 사람 받는 사람 8 !!!===");
			//주문 클레임 받는 배송지 [OD_CLAIM_SHIPPING_ZONE] Insert or Update
			mergeOrderClaimShippingZone(requestDto, requestDto.getOdClaimId());

			log.debug("=== 보내는 사람 받는 사람 9 !!!===");
			//주문 클레임 보내는 배송지 [OD_CLAIM_SEND_SHIPPING_ZONE] Insert or Update
			mergeOrderClaimSendShippingZone(requestDto);

			//롯데 택배 반품 접수
			log.debug("=== 롯데택배접수 10 !!!===");
			lotteReturnReceive(requestDto);

			log.debug("=== CJ택배접수 13 !!!===");
			cjReturnReceive(requestDto);
		}

		log.debug("=== 클레임상태를 업데이트 !!!===");
		setClaimStatus(requestDto, isClaimSave);

	}

	/**
	 * 택배사가 CJ 택배이면 반품 접수를 한다.
	 * @param reqDto
	 * @return
	 */
	public void cjReturnReceive(OrderClaimRegisterRequestDto reqDto) throws Exception {

		this.getUserInfo(reqDto);

		if (org.apache.commons.collections.CollectionUtils.isNotEmpty(reqDto.getGoodsInfoList()) && org.apache.commons.collections.CollectionUtils.isNotEmpty(reqDto.getRecvShippingList())) {
			for (OrderClaimGoodsInfoDto goodsInfo : reqDto.getGoodsInfoList()) {
				log.debug("CJ택배 ClaimStatusCd :: <{}>, ReturnsYn :: <{}>, PsShippingCompId :: <{}>, CJ택배사 코드 :: <{}>", reqDto.getClaimStatusCd(), reqDto.getReturnsYn(), goodsInfo.getPsShippingCompId(), OrderEnums.LogisticsCd.CJ.getCode());

				// CJ택배 PsShippingCompId 가져오기
				String lottePsShippingCompId = claimProcessMapper.getPsShippingCompId(OrderEnums.LogisticsCd.CJ.getLogisticsCode());

				//주문클레임이 반품승인이면서 회수여부가 Y 이고 CJ 택배사 이면 반품 접수 시킨다.
				if (goodsInfo.getPsShippingCompId() == Long.parseLong(lottePsShippingCompId)) {	//CJ대한통운 일 때

					for (OrderClaimRecvShippingZoneInfoDto shippingDto : reqDto.getRecvShippingList()) {

						// 출고처 용인물류/백암물류 값 가져오기
						String goodsWarehouseCode = claimProcessMapper.selectGoodsWarehouseCode(shippingDto.getUrWarehouseId());

						if (goodsWarehouseCode != null && shippingDto.getUrWarehouseId() == goodsInfo.getUrWarehouseId() && shippingDto.getOdOrderDetlId() == goodsInfo.getOdOrderDetlId()) {
							ClaimDetlVo claimDetlVo = new ClaimDetlVo();
							claimDetlVo.setOdClaimDetlId(goodsInfo.getOdClaimDetlId());

							log.debug("출고지가 같을 때 <{}>, <{}>", shippingDto.getUrWarehouseId(), goodsInfo.getUrWarehouseId());
							log.debug("회원 :: <{}>, 비회원 :: <{}>, 임직원 :: <{}>", reqDto.getUrUserId(), reqDto.getGuestCi(),reqDto.getUrEmployeeCd());
							log.debug("송화인 전화번호 : <{}>, 송화인 핸드폰 번호 : <{}>", reqDto.getSendRecvTel(), reqDto.getSendRecvHp());

							// 백암물류
							String custId = ErpEnums.ParcelServiceCustId.CJ_CUST_CD.getCode();
							// 용인물류 새벽
							if(GoodsEnums.GoodsWarehouseCode.WAREHOUSE_YONGIN_CODE.getCode().equals(goodsWarehouseCode)
									&& GoodsEnums.GoodsDeliveryType.DAWN.getCode().equals(goodsInfo.getGoodsDeliveryType())) {
								custId = ErpEnums.ParcelServiceCustId.CJ_DAWN_CUST_CD.getCode();
							}
							// 용인물류 일반
							else if(GoodsEnums.GoodsWarehouseCode.WAREHOUSE_YONGIN_CODE.getCode().equals(goodsWarehouseCode)
									&& GoodsEnums.GoodsDeliveryType.NORMAL.getCode().equals(goodsInfo.getGoodsDeliveryType())) {
								custId = ErpEnums.ParcelServiceCustId.CJ_NORMAL_CUST_CD.getCode();
							}

							String currentDate = DateUtil.getCurrentDate();
							kr.co.pulmuone.v1.api.cjlogistics.dto.CJLogisticsOrderAcceptDto dto = new CJLogisticsOrderAcceptDto();
							dto.setCustId(custId);			    /**고객ID*/
							dto.setRcptYmd(currentDate);  													/**접수일자YYYMMDD*/
							dto.setCustUseNo(StringUtil.nvl(goodsInfo.getOdClaimDetlId()));  				/**고객사용번호	 기업고객이관리하는주문번호/영수번호등내부관리번호*/
							dto.setRcptDv(ClaimEnums.DeliveryReceiptType.RETURN.getCode());              	/**접수구분	01:일반,02:반품*/
							dto.setWorkDvCd(ClaimEnums.DeliveryWorkTypeCode.NORMAL.getCode());            	/**작업구분코드	01:일반,02:교환,03:A/S*/
							dto.setReqDvCd(ClaimEnums.DeliveryRequestTypeCode.REQUEST.getCode());           /**요청구분코드	01:요청,02:취소*/
							dto.setMpckKey(currentDate + "_" + ErpEnums.ParcelServiceCustId.CJ_CUST_CD.getCode() + "_" + StringUtil.nvl(goodsInfo.getOdClaimDetlId()) + "_" + goodsInfo.getTrackingNo());	/**합포장키	다수데이터를한송장에출력할경우처리(합포없는경우YYYYMMDD_고객ID_고객사용번호orYYYYMMDD_고객ID_운송장번호);*/
							dto.setMpckSeq(Constants.COMBINED_PACKING_ORDER);              					/**합포장순번	합포장처리건수가다수일경우SEQ처리를수행한다.(합포없는경우무조건1);*/
							dto.setCalDvCd(ClaimEnums.DeliveryCalculateTypeCode.CONTRACT_FARE.getCode());   /**정산구분코드	01:계약운임,02:자료운임(계약운임인지업체에서넣어주는운임으로할지);*/
							dto.setFrtDvCd(ClaimEnums.DeliveryFareTypeCode.CREDIT.getCode());             	/**운임구분코드	01:선불,02:착불,03:신용*/
							dto.setCntrItemCd(ClaimEnums.DeliveryContractItemTypeCode.GENERAL_ITEM.getCode());/**계약품목코드01:일반품목*/
							dto.setBoxTypeCd(ClaimEnums.DeliveryBoxTypeCode.MEDIUM.getCode());           	/**박스타입코드	01:극소,02:소,03:중,04:대,05:특대*/
							dto.setBoxQty(Constants.DELIVERY_BOX_QUANTITY);               					/**택배박스수량	*/
							//dto.setFrt(0);                 												/**운임적용구분이 자료 운임일 경우 등록처리*/
							dto.setCustMgmtDlcmCd(ErpEnums.ParcelServiceCustId.CJ_CUST_CD.getCode());   	/**고객관리거래처코드주관사관리협력업체코드혹은택배사관리업체코드*/
							dto.setSendrNm(reqDto.getSendRecvNm());			/**송화인명 */
							dto.setSendrTelNo1(reqDto.getSendRecvHp1()); 	/**송화인전화번호1*/
							dto.setSendrTelNo2(reqDto.getSendRecvHp2()); 	/**송화인전화번호2*/
							dto.setSendrTelNo3(reqDto.getSendRecvHp3()); 	/**송화인전화번호3*/
							dto.setSendrCellNo1(reqDto.getSendRecvHp1());	/**송화인휴대폰번호1*/
							dto.setSendrCellNo2(reqDto.getSendRecvHp2());   /**송화인휴대폰번호2*/
							dto.setSendrCellNo3(reqDto.getSendRecvHp3());   /**송화인휴대폰번호3*/
							dto.setSendrSafeNo1("");        					/**송화인안심번호1*/
							dto.setSendrSafeNo2("");        					/**송화인안심번호2*/
							dto.setSendrSafeNo3("");        					/**송화인안심번호3*/
							dto.setSendrZipNo(reqDto.getSendRecvZipCd());   /**송화인우편번호*/
							dto.setSendrAddr(reqDto.getSendRecvAddr1());    /**송화인주소*/
							dto.setSendrDetailAddr(reqDto.getSendRecvAddr2()+ " " + reqDto.getSendRecvBldNo());     	/**송화인상세주소*/
							dto.setRcvrNm(shippingDto.getRecvNm());         	 /**수화인명*/
							dto.setRcvrTelNo1(shippingDto.getRecvHp1());        /**수화인전화번호1*/
							dto.setRcvrTelNo2(shippingDto.getRecvHp2());        /**수화인전화번호2*/
							dto.setRcvrTelNo3(shippingDto.getRecvHp3());        /**수화인전화번호3*/
							dto.setRcvrCellNo1(shippingDto.getRecvHp1());       /**수화인휴대폰번호1*/
							dto.setRcvrCellNo2(shippingDto.getRecvHp2());       /**수화인휴대폰번호2*/
							dto.setRcvrCellNo3(shippingDto.getRecvHp3());       /**수화인휴대폰번호3*/
							dto.setRcvrSafeNo1("");         					/**수화인안심번호1*/
							dto.setRcvrSafeNo2("");         					/**수화인안심번호2*/
							dto.setRcvrSafeNo3("");         					/**수화인안심번호3*/
							dto.setRcvrZipNo(shippingDto.getRecvZipCd());		/**수화인우편번호*/
							dto.setRcvrAddr(shippingDto.getRecvAddr1());   		/**수화인주소*/
							dto.setRcvrDetailAddr(shippingDto.getRecvAddr2() + " " + shippingDto.getRecvBldNo());      /**수화인상세주소*/
							dto.setOrdrrNm("");             							/**주문자명*/
							dto.setOrdrrTelNo1("");         							/**주문자전화번호1*/
							dto.setOrdrrTelNo2("");         							/**주문자전화번호2*/
							dto.setOrdrrTelNo3("");         							/**주문자전화번호3*/
							dto.setOrdrrCellNo1("");        							/**주문자휴대폰번호1*/
							dto.setOrdrrCellNo2("");        							/**주문자휴대폰번호2*/
							dto.setOrdrrCellNo3("");        							/**주문자휴대폰번호3*/
							dto.setOrdrrSafeNo1("");        							/**주문자안심번호1*/
							dto.setOrdrrSafeNo2("");        							/**주문자안심번호2*/
							dto.setOrdrrSafeNo3("");        							/**주문자안심번호3*/
							dto.setOrdrrZipNo("");          							/**주문자우편번호*/
							dto.setOrdrrAddr("");           							/**주문자주소*/
							dto.setOrdrrDetailAddr("");     							/**주문자상세주소*/
							dto.setInvcNo("");              							/**운송장번호(12자리);*/
							dto.setOriInvcNo(goodsInfo.getTrackingNo());           		/**원운송장번호*/
							dto.setOriOrdNo(String.valueOf(reqDto.getOdOrderId()));	/**원주문번호*/
							dto.setColctExpctYmd("");       							/**집화예정일자*/
							dto.setColctExpctHour("");      							/**집화예정시간*/
							dto.setShipExpctYmd("");        							/**배송예정일자*/
							dto.setShipExpctHour("");       							/**배송예정시간*/
							dto.setPrtSt("01");               							/**출력상태	01:미출력,02:선출력,03:선발번(반품은선발번이없음);*/
							dto.setArticleAmt(goodsInfo.getSalePrice());          		/**물품가액	*/
							dto.setRemark1("");             							/**배송메세지1(비고);*/
							dto.setRemark2(reqDto.getSendDeliveryMsg());			/**배송메세지2(송화인비고);*/
							dto.setRemark3(shippingDto.getDeliveryMsg());      			/**배송메세지3(수화인비고);*/
							dto.setCodYn("");               							/**COD여부대면결제서비스업체의경우대면결제발생시Y로셋팅*/
							dto.setGdsCd(String.valueOf(goodsInfo.getIlGoodsId())); 	/**상품코드	*/
							dto.setGdsNm(goodsInfo.getGoodsNm());   					/**상품명*/
							dto.setGdsQty(goodsInfo.getClaimCnt());						/**상품수량*/
							dto.setUnitCd("");              							/**단품코드*/
							dto.setUnitNm("");              							/**단품명*/
							dto.setGdsAmt(goodsInfo.getSalePrice()); 					/**상품가액*/
							dto.setDlvDv("01");               							/**택배구분	택배:'01',중량물(설치물류);:'02',중량물(비설치물류);:'03'*/
							dto.setRcptErrYn("N");           							/**접수에러여부	DEFAULT:'N'	*/
							dto.setRcptErrMsg("");          							/**접수에러메세지					*/
							dto.setEaiPrgsSt("01");           							/**EAI전송상태		DEFAULT:'01'*/
							dto.setEaiErrMsg("");           							/**에러메세지*/
							dto.setRegEmpId(Constants.CJ_DELIVERY_RETURN_ID);  			/**등록사원ID*/
							dto.setRegDtime(currentDate);								/**등록일시 */
							dto.setModiEmpId(Constants.CJ_DELIVERY_RETURN_ID); 			/**수정사원ID*/
							dto.setModiDtime(currentDate);								/**수정일시 */

							try {
								// CJ 주문접수
								claimProcessMapper.addCJLogisticsOrderAccept(dto);
								claimDetlVo.setRecallType(ClaimEnums.RecallType.RECALL_SUCCESS.getCode());
							}catch (Exception e) {
								log.error("=====CJ택배 반품접수 저장 오류======", e.getMessage());
								claimDetlVo.setRecallType(ClaimEnums.RecallType.RECALL_FAIL.getCode());
							}

							claimProcessMapper.putOrderClaimDetl(claimDetlVo);
						}
					}
				}
			}
		}
	}

	/**
	 * 주문 클레임 받는 배송지 [OD_CLAIM_SHIPPING_ZONE] Insert or Update
	 * @param reqDto
	 * @return
	 */
	private void mergeOrderClaimShippingZone(OrderClaimRegisterRequestDto reqDto, long odClaimId) {
		if (org.apache.commons.collections.CollectionUtils.isNotEmpty( reqDto.getRecvShippingList())) {

			for (OrderClaimRecvShippingZoneInfoDto recvDto : reqDto.getRecvShippingList()) {
				log.debug("받는배송지 상품 출고지  <{}>==<{}>, <{}>==<{}>", recvDto.getOdOrderDetlId(), recvDto.getOdOrderDetlId(), recvDto.getUrWarehouseId(), recvDto.getUrWarehouseId());

				OrderClaimGoodsInfoDto orderClaimGoodsInfoDto = reqDto.getGoodsInfoList().stream().filter(x -> x.getOdOrderDetlId() == recvDto.getOdOrderDetlId() && x.getUrWarehouseId() == recvDto.getUrWarehouseId()).findAny().get();

				if (orderClaimGoodsInfoDto != null) {

					log.debug("받는배송지 상품 출고지가 같다 <{}>==<{}>, <{}>==<{}>", recvDto.getOdOrderDetlId(), recvDto.getOdOrderDetlId(), recvDto.getUrWarehouseId(), recvDto.getUrWarehouseId());
					long odClaimShippingZoneId = recvDto.getOdClaimShippingZoneId() == 0 ? claimProcessMapper.getOdClaimShippingZoneId() : recvDto.getOdClaimShippingZoneId();
					ClaimShippingZoneVo claimShippingZoneVo = ClaimShippingZoneVo.builder()
							.odClaimShippingZoneId(odClaimShippingZoneId)	//주문클레임 배송지 PK
							.odClaimId(odClaimId)							//주문클레임 PK
							.odClaimDetlId(orderClaimGoodsInfoDto.getOdClaimDetlId()) 		//주문 클레임 상세 PK
							.urWarehouseId(recvDto.getUrWarehouseId()) 		//출고처 PK : UR_WAREHOUSE.UR_WAREHOUSE_ID
							.recvNm(recvDto.getRecvNm())					//수령인명
							.recvHp(recvDto.getRecvHp())					//수령인핸드폰
							.recvTel(recvDto.getRecvTel())					//수령인연락처
							.recvZipCd(recvDto.getRecvZipCd())				//수령인우편번호
							.recvAddr1(recvDto.getRecvAddr1())				//수령인주소1
							.recvAddr2(recvDto.getRecvAddr2())				//수령인주소2
							.recvBldNo(recvDto.getRecvBldNo())				//건물번호
							.deliveryMsg(recvDto.getDeliveryMsg())			//배송요청사항
							.doorMsgCd(recvDto.getDoorMsgCd())				//출입정보타입 공통코드(DOOR_MSG_CD)
							.doorMsg(recvDto.getDoorMsg())					//배송출입 현관 비밀번호
							.build();
					log.debug("주문 클레임 받는 배송지 OD_CLAIM_SHIPPING_ZONE <{}>" , claimShippingZoneVo);
					if (recvDto.getOdClaimShippingZoneId() == 0)
						claimProcessMapper.addOrderClaimShippingZone(claimShippingZoneVo);
					else
						claimProcessMapper.putOrderClaimShippingZone(claimShippingZoneVo);
				}
			}

		}
	}

	/**
	 * 주문 클레임 보내는 배송지 [OD_CLAIM_SEND_SHIPPING_ZONE] Insert or Update
	 * @param reqDto
	 * @return
	 */
	public void mergeOrderClaimSendShippingZone(OrderClaimRegisterRequestDto reqDto) {

		long odClaimSendShippingZoneId = reqDto.getOdClaimSendShippingZoneId() == 0 ? claimProcessMapper.getOdClaimSendShippingZoneId() : reqDto.getOdClaimSendShippingZoneId();

		ClaimSendShippingZoneVo claimSendShippingZoneVo = ClaimSendShippingZoneVo.builder()
				.odClaimSendShippingZoneId(odClaimSendShippingZoneId)			//주문클레임 보내는 배송지 PK
				.odClaimId(reqDto.getOdClaimId())								//주문클레임 PK
				.recvNm(reqDto.getSendRecvNm())				//수령인명
				.recvHp(reqDto.getSendRecvHp())				//수령인핸드폰
				.recvTel(reqDto.getSendRecvTel())			//수령인연락처
				.recvZipCd(reqDto.getSendRecvZipCd())		//수령인우편번호
				.recvAddr1(reqDto.getSendRecvAddr1())		//수령인주소1
				.recvAddr2(reqDto.getSendRecvAddr2())		//수령인주소2
				.recvBldNo(reqDto.getSendRecvBldNo())		//건물번호
				.deliveryMsg(reqDto.getSendDeliveryMsg())	//배송요청사항
				.doorMsgCd(reqDto.getSendDoorMsgCd())		//출입정보타입 공통코드(DOOR_MSG_CD)
				.doorMsg(reqDto.getSendDoorMsg())			//배송출입 현관 비밀번호
				.build();

		log.debug("주문 클레임 보내는 배송지 OD_CLAIM_SEND_SHIPPING_ZONE <{}>" , claimSendShippingZoneVo);
		if (reqDto.getOdClaimSendShippingZoneId() == 0)
			claimProcessMapper.addOrderClaimSendShippingZone(claimSendShippingZoneVo);
		else
			claimProcessMapper.putOrderClaimSendShippingZone(claimSendShippingZoneVo);
	}

	/**
	 * 택배사가 롯데 택배이면 반품 접수를 한다.
	 * @param reqDto
	 * @return
	 */
	private LotteGlogisClientOrderResponseDto lotteReturnReceive(OrderClaimRegisterRequestDto reqDto) {

		LotteGlogisClientOrderResponseDto responseDto = null;

		if (org.apache.commons.collections.CollectionUtils.isNotEmpty(reqDto.getGoodsInfoList()) && org.apache.commons.collections.CollectionUtils.isNotEmpty(reqDto.getRecvShippingList())) {
			for (OrderClaimGoodsInfoDto goodsInfo : reqDto.getGoodsInfoList()) {
				log.debug("롯데택배 ClaimStatusCd :: <{}>, ReturnsYn :: <{}>, PsShippingCompId :: <{}>, 롯데 택배사 코드 :: <{}>", reqDto.getClaimStatusCd(), reqDto.getReturnsYn(), goodsInfo.getPsShippingCompId(), OrderEnums.LogisticsCd.LOTTE.getCode());

				// 롯데택배 PsShippingCompId 가져오기
				String lottePsShippingCompId = claimProcessMapper.getPsShippingCompId(OrderEnums.LogisticsCd.LOTTE.getLogisticsCode());

				//주문클레임이 반품승인이면서 회수여부가 Y 이고 롯데 택배사 이면 반품 접수 시킨다.
				if (goodsInfo.getPsShippingCompId() == Long.parseLong(lottePsShippingCompId)) {	//롯데택배 일 때
					for (OrderClaimRecvShippingZoneInfoDto shippingDto : reqDto.getRecvShippingList()) {

						boolean isReturnSave = false; // 반품택배접수 저장여부
						boolean ifRecallTypeSave = false; // 반품택배접수 연동 성공 저장여부

						// 출고처그룹이 온라인사업부 인지 체크
						// 추가: 출고처가 용인물류만 체크 조건 추가
						int chkCnt = claimProcessMapper.selectUrWarehouseGrpIdCheck(shippingDto.getUrWarehouseId());

						ClaimDetlVo claimDetlVo = new ClaimDetlVo();
						claimDetlVo.setOdClaimDetlId(goodsInfo.getOdClaimDetlId());

						if(chkCnt > 0){
							if (shippingDto.getUrWarehouseId() == goodsInfo.getUrWarehouseId() && shippingDto.getOdOrderDetlId() == goodsInfo.getOdOrderDetlId()) {
								log.debug("출고지가 같을 때 <{}>, <{}>", shippingDto.getUrWarehouseId(), goodsInfo.getUrWarehouseId());
								LotteGlogisClientOrderRequestDto lotteDto = new LotteGlogisClientOrderRequestDto();
								lotteDto.setJobCustCd(ErpEnums.ParcelServiceCustId.LOTTE_CUST_CD.getCode());    //거래처코드(6 자리 택배코드)
								lotteDto.setUstRtgSctCd("02");                                    //출고반품구분 (01:출고 02:반품)
								lotteDto.setOrdSct("1");                                        //오더구분 (1:일반 2:교환 3:AS)
								lotteDto.setFareSctCd("03");                                    //운임구분 (03:신용 04:복합)
								lotteDto.setOrdNo(String.valueOf(goodsInfo.getOdClaimDetlId()));//주문번호 (상세번호)
								lotteDto.setInvNo("");                                            //운송장번호
								lotteDto.setOrglInvNo(goodsInfo.getTrackingNo());                //원송장번호
								lotteDto.setSnperNm(reqDto.getSendRecvNm());                //송하인명
								lotteDto.setSnperTel(reqDto.getSendRecvTel());                //송하인전화번호
								lotteDto.setSnperCpno(reqDto.getSendRecvHp());                //송하인휴대전화번호
								lotteDto.setSnperZipcd(reqDto.getSendRecvZipCd());            //송하인우편번호
								lotteDto.setSnperAdr(reqDto.getSendRecvAddr1() + " " + reqDto.getSendRecvAddr2() + " " + reqDto.getSendRecvBldNo());    //송하인주소 (기본주소 + 상세주소)
								lotteDto.setAcperNm(shippingDto.getRecvNm());                    //수하인명
								lotteDto.setAcperTel(shippingDto.getRecvTel());                    //수하인전화번호
								lotteDto.setAcperCpno(shippingDto.getRecvHp());                    //수하인휴대전화번호
								lotteDto.setAcperZipcd(shippingDto.getRecvZipCd());                //수하인우편번호
								lotteDto.setAcperAdr(shippingDto.getRecvAddr1() + " " + shippingDto.getRecvAddr2() + " " + shippingDto.getRecvBldNo());        //수하인주소 (기본주소 + 상세주소)
								lotteDto.setBoxTypCd("C");                                        //박스크기 (A, B, C, D, E, F)
								lotteDto.setGdsNm(goodsInfo.getGoodsNm());                        //상품명
								lotteDto.setDlvMsgCont(reqDto.getSendDeliveryMsg());        //배달메세지내용
								lotteDto.setCusMsgCont(shippingDto.getDeliveryMsg());            //고객메세지내용
								lotteDto.setPickReqYmd("");                                    //집하요청일
								lotteDto.setBdpkSctCd("N");                                        //합포장여부(Y|N)

								responseDto = lotteGlogisBiz.convertLotteGlogisClientOrder(lotteDto);

								// API 성공
								if (responseDto.getResponseCode().equals(ApiEnums.LotteGlogisClientOrderApiResponse.SUCCESS.getCode())) {
									isReturnSave = true;
									ifRecallTypeSave = true;
									claimDetlVo.setRecallType(ClaimEnums.RecallType.RECALL_SUCCESS.getCode());
								}
								// API 실패
								else {
									isReturnSave = false;
									ifRecallTypeSave = true;
									claimDetlVo.setRecallType(ClaimEnums.RecallType.RECALL_FAIL.getCode());
								}
							}
						}
						// 온라인사업부 아닌 롯데택배
						else {
							isReturnSave = false;
							ifRecallTypeSave = true;
							claimDetlVo.setRecallType(ClaimEnums.RecallType.RECALL_NONE.getCode());
						}

						if(isReturnSave) {
							ReturnTrackingNumberVo returnTrackingNumberVo = ReturnTrackingNumberVo.builder()
									.odClaimDetlId(StringUtil.nvl(goodsInfo.getOdClaimDetlId()))
									.trackingNo(StringUtil.nvl(goodsInfo.getOdClaimDetlId()))
									.sort("1")
									.logisticsCd(OrderEnums.LogisticsCd.LOTTE.getLogisticsCode())
									.build();
							claimProcessMapper.addReturnTrackingNumber(returnTrackingNumberVo);
						}

						if(ifRecallTypeSave)
							claimProcessMapper.putOrderClaimDetl(claimDetlVo);
					}
				}
			}
		}

		return responseDto;
	}

	/**
	 * 클레임상태변경
	 * @param requestDto
	 * @param isClaimSave
	 */
	public void setClaimStatus(OrderClaimRegisterRequestDto requestDto, boolean isClaimSave) {

		ClaimDetlVo claimDetlVo = new ClaimDetlVo();
		String claimStatusTp = requestDto.getClaimStatusTp(); // 클레임상태구분
		String claimStatusCd = requestDto.getClaimStatusCd(); // 클레임주문상태코드
		long odClaimId = requestDto.getOdClaimId();
		List<OrderClaimGoodsInfoDto> orderClaimGoodsInfoList = requestDto.getGoodsInfoList(); // 상품정보 리스트
		long urUserId = Long.parseLong(requestDto.getUrUserId());
		boolean isClaimStatusSave = false; // 상태값저장여부
		boolean isDenyDefe = false; // 거부유형여부

		if(OrderEnums.OrderStatus.INCOM_BEFORE_CANCEL_COMPLETE.getCode().equals(claimStatusCd)) isClaimSave = true;

		if(isClaimSave) {
			for(OrderClaimGoodsInfoDto orderClaimGoodsInfoDto : orderClaimGoodsInfoList) {
				long odClaimDetlId = orderClaimGoodsInfoDto.getOdClaimDetlId();
				// 취소
				if(OrderClaimEnums.ClaimStatusTp.CANCEL.getCode().equals(claimStatusTp)) {
					// 취소완료
					if(OrderEnums.OrderStatus.CANCEL_COMPLETE.getCode().equals(claimStatusCd)) {
						claimDetlVo.setCcId(urUserId);
						claimDetlVo.setCeId(urUserId);
						claimDetlVo.setPriorityUndelivered(StringUtil.nvl(requestDto.getPriorityUndelivered(),"N"));
						isClaimStatusSave = true;
					}
					// 취소거부
					else if(OrderEnums.OrderStatus.CANCEL_DENY_DEFE.getCode().equals(claimStatusCd)) {
						isDenyDefe = true;
					}
				}
				// 반품
				else if(OrderClaimEnums.ClaimStatusTp.RETURN.getCode().equals(claimStatusTp)) {
					// 반품승인
					if(OrderEnums.OrderStatus.RETURN_ING.getCode().equals(claimStatusCd)) {
						claimDetlVo.setRiId(urUserId);
						isClaimStatusSave = true;
					}
					// 반품보류
					else if(OrderEnums.OrderStatus.RETURN_DEFER.getCode().equals(claimStatusCd)) {
						claimDetlVo.setRfId(urUserId);
						isClaimStatusSave = true;
					}
					// 반품완료
					else if(OrderEnums.OrderStatus.RETURN_COMPLETE.getCode().equals(claimStatusCd)) {
						claimDetlVo.setRcId(urUserId);
						claimDetlVo.setCeId(urUserId);
						claimDetlVo.setPriorityUndelivered(StringUtil.nvl(requestDto.getPriorityUndelivered(),"N"));
						isClaimStatusSave = true;
					}
					// 반품거부
					else if(OrderEnums.OrderStatus.RETURN_DENY_DEFER.getCode().equals(claimStatusCd)) {
						isDenyDefe = true;
					}
				}

				if(isClaimStatusSave) {
					// 상태값저장
					claimDetlVo.setOdClaimDetlId(odClaimDetlId);
					claimDetlVo.setClaimStatusCd(claimStatusCd);
					claimDetlVo.setAddPaymentShippingPrice(orderClaimGoodsInfoDto.getAddPaymentShippingPrice());

					claimProcessMapper.putOrderClaimDetl(claimDetlVo);

					// 클레임 상세 이력 등록
					ClaimDetlHistVo claimDetlHistVo = ClaimDetlHistVo.builder()
										.odClaimDetlId(odClaimDetlId)
										.statusCd(claimStatusCd)
										.histMsg(OrderEnums.OrderStatus.findByCode(claimStatusCd).getCodeName())
										.createId(urUserId)
										.build();
					orderStatusMapper.putClaimDetailStatusHist(claimDetlHistVo);

				}
			}

			// 거부일때
			if(isDenyDefe) {

				String orderStatusCd = OrderEnums.OrderStatus.CANCEL_DENY_DEFE.getCode().equals(claimStatusCd) ? OrderEnums.OrderStatus.CANCEL_APPLY.getCode() : OrderEnums.OrderStatus.RETURN_APPLY.getCode();
				claimDetlVo.setOrderStatusCd(orderStatusCd);
				claimDetlVo.setOdClaimId(odClaimId);
				claimDetlVo.setClaimStatusCd(claimStatusCd);

				// 클레임 상세 이력 등록
				ClaimDetlHistVo claimDetlHistVo = ClaimDetlHistVo.builder()
						.odClaimId(odClaimId)
						.statusCd(claimStatusCd)
						.histMsg(OrderEnums.OrderStatus.findByCode(claimStatusCd).getCodeName())
						.prevStatusCd(orderStatusCd)
						.createId(urUserId)
						.build();
				orderStatusMapper.putDenyDefeClaimDetailStatusHist(claimDetlHistVo);

				// 클레임 거부 상태 변경
				claimProcessMapper.putDenyDefeOrderClaimDetl(claimDetlVo);
			}
		}

	}

	/**
	 * 환불상태변경
	 * @param odClaimId
	 * @param requestDto
	 * @param refundStatusCd
	 * @param isClaimSave
	 */
	public void setClaimRefundPointPriceStatus(long odClaimId, OrderClaimRegisterRequestDto requestDto, String refundStatusCd, List<?> claimList, boolean isClaimSave) {

		ClaimDetlVo claimDetlVo = new ClaimDetlVo();
		long urUserId = Long.parseLong(requestDto.getUrUserId());

		// 환불
		if (requestDto.getRefundPointPrice() != 0 || requestDto.getRefundPrice() != 0) {
			claimDetlVo.setOdClaimId(odClaimId);
			// 환불요청으로 업데이트
			if (OrderEnums.OrderStatus.REFUND_APPLY.getCode().equals(refundStatusCd)) {
				claimDetlVo.setFaId(urUserId);//환불요청등록자
				claimProcessMapper.putOrderClaimDetl(claimDetlVo);

				if(ObjectUtils.isNotEmpty(claimList)) {
					for (int i = 0; i < claimList.size(); i++) {
						OrderClaimGoodsInfoDto orderClaimDetlListInfoDto = (OrderClaimGoodsInfoDto) claimList.get(i);

						// 클레임 상세 이력 등록
						ClaimDetlHistVo claimDetlHistVo = ClaimDetlHistVo.builder()
								.odClaimDetlId(orderClaimDetlListInfoDto.getOdClaimDetlId())
								.statusCd(refundStatusCd)
								.histMsg(OrderEnums.OrderStatus.findByCode(refundStatusCd).getCodeName())
								.createId(urUserId)
								.build();
						orderStatusMapper.putClaimDetailStatusHist(claimDetlHistVo);
					}
				}
			}
			// 환불완료로 업데이트
			else if (OrderEnums.OrderStatus.REFUND_COMPLETE.getCode().equals(refundStatusCd)) {
				if(ObjectUtils.isNotEmpty(claimList)) {
					for (int i = 0; i < claimList.size(); i++) {

						long odClaimDetlId = 0;
						Object objectDto = claimList.get(i);

						if (isClaimSave) {
							OrderClaimGoodsInfoDto orderClaimGoodsInfoDto = (OrderClaimGoodsInfoDto) objectDto;
							odClaimDetlId = orderClaimGoodsInfoDto.getOdClaimDetlId();
						} else if (!isClaimSave) {
							OrderClaimDetlListInfoDto orderClaimDetlListInfoDto = (OrderClaimDetlListInfoDto) objectDto;
							odClaimDetlId = orderClaimDetlListInfoDto.getOdClaimDetlId();
						}

						claimDetlVo.setFcId(urUserId);    //환불완료등록자
						claimDetlVo.setOdClaimDetlId(odClaimDetlId);
						claimProcessMapper.putOrderClaimDetl(claimDetlVo);

						// 클레임 상세 이력 등록
						ClaimDetlHistVo claimDetlHistVo = ClaimDetlHistVo.builder()
								.odClaimDetlId(odClaimDetlId)
								.statusCd(refundStatusCd)
								.histMsg(OrderEnums.OrderStatus.findByCode(refundStatusCd).getCodeName())
								.createId(urUserId)
								.build();
						orderStatusMapper.putClaimDetailStatusHist(claimDetlHistVo);
					}
				}
			}

		}

	}

	/**
	 * 철회상태변경
	 * @param odClaimId
	 * @param targetStatusCd
	 * @param createId
	 * @param sourceStatusCd
	 */
	public void setClaimRestoreStatus(long odClaimId, String targetStatusCd, long createId, String sourceStatusCd) {

		ClaimDetlVo claimDetlVo = new ClaimDetlVo();

		// 취소철회
		if(OrderEnums.OrderStatus.CANCEL_WITHDRAWAL.getCode().equals(targetStatusCd)) {
			claimDetlVo.setCwId(createId);
			claimDetlVo.setOdClaimId(odClaimId);
		}
		// 반품철회
		else if(OrderEnums.OrderStatus.RETURN_WITHDRAWAL.getCode().equals(targetStatusCd)) {
			claimDetlVo.setRwId(createId);
			claimDetlVo.setOdClaimId(odClaimId);

		}

		claimDetlVo.setOrderStatusCd(sourceStatusCd);
		claimDetlVo.setClaimStatusCd(targetStatusCd);

		// 클레임 상세 이력 등록
		ClaimDetlHistVo claimDetlHistVo = ClaimDetlHistVo.builder()
				.odClaimId(odClaimId)
				.statusCd(targetStatusCd)
				.histMsg(OrderEnums.OrderStatus.findByCode(targetStatusCd).getCodeName())
				.prevStatusCd(sourceStatusCd)
				.createId(createId)
				.build();
		orderStatusMapper.putDenyDefeClaimDetailStatusHist(claimDetlHistVo);

		// 클레임 상태 변경
		claimProcessMapper.putDenyDefeOrderClaimDetl(claimDetlVo);
	}

	/**
	 * 재배송 프로세스
	 * @param requestDto
	 * @return
	 * @throws Exception
	 */
	public ClaimDetlVo setOrderClaimRedeliveryComplete(OrderClaimRegisterRequestDto requestDto) throws Exception {

		ClaimDetlVo claimDetlVo = new ClaimDetlVo();
		long urUserId = Long.parseLong(requestDto.getUrUserId());

		log.debug("재배송 시작!!!");

		log.debug("클레임 상태 코드 ::: <{}>", requestDto.getClaimStatusCd());

		long ecId = Long.parseLong(requestDto.getUrUserId());
		long orderIfId = Long.parseLong(requestDto.getUrUserId());
		long shippingId = Long.parseLong(requestDto.getUrUserId());
		long deliveryId = Long.parseLong(requestDto.getUrUserId());

		log.debug("orderIfId ::: <{}>, shippingId :: <{}>, deliveryId : <{}>", orderIfId, shippingId, deliveryId);

		if (CollectionUtils.isNotEmpty(requestDto.getGoodsInfoList())) {

			List<OrderClaimGoodsInfoDto> goodsInfoList = requestDto.getGoodsInfoList();

			log.debug("재배송 상품 존재 여부 :: <{}>", goodsInfoList.isEmpty());

			// 상품 정보가 존재할 경우
			if(!goodsInfoList.isEmpty()) {
				// 상품 정보가 재배송, 대체배송일 경우 주문상세PK 정보가 있는 상품만 Filter
				List<OrderClaimGoodsInfoDto> filterGoodsInfoList = goodsInfoList.stream().filter(x -> (OrderClaimEnums.RedeliveryType.RETURN_DELIVERY.getCode().equals(x.getRedeliveryType())
																									|| (OrderClaimEnums.RedeliveryType.SUBSTITUTE_GOODS.getCode().equals(x.getRedeliveryType())) && x.getOdOrderDetlId() != 0))
																							.collect(Collectors.toList());
				for(OrderClaimGoodsInfoDto goodsInfo : filterGoodsInfoList) {

					log.debug("재배송 또는 대체상품 dto.orderIfDt :: <{}>, dto.shippingDt :: <{}>, dto.deliveryDt :: <{}>", goodsInfo.getOrderIfDt(), goodsInfo.getShippingDt(), goodsInfo.getDeliveryDt());
					LocalDate orderIfDt = LocalDate.parse(goodsInfo.getOrderIfDt(), DateTimeFormatter.ISO_DATE);
					LocalDate shippingDt = LocalDate.parse(goodsInfo.getShippingDt(), DateTimeFormatter.ISO_DATE);
					LocalDate deliveryDt = LocalDate.parse(goodsInfo.getDeliveryDt(), DateTimeFormatter.ISO_DATE);

					log.debug("재배송 또는 대체상품 orderIfDt :: <{}>, shippingDt :: <{}>, deliveryDt :: <{}>", orderIfDt, shippingDt, deliveryDt);
					log.debug("재배송 또는 대체상품 getUrWarehouseId :: <{}>, getIlGoodsId :: <{}> getIsDawnDelivery :: <{}>, getOrderCnt :: <{}>, getGoodsCycleTp :: <{}> ", goodsInfo.getUrWarehouseId(), goodsInfo.getIlGoodsId(), goodsInfo.getIsDawnDelivery(), goodsInfo.getOrderCnt(), goodsInfo.getGoodsCycleTp());
					log.debug("claimCnt :: <{}>", goodsInfo.getClaimCnt());

					goodsInfo.setOrderYn(OrderClaimEnums.AllTypeYn.ALL_TYPE_N.getCode());	// 재고 원복 대상

					long odOrderId = goodsInfo.getOdOrderId();
					long odOrderDetlStepId = goodsInfo.getOdOrderDetlStepId();
					int odOrderDetlSeq = 0;

					// 재배송 상품 금액정보 조회
					OrderClaimReShippingPaymentInfoDto orderPaymentInfo = claimRequestMapper.getOrderClaimReShippingGoodsPaymentInfo(goodsInfo.getOdOrderId(),
																																		goodsInfo.getOdOrderDetlId(),
																																		goodsInfo.getClaimCnt());

					List<OrderClaimGoodsInfoDto> subGoodsInfoList = null;
					// 주문 상세 Depth ID가 3일 경우 : 이미 재배송 처리된 상품의 재배송
					// 주문 상세번호가 존재하지 않고, 주문 상세 PK와 SUB 상품의 부모 상세 PK가 동일할 경우
					// 이전 재배송 처리된 상품의 odOrderParentId == 신청하고자하는 상품의 odOrderParentId 비교
					if(goodsInfo.getOdOrderDetlDepthId() > 2) {
						subGoodsInfoList = goodsInfoList.stream().filter(x -> x.getOdOrderDetlId() == 0 && goodsInfo.getOdOrderDetlParentId() == x.getOdOrderDetlParentId()).collect(Collectors.toList());
					}
					// 주문 상세 Depth ID가 3이 아닐 경우 : 최초 주문상품의 재배송
					// 원상품의 odOrderDetlId == 재배송/대체배송 상품의 odOrderParentId 비교
					else {
						subGoodsInfoList = goodsInfoList.stream().filter(x -> x.getOdOrderDetlId() == 0 && goodsInfo.getOdOrderDetlId() == x.getOdOrderDetlParentId()).collect(Collectors.toList());
					}

					log.debug("상품 재배송 / 대체배송 타입 :: <{}>, subGoodsInfoList.isEmpty() :: <{}>", goodsInfo.getRedeliveryType(), subGoodsInfoList.isEmpty());
					if(!subGoodsInfoList.isEmpty()) {

						log.debug("Filter 상품 목록 수 :: <{}>", subGoodsInfoList.size());
						int cartCouponPrice = orderPaymentInfo.getCartCouponPrice();
						int goodsCouponPrice = orderPaymentInfo.getGoodsCouponPrice();
						int employeeDiscountPrice = orderPaymentInfo.getEmployeeDiscountPrice();
						List<Long> odOrderDetlIds = new ArrayList<>();
						String isLastYn = OrderClaimEnums.AllTypeYn.ALL_TYPE_N.getCode();

						// 할인비율로 Sort
						subGoodsInfoList.sort(new Comparator<OrderClaimGoodsInfoDto>() {
							@Override
							public int compare(OrderClaimGoodsInfoDto dto1, OrderClaimGoodsInfoDto dto2) {
								return (dto1.getDiscountRate() > dto2.getDiscountRate() ? -1 : 1);
							}
						});

						for (int i=0; i<subGoodsInfoList.size(); i++) {

							OrderClaimGoodsInfoDto subGoodsInfo = subGoodsInfoList.get(i);

							orderRegistrationBiz.putOdOrderDetlStepId(odOrderId, odOrderDetlStepId);

							long odOrderDetlId = orderRegistrationBiz.getOrderDetlSeq();
							subGoodsInfo.setOrderYn(OrderClaimEnums.AllTypeYn.ALL_TYPE_Y.getCode());	// 재고 차감 대상
							subGoodsInfo.setOdOrderDetlId(odOrderDetlId);								// 주문상세번호 Set

							odOrderDetlIds.add(odOrderDetlId);
							log.debug("상품 클레임 상세 업데이트 완료 :: 클레임 PK <{}>, 클레임 상세 PK <{}>, 주문상세 PK <{}>", requestDto.getOdClaimId(), subGoodsInfo.getOdClaimDetlId(), odOrderDetlId);
							GoodsInfoDto goodsDto = orderCreateBiz.getGoodsInfo(subGoodsInfo.getIlGoodsId());
							odOrderDetlSeq = claimProcessMapper.getOrderDetlSeq(requestDto.getOdOrderId());

							// 대체배송일 경우 비율로 할인 금액 계산
							if(OrderClaimEnums.RedeliveryType.SUBSTITUTE_GOODS.getCode().equals(goodsInfo.getRedeliveryType())) {
								// 마지막 일때
								if((subGoodsInfoList.size() - 1) == i) {
									subGoodsInfo.setCartCouponPrice(cartCouponPrice);
									subGoodsInfo.setGoodsCouponPrice(goodsCouponPrice);
									subGoodsInfo.setDirectPrice(employeeDiscountPrice);
									isLastYn = OrderClaimEnums.AllTypeYn.ALL_TYPE_Y.getCode();
								}
								else {
									// 총 할인금액 * (할인비율 / (double) 100);
									int rateCartCouponPrice = (int) (orderPaymentInfo.getCartCouponPrice() * (subGoodsInfo.getDiscountRate() / (double) 100));
									int rateGoodsCouponPrice = (int) (orderPaymentInfo.getGoodsCouponPrice() * (subGoodsInfo.getDiscountRate() / (double) 100));
									int rateEmployeeDiscountPrice = (int) (orderPaymentInfo.getEmployeeDiscountPrice() * (subGoodsInfo.getDiscountRate() / (double) 100));

									subGoodsInfo.setCartCouponPrice(rateCartCouponPrice);
									subGoodsInfo.setGoodsCouponPrice(rateGoodsCouponPrice);
									subGoodsInfo.setDirectPrice(rateEmployeeDiscountPrice);

									// 계산된 금액을 총 할인금액에서 차감한다
									cartCouponPrice -= rateCartCouponPrice;
									goodsCouponPrice -= rateGoodsCouponPrice;
									employeeDiscountPrice -= rateEmployeeDiscountPrice;
								}
							}
							else {
								subGoodsInfo.setCartCouponPrice(cartCouponPrice);
								subGoodsInfo.setGoodsCouponPrice(goodsCouponPrice);
								subGoodsInfo.setDirectPrice(employeeDiscountPrice);
								subGoodsInfo.setDiscountRate(100);
								isLastYn = OrderClaimEnums.AllTypeYn.ALL_TYPE_Y.getCode();
							}

							// 주문상세할인등록
							OrderDetlDiscountInfoDto orderDetlDiscountInfoDto = new OrderDetlDiscountInfoDto();
							orderDetlDiscountInfoDto.setOdOrderId(goodsInfo.getOdOrderId());
							orderDetlDiscountInfoDto.setOdOrderDetlId(odOrderDetlId);
							orderDetlDiscountInfoDto.setTargetOdOrderDetlId(goodsInfo.getOdOrderDetlId());
							orderDetlDiscountInfoDto.setOrderCnt(goodsInfo.getOrgOrderCnt());
							orderDetlDiscountInfoDto.setClaimCnt(goodsInfo.getClaimCnt());
							orderDetlDiscountInfoDto.setDiscountRate(subGoodsInfo.getDiscountRate());
							orderDetlDiscountInfoDto.setOdOrderDetlIds(odOrderDetlIds);
							orderDetlDiscountInfoDto.setIsLastYn(isLastYn);
							orderDetlDiscountInfoDto.setReDeliveryType(goodsInfo.getRedeliveryType());
							orderDetlDiscountInfoDto.setGoodsCouponPrice(subGoodsInfo.getGoodsCouponPrice());

							orderRegistrationBiz.selectAddOrderDetlDiscount(orderDetlDiscountInfoDto);

							LocalDate subOrderIfDt = LocalDate.parse(subGoodsInfo.getOrderIfDt(), DateTimeFormatter.ISO_DATE);
							LocalDate subShippingDt = LocalDate.parse(subGoodsInfo.getShippingDt(), DateTimeFormatter.ISO_DATE);
							LocalDate subDeliveryDt = LocalDate.parse(subGoodsInfo.getDeliveryDt(), DateTimeFormatter.ISO_DATE);

							OrderDetlVo orderDetlVo = OrderDetlVo.builder()
									.odOrderDetlId(odOrderDetlId)                              /** 주문상세 pk*/
									.odOrderDetlSeq(odOrderDetlSeq)                            /** 주문상세 순번(라인번호) 주문번호에 대한 순번*/
									.odOrderId(odOrderId)                                      /** 주문 pk*/
									.odOrderDetlStepId(odOrderDetlStepId - 1)                  /** 주문상세 정렬 키*/
									.odOrderDetlDepthId(3)                                     /** 주문상세 뎁스*/
									.odOrderDetlParentId(subGoodsInfo.getOdOrderDetlParentId())/** 주문상세 부모 id*/
									.odShippingPriceId(goodsInfo.getOdShippingPriceId())       /** 주문 배송비 pk : odShippingPrice.odShippingPriceId*/
									.odShippingZoneId(subGoodsInfo.getOdShippingZoneId())      /** 주문 배송지 pk : odShippingZone.odShippingZoneId*/
									.ilGoodsShippingTemplateId(subGoodsInfo.getIlGoodsShippingTemplateId())    /** 배송비 정책 pk : ilGoodsShippingTemplate.ilGoodsShippingTemplateId*/
									.urWarehouseId(goodsDto.getUrWarehouseId())                /** 출고처 pk : urWarehouse.urWarehouseId*/
									.urSupplierId(goodsDto.getUrSupplierId())                  /** 공급업체 pk : ilItem.urSupplierId*/
									.ilGoodsReserveOptnId(goodsDto.getIlGoodsReserveOptnId())  /** 예약정보 pk : ilGoodsReserveOptn.ilGoodsReserveOptnId*/
									.evExhibitId(goodsDto.getEvExhibitId())                    /** 기획전 pk : evExhibit.evExhibitId*/
									.promotionTp("")                                           /** 기획전 유형 골라담기(균일가)(cartPromotionTp.exhibitSelect), 녹즙내맘대로 주문 (cartPromotionTp.greenjuiceSelect)*/
									.orderStatusCd(OrderEnums.OrderStatus.INCOM_COMPLETE.getCode())    /** 정상주문상태 odStatusInfo.statusCd*/
									.collectionMallDetailId(null)                              /** 수집몰 상세번호 (이지어드민 seq)*/
									.outmallDetailId(null)                                     /** 외부몰 상세번호 (이지어드민 orderIdSeq, orderIdSeq2)*/
									.urWarehouseGrpCd(goodsDto.getUrWarehouseGroupCd())        /** 출고처그룹 공통코드: (warehouseGroup)  own: 자사, account: 거래처, accountOrga: 산지직송, accountSourcing: 업체소싱, accountJuice: 녹즙건강생활*/
									.storageTypeCd(subGoodsInfo.getStorageTypeCd())            /** 상품보관방법 ilItem.storageMethodTp 공통코드(erpStorageType) cool: 냉장, frozen: 냉동, room: 실온, ordinary: 상온, etc: 기타, notDefined: 미정*/
									.goodsTpCd(subGoodsInfo.getGoodsTpCd())                    /** 상품유형 ilGoods.goodsTp 공통코드(goodsType) normal: 일반, disposal: 폐기임박, gift: 증정, additional: 추가, package: 묶음, daily: 일일, shopOnly: 매장, rental: 렌탈, incorporeity: 무형*/
									.goodsDailyTp(subGoodsInfo.getGoodsDailyTp())              /** 일일상품 유형(goodsDailyTp : greenjuice/babymeal/eatsslim )*/
									.orderStatusDeliTp(subGoodsInfo.getOrderStatusDeliTp())    /** 주문상태 배송유형 공통코드: orderStatusDeliTp*/
									.goodsDeliveryType(subGoodsInfo.getGoodsDeliveryType())        /** 배송유형 공통코드(goodsDeliveryType) normal: 일반, dawn: 새벽, shop: 매장, daily: 일일, noDelivery: 배송안함, reservation: 예약*/
									.saleTpCd(goodsDto.getSaleTp())                            /** 판매유형 ilGoods.saleTp 공통코드(saleType) normal: 일반, regular: 일반/정기, reservation: 예약, shop: 매장, daily: 일일*/
									.ilCtgryStdId(goodsDto.getIlCtgryStdId())                  /** 표준카테고리 : ilItem.ilCtgryStdId*/
									.ilCtgryDisplayId(goodsDto.getIlCtgryDisplayId())          /** 전시카테고리 : ilGoodsCtgry.ilCtgryId , basicYn=y, mallDiv = mallDiv.pulmu */
									.ilCtgryMallId(goodsDto.getIlCtgryMallId())                /** 몰인몰카테고리 : ilGoodsCtgry.ilCtgryId , basicYn=y, mallDiv = mallDiv.pulmu*/
									.itemBarcode(subGoodsInfo.getItemBarcode())                /** 품목바코드 : ilItem.itemBarcode*/
									.ilItemCd(subGoodsInfo.getIlItemCd())                      /** 품목코드 pk : ilItem.ilItemCd*/
									.ilGoodsId(subGoodsInfo.getIlGoodsId())                    /** 상품 pk : ilGoods.ilGoodsId*/
									.goodsNm(subGoodsInfo.getGoodsNm())                        /** 상품명 : ilGoods.goodsNm*/
									.taxYn(goodsDto.getTaxYn())                                /** 과세구분(y: 과세, n: 면세) : ilItem.taxYn*/
									.goodsDiscountTp("")                                       /** 할인유형(none, 우선, 올가, 즉시, 적용불가) : 공통코드(goodsDiscountTp)*/
									.prdSeq(0)                                                 /** 이지어드민 상품관리번호*/
									.orderCnt(subGoodsInfo.getClaimCnt())                      /** 주문수량*/
									.cancelCnt(0)                                              /** 주문취소수량*/
									.standardPrice(subGoodsInfo.getStandardPrice())            /** 원가 : ilGoods.standardPrice*/
									.recommendedPrice(subGoodsInfo.getRecommendedPrice())      /** 정상가 : ilGoods.recommendedPrice*/
									.salePrice(subGoodsInfo.getSalePrice())                    /** 판매가 : ilGoods.salePrice*/
									.cartCouponPrice(subGoodsInfo.getCartCouponPrice())        /** 장바구니쿠폰할인금액*/
									.goodsCouponPrice(subGoodsInfo.getGoodsCouponPrice())      /** 상품쿠폰할인금액*/
									.directPrice(subGoodsInfo.getDirectPrice())    	           /** 상품,장바구니쿠폰 할인 제외한 할인금액*/
									.paidPrice(subGoodsInfo.getPaidPrice())                    /** 결제금액 (쿠폰까지 할인된 금액) --- 임직원 할인 금액*/
									.totSalePrice(subGoodsInfo.getTotSalePrice())              /** 총상품금액 */
									.ecId(ecId)                                                /** 재배송등록자 */
									.orderIfId(orderIfId)                                      /** 주문 i/f 등록자*/
									.orderIfDt(subOrderIfDt)                                   /** 주문 i/f 일자*/
									.shippingId(shippingId)                                    /** 출고예정일등록자*/
									.shippingDt(subShippingDt)                                 /** 출고예정일일자*/
									.deliveryId(deliveryId)                                    /** 도착예정일등록자*/
									.deliveryDt(subDeliveryDt)                                 /** 도착예정일일자*/
									.batchExecFl(OrderClaimEnums.AllTypeYn.ALL_TYPE_N.getCode())/** 배치실행여부*/
									.batchExecDt(null)                                         /** 배치실행일자*/
									.salesExecFl(OrderClaimEnums.AllTypeYn.ALL_TYPE_N.getCode())/** 매출연동여부 */
									.salesExecDt(null)                                         /** 매출연동일자 */
									.redeliveryType(goodsInfo.getRedeliveryType())             /** 재배송구분 */
									.build();
							orderRegistrationBiz.selectAddOrderDetl(orderDetlVo, subGoodsInfo.getOdOrderDetlParentId());
						}
					}
				}

				// 재고 증 / 차감 처리
				this.setRedeliveryStockOrderUpdate(requestDto);
			}
			// 재배송 등록자
			claimDetlVo.setEcId(urUserId);
		}
		return claimDetlVo;
	}

	/**
	 * CS환불 승인 처리
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	public ClaimDetlVo setOrderClaimCSRefundApprove(OrderClaimRegisterRequestDto reqDto, boolean isClaimSave) throws Exception {
		ClaimDetlVo claimDetlVo = new ClaimDetlVo();

		boolean refundPriceFlag = false;
		//환불 금액이 있을 때
		if (reqDto.getCsRefundPrice() != 0) {
			log.debug("=== CS 환불 환불금액이 있을때 5 !!!===");
			claimUtilRefundService.refundPrice(reqDto);
			refundPriceFlag = true;
		}

		//환불 적립금이 있을 때
		if (reqDto.getCsRefundPointPrice() != 0) {
			log.debug("=== CS 환불 환불적립금이 있을때 6 !!!===");
			claimUtilRefundService.refundPointPrice(reqDto);
			if(!refundPriceFlag) {
				reqDto.setPayTp(OrderEnums.PaymentType.FREE.getCode());
			}
		}

		//환불적립금 또는 환불금액이 있을 때 환불상태 값을 환불완료로 바꾼다.
		if (reqDto.getCsRefundPrice() != 0 || reqDto.getCsRefundPointPrice() != 0) {
			log.debug("=== CS 환불 환불상태를 환불완료로 업데이트 !!!===");
			List<OrderClaimDetlListInfoDto> claimDetlList	= claimProcessMapper.getClaimDetlList(reqDto.getOdClaimId());
			setClaimRefundPointPriceStatus(reqDto.getOdClaimId(), reqDto, OrderEnums.OrderStatus.REFUND_COMPLETE.getCode(), claimDetlList, isClaimSave);

			log.debug("=== CS 환불 환불 금액 또는 환불 포인트가 존재할 경우 환불 정보 payment 테이블 입력 ===");
			reqDto.setType(OrderEnums.PayType.F.getCode());
			this.putOrderPaymentInfo(reqDto);
		}

		log.debug("=== CS 환불 클레임상태를 업데이트 !!!===");
		setClaimStatus(reqDto, isClaimSave);
		return claimDetlVo;
	}

	/**
	 * CS환불 계좌 정보 등록
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	public ClaimDetlVo setOrderClaimCsRefundAccountInfo(OrderClaimRegisterRequestDto reqDto) throws Exception {
		ClaimDetlVo claimDetlVo = new ClaimDetlVo();
		if (StringUtil.isNotEmpty(reqDto.getBankCd())
				&& StringUtil.isNotEmpty(reqDto.getAccountHolder())
				&& StringUtil.isNotEmpty(reqDto.getAccountNumber())) {
			long odClaimAccountId = reqDto.getOdClaimAccountId() == 0 ? claimProcessMapper.getOdClaimAccountId() : reqDto.getOdClaimAccountId();

			ClaimAccountVo claimAccountVo = ClaimAccountVo.builder()
															.odClaimAccountId(odClaimAccountId)            //주문클레임 환불계좌 PK
															.odClaimId(reqDto.getOdClaimId())            //주문클레임 PK
															.bankCd(reqDto.getBankCd())                    //은행코드
															.accountHolder(reqDto.getAccountHolder())    //예금주
															.accountNumber(reqDto.getAccountNumber())    //계좌번호
															.build();

			log.debug("주문 클레임 환불 계좌 OD_CLAIM_ACCOUNT <{}>", claimAccountVo);
			if (reqDto.getOdClaimAccountId() == 0) {
				claimProcessMapper.addOrderClaimAccount(claimAccountVo);
			}
			else {
				claimProcessMapper.putOrderClaimAccount(claimAccountVo);
			}
		}
		return claimDetlVo;
	}

	/**
	 * CS환불 상태 변경 처리
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	public ClaimDetlVo setOrderClaimCSRefundApproveCd(OrderClaimRegisterRequestDto reqDto) throws Exception {
		ClaimDetlVo claimDetlVo = new ClaimDetlVo();

		ClaimVo claimVo = ClaimVo.builder()
									.odOrderId(reqDto.getOdOrderId())
									.odClaimId(reqDto.getOdClaimId())
									.csRefundApproveCd(reqDto.getCsRefundApproveCd())
									.build();
		claimProcessMapper.putOrderClaimCSRefundApproveCd(claimVo);

		return claimDetlVo;
	}

	/**
	 * 주문상세 송장번호 [OD_TRACKING_NUMBER] Insert or Update
	 * @param reqDto
	 * @return
	 */
	public void mergeTrackingNumber(OrderClaimRegisterRequestDto reqDto) {
		long userId = 0;
		if (CollectionUtils.isNotEmpty(reqDto.getGoodsInfoList())) {
			for (OrderClaimGoodsInfoDto goodsInfo : reqDto.getGoodsInfoList()) {
				OrderTrackingNumberVo orderTrackingNumberVo = OrderTrackingNumberVo.builder()
						.odOrderDetlId(goodsInfo.getOdOrderDetlId())			//주문상세 PK
						.psShippingCompId(reqDto.getPsShippingCompId())
						.trackingNo(reqDto.getTrackingNo())
						.createId(userId)
						.build();
				log.debug("[OD_TRACKING_NUMBER] 송장번호 갯수 :: <{}>, 배송관련 정보 <{}>", reqDto.getGoodsInfoList().size(), orderTrackingNumberVo);

				//송장번호를 먼저 업데이트 한다.
				int result = orderBulkTrackingNumberBiz.putOrderTrackingNumber(orderTrackingNumberVo);

				log.debug("송장 번호가 있을 때 업데이트 가능 숫자 result <{}>", result );
				//송장번호를 먼저 업데이트 갯수가 0이면 입력을 한다.
				if (result == 0) orderBulkTrackingNumberBiz.addOrderTrackingNumber(orderTrackingNumberVo);
			}
		}
	}

	/**
	 * 주문상세  [OD_ORDER_DETL] Update
	 * @param reqDto
	 * @return
	 */
	public void putOrderStatusCdChange(OrderClaimRegisterRequestDto reqDto) {

		String claimStatusCd = OrderEnums.OrderStatus.DELIVERY_ING.getCode();
		long urUserId = Long.parseLong(reqDto.getUrUserId());

		if (CollectionUtils.isNotEmpty(reqDto.getGoodsInfoList())) {
			for (OrderClaimGoodsInfoDto goodsInfo : reqDto.getGoodsInfoList()) {
				OrderDetlVo orderDetlVo = OrderDetlVo.builder()
						.orderStatusCd(claimStatusCd)
						.odOrderDetlId(goodsInfo.getOdOrderDetlId())
						.odOrderDetlSeq(goodsInfo.getOdOrderDetlSeq())
						.diId(Long.parseLong(reqDto.getUrUserId()))
						.build();
				claimProcessMapper.putOrderStatusCdChange(orderDetlVo);

				// 이력 내용 등록
				OrderDetlHistVo orderDetlHistVo = OrderDetlHistVo.builder()
						.odOrderDetlId(goodsInfo.getOdOrderDetlId())
						.statusCd(claimStatusCd)
						.histMsg(OrderEnums.OrderStatus.findByCode(claimStatusCd).getCodeName())
						.createId(urUserId)
						.build();

				orderStatusMapper.putOrderDetailStatusHist(orderDetlHistVo);
			}
		}
	}

	/**
	 *  주문 클레임 첨부파일 관리 [OD_CLAIM_ATTC] Insert or Update
	 * @param reqDto
	 * @param reqDto
	 * @return
	 */
	public void orderClaimAttc(OrderClaimRegisterRequestDto reqDto) {
		if (org.apache.commons.collections.CollectionUtils.isNotEmpty(reqDto.getAttcInfoList())) {

			for (OrderClaimAttcInfoDto attcDto : reqDto.getAttcInfoList()) {
				long odClaimAttcId = attcDto.getOdClaimAttcId() == 0 ? claimProcessMapper.getOdClaimAttcId() : attcDto.getOdClaimAttcId();

				ClaimAttcVo claimAttcVo = ClaimAttcVo.builder()
						.odClaimAttcId(odClaimAttcId)			//주문클레임 파일 PK
						.odClaimId(reqDto.getOdClaimId())		//주문클레임 PK
						.originNm(attcDto.getOriginNm())		//업로드 원본 파일명
						.uploadNm(attcDto.getUploadNm())		//업로드 파일명
						.uploadPath(attcDto.getUploadPath())	//업로드 경로
						.build();

				log.debug("주문 클레임 첨부파일 OD_CLAIM_ATTC 갯수만큼 Insert :: <{}> param <{}>" , reqDto.getGoodsInfoList().size(), claimAttcVo);
				if (attcDto.getOdClaimAttcId() == 0)
					claimProcessMapper.addOrderClaimAttc(claimAttcVo);
				else
					claimProcessMapper.putOrderClaimAttc(claimAttcVo);
			}
		}
	}

	/**
	 * 추가결제 비인증 결제 처리
	 * @param orderClaimCardPayRequestDto
	 * @return
	 */
	public OrderClaimCardPayResponseDto addCardPayOrderCreate(OrderClaimCardPayRequestDto orderClaimCardPayRequestDto) throws Exception {

		// 추가결제는 배송비에 대한 부분만 결제 처리 되므로 과세금액으로 설정
		// 신용카드 비인증 결제 정보 Set
		InicisNonAuthenticationCartPayRequestDto payReqDto = new InicisNonAuthenticationCartPayRequestDto();
		payReqDto.setOdid(orderClaimCardPayRequestDto.getOdid());					// 주문번호로만 Set, 클레임 PK 등등 해당 시점에 설정 가능한 값이 존재하지 않음 ..
		payReqDto.setGoodsName(orderClaimCardPayRequestDto.getGoodsNm());			// 상품명
		payReqDto.setPaymentPrice(orderClaimCardPayRequestDto.getOrderPrice());		// 결제금액
		payReqDto.setTaxPaymentPrice(orderClaimCardPayRequestDto.getOrderPrice());	// 과세결제금액
		payReqDto.setTaxFreePaymentPrice(0);										// 면세결제금액
		payReqDto.setBuyerName(orderClaimCardPayRequestDto.getBuyerNm());			// 주문자명
		payReqDto.setBuyerEmail(orderClaimCardPayRequestDto.getBuyerMail());		// 주문자이메일
		payReqDto.setBuyerMobile(orderClaimCardPayRequestDto.getBuyerHp());			// 주문자핸드폰
		payReqDto.setQuota(orderClaimCardPayRequestDto.getPlanPeriod());			// 할부기간
		payReqDto.setCardNumber(orderClaimCardPayRequestDto.getCardNo());			// 카드번호
		payReqDto.setCardExpire(orderClaimCardPayRequestDto.getCardNumYy() + orderClaimCardPayRequestDto.getCardNumMm());	// 카드유효기간
		payReqDto.setRegNo(orderClaimCardPayRequestDto.getAddInfoVal());			// 부가정보
		payReqDto.setCardPw(orderClaimCardPayRequestDto.getCardPass());				// 카드비밀번호

		log.debug("카드결제 payReqDto <{}>", payReqDto);

		InicisNonAuthenticationCartPayResponseDto payResDto = inicisPgService.nonAuthenticationCartPay(payReqDto);

		log.debug("카드결제 결과 payResDto <{}>", payResDto);

		OrderEnums.OrderRegistrationResult orderRegistrationResult = OrderEnums.OrderRegistrationResult.FAIL;
		long odAddPaymentReqInfoId = 0;

		if (payResDto.isSuccess()) {
			// 결제 마스터 PK 얻기
			long odPaymentMasterId = orderRegistrationBiz.getPaymentMasterSeq();

			OrderEnums.PaymentType paymentType = OrderEnums.PaymentType.CARD;
			OrderClaimPaymentMasterDto orderPaymentMasterVo = new OrderClaimPaymentMasterDto();
			orderPaymentMasterVo.setOdPaymentMasterId(odPaymentMasterId);						//주문결제마스터PK
			orderPaymentMasterVo.setType(OrderEnums.PayType.A.getCode());						//결제타입 (G : 결제, F : 환불 , A : 추가)
			orderPaymentMasterVo.setStatus(OrderEnums.OrderStatus.INCOM_COMPLETE.getCode());	//결제상태(IR:입금예정,IC:입금완료)
			orderPaymentMasterVo.setPayTp(paymentType.getCode());								//결제방법 공통코드(PAY_TP)
			orderPaymentMasterVo.setPgService(PgEnums.PgAccountType.INICIS_ADMIN.getCode()); 	//PG 종류 공통코드(PG_SERVICE)
			orderPaymentMasterVo.setTid(payResDto.getTid()); 									//거래 ID
			orderPaymentMasterVo.setAuthCd(payResDto.getPayAuthCode()); 						//승인코드
			orderPaymentMasterVo.setInfo(InicisCardCode.findByCode(payResDto.getCardCode()).getCodeName()); //결제정보
			orderPaymentMasterVo.setCardNumber(MaskingUtil.cardNumber(orderClaimCardPayRequestDto.getCardNo()));	//카드번호
			orderPaymentMasterVo.setCardQuota(payResDto.getPayAuthQuota());						//할부기간
			orderPaymentMasterVo.setApprovalDt(payResDto.getPayDate() + "" + payResDto.getPayTime());	//승인일시
			orderPaymentMasterVo.setEscrowYn(OrderClaimEnums.AllTypeYn.ALL_TYPE_N.getCode());	//에스크로 결제 여부
			orderPaymentMasterVo.setPartCancelYn(StringUtil.nvl(OrderClaimEnums.PartCancelYn.findByCode(payResDto.getPrtcCode()).getCodeValue(), OrderClaimEnums.AllTypeYn.ALL_TYPE_Y.getCode()));	//부분취소가능여부
			orderPaymentMasterVo.setShippingPrice(orderClaimCardPayRequestDto.getOrderPrice());	// 배송비
			orderPaymentMasterVo.setTaxablePrice(orderClaimCardPayRequestDto.getOrderPrice());	// 과세금액
			orderPaymentMasterVo.setPaymentPrice(orderClaimCardPayRequestDto.getOrderPrice());	// 결제금액
			orderPaymentMasterVo.setResponseData(objectMapper.writeValueAsString(payResDto));

			String reqJson = new Gson().toJson(orderPaymentMasterVo);
			OdAddPaymentReqInfo odAddPaymentReqInfo = OdAddPaymentReqInfo.builder()
																		.reqJsonInfo(reqJson)
																		.build();
			claimProcessMapper.addOdAddPaymentReqInfo(odAddPaymentReqInfo);
			log.debug("추가결제 요청 정보 PK :: <{}>", odAddPaymentReqInfo.getOdAddPaymentReqInfoId());
			odAddPaymentReqInfoId = odAddPaymentReqInfo.getOdAddPaymentReqInfoId();
			orderRegistrationResult = OrderEnums.OrderRegistrationResult.SUCCESS;
		}

		return OrderClaimCardPayResponseDto.builder()
											.orderRegistrationResult(orderRegistrationResult)
											.message(payResDto.getMessage())
											.inicisNonAuthenticationCartPay(payResDto)
											.virtualAccountData(null)
											.odAddPaymentReqInfoId(odAddPaymentReqInfoId)
											.build();
	}

	/**
	 * 추가결제 가상계좌 채번
	 * @param orderClaimCardPayRequestDto
	 * @return
	 */
	public OrderClaimCardPayResponseDto addBankBookOrderCreate(OrderClaimCardPayRequestDto orderClaimCardPayRequestDto) throws Exception {

		OrderEnums.PaymentType paymentType = OrderEnums.PaymentType.VIRTUAL_BANK; //결제정보PK
		log.debug("무통장 paymentType <{}>", paymentType);

		PgAbstractService<?, ?> pgService = pgBiz.getService(paymentType, "");
		log.debug("무통장 pgService <{}>", pgService);
		String pgBankCode = pgBiz.getPgBankCode(pgService.getServiceType().getCode(), paymentType.getCode(), "BANK_CODE.NONGHYUP");

		// 결제 마스터 PK 얻기
		long odPaymentMasterId = orderRegistrationBiz.getPaymentMasterSeq();

		// 추가결제는 배송비에 대한 부분만 결제 처리 되므로 과세금액으로 설정
		// 가상계좌 정보 Set
		BasicDataRequestDto basicDataRequestDto = new BasicDataRequestDto();
		basicDataRequestDto.setOdid(orderClaimCardPayRequestDto.getOdid() + Constants.ORDER_ODID_DIV_ADD_PAYMENT_MASTER + odPaymentMasterId);
		basicDataRequestDto.setPaymentType(paymentType);
		basicDataRequestDto.setPgBankCode(pgBankCode);
		basicDataRequestDto.setGoodsName(orderClaimCardPayRequestDto.getGoodsNm());
		basicDataRequestDto.setPaymentPrice(orderClaimCardPayRequestDto.getOrderPrice());
		basicDataRequestDto.setTaxPaymentPrice(orderClaimCardPayRequestDto.getOrderPrice());
		basicDataRequestDto.setTaxFreePaymentPrice(0);
		basicDataRequestDto.setBuyerName(orderClaimCardPayRequestDto.getBuyerNm());
		basicDataRequestDto.setBuyerEmail(orderClaimCardPayRequestDto.getBuyerMail());
		basicDataRequestDto.setBuyerMobile(orderClaimCardPayRequestDto.getBuyerHp());
		basicDataRequestDto.setLoginId(orderClaimCardPayRequestDto.getLoginId());
		basicDataRequestDto.setVirtualAccountDateTime(pgBiz.getVirtualAccountDateTime());

		log.debug("무통장 basicDataRequestDto <{}>", basicDataRequestDto);

		VirtualAccountDataResponseDto responseDto = pgService.getVirtualAccountData(basicDataRequestDto);

		log.debug("가상계좌 채번 결과 responseDto ::: <{}>", responseDto);

		OrderEnums.OrderRegistrationResult orderRegistrationResult = OrderEnums.OrderRegistrationResult.FAIL;
		long odAddPaymentReqInfoId = 0;

		if (responseDto.isSuccess()) {

			String paidDueDt = responseDto.getValidDate();
			paidDueDt = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "235959";

			OrderClaimPaymentMasterDto orderPaymentMasterVo = new OrderClaimPaymentMasterDto();
			orderPaymentMasterVo.setOdPaymentMasterId(odPaymentMasterId);						//주문결제마스터 PK
			orderPaymentMasterVo.setType(OrderEnums.PayType.A.getCode());						//결제타입 (G : 결제, F : 환불 , A : 추가)
			orderPaymentMasterVo.setStatus(OrderEnums.OrderStatus.INCOM_READY.getCode());		//결제상태(IR:입금예정,IC:입금완료)
			orderPaymentMasterVo.setPayTp(paymentType.getCode());								//결제방법 공통코드(PAY_TP)
			orderPaymentMasterVo.setPgService(pgService.getServiceType().getCode()); 			//PG 종류 공통코드(PG_SERVICE)
			orderPaymentMasterVo.setTid(responseDto.getTid()); 									//거래 ID
			orderPaymentMasterVo.setAuthCd(""); 												//승인코드
			orderPaymentMasterVo.setVirtualAccountNumber(responseDto.getAccount()); 			//가상계좌번호
			orderPaymentMasterVo.setInfo(MaskingUtil.accountNumber(responseDto.getAccount())); 	//결제정보
			orderPaymentMasterVo.setBankNm(responseDto.getBankName()); 							//입금은행명
			orderPaymentMasterVo.setPaidHolder(responseDto.getDepositor()); 					//입금자명
			orderPaymentMasterVo.setApprovalDt(responseDto.getValidDate());	//승인일시
			orderPaymentMasterVo.setEscrowYn(OrderClaimEnums.AllTypeYn.ALL_TYPE_N.getCode());	//에스크로 결제 여부
			orderPaymentMasterVo.setShippingPrice(orderClaimCardPayRequestDto.getOrderPrice());	// 배송비
			orderPaymentMasterVo.setTaxablePrice(orderClaimCardPayRequestDto.getOrderPrice());	// 과세금액
			orderPaymentMasterVo.setPaymentPrice(orderClaimCardPayRequestDto.getOrderPrice());	// 결제금액
			orderPaymentMasterVo.setResponseData(objectMapper.writeValueAsString(responseDto));	//응담메세지
			orderPaymentMasterVo.setPaidDueDt(paidDueDt);
			String reqJson = objectMapper.writeValueAsString(orderPaymentMasterVo);
			OdAddPaymentReqInfo odAddPaymentReqInfo = OdAddPaymentReqInfo.builder()
																			.reqJsonInfo(reqJson)
																			.build();
			claimProcessMapper.addOdAddPaymentReqInfo(odAddPaymentReqInfo);
			log.debug("추가결제 요청 정보 PK :: <{}>", odAddPaymentReqInfo.getOdAddPaymentReqInfoId());
			odAddPaymentReqInfoId = odAddPaymentReqInfo.getOdAddPaymentReqInfoId();
			orderRegistrationResult = OrderEnums.OrderRegistrationResult.SUCCESS;
		}

		return OrderClaimCardPayResponseDto.builder()
											.orderRegistrationResult(orderRegistrationResult)
											.message(responseDto.getMessage())
											.inicisNonAuthenticationCartPay(null)
											.virtualAccountData(responseDto)
											.odAddPaymentReqInfoId(odAddPaymentReqInfoId)
											.build();
	}
}
