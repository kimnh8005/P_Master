package kr.co.pulmuone.v1.order.regular.service;

import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.GoodsDiscountType;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.enums.OrderEnums.*;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.util.PriceUtil;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ShippingDataResultVo;
import kr.co.pulmuone.v1.goods.goods.service.GoodsShippingTemplateBiz;
import kr.co.pulmuone.v1.order.email.dto.OrderInfoForEmailResultDto;
import kr.co.pulmuone.v1.order.email.dto.OrderRegularResultDto;
import kr.co.pulmuone.v1.order.email.service.OrderEmailBiz;
import kr.co.pulmuone.v1.order.email.service.OrderEmailSendBiz;
import kr.co.pulmuone.v1.order.registration.service.OrderRegistrationBiz;
import kr.co.pulmuone.v1.order.regular.dto.*;
import kr.co.pulmuone.v1.order.regular.dto.vo.*;
import kr.co.pulmuone.v1.policy.holiday.dto.GetHolidayListResponseDto;
import kr.co.pulmuone.v1.policy.holiday.dto.vo.GetHolidayListResultVo;
import kr.co.pulmuone.v1.policy.holiday.service.PolicyHolidayBiz;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송주문상세 OrderRegularDetailBizImpl
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020.02.07	  김명진           최초작성
 * =======================================================================
 * </PRE>
 */

@Slf4j
@Service
public class OrderRegularDetailBizImpl implements OrderRegularDetailBiz {

	@Autowired
	public PolicyHolidayBiz policyHolidayBiz;

	@Autowired
	private OrderRegularDetailService orderRegularDetailService;

	@Autowired
	private OrderRegularService orderRegularService;

	@Autowired
	private OrderRegularRegistrationService orderRegularRegistrationService;

	@Autowired
	OrderRegistrationBiz orderRegistrationBiz;

	@Autowired
	GoodsShippingTemplateBiz goodsShippingTemplateBiz;

	@Autowired
	private OrderEmailSendBiz orderEmailSendBiz;

	@Autowired
	private OrderEmailBiz orderEmailBiz;


	/**
	 * 정기배송주문신청 내역 상품 리스트 조회
	 * @param regularReqListRequestDto
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getOrderRegularReqDetailGoodsList(long odRegularReqId) throws Exception {

		List<RegularResultReqRoundGoodsListDto> regularReqDetailGoodsList = orderRegularDetailService.getOrderRegularReqDetailGoodsList(odRegularReqId, "");

		if(Objects.isNull(regularReqDetailGoodsList)) {
			regularReqDetailGoodsList = new ArrayList<> ();
		}
		else {

			// 배송지 정보 얻기
			RegularReqShippingZoneDto regularReqShippingZone = orderRegularDetailService.getOrderRegularReqDetailShippingZone(odRegularReqId);

			// 신청상태 별, 출고처 별 grouping
			Map<String, Map<Long, List<RegularResultReqRoundGoodsListDto>>> resultMap = regularReqDetailGoodsList.stream()
					.filter(obj -> obj.getParentIlGoodsId() == 0)
					.collect(groupingBy(RegularResultReqRoundGoodsListDto::getReqDetailStatusCd,
							groupingBy(RegularResultReqRoundGoodsListDto::getUrWarehouseId, LinkedHashMap::new, toList())));

			// 부모 상품 PK 별 Group
			Map<Long, List<RegularResultReqRoundGoodsListDto>> childGoodsList = regularReqDetailGoodsList.stream()
					.filter(obj -> obj.getParentIlGoodsId() != 0)
					.collect(groupingBy(RegularResultReqRoundGoodsListDto::getParentIlGoodsId, LinkedHashMap::new, toList()));

			List<RegularResultReqRoundGoodsListDto> regularReqDetailGoodsShippingList = new ArrayList<> ();

			resultMap.entrySet().forEach(statusList -> {

				String key = statusList.getKey();
				Map<Long, List<RegularResultReqRoundGoodsListDto>> statusValue = statusList.getValue();

				statusValue.entrySet().forEach(wareList -> {


					int shippingPriceIndex = regularReqDetailGoodsShippingList.size();
					boolean firstIndexFlag = false;

					// 배송비 계산용 변수
					List<Long> ilShippingTtmplIdList = wareList.getValue().stream().map(x -> x.getIlShippingTmplId()).distinct().collect(Collectors.toList());
					int goodsPrice = 0;
					int discountPrice = 0;
					int orderCnt = 0;

					// 상품 상세 정보
					List<RegularResultReqRoundGoodsListDto> reqRoundGoodsList = wareList.getValue();

					// 상품 상세 정보 만큼 Loop
					for(RegularResultReqRoundGoodsListDto reqRoundGoodsItem : reqRoundGoodsList) {

						// 해지상태인 것은 포함하지 않는다
						if(	!reqRoundGoodsItem.getReqDetailStatusCd().equals(OrderEnums.RegularDetlStatusCd.CANCEL_BUYER.getCode()) &&
							!reqRoundGoodsItem.getReqDetailStatusCd().equals(OrderEnums.RegularDetlStatusCd.CANCEL_SELLER.getCode())) {

							if(!firstIndexFlag) {
								shippingPriceIndex += reqRoundGoodsList.indexOf(reqRoundGoodsItem);
								firstIndexFlag = true;
							}

							// 배송정책 ID add
							if(!ilShippingTtmplIdList.contains(reqRoundGoodsItem.getIlShippingTmplId())) {
								ilShippingTtmplIdList.add(reqRoundGoodsItem.getIlShippingTmplId());
							}

							// 상품 금액
							int beforePaymentPrice = reqRoundGoodsItem.getSalePrice();
							// 정기배송 기본 할인 금액
							int afterPaymentPrice = PriceUtil.getPriceByRate(beforePaymentPrice, reqRoundGoodsItem.getBasicDiscountRate());
							int basicDiscountPrice = (beforePaymentPrice - afterPaymentPrice) * reqRoundGoodsItem.getOrderCnt();
							// 정기배송 3회차부터는 추가 할인
							if(reqRoundGoodsItem.getReqRound() >= reqRoundGoodsItem.getAddDiscountRound()) {
								// 할인한 상품금액 정보에 추가 할인 적용
								afterPaymentPrice = PriceUtil.getPriceByRate(afterPaymentPrice, reqRoundGoodsItem.getAdddiscountRate());
							}

							goodsPrice += beforePaymentPrice * reqRoundGoodsItem.getOrderCnt();
							discountPrice += (beforePaymentPrice - afterPaymentPrice) * reqRoundGoodsItem.getOrderCnt();
							orderCnt += reqRoundGoodsItem.getOrderCnt();

							reqRoundGoodsItem.setBasicDiscountPrice(basicDiscountPrice);
							reqRoundGoodsItem.setDiscountPrice((beforePaymentPrice - afterPaymentPrice) * reqRoundGoodsItem.getOrderCnt());

							// 추가 상품이 존재할 경우 추가 상품 금액 정보 얻기
							if(childGoodsList.containsKey(reqRoundGoodsItem.getIlGoodsId())) {
								List<RegularResultReqRoundGoodsListDto> chileItemList = childGoodsList.get(reqRoundGoodsItem.getIlGoodsId());
								// 상품 상세 정보 만큼 Loop
								for(RegularResultReqRoundGoodsListDto childGoodsInfo : chileItemList) {
									goodsPrice += childGoodsInfo.getSalePrice() * childGoodsInfo.getOrderCnt();
									orderCnt += childGoodsInfo.getOrderCnt();
								}
							}
						}
						else {

							// 상품 금액
							int beforePaymentPrice = reqRoundGoodsItem.getSalePrice();
							// 정기배송 기본 할인 금액
							int afterPaymentPrice = PriceUtil.getPriceByRate(beforePaymentPrice, reqRoundGoodsItem.getBasicDiscountRate());
							int basicDiscountPrice = (beforePaymentPrice - afterPaymentPrice) * reqRoundGoodsItem.getOrderCnt();

							reqRoundGoodsItem.setBasicDiscountPrice(basicDiscountPrice);
						}

						regularReqDetailGoodsShippingList.add(reqRoundGoodsItem);
					}

					// 출고처별 배송비 계산

					int shippingPrice = 0;
					try {
						RegularShippingPriceInfoDto shippingPriceInfo = orderRegularService.getShippingPrice(ilShippingTtmplIdList, (goodsPrice - discountPrice), orderCnt, regularReqShippingZone.getRecvZipCd());
						shippingPrice = shippingPriceInfo.getShippingPrice();
					}
					catch(Exception e) {
						e.printStackTrace();
					}

					RegularResultReqRoundGoodsListDto regularResultReqRoundGoodsInfo = regularReqDetailGoodsShippingList.get(shippingPriceIndex);
					regularResultReqRoundGoodsInfo.setShippingPrice(shippingPrice);
				});
			});

			regularReqDetailGoodsList = regularReqDetailGoodsShippingList;
		}

		RegularReqDetailGoodsListResponseDto regularReqDetailGoodsListResponse = new RegularReqDetailGoodsListResponseDto();
		regularReqDetailGoodsListResponse.setTotal(regularReqDetailGoodsList.size());
		regularReqDetailGoodsListResponse.setRows(regularReqDetailGoodsList);

		return ApiResult.success(regularReqDetailGoodsListResponse);
	}

	/**
	 * 정기배송주문신청 상품 구독해지
	 * @param odRegularReqId
	 * @param ilGoodsIdList
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> putOrderRegularReqGoodsCancel(long odRegularReqId, List<Long> ilGoodsIdList) throws Exception {

		UserVo userVo = SessionUtil.getBosUserVO();
		long urUserId = Long.parseLong(userVo.getUserId());
		int cancelCnt = 0;

		for(long ilGoodsId : ilGoodsIdList) {

			// 1. 정기배송신청ID 와 상품PK로 정기배송결과상세PK 조회
			long odRegularResultDetlId = orderRegularDetailService.getOrderRegularResultDetlId(odRegularReqId, ilGoodsId);
			if(odRegularResultDetlId > 0) {
				putOrderRegularGoodsCancel(odRegularResultDetlId, urUserId);
				cancelCnt++;
			}
		}

		if(cancelCnt < 1) {
			return ApiResult.result(OrderEnums.RegularOrderErrorCd.GOODS_CANCEL_FAIL_NONE);
		}

		return ApiResult.success();
	}

	/**
	 * 정기배송주문신청 상품 추가
	 * @param odRegularReqId
	 * @param ilGoodsIdList
	 * @param ilGoodsIdList
	 * @param ilItemCdList
	 * @param goodsNmList
	 * @param orderCntList
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> addOrderRegularReqGoods(long odRegularReqId, List<Long> ilGoodsIdList, List<String> ilItemCdList,
												List<String> goodsNmList, List<Integer> orderCntList) throws Exception {

		UserVo userVo = SessionUtil.getBosUserVO();
		long urUserId = Long.parseLong(userVo.getUserId());
		String regularStatusCd = OrderEnums.RegularStatusCd.APPLY.getCode();
		String reqDetailStatusCd = OrderEnums.RegularDetlStatusCd.APPLY.getCode();
		int insertCnt = 0;
		// 주문 생성 예정일이 내일보다 큰 것 조회
		LocalDate stdDate = LocalDate.now();
		stdDate = stdDate.plusDays(1);

		for(int i=0; i<ilGoodsIdList.size(); i++) {

			long ilGoodsId = ilGoodsIdList.get(i);
			String ilItemCd = ilItemCdList.get(i);
			String goodsNm = goodsNmList.get(i);
			int orderCnt = orderCntList.get(i);

			// 1. 정기배송 주문 신청 상세 테이블 INSERT
			// 정기배송 주문 신청 상세 테이블 중복 조회
			OrderRegularReqOrderDetlVo orderRegularReqOrderDetlVo = orderRegularRegistrationService.getOverlapOrderRegularReqOrderDetl(odRegularReqId, ilGoodsId, reqDetailStatusCd);
			// 이미 등록 된 상품 체크
			if(!Objects.isNull(orderRegularReqOrderDetlVo)) {
				continue;
			}

			orderRegularReqOrderDetlVo = new OrderRegularReqOrderDetlVo();
			orderRegularReqOrderDetlVo.setOdRegularReqId(odRegularReqId);
			orderRegularReqOrderDetlVo.setOdRegularReqOrderDetlId(orderRegularRegistrationService.getOdRegularReqOrderDetlIdSeq());
			orderRegularReqOrderDetlVo.setIlGoodsId(ilGoodsId);
			orderRegularReqOrderDetlVo.setOrderCnt(orderCnt);
			orderRegularReqOrderDetlVo.setIlItemCd(ilItemCd);
			orderRegularReqOrderDetlVo.setReqDetailStatusCd(reqDetailStatusCd);

			orderRegularRegistrationService.addOrderRegularReqOrderDetl(orderRegularReqOrderDetlVo);

			// 2. 정기배송 결과 정보 조회 - 주문서 생성예정일이 오늘보다 큰 것
			List<RegularResultListDto> regularResultList = orderRegularDetailService.getOrderRegularResultIdList(odRegularReqId, stdDate, regularStatusCd);

			for(RegularResultListDto regularResultItem : regularResultList) {

				// 3. 정기배송 결과 정보 만큼 상세 정보 INSERT
				OrderRegularResultDetlVo orderRegularResultDetlVo = new OrderRegularResultDetlVo();
				orderRegularResultDetlVo.setOdRegularResultId(regularResultItem.getOdRegularResultId());
				orderRegularResultDetlVo.setOdRegularResultDetlId(orderRegularRegistrationService.getOdRegularResultDetlIdSeq());
				orderRegularResultDetlVo.setIlGoodsId(ilGoodsId);
				orderRegularResultDetlVo.setIlItemCd(ilItemCd);
				orderRegularResultDetlVo.setGoodsNm(goodsNm);
				orderRegularResultDetlVo.setOrderCnt(orderCnt);
				orderRegularResultDetlVo.setReqDetailStatusCd(reqDetailStatusCd);
				orderRegularResultDetlVo.setSaleStatus(OrderEnums.RegularDetlSaleStatusCd.ON_SALE.getCode());// 판매상태

				orderRegularRegistrationService.addOrderRegularResultDetl(orderRegularResultDetlVo);
			}
			insertCnt++;
		}

		String reqGbnCd = RegularReqGbnCd.GA.getCode();
		String reqStatusCd = RegularReqStatusCd.PC.getCode();
		String regularReqCont = goodsNmList.get(0) + (goodsNmList.size() > 1 ? " 외 " + (goodsNmList.size() - 1) + "건" : "");

		// 7. 정기배송주문 히스토리 등록 값 세팅
		long odRegularReqHistoryId = orderRegularRegistrationService.getOdRegularReqHistoryIdSeq();
		OrderRegularReqHistoryVo orderRegularReqHistoryVo = new OrderRegularReqHistoryVo();
		orderRegularReqHistoryVo.setOdRegularReqHistoryId(odRegularReqHistoryId);	// 정기배송주문히스토리 PK
		orderRegularReqHistoryVo.setOdRegularReqId(odRegularReqId);					// 정기배송주문신청 PK
		orderRegularReqHistoryVo.setRegularReqGbnCd(reqGbnCd);						// 변경구분값 공통코드(REGULAR_REQ_GBN_CD)
		orderRegularReqHistoryVo.setRegularReqStatusCd(reqStatusCd);				// 처리상태 공통코드(REGULAR_REQ_STATUS_CD)
		orderRegularReqHistoryVo.setRegularReqCont(regularReqCont);					// 상세내용
		orderRegularReqHistoryVo.setCreateId(urUserId);								// 등록자

		// 8. 정기배송주문 히스토리 등록 처리
		orderRegularRegistrationService.addOrderRegularReqHistory(orderRegularReqHistoryVo);

		if(insertCnt < 1) {
			return ApiResult.result(OrderEnums.RegularOrderErrorCd.GOODS_ADD_FAIL_NONE);
		}

		return ApiResult.success();
	}

    /**
     * 정기배송 주문 신청 기간 연장
     * @param odRegularReqId
     * @return
     * @throws Exception
     */
	@Override
	public ApiResult<?> putOrderRegularGoodsCycleTermExt(long odRegularReqId) throws Exception {

		UserVo userVo = SessionUtil.getBosUserVO();
		int insertCnt = putOrderRegularGoodsCycleTermExtension(odRegularReqId, Long.parseLong(userVo.getUserId()));

		if(insertCnt < 1) {
			return ApiResult.result(OrderEnums.RegularOrderErrorCd.GOODS_CYCLE_TERM_EXTENSION_FAIL_NONE);
		}

		return ApiResult.success();
	}

