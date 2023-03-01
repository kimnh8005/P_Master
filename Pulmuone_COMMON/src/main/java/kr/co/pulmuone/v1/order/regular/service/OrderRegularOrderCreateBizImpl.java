package kr.co.pulmuone.v1.order.regular.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.goods.dto.ArrivalScheduledDateDto;
import kr.co.pulmuone.v1.goods.goods.service.GoodsGoodsBiz;
import kr.co.pulmuone.v1.goods.stock.dto.StockOrderRequestDto;
import kr.co.pulmuone.v1.goods.stock.service.GoodsStockOrderBiz;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimRegisterRequestDto;
import kr.co.pulmuone.v1.order.claim.service.ClaimProcessBiz;
import kr.co.pulmuone.v1.order.email.dto.OrderInfoForEmailResultDto;
import kr.co.pulmuone.v1.order.email.service.OrderEmailBiz;
import kr.co.pulmuone.v1.order.email.service.OrderEmailSendBiz;
import kr.co.pulmuone.v1.order.order.dto.StockCheckOrderDetailDto;
import kr.co.pulmuone.v1.order.order.service.OrderOrderBiz;
import kr.co.pulmuone.v1.order.registration.dto.OrderBindDto;
import kr.co.pulmuone.v1.order.registration.dto.OrderRegistrationResponseDto;
import kr.co.pulmuone.v1.order.registration.service.OrderBindRegularCreateBizImpl;
import kr.co.pulmuone.v1.order.registration.service.OrderRegistrationBiz;
import kr.co.pulmuone.v1.order.regular.dto.*;
import kr.co.pulmuone.v1.order.regular.dto.vo.OrderRegularReqHistoryVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송주문생성 Biz OrderRegularOrderCreateBizImpl
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020.09.23	  김명진           최초작성
 * =======================================================================
 * </PRE>
 */
@Slf4j
@Service
public class OrderRegularOrderCreateBizImpl implements OrderRegularOrderCreateBiz {

	@Autowired
	private OrderRegularOrderCreateService orderRegularOrderCreateService;

	@Autowired
	private OrderBindRegularCreateBizImpl orderBindBiz;

	@Autowired
	private OrderRegistrationBiz orderRegistrationBiz;

	@Autowired
	private OrderOrderBiz orderOrderBiz;

	@Autowired
	private GoodsStockOrderBiz goodsStockOrderBiz;

	@Autowired
	private OrderRegularDetailBiz orderRegularDetailBiz;

	@Autowired
	private OrderEmailBiz orderEmailBiz;

	@Autowired
	private OrderEmailSendBiz orderEmailSendBiz;

	@Autowired
	private GoodsGoodsBiz goodsGoodsBiz;

	@Autowired
	private ClaimProcessBiz claimProcessBiz;

	/**
	 * 정기배송 주문생성 대상 상품 목록 조회
	 * @param odRegularResultId
	 * @return
	 */
	@Override
	public List<RegularResultCreateOrderListDto> getRegularOrderResultCreateGoodsList(long odRegularResultId) throws Exception {

		RegularResultCreateOrderListRequestDto regularResultCreateOrderListRequestDto = new RegularResultCreateOrderListRequestDto();
		List<String> regularStatusCdList = new ArrayList<> ();
		regularStatusCdList.add(OrderEnums.RegularStatusCd.APPLY.getCode());
		regularStatusCdList.add(OrderEnums.RegularStatusCd.ING.getCode());
		// 1. 정기배송 신청 테이블 상태 APPLY 인것 중
		//    정기배송 결과 테이블 상태 APPLY 인것 중에 주문서 생성 예정일자가 배치생성일자와 동일한 건, 회차완료 여부가 N 인건, 주문생성여부가 N 인건 중
		//    정기배송 결과 상세 테이블 상태 APPLY 인것 목록 조회
		regularResultCreateOrderListRequestDto.setSaleStatus(GoodsEnums.SaleStatus.ON_SALE.getCode());
		regularResultCreateOrderListRequestDto.setRegularStatusCdList(regularStatusCdList);
		regularResultCreateOrderListRequestDto.setReqDetailStatusCd(OrderEnums.RegularDetlStatusCd.APPLY.getCode());
		regularResultCreateOrderListRequestDto.setOdRegularResultId(odRegularResultId);

		return orderRegularOrderCreateService.getRegularOrderResultCreateGoodsList(regularResultCreateOrderListRequestDto);
	}

