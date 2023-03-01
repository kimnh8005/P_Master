package kr.co.pulmuone.v1.order.create.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.constants.OrderShippingConstants;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.GoodsType;
import kr.co.pulmuone.v1.comm.enums.OrderClaimEnums.AllTypeYn;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.enums.OrderEnums.OrderIfType;
import kr.co.pulmuone.v1.comm.enums.OrderEnums.OrderStatus;
import kr.co.pulmuone.v1.comm.enums.OrderEnums.PayType;
import kr.co.pulmuone.v1.comm.enums.SellersEnums;
import kr.co.pulmuone.v1.comm.enums.ShoppingEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mapper.order.create.OrderCopyMapper;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.goods.dto.*;
import kr.co.pulmuone.v1.goods.goods.dto.vo.BasicSelectGoodsVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ShippingAreaVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ShippingDataResultVo;
import kr.co.pulmuone.v1.goods.goods.service.GoodsGoodsBiz;
import kr.co.pulmuone.v1.goods.goods.service.GoodsShippingTemplateBiz;
import kr.co.pulmuone.v1.order.create.dto.*;
import kr.co.pulmuone.v1.order.order.dto.*;
import kr.co.pulmuone.v1.order.order.dto.vo.*;
import kr.co.pulmuone.v1.order.order.service.OrderDetailBiz;
import kr.co.pulmuone.v1.order.registration.service.OrderRegistrationBiz;
import kr.co.pulmuone.v1.order.registration.service.OrderRegistrationSeqBiz;
import kr.co.pulmuone.v1.outmall.order.util.OutmallOrderUtil;
import kr.co.pulmuone.v1.outmall.sellers.dto.SellersExcelDto;
import kr.co.pulmuone.v1.pg.dto.BasicDataResponseDto;
import kr.co.pulmuone.v1.store.delivery.dto.WarehouseUnDeliveryableInfoDto;
import kr.co.pulmuone.v1.store.warehouse.service.StoreWarehouseBiz;
import kr.co.pulmuone.v1.user.buyer.dto.CommonGetRefundBankRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.vo.CommonGetRefundBankResultVo;
import kr.co.pulmuone.v1.user.buyer.service.UserBuyerBiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
*
* <PRE>
* Forbiz Korea
* 주문 복사 관련 Service
* </PRE>
*
* <PRE>
* <B>History:</B>
 * =======================================================================
 *  버전  :	작성일                		:	작성자      :	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 02. 24.		이규한	최초작성
 * =======================================================================
