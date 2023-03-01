package kr.co.pulmuone.v1.order.registration.service;

import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.*;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.goods.dto.ArrivalScheduledDateDto;
import kr.co.pulmuone.v1.goods.goods.dto.PackageGoodsListDto;
import kr.co.pulmuone.v1.goods.goods.dto.RecalculationPackageDto;
import kr.co.pulmuone.v1.goods.goods.service.GoodsGoodsBiz;
import kr.co.pulmuone.v1.order.order.dto.vo.*;
import kr.co.pulmuone.v1.order.registration.dto.*;
import kr.co.pulmuone.v1.order.registration.validate.OrderValidatorUtil;
import kr.co.pulmuone.v1.outmall.ezadmin.dto.EZAdminOrderDto;
import kr.co.pulmuone.v1.outmall.order.dto.OutMallOrderDto;
import kr.co.pulmuone.v1.outmall.order.util.OutmallOrderUtil;
import kr.co.pulmuone.v1.store.warehouse.service.StoreWarehouseBiz;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;


@Slf4j
@Service("orderBindEZAdminCreateBizImpl")
public class OrderBindEZAdminCreateBizImpl implements OrderBindBiz<Map<String, List<EZAdminOrderDto>>> {

	@Autowired
	private GoodsGoodsBiz goodsGoodsBiz;

	@Autowired
	private OrderValidatorUtil orderValidatorUtil;

	@Autowired
	StoreWarehouseBiz storeWarehouseBiz;

	List<Map<String, Object>> collectionMalllFailList = new ArrayList<>();