	/**
	 * I/F 일자, 배송일자, 도착예정일자 정보 유효성 체크 및 값 설정
	 * @param goodsList
	 * @return
	 */
	private List<RegularResultCreateOrderListDto> getValidArrivalDtGoodsList(List<RegularResultCreateOrderListDto> goodsList, List<RegularResultCreateOrderListDto> regularGoodsList) {

		List<RegularResultCreateOrderListDto> resultGoodsList = new ArrayList<>();
		// 상품 목록 loop
		for (RegularResultCreateOrderListDto goodsItem : goodsList) {

			// 도착일 기준 I/F일자 정보 조회
			ArrivalScheduledDateDto arrivalSchedule = null;
			try {
				arrivalSchedule = goodsGoodsBiz.getArrivalScheduledDateDtoByIlGoodsIdArrivalScheduledDate(goodsItem.getIlGoodsId(), goodsItem.getArriveDt());
			} catch (Exception e) {
				log.error("------------- arrivalSchedule error {}", e.getMessage());
			}
			if(arrivalSchedule != null) {
				goodsItem.setOrderIfDt(arrivalSchedule.getOrderDate());
				goodsItem.setShippingDt(arrivalSchedule.getForwardingScheduledDate());
				goodsItem.setDeliveryDt(arrivalSchedule.getArrivalScheduledDate());
				resultGoodsList.add(goodsItem);
			}
		}

		if(resultGoodsList.size() > 0) {

			List<RegularResultCreateOrderListDto> stockResultGoodsList = new ArrayList<>();
			try {
				// 재고 차감 START
				List<StockOrderRequestDto> stockOrderReqDtoList = null;
				StockOrderRequestDto stockOrderRequestDto = null;
				for (RegularResultCreateOrderListDto goods : goodsList) {

					stockOrderReqDtoList = new ArrayList<>();
					stockOrderRequestDto = new StockOrderRequestDto();

					if (ObjectUtils.isEmpty(goods.getShippingDt())) {
						continue;
					}
					StockOrderRequestDto stockOrderReqDto = new StockOrderRequestDto();
					stockOrderReqDto.setIlGoodsId(goods.getIlGoodsId());
					stockOrderReqDto.setOrderQty(goods.getOrderCnt());
					stockOrderReqDto.setScheduleDt(goods.getShippingDt().toString());
					stockOrderReqDto.setOrderYn("Y");
					stockOrderReqDto.setStoreYn("N");
					stockOrderReqDtoList.add(stockOrderReqDto);
					stockOrderRequestDto.setOrderList(stockOrderReqDtoList);
					ApiResult<?> stockRes = goodsStockOrderBiz.validStockOrderHandle(stockOrderRequestDto);
					if (!BaseEnums.Default.SUCCESS.getCode().equals(stockRes.getCode())) {
						RegularResultCreateOrderListDto resultItem = regularGoodsList.stream().filter(x ->	x.getOdRegularResultId() == goods.getOdRegularResultId() &&
																											x.getIlGoodsId() == goods.getIlGoodsId())
																								.findAny()
																								.get();
						if(ObjectUtils.isNotEmpty(resultItem)) {
							resultItem.setSaleStatus(OrderEnums.RegularDetlSaleStatusCd.OUT_OF_STOCK.getCode());
						}
					}
					else {
						stockResultGoodsList.add(goods);
					}
				}
				resultGoodsList = stockResultGoodsList;
			}
			catch(Exception e) {
				log.error("------------- stockCheck error {}", e.getMessage());
			}
		}

		return resultGoodsList;
	}