* </PRE>
*/
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderCopyService {

	private final OrderCopyMapper orderCopyMapper;

	@Autowired
	private GoodsGoodsBiz goodsGoodsBiz;

	@Autowired
    private OrderDetailBiz orderDetailBiz;		// 주문 상세 관련 Biz

    @Autowired
    private OrderRegistrationBiz orderRegistrationBiz;	//주문관련 입력

    @Autowired
    private OrderRegistrationSeqBiz orderRegistrationSeqBiz;	//주문관련 Seq 생성

    @Autowired
    private GoodsShippingTemplateBiz goodsShippingTemplateBiz;

	@Autowired
    private OrderCreateBiz orderCreateBiz;

	@Autowired
	private StoreWarehouseBiz storeWarehouseBiz;

	@Autowired
	private UserBuyerBiz userBuyerBiz;

	/**
     * 주문복사 주문 상세내역 조회
     *
     * @param orderCopyDetailInfoRequestDto
     * @return ApiResult<?>
     */
    protected ApiResult<?> getOrderCopyDetailInfo(OrderCopyDetailInfoRequestDto orderCopyDetailInfoRequestDto) throws Exception {
    	boolean isSuccess = true;
    	String failMessage = "";

		if (orderCopyDetailInfoRequestDto != null && StringUtil.isNotEmpty(orderCopyDetailInfoRequestDto.getConditionValue())) {
			UserVo userVo = SessionUtil.getBosUserVO();
	        List<String> listAuthWarehouseId = userVo.getListAuthWarehouseId();
	        listAuthWarehouseId.removeIf(s->StringUtils.isEmpty(s));
	        List<String> listAuthSupplierId = userVo.getListAuthSupplierId();
	        listAuthSupplierId.removeIf(s->StringUtils.isEmpty(s));
	        orderCopyDetailInfoRequestDto.setListAuthWarehouseId(listAuthWarehouseId);
	        orderCopyDetailInfoRequestDto.setListAuthSupplierId(listAuthSupplierId);

			OrderCopyBaseInfoDto orderCopyBaseInfoDto = getOrderCopyBaseInfoDto(orderCopyDetailInfoRequestDto);
			if (orderCopyBaseInfoDto != null) {
				String odOrderId = String.valueOf(orderCopyBaseInfoDto.getOdOrderId());
				String odid = orderCopyBaseInfoDto.getOdid();
				if (StringUtil.isNotEmpty(odOrderId)) {

					// 임직원 지원금 사용여부
					int orderEmployeeDiscountCnt = orderDetailBiz.getOrderEmployeeDiscountCnt(odOrderId);

					OrderDetailPayListResponseDto orderDetailPayListResponseDto = (OrderDetailPayListResponseDto)orderDetailBiz.getOrderDetailPayList(odOrderId).getData();
					List<OrderDetailPayDetlListDto> orderDetailPayDetlList = orderDetailPayListResponseDto.getPayDetailList();
					List<OrderDetailPayListDto> orderDetailPayList = orderDetailPayListResponseDto.getPayList();

					List<OrderDetailGoodsListDto> orderDetailGoodsListDtoList = ((OrderDetailGoodsListResponseDto)orderDetailBiz.getOrderDetailGoodsList(odOrderId).getData()).getRows();

					// 출고처pk, 배송정책pk로 그룹핑
					Map<String, List<OrderDetailGoodsListDto>> groupingOrderDetailList = orderDetailGoodsListDtoList.stream()
							.collect(groupingBy(OrderDetailGoodsListDto::getGrpShippingId,LinkedHashMap::new,toList()));


					for(String key : groupingOrderDetailList.keySet()){
						List<OrderDetailGoodsListDto> orderDetailGoodsList = groupingOrderDetailList.get(key).stream().filter(f->f.getOdOrderDetlSeq() != 0).collect(toList()); // 녹즙 내맘대로 껍데기상품 제외
						HashMap<String, Integer> overlapBuyItem = new HashMap<String, Integer>();   // 중복 품목 재고 정보
						List<ArrivalScheduledDateDto> stockList = new ArrayList<>();
						List<List<ArrivalScheduledDateDto>> groupStockList = new ArrayList<>();
						Map<String, List<ArrivalScheduledDateDto>> groupStockMap = new HashMap<>();

						for (OrderDetailGoodsListDto item : orderDetailGoodsList) {
							// 상품 날짜별 재고 리스트 조회
							try {
								if (GoodsEnums.GoodsType.PACKAGE.getCode().equals(item.getGoodsTpCd())){
									List<PackageGoodsListDto> packageGoodsList = goodsGoodsBiz.getPackagGoodsInfoList(StringUtil.nvlLong(item.getIlGoodsId()), false, false, false, null, StringUtil.nvlInt(item.getOrderCnt()));

									if (packageGoodsList.size() > 0) {
										RecalculationPackageDto recalculationPackageGoods = goodsGoodsBiz.getRecalculationPackage(GoodsEnums.SaleStatus.ON_SALE.getCode(), packageGoodsList);
										stockList = recalculationPackageGoods.getArrivalScheduledDateDtoList();

										if(stockList == null) {
											isSuccess = false;
											failMessage += "["+item.getIlGoodsId()+"] : 묶음 구성 상품 - "+ GoodsEnums.SaleStatus.findByCode(recalculationPackageGoods.getSaleStatus()).getCodeName() +" <br>";
										}

									} else {
										isSuccess = false;
										failMessage += "["+item.getIlGoodsId()+"] : 묶음상품에 구성상품이 존재 하지 않음 <br>";
									}
								} else {
									stockList = goodsGoodsBiz.getArrivalScheduledDateDtoList(StringUtil.nvlLong(item.getUrWarehouseId()), StringUtil.nvlLong(item.getIlGoodsId()), false, StringUtil.nvlInt(item.getOrderCnt()), StringUtil.nvl(item.getGoodsCycleTpCd()));

									// 일일배송일 경우 주기에 따라 도착예정일 수정
									if(GoodsEnums.SaleType.DAILY.getCode().equals(item.getSaleType()) && !"Y".equals(item.getDailyBulkYn())
											&& !ShoppingEnums.CartPromotionType.GREENJUICE_SELECT.getCode().equals(item.getPromotionTp())) {

										String weekCode = null;
										if (GoodsEnums.GoodsCycleType.DAY1_PER_WEEK.getCode().equals(item.getGoodsCycleTpCd())) {
											if (Integer.parseInt(item.getMonCnt()) > 0){
												weekCode = GoodsEnums.WeekCodeByGreenJuice.MON.getCode();
											}
											if (Integer.parseInt(item.getTueCnt()) > 0){
												weekCode = GoodsEnums.WeekCodeByGreenJuice.TUE.getCode();
											}
											if (Integer.parseInt(item.getWedCnt()) > 0){
												weekCode = GoodsEnums.WeekCodeByGreenJuice.WED.getCode();
											}
											if (Integer.parseInt(item.getThuCnt()) > 0){
												weekCode = GoodsEnums.WeekCodeByGreenJuice.THU.getCode();
											}
											if (Integer.parseInt(item.getFriCnt()) > 0){
												weekCode = GoodsEnums.WeekCodeByGreenJuice.FRI.getCode();
											}
										}
										stockList = goodsGoodsBiz.getArrivalScheduledDateDtoListByWeekCode(stockList, item.getUrWarehouseId(), item.getGoodsCycleTpCd(), weekCode);
									}
								}
								if(stockList != null) {

									// 중복 품목 재고 정보
									goodsGoodsBiz.convertOverlapBuyItem(overlapBuyItem, item.getIlItemCd(), StringUtil.nvlInt(item.getOrderCnt()), false, stockList);

									groupStockMap.put(String.valueOf(item.getOdOrderDetlId()), stockList);
									if (stockList.size() > 0) {
										groupStockList.add(stockList);
									}
								} else {
									isSuccess = false;
									failMessage += "["+item.getIlGoodsId()+"] : 재고 리스트 조회 에러";
								}

							} catch (Exception e) {
								isSuccess = false;
								failMessage += "["+item.getIlGoodsId()+"] : 재고 리스트 조회 에러";
								e.printStackTrace();
							}
						}

						// 배송가능한 첫번째 도착예정일 조회
						boolean stockFlag = true;
						LocalDate firstDt = LocalDate.now();
						if (CollectionUtils.isNotEmpty(stockList) && CollectionUtils.isNotEmpty(groupStockList)) {
							List<LocalDate> allDate = new ArrayList<>();
							try {
								allDate = goodsGoodsBiz.intersectionArrivalScheduledDateListByDto(groupStockList);
							} catch (Exception e) {
								isSuccess = false;
								failMessage += "도착예정일 조회 에러<br>";
								e.printStackTrace();
							}

							if (allDate != null && allDate.size() > 0) {
								firstDt = allDate.get(0);
							}else{
								isSuccess = false;
								failMessage += "도착예정일 교집합 없음<br>";
							}
						} else {
							stockFlag = false;
						}

						// 상품별 도착예정일 세팅
						for (OrderDetailGoodsListDto item : orderDetailGoodsList) {

							if(stockFlag){
								ArrivalScheduledDateDto arrivalScheduledDateDto = null;
								try {
									arrivalScheduledDateDto = goodsGoodsBiz.getArrivalScheduledDateDtoByArrivalScheduledDate(groupStockMap.get(String.valueOf(item.getOdOrderDetlId())), firstDt);
								} catch (Exception e) {
									isSuccess = false;
									failMessage += "["+item.getIlGoodsId()+"] : I/F일자 정보 오류<br>";
									e.printStackTrace();
								}

								LocalDate orderIfDt		= LocalDate.now();
								LocalDate shippingDt	= LocalDate.now();
								LocalDate deliveryDt	= LocalDate.now();

								if (arrivalScheduledDateDto != null){
									orderIfDt	= arrivalScheduledDateDto.getOrderDate();
									shippingDt	= arrivalScheduledDateDto.getForwardingScheduledDate();
									deliveryDt	= arrivalScheduledDateDto.getArrivalScheduledDate();
								} else {

									isSuccess = false;
									failMessage += "["+item.getIlGoodsId()+"] : I/F일자 정보 없음 <br>";
								}

								item.setOrderIfDt(orderIfDt.toString());
								item.setShippingDt(shippingDt.toString());
								item.setDeliveryDt(deliveryDt.toString());

							}else{
								
								isSuccess = false;
								failMessage += "["+item.getIlGoodsId()+"] : I/F일자 정보 없음<br>";
							}
						}
					}

					if(!isSuccess){
						return ApiResult.result(failMessage, OrderEnums.OrderCopyErrMsg.ORDER_GOODS_COPY_ERROR);
					}

					OrderBuyerDto orderBuyerDto = ((OrderBuyerResponseDto)orderDetailBiz.getOrderBuyer(odid).getData()).getRows();
					//주문자 환불계좌 조회
					CommonGetRefundBankRequestDto dto = new CommonGetRefundBankRequestDto();
					dto.setUrUserId(orderBuyerDto.getUrUserId());
					CommonGetRefundBankResultVo refundInfo = userBuyerBiz.getRefundBank(dto);

					return ApiResult.success(OrderCopyDetailInfoResponseDto.builder()
							// 주문PK
							.odOrderId(orderCopyBaseInfoDto.getOdOrderId())
							// 주문자 유형
							.buyerTypeCd(orderCopyBaseInfoDto.getBuyerTypeCd())
							// 주문자 정보
							.orderBuyerDto(orderBuyerDto)
							// 상품정보 리스트
							.orderDetailGoodsList(orderDetailGoodsListDtoList)
							// 배송정보 리스트
							.orderDetailShippingZoneList(((OrderDetailShippingZoneListResponseDto)orderDetailBiz.getOrderDetailShippingZoneList(odOrderId, "").getData()).getRows())
							// 결제상세정보 리스트
							.orderDetailPayDetlList(orderDetailPayDetlList)
							// 결제정보 리스트
							.orderDetailPayList(orderDetailPayList)
							.orderEmployeeDiscountCnt(orderEmployeeDiscountCnt)
							// 매장PK -> 매장배송/픽업 주문은 주문복사 불가
							.urStoreId(orderCopyBaseInfoDto.getUrStoreId())
							//환불계좌 조회
							.refundBankResult(refundInfo)
							.build()
							);
				}
			}
		}
		return ApiResult.success();
	}

	/**
     * 조회구분별(주문번호, 외부몰 주문번호, 수집몰주문번호) 주문 기본정보 조회
     *
     * @param orderCopyDetailInfoRequestDto
     * @return OrderCopyBaseInfoDto
     */
	protected OrderCopyBaseInfoDto getOrderCopyBaseInfoDto(OrderCopyDetailInfoRequestDto orderCopyDetailInfoRequestDto) throws Exception {
		return orderCopyMapper.getOrderCopyBaseInfoDto(orderCopyDetailInfoRequestDto);
	}

    /**
     * 주문복사 주문 수량 변경 결제상세정보 조회
     *
     * @param orderCopyDetailInfoRequestDto
     * @return ApiResult<?>
     */
	protected ApiResult<?> getOrderCopyCntChangeInfo(OrderCopyDetailInfoRequestDto orderCopyDetailInfoRequestDto) throws Exception {
		List<OrderCopyBaseInfoDto> orderCopyList = getOrderCntChangeInfo(orderCopyDetailInfoRequestDto);
		log.debug("orderCopyList :: <{}>", orderCopyList);

		int paidPrice = orderCopyList.stream().mapToInt(OrderCopyBaseInfoDto::getPaidPrice).sum();						//결제예정 합
		int cartCouponPrice = orderCopyList.stream().mapToInt(OrderCopyBaseInfoDto::getCartCouponPrice).sum();			//장바구니 합
		int goodsCouponPrice = orderCopyList.stream().mapToInt(OrderCopyBaseInfoDto::getGoodsCouponPrice).sum();		//상품쿠폰 합
		int directPrice = orderCopyList.stream().mapToInt(OrderCopyBaseInfoDto::getDirectPrice).sum();					//즉시할인 합
		int shippingCouponPrice = orderCopyList.stream().mapToInt(OrderCopyBaseInfoDto::getShippingCouponPrice).sum();	//배송비 쿠폰할인금 합계
		int couponPrice = cartCouponPrice + goodsCouponPrice + shippingCouponPrice;		//쿠폰할인 합계
		int salePrice = paidPrice + cartCouponPrice + goodsCouponPrice;	//판매가 합계
		int shippingPrice = 0;			//배송비 합계
		int pointPrice = 0;				//적립금할인
		int paymentPrice = 0;			//실결제금액
		int sumGoosPrice = 0;			//배송비 계산하기 위한 전체 상품가격
		int sumOrderCnt = 0;			//배송비 계산하기 위한 전체 상품갯수
		String zipCd = "";				//배송비 계산하기 위한 우편번호

		log.debug("결제예정금액 :: <{}>, 장바구니쿠폰 :: <{}>, 상품쿠폰 :: <{}>, 즉시쿠폰 :: <{}>, 배송비쿠폰 :: <{}>", paidPrice, cartCouponPrice, goodsCouponPrice, directPrice, shippingCouponPrice);
		//orgShippingPrice

		// 통합몰인경우에만 배송비 재계산
		if (SellersEnums.SellersGroupCd.MALL.getCode().equals(orderCopyDetailInfoRequestDto.getSellersGroupCd())) {

			Map<String, List<OrderCopyBaseInfoDto>> shippingTemplate = null;
			// 배송 정책별 그룹
			shippingTemplate = orderCopyList.stream().collect(Collectors.groupingBy(OrderCopyBaseInfoDto::getShippingZone, LinkedHashMap::new, Collectors.toList()));

			log.debug("shippingTemplate :: <{}>", shippingTemplate);


			//배송비
			for (String key : shippingTemplate.keySet()) {
				String[] shippingZoneArr = key.split(Constants.ARRAY_SEPARATORS);
				long urWarehouseId = Long.parseLong(shippingZoneArr[0]);    // 출고처PK
				long ilShippingId = Long.parseLong(shippingZoneArr[1]);        // 배송정책PK
				String bundleYn = shippingZoneArr[2];                        // 합배송여부

				log.debug("urWarehouseId :: <{}>, ilShippingId :: <{}>, bundleYn :: <{}>", urWarehouseId, ilShippingId, bundleYn);
				List<OrderCopyBaseInfoDto> shippingList = shippingTemplate.get(key);

				log.debug("shippingList :: <{}>", shippingList);

				//배송비 구하기 위한 상품금액, 상품 수량 초기화
				sumGoosPrice = 0;
				sumOrderCnt = 0;
				for (OrderCopyBaseInfoDto dto : shippingList) {
					log.debug("리스트 안에 dto ::: <{}>", dto);
					sumGoosPrice += dto.getGoodsPrice();
					sumOrderCnt += dto.getOrderCnt();
					zipCd = dto.getRecvZipCd();
				}

				log.debug("sumGoosPrice :: <{}>, sumOrderCnt:: <{}>, zipCd :: <{}>", sumGoosPrice, sumOrderCnt, zipCd);
				ShippingDataResultVo shippingDataResultVo = goodsShippingTemplateBiz.getShippingInfoByShippingTmplId(ilShippingId);
				log.debug("shippingDataResultVo :: <{}>", shippingDataResultVo);

				ShippingPriceResponseDto shippingPriceDto = goodsShippingTemplateBiz.getShippingPrice(shippingDataResultVo, sumGoosPrice, sumOrderCnt, zipCd);

				log.debug("shippingPriceDto :: <{}>", shippingPriceDto);
				shippingPrice += shippingPriceDto.getShippingPrice();

				log.debug("배송비 합계 ::: <{}>", shippingPrice);
			}
		} else {
			shippingPrice = orderCopyList.stream().mapToInt(OrderCopyBaseInfoDto::getOrgShippingPrice).sum();
		}
		paymentPrice = salePrice + shippingPrice - couponPrice - pointPrice;

		log.debug("판매가합계 :: <{}>, 총배송비 :: <{}>, 즉시할인 :: <{}>, 쿠폰할인 :: <{}>, 적립금 :: <{}>, 결제금액 :: <{}>", salePrice, shippingPrice, directPrice, couponPrice, pointPrice, paymentPrice);

		OrderDetailPayDetlListDto payDto = new OrderDetailPayDetlListDto();
		payDto.setSalePrice(String.valueOf(salePrice));						//판매가합계
		payDto.setCartCouponPrice(String.valueOf(cartCouponPrice));			//장바구니할인금액
		payDto.setGoodsCouponPrice(String.valueOf(goodsCouponPrice));		//상품쿠폰할인금액
		payDto.setShippingCouponPrice(String.valueOf(shippingCouponPrice));	//배송비 쿠폰할인금 합계
		payDto.setDiscountCouponPrice(String.valueOf(couponPrice));			//상품쿠폰 + 장바구니쿠폰 + 배송비쿠폰 합계
		payDto.setDirectPrice(String.valueOf(directPrice));					//즉시할인금액 합계
		payDto.setPaymentTargetPrice(String.valueOf(salePrice + shippingPrice - cartCouponPrice - goodsCouponPrice));	//결제대상금액
		payDto.setPaidPrice(String.valueOf(paidPrice));						//결제예정 합계
		payDto.setShippingPrice(String.valueOf(shippingPrice));				//배송비합계
		payDto.setPointPrice(String.valueOf(pointPrice));					//적립금합계
		payDto.setPaymentPrice(String.valueOf(paymentPrice));				//실결제금액
		return ApiResult.success(payDto);
	}

	/**
	 * 상품정보 수량 변경 시 금액 변경 조회
	 * @param orderCopyDetailInfoRequestDto
	 * @return
	 * @throws Exception
	 */
	protected List<OrderCopyBaseInfoDto> getOrderCntChangeInfo(OrderCopyDetailInfoRequestDto orderCopyDetailInfoRequestDto) throws Exception {
		return orderCopyMapper.getOrderCopyCntChangeInfo(orderCopyDetailInfoRequestDto);
	}

	/**
	 * 상품정보 수량 변경 시 금액 변경 조회 (매출만 전송)
	 * @param orderCopyDetailInfoRequestDto
	 * @return
	 * @throws Exception
	 */
	protected List<OrderCopyBaseInfoDto> getOrderCntChangeSalIfInfo(OrderCopyDetailInfoRequestDto orderCopyDetailInfoRequestDto) throws Exception {
		return orderCopyMapper.getOrderCntChangeSalIfInfo(orderCopyDetailInfoRequestDto);
	}

	/**
	 * 주문복사 저장
	 * @param reqDto
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = {BaseException.class,Exception.class})
	protected ApiResult<?> addOrderCopy(OrderCopySaveRequestDto reqDto, long userId) throws Exception {
		return saveOrderCopyDetailInfo(reqDto, userId);
	}

	/**
	 * 주문복사 저장
	 * @param reqDto
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	protected ApiResult<?> saveOrderCopyDetailInfo(OrderCopySaveRequestDto reqDto, long userId) throws Exception {
		log.debug("저장 parameter :: <{}>", reqDto);
		OrderCopySaveResponseDto resDto = null;
		OrderEnums.OrderRegistrationResult resutlCode = OrderEnums.OrderRegistrationResult.SUCCESS;
		long srchOdOrderId = reqDto.getOdOrderId();			//복사대상 주문PK
		String orderCopyOdid	= reqDto.getOdid();			//복사대상 주문번호
		long srchOdShippingZoneId = 0;						//복사대상 주문배송지PK
		long srchOdShippingPriceId = 0;						//복사대상 주문배송비PK
		long srchOdOrderDetlId = 0;							//복사대상 주문상세PK

		String orderPaymentType = reqDto.getPaymentType();	//결제수단
		String batchExecFl = "";							//배치실행여부(N: I/F배치실행안함, Y: I/F배치실행완료, C: 주문복사를 통해 매출만 연동했을 경우)

		long odOrderId = 0;									//신규 주문 PK
		String odid = null;										//신규 주문번호
		long odShippingZoneId = 0;							//신규 주문 배송지 PK
		long odShippingPriceId = 0;							//신규 주문 배송비 PK
		long odOrderDetlId = 0;								//신규 주문상세PK
		AtomicInteger odOrderDetlSeq = new AtomicInteger(0);//신규 주문상세 순번(라인번호) 주문번호에 대한 순번
		long odOrderDetlStepId = 0;							//신규 주문상세 정렬 키
		int  odOrderDetlDepthId = 0;						//신규 주문상세 뎁스
		long odOrderDetlParentId = 0;						//신규 주문상세 부모 ID
		long odPaymentMasterId = 0;							//결제마스터 PK
		int  totSalePrice = 0;								//판매가 합계 금액
		int  totGoodsCouponPrice = 0;						//상품쿠폰 합계 금액
		int  totCartCouponPrice = 0;						//장바구니쿠폰 합계 금액
		int  totDirectPrice = 0;							//상품,장바구니쿠폰 할인 제외한 할인금액
		int  totPaidPrice = 0;								//결제 합계 금액
		int  totTaxablePrice = 0;							//과세결제금액 합계
		int  totNonTaxablePrice = 0;						//비과세결제금액 합계
		int  totShippingPrice = 0;							//배송비합계
		int  goodsCouponPrice = 0;							//상품쿠폰금액
		int  cartCouponPrice = 0;							//장바구니쿠폰금액
		int  paidPrice = 0;									//결제금액
		int  shippingPrice = 0;								//배송비
		int  sumGoosPrice = 0;								//출고지,배송정책 그룹별 상품금액 합계
		int  sumOrderCnt = 0;								//출고지,배송정채 그룹별 상품걔수 합계
		String orderCopySalIfYn = "N";						// 주문복사 매출만 연동 여부
		String zipCd = null;								//지역에 따라서 배송비 변경
		Map<Long, Long> packMap = new HashMap<Long, Long>();
		Map<Long, Long> packStepMap = new HashMap<Long, Long>();
		try {
			log.debug("신규 주문상세 순번 odOrderDetlSeq :: <{}>", odOrderDetlSeq);
			log.debug("주문 복사 [OD_ORDER] 주문마스터 INSERT!!!");

			if (reqDto.getOrderIfType().equals(OrderIfType.ORDER_IF.getCode())) {
				batchExecFl = OrderIfType.ORDER_IF.getAttr1();
			} else if (reqDto.getOrderIfType().equals(OrderIfType.SAL_IF.getCode())) {
				batchExecFl = OrderIfType.SAL_IF.getAttr1();
			}

			//주문 정보
			odOrderId = orderRegistrationBiz.getOrderIdSeq();			//신규 주문PK
			odid = orderRegistrationBiz.getOrderNumber(odOrderId);		//신규주문번호

			log.debug("주문PK odOrderId :: <{}>, 주문번호 odid :: <{}>, 검색주문PK srchOdOrderId ::: <{}>", odOrderId, odid, srchOdOrderId);

			if (OrderIfType.SAL_IF.getCode().equals(reqDto.getOrderIfType())){
				orderCopySalIfYn = "Y";
			}

			OrderVo orderVo = OrderVo.builder()
					.odOrderId(odOrderId)				//신규 주문 PK
					.odid(odid) 						//주문번호
					.orderPaymentType(orderPaymentType)	//결제수단 : 공통코드(PAY_TP)
					.orderYn(AllTypeYn.ALL_TYPE_Y.getCode())
					.orderCopySalIfYn(orderCopySalIfYn)
					.orderCopyOdid(orderCopyOdid)
					.goodsNm(reqDto.getRepGoodsNm())
					.ilGoodsId(reqDto.getRepIlGoodsId())
					.orderCopyYn("Y")
					.orderCreateYn("N")
					.build();
			log.debug("주문 정보 orderVo :: <{}>" , orderVo);
			orderRegistrationBiz.addOrderCopyOdOrder(orderVo, srchOdOrderId);	//[OD_ORDER] 주문마스터 INSERT

			//주문상태일자 정보
			OrderDtVo orderDtVo = OrderDtVo.builder()
					.odOrderId(odOrderId) 	//신규 주문 PK
					.irId(userId) 			//입금대기중 등록자
					.build();
			orderRegistrationBiz.addOrderDt(orderDtVo);	//[OD_ORDER_DT] 주문상태일자 INSERT

			//상품관련 정보
			OrderCopyDetailInfoRequestDto goodsReqDto = new OrderCopyDetailInfoRequestDto();
			goodsReqDto.setOrderCntChangList(reqDto.getOrderGoodsList());

			List<OrderCopyBaseInfoDto> orderDetlList;
			// 매출만 전송이면
			if(reqDto.getOrderIfType().equals(OrderIfType.SAL_IF.getCode())) {
				orderDetlList = this.getOrderCntChangeSalIfInfo(goodsReqDto);
			}
			// 그 이외
			else {
				orderDetlList = this.getOrderCntChangeInfo(goodsReqDto);
			}

			// 배송 정책별 그룹
			Map<String, List<OrderCopyBaseInfoDto>> shippingTemplate = orderDetlList.stream().collect(Collectors.groupingBy(OrderCopyBaseInfoDto::getShippingZone, LinkedHashMap::new, Collectors.toList()));

			//배송지 정보
			List<OrderShippingZoneVo> shippingZoneList = reqDto.getOrderShippingList();
			Map<Long, List<OrderShippingZoneVo>> shippingZoneMap = shippingZoneList.stream().collect(Collectors.groupingBy(OrderShippingZoneVo::getOdShippingZoneId, LinkedHashMap::new, Collectors.toList()));
			log.debug("배송지 정보 shippingZoneMap :: :<{}>", shippingZoneMap);

			//배송비 정보
			List<OrderShippingPriceVo> shippingPriceList = reqDto.getOrderShippingPriceList();
			Map<Long, List<OrderShippingPriceVo>> shippingPriceMap = shippingPriceList.stream().collect(Collectors.groupingBy(OrderShippingPriceVo::getOdShippingPriceId, LinkedHashMap::new, Collectors.toList()));
			log.debug("배송비 정보 shippingPriceMap :: :<{}>", shippingPriceMap);



			for(Long zoneKey : shippingZoneMap.keySet()) {
				//배송지 입력
				log.debug("zoneKey :: <{}>", zoneKey);
				List<OrderShippingZoneVo> zoneList = shippingZoneMap.get(zoneKey);
				log.debug("배송지 데이터 유무 ::: <{}>", CollectionUtils.isNotEmpty(zoneList));
				if(CollectionUtils.isNotEmpty(zoneList)) {
					OrderShippingZoneVo zoneVo = zoneList.get(0);
					log.debug("zoneVo :: <{}>", zoneVo);

					log.debug("주문 복사 [OD_SHIPPING_ZONE] 주문상세 배송지 INSERT!!!");
					odShippingZoneId = orderRegistrationSeqBiz.getOrderShippingZoneSeq();	//신규 주문 배송지 PK
					srchOdShippingZoneId = zoneVo.getOdShippingZoneId();					//복사대상 주문배송지 PK
					zoneVo.setOdShippingZoneId(odShippingZoneId);							//신규 주문배송지PK
					zoneVo.setOdOrderId(odOrderId); 										//신규 주문PK

					log.debug("주문 배송지 PK odShippingZoneId :: <{}>, 복사대상 주문배송지PK :: <{}>" , zoneVo.getOdShippingZoneId(), srchOdShippingZoneId);
					log.debug("주문 배송지 정보 zoneVo :: <{}>" , zoneVo.toString());
					orderRegistrationBiz.addOrderCopyShippingZone(zoneVo, srchOdShippingZoneId);	// [OD_SHIPPING_ZONE] 주문상세 배송지 INSERT

					log.debug("주문 복사 [OD_SHIPPING_ZONE_HIST] 주문상세 배송지 이력 INSERT!!!");
					OrderShippingZoneHistVo orderShippingZoneHistVo = OrderShippingZoneHistVo.builder()
							.odShippingZoneId(odShippingZoneId)
							.odOrderId(odOrderId)
							.recvNm(zoneVo.getRecvNm())
							.recvHp(zoneVo.getRecvHp())
							.recvTel(zoneVo.getRecvTel())
							.recvMail(zoneVo.getRecvMail())
							.recvZipCd(zoneVo.getRecvZipCd())
							.recvAddr1(zoneVo.getRecvAddr1())
							.recvAddr2(zoneVo.getRecvAddr2())
							.recvBldNo(zoneVo.getRecvBldNo())
							.deliveryMsg(zoneVo.getDeliveryMsg())
							.doorMsgCd(zoneVo.getDoorMsgCd())
							.doorMsg(zoneVo.getDoorMsg())
							.build();
					log.debug("주문 배송지이력 정보 orderShippingZoneHistVo :: <{}>" , orderShippingZoneHistVo.toString());
					orderRegistrationBiz.addOrderCopyShippingZoneHist(orderShippingZoneHistVo, srchOdShippingZoneId);	// [OD_SHIPPING_ZONE_HIST] 주문상세 배송지 이력 INSERT

					for(Long priceKey : shippingPriceMap.keySet()) {

						List<OrderShippingPriceVo> priceList = shippingPriceMap.get(priceKey);
						log.debug("priceKey :: <{}>", priceKey);
						log.debug("배송비 데이터 유무 ::: <{}>", CollectionUtils.isNotEmpty(priceList));

						if(CollectionUtils.isNotEmpty(priceList)) {
							OrderShippingPriceVo priceVo = new OrderShippingPriceVo().copy(priceList.get(0));
							log.debug("priceVo :: <{}>", priceVo);

							//배송비 입력
							odShippingPriceId = orderRegistrationBiz.getOrderShippingPriceSeq();	//주문 배송비 PK
							srchOdShippingPriceId = priceVo.getOdShippingPriceId();					//복사대상 주문배송비 PK

							log.debug("주문 복사 [OD_SHIPPING_PRICE] 주문배송비 INSERT!!!");

							//배송비를 구한다.
							log.debug("shippingTemplate :: <{}>", shippingTemplate);
							shippingPrice = 0;
							//주문생성/주문복사시 배송비 무료 조건인 경우 배송비 0 처리
							if("Y".equals(reqDto.getFreeShippingPriceYn())) {
								priceVo.setIlShippingTmplId(OrderShippingConstants.FREE_SHIPPING_TEMPLATE_ID);  //주문생성_무료배송비 배송정책탬플릿 세팅
								priceVo.setPaymentMethod(1);
								priceVo.setMethod(1);
							}
							if (SellersEnums.SellersGroupCd.MALL.getCode().equals(reqDto.getSellersGroupCd())) {
								for (String shippingKey : shippingTemplate.keySet()) {

									String[] shippingZoneArr = shippingKey.split(Constants.ARRAY_SEPARATORS);
									long urWarehouseId = Long.parseLong(shippingZoneArr[0]);    // 출고처PK
									long ilShippingId = Long.parseLong(shippingZoneArr[1]);        // 배송정책PK
									String bundleYn = shippingZoneArr[2];                        // 합배송여부
									long shippingPriceId = Long.parseLong(shippingZoneArr[3]);    // 배송비PK

									//주문생성/주문복사시 배송비 무료 조건인 경우 배송비 0 처리
									if("Y".equals(reqDto.getFreeShippingPriceYn())) {
										ilShippingId = OrderShippingConstants.FREE_SHIPPING_TEMPLATE_ID;  //주문생성_무료배송비 배송정책탬플릿 세팅
									}
									log.debug("urWarehouseId :: <{}>, ilShippingId :: <{}>, bundleYn :: <{}>, shippingPriceId ::: <{}>", urWarehouseId, ilShippingId, bundleYn, shippingPriceId);
									log.debug("shippingPriceId ==  priceKey :: <{}>=====<{}>", shippingPriceId, priceKey);

									if (shippingPriceId == priceKey) {
										//배송비 구하기 위한 상품금액, 상품 수량 초기화
										sumGoosPrice = 0;
										sumOrderCnt = 0;
										List<OrderCopyBaseInfoDto> shippingList = shippingTemplate.get(shippingKey);
										log.debug("shippingList :: <{}>", shippingList);

										for (OrderCopyBaseInfoDto dto : shippingList) {
											log.debug("리스트 안에 dto ::: <{}>", dto);
											sumGoosPrice += dto.getGoodsPrice();
											sumOrderCnt += dto.getOrderCnt();
											zipCd = dto.getRecvZipCd();
										}
										log.debug("sumGoosPrice :: <{}>, sumOrderCnt:: <{}>, zipCd :: <{}>", sumGoosPrice, sumOrderCnt, zipCd);
										ShippingDataResultVo shippingDataResultVo = goodsShippingTemplateBiz.getShippingInfoByShippingTmplId(ilShippingId);
										log.debug("shippingDataResultVo :: <{}>", shippingDataResultVo);
										ShippingPriceResponseDto shippingPriceDto = goodsShippingTemplateBiz.getShippingPrice(shippingDataResultVo, sumGoosPrice, sumOrderCnt, zipCd);

										log.debug("shippingPriceDto :: <{}>", shippingPriceDto.toString());


										shippingPrice += shippingPriceDto.getShippingPrice();

										log.debug("배송비 합계 ::: <{}>", shippingPrice);
									}
								}
							} else {
								shippingPrice = priceVo.getShippingPrice();
							}
							totShippingPrice += shippingPrice;	//배송비합계
							priceVo.setShippingPrice(shippingPrice); 								//배송비
							priceVo.setOdShippingPriceId(odShippingPriceId);


							log.debug("주문 배송비 PK odShippingPriceId :: <{}>, 복사대상 주문배송비PK :: <{}>, 배송비 ::: <{}>" , priceVo.getOdShippingPriceId(), srchOdShippingPriceId, shippingPrice);

							orderRegistrationBiz.addOrderCopyShippingPrice(priceVo, srchOdShippingPriceId, reqDto.getSellersGroupCd()); // [OD_SHIPPING_PRICE] 주문배송비 INSERT

							//주문상세 입력
							log.debug("orderDetlList :: <{}>", orderDetlList);
							log.debug("주문상세 유무  :: <{}>", CollectionUtils.isNotEmpty(orderDetlList));

							for (OrderCopyBaseInfoDto detlDto : orderDetlList) {
								log.debug("주문 복사 [OD_ORDER_DETL] 주문상세 INSERT!!!");
								log.debug("delDto :: <{}>", detlDto);
								log.debug("orderDetlList.size :: <{}>", orderDetlList.size());
								log.debug("배송지 PK <{}>==<{}>, 배송비 PK <{}>==<{}>, 배송정책 PK <{}>==<{}>", srchOdShippingZoneId, detlDto.getOdShippingZoneId(), srchOdShippingPriceId, detlDto.getOdShippingPriceId(), priceVo.getIlShippingTmplId(), detlDto.getIlShippingTmplId());

								log.debug("srchOdShippingZoneId :: <{}  ==== {}>", srchOdShippingZoneId, detlDto.getOdShippingZoneId());
								log.debug("srchOdShippingPriceId :: <{}  ==== {}>", srchOdShippingPriceId, detlDto.getOdShippingPriceId());
								log.debug("priceVo.getIlShippingTmplId() :: <{}  ==== {}>", priceVo.getIlShippingTmplId(), detlDto.getIlShippingTmplId());

								if("Y".equals(reqDto.getFreeShippingPriceYn())) {
									detlDto.setIlShippingTmplId(OrderShippingConstants.FREE_SHIPPING_TEMPLATE_ID);  //주문생성_무료배송비 배송정책탬플릿 세팅
								}
								if (srchOdShippingZoneId == detlDto.getOdShippingZoneId()
										&& srchOdShippingPriceId == detlDto.getOdShippingPriceId()
										&& priceVo.getIlShippingTmplId() == detlDto.getIlShippingTmplId()) {	//복사대상 주문배송지 와 복사할 주문 배송비가 같아야 한다.
									srchOdOrderDetlId = detlDto.getOdOrderDetlId();			//복사대상 주문상세PK
									odOrderDetlId = orderRegistrationBiz.getOrderDetlSeq();	//신규 주문상세PK
									odOrderDetlStepId = (odOrderDetlId * 1000) + 999;		//신규 주문상세 정렬 키
									odOrderDetlDepthId = 1;									//신규 주문상세 뎁스
									odOrderDetlParentId = odOrderDetlId;					//주문상세 부모 ID
									if (detlDto.getOdOrderDetlDepthId() == 2){
										if (StringUtil.nvlLong(packStepMap.get(detlDto.getOdOrderDetlParentId())) > 0 &&
												StringUtil.nvlLong(packMap.get(detlDto.getOdOrderDetlParentId())) > 0
										) {
											odOrderDetlStepId = packStepMap.get(detlDto.getOdOrderDetlParentId()) - 1;        //신규 주문상세 정렬 키
											packStepMap.put(detlDto.getOdOrderDetlParentId(), odOrderDetlStepId);

											odOrderDetlDepthId = 2;                                    //신규 주문상세 뎁스
											odOrderDetlParentId = packMap.get(detlDto.getOdOrderDetlParentId());                    //주문상세 부모 ID
										}
									}

									cartCouponPrice = detlDto.getCartCouponPrice();			//장바구니할인금액
									goodsCouponPrice = detlDto.getGoodsCouponPrice();		//상품할인금액
									paidPrice = detlDto.getPaidPrice();						//결제대상금액
									totSalePrice += detlDto.getSalePrice();					//판매가 합계
									totGoodsCouponPrice += goodsCouponPrice;				//상품쿠폰 합계
									totCartCouponPrice += cartCouponPrice;					//장바구니 합계
									totDirectPrice += detlDto.getDirectPrice();				//상품,장바구니 제외한 할인금액
									totPaidPrice += detlDto.getPaidPrice();					//결제합계
									totTaxablePrice += detlDto.getTaxablePrice();			//과세결제금액 합계
									totNonTaxablePrice += detlDto.getNonTaxablePrice();		//비과세결제금액 합계

									List<ArrivalScheduledDateDto> stockList = new ArrayList<>();
									List<List<ArrivalScheduledDateDto>> groupStockList = new ArrayList<>();

									try {

										detlDto.setIsDawnDelivery(false);

										log.debug("출고처ID :: <{}>, 상품ID :: <{}>, 새벽배송여부 :: <{}>, 주문수량 :: <{}>, 일일배송주기코드 :: <{}>"
												, StringUtil.nvlLong(detlDto.getUrWarehouseId()), StringUtil.nvlLong(detlDto.getIlGoodsId()), detlDto.getIsDawnDelivery()
												, StringUtil.nvlInt(detlDto.getOrderCnt()), detlDto.getGoodsCycleTp());

										stockList = goodsGoodsBiz.getArrivalScheduledDateDtoList(
												StringUtil.nvlLong(detlDto.getUrWarehouseId()) 	//출고처ID(출고처PK)
												, StringUtil.nvlLong(detlDto.getIlGoodsId())	//상품ID(상품PK)
												, detlDto.getIsDawnDelivery()					//새벽배송여부 (true/false)
												, StringUtil.nvlInt(detlDto.getOrderCnt())		//주문수량
												, detlDto.getGoodsCycleTp());					//일일 배송주기코드

										log.debug("stockList :: <{}>", stockList);

										if (stockList.size() <= 0){
											resDto = OrderCopySaveResponseDto.builder()
													.odOrderId(odOrderId) 							//주문PK
													.odid(odid) 									//주문번호
													.odPaymentMasterId(odPaymentMasterId) 			//주문 결제 마스터 PK
													.result(OrderEnums.OrderRegistrationResult.FAIL)//결과코드
													.errMsg("["+ detlDto.getIlGoodsId() +"] 변경 가능한 출고지시일이 없습니다.")
													.build();
										}


										groupStockList.add(stockList);
									} catch (Exception e) {
										log.debug("재고 리스트 조회 에러 에러 ::: " + e.getMessage());

										resDto = OrderCopySaveResponseDto.builder()
												.odOrderId(odOrderId) 							//주문PK
												.odid(odid) 									//주문번호
												.odPaymentMasterId(odPaymentMasterId) 			//주문 결제 마스터 PK
												.result(OrderEnums.OrderRegistrationResult.FAIL)//결과코드
												.errMsg("재고를 조회 할 때 에러 입니다.")
												.build();
										e.printStackTrace();
										return ApiResult.success(resDto);
									}

									List<LocalDate> allDate  = new ArrayList<>();
									try {
										allDate = goodsGoodsBiz.intersectionArrivalScheduledDateListByDto(groupStockList);
										log.debug("allDate ::: <{}>", allDate);
									} catch (Exception e) {
										log.debug("도착예정일 조회 에러 ::: " + e.getMessage());
										resDto = OrderCopySaveResponseDto.builder()
												.odOrderId(odOrderId) 							//주문PK
												.odid(odid) 									//주문번호
												.odPaymentMasterId(odPaymentMasterId) 			//주문 결제 마스터 PK
												.result(OrderEnums.OrderRegistrationResult.FAIL)//결과코드
												.errMsg("도착예정일 조회 할 때 에러 입니다.")
												.build();
										e.printStackTrace();
										return ApiResult.success(resDto);
									}
									LocalDate firstDt = LocalDate.now();
									if (allDate != null && allDate.size() > 0) {
										firstDt = allDate.get(0);
									}

									if (StringUtil.isNotEmpty(detlDto.getDeliveryDt())){
										firstDt = detlDto.getDeliveryDt();
									}

									ArrivalScheduledDateDto schInfoDto = goodsGoodsBiz.getArrivalScheduledDateDtoByArrivalScheduledDate(stockList, firstDt);
									log.debug("schInfoDto ::: <{}>", schInfoDto);

									if (StringUtil.isEmpty(schInfoDto)) {
										//변경 가능한 출고지시일이 없습니다.
										resDto = OrderCopySaveResponseDto.builder()
												.odOrderId(odOrderId) 							//주문PK
												.odid(odid) 									//주문번호
												.odPaymentMasterId(odPaymentMasterId) 			//주문 결제 마스터 PK
												.result(OrderEnums.OrderRegistrationResult.FAIL)//결과코드
												.errMsg("["+ detlDto.getIlGoodsId() +"] 변경 가능한 출고지시일이 없습니다.")
												.build();
										return ApiResult.success(resDto);
									}

									String orderStatus = OrderStatus.INCOM_READY.getCode();
									if (OrderEnums.PaymentType.COLLECTION.getCode().equals(orderPaymentType)){
										orderStatus = OrderStatus.INCOM_COMPLETE.getCode();
									}

									// 패키지 상품
									if (detlDto.getOdOrderDetlDepthId() == 2){

										// 패키지 메인 상품인 경우에만 저장
										if (packMap.containsKey(detlDto.getOdOrderDetlParentId()) == false) {
											OrderDetlPackVo orderDetlPackVo = OrderDetlPackVo.builder()
													.odOrderId(odOrderId)                    //신규주문PK
													.odOrderDetlId(odOrderDetlId)            //신규주문상세PK
													.odOrderDetlStepId(odOrderDetlStepId)    //주문상세정렬키
													//.orderCnt(orderCnt)					//주문수량 만약 묶음상품도 수정을 하게 되면 여기도 변경을 해야 함
													.cartCouponPrice(totGoodsCouponPrice)    //장바구니쿠폰할인금액
													.goodsCouponPrice(totCartCouponPrice)    //상품쿠폰할인금액
													.paidPrice(totPaidPrice)                //결제금액
													.build();
											orderRegistrationBiz.addOrderCopyOrderDetlPack(orderDetlPackVo, detlDto.getOdOrderDetlParentId());

											packMap.put(detlDto.getOdOrderDetlParentId(), odOrderDetlId);
											packStepMap.put(detlDto.getOdOrderDetlParentId(), odOrderDetlStepId);

											// 내맘대로 골라담기
											if (GoodsEnums.GoodsDeliveryType.DAILY.getCode().equals(detlDto.getGoodsDeliveryType())
													&& ShoppingEnums.CartPromotionType.GREENJUICE_SELECT.getCode().equals(detlDto.getPromotionTp())
											) {
												// 기존 일일상품 패턴 조회
												OrderDetlDailyVo orderDetlDailyVo = orderDetailBiz.getOrderDetlDailySchByOdOrderDetlId(detlDto.getOdOrderDetlParentId());
												orderDetlDailyVo.setOdOrderId(odOrderId);
												orderDetlDailyVo.setOdOrderDetlSeq(odOrderDetlSeq.get());
												orderDetlDailyVo.setOdOrderDetlId(odOrderDetlId);
												orderDetlDailyVo.setDailyPackYn("Y");

												OrderDetlVo detlVo = OrderDetlVo.builder()
														.deliveryDt(schInfoDto.getArrivalScheduledDate())
														.urWarehouseId(detlDto.getUrWarehouseId())
														.odShippingZoneId(odShippingZoneId)
														.build();
												orderRegistrationBiz.setOrderDetlDaily(detlVo, orderDetlDailyVo);
											}

											odOrderDetlId = orderRegistrationBiz.getOrderDetlSeq();    //신규 주문상세PK
											odOrderDetlStepId = packStepMap.get(detlDto.getOdOrderDetlParentId())-1;		//신규 주문상세 정렬 키
											packStepMap.put(detlDto.getOdOrderDetlParentId(), odOrderDetlStepId);
											odOrderDetlParentId = packMap.get(detlDto.getOdOrderDetlParentId());                    //주문상세 부모 ID
											odOrderDetlDepthId = 2;
										}

									}

									OrderDetlVo detlVo = OrderDetlVo.builder()
											.odOrderDetlId(odOrderDetlId) 						//신규 주문상세PK 셋팅
											.odOrderDetlSeq(odOrderDetlSeq.getAndIncrement()) 	//신규 주문상세 순번(라인번호)
											.odOrderId(odOrderId)								//신규 주문PK
											.odid(odid) 										//신규 주문번호
											.odOrderDetlStepId(odOrderDetlStepId) 				//신규 주문상세 정렬키
											.odOrderDetlDepthId(odOrderDetlDepthId) 			//신규 주문상세 뎁스
											.odOrderDetlParentId(odOrderDetlParentId) 			//신규 주문상세 부모 ID
											.odShippingPriceId(odShippingPriceId) 				//신규 주문 배송비 PK
											.odShippingZoneId(odShippingZoneId) 				//신규 주문 배송지 PK
											.orderStatusCd(orderStatus) 	//정상주문상태
											.orderCnt(detlDto.getOrderCnt())					//주문수량
											.cartCouponPrice(cartCouponPrice)					//장바구니쿠폰할인금액
											.goodsCouponPrice(goodsCouponPrice)					//상품쿠폰할인금액
											.paidPrice(paidPrice)								//결제금액
											.orderIfId(userId)									//주문 I/F 등록자
											.orderIfDt(schInfoDto.getOrderDate())				//주문 I/F 일자
											.shippingId(userId)									//출고예정일등록자
											.shippingDt(schInfoDto.getForwardingScheduledDate())//출고예정일자
											.deliveryId(userId)									//도착예정일등록자
											.deliveryDt(schInfoDto.getArrivalScheduledDate())	//도착예정일자
											.batchExecFl(batchExecFl) 							//배치실행여부(N: I/F배치실행안함, Y: I/F배치실행완료, C: 주문복사를 통해 매출만 연동했을 경우)
											.urWarehouseId(detlDto.getUrWarehouseId())
											.build();
									log.debug("주문 상세 정보 ::: <{}>", detlVo);
									orderRegistrationBiz.addOrderCopyOrderDetl(detlVo, srchOdOrderDetlId);	//[OD_ORDER_DETL] 주문상세 INSERT

									// 일일상품 패턴&스케쥴 등록
									if(GoodsType.DAILY.getCode().equals(detlDto.getGoodsTpCd())){
										// 기존 일일상품 패턴 조회
										OrderDetlDailyVo orderDetlDailyVo = orderDetailBiz.getOrderDetlDailySchByOdOrderDetlId(detlDto.getOdOrderDetlId());
										
										long odOrderDetlDailyId = 0;
										orderDetlDailyVo.setOdOrderDetlDailyId(odOrderDetlDailyId);
										orderDetlDailyVo.setOdOrderId(odOrderId);
										orderDetlDailyVo.setOdOrderDetlSeq(odOrderDetlSeq.get());
										orderDetlDailyVo.setOdOrderDetlId(odOrderDetlId);
										orderDetlDailyVo.setDailyPackYn("N");

										orderRegistrationBiz.setOrderDetlDaily(detlVo, orderDetlDailyVo);
									}


									if (getOrderCopyDiscountCnt(srchOdOrderId, srchOdOrderDetlId) > 0) {
										log.debug("주문 복사 [OD_ORDER_DETL_DISCOUNT] 주문상세할인금액 INSERT!!!");
										OrderDetlDiscountVo orderDetlDiscountVo = OrderDetlDiscountVo.builder()
												.odOrderDetlId(odOrderDetlId)	//신규주문상세PK
												.odOrderId(odOrderId) 			//신규주문PK
												.build();
										log.debug("주문 상세 할인 정보 ::: <{}>", orderDetlDiscountVo);
										orderRegistrationBiz.addOrderCopyOrderDetlDiscount(orderDetlDiscountVo, srchOdOrderId, srchOdOrderDetlId, goodsCouponPrice, cartCouponPrice);	//[OD_ORDER_DETL_DISCOUNT] 주문상세할인금액 INSERT
									}

								}

							}
						}
					}
				}
			}

			orderRegistrationBiz.putOrderDetlSeq(odOrderId);	//주문복사에서 주문 상세 주문상세 정렬 키 업데이트

			log.debug("totSalePrice :: <{}>, totGoodsCouponPrice :: <{}>, totCartCouponPrice :: <{}>, totDirectPrice :: <{}> ", totSalePrice, totGoodsCouponPrice, totCartCouponPrice, totDirectPrice);
			log.debug("totPaidPrice :: <{}>, totTaxablePrice :: <{}>, totNonTaxablePrice :: <{}>", totPaidPrice, totTaxablePrice, totNonTaxablePrice);

			odPaymentMasterId = orderRegistrationSeqBiz.getPaymentMasterSeq();	//결제마스터 SEQ
			log.debug("odPaymentMasterId :: <{}>", odPaymentMasterId);

			log.debug("주문복사 [OD_PAYMENT] 주문결제 INSERT");
			OrderPaymentVo orderPaymentVo = OrderPaymentVo.builder()
					.odOrderId(odOrderId)								//주문 PK
					.odClaimId(0)										//주문 클레임 PK
					.odPaymentMasterId(odPaymentMasterId)				//주문결제 마스터 PK
					.salePrice(totSalePrice) 							//판매가 합계
					.cartCouponPrice(totCartCouponPrice) 				//장바구니쿠폰할인금액 합계
	 				.goodsCouponPrice(totGoodsCouponPrice) 				//상품쿠폰할인금액 합계
					.directPrice(totDirectPrice)						//상품,장바구니쿠폰 할인 제외한 할인금액 합계
					.paidPrice(totPaidPrice) 							//결제금액 (쿠폰까지 할인된 금액) 합계
					.shippingPrice(totShippingPrice) 					//배송비합계
					.taxablePrice(totTaxablePrice + totShippingPrice)	//과세결제금액 합계
					.nonTaxablePrice(totNonTaxablePrice) 				//비과세결제금액 합계
					.paymentPrice(totPaidPrice + totShippingPrice)		//결제금액 = GOODS_PRICE-DISCOUNT_PRICE+SHIPPING_PRICE
					.pointPrice(0) 										//적립금포인트
					.build();
			log.debug("결제 정보 :: <{}>", orderPaymentVo);
			orderRegistrationBiz.addPayment(orderPaymentVo);		//[OD_PAYMENT] 주문결제 INSERT

			log.debug("주문복사 [OD_PAYMENT_MASTER] 주문결제 마스터 INSERT");

			String orderStatus = OrderStatus.INCOM_READY.getCode();


			if (OrderEnums.PaymentType.COLLECTION.getCode().equals(orderPaymentType)){
				orderStatus = OrderStatus.INCOM_COMPLETE.getCode();
			}
			// 결제 마스터 등록
			OrderPaymentMasterVo orderPaymentMasterVo = OrderPaymentMasterVo.builder()
					.odPaymentMasterId(odPaymentMasterId)					//주문 결제 마스터 PK
					.type(PayType.G.getCode())								//결제타입 (G : 결제, F : 환불 , A : 추가)
					.status(orderStatus)									//결제상태(IR:입금예정,IC:입금완료)
					.payTp(orderPaymentType)								// 결제방법
					.salePrice(totSalePrice) 								//판매가 합계
					.cartCouponPrice(totCartCouponPrice) 					//장바구니쿠폰할인금액 합계
	 				.goodsCouponPrice(totGoodsCouponPrice) 					//상품쿠폰할인금액 합계
					.directPrice(totDirectPrice)							//상품,장바구니쿠폰 할인 제외한 할인금액 합계
					.paidPrice(totPaidPrice) 								//결제금액 (쿠폰까지 할인된 금액) 합계
					.shippingPrice(totShippingPrice) 						//배송비합계
					.taxablePrice(totTaxablePrice + totShippingPrice) 		//과세결제금액 합계
					.nonTaxablePrice(totNonTaxablePrice) 					//비과세결제금액 합계
					.paymentPrice(totPaidPrice + totShippingPrice)			//결제금액 = GOODS_PRICE-DISCOUNT_PRICE+SHIPPING_PRICE
					.escrowYn(AllTypeYn.ALL_TYPE_N.getCode())				//에스크로 결제 여부
					.pointPrice(0) 											//적립금포인트
					.approvalDt(null)
					.build();

			if (OrderEnums.PaymentType.COLLECTION.getCode().equals(orderPaymentType)){
				orderPaymentMasterVo.setPayTp(OrderEnums.PaymentType.COLLECTION.getCode());
			}
			log.debug("결제 마스터 정보 :: <{}>", orderPaymentMasterVo);
			orderRegistrationBiz.addPaymentMaster(orderPaymentMasterVo);	//[OD_PAYMENT_MASTER] 주문결제 마스터 INSERT


			// 외부몰 결제시
			if (OrderEnums.PaymentType.COLLECTION.getCode().equals(orderPaymentType)){
				// 비인증 결제 주문연동 매출만연동시 결제완료시 처리
				orderVo = orderRegistrationBiz.getOrderCopySalIfYn(odOrderId);
				log.debug("주문복사 정보 :: <{}>", orderVo.toString());
				orderCopySalIfYn 	= orderVo.getOrderCopySalIfYn();
				orderCopyOdid		= orderVo.getOrderCopyOdid();

				if ("Y".equals(orderCopySalIfYn) && StringUtil.isNotEmpty(orderCopyOdid)) {
					orderCreateBiz.orderCopySalIfExecute(odOrderId, orderCopySalIfYn, orderCopyOdid);
				}
			}

			// 환불 계좌 정보 - 가상계좌 일때만
			if (OrderEnums.PaymentType.VIRTUAL_BANK.getCode().equals(orderPaymentType) && StringUtils.isNotEmpty(reqDto.getBankCode())
					&& StringUtils.isNotEmpty(reqDto.getAccountNumber()) && StringUtils.isNotEmpty(reqDto.getHolderName())){
				OrderAccountVo orderAccountVo = OrderAccountVo.builder()
						.odOrderId(odOrderId)
						.bankCd(reqDto.getBankCode())
						.accountHolder(reqDto.getHolderName())
						.accountNumber(reqDto.getAccountNumber())
						.build();
				orderRegistrationBiz.addAccount(orderAccountVo);
			}
		}
		catch (Exception e) {

			log.error(e.getMessage());
			resDto = OrderCopySaveResponseDto.builder()
					.odOrderId(odOrderId) 							//주문PK
					.odid(odid) 									//주문번호
					.odPaymentMasterId(odPaymentMasterId) 			//주문 결제 마스터 PK
					.result(OrderEnums.OrderRegistrationResult.FAIL)//결과코드
					.errMsg(e.getMessage())
					.build();
			return ApiResult.success(resDto);
		}

		resDto = OrderCopySaveResponseDto.builder()
				.odOrderId(odOrderId) 					//주문PK
				.odid(odid) 							//주문번호
				.odPaymentMasterId(odPaymentMasterId) 	//주문 결제 마스터 PK
				.orderCopySalIfYn(orderCopySalIfYn)
				.result(resutlCode)						//결과코드
				.build();

		return ApiResult.success(resDto);
	}

	/**
	 * 주문복사 저장 후 비인증 카드 결제
	 *
	 * @param reqDto
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = {BaseException.class,Exception.class})
	protected ApiResult<?> addNonCardPayment(OrderCopySaveRequestDto reqDto, long userId) throws Exception {
		OrderCopySaveResponseDto saveDto = ((OrderCopySaveResponseDto)saveOrderCopyDetailInfo(reqDto, userId).getData());

		log.debug("saveDto :: <{}>", saveDto.toString());
		log.debug("odid :: <{}>", saveDto.getOdid());
		log.debug("paymasterid :: <{}>", saveDto.getOdPaymentMasterId());
		log.debug("odid :: <{}>", saveDto.getResult());
		log.debug("odid :: <{}>", saveDto.getErrMsg());

		if (OrderEnums.OrderRegistrationResult.SUCCESS.getCode().equals(saveDto.getResult().getCode())) {
			OrderCardPayRequestDto orderCardPayRequestDto = new OrderCardPayRequestDto();
			orderCardPayRequestDto.setOdid(saveDto.getOdid());                                //주문번호
			orderCardPayRequestDto.setOdPaymentMasterId(saveDto.getOdPaymentMasterId());    //주문결제 Master PK
			orderCardPayRequestDto.setPlanPeriod(reqDto.getCardInfo().getPlanPeriod());        //할부기간
			orderCardPayRequestDto.setCardNo(reqDto.getCardInfo().getCardNo());                //카드번호
			orderCardPayRequestDto.setCardNumYy(reqDto.getCardInfo().getCardNumYy());        //카드유효월
			orderCardPayRequestDto.setCardNumMm(reqDto.getCardInfo().getCardNumMm());        //카드유효년도
			orderCardPayRequestDto.setAddInfoVal(reqDto.getCardInfo().getAddInfoVal());        //부가정보값
			orderCardPayRequestDto.setCardPass(reqDto.getCardInfo().getCardPass());            //비밀번호
			orderCardPayRequestDto.setOrderCopySalIfYn(saveDto.getOrderCopySalIfYn());        //주문복사 매출만 연동 여부
			orderCardPayRequestDto.setOdOrderId(saveDto.getOdOrderId());
			return ApiResult.success(orderCreateBiz.cardPayOrderCreate(orderCardPayRequestDto));
		} else {

			orderCreateBiz.putOrderFail(saveDto.getOdOrderId());
			OrderCardPayResponseDto orderCreateResponseDto = OrderCardPayResponseDto.builder()
					.result(OrderEnums.OrderRegistrationResult.FAIL)
					.message(saveDto.getErrMsg())
					.build();
			return ApiResult.success(orderCreateResponseDto);
		}

	}

	/**
	 *  @Desc 주문복사 할 주문상세 할인금액 건수가 있는지 조회.
	 * @param srchOdOrderId
	 * @param srchOdOrderDetlId
	 * @return
	 */
	protected int getOrderCopyDiscountCnt(long srchOdOrderId, long srchOdOrderDetlId) {
		return orderCopyMapper.getOrderCopyDiscountCnt(srchOdOrderId, srchOdOrderDetlId);
	}

	/**
	 * @Desc 주문복사 할 주문상세 패키지 건수가 있는지 조회.
	 * @param srchOdOrderDetlId
	 * @return
	 */
	protected int getOrderCopyPackageCnt(long srchOdOrderDetlId) {
		return orderCopyMapper.getOrderCopyPackageCnt(srchOdOrderDetlId);
	}

	/**
	 * @Desc 주문상품 정보 조회
	 * @param odOrderDetlIdList
	 * @return
	 */
	protected ApiResult<?> getOrderDetlGoodsSaleStatus(List<Long> odOrderDetlIdList){

		List<GoodsVo> goodsVoList = orderCopyMapper.getOrderDetlGoodsSaleStatus(odOrderDetlIdList);
		List<GoodsVo> returnGoodsVoList = new ArrayList<>();

		// 주문복사시 저장, 일시품절(시스템), 판매중지, 영구판매중지 인 경우 실패처리
		for(GoodsVo goodsVo : goodsVoList){
			if(GoodsEnums.SaleStatus.STOP_SALE.getCode().equals(goodsVo.getSaleStatusCode())
					||GoodsEnums.SaleStatus.OUT_OF_STOCK_BY_SYSTEM.getCode().equals(goodsVo.getSaleStatusCode())
					||GoodsEnums.SaleStatus.STOP_PERMANENT_SALE.getCode().equals(goodsVo.getSaleStatusCode())
					||GoodsEnums.SaleStatus.SAVE.getCode().equals(goodsVo.getSaleStatusCode())){
				returnGoodsVoList.add(goodsVo);
			}
		}

		return ApiResult.success(returnGoodsVoList);
	}

	/**
	 * @Desc 주문복사 유효성 체크
	 * @param orderCopyValidationDtoList
	 * @return
	 */
	protected ApiResult<?> checkOrderCopyValidation(List<OrderCopyValidationDto> orderCopyValidationDtoList) throws Exception {

		// 1. 주문에 저장,일시품절(시스템),판매중지, 영구판매중지 상품 있을경우 실패처리(HGRM-8889)
		List<Long> odOrderDetlIdList = orderCopyValidationDtoList.stream().map(m -> m.getOdOrderDetlId()).collect(Collectors.toList());
		BasicDataResponseDto resDto = new BasicDataResponseDto();
		String failMessage = "";

		List<GoodsVo> goodsVoList = orderCopyMapper.getOrderDetlGoodsSaleStatus(odOrderDetlIdList);

		for (GoodsVo goodsVo : goodsVoList) {
			if (GoodsEnums.SaleStatus.STOP_SALE.getCode().equals(goodsVo.getSaleStatusCode())
					|| GoodsEnums.SaleStatus.OUT_OF_STOCK_BY_SYSTEM.getCode().equals(goodsVo.getSaleStatusCode())
					|| GoodsEnums.SaleStatus.STOP_PERMANENT_SALE.getCode().equals(goodsVo.getSaleStatusCode())
					|| GoodsEnums.SaleStatus.SAVE.getCode().equals(goodsVo.getSaleStatusCode())) {

				failMessage += "[" + goodsVo.getGoodsId() + "] " + goodsVo.getSaleStatusName() + " 상품으로 주문복사 할 수 없습니다. <br/>";
			}
		}


		// 2. 출고처별 배송 불가 지역 체크(SPMO-746)
		// 우편번호 기준 그룹핑
		Map<String, List<OrderCopyValidationDto>> resultMap = orderCopyValidationDtoList.stream().collect(
				groupingBy(OrderCopyValidationDto::getZipCode, LinkedHashMap::new, toList()));

		for (String zipCode : resultMap.keySet()) {

			List<OrderCopyValidationDto> orderList = resultMap.get(zipCode);

			for (OrderCopyValidationDto dto : orderList) {

				// 매출만 연동일 경우 출고처별 배송불가지역 체크x
				if(StringUtils.isNotEmpty(dto.getOrderIfType()) && OrderIfType.ORDER_IF.getCode().equals(dto.getOrderIfType())){

					// 출고처 주소 기반 배송 가능 정보 조회
					WarehouseUnDeliveryableInfoDto warehouseUnDeliveryableInfoDto = storeWarehouseBiz.getUnDeliverableInfo(dto.getUrWarehouseId(), zipCode, dto.isDawnDelivery());

					boolean isShippingPossibility = warehouseUnDeliveryableInfoDto.isShippingPossibility();
					String shippingImpossibilityMsg = warehouseUnDeliveryableInfoDto.getShippingImpossibilityMsg();

					if (!isShippingPossibility) {
						failMessage += "출고처 배송불가 지역입니다. [" + dto.getIlGoodsId() + "] " + dto.getGoodsName() + " <br/>";
					}

					// 품목, 배송정책 배송 불가 지역 체크
					// 도서산관 및 제주 배송 정보 조회
					ShippingAreaVo shippingAreaVo = goodsShippingTemplateBiz.getShippingArea(zipCode);
					if (shippingAreaVo == null) {
						continue;
					}

					// 상품 개별 정책
					BasicSelectGoodsVo goodsVo = goodsGoodsBiz.getBasicSelectGoods(GoodsRequestDto.builder().ilGoodsId(dto.getIlGoodsId()).build());
					if (goodsVo == null) {
						continue;
					}

					// 품목 도서산간 제주 배송불가 여부
					isShippingPossibility = !goodsShippingTemplateBiz.isUnDeliverableArea(goodsVo.getUndeliverableAreaType(), shippingAreaVo);

					if(!isShippingPossibility){
						failMessage += "품목 배송불가 지역입니다. [" + dto.getIlGoodsId() + "] " + dto.getGoodsName() + " <br/>";
					}

					// 주소기반 배송 정책으로 인한 도서산관,제주 지역 배송 가능여부 체크
					ShippingDataResultVo shippingDataResultVo = goodsShippingTemplateBiz.getShippingUndeliveryInfo(dto.getIlGoodsId(), dto.getUrWarehouseId());
					isShippingPossibility = !goodsShippingTemplateBiz.isUnDeliverableArea(shippingDataResultVo.getUndeliverableAreaType(), shippingAreaVo);

					if(!isShippingPossibility){
						failMessage += "배송정책 배송불가 지역입니다. [" + dto.getIlGoodsId() + "] " + dto.getGoodsName() + " <br/>";
					}

				}
			}

		}

		// 3. 반품완료 상태 체크
		for(OrderCopyValidationDto orderCopyValidationDto : orderCopyValidationDtoList) {
			// 매출만 연동일 경우만
			if(StringUtils.isNotEmpty(orderCopyValidationDto.getOrderIfType()) && OrderIfType.SAL_IF.getCode().equals(orderCopyValidationDto.getOrderIfType())){
				int resultCnt = orderCopyMapper.getOrderDetlReturnCompletedStatusCheck(orderCopyValidationDto.getOdOrderDetlId());
				if(resultCnt == 0) {
					failMessage += "매출만 연동 주문복사가 불가한 주문상품입니다. [" + orderCopyValidationDto.getIlGoodsId() + "] " + orderCopyValidationDto.getGoodsName() + " <br/>";
				}
			}
		}

		if (StringUtils.isNotEmpty(failMessage)) {
			resDto.setResult(OrderEnums.OrderRegistrationResult.FAIL);
			resDto.setFailMessage(failMessage);
		} else {
			resDto.setResult(OrderEnums.OrderRegistrationResult.SUCCESS);
		}

		return ApiResult.success(resDto);
	}

	/**
	 * @Desc 주문상품 출고처 조회
	 * @param odOrderDetlIdList
	 * @return
	 */
	protected ApiResult<?> getOrderDetlGoodsWarehouseCode(List<Long> odOrderDetlIdList){
		return ApiResult.success(orderCopyMapper.getOrderDetlGoodsWarehouseCode(odOrderDetlIdList));
	}
}