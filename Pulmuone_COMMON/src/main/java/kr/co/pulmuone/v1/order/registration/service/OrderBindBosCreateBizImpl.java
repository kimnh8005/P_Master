package kr.co.pulmuone.v1.order.registration.service;

import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.constants.OrderShippingConstants;
import kr.co.pulmuone.v1.comm.enums.*;
import kr.co.pulmuone.v1.comm.util.BeanUtils;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.goods.dto.ArrivalScheduledDateDto;
import kr.co.pulmuone.v1.goods.goods.dto.PackageGoodsListDto;
import kr.co.pulmuone.v1.goods.goods.dto.RecalculationPackageDto;
import kr.co.pulmuone.v1.goods.goods.dto.ShippingPriceResponseDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ShippingDataResultVo;
import kr.co.pulmuone.v1.goods.goods.service.GoodsGoodsBiz;
import kr.co.pulmuone.v1.goods.goods.service.GoodsShippingTemplateBiz;
import kr.co.pulmuone.v1.order.create.dto.GoodsInfoDto;
import kr.co.pulmuone.v1.order.create.dto.OrderCreateDto;
import kr.co.pulmuone.v1.order.create.dto.OrderCreateRequestDto;
import kr.co.pulmuone.v1.order.create.service.OrderCreateBiz;
import kr.co.pulmuone.v1.order.order.dto.vo.*;
import kr.co.pulmuone.v1.order.registration.dto.*;
import kr.co.pulmuone.v1.order.registration.validate.OrderValidatorUtil;
import kr.co.pulmuone.v1.store.delivery.dto.WarehouseUnDeliveryableInfoDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartBuyerDto;
import kr.co.pulmuone.v1.store.warehouse.service.StoreWarehouseBiz;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;


@Slf4j
@Service("orderBindBosCreateBizImpl")
public class OrderBindBosCreateBizImpl implements OrderBindBiz<OrderCreateRequestDto> {

	@Autowired
	private GoodsGoodsBiz goodsGoodsBiz;

	@Autowired
	OrderRegistrationBiz orderRegistrationBiz;

	@Autowired
	OrderValidatorUtil orderValidatorUtil;

	@Autowired
	StoreWarehouseBiz storeWarehouseBiz;