	/**
	 * 정기배송주문신청 내역 신청 정보 조회
	 * @param odRegularReqId
	 * @return
	 * @throws Exception
	 */
	@Override
	@UserMaskingRun(system = "BOS")
	public ApiResult<?> getOrderRegularReqDetailBuyer(long odRegularReqId) throws Exception {
		RegularReqListDto regularReqInfo = orderRegularDetailService.getOrderRegularReqInfo(odRegularReqId);

		if(!Objects.isNull(regularReqInfo)) {
			int months = OrderEnums.RegularShippingCycleTerm.findByCode(regularReqInfo.getGoodsCycleTermTp()).getMonth();
			regularReqInfo.setGoodsCycleTerm(months);
		}

		return ApiResult.success(regularReqInfo);
	}

	/**
	 * 기간 변경 기준일자, 기간연장 횟수 얻기
	 * @param standardDate
	 * @param month
	 * @param termExtensionCnt
	 * @return
	 */
	private RegularReqGoodsCycleTermInfo getStandardDateInfo(LocalDate standardDate, int month, int termExtensionCnt) throws Exception {
		RegularReqGoodsCycleTermInfo regularReqGoodsCycleTermInfo = new RegularReqGoodsCycleTermInfo();
		LocalDate nowDate = LocalDate.now();
		for(int i=0; i<termExtensionCnt; i++) {
			//if((standardDate.isBefore(nowDate) && nowDate.isBefore(standardDate.plusMonths(month))) ||
			if((standardDate.isBefore(nowDate) && nowDate.isBefore(standardDate.plusWeeks(month * 4))) ||
				standardDate.equals(nowDate)) {
				regularReqGoodsCycleTermInfo.setTermExtensionCnt(i);
				regularReqGoodsCycleTermInfo.setStandardDate(standardDate);
				break;
			}
			//standardDate = standardDate.plusMonths(month);
			standardDate = standardDate.plusWeeks(month * 4); // 1달은 4주로 고정
		}
		return regularReqGoodsCycleTermInfo;
	}

	private RegularReqGoodsCycleTermInfo getRegularGoodsTermChk(RegularReqBuyerChangeRequestDto regularReqBuyerChangeRequestDto, RegularReqCycleDaysInfoResponseDto regularCycleDaysInfo) throws Exception {

		// 정기배송 신청 상품 출고처 목록 조회
		List<Long> urWarehouseIds = orderRegularService.getOdRegularUrWarehouseIdsByOdRegularReqId(regularReqBuyerChangeRequestDto.getOdRegularReqId());

		// 마지막 도착 예정일
		LocalDate lastArriveDate = regularCycleDaysInfo.getLastArriveDt();
		// 기간연장횟수
		int termExtensionCnt = regularCycleDaysInfo.getTermExtensionCnt();
		// 기존 신청 기간
		int termMonth = OrderEnums.RegularShippingCycleTerm.findByCode(regularCycleDaysInfo.getGoodsCycleTermTp()).getMonth();
		// 변경 신청 기간
		int changeTermMonth = OrderEnums.RegularShippingCycleTerm.findByCode(regularReqBuyerChangeRequestDto.getGoodsCycleTermTp()).getMonth();
		// 체크 기준 일자
		//LocalDate standardDate = lastArriveDate.minusMonths((termExtensionCnt + 1) * termMonth);
		LocalDate standardDate = lastArriveDate.minusWeeks((termExtensionCnt + 1) * (termMonth * 4));

		// 오늘날짜가 포함 된 체크 기준 일자, 기간연장 횟수 얻기
		RegularReqGoodsCycleTermInfo regularReqGoodsCycleTermInfo = getStandardDateInfo(standardDate, termMonth, termExtensionCnt + 1);

		standardDate = regularReqGoodsCycleTermInfo.getStandardDate();
		//LocalDate endDate = standardDate.plusMonths(changeTermMonth);
		LocalDate endDate = orderRegularService.stdDateWeekEndCheckDate(getWeeksDaysDateInfo(standardDate.plusWeeks(changeTermMonth * 4),
																		RegularWeekCd.findByCode(regularCycleDaysInfo.getWeekCd()).getDays()),
																		urWarehouseIds);
		regularReqGoodsCycleTermInfo.setEndDate(endDate);
		regularReqGoodsCycleTermInfo.setLastArriveDate(lastArriveDate);
		regularReqGoodsCycleTermInfo.setRegularErrorCd(OrderEnums.RegularOrderErrorCd.SUCCESS.getCode());

		// 신청 주기
		int cycleTp = OrderEnums.RegularShippingCycle.findByCode(regularCycleDaysInfo.getGoodsCycleTp()).getWeeks();

		// 오늘일자
		LocalDate nowDate = LocalDate.now();

		// 마지막 배송 도착 예정일이 오늘보다 작거나 같을 경우
		if(endDate.isBefore(nowDate) || endDate.equals(nowDate)) {
			// 변경 신청기간이 기존 기간보다 작을경우
			if(termMonth > changeTermMonth) {
				// 변경 불가
				regularReqGoodsCycleTermInfo.setRegularErrorCd(OrderEnums.RegularOrderErrorCd.GOODS_REQ_INFO_TERM_CHANGE_1.getCode());
			}
		}
		// 마지막 배송 도착 예정일이 오늘보다 클 경우
		else {
			if(nowDate.isBefore(endDate)) {
				// 오늘 날짜와 종료일자의 차이가 배송주기보다 작을 경우 변경 붕가
				if(ChronoUnit.WEEKS.between(nowDate, endDate) < cycleTp) {
					regularReqGoodsCycleTermInfo.setRegularErrorCd(OrderEnums.RegularOrderErrorCd.GOODS_REQ_INFO_TERM_CHANGE_3.getCode());
				}
			}
			// 시간체크 시작일자에서 선택 기간만큼 더한 날짜가 오늘보다 작거나 같을 경우
			else {
				// 변경 불가
				regularReqGoodsCycleTermInfo.setRegularErrorCd(OrderEnums.RegularOrderErrorCd.GOODS_REQ_INFO_TERM_CHANGE_2.getCode());
			}
		}

		return regularReqGoodsCycleTermInfo;
	}

	/**
	 * 정기배송주문신청 내역 신청 변경 정보 조회
	 * @param regularReqBuyerChangeRequestDto
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getOrderRegularReqDetailChangeBuyerInfo(RegularReqBuyerChangeRequestDto regularReqBuyerChangeRequestDto) throws Exception {

		RegularReqCycleDaysInfoResponseDto regularReqCycleDaysInfo = new RegularReqCycleDaysInfoResponseDto();

		// 신청기간 변경 시
		if(OrderEnums.RegularOrderReqInfoChangeCd.GOODS_CYCLE_TERM_TP.getCode().equals(regularReqBuyerChangeRequestDto.getChangeTp())) {

			// 1. 기존 정기배송 신청 정보 조회
			RegularReqCycleDaysInfoResponseDto regularCycleDaysInfo = orderRegularDetailService.getOrderRegularCycleDaysInfo(regularReqBuyerChangeRequestDto.getOdRegularReqId());

			// 2. 신청기간 변경 체크
			RegularReqGoodsCycleTermInfo regularReqGoodsCycleTermInfo = getRegularGoodsTermChk(regularReqBuyerChangeRequestDto, regularCycleDaysInfo);

			// 3. 신청기간 변경 체크가 정상이 아닐 경우
			if(!OrderEnums.RegularOrderErrorCd.SUCCESS.getCode().equals(regularReqGoodsCycleTermInfo.getRegularErrorCd())) {
				return ApiResult.result(OrderEnums.RegularOrderErrorCd.findByCode(regularReqGoodsCycleTermInfo.getRegularErrorCd()));
			}
		}
		// 배송주기, 요일 변경 시
		else {
			// 신청기간이 1개월 또는 2개월 일 경우
			if(OrderEnums.RegularShippingCycleTerm.MONTH1.getCode().equals(regularReqBuyerChangeRequestDto.getGoodsCycleTermTp()) ||
				OrderEnums.RegularShippingCycleTerm.MONTH2.getCode().equals(regularReqBuyerChangeRequestDto.getGoodsCycleTermTp())) {
				int months = OrderEnums.RegularShippingCycleTerm.findByCode(regularReqBuyerChangeRequestDto.getGoodsCycleTermTp()).getMonth();
				int weeks = OrderEnums.RegularShippingCycle.findByCode(regularReqBuyerChangeRequestDto.getGoodsCycleTp()).getWeeks();
				// 선택한 신청 기간 보다 배송주기가 클 경우 변경 불가
				if(months < weeks) {
					return ApiResult.result(OrderEnums.RegularOrderErrorCd.GOODS_REQ_INFO_TERM_CHANGE_2);
				}
			}
			regularReqCycleDaysInfo = getOrderRegularArriveDtList(regularReqBuyerChangeRequestDto.getOdRegularReqId(),
																	regularReqBuyerChangeRequestDto.getGoodsCycleTp(),
																	regularReqBuyerChangeRequestDto.getWeekCd());
		}
		return ApiResult.success(regularReqCycleDaysInfo);
	}

	/**
	 * 정기배송 주문 신청 내역 신청정보 변경
	 * @param regularReqBuyerChangeRequestDto
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> putOrderRegularReqDetailChangeBuyerInfo(RegularReqBuyerChangeRequestDto regularReqBuyerChangeRequestDto) throws Exception {

		UserVo userVo = SessionUtil.getBosUserVO();
		long urUserId = Long.parseLong(userVo.getUserId());
		long odRegularReqId = regularReqBuyerChangeRequestDto.getOdRegularReqId();
		String regularStatusCd = OrderEnums.RegularStatusCd.APPLY.getCode();
		String regularDetlStatusCd = OrderEnums.RegularDetlStatusCd.APPLY.getCode();

		// 신청기간 변경 시
		if(OrderEnums.RegularOrderReqInfoChangeCd.GOODS_CYCLE_TERM_TP.getCode().equals(regularReqBuyerChangeRequestDto.getChangeTp())) {

			// 1. 기존 정기배송 신청 정보 조회
			RegularReqCycleDaysInfoResponseDto regularCycleDaysInfo = orderRegularDetailService.getOrderRegularCycleDaysInfo(regularReqBuyerChangeRequestDto.getOdRegularReqId());

			// 2. 신청기간 변경 체크
			RegularReqGoodsCycleTermInfo regularReqGoodsCycleTermInfo = getRegularGoodsTermChk(regularReqBuyerChangeRequestDto, regularCycleDaysInfo);

			// 3. 신청기간 변경 체크가 정상이 아닐 경우
			if(!OrderEnums.RegularOrderErrorCd.SUCCESS.getCode().equals(regularReqGoodsCycleTermInfo.getRegularErrorCd())) {
				return ApiResult.result(OrderEnums.RegularOrderErrorCd.findByCode(regularReqGoodsCycleTermInfo.getRegularErrorCd()));
			}

			// 기간연장횟수
			int termExtensionCnt = regularReqGoodsCycleTermInfo.getTermExtensionCnt();
			// 변경 종료일자
			LocalDate endDate = regularReqGoodsCycleTermInfo.getEndDate();
			// 기존 종료 일자
			LocalDate lastArriveDate = regularReqGoodsCycleTermInfo.getLastArriveDate();
			// 배송주기
			int weeks = OrderEnums.RegularShippingCycle.findByCode(regularCycleDaysInfo.getGoodsCycleTp()).getWeeks();
			// 배송주기코드
			String goodsCycleTp = regularCycleDaysInfo.getGoodsCycleTp();
			// 회차
			int reqRound = 0;

			// 변경 기간 일자가 마지막 도착 예정일 보다 더 작을 경우 변경기간 일자 이 후 데이터 삭제 (ex : 5개월 -> 3개월)
			if(lastArriveDate.isAfter(endDate)) {

				// 1. plusTerm 날짜 이 후 정기배송 결과 상세 데이터 삭제
				// 정기배송 상세 미완료 회차 정보 삭제
				orderRegularDetailService.deleteOrderRegularResultDetl(odRegularReqId, endDate.plusDays(1).toString());
				// 2. plusTerm 날짜 이 후 정기배송 결과 데이터 삭제
				// 정기배송 미완료 회차 정보 삭제
				orderRegularDetailService.deleteOrderRegularResult(odRegularReqId, endDate.plusDays(1).toString());
				// 3. 총 회차 정보 조회
				reqRound = orderRegularDetailService.getOrderRegularResultReqRound(odRegularReqId);
			}
			// 변경 기간 일자가 마지막 도착 예정일 보다 클 경우 도착예정일자 이 후 변경 일자 까지 데이터 추가 (ex : 3개월 -> 6개월)
			else if(lastArriveDate.isBefore(endDate)) {
				// 정기배송 신청 상품 출고처 목록 조회
				List<Long> urWarehouseIds = orderRegularService.getOdRegularUrWarehouseIdsByOdRegularReqId(regularReqBuyerChangeRequestDto.getOdRegularReqId());
				// 스케쥴생성기준일자
				LocalDate schStartStdDate = getWeeksDaysDateInfo(lastArriveDate.plusWeeks(weeks), RegularWeekCd.findByCode(regularCycleDaysInfo.getWeekCd()).getDays());
				List<LocalDate> changeDateList = orderRegularService.getRegularArrivalScheduledDateList(schStartStdDate,
																										RegularShippingCycle.findByCode(goodsCycleTp),
																										endDate,
																										urWarehouseIds);

				// 1. 완료된 건 중 마지막 회차 정보 얻기
				reqRound = orderRegularDetailService.getOrderRegularResultReqRound(odRegularReqId);

				// 2. 신청 상품 정보 얻기
				List<RegularResultReqRoundGoodsListDto> regularReqDetailGoodsList = orderRegularDetailService.getOrderRegularReqDetailGoodsList(odRegularReqId, regularDetlStatusCd);

				// 3. 정기배송 결과 테이블 내 회차 정보 생성
				for(LocalDate arriveDt : changeDateList) {

					reqRound++;
					LocalDate createDt = arriveDt.minusDays(3);
					LocalDate paymentDt = arriveDt.minusDays(2);
					// 정기배송 주문 결과 SEQ 얻기
					long odRegularResultId = orderRegularRegistrationService.getOdRegularResultIdSeq();
					OrderRegularResultVo orderRegularResultVo = new OrderRegularResultVo();
					orderRegularResultVo.setOdRegularResultId(odRegularResultId);	// 정기배송결과PK
					orderRegularResultVo.setOdRegularReqId(odRegularReqId);			// 정기배송신청PK
					orderRegularResultVo.setOdOrderId(0);							// 주문PK
					orderRegularResultVo.setReqRound(reqRound);						// 회차
					orderRegularResultVo.setPaymentFailCnt(0);						// 결제실패건수
					orderRegularResultVo.setOrderCreateDt(createDt.toString());		// 주문생성예정일자
					orderRegularResultVo.setPaymentDt(paymentDt.toString());		// 결제예정일자
					orderRegularResultVo.setArriveDt(arriveDt.toString());			// 도착예정일자
					orderRegularResultVo.setOrderCreateYn("N");						// 주문생성여부
					orderRegularResultVo.setReqRoundYn("N");						// 회차완료여부
					orderRegularResultVo.setRegularStatusCd(regularStatusCd);

					orderRegularRegistrationService.addOrderRegularResult(orderRegularResultVo);

					// 4. 정기배송 상세 테이블 내 상품 정보 생성
					for(RegularResultReqRoundGoodsListDto regularResultReqRoundGoods : regularReqDetailGoodsList) {

						long odRegularResultDetlId = orderRegularRegistrationService.getOdRegularResultDetlIdSeq();
						OrderRegularResultDetlVo orderRegularResultDetlVo = new OrderRegularResultDetlVo();
						orderRegularResultDetlVo.setOdRegularResultDetlId(odRegularResultDetlId);			// 정기배송결과상세PK
						orderRegularResultDetlVo.setOdRegularResultId(odRegularResultId);					// 정기배송결과PK
						orderRegularResultDetlVo.setIlItemCd(regularResultReqRoundGoods.getIlItemCd());		// 품목PK
						orderRegularResultDetlVo.setIlGoodsId(regularResultReqRoundGoods.getIlGoodsId());	// 상품PK
						orderRegularResultDetlVo.setGoodsNm(regularResultReqRoundGoods.getGoodsNm());		// 상품명
						orderRegularResultDetlVo.setOrderCnt(regularResultReqRoundGoods.getOrderCnt());		// 주문수량
						orderRegularResultDetlVo.setReqDetailStatusCd(regularDetlStatusCd);					// 신청상세상태

						orderRegularRegistrationService.addOrderRegularResultDetl(orderRegularResultDetlVo);
					}
				}
			}

			// 3. 정기배송 신청정보 내 총회차 정보 업데이트
			OrderRegularReqVo orderRegularReqVo = new OrderRegularReqVo();
			orderRegularReqVo.setOdRegularReqId(odRegularReqId);	// 정기배송주문신청 PK
			orderRegularReqVo.setTotCnt(reqRound);					// 총 회차 정보
			orderRegularReqVo.setGoodsCycleTermTp(regularReqBuyerChangeRequestDto.getGoodsCycleTermTp());// 주기정보
			orderRegularReqVo.setTermExtensionCnt(termExtensionCnt);				// 기간연장횟수
			orderRegularReqVo.setCreateRoundYn("Y");				// 회차정보 생성여부

			// 4. 정기배송주문신청 업데이트 처리
			orderRegularDetailService.putOrderRegularReq(orderRegularReqVo, "N", "Y");

			String reqGbnCd = RegularReqGbnCd.RC.getCode();
			String reqStatusCd = RegularReqStatusCd.PC.getCode();
			StringBuilder regularReqCont = new StringBuilder();
			String changeTermNm = OrderEnums.RegularShippingCycleTerm.findByCode(regularReqBuyerChangeRequestDto.getGoodsCycleTermTp()).getCodeName();

			regularReqCont.append("배송기간[" + regularCycleDaysInfo.getGoodsCycleTermTpNm() + "] -> [" + changeTermNm + "]");

			// 5. 정기배송주문 히스토리 등록 값 세팅
			long odRegularReqHistoryId = orderRegularRegistrationService.getOdRegularReqHistoryIdSeq();
			OrderRegularReqHistoryVo orderRegularReqHistoryVo = new OrderRegularReqHistoryVo();
			orderRegularReqHistoryVo.setOdRegularReqHistoryId(odRegularReqHistoryId);	// 정기배송주문히스토리 PK
			orderRegularReqHistoryVo.setOdRegularReqId(odRegularReqId);					// 정기배송주문신청 PK
			orderRegularReqHistoryVo.setRegularReqGbnCd(reqGbnCd);						// 변경구분값 공통코드(REGULAR_REQ_GBN_CD)
			orderRegularReqHistoryVo.setRegularReqStatusCd(reqStatusCd);				// 처리상태 공통코드(REGULAR_REQ_STATUS_CD)
			orderRegularReqHistoryVo.setRegularReqCont(regularReqCont.toString());		// 상세내용
			orderRegularReqHistoryVo.setCreateId(urUserId);								// 등록자

			// 6. 정기배송주문 히스토리 등록 처리
			orderRegularRegistrationService.addOrderRegularReqHistory(orderRegularReqHistoryVo);
		}
		// 배송주기, 요일 변경 시
		else {
			int updateCnt = putOrderRegularCycleDays(regularReqBuyerChangeRequestDto.getOdRegularReqId(),
													regularReqBuyerChangeRequestDto.getGoodsCycleTp(),
													regularReqBuyerChangeRequestDto.getWeekCd(),
													urUserId);

			if(updateCnt < 1) {
				ApiResult.result(OrderEnums.RegularOrderErrorCd.GOODS_REQ_INFO_FAIL_NONE);
			}
		}
		return ApiResult.success();
	}

	/**
	 * 정기배송주문신청 내역 배송지 조회
	 * @param odRegularReqId
	 * @return
	 * @throws Exception
	 */
	@Override
	@UserMaskingRun(system = "BOS")
	public ApiResult<?> getOrderRegularReqDetailShippingZone(long odRegularReqId) throws Exception {
		return ApiResult.success(orderRegularDetailService.getOrderRegularReqDetailShippingZone(odRegularReqId));
	}

