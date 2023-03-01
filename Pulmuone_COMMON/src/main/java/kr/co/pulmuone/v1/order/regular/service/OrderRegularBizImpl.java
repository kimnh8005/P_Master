package kr.co.pulmuone.v1.order.regular.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.enums.OrderEnums.*;
import kr.co.pulmuone.v1.comm.enums.ShoppingEnums;
import kr.co.pulmuone.v1.comm.enums.StoreEnums;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.comm.util.PriceUtil;
import kr.co.pulmuone.v1.goods.goods.dto.RegularShippingConfigDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ShippingDataResultVo;
import kr.co.pulmuone.v1.goods.goods.service.GoodsShippingTemplateBiz;
import kr.co.pulmuone.v1.order.email.dto.OrderInfoForEmailResultDto;
import kr.co.pulmuone.v1.order.email.service.OrderEmailBiz;
import kr.co.pulmuone.v1.order.email.service.OrderEmailSendBiz;
import kr.co.pulmuone.v1.order.regular.dto.*;
import kr.co.pulmuone.v1.order.regular.dto.vo.*;
import kr.co.pulmuone.v1.policy.config.service.PolicyConfigBiz;
import kr.co.pulmuone.v1.shopping.cart.dto.*;
import kr.co.pulmuone.v1.shopping.cart.dto.vo.ShippingTemplateGroupByVo;
import kr.co.pulmuone.v1.shopping.cart.dto.vo.SpCartVo;
import kr.co.pulmuone.v1.shopping.cart.service.ShoppingCartBiz;
import kr.co.pulmuone.v1.user.certification.dto.GetSessionShippingResponseDto;
import kr.co.pulmuone.v1.user.group.service.UserGroupBiz;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class OrderRegularBizImpl implements OrderRegularBiz {

	@Autowired
	private OrderRegularDetailBiz orderRegularDetailBiz;

	@Autowired
	private OrderRegularService orderRegularService;

	@Autowired
	private OrderRegularDetailService orderRegularDetailService;

	@Autowired
	private OrderRegularRegistrationService orderRegularRegistrationService;

	@Autowired
	private UserGroupBiz userGroupBiz;

	@Autowired
	private ShoppingCartBiz shoppingCartBiz;

	@Autowired
	private PolicyConfigBiz policyConfigBiz;

	@Autowired
	private OrderEmailBiz orderEmailBiz;

	@Autowired
	private OrderEmailSendBiz orderEmailSendBiz;

	@Autowired
	private GoodsShippingTemplateBiz goodsShippingTemplateBiz;



	/**
	 * 장바구니에서 정기배송 정보 조회
	 */
	@Override
	public CartRegularShippingDto getRegularInfoByCart(Long urUserId) throws Exception {
		CartRegularShippingDto resDto = new CartRegularShippingDto();

		OrderRegularInfoVo regularInfo = orderRegularService.getActiveRegularInfo(urUserId);

		resDto.setCycle(orderRegularService.getRegularShippingCycleList());
		resDto.setCycleTerm(orderRegularService.getRegularShippingCycleTermList());

		if (regularInfo != null) {
			resDto.setAdditionalOrder(true);
			resDto.setOdRegularReqId(regularInfo.getOdRegularReqId());
			resDto.setFirstArrivalScheduledDate(regularInfo.getFirstArrivalScheduledDate());
			resDto.setCycleType(regularInfo.getCycleType());
			resDto.setCycleType(regularInfo.getCycleType());
			resDto.setCycleTermType(regularInfo.getCycleTermType());
			resDto.setStartDate(regularInfo.getStartDate());
			resDto.setEndDate(regularInfo.getEndDate());
			resDto.setNextArrivalScheduledDate(regularInfo.getNextArrivalScheduledDate());
		} else {
			resDto.setAdditionalOrder(false);
		}

		return resDto;
	}

	/**
	 * 장바구니용 정기배송 기존 상품 리스트
	 */
	@Override
	public List<SpCartVo> getGoodsListByShippingPolicy(ShippingTemplateGroupByVo shippingTemplateData, Long urUserId)
			throws Exception {
		return orderRegularService.getGoodsListByShippingPolicy(shippingTemplateData, urUserId);
	}

	/**
	 * 사용중인 정기배송 배송지 조회
	 */
	@Override
	public GetSessionShippingResponseDto getRegularShippingZone(Long odRegularReqId) throws Exception {
		return orderRegularService.getRegularShippingZone(odRegularReqId);
	}

	/**
	 * 정기배송 KEY 저장
	 */
	@Override
	public int addRegularPaymentKey(OrderRegularPaymentKeyVo orderRegularPaymentKeyVo) throws Exception {

		orderRegularService.putNoPaymentRegularPaymentKey(orderRegularPaymentKeyVo.getUrUserId(), "사용자 새로운 정기배송 발급");

		return orderRegularService.addRegularPaymentKey(orderRegularPaymentKeyVo);
	}

	/**
	 * 정기결제 카드정보 조회
	 */
	@Override
	public RegularPaymentKeyVo getRegularPaymentKey(Long urUserId) throws Exception {
		return orderRegularService.getRegularPaymentKey(urUserId);
	}

	/**
	 * 정기배송 상품 신청
	 */
	@Override
	public ApplyRegularResponseDto applyRegular(CreateOrderCartDto createOrderCartDto) throws Exception {
		ApplyRegularResponseDto resDto = null;
		OrderRegularInfoVo regularInfo = orderRegularService.getActiveRegularInfo(createOrderCartDto.getBuyer().getUrUserId());
		// 정기배송 첫주문여부
		String firstOrderYn = "Y";

		if (regularInfo != null) {
			resDto = addRegularGoods(regularInfo, createOrderCartDto);
			firstOrderYn = "N";
		} else {
			resDto = createRegular(createOrderCartDto);
		}
		createRegularResult(resDto.getOdRegularReqId(), createOrderCartDto.getBuyer().getUrUserId(), resDto.getRegularGoodsList(), firstOrderYn);

		// 정기배송 신청완료 자동메일 발송
		try {
			OrderInfoForEmailResultDto orderInfoForEmailResultDto = orderEmailBiz.getOrderRegularApplyCompletedInfoForEmail(resDto.getOdRegularReqId(), firstOrderYn);

			orderEmailSendBiz.orderRegularApplyCompleted(orderInfoForEmailResultDto, firstOrderYn);
		} catch (Exception e) {
			log.error("ERROR ====== 정기배송 신청완료 자동메일 발송 오류 urUserId : {}" ,createOrderCartDto.getBuyer().getUrUserId());
			log.error("ERROR ====== 정기배송 신청완료 자동메일 발송 오류 odRegularReqId :: {}" ,resDto.getOdRegularReqId());
			log.error(e.getMessage());
		}

		return resDto;
	}

	/**
	 * 정기배송 결과 등록
	 * @param odRegularReqId
	 * @param urUserId
	 * @param regularGoodsList
	 * @param firstOrderYn
	 * @throws Exception
	 */
	private void createRegularResult(long odRegularReqId, long urUserId, List<CartGoodsDto> regularGoodsList, String firstOrderYn) throws Exception {

		String regularStatusCd = RegularStatusCd.APPLY.getCode();
		String regularDetlStatusCd = RegularDetlStatusCd.APPLY.getCode();
		StringBuilder regularReqCont = new StringBuilder();

		// 1. 정기배송주문 신청 정보 조회
		RegularReqListDto regularReqInfo = orderRegularDetailService.getOrderRegularReqInfo(odRegularReqId);

		// 2. 정기배송주문 신청 상세 조회 - 정기배송주문 상세 상태가 신청인 건
		List<RegularResultReqRoundGoodsListDto> regularReqDetailGoodsList = orderRegularDetailService.getOrderRegularReqDetailGoodsList(odRegularReqId, regularDetlStatusCd);

		// 추가 주문일 경우
		if("N".equals(firstOrderYn)) {

			// 상품 추가의 경우 주문생성예정일이 오늘보다 4일 뒤인 건 그 후로 회차정보에 포함 됨
			LocalDate stdDate = LocalDate.now().plusDays(4);

			// 동일상품 주문 수량 업데이트 처리
			orderRegularService.putOrderRegularResultDetailGoodsInfo(odRegularReqId, stdDate);

			// 정기배송 결과 상세 테이블 내 존재하지 않는 상품 정보 목록 얻기
			List<RegularResultDetailGoodsListDto> regularResultDetailGoodsList = orderRegularDetailService.getOrderRegularResultDetailGoodsList(odRegularReqId, regularDetlStatusCd);

			// 신규 등록 상품이 존재할 경우 처리
			if(CollectionUtils.isNotEmpty(regularResultDetailGoodsList)) {
				// 신규 상품 등록 처리
				List<RegularResultListDto> orderRegularResultIdList = orderRegularDetailService.getOrderRegularResultIdList(odRegularReqId, stdDate, regularStatusCd);

				// 정기배송주문신청결과정보PK 목록 수 만큼 LOOP
				for(RegularResultListDto regularResultItem : orderRegularResultIdList) {

					// 정기배송 결과 상세 상품 목록 수 만큼 LOOP
					for(RegularResultDetailGoodsListDto regularResultDetailGoodsItem : regularResultDetailGoodsList) {

						// 정기배송주문신청결과 상세 ID 얻기
						long odRegularResultDetlId = orderRegularRegistrationService.getOdRegularResultDetlIdSeq();
						OrderRegularResultDetlVo orderRegularResultDetlVo = new OrderRegularResultDetlVo();
						orderRegularResultDetlVo.setOdRegularResultDetlId(odRegularResultDetlId);					// 정기배송주문상세PK
						orderRegularResultDetlVo.setOdRegularResultId(regularResultItem.getOdRegularResultId());	// 정기배송주문PK
						orderRegularResultDetlVo.setIlItemCd(regularResultDetailGoodsItem.getIlItemCd());			// 상품품목PK
						orderRegularResultDetlVo.setIlGoodsId(regularResultDetailGoodsItem.getIlGoodsId());			// 상품PK
						orderRegularResultDetlVo.setGoodsNm(regularResultDetailGoodsItem.getGoodsNm());				// 상품명
						orderRegularResultDetlVo.setOrderCnt(regularResultDetailGoodsItem.getOrderCnt());			// 주문수량
						orderRegularResultDetlVo.setReqDetailStatusCd(regularDetlStatusCd);							// 상세상태
						orderRegularResultDetlVo.setSaleStatus(OrderEnums.RegularDetlSaleStatusCd.ON_SALE.getCode());// 판매상태

						orderRegularRegistrationService.addOrderRegularResultDetl(orderRegularResultDetlVo);
					}
				}
			}

			if(CollectionUtils.isNotEmpty(regularGoodsList)) {
				regularReqCont.append(regularGoodsList.get(0).getGoodsName());
				// 상품 수가 2개 이상일 경우
				if(regularGoodsList.size() > 1) {
					regularReqCont.append(" 외 " + (regularGoodsList.size() - 1) + "건 수량 변경");
				}
			}

			// 정기배송주문 히스토리 등록 값 세팅
			long odRegularReqHistoryId = orderRegularRegistrationService.getOdRegularReqHistoryIdSeq();
			OrderRegularReqHistoryVo orderRegularReqHistoryVo = new OrderRegularReqHistoryVo();
			orderRegularReqHistoryVo.setOdRegularReqHistoryId(odRegularReqHistoryId);					// 정기배송주문히스토리 PK
			orderRegularReqHistoryVo.setOdRegularReqId(odRegularReqId);									// 정기배송주문신청 PK
			orderRegularReqHistoryVo.setRegularReqGbnCd(OrderEnums.RegularReqGbnCd.GA.getCode());		// 변경구분값 공통코드(REGULAR_REQ_GBN_CD)
			orderRegularReqHistoryVo.setRegularReqStatusCd(OrderEnums.RegularReqStatusCd.PC.getCode());	// 처리상태 공통코드(REGULAR_REQ_STATUS_CD)
			orderRegularReqHistoryVo.setRegularReqCont(regularReqCont.toString());						// 상세내용
			orderRegularReqHistoryVo.setCreateId(urUserId);												// 등록자 UR_USER.UR_USER_ID

			// 5. 정기배송주문 히스토리 등록 처리
			orderRegularRegistrationService.addOrderRegularReqHistory(orderRegularReqHistoryVo);
		}
		else {

			// 3. 정기배송 주문신청 내 배송기간, 배송주기로 횟수 계산
			// 첫배송도착예정일자
			LocalDate deliveryDt = regularReqInfo.getDeliveryDt();

			List<Long> urWarehouseIds = new ArrayList<>();
			if(CollectionUtils.isNotEmpty(regularGoodsList)) {
				urWarehouseIds = regularGoodsList.stream().map(x -> x.getUrWareHouseId()).distinct().collect(toList());
			}

			// 배송 종료 일자 LocalDate 생성
			List<LocalDate> arriveDtList = orderRegularService.getRegularArrivalScheduledDateList(deliveryDt,
																									RegularShippingCycle.findByCode(regularReqInfo.getGoodsCycleTp()),
																									RegularShippingCycleTerm.findByCode(regularReqInfo.getGoodsCycleTermTp()),
																									urWarehouseIds);
			// 회차
			int reqRound = 0;

			// 계산된 회차 만큼 결과 생성
			for(LocalDate arriveDt : arriveDtList) {

				// 주문 생성 관련 일자 정보는 도착예정일 기준 -3일
				LocalDate orderCreateDt = arriveDt.minusDays(3);
				// 결제 예정 일자 정보는 도착예정일 기준 -2일
				LocalDate paymentDt = arriveDt.minusDays(2);

				// 정기배송주문신청결과 ID 얻기
				long odRegularResultId = orderRegularRegistrationService.getOdRegularResultIdSeq();
				OrderRegularResultVo orderRegularResultVo = new OrderRegularResultVo();
				orderRegularResultVo.setOdRegularResultId(odRegularResultId);				// 정기배송주문경과PK
				orderRegularResultVo.setOdRegularReqId(odRegularReqId);						// 정기배송주문신청PK
				orderRegularResultVo.setOdOrderId(0);										// 주문PK
				orderRegularResultVo.setReqRound(++reqRound);								// 요청회차
				orderRegularResultVo.setPaymentFailCnt(0);									// 결제실패건수
				orderRegularResultVo.setOrderCreateDt(orderCreateDt.toString());			// 주문생성예정일
				orderRegularResultVo.setPaymentDt(paymentDt.toString());					// 결제예정일
				orderRegularResultVo.setArriveDt(arriveDt.toString());						// 도착예정일
				orderRegularResultVo.setOrderCreateYn("N");									// 주문생성여부
				orderRegularResultVo.setRegularStatusCd(regularStatusCd);					// 신청상태
				orderRegularResultVo.setReqRoundYn("N");									// 회차완료여부

				// 정기배송 주문 신청 결과 정보 Insert
				orderRegularRegistrationService.addOrderRegularResult(orderRegularResultVo);

				// 정기배송 신청 상세 상품 목록 수 만큼 LOOP
				for(RegularResultReqRoundGoodsListDto regularReqDetailGoodsItem : regularReqDetailGoodsList) {

					// 정기배송주문신청결과 상세 ID 얻기
					long odRegularResultDetlId = orderRegularRegistrationService.getOdRegularResultDetlIdSeq();
					OrderRegularResultDetlVo orderRegularResultDetlVo = new OrderRegularResultDetlVo();
					orderRegularResultDetlVo.setOdRegularResultDetlId(odRegularResultDetlId);			// 정기배송주문결과상세PK
					orderRegularResultDetlVo.setOdRegularResultId(odRegularResultId);					// 정기배송주문결과PK
					orderRegularResultDetlVo.setIlItemCd(regularReqDetailGoodsItem.getIlItemCd());		// 상품품목PK
					orderRegularResultDetlVo.setIlGoodsId(regularReqDetailGoodsItem.getIlGoodsId());	// 상품PK
					orderRegularResultDetlVo.setGoodsNm(regularReqDetailGoodsItem.getGoodsNm());		// 상품PK
					orderRegularResultDetlVo.setOrderCnt(regularReqDetailGoodsItem.getOrderCnt());		// 주문수량
					orderRegularResultDetlVo.setReqDetailStatusCd(regularDetlStatusCd);					// 상세상태
					orderRegularResultDetlVo.setSaleStatus(OrderEnums.RegularDetlSaleStatusCd.ON_SALE.getCode());// 판매상태

					// 정기배송 주문 신청 결과 상세 정보 Insert
					orderRegularRegistrationService.addOrderRegularResultDetl(orderRegularResultDetlVo);
				}
			}

			// 정기배송주문신청 업데이트 값 세팅
			OrderRegularReqVo orderRegularReqVo = new OrderRegularReqVo();
			orderRegularReqVo.setOdRegularReqId(odRegularReqId);	// 정기배송주문신청 PK
			orderRegularReqVo.setCreateRoundYn("Y");				// 회차정보 생성여부

			// 4. 정기배송주문신청 업데이트 처리
			orderRegularDetailService.putOrderRegularReq(orderRegularReqVo, "N", "N");
		}
	}

	private ApplyRegularResponseDto createRegular(CreateOrderCartDto createOrderCartDto) throws Exception {
		ApplyRegularResponseDto resDto = new ApplyRegularResponseDto();

		Long odRegularReqId = orderRegularRegistrationService.getOdRegularReqIdSeq();
		String reqId = orderRegularRegistrationService.getOdRegularReqIdSeq(odRegularReqId);

		CartBuyerDto buyerDto = createOrderCartDto.getBuyer();
		CartRegularDto regularDto = createOrderCartDto.getRegular();
		GetSessionShippingResponseDto shippingZoneDto = createOrderCartDto.getShippingZone();

		List<CartDeliveryDto> cartList = createOrderCartDto.getCartList();
		List<CartShippingDto> cartShippingDtoList = shoppingCartBiz.getCartShippingList(cartList);
		List<CartGoodsDto> cartGoodsList = shoppingCartBiz.getCartGoodsList(cartShippingDtoList);
		List<CartGoodsDto> regularGoodsList = new ArrayList<>();

		String urGroupNm = "비회원";
		if (buyerDto.getUrUserId() > 0) {
			urGroupNm = userGroupBiz.getGroupByUser(buyerDto.getUrUserId()).getGroupName();
		}

		RegularShippingCycle goodsCycleType = OrderEnums.RegularShippingCycle.findByCode(regularDto.getCycleType());
		RegularShippingCycleTerm goodsCycleTermType = OrderEnums.RegularShippingCycleTerm.findByCode(regularDto.getCycleTermType());

		RegularShippingConfigDto regularShippingConfigDto = policyConfigBiz.getRegularShippingConfig();
		// 정기배송 기본 할인 정보
		int basicDiscountRate = regularShippingConfigDto.getRegularShippingBasicDiscountRate();
		// 정기배송 추가 할인 회차 정보
		int addDiscountRound = regularShippingConfigDto.getRegularShippingAdditionalDiscountApplicationTimes();
		// 정기배송 추가 할인 정보
		int addDiscountRate = regularShippingConfigDto.getRegularShippingAdditionalDiscountRate();
		// 상품 출고처 목록
		List<Long> urWarehouseIds = cartGoodsList.stream().filter(x -> "N".equals(x.getIngRegularGoodsYn())).map(x -> x.getUrWareHouseId()).distinct().collect(toList());

		// 신청
		OrderRegularReqVo orderRegularReqVo = new OrderRegularReqVo();
		orderRegularReqVo.setOdRegularReqId(odRegularReqId);
		orderRegularReqVo.setReqId(reqId);
		orderRegularReqVo.setUrGroupId(String.valueOf(buyerDto.getUrGroupId()));
		orderRegularReqVo.setUrGroupNm(urGroupNm);
		orderRegularReqVo.setUrUserId(buyerDto.getUrUserId());
		orderRegularReqVo.setBuyerNm(buyerDto.getBuyerName());
		orderRegularReqVo.setBuyerHp(buyerDto.getBuyerMobile());
		orderRegularReqVo.setBuyerMail(buyerDto.getBuyerEmail());
		orderRegularReqVo.setPaymentTypeCd(OrderEnums.PaymentType.CARD.getCode());
		orderRegularReqVo.setBuyerTypeCd(buyerDto.getBuyerType().getCode());
		orderRegularReqVo.setAgentTypeCd(buyerDto.getAgentType().getCode());
		List<LocalDate> regularArrivalScheduledDateList = orderRegularService.getRegularArrivalScheduledDateList(regularDto.getArrivalScheduledDate(), goodsCycleType, goodsCycleTermType, urWarehouseIds);
		orderRegularReqVo.setTotCnt(regularArrivalScheduledDateList.size());
		orderRegularReqVo.setFirstGoodsCycleTermTp(goodsCycleTermType.getCode());
		orderRegularReqVo.setFirstGoodsCycleTp(goodsCycleType.getCode());
		orderRegularReqVo.setFirstWeekCd(StoreEnums.WeekCode.findByWeekValue(regularDto.getArrivalScheduledDate().getDayOfWeek().getValue()).getCode());
		orderRegularReqVo.setGoodsCycleTermTp(goodsCycleTermType.getCode());
		orderRegularReqVo.setGoodsCycleTp(goodsCycleType.getCode());
		orderRegularReqVo.setWeekCd(StoreEnums.WeekCode.findByWeekValue(regularDto.getArrivalScheduledDate().getDayOfWeek().getValue()).getCode());
		orderRegularReqVo.setUrPcidCd(buyerDto.getUrPcidCd());
		orderRegularReqVo.setDeliveryDt(regularDto.getArrivalScheduledDate());
		orderRegularReqVo.setRegularStatusCd(OrderEnums.RegularStatus.APPLY.getCode());
		orderRegularReqVo.setCreateRoundYn("N");
		orderRegularReqVo.setBasicDiscountRate(basicDiscountRate);
		orderRegularReqVo.setAddDiscountRound(addDiscountRound);
		orderRegularReqVo.setAddDiscountRate(addDiscountRate);
		orderRegularRegistrationService.addOrderRegularReq(orderRegularReqVo);

		// 상세
		for (CartGoodsDto cartGoods : cartGoodsList) {
			if ("N".equals(cartGoods.getIngRegularGoodsYn())) {
				OrderRegularReqOrderDetlVo orderRegularReqOrderDetlVo = new OrderRegularReqOrderDetlVo();
				orderRegularReqOrderDetlVo.setOdRegularReqOrderDetlId(orderRegularRegistrationService.getOdRegularReqOrderDetlIdSeq());
				orderRegularReqOrderDetlVo.setOdRegularReqId(odRegularReqId);
				orderRegularReqOrderDetlVo.setIlGoodsId(cartGoods.getIlGoodsId());
				orderRegularReqOrderDetlVo.setIlItemCd(cartGoods.getIlItemCd());
				orderRegularReqOrderDetlVo.setOrderCnt(cartGoods.getQty());
				orderRegularReqOrderDetlVo.setSalePrice(cartGoods.getSalePrice());
				orderRegularReqOrderDetlVo.setReqDetailStatusCd(OrderEnums.RegularDetailStatus.APPLY.getCode());
				orderRegularRegistrationService.addOrderRegularReqOrderDetl(orderRegularReqOrderDetlVo);
				regularGoodsList.add(cartGoods);
			}
		}

		//배송지
		OrderRegularReqShippingZoneVo orderRegularReqShippingZoneVo = new OrderRegularReqShippingZoneVo();
		orderRegularReqShippingZoneVo.setOdRegularReqShippingZoneId(orderRegularRegistrationService.getOdRegularReqShippingZoneIdSeq());
		orderRegularReqShippingZoneVo.setOdRegularReqId(odRegularReqId);
		orderRegularReqShippingZoneVo.setDeliveryType(ShoppingEnums.DeliveryType.REGULAR.getCode());
		orderRegularReqShippingZoneVo.setRecvNm(shippingZoneDto.getReceiverName());
		orderRegularReqShippingZoneVo.setRecvHp(shippingZoneDto.getReceiverMobile());
		orderRegularReqShippingZoneVo.setRecvZipCd(shippingZoneDto.getReceiverZipCode());
		orderRegularReqShippingZoneVo.setRecvAddr1(shippingZoneDto.getReceiverAddress1());
		orderRegularReqShippingZoneVo.setRecvAddr2(shippingZoneDto.getReceiverAddress2());
		orderRegularReqShippingZoneVo.setRecvBldNo(shippingZoneDto.getBuildingCode());
		orderRegularReqShippingZoneVo.setDeliveryMsg(shippingZoneDto.getShippingComment());
		orderRegularReqShippingZoneVo.setDoorMsgCd(shippingZoneDto.getAccessInformationType());
		orderRegularReqShippingZoneVo.setDoorMsg(shippingZoneDto.getAccessInformationPassword());
		orderRegularRegistrationService.addOrderRegularReqShippingZone(orderRegularReqShippingZoneVo);

		//히스토리 등록
		OrderRegularReqHistoryVo orderRegularReqHistoryVo = new OrderRegularReqHistoryVo();
		orderRegularReqHistoryVo.setOdRegularReqHistoryId(orderRegularRegistrationService.getOdRegularReqHistoryIdSeq());
		orderRegularReqHistoryVo.setOdRegularReqId(odRegularReqId);
		orderRegularReqHistoryVo.setRegularReqGbnCd(RegularReqGbnCd.FR.getCode());
		orderRegularReqHistoryVo.setRegularReqStatusCd(RegularReqStatusCd.PC.getCode());
		orderRegularReqHistoryVo.setRegularReqCont("고객 신청");
		orderRegularRegistrationService.addOrderRegularReqHistory(orderRegularReqHistoryVo);

		resDto.setOdRegularReqId(odRegularReqId);
		resDto.setReqId(reqId);
		resDto.setResult(true);
		resDto.setRegularGoodsList(regularGoodsList);
		return resDto;
	}

	private ApplyRegularResponseDto addRegularGoods(OrderRegularInfoVo regularInfo, CreateOrderCartDto createOrderCartDto) throws Exception {
		ApplyRegularResponseDto resDto = new ApplyRegularResponseDto();
		List<CartDeliveryDto> cartList = createOrderCartDto.getCartList();
		List<CartShippingDto> cartShippingDtoList = shoppingCartBiz.getCartShippingList(cartList);
		List<CartGoodsDto> cartGoodsList = shoppingCartBiz.getCartGoodsList(cartShippingDtoList);
		List<CartGoodsDto> regularGoodsList = new ArrayList<>();
		for (CartGoodsDto cartGoods : cartGoodsList) {
			if ("N".equals(cartGoods.getIngRegularGoodsYn())) {
				OrderRegularReqOrderDetlVo orderRegularReqOrderDetlVo = orderRegularRegistrationService
						.getOverlapOrderRegularReqOrderDetl(regularInfo.getOdRegularReqId(), cartGoods.getIlGoodsId(), OrderEnums.RegularDetlStatusCd.APPLY.getCode());

				regularGoodsList.add(cartGoods);
				if (orderRegularReqOrderDetlVo == null) {
					orderRegularReqOrderDetlVo = new OrderRegularReqOrderDetlVo();
					orderRegularReqOrderDetlVo.setOdRegularReqOrderDetlId(orderRegularRegistrationService.getOdRegularReqOrderDetlIdSeq());
					orderRegularReqOrderDetlVo.setOdRegularReqId(regularInfo.getOdRegularReqId());
					orderRegularReqOrderDetlVo.setIlGoodsId(cartGoods.getIlGoodsId());
					orderRegularReqOrderDetlVo.setIlItemCd(cartGoods.getIlItemCd());
					orderRegularReqOrderDetlVo.setOrderCnt(cartGoods.getQty());
					orderRegularReqOrderDetlVo.setSalePrice(cartGoods.getSalePrice());
					orderRegularReqOrderDetlVo.setReqDetailStatusCd(OrderEnums.RegularDetailStatus.APPLY.getCode());
					orderRegularRegistrationService.addOrderRegularReqOrderDetl(orderRegularReqOrderDetlVo);
				} else {
					orderRegularReqOrderDetlVo.setOrderCnt(orderRegularReqOrderDetlVo.getOrderCnt() + cartGoods.getQty());
					orderRegularRegistrationService.putOrderRegularReqOrderDetlOrderCnt(orderRegularReqOrderDetlVo);
				}
			}
		}
		resDto.setOdRegularReqId(regularInfo.getOdRegularReqId());
		resDto.setReqId(regularInfo.getReqId());
		resDto.setResult(true);
		resDto.setRegularGoodsList(regularGoodsList);
		return resDto;
	}

	/**
	 * 정기배송주문신청 리스트 엑셀다운로드
	 * @param regularReqListRequestDto
	 * @return
	 * @throws Exception
	 */
	@Override
	public ExcelDownloadDto getOrderRegularReqListExcel(RegularReqListRequestDto regularReqListRequestDto) throws Exception {

       	List<RegularReqListDto> orderRegularReqList = orderRegularService.getOrderRegularReqList(regularReqListRequestDto);

		List<String> regularDetlStatuCdList = new ArrayList<>();
		regularDetlStatuCdList.add(RegularDetlStatusCd.CANCEL_BUYER.getCode());
		regularDetlStatuCdList.add(RegularDetlStatusCd.CANCEL_SELLER.getCode());
		for(RegularReqListDto regularReqListItem : orderRegularReqList) {
			List<RegularResultReqRoundGoodsListDto> regularResultReqRoundGoodsList = orderRegularDetailService.getOrderRegularReqDetailPaymentGoods(
																																					regularReqListItem.getOdRegularReqId(),
																																					0,
																																					regularDetlStatuCdList
																																					);
			// 상품 조회 결과가 존재하지 않을 경우 신청정보 기준으로 재조회
			if(CollectionUtils.isEmpty(regularResultReqRoundGoodsList)) {
				regularResultReqRoundGoodsList = orderRegularDetailService.getOrderRegularReqDetailPaymentGoods(
						regularReqListItem.getOdRegularReqId(),
						regularReqListItem.getOdRegularResultId(),
						new ArrayList<>()
				);
			}
			// 정기배송 추가 할인 정보 조회
			RegularResultGoodsDetailInfoDto regularResultAddDiscountInfo = orderRegularDetailService.getOrderRegularResultAddDiscountInfo(regularReqListItem.getOdRegularReqId());
			RegularReqPaymentListDto paymentInfo = new RegularReqPaymentListDto();
			orderRegularDetailBiz.getOrderRegularPaymentInfo(regularResultReqRoundGoodsList, paymentInfo, 0, regularResultAddDiscountInfo.getAddDiscountStdReqRound());
			regularReqListItem.setSalePrice(paymentInfo.getRecommendedPrice());
			regularReqListItem.setDiscountPrice((paymentInfo.getRecommendedPrice() - paymentInfo.getSalePrice()) + paymentInfo.getDiscountPrice() + paymentInfo.getAddDiscountPrice());
			regularReqListItem.setPaidPrice(paymentInfo.getPaidPrice());
		}

        String excelFileName = OrderExcelNm.REGULAR.getCodeName();
        String excelSheetName = "sheet";
        /* 화면값보다 20더 하면맞다. */
        Integer[] widthListOfFirstWorksheet = { 200, 180, 120, 120, 120, 120, 120, 120, 120, 120, 120, 320, 120, 120, 120, 120, 120 };

        String[] alignListOfFirstWorksheet = { "center", "center", "center", "center", "center", "center", "center", "center",
        		"center", "center", "center", "center", "center", "center", "center", "center", "center" };

        String[] propertyListOfFirstWorksheet = { "createDt", "reqId", "reqRound", "totCnt", "goodsCycleTermTpNm", "goodsCycleTpNm", "weekCdNm",
        		"regularStatusCdNm", "buyerNm", "loginId", "recvNm", "goodsNm", "goodsCnt", "salePrice", "discountPrice", "paidPrice", "agentTypeCdNm" };

        String[] firstHeaderListOfFirstWorksheet = { "신청일자", "신청번호", "요청회차", "총회차", "신청기간", "신청주기", "신청요일", "신청상태", "주문자정보", "주문자ID",
        		"수취인명", "상품명", "상품수", "판매가", "할인금액", "결제예정금액", "유형" };

        String[] cellTypeListOfFirstWorksheet = {  "String", "String", "String", "String", "String", "String", "String", "String", "String", "String",
				"String", "String", "int", "int", "int", "int", "String"};

        ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder()
                                                               .workSheetName(excelSheetName)
                                                               .propertyList(propertyListOfFirstWorksheet)
                                                               .widthList(widthListOfFirstWorksheet)
                                                               .alignList(alignListOfFirstWorksheet)
													           .cellTypeList(cellTypeListOfFirstWorksheet)
                                                               .build();

        firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet);

        firstWorkSheetDto.setExcelDataList(orderRegularReqList);

        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder().excelFileName(excelFileName).build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

        return excelDownloadDto;
	}

	/**
	 * 정기배송 주문 신청 리스트 조회
	 * @param regularReqListRequestDto
	 * @return
	 * @throws Exception
	 */
	@Override
	@UserMaskingRun(system = "MUST_MASKING")
	public RegularReqListResponseDto getOrderRegularReqList(RegularReqListRequestDto regularReqListRequestDto) throws Exception {

		RegularReqListResponseDto regularReqListResponseDto = new RegularReqListResponseDto();
		PageMethod.startPage(regularReqListRequestDto.getPage(), regularReqListRequestDto.getPageSize());

		Page<RegularReqListDto> orderRegularReqList = orderRegularService.getOrderRegularReqList(regularReqListRequestDto);

		List<RegularReqListDto> orderRegularReqInfoList = orderRegularReqList.getResult();

		List<String> regularDetlStatuCdList = new ArrayList<>();
		regularDetlStatuCdList.add(RegularDetlStatusCd.CANCEL_BUYER.getCode());
		regularDetlStatuCdList.add(RegularDetlStatusCd.CANCEL_SELLER.getCode());
		for(RegularReqListDto regularReqListItem : orderRegularReqInfoList) {
			List<RegularResultReqRoundGoodsListDto> regularResultReqRoundGoodsList = orderRegularDetailService.getOrderRegularReqDetailPaymentGoods(
					regularReqListItem.getOdRegularReqId(),
					0,
					regularDetlStatuCdList
			);

			// 상품 조회 결과가 존재하지 않을 경우 신청정보 기준으로 재조회
			if(CollectionUtils.isEmpty(regularResultReqRoundGoodsList)) {
				regularResultReqRoundGoodsList = orderRegularDetailService.getOrderRegularReqDetailPaymentGoods(
						regularReqListItem.getOdRegularReqId(),
						regularReqListItem.getOdRegularResultId(),
						new ArrayList<>()
				);
			}

			// 정기배송 추가 할인 정보 조회
			RegularResultGoodsDetailInfoDto regularResultAddDiscountInfo = orderRegularDetailService.getOrderRegularResultAddDiscountInfo(regularReqListItem.getOdRegularReqId());
			RegularReqPaymentListDto paymentInfo = new RegularReqPaymentListDto();
			orderRegularDetailBiz.getOrderRegularPaymentInfo(regularResultReqRoundGoodsList, paymentInfo, 0, regularResultAddDiscountInfo.getAddDiscountStdReqRound());
			regularReqListItem.setSalePrice(paymentInfo.getRecommendedPrice());
			regularReqListItem.setDiscountPrice((paymentInfo.getRecommendedPrice() - paymentInfo.getSalePrice()) + paymentInfo.getDiscountPrice() + paymentInfo.getAddDiscountPrice());
			regularReqListItem.setPaidPrice(paymentInfo.getPaidPrice());
		}

		regularReqListResponseDto.setRows(orderRegularReqInfoList);
		regularReqListResponseDto.setTotal(orderRegularReqList.getTotal());

		return regularReqListResponseDto;
	}

	/**
	 * 정기배송 신청 내역 정보 조회
	 * @param regularReqGoodsListRequestDto
	 * @return
	 * @throws Exception
	 */
	@Override
	public MallRegularReqInfoResponseDto getOrderRegularReqInfo(RegularReqGoodsListRequestDto regularReqGoodsListRequestDto) throws Exception {

		// 정기배송 주문 신청 정보 조회
		MallRegularReqInfoResponseDto mallRegularReqInfoResponseDto = orderRegularService.getOrderRegularReqInfo(regularReqGoodsListRequestDto.getUrUserId());

		if(!Objects.isNull(mallRegularReqInfoResponseDto)) {

			// 전체 회차
			int totReqRound = mallRegularReqInfoResponseDto.getTotCnt();
			// 현재 회차
			int nowReqRound = mallRegularReqInfoResponseDto.getReqRound();
			mallRegularReqInfoResponseDto.setTermExtensionYn("N");

			RegularResultOrderStatusResultDto regularResultorderStatusResult = null;
			// 마지막 회차일 경우
			if(totReqRound == nowReqRound) {
				// 결제 확인 정보가 없을 경우 기간연장 가능
				regularResultorderStatusResult = orderRegularDetailService.getOrderRegularResultGoodsOrderStatusCd(mallRegularReqInfoResponseDto.getOdRegularResultId());
				if(Objects.isNull(regularResultorderStatusResult)) {
					mallRegularReqInfoResponseDto.setTermExtensionYn("Y");
				}
			}
			// 마지막 이전 회차일 경우
			else if((totReqRound - 1) == nowReqRound) {
				// 결제 확인이 되었고  마지막회차 정보가 결제확인이 아닐 경우 기간연장 가능
				regularResultorderStatusResult = orderRegularDetailService.getOrderRegularResultGoodsOrderStatusCd(mallRegularReqInfoResponseDto.getOdRegularResultId());
				if(!Objects.isNull(regularResultorderStatusResult)) {
					regularResultorderStatusResult = orderRegularDetailService.getOrderRegularResultGoodsOrderStatusCd(mallRegularReqInfoResponseDto.getMaxOdRegularResultId());
					if(Objects.isNull(regularResultorderStatusResult)) {
						mallRegularReqInfoResponseDto.setTermExtensionYn("Y");
					}
				}
			}

			// 수령인 우편번호
			String recvZipCd = mallRegularReqInfoResponseDto.getRecvZipCd();

			// 정기배송 기본 할인 정보
			int basicDiscountRate = mallRegularReqInfoResponseDto.getBasicDiscountRate();
			// 정기배송 추가 할인 회차 정보
			int addDiscountRound = mallRegularReqInfoResponseDto.getAddDiscountRound();
			// 정기배송 추가 할인 정보
			int addDiscountRate = mallRegularReqInfoResponseDto.getAddDiscountRate();

			// 정기배송상세 상태코드
			List<String> reqDetailStatusCdList = new ArrayList<> ();
			reqDetailStatusCdList.add(OrderEnums.RegularDetailStatus.APPLY.getCode());
			reqDetailStatusCdList.add(OrderEnums.RegularDetailStatus.SKIP.getCode());
			// 상품 조회 상품 상세 상태 파라미터 Set
			regularReqGoodsListRequestDto.setReqDetailStatusCdList(reqDetailStatusCdList);
			regularReqGoodsListRequestDto.setReqDetailStatusCd(OrderEnums.RegularDetailStatus.APPLY.getCode());

			// 정기배송 조회 회차 총 수 조회
			int totItemCnt = orderRegularService.getOrderRegularReqRoundGoodsListTotCnt(regularReqGoodsListRequestDto);

			mallRegularReqInfoResponseDto.setTotItemCnt(totItemCnt);

			// 상품목록 미 존재 시 종료
			if(totItemCnt < 1) {
				mallRegularReqInfoResponseDto.setReqRoundList(new ArrayList<>());
				return mallRegularReqInfoResponseDto;
			}

			// 정기배송 회차별 상품 목록 조회
			List<RegularResultReqRoundGoodsListDto> orderRegularReqRoundGoodsList = orderRegularService.getOrderRegularReqRoundGoodsList(regularReqGoodsListRequestDto);

			// 회차별, 출고처별 grouping
			Map<Integer, Map<String, List<RegularResultReqRoundGoodsListDto>>> resultMap = orderRegularReqRoundGoodsList.stream()
					.filter(obj -> obj.getParentIlGoodsId() == 0)
					.collect(groupingBy(RegularResultReqRoundGoodsListDto::getReqRound, LinkedHashMap::new,
							groupingBy(RegularResultReqRoundGoodsListDto::getGrpWarehouseShippingTmplId, LinkedHashMap::new, toList())));

			// 부모 상품 PK 별 Group
			Map<Long, List<RegularResultReqRoundGoodsListDto>> childGoodsList = orderRegularReqRoundGoodsList.stream()
					.filter(obj -> obj.getParentIlGoodsId() != 0)
					.collect(groupingBy(RegularResultReqRoundGoodsListDto::getParentIlGoodsId, LinkedHashMap::new, toList()));

			List<RegularResultReqRoundListDto> regularResultReqRoundList = new ArrayList<>();

			// 회차별 주문상세 forEach
			//resultMap.entrySet().forEach(entry -> {
			for(int reqRound : resultMap.keySet()) {

				//int reqRound = entry.getKey();
				RegularResultReqRoundListDto regularResultReqRoundInfo = new RegularResultReqRoundListDto();
				regularResultReqRoundInfo.setReqRound(reqRound);

				// 출고처 리스트
				//Map<Long, List<RegularResultReqRoundGoodsListDto>> urWarehouseIdList = entry.getValue();
				Map<String, List<RegularResultReqRoundGoodsListDto>> urWarehouseIdList = resultMap.get(reqRound);

				// 배송정책별 리스트
				List<RegularResultShippingZoneListDto> regularResultShippingZoneList = new ArrayList<>();

				// 해당 회차의 상품 수 저장을 위한 Map
				int goodsCnt = 0;
//				Map<Integer, Integer> reqRoundGoodsSizeMap = new HashMap<>();
//				reqRoundGoodsSizeMap.put(reqRound, 0);
				// 해당 회차의 Skip 수 체크를 위한 Map
				int goodsSkipCnt = 0;
//				Map<Integer, Integer> reqRoundSkipMap = new HashMap<>();
//				reqRoundSkipMap.put(reqRound, 0);

				// 출고처 별 Looping
				//urWarehouseIdList.entrySet().forEach(wareList -> {
				for(String urWarehouseId : urWarehouseIdList.keySet()) {

					// 배송비 계산용 변수
					//List<Long> ilShippingTtmplIdList = wareList.getValue().stream().map(x -> x.getIlShippingTmplId()).distinct().collect(Collectors.toList());
					List<Long> ilShippingTtmplIdList = urWarehouseIdList.get(urWarehouseId).stream().filter(x -> OrderEnums.RegularDetailStatus.APPLY.getCode().equals(x.getReqDetailStatusCd())).map(x -> x.getIlShippingTmplId()).distinct().collect(Collectors.toList());

					//String [] arrGrpWarehouseShippingTmplId = wareList.getKey().split(Constants.ARRAY_SEPARATORS);
					RegularResultShippingZoneListDto regularResultShippingZoneInfo = new RegularResultShippingZoneListDto();
//					long ilShippingTmplId = Long.parseLong(arrGrpWarehouseShippingTmplId[1]);
					long ilShippingTmplId = 0;

					// 상품 상세 정보
					//List<RegularResultReqRoundGoodsListDto> reqRoundGoodsList = wareList.getValue();
					List<RegularResultReqRoundGoodsListDto> reqRoundGoodsList = urWarehouseIdList.get(urWarehouseId);
					List<RegularResultGoodsListDto> regularResultGoodsList = new ArrayList<>();

					//reqRoundGoodsSizeMap.put(reqRound, reqRoundGoodsSizeMap.get(reqRound) + reqRoundGoodsList.size());
					goodsCnt += reqRoundGoodsList.size();
					int goodsPrice = 0;
					int discountPrice = 0;
					int directDiscountPrice = 0;
					int orderCnt = 0;

					// 상품 상세 정보 만큼 Loop
					for (RegularResultReqRoundGoodsListDto reqRoundGoodsItem : reqRoundGoodsList) {

						// 회차정보 Set Start
						regularResultReqRoundInfo.setOdRegularResultId(reqRoundGoodsItem.getOdRegularResultId());
						regularResultReqRoundInfo.setArriveDt(reqRoundGoodsItem.getArriveDt());
						regularResultReqRoundInfo.setRegularSkipPsbYn(reqRoundGoodsItem.getRegularSkipPsbYn());
						regularResultReqRoundInfo.setReqRoundYn(reqRoundGoodsItem.getReqRoundYn());
						regularResultReqRoundInfo.setRegularStatusCd(reqRoundGoodsItem.getRegularStatusCd());
						regularResultReqRoundInfo.setRegularStatusCdNm(reqRoundGoodsItem.getRegularStatusCdNm());
						regularResultReqRoundInfo.setOrderCreateYn(reqRoundGoodsItem.getOrderCreateYn());
						regularResultReqRoundInfo.setRegularRoundEndYn(reqRoundGoodsItem.getRegularRoundEndYn());
						regularResultReqRoundInfo.setPaymentFailCnt(reqRoundGoodsItem.getPaymentFailCnt());

						// 결제정보가 존재할 경우 결제 정보로 Set
						if ("Y".equals(reqRoundGoodsItem.getReqRoundYn())) {
							regularResultReqRoundInfo.setSalePrice(reqRoundGoodsItem.getTotSalePrice());
							regularResultReqRoundInfo.setDiscountPrice(reqRoundGoodsItem.getTotDiscountPrice());
							regularResultReqRoundInfo.setShippingPrice(reqRoundGoodsItem.getTotShippingPrice());
							regularResultReqRoundInfo.setPaidPrice(reqRoundGoodsItem.getTotPaidPrice());
						}
						// 회차정보 Set End

						RegularResultGoodsListDto regularResultGoodsInfo = new RegularResultGoodsListDto();
						regularResultGoodsInfo.setOdRegularResultDetlId(reqRoundGoodsItem.getOdRegularResultDetlId());
						regularResultGoodsInfo.setOdRegularResultId(reqRoundGoodsItem.getOdRegularResultId());
						regularResultGoodsInfo.setIlItemCd(reqRoundGoodsItem.getIlItemCd());
						regularResultGoodsInfo.setIlGoodsId(reqRoundGoodsItem.getIlGoodsId());
						regularResultGoodsInfo.setGoodsNm(reqRoundGoodsItem.getGoodsNm());
						regularResultGoodsInfo.setOrderCnt(reqRoundGoodsItem.getOrderCnt());
						regularResultGoodsInfo.setReqDetailStatusCd(reqRoundGoodsItem.getReqDetailStatusCd());
						regularResultGoodsInfo.setReqDetailStatusCdNm(reqRoundGoodsItem.getReqDetailStatusCdNm());
						regularResultGoodsInfo.setThumbnailPath(reqRoundGoodsItem.getThumbnailPath());
						regularResultGoodsInfo.setSaleStatus(reqRoundGoodsItem.getSaleStatus());
						regularResultGoodsInfo.setSaleStatusNm(reqRoundGoodsItem.getSaleStatusNm());
						regularResultGoodsInfo.setRegularSaleStatus(reqRoundGoodsItem.getRegularSaleStatus());
						regularResultGoodsInfo.setRegularSaleStatusNm(reqRoundGoodsItem.getRegularSaleStatusNm());
						regularResultGoodsInfo.setRegularSkipYn("N");
						regularResultGoodsInfo.setRegularSkipPsbYn(reqRoundGoodsItem.getRegularSkipPsbYn());
						regularResultGoodsInfo.setGoodsDefaultRate(basicDiscountRate);
						regularResultGoodsInfo.setRegularCancelPsbYn(reqRoundGoodsItem.getRegularCancelPsbYn());

						// 정기배송상세 상태가 건너뛰기 일경우 건너뛰기 여부 Y, 회차 건너뛰기 상품 수 증가
						if (RegularDetailStatus.SKIP.getCode().equals(reqRoundGoodsItem.getReqDetailStatusCd())) {
							//reqRoundSkipMap.put(reqRound, reqRoundSkipMap.get(reqRound) + 1);
							goodsSkipCnt++;
							regularResultGoodsInfo.setRegularSkipYn("Y");
						}

						// 상품 금액
						int beforePaymentPrice = reqRoundGoodsItem.getSalePrice();
						// 정기배송 기본 할인 금액
						int afterPaymentPrice = PriceUtil.getPriceByRate(beforePaymentPrice, basicDiscountRate);
						// 정기배송 기준 회차부터는 추가 할인
						if (reqRound >= addDiscountRound) {
							// 할인한 상품금액 정보에 추가 할인 적용
							afterPaymentPrice = PriceUtil.getPriceByRate(afterPaymentPrice, addDiscountRate);
						}

						regularResultGoodsInfo.setPaidPrice(afterPaymentPrice * reqRoundGoodsItem.getOrderCnt());

						// 판매중 상태, 신청 상태 인 것만 상품 정보 Set
						if (GoodsEnums.SaleStatus.ON_SALE.getCode().equals(reqRoundGoodsItem.getSaleStatus()) &&
								OrderEnums.RegularDetailStatus.APPLY.getCode().equals(reqRoundGoodsItem.getReqDetailStatusCd())) {

							goodsPrice += beforePaymentPrice * reqRoundGoodsItem.getOrderCnt();
							discountPrice += (beforePaymentPrice - afterPaymentPrice) * reqRoundGoodsItem.getOrderCnt();
							directDiscountPrice += (reqRoundGoodsItem.getRecommendedPrice() - reqRoundGoodsItem.getSalePrice()) * reqRoundGoodsItem.getOrderCnt();
							orderCnt += reqRoundGoodsItem.getOrderCnt();

							// 추가 상품 담기 위한 리스트
							List<RegularResultGoodsListDto> addChildGoodsList = new ArrayList<>();
							// 자식 상품이 존재할 경우 리스트 추가
							if (childGoodsList.containsKey(reqRoundGoodsItem.getIlGoodsId())) {
								List<RegularResultReqRoundGoodsListDto> chileItemList = childGoodsList.get(reqRoundGoodsItem.getIlGoodsId());
								// 상품 상세 정보 만큼 Loop
								for (RegularResultReqRoundGoodsListDto childGoodsInfo : chileItemList) {
									RegularResultGoodsListDto regularChildGoodsInfo = new RegularResultGoodsListDto();
									regularChildGoodsInfo.setOdRegularResultDetlId(childGoodsInfo.getOdRegularResultDetlId());
									regularChildGoodsInfo.setOdRegularResultId(childGoodsInfo.getOdRegularResultId());
									regularChildGoodsInfo.setIlItemCd(childGoodsInfo.getIlItemCd());
									regularChildGoodsInfo.setIlGoodsId(childGoodsInfo.getIlGoodsId());
									regularChildGoodsInfo.setGoodsNm(childGoodsInfo.getGoodsNm());
									regularChildGoodsInfo.setOrderCnt(childGoodsInfo.getOrderCnt());
									regularChildGoodsInfo.setReqDetailStatusCd(childGoodsInfo.getReqDetailStatusCd());
									regularChildGoodsInfo.setReqDetailStatusCdNm(childGoodsInfo.getReqDetailStatusCdNm());
									regularChildGoodsInfo.setThumbnailPath(childGoodsInfo.getThumbnailPath());

									goodsPrice += childGoodsInfo.getSalePrice() * childGoodsInfo.getOrderCnt();
									orderCnt += childGoodsInfo.getOrderCnt();

									// 추가상품 정보 add
									addChildGoodsList.add(regularChildGoodsInfo);
								}
							}
							regularResultGoodsInfo.setAddGoodsList(addChildGoodsList);
						}

						// 상품리스트 add
						regularResultGoodsList.add(regularResultGoodsInfo);
					}
					regularResultShippingZoneInfo.setGoodsList(regularResultGoodsList);

					// 출고처별 배송비 계산
					int shippingPrice = 0;
					String shippingPriceText = "";
					try {
						RegularShippingPriceInfoDto shippingPriceInfo = orderRegularService.getShippingPrice(ilShippingTtmplIdList, goodsPrice, orderCnt, recvZipCd);
						shippingPrice = shippingPriceInfo.getShippingPrice();
						ilShippingTmplId = shippingPriceInfo.getIlShippingTmplId();
						ShippingDataResultVo shippingDataResultVo = goodsShippingTemplateBiz.getShippingInfoByShippingTmplId(ilShippingTmplId);
						shippingPriceText = goodsShippingTemplateBiz.getShippingPriceText(shippingDataResultVo);
					} catch (Exception e) {
						e.printStackTrace();
					}

					regularResultShippingZoneInfo.setUrWarehouseId(ilShippingTmplId);
					regularResultShippingZoneInfo.setDeliveryTmplNm(shippingPriceText);

					// 출고처 별 주문금액 정보 Set
					regularResultShippingZoneInfo.setPaidPrice((goodsPrice - discountPrice) + shippingPrice);
					// 배송비 정보 Set
					regularResultShippingZoneInfo.setShippingPrice(shippingPrice);

					// 배송정책 리스트 add
					regularResultShippingZoneList.add(regularResultShippingZoneInfo);

					// 결제 금액 정보가 없을 경우 결제 예정 금액으로 Set
					if ("N".equals(regularResultReqRoundInfo.getReqRoundYn())) {
						regularResultReqRoundInfo.setSalePrice(goodsPrice + regularResultReqRoundInfo.getSalePrice());
						regularResultReqRoundInfo.setDiscountPrice((discountPrice + directDiscountPrice) + regularResultReqRoundInfo.getDiscountPrice());
						regularResultReqRoundInfo.setShippingPrice(shippingPrice + regularResultReqRoundInfo.getShippingPrice());
						regularResultReqRoundInfo.setPaidPrice(regularResultReqRoundInfo.getSalePrice() + regularResultReqRoundInfo.getShippingPrice() - regularResultReqRoundInfo.getDiscountPrice());
					}
				}
				//});

				// 건너뛰기 수 와 리스트 사이즈가 동일 할 경우 전체 건너뛰기 처리 한 것
				//regularResultReqRoundInfo.setRegularSkipYn((reqRoundGoodsSizeMap.get(reqRound).equals(reqRoundSkipMap.get(reqRound)) ? "Y" : "N"));
				regularResultReqRoundInfo.setRegularSkipYn((goodsCnt == goodsSkipCnt) ? "Y" : "N");

				regularResultReqRoundInfo.setShippingZoneList(regularResultShippingZoneList);

				// 회차정보 리스트 add
				regularResultReqRoundList.add(regularResultReqRoundInfo);
			}
			//});

			// 회차별 상품정보 add
			mallRegularReqInfoResponseDto.setReqRoundList(regularResultReqRoundList);
		}

		return mallRegularReqInfoResponseDto;
	}
}