	/**
	 * 정기배송 주문 생성 처리
	 * @param regularGoodsList
	 * @param odRegularResultId
	 * @param entryData
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = {BaseException.class,Exception.class})
	public RegularResultCreateOrderResponseDto createOrderRegular(List<RegularResultCreateOrderListDto> regularGoodsList, long odRegularResultId, Map<String, List<RegularResultCreateOrderListDto>> entryData) throws Exception {

		RegularResultCreateOrderResponseDto result = new RegularResultCreateOrderResponseDto();
		result.setIbFlag(false);
		result.setOrderCreateFlag(false);
		int addDiscountStdReqRound = orderRegularOrderCreateService.getAddDiscountStdReqRound(odRegularResultId);	// 정기배송 추가할인 기준 회차 정보 조회
		String orderCreateFailMsg = "";
		String resultOdids = "";

		// -- 판매중 상태인 상품 리스트 생성
		List<RegularResultCreateOrderListDto> onSaleGoods = regularGoodsList.stream().filter(data -> data.getParentIlGoodsId() == 0 &&
																										OrderEnums.RegularDetlSaleStatusCd.ON_SALE.getCode().equals(data.getSaleStatus()))
																						.collect(Collectors.toList());

		// 추가 상품이 존재할 경우 부모 상품 PK 별 Group
		Map<Long, List<RegularResultCreateOrderListDto>> childGoodsList = null;

		// -- 판매중 상품 리스트.size 가 0 일 경우 입금전 취소
		if(onSaleGoods.size() == 0) {
			orderCreateFailMsg = "판매중 상품 미존재";
			//    --> 전체 상품 주문서 생성 후 입금전 취소 처리, isIbFlag = true;
			result.setIbFlag(true);
			childGoodsList = regularGoodsList.stream()
												.filter(data -> data.getOdRegularResultId() == odRegularResultId && data.getParentIlGoodsId() != 0)
												.collect(Collectors.groupingBy(RegularResultCreateOrderListDto::getParentIlGoodsId, LinkedHashMap::new, Collectors.toList()));
		}
		// -- 판매중 상품 리스트.size 가 0 이 아닐 경우
		else {
			// 판매중 상품 중 I/F날짜정보 존재하지 않는 건들 제외 처리
			List<RegularResultCreateOrderListDto> validGoods = this.getValidArrivalDtGoodsList(onSaleGoods, regularGoodsList);
			if(validGoods.size() < 1) {
				orderCreateFailMsg = "재고부족";
				result.setIbFlag(true);
				childGoodsList = regularGoodsList.stream()
													.filter(data -> data.getOdRegularResultId() == odRegularResultId && data.getParentIlGoodsId() != 0)
													.collect(Collectors.groupingBy(RegularResultCreateOrderListDto::getParentIlGoodsId, LinkedHashMap::new, Collectors.toList()));
			}
			else {
				//    --> 판매중 상품만 주문서 생성
				entryData = validGoods.stream().collect(Collectors.groupingBy(RegularResultCreateOrderListDto::getGrpWarehouseShippingTmplId, LinkedHashMap::new, Collectors.toList()));
				childGoodsList = regularGoodsList.stream()
													.filter(data -> data.getOdRegularResultId() == odRegularResultId && data.getParentIlGoodsId() != 0 && OrderEnums.RegularDetlSaleStatusCd.ON_SALE.getCode().equals(data.getSaleStatus()))
													.collect(Collectors.groupingBy(RegularResultCreateOrderListDto::getParentIlGoodsId, LinkedHashMap::new, Collectors.toList()));
			}
		}

		RegularResultCreateOrderGoodsListDto createOrderGoodsList = new RegularResultCreateOrderGoodsListDto();
		createOrderGoodsList.setOdRegularResultId(odRegularResultId);
		createOrderGoodsList.setAddDiscountStdReqRound(addDiscountStdReqRound);
		createOrderGoodsList.setGoodsList(entryData);
		createOrderGoodsList.setChildGoodsList(childGoodsList);

		RegularResultCreateOrderListDto regularResultCreateOrderItem = regularGoodsList.get(0);
		String regularStatusCd = OrderEnums.RegularStatusCd.ING.getCode();
		// 마지막 회차면 종료로 상태 값 변경 처리
		if(regularResultCreateOrderItem.getLastReqRound() == regularResultCreateOrderItem.getReqRound()) {
			regularStatusCd = OrderEnums.RegularStatusCd.END.getCode();
		}
		// 정기배송 상세 상품 판매상태 업데이트
		orderRegularOrderCreateService.putOrderRegularDetlGoodsSaleStatus(odRegularResultId, OrderEnums.RegularDetlSaleStatusCd.ON_SALE.getCode());
		// 신청 정보 테이블 상태 값 변경
		orderRegularOrderCreateService.putRegularOrderReqStatus(odRegularResultId, regularStatusCd);
		// 신청 결과 테이블 상태 값 변경
		orderRegularOrderCreateService.putRegularOrderResultStatus(odRegularResultId, regularStatusCd);

		// 판매중 상품 아닌 것 목록 생성
		List<RegularResultCreateOrderListDto> notOnSaleGoodsList = regularGoodsList.stream()
																					.filter(x -> x.getParentIlGoodsId() == 0 &&
																							!OrderEnums.RegularDetlSaleStatusCd.ON_SALE.getCode().equals(x.getSaleStatus()))
																					.collect(Collectors.toList());

		// 판매중 상태가 아닌 상품이 존재할 경우
		if(CollectionUtils.isNotEmpty(notOnSaleGoodsList)) {
			if (childGoodsList != null && !childGoodsList.isEmpty()) {
				for (long childKey : childGoodsList.keySet()) {
					notOnSaleGoodsList.addAll(childGoodsList.get(childKey));
				}
			}
			// 정기배송 상세 상품 판매상태 업데이트
			orderRegularOrderCreateService.putOrderRegularDetlGoodsSaleStatusByGoodsList(odRegularResultId, notOnSaleGoodsList);
		}

		// 주문서 생성 후 입금전 취소 처리 여부가 false 일 경우, 주문서 생성 전에 재고 수량 체크를 한번 해준다

		// 주문서 생성용 데이터 Bind
		List<OrderBindDto> orderBindList = orderBindBiz.orderDataBind(createOrderGoodsList);

		// 주문서 생성
		OrderRegistrationResponseDto orderRegistrationResponseDto = orderRegistrationBiz.createOrderNonTransaction(orderBindList, "N");

		// 2. 정기배송결과 테이블 주문서 생성 된 것 주문서 생성여부 변경
		// 생성된 주문번호 목록
		resultOdids = orderRegistrationResponseDto.getOdids();
		if(!StringUtil.isEmpty(resultOdids)) {

			result.setOrderCreateFlag(true);
			result.setOdid(resultOdids);

			// 정기배송 주문 결과 테이블 변경 - 주문번호, 주문서 생성여부
			int updateCnt = orderRegularOrderCreateService.putRegularResultOrderCreateYn(odRegularResultId, resultOdids);
			if(updateCnt > 0) {

				String stockMsg = "";
				// 입금전 취소 처리 해야 하는 경우에는 재고차감을 하지 않는다
				if(!result.isIbFlag()) {
					// 재고 차감 START
					List<StockOrderRequestDto> stockOrderReqDtoList = new ArrayList<>();
					StockOrderRequestDto stockOrderRequestDto = new StockOrderRequestDto();
					List<StockCheckOrderDetailDto> orderGoodsList = orderOrderBiz.getStockCheckOrderDetailList(Long.parseLong(orderRegistrationResponseDto.getOdOrderIds()));
					for (StockCheckOrderDetailDto goods : orderGoodsList) {
						if (ObjectUtils.isEmpty(goods.getShippingDt())) {
							continue;
						}
						StockOrderRequestDto stockOrderReqDto = new StockOrderRequestDto();
						stockOrderReqDto.setIlGoodsId(goods.getIlGoodsId());
						stockOrderReqDto.setOrderQty(goods.getOrderCnt());
						stockOrderReqDto.setScheduleDt(goods.getShippingDt().toString());
						stockOrderReqDto.setOrderYn("Y");
						stockOrderReqDto.setStoreYn(GoodsEnums.GoodsDeliveryType.SHOP.getCode().equals(goods.getGoodsDeliveryType()) ? "Y" : "N");
						stockOrderReqDto.setMemo(String.valueOf(goods.getOdOrderDetlId()));
						stockOrderReqDtoList.add(stockOrderReqDto);
					}
					stockOrderRequestDto.setOrderList(stockOrderReqDtoList);
					ApiResult<?> stockRes = goodsStockOrderBiz.stockOrderHandle(stockOrderRequestDto);
					stockMsg = stockRes.getMessage();
					// 출고처 일자별 출고수량 업데이트
					orderOrderBiz.putWarehouseDailyShippingCount(orderRegistrationResponseDto.getOdOrderIds());
					// 재고 차감 END
				}

				// 3. ODID 기준 정기배송 신청 정보 조회
				RegularReqCreateOrderListDto regularReqCreateOrderInfo = orderRegularOrderCreateService.getRegularOrderReqInfo(odRegularResultId);

				if(!Objects.isNull(regularReqCreateOrderInfo)) {

					String regularReqCont = "주문서 생성[배치], 주문번호[" + resultOdids + "]" +
											(StringUtil.isNotEmpty(stockMsg) ? ", 재고차감결과[" + stockMsg + "]" : "") +
											(StringUtil.isNotEmpty(orderCreateFailMsg) ? ", 입금전취소[" + orderCreateFailMsg + "]" : "");

					// 4. 정기배송주문히스토리 테이블 INSERT
					OrderRegularReqHistoryVo orderRegularReqHistoryVo = new OrderRegularReqHistoryVo();
					orderRegularReqHistoryVo.setOdRegularReqId(regularReqCreateOrderInfo.getOdRegularReqId());
					orderRegularReqHistoryVo.setRegularReqGbnCd(OrderEnums.RegularReqGbnCd.OC.getCode());
					orderRegularReqHistoryVo.setRegularReqStatusCd(OrderEnums.RegularReqStatusCd.PC.getCode());
					orderRegularReqHistoryVo.setRegularReqCont(regularReqCont);
					orderRegularReqHistoryVo.setCreateId(Constants.BATCH_CREATE_USER_ID);

					orderRegularDetailBiz.putRegularOrderReqHistory(orderRegularReqHistoryVo);

					// 5. 결제안내문자 발송
					// 입금전 취소 처리 해야하는 경우가 아닐 때에만 주문 생성 완료 SMS발송 처리
					if(!result.isIbFlag()) {
						// 정기배송 주문 생성완료 SMS 발송
						try {
							OrderInfoForEmailResultDto orderInfoForEmailResultDto = orderEmailBiz.getOrderRegularInfoForEmail(odRegularResultId);
							orderEmailSendBiz.orderRegularCreationCompleted(orderInfoForEmailResultDto);
						} catch (Exception e) {
							log.error("ERROR ====== 정기배송 주문 생성완료 SMS 발송 오류 odRegularResultId ::{}", odRegularResultId);
							log.error(e.getMessage());
						}
					}
				}
			}

			// 입금전 취소 플래그가 true일 경우
			if(result.isIbFlag()) {
				// 상품 ID 리스트
				List<Long> ilGoodsIds = regularGoodsList.stream().map(x -> x.getIlGoodsId()).collect(Collectors.toList());

				// 입금전 취소 처리
				OrderClaimRegisterRequestDto orderClaimRegisterRequestDto = orderRegularOrderCreateService.putOdRegularIncomBeforeCancelComplete(ilGoodsIds, result.getOdid(), OrderEnums.RegularOrderBatchTypeCd.CREATE_ORDER.getCode());
				claimProcessBiz.addOrderClaimNonTransaction(orderClaimRegisterRequestDto);
			}
		}

		return result;
	}
}