	/**
	 * 정기배송주문신청 팝업 배송지 정보 조회
	 * @param odRegularReqId
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getOrderRegularReqDetailShippingZonePopup(long odRegularReqId) throws Exception {
		return ApiResult.success(orderRegularDetailService.getOrderRegularReqDetailShippingZone(odRegularReqId));
	}

	/**
	 * 정기배송 배송지 변경 - BOS
	 * @param regularReqShippingZoneDto
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> putOrderRegularReqShippingZone(RegularReqShippingZoneDto regularReqShippingZoneDto) throws Exception {

		UserVo userVo = SessionUtil.getBosUserVO();

		putOrderRegularShippingZone(regularReqShippingZoneDto, Long.parseLong(userVo.getUserId()));

		return ApiResult.success();
	}

	/**
	 * 정기배송 결제정보 얻기
	 * @param regularResultReqRoundGoodsList
	 * @param regularReqPayment
	 * @throws Exception
	 */
	@Override
	public void getOrderRegularPaymentInfo(List<RegularResultReqRoundGoodsListDto> regularResultReqRoundGoodsList, RegularReqPaymentListDto regularReqPayment, long urWarehouseId, int addDiscountStdReqRound) throws Exception {

		// 출고처 별 grouping
		Map<String, List<RegularResultReqRoundGoodsListDto>> resultMap = regularResultReqRoundGoodsList.stream()
				.filter(obj -> obj.getParentIlGoodsId() == 0)
				.collect(groupingBy(RegularResultReqRoundGoodsListDto::getGrpWarehouseShippingTmplId, LinkedHashMap::new, toList()));

		// 부모 상품 PK 별 Group
		Map<Long, List<RegularResultReqRoundGoodsListDto>> childGoodsList = regularResultReqRoundGoodsList.stream()
				.filter(obj -> obj.getParentIlGoodsId() != 0)
				.collect(groupingBy(RegularResultReqRoundGoodsListDto::getParentIlGoodsId, LinkedHashMap::new, toList()));

		resultMap.entrySet().forEach(wareList -> {

			// 기준 출고처PK
			long stdWarehouseId = Long.parseLong(wareList.getKey().split(Constants.ARRAY_SEPARATORS)[0]);

			// 배송비 계산용 변수
			List<Long> ilShippingTtmplIdList = wareList.getValue().stream().map(x -> x.getIlShippingTmplId()).distinct().collect(Collectors.toList());
			int recommendedPrice = 0;
			int goodsPrice = 0;
			int discountPrice = 0;
			int orderCnt = 0;
			int addDiscountPrice = 0;

			// 상품 상세 정보
			List<RegularResultReqRoundGoodsListDto> reqRoundGoodsList = wareList.getValue();

			// 상품 상세 정보 만큼 Loop
			for(RegularResultReqRoundGoodsListDto reqRoundGoodsItem : reqRoundGoodsList) {

				// 상품 금액
				int beforePaymentPrice = reqRoundGoodsItem.getSalePrice();
				// 정기배송 기본 할인 금액
				int afterPaymentPrice = PriceUtil.getPriceByRate(beforePaymentPrice, reqRoundGoodsItem.getBasicDiscountRate());
				// 정기배송 3회차부터는 추가 할인
				if(reqRoundGoodsItem.getReqRound() >= addDiscountStdReqRound) {
					// 할인한 상품금액 정보에 추가 할인 적용
					int addAfterPaymentPrice = PriceUtil.getPriceByRate(afterPaymentPrice, reqRoundGoodsItem.getAdddiscountRate());
					addDiscountPrice += (afterPaymentPrice - addAfterPaymentPrice) * reqRoundGoodsItem.getOrderCnt();
				}

				goodsPrice += beforePaymentPrice * reqRoundGoodsItem.getOrderCnt();
				discountPrice += (beforePaymentPrice - afterPaymentPrice) * reqRoundGoodsItem.getOrderCnt();
				orderCnt += reqRoundGoodsItem.getOrderCnt();
				recommendedPrice += reqRoundGoodsItem.getRecommendedPrice() * reqRoundGoodsItem.getOrderCnt();

				regularReqPayment.setDiscountRate(reqRoundGoodsItem.getBasicDiscountRate());
				regularReqPayment.setAddDiscountRate(reqRoundGoodsItem.getAdddiscountRate());
				regularReqPayment.setAddDiscountReqRound(reqRoundGoodsItem.getAddDiscountRound());

				// 추가 상품이 존재할 경우 추가 상품 금액 정보 얻기
				if(childGoodsList.containsKey(reqRoundGoodsItem.getIlGoodsId())) {
					List<RegularResultReqRoundGoodsListDto> chileItemList = childGoodsList.get(reqRoundGoodsItem.getIlGoodsId());
					// 상품 상세 정보 만큼 Loop
					for(RegularResultReqRoundGoodsListDto childGoodsInfo : chileItemList) {
						goodsPrice += childGoodsInfo.getSalePrice() * childGoodsInfo.getOrderCnt();
						orderCnt += childGoodsInfo.getOrderCnt();
					}
				}
			}

			// 출고처별 배송비 계산
			int shippingPrice = 0;
			try {
				RegularShippingPriceInfoDto shippingPriceInfo = orderRegularService.getShippingPrice(ilShippingTtmplIdList, (goodsPrice - discountPrice - addDiscountPrice), orderCnt, regularReqPayment.getRecvZipCd());
				shippingPrice = shippingPriceInfo.getShippingPrice();
				regularReqPayment.setIlShippingTmplId(shippingPriceInfo.getIlShippingTmplId());
			}
			catch(Exception e) {
				e.printStackTrace();
			}

			regularReqPayment.setRecommendedPrice(recommendedPrice + regularReqPayment.getRecommendedPrice());
			regularReqPayment.setSalePrice(goodsPrice + regularReqPayment.getSalePrice());
			regularReqPayment.setDiscountPrice(discountPrice + regularReqPayment.getDiscountPrice());
			regularReqPayment.setAddDiscountPrice(addDiscountPrice + regularReqPayment.getAddDiscountPrice());
			regularReqPayment.setShippingPrice(shippingPrice + regularReqPayment.getShippingPrice());
			if(urWarehouseId == stdWarehouseId) {
				regularReqPayment.setWarehouseShippingPrice(shippingPrice);
			}
		});

		regularReqPayment.setPaidPrice(regularReqPayment.getSalePrice() - regularReqPayment.getDiscountPrice() - regularReqPayment.getAddDiscountPrice() + regularReqPayment.getShippingPrice());
	}