    @Override
    public List<OrderBindDto> orderDataBind(OrderCreateRequestDto orderCreateRequestDto) throws Exception {

    	GoodsShippingTemplateBiz goodsShippingTemplateBiz = BeanUtils.getBeanByClass(GoodsShippingTemplateBiz.class);
    	OrderCreateBiz orderCreateBiz = BeanUtils.getBeanByClass(OrderCreateBiz.class);

		List<OrderCreateDto> orderCreateList = orderCreateRequestDto.getOrderCreateList();

		Map<String, Map<String, List<OrderCreateDto>>> shippingZoneMap = orderCreateList.stream()
				// 수취인명, 수취인 연락처, 우편번호, 주소1, 주소2로 그룹
				.collect(groupingBy(OrderCreateDto::getKey, LinkedHashMap::new,
						// 출고처, 배송정책으로 그룹
						groupingBy(OrderCreateDto::getGrpShippingId, LinkedHashMap::new, toList())));


		List<OrderBindDto> orderBindDtoList = new ArrayList<>();
		shippingZoneMap.entrySet().forEach(shippingZoneEntry -> {
			AtomicInteger detlSeq = new AtomicInteger(0);

			List<OrderBindShippingZoneDto> orderBindShippingZoneDtoList = new ArrayList<OrderBindShippingZoneDto>();
			Map<String, List<OrderCreateDto>> shippingPriceMap = shippingZoneEntry.getValue();
			List<String> goodsNmList	= new ArrayList<>();
			OrderCreateDto shippingZoneDto = shippingPriceMap.entrySet().iterator().next().getValue().get(0);

			String orderYn = "N";

			OrderVo orderVo = OrderVo.builder()
					.omSellersId(0)
					.collectionMallId("")
					.outmallId("")
					.urGroupId(0L)
					.urGroupNm("")
					.urUserId(0L)
					.urEmployeeCd("")
					.guestCi("")
					.buyerNm(orderCreateRequestDto.getBuyerNm())
					.buyerHp(orderCreateRequestDto.getBuyerHp())
					.buyerTel("")
					.buyerMail(orderCreateRequestDto.getBuyerMail())
					.goodsNm("")
					.sellersGroupCd(SellersEnums.SellersGroupCd.MALL.getCode())
					.orderPaymentType(orderCreateRequestDto.getPsPayCd())
					.buyerTypeCd(UserEnums.BuyerType.GUEST.getCode())
					.agentTypeCd(SystemEnums.AgentType.ADMIN.getCode())
					.orderHpnCd("")
					.giftYn("N")
					.urPcidCd("")
					.orderYn(orderYn)
					.createYn("Y")
					.orderCopyYn("N")
					.orderCreateYn("Y")
					.build();
			OrderPaymentVo orderPaymentVo = OrderPaymentVo.builder()
					.salePrice(0)
					.cartCouponPrice(0)
					.goodsCouponPrice(0)
					.directPrice(0)
					.paidPrice(0)
					.shippingPrice(0)
					.taxablePrice(0)
					.nonTaxablePrice(0)
					.paymentPrice(0)
					.pointPrice(0)
					.build();

			OrderShippingZoneVo orderShippingZoneVo = OrderShippingZoneVo.builder()
					.deliveryType(ShoppingEnums.DeliveryType.NORMAL.getCode())
					.shippingType(1)
					.recvNm(shippingZoneDto.getRecvNm())
					.recvTel("")
					.recvHp(shippingZoneDto.getRecvHp())
					.recvZipCd(shippingZoneDto.getRecvZipCd())
					.recvAddr1(shippingZoneDto.getRecvAddr1())
					.recvAddr2(shippingZoneDto.getRecvAddr2())
					.recvBldNo("")
					.deliveryMsg("")
					.doorMsgCd("")
					.doorMsg("").build();

				List<OrderOutmallMemoDto> memoList = new ArrayList<>();
				List<OrderBindShippingPriceDto> orderBindShippingPriceDtoList = new ArrayList<OrderBindShippingPriceDto>();
				shippingPriceMap.entrySet().forEach(shippingPriceEntry -> {
					List<OrderBindOrderDetlDto> orderBindOrderDetlDtoList = new ArrayList<OrderBindOrderDetlDto>();
					List<OrderCreateDto> goodsList		= shippingPriceEntry.getValue();
					OrderCreateDto shippingPriceDto		= goodsList.get(0);

					GoodsInfoDto goodsDto = null;
					int shippingPrice = 0;

					String setlYn = null;

					try {
						setlYn = storeWarehouseBiz.getWarehouseSetlYn(new Long(shippingPriceDto.getUrWarehouseId())).getCode();
					} catch (Exception e) {
						e.printStackTrace();
					}

					try {
						goodsDto = orderCreateBiz.getGoodsInfo(Long.parseLong(shippingPriceDto.getGoodsId()));
					} catch (Exception e) {
						e.printStackTrace();
					}

					ShippingDataResultVo subShippingDataResultVo = null;
					try {
						//주문생성/주문복사시 배송비 무료 조건인 경우 배송비 0 처리
						if("Y".equals(orderCreateRequestDto.getFreeShippingPriceYn())) {
							goodsDto.setIlGoodsShippingTemplateId(OrderShippingConstants.FREE_SHIPPING_TEMPLATE_ID);  //주문생성_무료배송비 배송정책탬플릿 세팅
						}
						subShippingDataResultVo = goodsShippingTemplateBiz.getShippingInfoByShippingTmplId(goodsDto.getIlGoodsShippingTemplateId());
					} catch (Exception e) {
						e.printStackTrace();
					}

					ShippingPriceResponseDto subShippingPriceDto = null;
					if (subShippingDataResultVo != null) {
						try {
							subShippingPriceDto = goodsShippingTemplateBiz.getShippingPrice(subShippingDataResultVo, StringUtil.nvlInt(shippingPriceDto.getOrderAmt()), Integer.parseInt(shippingPriceDto.getOrderCnt()), shippingPriceDto.getRecvZipCd());
						} catch (Exception e) {
							e.printStackTrace();
						}
						shippingPrice = subShippingPriceDto.getShippingPrice();
					}

					OrderShippingPriceVo orderShippingPriceVo = OrderShippingPriceVo.builder()
							.ilShippingTmplId(StringUtil.nvlLong(shippingPriceDto.getIlShippingTmplId()))
							.paymentMethod(1).method(1)
							.shippingPrice(shippingPrice)
							.pmCouponIssueId(0L)
							.pmCouponNm("")
							.shippingDiscountPrice(0)
							.orderShippingPrice(shippingPrice)
							.orgShippingPrice(shippingPrice)
							.setlYn(setlYn)
							.build();


					Map<String, List<ArrivalScheduledDateDto>> groupStockMap = new HashMap<>();
					List<ArrivalScheduledDateDto> stockList = new ArrayList<>();
					List<List<ArrivalScheduledDateDto>> groupStockList = new ArrayList<>();
					String ilGoodsId	= "0";
					String failMessage	= "";
					for (OrderCreateDto item : goodsList) {
						// 고객 새벽배송 의사 여부
						boolean isDawnDelivery = false;

						// 상품 날짜별 재고 리스트 조회
						try {
							if (GoodsEnums.GoodsType.PACKAGE.getCode().equals(item.getGoodsTp())){
								List<PackageGoodsListDto> packageGoodsList = goodsGoodsBiz.getPackagGoodsInfoList(StringUtil.nvlLong(item.getIlGoodsId()), false, false, isDawnDelivery, null, StringUtil.nvlInt(item.getOrderCnt()));

								if (packageGoodsList.size() > 0) {
									RecalculationPackageDto recalculationPackageGoods = goodsGoodsBiz.getRecalculationPackage(GoodsEnums.SaleStatus.ON_SALE.getCode(), packageGoodsList);
									stockList = recalculationPackageGoods.getArrivalScheduledDateDtoList();

									if(stockList == null) {
										orderVo.setCreateYn("N");
										orderVo.setFailMessage("["+item.getIlGoodsId()+"] " + item.getGoodsName() + " :  묶음 구성 상품 "+ GoodsEnums.SaleStatus.findByCode(recalculationPackageGoods.getSaleStatus()).getCodeName() +" 입니다.");
									}
								} else {
									orderVo.setCreateYn("N");
									orderVo.setFailMessage("["+item.getIlGoodsId()+"] " + item.getGoodsName() + " :  묶음상품에 구성상품이 존재 하지 않음");
								}
							}else{
								stockList = goodsGoodsBiz.getArrivalScheduledDateDtoList(StringUtil.nvlLong(item.getUrWarehouseId()), StringUtil.nvlLong(item.getIlGoodsId()), false, StringUtil.nvlInt(item.getOrderCnt()), null);
							}

							groupStockList.add(stockList);


							if (StringUtil.isEmpty(stockList)) {
								orderVo.setCreateYn("N");
								orderVo.setFailMessage("["+item.getIlGoodsId()+"] " + item.getGoodsName() + " :  재고 리스트 조회 에러");
								//throw new Exception();
							}

						} catch (Exception e) {
							orderVo.setCreateYn("N");
							orderVo.setFailMessage("["+item.getIlGoodsId()+"] " + item.getGoodsName() + " :  재고 리스트 조회 에러");
							e.printStackTrace();
						}
					}

					List<LocalDate> allDate  = new ArrayList<>();
					try {
						allDate = goodsGoodsBiz.intersectionArrivalScheduledDateListByDto(groupStockList);
					} catch (Exception e) {
						orderVo.setCreateYn("N");
						orderVo.setFailMessage("도착예정일 조회 에러");
						e.printStackTrace();
					}
					LocalDate firstDt = LocalDate.now();
					if (allDate != null && allDate.size() > 0) {
						firstDt = allDate.get(0);
					}else{
						orderVo.setCreateYn("N");
						orderVo.setFailMessage("도착예정일 조회 에러");
					}

					orderPaymentVo.setShippingPrice(orderPaymentVo.getShippingPrice() + StringUtil.nvlInt(shippingPrice));
					orderPaymentVo.setPaymentPrice(orderPaymentVo.getPaymentPrice() + StringUtil.nvlInt(shippingPrice));
					for (OrderCreateDto item : goodsList) {

						GoodsInfoDto itemGoodsDto = null;
						try{
							itemGoodsDto = orderCreateBiz.getGoodsInfo(item.getIlGoodsId());
						}catch (Exception e){
							itemGoodsDto = goodsDto;
							e.printStackTrace();
						}

						orderVo.setBuyerNm(orderCreateRequestDto.getBuyerNm());
						orderVo.setBuyerHp(orderCreateRequestDto.getBuyerHp());
						orderVo.setBuyerTel("");
						orderVo.setBuyerMail(orderCreateRequestDto.getBuyerMail());
						orderVo.setOmSellersId(0);
						orderVo.setSellersGroupCd(SellersEnums.SellersGroupCd.MALL.getCode());
						orderVo.setCollectionMallId("");
						orderVo.setOutmallId("");

						goodsNmList.add(item.getGoodsName());


						ArrivalScheduledDateDto arrivalScheduledDateDto = null;
						try {
							arrivalScheduledDateDto = goodsGoodsBiz.getArrivalScheduledDateDtoByArrivalScheduledDate(stockList, firstDt);
						} catch (Exception e) {
							e.printStackTrace();
						}

						if(arrivalScheduledDateDto == null) {
							orderVo.setCreateYn("N");
							orderVo.setFailMessage("["+StringUtil.nvlLong(item.getIlGoodsId())+"] " + item.getGoodsName() + " :  I/F일자 정보 없음");
						}

						String orderStatusCd = OrderEnums.OrderStatus.INCOM_READY.getCode();

						List<OrderBindOrderDetlPackDto> orderDetlPackList = new ArrayList<>();

						LocalDate orderIfDt		= LocalDate.now();
						LocalDate shippingDt	= LocalDate.now();
						LocalDate deliveryDt	= LocalDate.now();

						if (arrivalScheduledDateDto != null){
							orderIfDt	= arrivalScheduledDateDto.getOrderDate();
							shippingDt	= arrivalScheduledDateDto.getForwardingScheduledDate();
							deliveryDt	= arrivalScheduledDateDto.getArrivalScheduledDate();

							// 묶음상품일 경우
							if (GoodsEnums.GoodsType.PACKAGE.getCode().equals(itemGoodsDto.getGoodsTp())){
								List<OrderDetlVo> subPackList = null;
								try {
									subPackList = goodsGoodsBiz.getOutmallGoodsPackList(StringUtil.nvlLong(item.getIlGoodsId()));
									String finalOrderStatusCd = orderStatusCd;
									subPackList.forEach(f->f.setOrderStatusCd(finalOrderStatusCd));
								} catch (Exception e) {
									orderVo.setCreateYn("N");
									e.printStackTrace();
								}

								orderValidatorUtil.makeOrderDetlPacklist(orderDetlPackList, subPackList, arrivalScheduledDateDto, StringUtil.nvlInt(item.getOrderAmt()), StringUtil.nvlInt(item.getOrderAmt()), Integer.parseInt(item.getOrderCnt()), false, null, null, "");
							}
						} else {
							orderVo.setCreateYn("N");
							orderVo.setFailMessage("["+StringUtil.nvlLong(item.getIlGoodsId())+"] " + item.getGoodsName() + " :  I/F일자 정보 없음");
						}


						// 출고처별 배송 불가 지역 체크
						try{

							// 출고처 주소 기반 배송 가능 정보 조회
							WarehouseUnDeliveryableInfoDto warehouseUnDeliveryableInfoDto = storeWarehouseBiz.getUnDeliverableInfo(Long.parseLong(item.getUrWarehouseId()), item.getRecvZipCd(), false);

							boolean isShippingPossibility = warehouseUnDeliveryableInfoDto.isShippingPossibility();
							String shippingImpossibilityMsg = warehouseUnDeliveryableInfoDto.getShippingImpossibilityMsg();

							if(!isShippingPossibility){
								orderVo.setCreateYn("N");
								failMessage += "["+item.getIlGoodsId()+"] " + item.getGoodsName() + " : 출고처 배송불가 지역입니다." + Constants.ARRAY_SEPARATORS;
								orderVo.setFailMessage(failMessage);
							}
						}catch(Exception e){
							e.printStackTrace();
							orderVo.setCreateYn("N");
							failMessage += "["+item.getIlGoodsId()+"] " + item.getGoodsName() + " : 출고처 배송불가 지역입니다." + Constants.ARRAY_SEPARATORS;
							orderVo.setFailMessage(failMessage);
						}


						// 정상가 - 판매가 > 0 일 경우 즉시할인 등록
						List<OrderDetlDiscountVo> orderDetlDiscountList = new ArrayList<>();
						int directPrice = 0;
//							int discountPrice = item.getRecommendedPrice() - StringUtil.nvlInt(item.getSalePrice());
						int discountPrice = item.getOrgSalePrice() - StringUtil.nvlInt(item.getSalePrice());
						if (discountPrice > 0
								&& !GoodsEnums.GoodsType.GIFT.getCode().equals(itemGoodsDto.getGoodsTp()) && !GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(itemGoodsDto.getGoodsTp())) {
							OrderDetlDiscountVo orderDetlDiscountVo = OrderDetlDiscountVo.builder()
									.discountTp(GoodsEnums.GoodsDiscountType.IMMEDIATE.getCode())
									.pmCouponIssueId(0)
									.pmCouponNm("")
									.discountPrice(discountPrice)
									.psEmplDiscGrpId(0)
									.urBrandId(0).build();
							orderDetlDiscountList.add(orderDetlDiscountVo);
							directPrice = discountPrice;
						}

						orderPaymentVo.setSalePrice(orderPaymentVo.getSalePrice() + StringUtil.nvlInt(item.getOrderAmt()));
						orderPaymentVo.setPaidPrice(orderPaymentVo.getPaidPrice() + StringUtil.nvlInt(item.getOrderAmt()));
						orderPaymentVo.setPaymentPrice(orderPaymentVo.getPaymentPrice() + StringUtil.nvlInt(item.getOrderAmt()));
						orderPaymentVo.setDirectPrice(orderPaymentVo.getDirectPrice() + directPrice);
						if ("N".equals(itemGoodsDto.getTaxYn())) {
							orderPaymentVo.setNonTaxablePrice(orderPaymentVo.getNonTaxablePrice() + StringUtil.nvlInt(item.getOrderAmt()));
						} else {
							orderPaymentVo.setTaxablePrice(orderPaymentVo.getTaxablePrice() + StringUtil.nvlInt(item.getOrderAmt()));
						}

						// 일반 상품 및 묶음 대표상품
						orderBindOrderDetlDtoList.add(OrderBindOrderDetlDto.builder()
								.detlSeq(detlSeq.incrementAndGet())
								.orderDetlVo(OrderDetlVo.builder()
										.ilGoodsShippingTemplateId(StringUtil.nvlLong(item.getIlShippingTmplId()))
										.urWarehouseId(StringUtil.nvlLong(item.getUrWarehouseId()))
										.urSupplierId(StringUtil.nvlLong(itemGoodsDto.getUrSupplierId()))
										.ilGoodsReserveOptnId(0L)
										.evExhibitId(0L)
										.promotionTp("")
										.orderStatusCd(orderStatusCd)
										.collectionMallDetailId("")  										// 수집몰 상세번호 (이지어드민 SEQ)
										.outmallDetailId("")												// 외부몰 상세번호 (이지어드민 ORDER_ID_SEQ, ORDER_ID_SEQ2)
										.prdSeq(0)															// 이지어드민 상품관리번호
										.urWarehouseGrpCd(itemGoodsDto.getUrWarehouseGroupCd())
										.storageTypeCd(item.getStorageMethodTypeCode())
										.goodsTpCd(itemGoodsDto.getGoodsTp())
										.orderStatusDeliTp(OrderEnums.OrderStatusDetailType.NORMAL.getCode())
										.goodsDeliveryType(GoodsEnums.GoodsDeliveryType.NORMAL.getCode())
										.saleTpCd(itemGoodsDto.getSaleTp())
										.ilCtgryStdId(StringUtil.nvlLong(itemGoodsDto.getIlCtgryStdId()))
										.ilCtgryDisplayId(StringUtil.nvlLong(itemGoodsDto.getIlCtgryDisplayId()))
										.ilCtgryMallId(StringUtil.nvlLong(itemGoodsDto.getIlCtgryMallId()))
										.itemBarcode(item.getItemBarcode())
										.ilItemCd(item.getItemCode())
										.ilGoodsId(StringUtil.nvlLong(item.getIlGoodsId()))
										.goodsNm(item.getGoodsName())
										.taxYn(itemGoodsDto.getTaxYn())
										.orderCnt(StringUtil.nvlInt(item.getOrderCnt()))
										.cancelCnt(0)
										.standardPrice(itemGoodsDto.getStandardPrice())
										.recommendedPrice(item.getRecommendedPrice())
										.salePrice(Integer.parseInt(item.getSalePrice()))
										.totSalePrice(StringUtil.nvlInt(item.getOrderAmt()))
										.cartCouponPrice(0)
										.goodsCouponPrice(0)
										.paidPrice(StringUtil.nvlInt(item.getOrderAmt()))
										.directPrice(directPrice)
										.orderIfDt(orderIfDt)
										.shippingDt(shippingDt)
										.deliveryDt(deliveryDt)
										.batchExecFl("N")
										.outmallType("")
										.ifOutmallExcelSuccId((!"0".equals(orderVo.getIfOutmallExcelSuccId())? orderVo.getIfOutmallExcelSuccId(): ""))
										.failMessage((!"0".equals(orderVo.getIfOutmallExcelSuccId())? orderVo.getFailMessage(): ""))
										.build())
								.orderDetlPackList(orderDetlPackList)
								.orderDetlDiscountList(orderDetlDiscountList)
								.orderDetlDailyVo(null)
								.build()
						);
					}

					if(!orderBindOrderDetlDtoList.isEmpty()) {
						orderBindShippingPriceDtoList.add(OrderBindShippingPriceDto.builder()
								.orderShippingPriceVo(orderShippingPriceVo)
								.orderDetlList(orderBindOrderDetlDtoList).build());
					}
				});

			OrderBindShippingZoneDto orderBindShippingZoneDto = OrderBindShippingZoneDto.builder()
					.orderShippingZoneVo(orderShippingZoneVo)
					.shippingPriceList(orderBindShippingPriceDtoList).build();

			orderBindShippingZoneDtoList.add(orderBindShippingZoneDto);

			goodsNmList.stream()
					.distinct()
					.collect(Collectors.toList());
			int goodsNmLength = goodsNmList.size();

			orderVo.setGoodsNm(goodsNmList.get(0) + ((goodsNmLength > 1) ? " 외 " + (goodsNmLength-1) + "건" : ""));

			orderPaymentVo.setTaxablePrice(orderPaymentVo.getTaxablePrice() + orderPaymentVo.getShippingPrice());

			OrderAccountVo orderAccountVo = null;
			// 환불 계좌 정보
			if (OrderEnums.PaymentType.VIRTUAL_BANK.getCode().equals(orderCreateRequestDto.getPsPayCd()) && StringUtils.isNotEmpty(orderCreateRequestDto.getBankCode())
					&& StringUtils.isNotEmpty(orderCreateRequestDto.getHolderName()) && StringUtils.isNotEmpty(orderCreateRequestDto.getAccountNumber())) {
				orderAccountVo = OrderAccountVo.builder()
						.bankCd(orderCreateRequestDto.getBankCode())
						.accountHolder(orderCreateRequestDto.getHolderName())
						.accountNumber(orderCreateRequestDto.getAccountNumber())
						.build();
			}

			OrderBindDto orderBindDto = OrderBindDto.builder()
					.order(orderVo)
					.orderDt(OrderDtVo.builder().irId(0).ibId(0).icId(Constants.BATCH_CREATE_USER_ID).build())
					.orderShippingZoneList(orderBindShippingZoneDtoList)
					.orderPaymentVo(orderPaymentVo)
					.orderAccount(orderAccountVo)
					.build();

			// 주문 상세 정보 존재할 경우만 ADD
			if(!orderBindShippingPriceDtoList.isEmpty()) {
				orderBindDtoList.add(orderBindDto);
			}

		});
		//orderBindDtoList.add(null);

        return orderBindDtoList;
    }
}