    @Override
    public List<OrderBindDto> orderDataBind(Map<String, List<EZAdminOrderDto>> shippingPriceMap) throws Exception {
    	collectionMalllFailList.clear();

		List<OrderBindDto> orderBindDtoList = new ArrayList<>();
		AtomicInteger detlSeq = new AtomicInteger(0);

		List<OrderBindShippingZoneDto> orderBindShippingZoneDtoList = new ArrayList<OrderBindShippingZoneDto>();
		//Map<String, List<EZAdminOrderDto>> shippingPriceMap = shippingZoneEntry.getValue();
		List<String> goodsNmList	= new ArrayList<>();
		EZAdminOrderDto shippingZoneDto = shippingPriceMap.entrySet().iterator().next().getValue().get(0);
		//System.out.println("shippingZoneDto  : " + shippingZoneDto);

		OrderVo orderVo = OrderVo.builder()
				.omSellersId(0)
				.collectionMallId("")
				.outmallId("")
				.urGroupId(0L)
				.urGroupNm("")
				.urUserId(0L)
				.urEmployeeCd("")
				.guestCi("")
				/*.buyerNm(buyerNm)
				.buyerHp(buyerHp)
				.buyerTel(buyerTel)
				.buyerMail(buyerMail)
				.goodsNm(goodsNm) */
				.sellersGroupCd(SellersEnums.SellersGroupCd.DIRECT_MNG.getCode())
				.orderPaymentType(OrderEnums.PaymentType.COLLECTION.getCode())
				.buyerTypeCd(UserEnums.BuyerType.GUEST.getCode())
				.agentTypeCd(SystemEnums.AgentType.OUTMALL.getCode())
				.orderHpnCd("")
				.giftYn("N")
				.urPcidCd("")
				.orderYn("Y")
				.createYn("Y")
				.orderCopyYn("N")
				.orderCreateYn("N")
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
				.recvNm(shippingZoneDto.getReceiverName())
				.recvTel(shippingZoneDto.getReceiverTel())
				.recvHp(shippingZoneDto.getReceiverMobile())
				.recvZipCd(shippingZoneDto.getReceiverZipCode())
				.recvAddr1(shippingZoneDto.getReceiverAddress1())
				.recvAddr2(shippingZoneDto.getReceiverAddress2())
				.recvBldNo("")
				.deliveryMsg(shippingZoneDto.getDeliveryMessage())
				.doorMsgCd("")
				.doorMsg("").build();

			List<OrderBindShippingPriceDto> orderBindShippingPriceDtoList = new ArrayList<OrderBindShippingPriceDto>();

			shippingPriceMap.entrySet().forEach(shippingPriceEntry -> {
				List<OrderBindOrderDetlDto> orderBindOrderDetlDtoList = new ArrayList<OrderBindOrderDetlDto>();
				List<EZAdminOrderDto> goodsList		= shippingPriceEntry.getValue();
				EZAdminOrderDto shippingPriceDto	= goodsList.get(0);
				//System.out.println("shippingPriceDto  : " + shippingPriceDto);
				int orderShippingPrice = 0;
				String setlYn = null;
				try {
					setlYn = storeWarehouseBiz.getWarehouseSetlYn(new Long(shippingPriceDto.getUrWarehouseId())).getCode();
				} catch (Exception e) {
					e.printStackTrace();
				}

				for(EZAdminOrderDto eZAdminOrderDto : goodsList){
					orderShippingPrice += Integer.parseInt(eZAdminOrderDto.getShippingPrice());
				}
				//orderShippingPrice = StringUtil.nvlInt(shippingPriceDto.getShippingPrice());

				OrderShippingPriceVo orderShippingPriceVo = OrderShippingPriceVo.builder()
						.ilShippingTmplId(StringUtil.nvlLong(shippingPriceDto.getIlShippingTmplId()))
						.paymentMethod(1).method(1)
						.shippingPrice(orderShippingPrice)
						.pmCouponIssueId(0L)
						.pmCouponNm("")
						.shippingDiscountPrice(0)
						.orderShippingPrice(orderShippingPrice)
						.orgShippingPrice(orderShippingPrice)
						.setlYn(setlYn)
						.build();


				Map<String, List<ArrivalScheduledDateDto>> groupStockMap = new HashMap<>();
				List<ArrivalScheduledDateDto> stockList = new ArrayList<>();
				List<List<ArrivalScheduledDateDto>> groupStockList = new ArrayList<>();
				HashMap<String, Integer> overlapBuyItem = new HashMap<String, Integer>();   // 중복 품목 재고 정보
				String ifEasyadminOrderSuccDetlId = "";
				for (EZAdminOrderDto item : goodsList) {
					// 고객 새벽배송 의사 여부
					boolean isDawnDelivery = false;

					// 상품 날짜별 재고 리스트 조회

					try {
						if (GoodsEnums.GoodsType.PACKAGE.getCode().equals(item.getGoodsTp())){
							List<PackageGoodsListDto> packageGoodsList = goodsGoodsBiz.getPackagGoodsInfoList(StringUtil.nvlLong(item.getIlGoodsId()), false, false, isDawnDelivery, null, StringUtil.nvlInt(item.getOrderCount()));

							if (packageGoodsList.size() > 0) {
								RecalculationPackageDto recalculationPackageGoods = goodsGoodsBiz.getRecalculationPackage(GoodsEnums.SaleStatus.ON_SALE.getCode(), packageGoodsList);
								stockList = recalculationPackageGoods.getArrivalScheduledDateDtoList();

								if(stockList == null) {
									orderVo.setCreateYn("N");
									collectionMalllFailList = OutmallOrderUtil.setCollectionMallFail(collectionMalllFailList, item.getIfEasyadminOrderSuccDetlId(), "01. 묶음 구성 상품 "+ GoodsEnums.SaleStatus.findByCode(recalculationPackageGoods.getSaleStatus()).getCodeName() +" 입니다.");
								}

							} else {
								orderVo.setCreateYn("N");
								collectionMalllFailList = OutmallOrderUtil.setCollectionMallFail(collectionMalllFailList, item.getIfEasyadminOrderSuccDetlId(), "02. 묶음상품에 구성상품이 존재 하지 않음");

							}
						} else {
							stockList = goodsGoodsBiz.getArrivalScheduledDateDtoList(StringUtil.nvlLong(item.getUrWarehouseId()), StringUtil.nvlLong(item.getIlGoodsId()), false, StringUtil.nvlInt(item.getOrderCount()), null);
						}
						if(stockList != null) {

							// 중복 품목 재고 정보
							goodsGoodsBiz.convertOverlapBuyItem(overlapBuyItem, item.getIlItemCd(), StringUtil.nvlInt(item.getOrderCount()), false, stockList);

							groupStockMap.put(item.getIfEasyadminOrderSuccDetlId(), stockList);
							if (stockList.size() > 0) {
								groupStockList.add(stockList);
							}
						} else {
							orderVo.setCreateYn("N");

							collectionMalllFailList = OutmallOrderUtil.setCollectionMallFail(collectionMalllFailList, item.getIfEasyadminOrderSuccDetlId(), "03. 재고 리스트 조회 에러");
							groupStockMap.put(item.getIfEasyadminOrderSuccDetlId(), null);
						}
					} catch (Exception e) {
						orderVo.setCreateYn("N");
						collectionMalllFailList = OutmallOrderUtil.setCollectionMallFail(collectionMalllFailList, item.getIfEasyadminOrderSuccDetlId(), "04. 재고 조회 : " + e.getMessage());

						e.printStackTrace();
					}
					ifEasyadminOrderSuccDetlId = item.getIfEasyadminOrderSuccDetlId();
				}
				boolean stockFlag = true;
				LocalDate firstDt = LocalDate.now();
				if (CollectionUtils.isNotEmpty(stockList) && CollectionUtils.isNotEmpty(groupStockList)) {
					List<LocalDate> allDate = new ArrayList<>();
					try {
						allDate = goodsGoodsBiz.intersectionArrivalScheduledDateListByDto(groupStockList);
					} catch (Exception e) {
						orderVo.setCreateYn("N");
						collectionMalllFailList = OutmallOrderUtil.setCollectionMallFail(collectionMalllFailList, ifEasyadminOrderSuccDetlId, "05. 도착예정일 조회 에러");
						e.printStackTrace();
					}

					if (allDate != null && allDate.size() > 0) {
						firstDt = allDate.get(0);
					}else{
						orderVo.setCreateYn("N");
						collectionMalllFailList = OutmallOrderUtil.setCollectionMallFail(collectionMalllFailList, ifEasyadminOrderSuccDetlId, "06. 도착예정일 교집합 없음");
					}
				} else {
					stockFlag = false;
				}


				for (EZAdminOrderDto item : goodsList) {

					if ("N".equals(orderVo.getCreateYn())){
						boolean addFailFlag = true;
						for (Map<String, Object> failMap : collectionMalllFailList){

							if (StringUtil.nvl(failMap.get("succId"), "").equals(item.getIfEasyadminOrderSuccDetlId())){

								addFailFlag = false;
								break;
							}
						}
						if (addFailFlag == true) {
							collectionMalllFailList = OutmallOrderUtil.setCollectionMallFail(collectionMalllFailList, item.getIfEasyadminOrderSuccDetlId(), "-");
						}
					} else {

						if (stockFlag == true) {
							// 고객 새벽배송 의사 여부
							boolean isDawnDelivery = false;

							orderVo.setOmSellersId(Long.parseLong(shippingZoneDto.getOmSellersId()));
							orderVo.setSellersGroupCd(item.getSellersGroupCd());
							if (StringUtil.isEmpty(orderVo.getCollectionMallId()) || StringUtil.isEmpty(orderVo.getOutmallId())) {
								orderVo.setCollectionMallId(item.getCollectionMallId());
								orderVo.setOutmallId(item.getOutMallId());
							}
							orderVo.setBuyerNm(item.getBuyerName());
							orderVo.setBuyerHp(item.getBuyerMobile());
							orderVo.setBuyerTel(item.getBuyerTel());
							orderVo.setBuyerMail("");


							//try {
							int salePrice = StringUtil.nvlInt(item.getPaidPrice());
							int paidPrice = salePrice;//*StringUtil.nvlInt(item.getOrderCount());

							double newSalePrice = paidPrice / StringUtil.nvlInt(item.getOrderCount());
							if (GoodsEnums.GoodsType.GIFT.getCode().equals(item.getGoodsTp()) || GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(item.getGoodsTp())) {
								paidPrice = 0;
							}

							goodsNmList.add(item.getPromotionNm() + item.getGoodsName());

							ArrivalScheduledDateDto arrivalScheduledDateDto = null;
							try {
								arrivalScheduledDateDto = goodsGoodsBiz.getArrivalScheduledDateDtoByArrivalScheduledDate(groupStockMap.get(item.getIfEasyadminOrderSuccDetlId()), firstDt);
							} catch (Exception e) {
								e.printStackTrace();
							}
							if (Objects.isNull(arrivalScheduledDateDto.getOrderDate())) {
								orderVo.setCreateYn("N");
								orderVo.setIfOutmallExcelSuccId(item.getIfEasyadminOrderSuccDetlId());
								orderVo.setFailMessage("[" + StringUtil.nvlLong(item.getIlGoodsId()) + "] " + item.getGoodsName() + " :  I/F일자 정보 없음");

								collectionMalllFailList = OutmallOrderUtil.setCollectionMallFail(collectionMalllFailList, item.getIfEasyadminOrderSuccDetlId(), "07. [" + StringUtil.nvlLong(item.getIlGoodsId()) + "] " + item.getGoodsName() + " :  I/F일자 정보 없음");
							}

							List<OrderBindOrderDetlPackDto> orderDetlPackList = new ArrayList<>();
							if (GoodsEnums.GoodsType.PACKAGE.getCode().equals(item.getGoodsTp())) { // 묶음
								List<OrderDetlVo> subPackList = null;
								try {
									subPackList = goodsGoodsBiz.getOutmallGoodsPackList(StringUtil.nvlLong(item.getIlGoodsId()));
								} catch (Exception e) {
									e.printStackTrace();
								}
								orderValidatorUtil.makeOrderDetlPacklist(orderDetlPackList, subPackList, arrivalScheduledDateDto, (int) newSalePrice, paidPrice, Integer.parseInt(item.getOrderCount()), isDawnDelivery, item.getCollectionMallDetailId(), item.getOutMallDetailId(), OutmallEnums.OutmallType.EASYADMIN.getCode(), Integer.parseInt(item.getPrdSeq()));
							}



							// 정상가 - 판매가 > 0 일 경우 즉시할인 등록
							List<OrderDetlDiscountVo> orderDetlDiscountList = null;
							int directPrice = 0;
							/*
							int discountPrice = item.getRecommendedPrice() - paidPrice;
							if (discountPrice > 0 && !GoodsEnums.GoodsType.GIFT.getCode().equals(item.getGoodsTp())) {
								orderDetlDiscountList = new ArrayList<>();
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

							 */

							orderPaymentVo.setSalePrice(orderPaymentVo.getSalePrice() + paidPrice);
							orderPaymentVo.setPaidPrice(orderPaymentVo.getPaidPrice() + paidPrice);
							orderPaymentVo.setPaymentPrice(orderPaymentVo.getPaymentPrice() + paidPrice + StringUtil.nvlInt(item.getShippingPrice()));
							orderPaymentVo.setShippingPrice(orderPaymentVo.getShippingPrice() + StringUtil.nvlInt(item.getShippingPrice()));
							orderPaymentVo.setDirectPrice(orderPaymentVo.getDirectPrice() + directPrice);
							if ("N".equals(item.getTaxYn())) {
								orderPaymentVo.setNonTaxablePrice(orderPaymentVo.getNonTaxablePrice() + paidPrice);
							} else {
								orderPaymentVo.setTaxablePrice(orderPaymentVo.getTaxablePrice() + paidPrice);
							}
							// 일반 상품 및 묶음 대표상품
							orderBindOrderDetlDtoList.add(OrderBindOrderDetlDto.builder()
									.detlSeq(detlSeq.incrementAndGet())
									.orderDetlVo(OrderDetlVo.builder()
											.ilGoodsShippingTemplateId(StringUtil.nvlLong(item.getIlShippingTmplId()))
											.urWarehouseId(StringUtil.nvlLong(item.getUrWarehouseId()))
											.urSupplierId(StringUtil.nvlLong(item.getUrSupplierId()))
											.ilGoodsReserveOptnId(0L)
											.evExhibitId(0L)
											.promotionTp("")
											.orderStatusCd(OrderEnums.OrderStatus.INCOM_COMPLETE.getCode())
											.collectionMallDetailId(item.getCollectionMallDetailId())            // 수집몰 상세번호 (이지어드민 PRD_SEQ)
											.outmallDetailId(item.getOutMallDetailId())                            // 외부몰 상세번호 (이지어드민 ORDER_ID_SEQ, ORDER_ID_SEQ2)
											.prdSeq(Integer.parseInt(item.getPrdSeq()))                            // 이지어드민 상품관리번호
											.urWarehouseGrpCd(item.getWarehouseGrpCd())
											.storageTypeCd(item.getStorageMethodTp())
											.goodsTpCd(item.getGoodsTp())
											.goodsDailyTp(item.getGoodsDailyTp())
											.orderStatusDeliTp(OrderEnums.OrderStatusDetailType.NORMAL.getCode())
											.goodsDeliveryType(GoodsEnums.GoodsDeliveryType.NORMAL.getCode())
											.saleTpCd(item.getSaleTp())
											.ilCtgryStdId(StringUtil.nvlLong(item.getIlCtgryStdId()))
											.ilCtgryDisplayId(StringUtil.nvlLong(item.getIlCtgryDisplayId()))
											.ilCtgryMallId(StringUtil.nvlLong(item.getIlCtgryMallId()))
											.itemBarcode(item.getItemBarcode())
											.ilItemCd(item.getIlItemCd())
											.ilGoodsId(StringUtil.nvlLong(item.getIlGoodsId()))
											.goodsNm(item.getPromotionNm() + item.getGoodsName())
											.taxYn(item.getTaxYn())
											.orderCnt(StringUtil.nvlInt(item.getOrderCount()))
											.cancelCnt(0)
											.standardPrice(item.getStandardPrice())
											.recommendedPrice(item.getRecommendedPrice())
											.salePrice(newSalePrice)
											.totSalePrice(paidPrice)
											.cartCouponPrice(0)
											.goodsCouponPrice(0)
											.paidPrice(paidPrice)
											.directPrice(directPrice)
											.orderIfDt(arrivalScheduledDateDto.getOrderDate())
											.shippingDt(arrivalScheduledDateDto.getForwardingScheduledDate())
											.deliveryDt(arrivalScheduledDateDto.getArrivalScheduledDate())
											.batchExecFl("N")
											.outmallType(OutmallEnums.OutmallType.EASYADMIN.getCode())
											.ifOutmallExcelSuccId(item.getIfEasyadminOrderSuccDetlId())
											.failMessage((!"0".equals(orderVo.getIfOutmallExcelSuccId()) ? orderVo.getFailMessage() : ""))
											.build())
									.orderDetlPackList(orderDetlPackList)
									.orderDetlDiscountList(orderDetlDiscountList)
									.orderDetlDailyVo(null)
									.build()
							);
							//} catch (Exception e) {
							//log.error(e.getMessage());
							//}
						} else {
							orderVo.setCreateYn("N");
							collectionMalllFailList = OutmallOrderUtil.setCollectionMallFail(collectionMalllFailList, item.getIfEasyadminOrderSuccDetlId(), "08. 재고리스트 미존재로 인한 도착예정일 에러");
						}
					}
				}
				//if(!orderBindOrderDetlDtoList.isEmpty()) {
					orderBindShippingPriceDtoList.add(OrderBindShippingPriceDto.builder()
							.orderShippingPriceVo(orderShippingPriceVo)
							.orderDetlList(orderBindOrderDetlDtoList).build());
				//}
			});

		OrderBindShippingZoneDto orderBindShippingZoneDto = OrderBindShippingZoneDto.builder()
				.orderShippingZoneVo(orderShippingZoneVo)
				.shippingPriceList(orderBindShippingPriceDtoList).build();

		orderBindShippingZoneDtoList.add(orderBindShippingZoneDto);

		goodsNmList.stream()
				.distinct()
				.collect(Collectors.toList());
		int goodsNmLength = goodsNmList.size();
		if (goodsNmList.size() > 0) {
			orderVo.setGoodsNm(goodsNmList.get(0) + ((goodsNmLength > 1) ? " 외 " + (goodsNmLength - 1) + "건" : ""));
		}

		// 배송비 과세 세팅
		orderPaymentVo.setTaxablePrice(orderPaymentVo.getTaxablePrice() + orderPaymentVo.getShippingPrice());

		OrderBindDto orderBindDto = OrderBindDto.builder()
				.order(orderVo)
				.orderDt(OrderDtVo.builder().irId(0).ibId(0).icId(Constants.BATCH_CREATE_USER_ID).build())
				.orderShippingZoneList(orderBindShippingZoneDtoList)
				.orderPaymentVo(orderPaymentVo)
				.collectionMalllFailList(collectionMalllFailList)
				.build();

		// 주문 상세 정보 존재할 경우만 ADD
		if(!orderBindShippingPriceDtoList.isEmpty()) {
			orderBindDtoList.add(orderBindDto);
		}

		//orderBindDtoList.add(null);

        return orderBindDtoList;
    }


}