	/**
	 * 정기배송주문신청 내역 결제정보 조회
	 * @param odRegularReqId
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getOrderRegularReqDetailPayInfo(long odRegularReqId) throws Exception {

		RegularReqPaymentListResponseDto regularReqPaymentListResponse = new RegularReqPaymentListResponseDto();

		// 구매 해지 상품 제외 하기 위한 리스트 Set
		List<String> regularStatusList = new ArrayList<>();
		regularStatusList.add(OrderEnums.RegularStatusCd.APPLY.getCode());
		regularStatusList.add(OrderEnums.RegularStatusCd.ING.getCode());
		// 결제 정보 조회
		List<RegularReqPaymentListDto> regularReqPaymentList = orderRegularDetailService.getOrderRegularReqDetailPaymentList(odRegularReqId, regularStatusList);

		if(!Objects.isNull(regularReqPaymentList)) {

			// 구매 해지 상품 제외 하기 위한 리스트 Set
			List<String> regularDetlStatuCdList = new ArrayList<>();
			regularDetlStatuCdList.add(RegularDetlStatusCd.CANCEL_BUYER.getCode());
			regularDetlStatuCdList.add(RegularDetlStatusCd.CANCEL_SELLER.getCode());

			for(RegularReqPaymentListDto regularReqPayment : regularReqPaymentList) {

				if(!"N".equals(regularReqPayment.getReqRoundYn())) {
					break;
				}
				// 회차 완료 되지 않은 건의 경우 결제 예정금액으로 Set 해줘야 함
				List<RegularResultReqRoundGoodsListDto> regularResultReqRoundGoodsList = orderRegularDetailService.getOrderRegularReqDetailPaymentGoods(
																																						odRegularReqId,
																																						regularReqPayment.getOdRegularResultId(),
																																						regularDetlStatuCdList
																																						);
				// 결제 예정금액 계산용 초기화
				regularReqPayment.setSalePrice(0);
				regularReqPayment.setDiscountPrice(0);
				regularReqPayment.setAddDiscountPrice(0);
				regularReqPayment.setShippingPrice(0);
				regularReqPayment.setPaidPrice(0);

				// 정기배송 추가 할인 정보 조회
				RegularResultGoodsDetailInfoDto regularResultAddDiscountInfo = orderRegularDetailService.getOrderRegularResultAddDiscountInfo(odRegularReqId);
				// 결제정보 얻기
				this.getOrderRegularPaymentInfo(regularResultReqRoundGoodsList, regularReqPayment, 0, regularResultAddDiscountInfo.getAddDiscountStdReqRound());
			}
		}

		regularReqPaymentListResponse.setRows(regularReqPaymentList);

		return ApiResult.success(regularReqPaymentListResponse);
	}

	/**
	 * 정기배송 주문 신청 상담 등록
	 * @param regularReqReqConsultDto
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> addOrderRegularReqDetailConsult(RegularReqReqConsultDto regularReqReqConsultDto) throws Exception {

		UserVo userVo = SessionUtil.getBosUserVO();

		OrderRegularReqMemoVo orderRegularReqMemoVo = new OrderRegularReqMemoVo();
		orderRegularReqMemoVo.setOdRegularReqMemoId(orderRegularRegistrationService.getOdRegularReqMemoIdSeq());
		orderRegularReqMemoVo.setOdRegularReqId(regularReqReqConsultDto.getOdRegularReqId());
		orderRegularReqMemoVo.setMemo(regularReqReqConsultDto.getMemo());
		orderRegularReqMemoVo.setCreateId(Long.parseLong(userVo.getUserId()));

		int insertCnt = orderRegularRegistrationService.addOrderRegularReqMemo(orderRegularReqMemoVo);

		if(insertCnt < 1) {
			return ApiResult.fail();
		}

		return ApiResult.success();
	}

	/**
	 * 정기배송 주문 신청 상담 수정
	 * @param regularReqReqConsultDto
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> putOrderRegularReqDetailConsult(RegularReqReqConsultDto regularReqReqConsultDto) throws Exception {

		UserVo userVo = SessionUtil.getBosUserVO();

		OrderRegularReqMemoVo orderRegularReqMemoVo = new OrderRegularReqMemoVo();
		orderRegularReqMemoVo.setOdRegularReqMemoId(regularReqReqConsultDto.getOdRegularReqMemoId());
		orderRegularReqMemoVo.setMemo(regularReqReqConsultDto.getMemo());
		orderRegularReqMemoVo.setCreateId(Long.parseLong(userVo.getUserId()));

		int updateCnt = orderRegularDetailService.putOrderRegularReqMemo(orderRegularReqMemoVo);

		if(updateCnt < 1) {
			return ApiResult.fail();
		}

		return ApiResult.success();
	}

	/**
	 * 정기배송 주문 신청 상담 삭제
	 * @param regularReqReqConsultDto
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> delOrderRegularReqDetailConsult(RegularReqReqConsultDto regularReqReqConsultDto) throws Exception {

		int delCnt = orderRegularDetailService.delOrderRegularReqMemo(regularReqReqConsultDto);

		if(delCnt < 1) {
			return ApiResult.fail();
		}

		return ApiResult.success();
	}

	/**
	 * 정기배송주문신청 신청상담 목록 조회
	 * @param odRegularReqId
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getOrderRegularReqDetailConsultList(long odRegularReqId) throws Exception {

		List<RegularReqConsultListDto> regularReqConsultList = orderRegularDetailService.getOrderRegularReqDetailConsultList(odRegularReqId);

		RegularReqConsultListResponseDto regularReqConsultListResponseDto = new RegularReqConsultListResponseDto();
		regularReqConsultListResponseDto.setRows(regularReqConsultList);

		return ApiResult.success(regularReqConsultListResponseDto);
	}

	/**
	 * 정기배송주문신청 내역 처리 이력 목록 조회
	 * @param odRegularReqId
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getOrderRegularReqDetailHistoryList(long odRegularReqId) throws Exception {

		List<RegularReqHistoryListDto> regularReqHistoryList = orderRegularDetailService.getOrderRegularReqDetailHistoryList(odRegularReqId, Constants.BATCH_CREATE_USER_ID);
		if(Objects.isNull(regularReqHistoryList)) {
			regularReqHistoryList = new ArrayList<>();
		}

		RegularReqHistoryListResponseDto regularReqHistoryListResponseDto = new RegularReqHistoryListResponseDto();
		regularReqHistoryListResponseDto.setRows(regularReqHistoryList);
		regularReqHistoryListResponseDto.setTotal(regularReqHistoryList.size());

		return ApiResult.success(regularReqHistoryListResponseDto);
	}

	/**
	 * 회차별 상품 목록 조회, 건너뛰기 목록 조회 응답 데이터 생성
	 * @param regularReqRoundGoodsList
	 * @throws Exception
	 */
	private List<RegularReqRoundGoodsListDto> makeOrderRegularReqDetailShippingExpect(List<RegularReqRoundGoodsListResultDto> regularReqRoundGoodsList) throws Exception {

		List<RegularReqRoundGoodsListDto> regularReqRoundGoodsListResponseDto = new ArrayList<> ();

		if(regularReqRoundGoodsList.isEmpty()) {
			return regularReqRoundGoodsListResponseDto;
		}

		Map<Integer, List<RegularReqRoundGoodsListResultDto>> resultMap = regularReqRoundGoodsList.stream()
																							.collect(groupingBy(RegularReqRoundGoodsListResultDto::getReqRound, LinkedHashMap::new, toList()));

		resultMap.entrySet().forEach(reqList -> {

			int reqRound = reqList.getKey();
			RegularReqRoundGoodsListDto regularReqRoundGoodsResponse = new RegularReqRoundGoodsListDto();

			List<RegularReqRoundGoodsListResultDto> goodsList = reqList.getValue();

			if(!goodsList.isEmpty()) {

				regularReqRoundGoodsResponse.setOdRegularReqId(goodsList.get(0).getOdRegularReqId());
				regularReqRoundGoodsResponse.setOdRegularResultId(goodsList.get(0).getOdRegularResultId());
				regularReqRoundGoodsResponse.setOdRegularResultDetlId(goodsList.get(0).getOdRegularResultDetlId());
				regularReqRoundGoodsResponse.setReqRound(reqRound);
				regularReqRoundGoodsResponse.setWeekCdNm(goodsList.get(0).getWeekCdNm());
				regularReqRoundGoodsResponse.setOdid(goodsList.get(0).getOdid());
				regularReqRoundGoodsResponse.setArriveDt(goodsList.get(0).getArriveDt());
				boolean isNextRound = regularReqRoundGoodsListResponseDto.stream().anyMatch(d -> d.isNextRound() == true);
				if(!isNextRound && goodsList.get(0).isNextRound()) {
					regularReqRoundGoodsResponse.setNextRound(true);
				} else {
					regularReqRoundGoodsResponse.setNextRound(false);
				}
				regularReqRoundGoodsResponse.setGoodsList(goodsList);
			}

			regularReqRoundGoodsListResponseDto.add(regularReqRoundGoodsResponse);
		});

		return regularReqRoundGoodsListResponseDto;
	}

	/**
	 * 정기배송 주문 신청 내역 회차별 상품 정보 배송예정내역조회
	 * @param odRegularReqId
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getOrderRegularReqDetailShippingExpect(long odRegularReqId) throws Exception {

		List<RegularReqRoundGoodsListDto> regularReqRoundGoodsList = makeOrderRegularReqDetailShippingExpect(orderRegularDetailService.getOrderRegularReqDetailRoundGoodsList(odRegularReqId, ""));
		RegularReqRoundGoodsListResponseDto regularReqRoundGoodsListResponse = new RegularReqRoundGoodsListResponseDto();

		if(!regularReqRoundGoodsList.isEmpty()) {
			regularReqRoundGoodsListResponse.setTotal(regularReqRoundGoodsList.size());
			regularReqRoundGoodsListResponse.setRows(regularReqRoundGoodsList);
		}

		return ApiResult.success(regularReqRoundGoodsListResponse);
	}

	/**
	 * 정기배송 주문 신청 내역 회차별 상품 정보 건너뛰기내역조회
	 * @param odRegularReqId
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getOrderRegularReqDetailShippingSkip(long odRegularReqId) throws Exception {

		String reqDetailStatusCd = OrderEnums.RegularDetlStatusCd.SKIP.getCode();

		List<RegularReqRoundGoodsListDto> regularReqRoundGoodsList = makeOrderRegularReqDetailShippingExpect(orderRegularDetailService.getOrderRegularReqDetailRoundGoodsList(odRegularReqId, reqDetailStatusCd));
		RegularReqRoundGoodsListResponseDto regularReqRoundGoodsListResponse = new RegularReqRoundGoodsListResponseDto();

		if(!regularReqRoundGoodsList.isEmpty()) {
			regularReqRoundGoodsListResponse.setTotal(regularReqRoundGoodsList.size());
			regularReqRoundGoodsListResponse.setRows(regularReqRoundGoodsList);
		}

		return ApiResult.success(regularReqRoundGoodsListResponse);
	}

	/**
	 * 정기배송상품 상태 변경
	 * @param regularReqRoundGoodsSkipListDtoList
	 * @param reqGhbCd
	 * @param prevStatus
	 * @param chgStatus
	 * @return
	 * @throws Exception
	 */
	private int putOrderRegularReqDetailGoodsStatusChg(RegularReqRoundGoodsSkipListRequestDto regularReqRoundGoodsSkipListDtoList, String reqGhbCd, String prevStatus, String chgStatus) throws Exception {

		int updateCnt = 0;
		UserVo userVo = SessionUtil.getBosUserVO();
		long urUserId = Long.parseLong(userVo.getUserId());
		long odRegularReqId = regularReqRoundGoodsSkipListDtoList.getOdRegularReqId();

		List<RegularReqRoundGoodsSkipListDto> regularReqRoundGoodsSkipList = regularReqRoundGoodsSkipListDtoList.getReqInfoList();
		Map<Long, List<RegularReqRoundGoodsSkipListDto>> groupByResultId = regularReqRoundGoodsSkipList.stream()
				.collect(groupingBy(RegularReqRoundGoodsSkipListDto::getOdRegularResultId, LinkedHashMap::new, toList()));

		//groupByResultId.entrySet().forEach(resultMap -> {
		for (long odRegularResultId : groupByResultId.keySet()) {

			try {

				List<RegularReqRoundGoodsSkipListDto> goodsList = groupByResultId.get(odRegularResultId);
				int reqRound = 0;

				for (RegularReqRoundGoodsSkipListDto goodsItem : goodsList) {
					// 정기배송 상세 정보 가져오기
					RegularResultGoodsDetailInfoDto regularResultGoodsDetailInfo = orderRegularDetailService.getOrderRegularResultDetailGoodsInfo(goodsItem.getOdRegularResultDetlId());

					long ilGoodsId = regularResultGoodsDetailInfo.getIlGoodsId();
					reqRound = regularResultGoodsDetailInfo.getReqRound();

					orderRegularDetailService.putOrderRegularDetailStatusCd(odRegularResultId, prevStatus, chgStatus, ilGoodsId);
				}

				OrderRegularReqHistoryVo orderRegularReqHistoryVo = new OrderRegularReqHistoryVo();
				orderRegularReqHistoryVo.setOdRegularReqHistoryId(orderRegularRegistrationService.getOdRegularReqHistoryIdSeq());    // 정기배송주문히스토리 PK
				orderRegularReqHistoryVo.setOdRegularReqId(odRegularReqId);
				orderRegularReqHistoryVo.setRegularReqGbnCd(reqGhbCd);    // 변경구분값 공통코드(REGULAR_REQ_GBN_CD)
				orderRegularReqHistoryVo.setRegularReqStatusCd(RegularReqStatusCd.PC.getCode());// 처리상태 공통코드(REGULAR_REQ_STATUS_CD)
				orderRegularReqHistoryVo.setRegularReqCont(reqRound + "회차 " + goodsList.size() + "개 상품");    // 상세내용
				orderRegularReqHistoryVo.setCreateId(urUserId);                            // 등록자

				orderRegularRegistrationService.addOrderRegularReqHistory(orderRegularReqHistoryVo);
				updateCnt++;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//}):

		return updateCnt;
	}

	/**
	 * 정기배송 주문 신청 내역 회차별 상품 건너뛰기
	 * @param regularReqRoundGoodsSkipListRequestDto
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> putOrderRegularReqDetailGoodsSkip(RegularReqRoundGoodsSkipListRequestDto regularReqRoundGoodsSkipListRequestDto) throws Exception {

		int updateCnt = putOrderRegularReqDetailGoodsStatusChg(regularReqRoundGoodsSkipListRequestDto,
																RegularReqGbnCd.GS.getCode(),
																RegularDetlStatusCd.APPLY.getCode(),
																RegularDetlStatusCd.SKIP.getCode());

		if(updateCnt < 1) {
			return ApiResult.result(OrderEnums.RegularOrderErrorCd.GOODS_SKIP_FAIL_NONE);
		}

		return ApiResult.success();
	}

	/**
	 * 정기배송 주문 신청 내역 회차별 상품 건너뛰기 철회
	 * @param regularReqRoundGoodsSkipListRequestDto
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> putOrderRegularReqDetailGoodsSkipCancel(RegularReqRoundGoodsSkipListRequestDto regularReqRoundGoodsSkipListRequestDto) throws Exception {

		int updateCnt = putOrderRegularReqDetailGoodsStatusChg(regularReqRoundGoodsSkipListRequestDto,
																RegularReqGbnCd.GSC.getCode(),
																RegularDetlStatusCd.SKIP.getCode(),
																RegularDetlStatusCd.APPLY.getCode());

		if(updateCnt < 1) {
			return ApiResult.result(OrderEnums.RegularOrderErrorCd.GOODS_SKIP_CANCEL_FAIL_NONE);
		}

		long odRegularReqId = regularReqRoundGoodsSkipListRequestDto.getOdRegularReqId();
		String regularStatusCd = OrderEnums.RegularStatusCd.ING.getCode();
		String reqDetailStatusCd = OrderEnums.RegularDetlStatusCd.APPLY.getCode();
		orderRegularDetailService.putOrderRegularResultStatusIng(odRegularReqId, regularStatusCd, reqDetailStatusCd);

		return ApiResult.success();
	}

	/**
	 * 정기배송 주문 신청 내역 회차별 상품 정보 배송내역조회
	 * @param odRegularReqId
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getOrderRegularReqDetailShippingHistory(long odRegularReqId) throws Exception {

		List<RegularReqRoundGoodsListDto> regularReqRoundGoodsList = makeOrderRegularReqDetailShippingExpect(orderRegularDetailService.getOrderRegularReqDetailDeliveryGoodsList(odRegularReqId));
		RegularReqRoundGoodsListResponseDto regularReqRoundGoodsListResponse = new RegularReqRoundGoodsListResponseDto();

		if(!regularReqRoundGoodsList.isEmpty()) {
			regularReqRoundGoodsListResponse.setTotal(regularReqRoundGoodsList.size());
			regularReqRoundGoodsListResponse.setRows(regularReqRoundGoodsList);
		}

		return ApiResult.success(regularReqRoundGoodsListResponse);
	}

	/**
	 * 정기배송 주기 요일 변경 정보 조회
	 * @param odRegularReqId
	 * @return
	 * @throws Exception
	 */
	@Override
	public RegularReqCycleDaysInfoResponseDto getOrderRegularDaysInfo(long odRegularReqId) throws Exception {

		// 정기배송 신청 주기, 요일 정보 조회
		RegularReqCycleDaysInfoResponseDto regularCycleDaysInfo = orderRegularDetailService.getOrderRegularCycleDaysInfo(odRegularReqId);
		// 정기배송 도착 에정일 목록 조회
		List<RegularReqArriveDtListDto> arrideDtList = orderRegularDetailService.getOrderRegularArriveDays(odRegularReqId);
		regularCycleDaysInfo.setArriveDtList(arrideDtList);
		// 휴일 목록 조회
		GetHolidayListResponseDto holidayListResponse = (GetHolidayListResponseDto) policyHolidayBiz.getHolidayList().getData();
		List<GetHolidayListResultVo> holidayList = holidayListResponse.getRows();
		regularCycleDaysInfo.setHolidayList(holidayList);

		// 앞으로 남은 회차 정보 내 상품 스킵 정보 있는지 조회
		regularCycleDaysInfo.setExistingSkipYn("N");
		String reqDetailStatusCd = RegularDetlStatusCd.SKIP.getCode();
		RegularResultNextReqRoundSkipResultDto regularResultNextReqRoundSkipResult = orderRegularDetailService.getOrderRegularResultNextGoodsSkipInfo(odRegularReqId, reqDetailStatusCd);
		if(!Objects.isNull(regularResultNextReqRoundSkipResult)) {
			if(regularResultNextReqRoundSkipResult.getNextSkipCnt() > 0) {
				regularCycleDaysInfo.setExistingSkipYn("Y");
			}
		}

		return regularCycleDaysInfo;
	}

	/**
	 * 해당 주의 해당 요일 정보 얻기
	 * @param stdDt
	 * @return
	 */
	private LocalDate getWeeksDaysDateInfo(LocalDate stdDt, DayOfWeek dayInfo) {
		LocalDate startDt = stdDt;
		// 4일뒤의 날짜부터 선택한 요일에 해당하는 날짜를 찾는다
		while(!dayInfo.equals(startDt.getDayOfWeek())) {
			startDt = startDt.minusDays(1);
		}

		return startDt;
	}

	/**
	 * 요일 기준 시작일자 조회
	 * @param weekCd
	 * @return
	 */
	private LocalDate getStartDate(LocalDate stdDt, String weekCd, int plusDays) {
		LocalDate startDt = stdDt;
		startDt = startDt.plusDays(plusDays);

		DayOfWeek dw = RegularWeekCd.findByCode(weekCd).getDays();
		// 4일뒤의 날짜부터 선택한 요일에 해당하는 날짜를 찾는다
		while(dw != startDt.getDayOfWeek()) {
			startDt = startDt.plusDays(1);
		}

		return startDt;
	}

	/**
	 * 정기배송 주기 요일 변경 도착일 목록 조회
	 * @param odRegularReqId
	 * @param goodsCycleTp
	 * @param weekCd
	 * @return
	 * @throws Exception
	 */
	@Override
	public RegularReqCycleDaysInfoResponseDto getOrderRegularArriveDtList(long odRegularReqId, String goodsCycleTp, String weekCd) throws Exception {

		// 1. 기존 정기배송 신청 정보 조회
		RegularReqCycleDaysInfoResponseDto regularCycleDaysInfo = orderRegularDetailService.getOrderRegularCycleDaysInfo(odRegularReqId);

		// 2. 현재 일자 조회
		LocalDate startDt = getStartDate(LocalDate.now(), weekCd, 4); // 오늘의 4일 뒤부터 변경 가능, 주문 생성, 결제가 3일전에 만들어지므로 최소 4일은 지나야 가능 ..

		// 3. 해당 주문 신청 건의 마지막 회차 도착 예정일 조회
		RegularResultLastInfoDto regularResultLastInfo = orderRegularDetailService.getRegularResultLastOrderArriveDt(odRegularReqId);
		// 2주에서 1주로 변경시 마지막 일자가 1주 빠지게 되어 첫 배송일자에서 다시 구해야함
//		LocalDate endDt = regularResultLastInfo.getArriveDt(); -> RegularShippingCycleTerm.findByCode(regularCycleDaysInfo.getGoodsCycleTermTp()) 로 변경


		// 정기배송 신청 상품 출고처 목록 조회
		List<Long> urWarehouseIds = orderRegularService.getOdRegularUrWarehouseIdsByOdRegularReqId(odRegularReqId);

		// 4. 변경 도착 예정일을 가져온다.
		List<LocalDate> changeDateList = new ArrayList<>();
		if(!regularCycleDaysInfo.getWeekCd().equals(weekCd) && regularCycleDaysInfo.getGoodsCycleTp().equals(goodsCycleTp)) {
			// 회차 기준으로 배송 에정일자 조회
			changeDateList = orderRegularService.getRegularArrivalScheduledDateListByReqRound(startDt,
																								RegularShippingCycle.findByCode(goodsCycleTp),
																								regularCycleDaysInfo.getReqRound(),
																								regularResultLastInfo.getReqRound(),
																								urWarehouseIds);
		}
		else {
			// 첫 배송일 기준 마지막 배송 예정일자 기준으로 가져온다
			LocalDate stdDt = getStartDate(regularCycleDaysInfo.getDeliveryDt(), weekCd, 0);
			// 현재 일자 기준 선택 가능일자가 첫 배송일자보다 작을 경우 기준 일자는 현재 일자 기준 선택 가능일자로 변경
			if(startDt.isBefore(regularCycleDaysInfo.getDeliveryDt())) {
				stdDt = startDt;
			}
			changeDateList = orderRegularService.getRegularArrivalScheduledDateList(stdDt,
																					RegularShippingCycle.findByCode(goodsCycleTp),
																					RegularShippingCycleTerm.findByCode(regularCycleDaysInfo.getGoodsCycleTermTp()),
																					urWarehouseIds);
		}

		List<RegularReqArriveDtListDto> arriveDtList = new ArrayList<>();
		for(LocalDate changeDate : changeDateList) {
			RegularReqArriveDtListDto regularReqArriveDt = new RegularReqArriveDtListDto();
			regularReqArriveDt.setArriveDt(changeDate);
			arriveDtList.add(regularReqArriveDt);
		}

		RegularReqCycleDaysInfoResponseDto changeRegularCycleDaysInfo = new RegularReqCycleDaysInfoResponseDto();
		// 회차 완료 여부가 N 일 경우
		if("N".equals(regularCycleDaysInfo.getReqRoundYn())) {
			// 변경 회차 정보는 현재 회차 - 1 + 변경 도착 예정일자 Size
			int changeReqRound = regularCycleDaysInfo.getReqRound() - 1 + arriveDtList.size();
			changeRegularCycleDaysInfo.setReqRound(changeReqRound);

			if(!arriveDtList.isEmpty()) {
				changeRegularCycleDaysInfo.setNextDeliveryDt(changeDateList.get(0));
			}
		}

		changeRegularCycleDaysInfo.setGoodsCycleTp(regularCycleDaysInfo.getGoodsCycleTp());
		changeRegularCycleDaysInfo.setGoodsCycleTpNm(regularCycleDaysInfo.getGoodsCycleTpNm());
		changeRegularCycleDaysInfo.setChangeGoodsCycleTp(goodsCycleTp);
		changeRegularCycleDaysInfo.setChangeGoodsCycleTpNm(RegularShippingCycle.findByCode(goodsCycleTp).getCodeName());
		changeRegularCycleDaysInfo.setWeekCd(regularCycleDaysInfo.getWeekCd());
		changeRegularCycleDaysInfo.setWeekCdNm(regularCycleDaysInfo.getWeekCdNm());
		changeRegularCycleDaysInfo.setChangeWeekCd(weekCd);
		changeRegularCycleDaysInfo.setChangeWeekCdNm(RegularWeekCd.findByCode(weekCd).getCodeName());

		changeRegularCycleDaysInfo.setArriveDtList(arriveDtList);

		return changeRegularCycleDaysInfo;
	}

	/**
	 * 정기배송 주기 요일 변경
	 * @param odRegularReqId
	 * @param goodsCycleTp
	 * @param weekCd
	 * @return
	 * @throws Exception
	 */
	@Override
	public int putOrderRegularCycleDays(long odRegularReqId, String goodsCycleTp, String weekCd, long urUserId) throws Exception {

		// 변경 날짜 정보를 얻어온다
		RegularReqCycleDaysInfoResponseDto getOrderRegularArriveDtList = getOrderRegularArriveDtList(odRegularReqId, goodsCycleTp, weekCd);
		List<RegularReqArriveDtListDto> arriveDtList = getOrderRegularArriveDtList.getArriveDtList();
		int updateCnt = 0;

		// 변경 날짜 정보가 존재할 경우
		if(!arriveDtList.isEmpty()) {

			// 정기배송상태코드
			String regularStatusCd = RegularStatusCd.APPLY.getCode();
			// 정기배송상세상태코드
			String regularDetlStatusCd = RegularDetlStatusCd.APPLY.getCode();

			// 1. 기존 정기배송 결과 및 상세 테이블 내 회차 완료 되지 않은 정보 삭제
			//String standArriveDt = getStartDate(LocalDate.now(), weekCd, 4).toString();
			String standArriveDt = LocalDate.now().plusDays(4).toString();
			// 정기배송 상세 미완료 회차 정보 삭제
			orderRegularDetailService.deleteOrderRegularResultDetl(odRegularReqId, standArriveDt);
			// 정기배송 미완료 회차 정보 삭제
			orderRegularDetailService.deleteOrderRegularResult(odRegularReqId, standArriveDt);

			// 2. 완료된 건 중 마지막 회차 정보 얻기
			int reqRound = orderRegularDetailService.getOrderRegularResultReqRound(odRegularReqId);

			// 3. 신청 상품 정보 얻기
			List<RegularResultReqRoundGoodsListDto> regularReqDetailGoodsList = orderRegularDetailService.getOrderRegularReqDetailGoodsList(odRegularReqId, regularDetlStatusCd);

			// 4. 정기배송 결과 테이블 내 회차 정보 생성
			for(RegularReqArriveDtListDto regularReqArriveDt : arriveDtList) {

				reqRound++;
				LocalDate arriveDt = regularReqArriveDt.getArriveDt();
				LocalDate createDt = arriveDt.minusDays(3);
				LocalDate paymentDt = arriveDt.minusDays(2);
				// 정기배송 주문 결과 SEQ 얻기
				long odRegularResultId = orderRegularRegistrationService.getOdRegularResultIdSeq();
				OrderRegularResultVo orderRegularResultVo = new OrderRegularResultVo();
				orderRegularResultVo.setOdRegularResultId(odRegularResultId);	// 정기배송결과PK
				orderRegularResultVo.setOdRegularReqId(odRegularReqId);			// 정기배송신청PK
				orderRegularResultVo.setOdOrderId(0);							// 주문PK
				orderRegularResultVo.setReqRound(reqRound);						// 회차
				orderRegularResultVo.setPaymentFailCnt(0);						// 결제실패건수
				orderRegularResultVo.setOrderCreateDt(createDt.toString());		// 주문생성예정일자
				orderRegularResultVo.setPaymentDt(paymentDt.toString());		// 결제예정일자
				orderRegularResultVo.setArriveDt(arriveDt.toString());			// 도착예정일자
				orderRegularResultVo.setOrderCreateYn("N");						// 주문생성여부
				orderRegularResultVo.setReqRoundYn("N");						// 회차완료여부
				orderRegularResultVo.setRegularStatusCd(regularStatusCd);

				orderRegularRegistrationService.addOrderRegularResult(orderRegularResultVo);

				// 4. 정기배송 상세 테이블 내 상품 정보 생성
				for(RegularResultReqRoundGoodsListDto regularResultReqRoundGoods : regularReqDetailGoodsList) {

					long odRegularResultDetlId = orderRegularRegistrationService.getOdRegularResultDetlIdSeq();
					OrderRegularResultDetlVo orderRegularResultDetlVo = new OrderRegularResultDetlVo();
					orderRegularResultDetlVo.setOdRegularResultDetlId(odRegularResultDetlId);			// 정기배송결과상세PK
					orderRegularResultDetlVo.setOdRegularResultId(odRegularResultId);					// 정기배송결과PK
					orderRegularResultDetlVo.setIlItemCd(regularResultReqRoundGoods.getIlItemCd());		// 품목PK
					orderRegularResultDetlVo.setIlGoodsId(regularResultReqRoundGoods.getIlGoodsId());	// 상품PK
					orderRegularResultDetlVo.setGoodsNm(regularResultReqRoundGoods.getGoodsNm());		// 상품명
					orderRegularResultDetlVo.setOrderCnt(regularResultReqRoundGoods.getOrderCnt());		// 주문수량
					orderRegularResultDetlVo.setReqDetailStatusCd(regularDetlStatusCd);					// 신청상세상태
					orderRegularResultDetlVo.setSaleStatus(OrderEnums.RegularDetlSaleStatusCd.ON_SALE.getCode());	// 상품 판매상태

					orderRegularRegistrationService.addOrderRegularResultDetl(orderRegularResultDetlVo);
				}

				updateCnt++;
			}

			// 5. 정기배송 신청정보 내 총회차 정보 업데이트
			OrderRegularReqVo orderRegularReqVo = new OrderRegularReqVo();
			orderRegularReqVo.setOdRegularReqId(odRegularReqId);	// 정기배송주문신청 PK
			orderRegularReqVo.setTotCnt(reqRound);					// 총 회차 정보
			orderRegularReqVo.setGoodsCycleTp(goodsCycleTp);		// 주기정보
			orderRegularReqVo.setWeekCd(weekCd);					// 요일정보
			orderRegularReqVo.setCreateRoundYn("Y");				// 회차정보 생성여부

			// 6. 정기배송주문신청 업데이트 처리
			orderRegularDetailService.putOrderRegularReq(orderRegularReqVo, "N", "N");

			String reqGbnCd = RegularReqGbnCd.RC.getCode();
			String reqStatusCd = RegularReqStatusCd.PC.getCode();
			StringBuilder regularReqCont = new StringBuilder();

			regularReqCont.append("배송주기[" + getOrderRegularArriveDtList.getGoodsCycleTpNm() + "] -> [" + getOrderRegularArriveDtList.getChangeGoodsCycleTpNm() + "],");
			regularReqCont.append("요일[" + getOrderRegularArriveDtList.getWeekCdNm() + "] -> [" + getOrderRegularArriveDtList.getChangeWeekCdNm() + "]");

			// 7. 정기배송주문 히스토리 등록 값 세팅
			long odRegularReqHistoryId = orderRegularRegistrationService.getOdRegularReqHistoryIdSeq();
			OrderRegularReqHistoryVo orderRegularReqHistoryVo = new OrderRegularReqHistoryVo();
			orderRegularReqHistoryVo.setOdRegularReqHistoryId(odRegularReqHistoryId);	// 정기배송주문히스토리 PK
			orderRegularReqHistoryVo.setOdRegularReqId(odRegularReqId);					// 정기배송주문신청 PK
			orderRegularReqHistoryVo.setRegularReqGbnCd(reqGbnCd);						// 변경구분값 공통코드(REGULAR_REQ_GBN_CD)
			orderRegularReqHistoryVo.setRegularReqStatusCd(reqStatusCd);				// 처리상태 공통코드(REGULAR_REQ_STATUS_CD)
			orderRegularReqHistoryVo.setRegularReqCont(regularReqCont.toString());		// 상세내용
			orderRegularReqHistoryVo.setCreateId(urUserId);								// 등록자

			// 8. 정기배송주문 히스토리 등록 처리
			orderRegularRegistrationService.addOrderRegularReqHistory(orderRegularReqHistoryVo);
		}

		return updateCnt;
	}

	/**
	 * 정기배송 배송지 변경
	 * @param regularReqShippingZoneDto
	 * @throws Exception
	 */
	@Override
	public void putOrderRegularShippingZone(RegularReqShippingZoneDto regularReqShippingZoneDto, long urUserId) throws Exception {

		// 이전 배송지 조회
		RegularReqShippingZoneDto prevShippingZone = orderRegularDetailService.getOrderRegularReqDetailShippingZone(regularReqShippingZoneDto.getOdRegularReqId());

		// 배송지 업데이트
		orderRegularDetailService.putOrderRegularShippingZone(regularReqShippingZoneDto);

		StringBuilder regularReqCont = new StringBuilder();

		// 수령인명 변경
		if(StringUtil.isNotEmpty(regularReqShippingZoneDto.getRecvNm()) && !prevShippingZone.getRecvNm().equals(regularReqShippingZoneDto.getRecvNm())) {
			regularReqCont.append("수령인명 [" + prevShippingZone.getRecvNm() + "] -> [" + regularReqShippingZoneDto.getRecvNm() + "], " + System.getProperty("line.separator"));
		}
		// 수령인핸드폰 변경
		if(StringUtil.isNotEmpty(regularReqShippingZoneDto.getRecvHp()) && !prevShippingZone.getRecvHp().equals(regularReqShippingZoneDto.getRecvHp())) {
			regularReqCont.append("수령인핸드폰 [" + prevShippingZone.getRecvHp() + "] -> [" + regularReqShippingZoneDto.getRecvHp() + "], " + System.getProperty("line.separator"));
		}
		// 수령인우편번호 변경
		if(StringUtil.isNotEmpty(regularReqShippingZoneDto.getRecvZipCd()) && !prevShippingZone.getRecvZipCd().equals(regularReqShippingZoneDto.getRecvZipCd())) {
			regularReqCont.append("수령인우편번호 [" + prevShippingZone.getRecvZipCd() + "] -> [" + regularReqShippingZoneDto.getRecvZipCd() + "], " + System.getProperty("line.separator"));
		}
		// 수령인주소1 변경
		if(StringUtil.isNotEmpty(regularReqShippingZoneDto.getRecvAddr1()) && !prevShippingZone.getRecvAddr1().equals(regularReqShippingZoneDto.getRecvAddr1())) {
			regularReqCont.append("수령인주소1 [" + prevShippingZone.getRecvAddr1() + "] -> [" + regularReqShippingZoneDto.getRecvAddr1() + "], " + System.getProperty("line.separator"));
		}
		// 수령인주소2 변경
		if(StringUtil.isNotEmpty(regularReqShippingZoneDto.getRecvAddr2()) && !prevShippingZone.getRecvAddr2().equals(regularReqShippingZoneDto.getRecvAddr2())) {
			regularReqCont.append("수령인주소2 [" + prevShippingZone.getRecvAddr2() + "] -> [" + regularReqShippingZoneDto.getRecvAddr2() + "], " + System.getProperty("line.separator"));
		}
		// 빌딩번호 변경
		if(StringUtil.isNotEmpty(regularReqShippingZoneDto.getRecvBldNo()) && !prevShippingZone.getRecvBldNo().equals(regularReqShippingZoneDto.getRecvBldNo())) {
			regularReqCont.append("빌딩번호 [" + prevShippingZone.getRecvBldNo() + "] -> [" + regularReqShippingZoneDto.getRecvBldNo() + "], " + System.getProperty("line.separator"));
		}
		// 출입정보타입 변경
		if(StringUtil.isNotEmpty(regularReqShippingZoneDto.getDoorMsgCd()) && !prevShippingZone.getDoorMsgCd().equals(regularReqShippingZoneDto.getDoorMsgCd())) {
			String doorMsgCdNm = "";
			OrderEnums.AccessInformation accessInformation = OrderEnums.AccessInformation.findByCode(StringUtil.nvl(regularReqShippingZoneDto.getDoorMsgCd(), ""));
			if(ObjectUtils.isNotEmpty(accessInformation)) {
				doorMsgCdNm = accessInformation.getCodeName();
			}
			regularReqCont.append("출입정보타입 [" + prevShippingZone.getDoorMsgCdNm() + "] -> [" + doorMsgCdNm + "], " + System.getProperty("line.separator"));
		}
		// 출입정보 변경
		if(StringUtil.isNotEmpty(regularReqShippingZoneDto.getDoorMsg()) && !prevShippingZone.getDoorMsg().equals(regularReqShippingZoneDto.getDoorMsg())) {
			regularReqCont.append("출입정보 [" + prevShippingZone.getDoorMsg() + "] -> [" + regularReqShippingZoneDto.getDoorMsg() + "], " + System.getProperty("line.separator"));
		}
		// 배송요청사항 변경
		if(StringUtil.isNotEmpty(regularReqShippingZoneDto.getDeliveryMsg()) && !prevShippingZone.getDeliveryMsg().equals(regularReqShippingZoneDto.getDeliveryMsg())) {
			regularReqCont.append("배송요청사항 [" + prevShippingZone.getDeliveryMsg() + "] -> [" + regularReqShippingZoneDto.getDeliveryMsg() + "], " + System.getProperty("line.separator"));
		}
		// 배송요청사항 코드 변경
		if(StringUtil.isNotEmpty(regularReqShippingZoneDto.getDeliveryMsgCd()) && !prevShippingZone.getDeliveryMsgCd().equals(regularReqShippingZoneDto.getDeliveryMsgCd())) {
			String deliveryMsgCdNm = "";
			OrderEnums.DeliveryMsgType deliveryMsgType = OrderEnums.DeliveryMsgType.findByCode(StringUtil.nvl(regularReqShippingZoneDto.getDeliveryMsgCd(), ""));
			if(ObjectUtils.isNotEmpty(deliveryMsgType)) {
				deliveryMsgCdNm = deliveryMsgType.getCodeName();
			}
			regularReqCont.append("배송요청사항코드 [" + prevShippingZone.getDeliveryMsgCdNm() + "] -> [" + deliveryMsgCdNm + "], " + System.getProperty("line.separator"));
		}
		// 배송출입 기타 직접 입력 메시지 변경
		if(StringUtil.isNotEmpty(regularReqShippingZoneDto.getDoorEtc()) && !prevShippingZone.getDoorEtc().equals(regularReqShippingZoneDto.getDoorEtc())) {
			regularReqCont.append("배송출입 직접입력 메시지 [" + prevShippingZone.getDoorEtc() + "] -> [" + regularReqShippingZoneDto.getDoorEtc() + "], " + System.getProperty("line.separator"));
		}

		if(regularReqCont.toString().length() > 0) {

			// 정기배송주문 히스토리 등록 값 세팅
			long odRegularReqHistoryId = orderRegularRegistrationService.getOdRegularReqHistoryIdSeq();
			OrderRegularReqHistoryVo orderRegularReqHistoryVo = new OrderRegularReqHistoryVo();
			orderRegularReqHistoryVo.setOdRegularReqHistoryId(odRegularReqHistoryId);	// 정기배송주문히스토리 PK
			orderRegularReqHistoryVo.setOdRegularReqId(regularReqShippingZoneDto.getOdRegularReqId());	// 정기배송주문신청 PK
			orderRegularReqHistoryVo.setRegularReqGbnCd(RegularReqGbnCd.DC.getCode());					// 변경구분값 공통코드(REGULAR_REQ_GBN_CD)
			orderRegularReqHistoryVo.setRegularReqStatusCd(RegularReqStatusCd.PC.getCode());			// 처리상태 공통코드(REGULAR_REQ_STATUS_CD)
			orderRegularReqHistoryVo.setRegularReqCont(regularReqCont.toString().substring(0, regularReqCont.toString().lastIndexOf(',')));		// 상세내용
			orderRegularReqHistoryVo.setCreateId(urUserId);								// 등록자

			// 정기배송주문 히스토리 등록 처리
			orderRegularRegistrationService.addOrderRegularReqHistory(orderRegularReqHistoryVo);
		}
	}

	/**
	 * 정기배송 기간연장
	 * @param odRegularReqId
	 * @param urUserId
	 * @throws Exception
	 */
	@Override
	public int putOrderRegularGoodsCycleTermExtension(long odRegularReqId, long urUserId) throws Exception {

		// 1. 해당 주문 신청 건 배송 기간, 배송주기, 요일정보 얻기
		RegularReqListDto orderRegularReqInfo = orderRegularDetailService.getOrderRegularReqInfo(odRegularReqId);

		int insertCnt = 0;
		int goodsCycleTermValue = RegularShippingCycleTerm.findByCode(orderRegularReqInfo.getGoodsCycleTermTp()).getMonth();
		int goodsCycleValue = RegularShippingCycle.findByCode(orderRegularReqInfo.getGoodsCycleTp()).getWeeks();
		int termExtension = (orderRegularReqInfo.getTermExtensionCnt() + 1);
		String weekCd = orderRegularReqInfo.getWeekCd();
		String regularStatusCd = RegularStatusCd.APPLY.getCode();
		String regularDetlStatusCd = RegularDetlStatusCd.APPLY.getCode();

		// 2. 해당 주문 신청 건의 마지막 회차 주문 생성 예정일 조회
		RegularResultLastInfoDto regularResultLastInfo = orderRegularDetailService.getRegularResultLastOrderArriveDt(odRegularReqId);
		LocalDate lastOrderCreateDt = regularResultLastInfo.getArriveDt();
		int reqRound = regularResultLastInfo.getReqRound();

		// 3. 마지막 회차 주문 생성 예정일 + 배송 주기 = 시작일자
		LocalDate startDate = getStartDate(getWeeksDaysDateInfo(lastOrderCreateDt.plusWeeks(goodsCycleValue), DayOfWeek.MONDAY), weekCd, 0);

		// 4. 종료일 계산
		//LocalDate endDate = lastOrderCreateDt.plusMonths(goodsCycleTermValue);
		LocalDate endDate = lastOrderCreateDt.plusWeeks(goodsCycleTermValue * 4);

		// 정기배송 신청 상품 출고처 목록 조회
		List<Long> urWarehouseIds = orderRegularService.getOdRegularUrWarehouseIdsByOdRegularReqId(odRegularReqId);

		// 5. 추가 시작일자 ~ 추가 종료일자 기간 내 배송 주기별 날짜 정보 얻기
		List<LocalDate> arriveDtList = orderRegularService.getRegularArrivalScheduledDateList(startDate,
																								RegularShippingCycle.findByCode(orderRegularReqInfo.getGoodsCycleTp()),
																								endDate,
																								urWarehouseIds);

		// 6. 정기배송 신청 상품 목록 조회
		List<RegularResultReqRoundGoodsListDto> regularReqDetailGoodsList = orderRegularDetailService.getOrderRegularReqDetailGoodsList(odRegularReqId, regularDetlStatusCd);

		// 7. 회차정보 생성
		for(LocalDate arriveDt : arriveDtList) {

			reqRound++;
			LocalDate createDt = arriveDt.minusDays(3);
			LocalDate paymentDt = arriveDt.minusDays(2);
			// 8. 정기배송 주문 결과 SEQ 얻기
			long odRegularResultId = orderRegularRegistrationService.getOdRegularResultIdSeq();
			OrderRegularResultVo orderRegularResultVo = new OrderRegularResultVo();
			orderRegularResultVo.setOdRegularResultId(odRegularResultId);	// 정기배송결과PK
			orderRegularResultVo.setOdRegularReqId(odRegularReqId);			// 정기배송신청PK
			orderRegularResultVo.setOdOrderId(0);							// 주문PK
			orderRegularResultVo.setReqRound(reqRound);						// 회차
			orderRegularResultVo.setPaymentFailCnt(0);						// 결제실패건수
			orderRegularResultVo.setOrderCreateDt(createDt.toString());		// 주문생성예정일자
			orderRegularResultVo.setPaymentDt(paymentDt.toString());		// 결제예정일자
			orderRegularResultVo.setArriveDt(arriveDt.toString());			// 도착예정일자
			orderRegularResultVo.setOrderCreateYn("N");						// 주문생성여부
			orderRegularResultVo.setReqRoundYn("N");						// 회차완료여부
			orderRegularResultVo.setRegularStatusCd(regularStatusCd);

			orderRegularRegistrationService.addOrderRegularResult(orderRegularResultVo);

			// 9. 정기배송 상세 테이블 내 상품 정보 생성
			for(RegularResultReqRoundGoodsListDto regularResultReqRoundGoods : regularReqDetailGoodsList) {

				long odRegularResultDetlId = orderRegularRegistrationService.getOdRegularResultDetlIdSeq();
				OrderRegularResultDetlVo orderRegularResultDetlVo = new OrderRegularResultDetlVo();
				orderRegularResultDetlVo.setOdRegularResultDetlId(odRegularResultDetlId);			// 정기배송결과상세PK
				orderRegularResultDetlVo.setOdRegularResultId(odRegularResultId);					// 정기배송결과PK
				orderRegularResultDetlVo.setIlItemCd(regularResultReqRoundGoods.getIlItemCd());		// 품목PK
				orderRegularResultDetlVo.setIlGoodsId(regularResultReqRoundGoods.getIlGoodsId());	// 상품PK
				orderRegularResultDetlVo.setGoodsNm(regularResultReqRoundGoods.getGoodsNm());		// 상품명
				orderRegularResultDetlVo.setOrderCnt(regularResultReqRoundGoods.getOrderCnt());		// 주문수량
				orderRegularResultDetlVo.setReqDetailStatusCd(regularDetlStatusCd);					// 신청상세상태

				orderRegularRegistrationService.addOrderRegularResultDetl(orderRegularResultDetlVo);
			}
			insertCnt++;
		}

		// 10. 정기배송 신청정보 내 총회차 정보 업데이트
		OrderRegularReqVo orderRegularReqVo = new OrderRegularReqVo();
		orderRegularReqVo.setOdRegularReqId(odRegularReqId);	// 정기배송주문신청 PK
		orderRegularReqVo.setTotCnt(reqRound);					// 총 회차 정보

		// 11. 정기배송주문신청 업데이트 처리
		orderRegularDetailService.putOrderRegularReq(orderRegularReqVo, "Y", "N");

		String reqGbnCd = RegularReqGbnCd.TE.getCode();
		String reqStatusCd = RegularReqStatusCd.PC.getCode();
		StringBuilder regularReqCont = new StringBuilder();
		regularReqCont.append("신청기간 : " + (termExtension * goodsCycleTermValue) + "개월 -> 연장 후 신청기간 : " + ((termExtension + 1) * goodsCycleTermValue) + "개월");

//		if(!regularReqDetailGoodsList.isEmpty()) {
//			regularReqCont.append(regularReqDetailGoodsList.get(0).getGoodsNm());
//			if(regularReqDetailGoodsList.size() > 1) {
//				regularReqCont.append(" 외 " + (regularReqDetailGoodsList.size() - 1) + "개 상품");
//			}
//		}

		// 12. 이력 등록 처리
		long odRegularReqHistoryId = orderRegularRegistrationService.getOdRegularReqHistoryIdSeq();
		OrderRegularReqHistoryVo orderRegularReqHistoryVo = new OrderRegularReqHistoryVo();
		orderRegularReqHistoryVo.setOdRegularReqHistoryId(odRegularReqHistoryId);	// 정기배송주문히스토리 PK
		orderRegularReqHistoryVo.setOdRegularReqId(odRegularReqId);					// 정기배송주문신청 PK
		orderRegularReqHistoryVo.setRegularReqGbnCd(reqGbnCd);						// 변경구분값 공통코드(REGULAR_REQ_GBN_CD)
		orderRegularReqHistoryVo.setRegularReqStatusCd(reqStatusCd);				// 처리상태 공통코드(REGULAR_REQ_STATUS_CD)
		orderRegularReqHistoryVo.setRegularReqCont(regularReqCont.toString());		// 상세내용
		orderRegularReqHistoryVo.setCreateId(urUserId);								// 등록자

		// 13. 정기배송주문 히스토리 등록 처리
		orderRegularRegistrationService.addOrderRegularReqHistory(orderRegularReqHistoryVo);

		return insertCnt;
	}

	/**
	 * 정기배송 상품 취소/건너뛰기 정보 조회
	 * @param odRegularResultDetlId
	 * @return
	 * @throws Exception
	 */
	@Override
	public RegularReqGoodsSkipCancelResponseDto getOrderRegularGoodsSkipCancelInfo(long odRegularResultDetlId) throws Exception {

		// 정기배송 상세 정보 가져오기
		RegularResultGoodsDetailInfoDto regularResultGoodsDetailInfo = orderRegularDetailService.getOrderRegularResultDetailGoodsInfo(odRegularResultDetlId);

		long odRegularResultId = regularResultGoodsDetailInfo.getOdRegularResultId();
		long ilGoodsId = regularResultGoodsDetailInfo.getIlGoodsId();
		long urWarehouseId = regularResultGoodsDetailInfo.getUrWarehouseId();
		int addDiscountStdReqRound = regularResultGoodsDetailInfo.getAddDiscountStdReqRound();	// 추가할인 기준 회차

		// 회차 완료 여부가 "Y" 일 경우 다음 회차의 결제 예정 정보 조회
		if("Y".equals(regularResultGoodsDetailInfo.getReqRoundYn())) {
			// 선택한 상품의 회차 이 후 결제 예정 회차 정보의 정기배송결과 PK를 조회한다
			RegularResultGoodsDetailInfoDto nextRegularInfo = orderRegularDetailService.getOrderRegularResultNextResultId(regularResultGoodsDetailInfo);
			if(ObjectUtils.isNotEmpty(nextRegularInfo)) {
				odRegularResultId = nextRegularInfo.getOdRegularResultId();
			}
		}

		// 해당 회차의 진행중인 모든 상품의 결제 정보
		RegularReqPaymentListDto regularReqPayment = new RegularReqPaymentListDto();

		// 구매 해지 상품 제외 하기 위한 리스트 Set
		List<String> regularDetlStatuCdList = new ArrayList<>();
		regularDetlStatuCdList.add(RegularDetlStatusCd.CANCEL_BUYER.getCode());
		regularDetlStatuCdList.add(RegularDetlStatusCd.CANCEL_SELLER.getCode());
		// 회차 완료 되지 않은 건의 경우 결제 예정금액으로 Set 해줘야 함
		List<RegularResultReqRoundGoodsListDto> regularResultReqRoundGoodsList = orderRegularDetailService.getOrderRegularReqDetailPaymentGoods(
																																				0,
																																				odRegularResultId,
																																				regularDetlStatuCdList
																																				);
		// 결제정보 얻기
		this.getOrderRegularPaymentInfo(regularResultReqRoundGoodsList, regularReqPayment, urWarehouseId, addDiscountStdReqRound);

		// 선택 상품을 제외한 상품들의 결제 정보
		RegularReqPaymentListDto removeGoodsReqPayment = new RegularReqPaymentListDto();

		// 선택 상품 제외 상품 목록
		List<RegularResultReqRoundGoodsListDto> removeGoodsReqRoundGoodsList = regularResultReqRoundGoodsList.stream()
																												.filter(obj -> obj.getIlGoodsId() != ilGoodsId)
																												.collect(toList());

		// 결제정보 얻기
		this.getOrderRegularPaymentInfo(removeGoodsReqRoundGoodsList, removeGoodsReqPayment, urWarehouseId, addDiscountStdReqRound);

		// 선택 상품의 배송비 얻기
		RegularReqPaymentListDto selectGoodsShippingInfo = new RegularReqPaymentListDto();

		// 선택 상품 제외 상품 목록
		String grpWarehouseShippingTmplId = regularResultReqRoundGoodsList.stream().filter(obj -> obj.getIlGoodsId() == ilGoodsId).map(x -> x.getGrpWarehouseShippingTmplId()).findAny().get();
		List<RegularResultReqRoundGoodsListDto> selectGoodsInfoList = regularResultReqRoundGoodsList.stream()
																									.filter(obj -> grpWarehouseShippingTmplId.equals(obj.getGrpWarehouseShippingTmplId()) && obj.getIlGoodsId() != ilGoodsId)
																									.collect(toList());
		// 선택 상품 제외 상품 목록이 존재하지 않을 경우
		if(CollectionUtils.isEmpty(selectGoodsInfoList)) {
			// 해당 배송정책의 전체 취소로 판단하여, 해당 상품의 배송비를 계산한다.
			selectGoodsInfoList = regularResultReqRoundGoodsList.stream()
																.filter(obj -> grpWarehouseShippingTmplId.equals(obj.getGrpWarehouseShippingTmplId()) && obj.getIlGoodsId() == ilGoodsId)
																.collect(toList());
		}

		// 선택 상품과 동일한 출고처 배송비 얻기
		this.getOrderRegularPaymentInfo(selectGoodsInfoList, selectGoodsShippingInfo, urWarehouseId, addDiscountStdReqRound);

		RegularReqGoodsSkipCancelResponseDto regularReqGoodsSkipCancelResponseDto = new RegularReqGoodsSkipCancelResponseDto();

		regularReqGoodsSkipCancelResponseDto.setIlGoodsId(ilGoodsId);
		regularReqGoodsSkipCancelResponseDto.setReqRound(regularResultGoodsDetailInfo.getReqRound());
		regularReqGoodsSkipCancelResponseDto.setAddDiscountStdReqRound(addDiscountStdReqRound);

		Map<String, Integer> goodsDisCountMap = new LinkedHashMap<>();
		// 선택한 상품정보 얻어오기
		for(RegularResultReqRoundGoodsListDto regularResultReqRoundGoodsItem : regularResultReqRoundGoodsList) {

			if(ilGoodsId == regularResultReqRoundGoodsItem.getIlGoodsId()) {
				// 상품명
				regularReqGoodsSkipCancelResponseDto.setGoodsNm(regularResultReqRoundGoodsItem.getGoodsNm());
				// 상품이미지
				regularReqGoodsSkipCancelResponseDto.setThumbnailPath(regularResultReqRoundGoodsItem.getThumbnailPath());
				// 주문수량
				regularReqGoodsSkipCancelResponseDto.setOrderCnt(regularResultReqRoundGoodsItem.getOrderCnt());

				// 상품 금액
				int beforePaymentPrice = regularResultReqRoundGoodsItem.getSalePrice();
				// 정기배송 기본 할인 금액
				int afterPaymentPrice = PriceUtil.getPriceByRate(beforePaymentPrice, regularResultReqRoundGoodsItem.getBasicDiscountRate());
				// 정기배송 기준 회차부터는 추가 할인
				if (regularResultReqRoundGoodsItem.getReqRound() >= addDiscountStdReqRound) {
					// 할인한 상품금액 정보에 추가 할인 적용
					afterPaymentPrice = PriceUtil.getPriceByRate(afterPaymentPrice, regularResultReqRoundGoodsItem.getAdddiscountRate());
				}

				// 상품금액
				//regularReqGoodsSkipCancelResponseDto.setGoodsSalePrice(regularResultReqRoundGoodsItem.getSalePrice() * regularResultReqRoundGoodsItem.getOrderCnt());
				regularReqGoodsSkipCancelResponseDto.setGoodsSalePrice(afterPaymentPrice * regularResultReqRoundGoodsItem.getOrderCnt());

				ShippingDataResultVo shippingDataResultVo = new ShippingDataResultVo();
				String shippingPriceText = "";
				try {
					shippingDataResultVo = goodsShippingTemplateBiz.getShippingInfoByShippingTmplId(selectGoodsShippingInfo.getIlShippingTmplId());
					shippingPriceText = goodsShippingTemplateBiz.getShippingPriceText(shippingDataResultVo);
				} catch (Exception e) {
					e.printStackTrace();
				}

				regularResultReqRoundGoodsItem.setDeliveryTmplNm(shippingPriceText);

				// 배송정책명
				regularReqGoodsSkipCancelResponseDto.setDeliveryTmplNm(regularResultReqRoundGoodsItem.getDeliveryTmplNm());
			}
			else {
				String goodsDiscountTp = regularResultReqRoundGoodsItem.getDiscountTp();
				String goodsDiscountTpNm = regularResultReqRoundGoodsItem.getDiscountTpNm();
				// 상품 할인타입이 할인정보 없음이 아닐 경우
				if(!GoodsDiscountType.NONE.getCode().equals(goodsDiscountTp)) {
					if(!goodsDisCountMap.containsKey(goodsDiscountTpNm)) {
						goodsDisCountMap.put(goodsDiscountTpNm, 0);
					}
					goodsDisCountMap.put(goodsDiscountTpNm, goodsDisCountMap.get(goodsDiscountTpNm) + ((regularResultReqRoundGoodsItem.getRecommendedPrice() - regularResultReqRoundGoodsItem.getSalePrice()) * regularResultReqRoundGoodsItem.getOrderCnt()));
				}
			}
		}

		// 선택 상품 제외 상품 목록이 없을 경우
		if(CollectionUtils.isEmpty(removeGoodsReqRoundGoodsList)){
			// 기본할인율
			regularReqGoodsSkipCancelResponseDto.setDiscountRate(regularReqPayment.getDiscountRate());
			// 추가할인율
			regularReqGoodsSkipCancelResponseDto.setAddDiscountRate(regularReqPayment.getAddDiscountRate());
			// 추가할인회차정보
			regularReqGoodsSkipCancelResponseDto.setAddDiscountReqRound(regularReqPayment.getAddDiscountReqRound());
		}else{
			// 기본할인율
			regularReqGoodsSkipCancelResponseDto.setDiscountRate(removeGoodsReqPayment.getDiscountRate());
			// 추가할인율
			regularReqGoodsSkipCancelResponseDto.setAddDiscountRate(removeGoodsReqPayment.getAddDiscountRate());
			// 추가할인회차정보
			regularReqGoodsSkipCancelResponseDto.setAddDiscountReqRound(removeGoodsReqPayment.getAddDiscountReqRound());
		}
		// 변경배송비
		regularReqGoodsSkipCancelResponseDto.setChangeShippingPrice(selectGoodsShippingInfo.getWarehouseShippingPrice());
		// 기본할인금액
		regularReqGoodsSkipCancelResponseDto.setDiscountPrice(removeGoodsReqPayment.getDiscountPrice());
		// 추가할인금액
		regularReqGoodsSkipCancelResponseDto.setAddDiscountPrice(removeGoodsReqPayment.getAddDiscountPrice());
		// 총상품금액
		regularReqGoodsSkipCancelResponseDto.setRecommendedPrice(removeGoodsReqPayment.getRecommendedPrice());
		// 총배송비
		regularReqGoodsSkipCancelResponseDto.setShippingPrice(removeGoodsReqPayment.getShippingPrice());
		// 촐 할인 금액
		regularReqGoodsSkipCancelResponseDto.setTotDiscountPrice(removeGoodsReqPayment.getDiscountPrice() + removeGoodsReqPayment.getAddDiscountPrice() + (removeGoodsReqPayment.getRecommendedPrice() - removeGoodsReqPayment.getSalePrice()));
		// 주문금액
		regularReqGoodsSkipCancelResponseDto.setOrderPrice(removeGoodsReqPayment.getRecommendedPrice() + removeGoodsReqPayment.getShippingPrice());
		// 총결제예정금액
		regularReqGoodsSkipCancelResponseDto.setPaidPrice(removeGoodsReqPayment.getPaidPrice());
		// 상품 주문상태
		regularReqGoodsSkipCancelResponseDto.setPaymentYn("N");

		// 정기배송 결과 상품 주문 상태 확인
		RegularResultOrderStatusResultDto regularResultOrderStatusResult = orderRegularDetailService.getOrderRegularResultGoodsOrderStatusCd(odRegularResultId);
		if(!Objects.isNull(regularResultOrderStatusResult)) {
			if(!StringUtil.isEmpty(regularResultOrderStatusResult.getIcDt())) {
				regularReqGoodsSkipCancelResponseDto.setPaymentYn("Y");
			}
		}

		// 상품 할인정보 Set
		List<RegularResultGoodsDiscountListDto> discountList = new ArrayList<>();
		// 상품 할인정보 존재 시
		if(!goodsDisCountMap.isEmpty()) {
			// 상품 할인 종류 코드 및 즉시/우선 등등 할인금액
			for(String goodsDisCountMapKey : goodsDisCountMap.keySet()) {
				RegularResultGoodsDiscountListDto regularResultGoodsDiscount = new RegularResultGoodsDiscountListDto();
				regularResultGoodsDiscount.setDiscountTpNm(goodsDisCountMapKey);
				regularResultGoodsDiscount.setDiscountPrice(goodsDisCountMap.get(goodsDisCountMapKey));
				discountList.add(regularResultGoodsDiscount);
			}
		}
		regularReqGoodsSkipCancelResponseDto.setDiscountList(discountList);

		return regularReqGoodsSkipCancelResponseDto;
	}

	/**
	 * 정기배송 결과 상세코드 변경
	 * @param odRegularResultId
	 * @param reqDetailStatusCd
	 * @param changeReqDetailStatusCd
	 * @param ilGoodsId
	 * @param orderRegularReqHistoryVo
	 * @throws Exception
	 */
	private int putOrderRegularReqRoundReqDetailStatusCd(long odRegularResultId, String reqDetailStatusCd, String changeReqDetailStatusCd, long ilGoodsId, OrderRegularReqHistoryVo orderRegularReqHistoryVo) throws Exception{

		int updateCnt = orderRegularDetailService.putOrderRegularDetailStatusCd(odRegularResultId, reqDetailStatusCd, changeReqDetailStatusCd, ilGoodsId);

		// 정기배송주문 히스토리 등록 값 세팅
		long odRegularReqHistoryId = orderRegularRegistrationService.getOdRegularReqHistoryIdSeq();
		orderRegularReqHistoryVo.setOdRegularReqHistoryId(odRegularReqHistoryId);	// 정기배송주문히스토리 PK

		// 정기배송주문 히스토리 등록 처리
		orderRegularRegistrationService.addOrderRegularReqHistory(orderRegularReqHistoryVo);

		return updateCnt;
	}


	/**
	 * 정기배송 회차 건너뛰기
	 * @param odRegularResultId
	 * @throws Exception
	 */
	@Override
	public void putOrderRegularReqRoundSkip(long odRegularResultId, long urUserId) throws Exception {

		// 정기배송 신청 정보 가져오기
		RegularReqListDto regularReqInfo = orderRegularDetailService.getOrderRegularResultGoodsInfo(odRegularResultId, 0);

		if(!Objects.isNull(regularReqInfo)) {
			OrderRegularReqHistoryVo orderRegularReqHistoryVo = new OrderRegularReqHistoryVo();
			orderRegularReqHistoryVo.setOdRegularReqId(regularReqInfo.getOdRegularReqId());
			orderRegularReqHistoryVo.setRegularReqGbnCd(OrderEnums.RegularReqGbnCd.RS.getCode());	// 변경구분값 공통코드(REGULAR_REQ_GBN_CD)
			orderRegularReqHistoryVo.setRegularReqStatusCd(OrderEnums.RegularReqStatusCd.PC.getCode());// 처리상태 공통코드(REGULAR_REQ_STATUS_CD)
			orderRegularReqHistoryVo.setRegularReqCont(regularReqInfo.getReqRound() + " 회차 " + regularReqInfo.getGoodsCnt() + "개 상품");					// 상세내용
			orderRegularReqHistoryVo.setCreateId(urUserId);								// 등록자

			// 정기배송 결과 상세 테이블 내 신청건에 한해 건너뛰기로 변경
			int updateCnt = this.putOrderRegularReqRoundReqDetailStatusCd(odRegularResultId, OrderEnums.RegularDetlStatusCd.APPLY.getCode(), OrderEnums.RegularDetlStatusCd.SKIP.getCode(), 0, orderRegularReqHistoryVo);

			// 회차 건너뛰기 완료 시 SMS / EMAIL 발송
			if(updateCnt > 0) {
				OrderInfoForEmailResultDto orderInfoForEmailResultDto = orderEmailBiz.getOrderRegularInfoForEmail(odRegularResultId);
				orderEmailSendBiz.orderRegularReqRoundSkipCompleted(orderInfoForEmailResultDto);
			}
		}
	}

	/**
	 * 정기배송 회차 건너뛰기 철회
	 * @param odRegularResultId
	 * @throws Exception
	 */
	@Override
	public void putOrderRegularReqRoundSkipCancel(long odRegularResultId, long urUserId) throws Exception {

		// 정기배송 신청 정보 가져오기
		RegularReqListDto regularReqInfo = orderRegularDetailService.getOrderRegularResultGoodsInfo(odRegularResultId, 0);

		if(!Objects.isNull(regularReqInfo)) {
			OrderRegularReqHistoryVo orderRegularReqHistoryVo = new OrderRegularReqHistoryVo();
			orderRegularReqHistoryVo.setOdRegularReqId(regularReqInfo.getOdRegularReqId());
			orderRegularReqHistoryVo.setRegularReqGbnCd(OrderEnums.RegularReqGbnCd.RSC.getCode());	// 변경구분값 공통코드(REGULAR_REQ_GBN_CD)
			orderRegularReqHistoryVo.setRegularReqStatusCd(OrderEnums.RegularReqStatusCd.PC.getCode());// 처리상태 공통코드(REGULAR_REQ_STATUS_CD)
			orderRegularReqHistoryVo.setRegularReqCont(regularReqInfo.getReqRound() + " 회차 " + regularReqInfo.getGoodsCnt() + "개 상품");					// 상세내용
			orderRegularReqHistoryVo.setCreateId(urUserId);								// 등록자

			// 정기배송 결과 상세 테이블 내 신청건에 한해 승인으로 변경
			putOrderRegularReqRoundReqDetailStatusCd(odRegularResultId, OrderEnums.RegularDetlStatusCd.SKIP.getCode(), OrderEnums.RegularDetlStatusCd.APPLY.getCode(), 0, orderRegularReqHistoryVo);

			long odRegularReqId = regularReqInfo.getOdRegularReqId();
			String regularStatusCd = OrderEnums.RegularStatusCd.ING.getCode();
			String reqDetailStatusCd = OrderEnums.RegularDetlStatusCd.APPLY.getCode();
			orderRegularDetailService.putOrderRegularResultStatusIng(odRegularReqId, regularStatusCd, reqDetailStatusCd);
		}
	}

	/**
	 * 정기배송 상품 건너뛰기
	 * @param odRegularResultDetlId
	 * @param urUserId
	 * @throws Exception
	 */
	@Override
	public int putOrderRegularGoodsSkip(long odRegularResultDetlId, long urUserId) throws Exception {

		int updateCnt = 0;

		// 정기배송 상세 정보 가져오기
		RegularResultGoodsDetailInfoDto regularResultGoodsDetailInfo = orderRegularDetailService.getOrderRegularResultDetailGoodsInfo(odRegularResultDetlId);

		long odRegularResultId = regularResultGoodsDetailInfo.getOdRegularResultId();
		long ilGoodsId = regularResultGoodsDetailInfo.getIlGoodsId();
		int reqRound = regularResultGoodsDetailInfo.getReqRound();

		// 정기배송 신청 정보 가져오기
		RegularReqListDto regularReqInfo = orderRegularDetailService.getOrderRegularResultGoodsInfo(odRegularResultId, ilGoodsId);

		if(!Objects.isNull(regularReqInfo)) {
			OrderRegularReqHistoryVo orderRegularReqHistoryVo = new OrderRegularReqHistoryVo();
			orderRegularReqHistoryVo.setOdRegularReqId(regularReqInfo.getOdRegularReqId());
			orderRegularReqHistoryVo.setRegularReqGbnCd(OrderEnums.RegularReqGbnCd.GS.getCode());	// 변경구분값 공통코드(REGULAR_REQ_GBN_CD)
			orderRegularReqHistoryVo.setRegularReqStatusCd(OrderEnums.RegularReqStatusCd.PC.getCode());// 처리상태 공통코드(REGULAR_REQ_STATUS_CD)
			orderRegularReqHistoryVo.setRegularReqCont(reqRound + "회차 " + regularReqInfo.getGoodsNm());	// 상세내용
			orderRegularReqHistoryVo.setCreateId(urUserId);								// 등록자

			// 정기배송 결과 상세 테이블 내 신청건에 한해 건너뛰기로 변경
			updateCnt = putOrderRegularReqRoundReqDetailStatusCd(odRegularResultId, OrderEnums.RegularDetlStatusCd.APPLY.getCode(), OrderEnums.RegularDetlStatusCd.SKIP.getCode(), ilGoodsId, orderRegularReqHistoryVo);

			// 회차 건너뛰기 완료 시 SMS / EMAIL 발송
			if(updateCnt > 0) {
				OrderInfoForEmailResultDto orderInfoForEmailResultDto = orderEmailBiz.getOrderRegularResultDetlInfoForEmail(odRegularResultDetlId);
				orderEmailSendBiz.orderRegularGoodsSkipCompleted(orderInfoForEmailResultDto);
			}
		}

		return updateCnt;
	}

	/**
	 * 정기배송 상품 건너뛰기 철회
	 * @param odRegularResultDetlId
	 * @param urUserId
	 * @throws Exception
	 */
	@Override
	public void putOrderRegularGoodsSkipCancel(long odRegularResultDetlId, long urUserId) throws Exception {

		// 정기배송 상세 정보 가져오기
		RegularResultGoodsDetailInfoDto regularResultGoodsDetailInfo = orderRegularDetailService.getOrderRegularResultDetailGoodsInfo(odRegularResultDetlId);

		long odRegularResultId = regularResultGoodsDetailInfo.getOdRegularResultId();
		long ilGoodsId = regularResultGoodsDetailInfo.getIlGoodsId();

		// 정기배송 신청 정보 가져오기
		RegularReqListDto regularReqInfo = orderRegularDetailService.getOrderRegularResultGoodsInfo(odRegularResultId, ilGoodsId);

		if(!Objects.isNull(regularReqInfo)) {
			OrderRegularReqHistoryVo orderRegularReqHistoryVo = new OrderRegularReqHistoryVo();
			orderRegularReqHistoryVo.setOdRegularReqId(regularReqInfo.getOdRegularReqId());
			orderRegularReqHistoryVo.setRegularReqGbnCd(OrderEnums.RegularReqGbnCd.GSC.getCode());	// 변경구분값 공통코드(REGULAR_REQ_GBN_CD)
			orderRegularReqHistoryVo.setRegularReqStatusCd(OrderEnums.RegularReqStatusCd.PC.getCode());// 처리상태 공통코드(REGULAR_REQ_STATUS_CD)
			orderRegularReqHistoryVo.setRegularReqCont(regularReqInfo.getGoodsNm());	// 상세내용
			orderRegularReqHistoryVo.setCreateId(urUserId);								// 등록자

			// 정기배송 결과 상세 테이블 내 신청건에 한해 건너뛰기로 변경
			putOrderRegularReqRoundReqDetailStatusCd(odRegularResultId, OrderEnums.RegularDetlStatusCd.SKIP.getCode(), OrderEnums.RegularDetlStatusCd.APPLY.getCode(), ilGoodsId, orderRegularReqHistoryVo);

			long odRegularReqId = regularResultGoodsDetailInfo.getOdRegularReqId();
			String regularStatusCd = OrderEnums.RegularStatusCd.ING.getCode();
			String reqDetailStatusCd = OrderEnums.RegularDetlStatusCd.APPLY.getCode();
			orderRegularDetailService.putOrderRegularResultStatusIng(odRegularReqId, regularStatusCd, reqDetailStatusCd);
		}
	}

	/**
	 * 정기배송 상품 취소
	 * @param odRegularResultDetlId
	 * @param urUserId
	 * @throws Exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {BaseException.class,Exception.class})
	public void putOrderRegularGoodsCancel(long odRegularResultDetlId, long urUserId) throws Exception {

		// 정기배송 상세 정보 가져오기
		RegularResultGoodsDetailInfoDto regularResultGoodsDetailInfo = orderRegularDetailService.getOrderRegularResultDetailGoodsInfo(odRegularResultDetlId);

		long odRegularResultId = regularResultGoodsDetailInfo.getOdRegularResultId();
		long ilGoodsId = regularResultGoodsDetailInfo.getIlGoodsId();

		// 정기배송 신청 정보 가져오기
		RegularReqListDto regularReqInfo = orderRegularDetailService.getOrderRegularResultGoodsInfo(odRegularResultId, ilGoodsId);

		// 정기배송 신청 PK
		long odRegularReqId = regularReqInfo.getOdRegularReqId();
		String regularStatusCd = OrderEnums.RegularStatusCd.CANCEL.getCode();
		String reqDetailStatusCd = OrderEnums.RegularDetlStatusCd.CANCEL_BUYER.getCode();

		// 1. odRegularResultId 에 엮여있는 주문 상세의 해당 하는 IL_GOODS_ID의 주문 상태값을 변경

		// 정기배송 상품 취소 시 회차 완료 되지 않은 건들에 대해서 상품 취소 처리
		// 1. 정기배송 결과 상세 테이블 내 해당 상품 코드 해지 상태로 변경
		orderRegularDetailService.putOrderRegularResultDetailGoodsCancel(odRegularReqId, reqDetailStatusCd, ilGoodsId);

		reqDetailStatusCd = OrderEnums.RegularDetlStatusCd.APPLY.getCode();
		// 2. 정기배송 결과 테이블에 신청 상품이 없는 회차 정보의 경우 해지 회차로 처리
		orderRegularDetailService.putOrderRegularResultStatusCancel(odRegularReqId, regularStatusCd, reqDetailStatusCd);

		reqDetailStatusCd = OrderEnums.RegularDetlStatusCd.CANCEL_BUYER.getCode();
		// 3. 정기배송 신청 상세 테이블 내 해당 상품 해지상태로 변경
		orderRegularDetailService.putOrderRegularReqDetailGoodsCancel(odRegularReqId, reqDetailStatusCd, ilGoodsId);

		reqDetailStatusCd = OrderEnums.RegularDetlStatusCd.APPLY.getCode();
		// 4. 정기배송 신청 상세 테이블 내 신청 가능 상품 없는 신청정보의 경우 신청 상태 해지로 변경
		orderRegularDetailService.putOrderRegularReqStatusCancel(odRegularReqId, regularStatusCd, reqDetailStatusCd);

		// 정기배송주문 히스토리 등록 값 세팅
		long odRegularReqHistoryId = orderRegularRegistrationService.getOdRegularReqHistoryIdSeq();
		OrderRegularReqHistoryVo orderRegularReqHistoryVo = new OrderRegularReqHistoryVo();
		orderRegularReqHistoryVo.setOdRegularReqHistoryId(odRegularReqHistoryId);	// 정기배송주문히스토리 PK
		orderRegularReqHistoryVo.setOdRegularReqId(regularReqInfo.getOdRegularReqId());
		orderRegularReqHistoryVo.setRegularReqGbnCd(OrderEnums.RegularReqGbnCd.GC.getCode());	// 변경구분값 공통코드(REGULAR_REQ_GBN_CD)
		orderRegularReqHistoryVo.setRegularReqStatusCd(OrderEnums.RegularReqStatusCd.PC.getCode());// 처리상태 공통코드(REGULAR_REQ_STATUS_CD)
		orderRegularReqHistoryVo.setRegularReqCont(regularReqInfo.getGoodsNm());	// 상세내용
		orderRegularReqHistoryVo.setCreateId(urUserId);								// 등록자

		// 5. 정기배송주문 히스토리 등록 처리
		orderRegularRegistrationService.addOrderRegularReqHistory(orderRegularReqHistoryVo);
	}

	/**
	 * 정기결제 카드 정보 조회
	 * @param urUserId
	 * @return
	 * @throws Exception
	 */
	@Override
	public RegularReqPaymentCardResponseDto getRegularPaymentCardInfo(long urUserId) throws Exception {

		RegularReqPaymentCardResponseDto regularReqPaymentCardResponseDto = new RegularReqPaymentCardResponseDto();
		regularReqPaymentCardResponseDto.setIsExistPaymentKey("N");
		// 정기결제 카드 정보 조회
		RegularReqPaymentCardDto regularReqPaymentCardInfo = orderRegularDetailService.getRegularPaymentCardInfo(urUserId);
		if(!Objects.isNull(regularReqPaymentCardInfo)) {

			regularReqPaymentCardResponseDto.setOdRegularPaymentKeyId(regularReqPaymentCardInfo.getOdRegularPaymentKeyId());	// 정기배송카드결제정보PK
			regularReqPaymentCardResponseDto.setIsExistPaymentKey("Y");											// 정기배송카드등록여부
			regularReqPaymentCardResponseDto.setCardNm(regularReqPaymentCardInfo.getCardNm());					// 카드명
			regularReqPaymentCardResponseDto.setCardMaskNumber(regularReqPaymentCardInfo.getCardMaskNumber());	// 카드번호마스킹
			regularReqPaymentCardResponseDto.setOrderRegularYn("N");											// 정기배송주문 존재 여부

			// 다음 결제 예정일 조회
			regularReqPaymentCardInfo = orderRegularDetailService.getRegularNextPaymentDtInfo(urUserId, RegularStatusCd.APPLY.getCode());
			if(!Objects.isNull(regularReqPaymentCardInfo)) {
				regularReqPaymentCardResponseDto.setPaymentDt(regularReqPaymentCardInfo.getPaymentDt());		// 다음 결제 예정일
				regularReqPaymentCardResponseDto.setOrderRegularYn("Y");
			}
		}

		return regularReqPaymentCardResponseDto;
	}

	/**
	 * 정기결제 카드 삭제
	 * @param odRegularPaymentKeyId
	 * @param urUserId
	 * @throws Exception
	 */
	@Override
	public void delOrderRegularPaymentCardInfo(long odRegularPaymentKeyId, long urUserId) throws Exception {

		// 1. 정기결제 카드 정보 사용여부 업데이트
		orderRegularDetailService.delOrderRegularPaymentCardInfo(odRegularPaymentKeyId, urUserId);
	}

	/**
	 * 배송비얻기
	 */
	@Override
	public RegularShippingPriceInfoDto getShippingPrice(List<Long> ilGoodsShippingTemplateIdList, int goodsPrice, int orderCnt, String recvZipCd) throws Exception {
		return orderRegularService.getShippingPrice(ilGoodsShippingTemplateIdList, goodsPrice, orderCnt, recvZipCd);
	}

	/**
	 * 정기배송 주문 히스토리 등록
	 * @param orderRegularReqHistoryVo
	 * @throws Exception
	 */
	@Override
	public void putRegularOrderReqHistory(OrderRegularReqHistoryVo orderRegularReqHistoryVo) throws Exception {

		long odRegularReqHistoryId = orderRegularRegistrationService.getOdRegularReqHistoryIdSeq();
		orderRegularReqHistoryVo.setOdRegularReqHistoryId(odRegularReqHistoryId);

		orderRegularRegistrationService.addOrderRegularReqHistory(orderRegularReqHistoryVo);
	}
}
