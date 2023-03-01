package kr.co.pulmuone.v1.order.registration.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.enums.OrderEnums.PaymentType;
import kr.co.pulmuone.v1.comm.enums.ShoppingEnums.DeliveryType;
import kr.co.pulmuone.v1.comm.enums.OrderEnums.PresentOrderStatus;
import kr.co.pulmuone.v1.comm.enums.SellersEnums;
import kr.co.pulmuone.v1.comm.enums.ShoppingEnums;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.goods.dto.ArrivalScheduledDateDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.OrderSelectGoodsVo;
import kr.co.pulmuone.v1.goods.goods.service.GoodsGoodsBiz;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderAccountVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlDailyVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlDiscountVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDtVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderPaymentVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderPresentVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderShippingPriceVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderShippingZoneVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderVo;
import kr.co.pulmuone.v1.order.registration.dto.OrderBindDto;
import kr.co.pulmuone.v1.order.registration.dto.OrderBindOrderDetlDto;
import kr.co.pulmuone.v1.order.registration.dto.OrderBindOrderDetlPackDto;
import kr.co.pulmuone.v1.order.registration.dto.OrderBindShippingPriceDto;
import kr.co.pulmuone.v1.order.registration.dto.OrderBindShippingZoneDto;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.CouponApplicationListByUserVo;
import kr.co.pulmuone.v1.promotion.coupon.service.PromotionCouponBiz;
import kr.co.pulmuone.v1.shopping.cart.dto.CartAdditionalGoodsDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartBuyerDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartDeliveryDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartGoodsDiscountDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartGoodsDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartGoodsPackageDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartPickGoodsDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartShippingDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartStoreDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartSummaryDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CreateOrderCartDto;
import kr.co.pulmuone.v1.store.delivery.dto.StoreDeliveryScheduleDto;
import kr.co.pulmuone.v1.store.delivery.service.StoreDeliveryBiz;
import kr.co.pulmuone.v1.store.warehouse.service.StoreWarehouseBiz;
import kr.co.pulmuone.v1.user.certification.dto.GetSessionShippingResponseDto;
import kr.co.pulmuone.v1.user.certification.service.UserCertificationBiz;
import kr.co.pulmuone.v1.user.group.service.UserGroupBiz;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("orderBindCartBizImpl")
public class OrderBindCartBizImpl implements OrderBindBiz<CreateOrderCartDto> {

	@Autowired
	private UserGroupBiz userGroupBiz;

	@Autowired
	private PromotionCouponBiz promotionCouponBiz;

	@Autowired
	private GoodsGoodsBiz goodsGoodsBiz;

	@Autowired
	private StoreDeliveryBiz storeDeliveryBiz;

	@Autowired
	private StoreWarehouseBiz storeWarehouseBiz;

	@Autowired
	private UserCertificationBiz userCertificationBiz;

	private String exceptionCouponPriceCode = "EXCEPTION_COUPON";

	@Override
	public List<OrderBindDto> orderDataBind(CreateOrderCartDto createOrderCartDto) throws Exception {

		CartBuyerDto buyerDto = createOrderCartDto.getBuyer();
		GetSessionShippingResponseDto sessionShippingDto = createOrderCartDto.getShippingZone();
		CartSummaryDto summaryDto = createOrderCartDto.getCartSummary();
		CartStoreDto storeDto = createOrderCartDto.getStore();

		List<OrderBindDto> orderBindDtoList = new ArrayList<OrderBindDto>();
		List<OrderBindShippingZoneDto> orderBindShippingZoneDtoList = new ArrayList<OrderBindShippingZoneDto>();
		AtomicInteger detlSeq = new AtomicInteger(0);
		String dailyPackYn = "";

		// 배송 타입
		for (CartDeliveryDto deliveryDto : createOrderCartDto.getCartList()) {

			// buyerDto 정보로 배송지 정보 구성 여부 (무형, 선물하기 일경우)
			boolean isBuyerSetData = DeliveryType.INCORPOREITY.getCode().equals(deliveryDto.getDeliveryType()) || "Y".equals(buyerDto.getPresentYn());

			// 배송지
			OrderShippingZoneVo orderShippingZoneVo = OrderShippingZoneVo.builder()
					.deliveryType(deliveryDto.getDeliveryType()).shippingType(1)
                    .recvNm(isBuyerSetData ? buyerDto.getReceiveName() : sessionShippingDto.getReceiverName())
                    .recvHp(isBuyerSetData ? buyerDto.getReceiveMobile() : sessionShippingDto.getReceiverMobile())
                    .recvZipCd(isBuyerSetData ? "" : sessionShippingDto.getReceiverZipCode())
                    .recvAddr1(isBuyerSetData ? "" : sessionShippingDto.getReceiverAddress1())
                    .recvAddr2(isBuyerSetData ? "" : sessionShippingDto.getReceiverAddress2())
                    .recvBldNo(isBuyerSetData ? "" : sessionShippingDto.getBuildingCode())
                    .deliveryMsg(isBuyerSetData ? "" : sessionShippingDto.getShippingComment())
                    .doorMsgCd(isBuyerSetData ? "" : sessionShippingDto.getAccessInformationType())
                    .doorMsg(isBuyerSetData ? "" : sessionShippingDto.getAccessInformationPassword()).build();

			List<OrderBindShippingPriceDto> orderBindShippingPriceDtoList = new ArrayList<OrderBindShippingPriceDto>();

			// 배송 정책별
			for (CartShippingDto shippingDto : deliveryDto.getShipping()) {

				List<OrderBindOrderDetlDto> orderBindOrderDetlDtoList = new ArrayList<OrderBindOrderDetlDto>();

				// 배송비
				String shippingCouponName = "";
				if (shippingDto.getPmCouponIssueId() != null && shippingDto.getPmCouponIssueId() > 0) {
					shippingCouponName = getCouponName(shippingDto.getPmCouponIssueId());
				}
				String setlYn = storeWarehouseBiz.getWarehouseSetlYn(shippingDto.getUrWarehouseId()).getCode();

				OrderShippingPriceVo orderShippingPriceVo = OrderShippingPriceVo.builder()
						.ilShippingTmplId(shippingDto.getIlShippingTmplId()).paymentMethod(1).method(1)
						.orderShippingPrice(shippingDto.getShippingBaiscPrice())
						.jejuIslandShippingPrice(shippingDto.getShippingRegionPrice())
						.shippingPrice(shippingDto.getShippingPaymentPrice())
						.pmCouponIssueId(StringUtil.nvlLong(shippingDto.getPmCouponIssueId()))
						.pmCouponNm(shippingCouponName).shippingDiscountPrice(shippingDto.getShippingDiscountPrice())
						.orgShippingPrice(shippingDto.getOriginShippingPrice()).setlYn(setlYn).build();

				// 상품
				for (CartGoodsDto goodsDto : shippingDto.getGoods()) {
					List<OrderBindOrderDetlPackDto> orderDetlPackList = new ArrayList<OrderBindOrderDetlPackDto>();
					OrderDetlDailyVo orderDetlDailyVo = null;

					OrderSelectGoodsVo selectGoodsDto = goodsGoodsBiz.getOrderGoodsInfo(goodsDto.getIlGoodsId());
					String goodsDeliveryType = getGoodsDeliveryType(deliveryDto.getDeliveryType(), shippingDto.getDawnDeliveryYn());
					String orderStatusDetailType = getOrderStatusDetailType(goodsDto.getGoodsType(), deliveryDto.getDeliveryType(), shippingDto.getDawnDeliveryYn());

					int cartCouponPrice = getGoodsDiscountPrice(goodsDto.getDiscount(),
							GoodsEnums.GoodsDiscountType.CART_COUPON.getCode());

					int goodsCouponPrice = getGoodsDiscountPrice(goodsDto.getDiscount(),
							GoodsEnums.GoodsDiscountType.GOODS_COUPON.getCode());

					int exceptionCouponPrice = getGoodsDiscountPrice(goodsDto.getDiscount(), exceptionCouponPriceCode);

					ArrivalScheduledDateDto scheduledDateDto = goodsDto.getArrivalScheduledDateDto();

					List<OrderDetlDiscountVo> orderBindOrderDetlDiscountList = convertCartGoodsDiscountDto(goodsDto.getDiscount(), goodsDto.getUrBrandId());

					if(goodsDto.getGoodsPackage()!=null && !goodsDto.getGoodsPackage().isEmpty()) {
						// 묶음 구성 상품

						for (CartGoodsPackageDto goodsPackageDto : goodsDto.getGoodsPackage()) {

							OrderSelectGoodsVo selectGoodsPackageDto = goodsGoodsBiz.getOrderGoodsInfo(goodsPackageDto.getIlGoodsId());

							String packageOrderStatusDetailType = getOrderStatusDetailType(selectGoodsPackageDto.getGoodsType(), deliveryDto.getDeliveryType(), shippingDto.getDawnDeliveryYn());

							// 묶음 상품의 경우 임직원 할인에 들어가는 urBrandId는 구성상품을 따라감
							List<OrderDetlDiscountVo> goodsPackageDiscountList = convertCartGoodsDiscountDto(goodsPackageDto.getDiscount(), goodsPackageDto.getUrBrandId());

							int packageCartCouponPrice = getGoodsDiscountPriceByVo(goodsPackageDiscountList, GoodsEnums.GoodsDiscountType.CART_COUPON.getCode());

							int packageGoodsCouponPrice = getGoodsDiscountPriceByVo(goodsPackageDiscountList, GoodsEnums.GoodsDiscountType.GOODS_COUPON.getCode());

							int packageExceptionCouponPrice = getGoodsDiscountPriceByVo(goodsPackageDiscountList, exceptionCouponPriceCode);

							orderDetlPackList.add(OrderBindOrderDetlPackDto.builder()
									.detlSeq(detlSeq.incrementAndGet())
									.orderDetlVo(OrderDetlVo.builder().ilGoodsShippingTemplateId(goodsDto.getIlShippingTmplId())
											.urWarehouseId(selectGoodsPackageDto.getUrWarehouseId()).urSupplierId(selectGoodsPackageDto.getUrSupplierId())
											.ilGoodsReserveOptnId(StringUtil.nvlLong(goodsDto.getIlGoodsReserveOptionId()))
											.evExhibitId(StringUtil.nvlLong(goodsDto.getEvExhibitId()))
											.promotionTp(goodsDto.getCartPromotionType())
											.orderStatusCd(OrderEnums.OrderStatus.INCOM_READY.getCode())
											.collectionMallDetailId("")  										// 수집몰 상세번호 (이지어드민 SEQ)
											.outmallDetailId("")												// 외부몰 상세번호 (이지어드민 ORDER_ID_SEQ, ORDER_ID_SEQ2)
											.prdSeq(0)															// 이지어드민 상품관리번호
											.urWarehouseGrpCd(selectGoodsPackageDto.getWarehouseGroupCode())
											.storageTypeCd(selectGoodsPackageDto.getStorageMethodType())
											.goodsTpCd(selectGoodsPackageDto.getGoodsType())
											.goodsDailyTp("")
											.orderStatusDeliTp(packageOrderStatusDetailType)
											.goodsDeliveryType(goodsDeliveryType)
											.saleTpCd(selectGoodsPackageDto.getSaleType())
											.ilCtgryStdId(selectGoodsPackageDto.getIlCtgryStdId())
											.ilCtgryDisplayId(StringUtil.nvlLong(selectGoodsPackageDto.getIlCtgryDisplayId()))
											.ilCtgryMallId(StringUtil.nvlLong(selectGoodsPackageDto.getIlCtgryMallId()))
											.itemBarcode(selectGoodsPackageDto.getItemBarcode())
											.ilItemCd(selectGoodsPackageDto.getIlItemCode())
											.ilGoodsId(goodsPackageDto.getIlGoodsId())
											.goodsNm(goodsPackageDto.getGoodsName())
											.taxYn(goodsPackageDto.getTaxYn())
											.orderCnt(goodsPackageDto.getDeliveryQty()).cancelCnt(0)
											.standardPrice(selectGoodsPackageDto.getStandardPrice())
											.recommendedPrice(selectGoodsPackageDto.getRecommendedPrice())
											.salePrice(goodsPackageDto.getSalePrice())
											.totSalePrice(goodsPackageDto.getSaleTotalPrice())
											.cartCouponPrice(packageCartCouponPrice)
											.goodsCouponPrice(packageGoodsCouponPrice)
											.directPrice(packageExceptionCouponPrice)
											.paidPrice(goodsPackageDto.getPaymentPrice())
											.orderIfDt(scheduledDateDto.getOrderDate())
											.shippingDt(scheduledDateDto.getForwardingScheduledDate())
											.deliveryDt(scheduledDateDto.getArrivalScheduledDate())
											.batchExecFl("N")
											.pmAdExternalCd(goodsDto.getPmAdExternalCd())
											.pmAdInternalPageCd(goodsDto.getPmAdInternalPageCd())
											.pmAdInternalContentCd(goodsDto.getPmAdInternalContentCd()).build())
									.orderDetlDiscountList(goodsPackageDiscountList)
									.build());
						}
					}
					dailyPackYn = "N";
					if (goodsDto.getPickGoods() != null && !goodsDto.getPickGoods().isEmpty()) {
						dailyPackYn = "Y";
						goodsDto.setGoodsType(GoodsEnums.GoodsType.PACKAGE.getCode());
						// 골라담기
						for (CartPickGoodsDto goodsPickDto : goodsDto.getPickGoods()) {

							OrderSelectGoodsVo selectGoodsPackageDto = goodsGoodsBiz.getOrderGoodsInfo(goodsPickDto.getIlGoodsId());

							String packageOrderStatusDetailType = getOrderStatusDetailType(selectGoodsPackageDto.getGoodsType(), deliveryDto.getDeliveryType(), shippingDto.getDawnDeliveryYn());

							// 골라담기는 경우 임직원에 들어가는 urBrandId는 각각 구성 상품을 따라감
							List<OrderDetlDiscountVo> pickGoodsDiscountList = convertCartGoodsDiscountDto(goodsPickDto.getDiscount(), goodsPickDto.getUrBrandId());

							int packageCartCouponPrice = getGoodsDiscountPriceByVo(pickGoodsDiscountList, GoodsEnums.GoodsDiscountType.CART_COUPON.getCode());

							int packageGoodsCouponPrice = getGoodsDiscountPriceByVo(pickGoodsDiscountList, GoodsEnums.GoodsDiscountType.GOODS_COUPON.getCode());

							int packageExceptionCouponPrice = getGoodsDiscountPriceByVo(pickGoodsDiscountList, exceptionCouponPriceCode);

							// 일일상품 처리
							orderDetlDailyVo = getOrderDetlDailyVo(goodsDto, scheduledDateDto, goodsPickDto, "N");

							orderDetlPackList.add(OrderBindOrderDetlPackDto.builder()
									.detlSeq(detlSeq.incrementAndGet())
									.orderDetlVo(OrderDetlVo.builder().ilGoodsShippingTemplateId(goodsDto.getIlShippingTmplId())
											.urWarehouseId(selectGoodsPackageDto.getUrWarehouseId()).urSupplierId(selectGoodsPackageDto.getUrSupplierId())
											.ilGoodsReserveOptnId(StringUtil.nvlLong(goodsDto.getIlGoodsReserveOptionId()))
											.evExhibitId(StringUtil.nvlLong(goodsDto.getEvExhibitId()))
											.promotionTp(goodsDto.getCartPromotionType())
											.orderStatusCd(OrderEnums.OrderStatus.INCOM_READY.getCode())
											.collectionMallDetailId("")  										// 수집몰 상세번호 (이지어드민 SEQ)
											.outmallDetailId("")												// 외부몰 상세번호 (이지어드민 ORDER_ID_SEQ, ORDER_ID_SEQ2)
											.prdSeq(0)															// 이지어드민 상품관리번호
											.urWarehouseGrpCd(selectGoodsPackageDto.getWarehouseGroupCode())
											.storageTypeCd(selectGoodsPackageDto.getStorageMethodType())
											.goodsTpCd(selectGoodsPackageDto.getGoodsType())
											.goodsDailyTp(selectGoodsPackageDto.getGoodsDailyType())
											.orderStatusDeliTp(packageOrderStatusDetailType)
											.goodsDeliveryType(goodsDeliveryType)
											.saleTpCd(selectGoodsPackageDto.getSaleType())
											.ilCtgryStdId(selectGoodsPackageDto.getIlCtgryStdId())
											.ilCtgryDisplayId(StringUtil.nvlLong(selectGoodsPackageDto.getIlCtgryDisplayId()))
											.ilCtgryMallId(StringUtil.nvlLong(selectGoodsPackageDto.getIlCtgryMallId()))
											.itemBarcode(selectGoodsPackageDto.getItemBarcode())
											.ilItemCd(selectGoodsPackageDto.getIlItemCode())
											.ilGoodsId(goodsPickDto.getIlGoodsId())
											.goodsNm(goodsPickDto.getGoodsName())
											.taxYn(goodsPickDto.getTaxYn())
											.orderCnt(goodsPickDto.getDeliveryQty()).cancelCnt(0)
											.standardPrice(selectGoodsPackageDto.getStandardPrice())
											.recommendedPrice(selectGoodsPackageDto.getRecommendedPrice())
											.salePrice(goodsPickDto.getSalePrice())
											.totSalePrice(goodsPickDto.getSaleTotalPrice())
											.cartCouponPrice(packageCartCouponPrice)
											.goodsCouponPrice(packageGoodsCouponPrice)
											.directPrice(packageExceptionCouponPrice)
											.paidPrice(goodsPickDto.getPaymentPrice())
											.orderIfDt(scheduledDateDto.getOrderDate())
											.shippingDt(scheduledDateDto.getForwardingScheduledDate())
											.deliveryDt(scheduledDateDto.getArrivalScheduledDate())
											.batchExecFl("N")
											.pmAdExternalCd(goodsDto.getPmAdExternalCd())
											.pmAdInternalPageCd(goodsDto.getPmAdInternalPageCd())
											.pmAdInternalContentCd(goodsDto.getPmAdInternalContentCd()).build())
									.orderDetlDiscountList(pickGoodsDiscountList)
									.orderDetlDailyVo(orderDetlDailyVo)
									.build());
						}
					}
                    // 추가 구성 상품
                    for (CartAdditionalGoodsDto additionalGoodsDto : goodsDto.getAdditionalGoods()) {

                        OrderSelectGoodsVo selectAdditionalGoodsDto = goodsGoodsBiz
                                .getOrderGoodsInfo(additionalGoodsDto.getIlGoodsId());

                        String addGoodsOrderStatusDetailType = getOrderStatusDetailType(GoodsEnums.GoodsType.ADDITIONAL.getCode(), deliveryDto.getDeliveryType(), shippingDto.getDawnDeliveryYn());

                        // 묶음 상품의 경우 임직원 할인에 들어가는 urBrandId는 구성상품을 따라감
						List<OrderDetlDiscountVo> addGoodsDiscountList = convertCartGoodsDiscountDto(additionalGoodsDto.getDiscount(), additionalGoodsDto.getUrBrandId());

						int addGoodsCartCouponPrice = getGoodsDiscountPriceByVo(addGoodsDiscountList, GoodsEnums.GoodsDiscountType.CART_COUPON.getCode());

						int addGoodsGoodsCouponPrice = getGoodsDiscountPriceByVo(addGoodsDiscountList, GoodsEnums.GoodsDiscountType.GOODS_COUPON.getCode());

						int addGoodsExceptionCouponPrice = getGoodsDiscountPriceByVo(addGoodsDiscountList, exceptionCouponPriceCode);

						orderDetlPackList.add(OrderBindOrderDetlPackDto.builder()
                                .detlSeq(detlSeq.incrementAndGet())
                                .orderDetlVo(OrderDetlVo.builder()
                                        .ilGoodsShippingTemplateId(selectAdditionalGoodsDto.getIlShippingTmplId())
                                        .urWarehouseId(selectAdditionalGoodsDto.getUrWarehouseId())
                                        .urSupplierId(selectAdditionalGoodsDto.getUrSupplierId())
                                        .ilGoodsReserveOptnId(StringUtil.nvlLong(goodsDto.getIlGoodsReserveOptionId()))
                                        .evExhibitId(StringUtil.nvlLong(goodsDto.getEvExhibitId()))
                                        .promotionTp(goodsDto.getCartPromotionType())
                                        .orderStatusCd(OrderEnums.OrderStatus.INCOM_READY.getCode())
                                        .collectionMallDetailId("")  										// 수집몰 상세번호 (이지어드민 SEQ)
                                        .outmallDetailId("")												// 외부몰 상세번호 (이지어드민 ORDER_ID_SEQ, ORDER_ID_SEQ2)
                                        .prdSeq(0)															// 이지어드민 상품관리번호
                                        .urWarehouseGrpCd(selectAdditionalGoodsDto.getWarehouseGroupCode())
                                        .storageTypeCd(selectAdditionalGoodsDto.getStorageMethodType())
                                        .goodsDailyTp("")
                                        .orderStatusDeliTp(addGoodsOrderStatusDetailType)
                                        .goodsTpCd(selectAdditionalGoodsDto.getGoodsType())
                                        .goodsDeliveryType(goodsDeliveryType)
                                        .saleTpCd(selectAdditionalGoodsDto.getSaleType())
                                        .ilCtgryStdId(selectAdditionalGoodsDto.getIlCtgryStdId())
                                        .ilCtgryDisplayId(StringUtil.nvlLong(selectAdditionalGoodsDto.getIlCtgryDisplayId()))
                                        .ilCtgryMallId(StringUtil.nvlLong(selectAdditionalGoodsDto.getIlCtgryMallId()))
                                        .itemBarcode(selectAdditionalGoodsDto.getItemBarcode())
                                        .ilItemCd(selectAdditionalGoodsDto.getIlItemCode())
                                        .ilGoodsId(additionalGoodsDto.getIlGoodsId()).goodsNm(additionalGoodsDto.getGoodsName())
                                        .taxYn(additionalGoodsDto.getTaxYn()).orderCnt(additionalGoodsDto.getQty()).cancelCnt(0)
                                        .standardPrice(selectAdditionalGoodsDto.getStandardPrice())
                                        .recommendedPrice(additionalGoodsDto.getRecommendedPrice())
                                        .salePrice(additionalGoodsDto.getSalePrice())
                                        .totSalePrice(additionalGoodsDto.getPaymentPrice())
                                        .cartCouponPrice(addGoodsCartCouponPrice).goodsCouponPrice(addGoodsGoodsCouponPrice)
                                        .directPrice(addGoodsExceptionCouponPrice).paidPrice(additionalGoodsDto.getPaymentPrice())
                                        .orderIfDt(scheduledDateDto.getOrderDate())
                                        .shippingDt(scheduledDateDto.getForwardingScheduledDate())
                                        .deliveryDt(scheduledDateDto.getArrivalScheduledDate())
                                        .batchExecFl("N")
                                        .pmAdExternalCd(goodsDto.getPmAdExternalCd())
										.pmAdInternalPageCd(goodsDto.getPmAdInternalPageCd())
										.pmAdInternalContentCd(goodsDto.getPmAdInternalContentCd()).build())
                                .orderDetlDiscountList(addGoodsDiscountList)
                                .build());
                    }

					// 일일상품 처리
					orderDetlDailyVo = getOrderDetlDailyVo(goodsDto, scheduledDateDto, dailyPackYn, orderDetlPackList);

					// 일반 상품 및 묶음 대표상품
					orderBindOrderDetlDtoList.add(OrderBindOrderDetlDto.builder()
							.detlSeq(detlSeq.incrementAndGet())
							.orderDetlVo(OrderDetlVo.builder().ilGoodsShippingTemplateId(goodsDto.getIlShippingTmplId())
									.urWarehouseId(goodsDto.getUrWareHouseId()).urSupplierId(goodsDto.getUrSupplierId())
									.ilGoodsReserveOptnId(StringUtil.nvlLong(goodsDto.getIlGoodsReserveOptionId()))
									.evExhibitId(StringUtil.nvlLong(goodsDto.getEvExhibitId()))
									.promotionTp(goodsDto.getCartPromotionType())
									.orderStatusCd(OrderEnums.OrderStatus.INCOM_READY.getCode())
									.collectionMallDetailId("")  										// 수집몰 상세번호 (이지어드민 SEQ)
									.outmallDetailId("")												// 외부몰 상세번호 (이지어드민 ORDER_ID_SEQ, ORDER_ID_SEQ2)
									.prdSeq(0)															// 이지어드민 상품관리번호
									.urWarehouseGrpCd(selectGoodsDto.getWarehouseGroupCode())
									.storageTypeCd(selectGoodsDto.getStorageMethodType())
									.goodsTpCd(goodsDto.getGoodsType())
									.goodsDailyTp(goodsDto.getGoodsDailyType())
									.orderStatusDeliTp(orderStatusDetailType)
									.goodsDeliveryType(goodsDeliveryType)
									.saleTpCd(goodsDto.getSaleType())
									.ilCtgryStdId(selectGoodsDto.getIlCtgryStdId())
									.ilCtgryDisplayId(StringUtil.nvlLong(selectGoodsDto.getIlCtgryDisplayId()))
									.ilCtgryMallId(StringUtil.nvlLong(selectGoodsDto.getIlCtgryMallId()))
									.itemBarcode(selectGoodsDto.getItemBarcode())
									.ilItemCd(goodsDto.getIlItemCd())
									.ilGoodsId(goodsDto.getIlGoodsId())
									.goodsNm(goodsDto.getGoodsName())
									.taxYn(goodsDto.getTaxYn())
									.orderCnt(goodsDto.getDeliveryQty()).cancelCnt(0)
									.standardPrice(selectGoodsDto.getStandardPrice())
									.recommendedPrice(goodsDto.getRecommendedPrice())
									.salePrice(goodsDto.getSalePrice()).cartCouponPrice(cartCouponPrice)
									.totSalePrice(goodsDto.getSalePriceMltplQty())
									.goodsCouponPrice(goodsCouponPrice).directPrice(exceptionCouponPrice)
									.paidPrice(goodsDto.getPaymentPrice())
									.orderIfDt(scheduledDateDto.getOrderDate())
									.shippingDt(scheduledDateDto.getForwardingScheduledDate())
									.deliveryDt(scheduledDateDto.getArrivalScheduledDate())
									.batchExecFl("N")
									.pmAdExternalCd(goodsDto.getPmAdExternalCd())
									.pmAdInternalPageCd(goodsDto.getPmAdInternalPageCd())
									.pmAdInternalContentCd(goodsDto.getPmAdInternalContentCd()).build())
							.orderDetlPackList(orderDetlPackList)
							.orderDetlDiscountList(orderBindOrderDetlDiscountList)
							.orderDetlDailyVo(orderDetlDailyVo).build());



					// 일일배송일때 배송지는 상품별로 각각 생성처리 해줘야함
					if(ShoppingEnums.DeliveryType.DAILY.getCode().equals(deliveryDto.getDeliveryType())) {
						// 비송비 정보
						orderBindShippingPriceDtoList.add(OrderBindShippingPriceDto.builder()
								.shippingPriceSeq(detlSeq.incrementAndGet())
								.orderShippingPriceVo(orderShippingPriceVo)
								.orderDetlList(orderBindOrderDetlDtoList).build());

						// 배송지 정보
						OrderBindShippingZoneDto orderBindShippingZoneDto = OrderBindShippingZoneDto.builder()
								.shippingZoneSeq(detlSeq.incrementAndGet())
								.orderShippingZoneVo(orderShippingZoneVo)
								.shippingPriceList(orderBindShippingPriceDtoList).build();

						orderBindShippingZoneDtoList.add(orderBindShippingZoneDto);
						orderBindOrderDetlDtoList = new ArrayList<OrderBindOrderDetlDto>();
						orderBindShippingPriceDtoList = new ArrayList<OrderBindShippingPriceDto>();
					}
				}

				// 일일배송이 아닐때
				if(!ShoppingEnums.DeliveryType.DAILY.getCode().equals(deliveryDto.getDeliveryType())) {
					// 비송비 정보
					orderBindShippingPriceDtoList.add(OrderBindShippingPriceDto.builder()
							.orderShippingPriceVo(orderShippingPriceVo)
							.shippingPriceSeq(detlSeq.incrementAndGet())
							.orderDetlList(orderBindOrderDetlDtoList).build());
				}
			}

			// 일일배송이 아닐때
			if(!ShoppingEnums.DeliveryType.DAILY.getCode().equals(deliveryDto.getDeliveryType())) {
				// 배송지 정보
				OrderBindShippingZoneDto orderBindShippingZoneDto = OrderBindShippingZoneDto.builder()
						.orderShippingZoneVo(orderShippingZoneVo)
						.shippingPriceList(orderBindShippingPriceDtoList).build();

				orderBindShippingZoneDtoList.add(orderBindShippingZoneDto);
			}
		}

		String urGroupNm = "비회원";
		if (buyerDto.getUrUserId() > 0) {
			urGroupNm = userGroupBiz.getGroupByUser(buyerDto.getUrUserId()).getGroupName();
		}

		// TODO: orderHpnCd 나중에 처리 해야함
		int cartCouponPrice = getGoodsDiscountPrice(summaryDto.getDiscount(), GoodsEnums.GoodsDiscountType.CART_COUPON.getCode());

		int goodsCouponPrice = getGoodsDiscountPrice(summaryDto.getDiscount(), GoodsEnums.GoodsDiscountType.GOODS_COUPON.getCode());

		int exceptionCouponPrice = getGoodsDiscountPrice(summaryDto.getDiscount(), exceptionCouponPriceCode);

		OrderAccountVo orderAccountVo = null;

		// 환불 계좌 정보
		if (PaymentType.VIRTUAL_BANK.getCode().equals(buyerDto.getPaymentType().getCode()) && StringUtils.isNotEmpty(buyerDto.getBankCode())
				&& StringUtils.isNotEmpty(buyerDto.getHolderName()) && StringUtils.isNotEmpty(buyerDto.getAccountNumber())) {
			orderAccountVo = OrderAccountVo.builder()
					.bankCd(buyerDto.getBankCode())
					.accountHolder(buyerDto.getHolderName())
					.accountNumber(buyerDto.getAccountNumber())
					.build();
		}

	    String urStoreId = null;
	    Long urStoreScheduleId = null;
	    Long storeScheduleNo = null;
	    LocalDateTime storeStartTime = null;
	    LocalDateTime storeEndTime = null;
		if (storeDto != null) {
			StoreDeliveryScheduleDto storeScheduleDto = storeDeliveryBiz.getStoreScheduleByUrStoreScheduleId(storeDto.getUrStoreScheduleId(), storeDto.getStoreArrivalScheduledDate());
			urStoreId = storeScheduleDto.getUrStoreId();
			urStoreScheduleId = storeDto.getUrStoreScheduleId();
			storeScheduleNo = storeScheduleDto.getScheduleNo();
			storeStartTime = LocalDateTime.of(storeDto.getStoreArrivalScheduledDate(), storeScheduleDto.getStartTimeLocalTime());
			storeEndTime = LocalDateTime.of(storeDto.getStoreArrivalScheduledDate(), storeScheduleDto.getEndTimeLocalTime());
		}
		String orderYn = "N";
//		if(PaymentType.VIRTUAL_BANK.getCode().equals(buyerDto.getPaymentType().getCode())){
//			orderYn = "Y";
//		}
		// 주문자 정보
		OrderBindDto orderBindDto = OrderBindDto.builder()
				.order(OrderVo.builder()
						.omSellersId(0)
						.collectionMallId("")
						.outmallId("")
						.urGroupId(buyerDto.getUrGroupId())
						.urGroupNm(urGroupNm)
						.urUserId(buyerDto.getUrUserId())
						.urEmployeeCd(buyerDto.getUrEmployeeCd())
						.guestCi(buyerDto.getGuestCi())
						.buyerNm(buyerDto.getBuyerName())
						.buyerHp(buyerDto.getBuyerMobile())
						.buyerTel("")
						.buyerMail(buyerDto.getBuyerEmail())
						.ilGoodsId(summaryDto.getSummaryGoodsId())
						.goodsNm(summaryDto.getGoodsSummaryName())
						.sellersGroupCd(SellersEnums.SellersGroupCd.MALL.getCode())
						.orderPaymentType(buyerDto.getPaymentType().getCode())
						.buyerTypeCd(buyerDto.getBuyerType().getCode())
						.agentTypeCd(buyerDto.getAgentType().getCode())
						.orderHpnCd("")
						.giftYn("Y".equals(buyerDto.getPresentYn()) ? "Y" : "N")
						.urPcidCd(buyerDto.getUrPcidCd())
						.orderYn(orderYn)
						.urStoreId(urStoreId)
						.urStoreScheduleId(urStoreScheduleId)
						.storeScheduleNo(storeScheduleNo)
						.storeStartTime(storeStartTime)
						.storeEndTime(storeEndTime)
						.orderCopyYn("N")
						.orderCreateYn(StringUtil.nvl(buyerDto.getOrderCreateYn(), "N"))
						.build()
				)
				.orderDt(OrderDtVo.builder().irId(0).ibId(0).icId(0).build())
				.orderShippingZoneList(orderBindShippingZoneDtoList)
				.orderPaymentVo(OrderPaymentVo.builder().salePrice(summaryDto.getGoodsSalePrice())
						.cartCouponPrice(cartCouponPrice).goodsCouponPrice(goodsCouponPrice)
						.directPrice(exceptionCouponPrice).paidPrice(summaryDto.getGoodsPaymentPrice())
						.shippingPrice(summaryDto.getShippingPaymentPrice())
						.taxablePrice(summaryDto.getTaxPaymentPrice())
						.nonTaxablePrice(summaryDto.getTaxFreePaymentPrice()).paymentPrice(summaryDto.getPaymentPrice())
						.pointPrice(summaryDto.getUsePoint()).build())
				.orderAccount(orderAccountVo)
				.orderPresentVo("Y".equals(buyerDto.getPresentYn()) ?  OrderPresentVo.builder()
						.presentReceiveNm(buyerDto.getReceiveName())
						.presentReceiveHp(buyerDto.getReceiveMobile())
						.presentCardType(buyerDto.getPresentCardType())
						.presentCardMsg(buyerDto.getPresentCardMessage())
						.presentAuthCd(userCertificationBiz.getRandom6())
						.presentExpirationDt(LocalDate.now().plusDays(3).atTime(23, 59, 59))
						.presentOrderStatus(PresentOrderStatus.WAIT.getCode())
						.build()
						: null)
				.build();

		orderBindDtoList.add(orderBindDto);

		return orderBindDtoList;
	}

	/**
	 * 녹즙 골라담기 구성상품 일일상품 일일배송데이터 OrderDetlDailyVo 생성
	 *
	 * @param goodsDto
	 * @param scheduledDateDto
	 * @param goodsPickDto
	 * @param dailyPackYn
	 * @return
	 */
	private OrderDetlDailyVo getOrderDetlDailyVo(CartGoodsDto goodsDto, ArrivalScheduledDateDto scheduledDateDto, CartPickGoodsDto goodsPickDto, String dailyPackYn) {
		// 공통 일일상품 일일배송데이터 OrderDetlDailyVo 생성
		OrderDetlDailyVo orderDetlDailyVo = getOrderDetlDailyVo(goodsDto, scheduledDateDto, dailyPackYn, null);

		// 녹즙 골라담기 구성상품의 경우
		if (orderDetlDailyVo != null && ShoppingEnums.CartPromotionType.GREENJUICE_SELECT.getCode().equals(goodsDto.getCartPromotionType())) {
			orderDetlDailyVo.setIlGoodsId(goodsPickDto.getIlGoodsId());

			// 골라담기는 요일별 상품을 담기 떄문에 주 1일로 고정으로 넘김
			String goodsCycleTp = GoodsEnums.GoodsCycleType.DAY1_PER_WEEK.getCode();
			orderDetlDailyVo.setGoodsCycleTp(goodsCycleTp);

			convertOrderDetlDailyWeekType(orderDetlDailyVo, goodsDto.getGoodsDailyType(), goodsCycleTp, goodsPickDto.getGoodsQty(), goodsPickDto.getGoodsDailyCycleWeekTypeCode());

			return orderDetlDailyVo;
		} else {
			return null;
		}
	}

	/**
	 * 공통 (일반 일일상품, 녹즙골라담기 본상품) 일일배송데이터 OrderDetlDailyVo 생성
	 *
	 * @param goodsDto
	 * @param scheduledDateDto
	 * @param dailyPackYn
	 * @param orderDetlPackList
	 * @return
	 */
	private OrderDetlDailyVo getOrderDetlDailyVo(CartGoodsDto goodsDto, ArrivalScheduledDateDto scheduledDateDto, String dailyPackYn, List<OrderBindOrderDetlPackDto> orderDetlPackList) {
		if (GoodsEnums.SaleType.DAILY.getCode().equals(goodsDto.getSaleType())) {
			int setCnt = 0;
			String allergyYn = "N";
			String dailyBulkYn = "N";
			if (goodsDto.getGoodsBulkType() != null && !goodsDto.getGoodsBulkType().isEmpty()) {
				setCnt = GoodsEnums.GoodsDailyBulkQtyType.findByCode(goodsDto.getGoodsBulkType()).getSetCnt();
			}
			if (goodsDto.getGoodsDailyAllergyYn() != null) {
				allergyYn = goodsDto.getGoodsDailyAllergyYn();
			}
			if (goodsDto.getGoodsDailyBulkYn() != null) {
				dailyBulkYn = goodsDto.getGoodsDailyBulkYn();
			}

			OrderDetlDailyVo orderDetlDailyVo = OrderDetlDailyVo.builder()
				.ilGoodsId(goodsDto.getIlGoodsId())
				.goodsCycleTp(goodsDto.getGoodsDailyCycleType())
				.goodsCycleTermTp(goodsDto.getGoodsDailyCycleTermType())
				.setCnt(setCnt)
				.allergyYn(allergyYn)
				.dailyBulkYn(dailyBulkYn)
				.scheduleYn("N")
				.storeDeliveryType(goodsDto.getStoreDeliveryTypeCode())
				.urStoreId(goodsDto.getUrStoreId())
				.dailyPackYn(dailyPackYn)
				.build();

			if("N".equals(dailyBulkYn)) {
				// 일일상품의 경우
				String goodsDailyCycleGreenJuiceWeekType = "";
				if (goodsDto.getGoodsDailyCycleGreenJuiceWeekType() != null) {
					goodsDailyCycleGreenJuiceWeekType = goodsDto.getGoodsDailyCycleGreenJuiceWeekType()[0];
				}

				if (orderDetlPackList != null && !orderDetlPackList.isEmpty()) {
					// 녹즙골라담기 본상품의 경우
					convertOrderDetlDailyWeekType(orderDetlDailyVo, orderDetlPackList);
				} else {
					// 일반 일일상품의 경우
					convertOrderDetlDailyWeekType(orderDetlDailyVo, goodsDto.getGoodsDailyType(), goodsDto.getGoodsDailyCycleType(), goodsDto.getQty(), goodsDailyCycleGreenJuiceWeekType);
				}
			} else {
				// 일괄 배송일 경우
				orderDetlDailyVo.setOrderCnt(goodsDto.getQty());
			}

			return orderDetlDailyVo;
		} else {
			return null;
		}
	}

	/**
	 * 녹즙 골라담기시 요일별 배송가능수량 set
	 *
	 * @param vo
	 * @param orderDetlPackList
	 */
	private void convertOrderDetlDailyWeekType(OrderDetlDailyVo vo, List<OrderBindOrderDetlPackDto> orderDetlPackList) {
		// 녹즙 골라담기시 List<OrderBindOrderDetlPackDto> 리스트에 `convertOrderDetlDailyWeekType-일반 일일상품 요일별 배송가능수량 set` 의 정보로 구성된 데이터로 집계로 처리
		List<OrderDetlDailyVo> orderDetlDailyVoList = orderDetlPackList.stream().map(OrderBindOrderDetlPackDto::getOrderDetlDailyVo).collect(Collectors.toList());

		int monCnt = orderDetlDailyVoList.stream().collect(Collectors.summingInt(OrderDetlDailyVo::getMonCnt));
		int tueCnt = orderDetlDailyVoList.stream().collect(Collectors.summingInt(OrderDetlDailyVo::getTueCnt));
		int wedCnt = orderDetlDailyVoList.stream().collect(Collectors.summingInt(OrderDetlDailyVo::getWedCnt));
		int thuCnt = orderDetlDailyVoList.stream().collect(Collectors.summingInt(OrderDetlDailyVo::getThuCnt));
		int friCnt = orderDetlDailyVoList.stream().collect(Collectors.summingInt(OrderDetlDailyVo::getFriCnt));
		int orderCnt = orderDetlDailyVoList.stream().collect(Collectors.summingInt(OrderDetlDailyVo::getOrderCnt));

		convertOrderDetlDailySetCnt(vo, monCnt, tueCnt, wedCnt, thuCnt, friCnt, orderCnt);
	}

	/**
	 * 일반 일일상품 요일별 배송가능수량 set
	 *
	 * @param vo
	 * @param goodsDailyType
	 * @param goodsDailyCycleType
	 * @param buyQty
	 * @param goodsDailyCycleGreenJuiceWeekType
	 */
	private void convertOrderDetlDailyWeekType(OrderDetlDailyVo vo, String goodsDailyType, String goodsDailyCycleType, int buyQty, String goodsDailyCycleGreenJuiceWeekType) {
		int monCnt = 0;
		int tueCnt = 0;
		int wedCnt = 0;
		int thuCnt = 0;
		int friCnt = 0;
		int orderCnt = buyQty;
		if(GoodsEnums.GoodsDailyType.BABYMEAL.getCode().equals(goodsDailyType)) {
			//베이비밀
			if (GoodsEnums.GoodsCycleType.DAYS3_PER_WEEK.getCode().equals(goodsDailyCycleType)) {
				monCnt = buyQty;
				wedCnt = buyQty;
				friCnt = buyQty;
			} else if (GoodsEnums.GoodsCycleType.DAYS6_PER_WEEK.getCode().equals(goodsDailyCycleType)) {
				monCnt = buyQty;
				tueCnt = buyQty;
				wedCnt = buyQty;
				thuCnt = buyQty;
				friCnt = buyQty * 2;
			} else if (GoodsEnums.GoodsCycleType.DAYS7_PER_WEEK.getCode().equals(goodsDailyCycleType)) {
				monCnt = buyQty;
				tueCnt = buyQty;
				wedCnt = buyQty;
				thuCnt = buyQty;
				friCnt = buyQty * 3;
			}
		} else if(GoodsEnums.GoodsDailyType.EATSSLIM.getCode().equals(goodsDailyType)) {
			// 잇슬림
			if (GoodsEnums.GoodsCycleType.DAYS3_PER_WEEK.getCode().equals(goodsDailyCycleType)) {
				monCnt = buyQty;
				wedCnt = buyQty;
				friCnt = buyQty;
			} else if (GoodsEnums.GoodsCycleType.DAYS5_PER_WEEK.getCode().equals(goodsDailyCycleType)) {
				monCnt = buyQty;
				tueCnt = buyQty;
				wedCnt = buyQty;
				thuCnt = buyQty;
				friCnt = buyQty;
			}
		} else {
			// 녹즙
			if (GoodsEnums.GoodsCycleType.DAY1_PER_WEEK.getCode().equals(goodsDailyCycleType)) {
				if(GoodsEnums.WeekCodeByGreenJuice.MON.getCode().equals(goodsDailyCycleGreenJuiceWeekType)) {
					monCnt = buyQty;
				} else if(GoodsEnums.WeekCodeByGreenJuice.TUE.getCode().equals(goodsDailyCycleGreenJuiceWeekType)) {
					tueCnt = buyQty;
				} else if(GoodsEnums.WeekCodeByGreenJuice.WED.getCode().equals(goodsDailyCycleGreenJuiceWeekType)) {
					wedCnt = buyQty;
				} else if(GoodsEnums.WeekCodeByGreenJuice.THU.getCode().equals(goodsDailyCycleGreenJuiceWeekType)) {
					thuCnt = buyQty;
				} else if(GoodsEnums.WeekCodeByGreenJuice.FRI.getCode().equals(goodsDailyCycleGreenJuiceWeekType)) {
					friCnt = buyQty;
				} else {
					friCnt = buyQty;
				}
			} else if (GoodsEnums.GoodsCycleType.DAYS3_PER_WEEK.getCode().equals(goodsDailyCycleType)) {
				monCnt = buyQty;
				wedCnt = buyQty;
				friCnt = buyQty;
			} else if (GoodsEnums.GoodsCycleType.DAYS4_PER_WEEK.getCode().equals(goodsDailyCycleType)) {
				monCnt = buyQty;
				tueCnt = buyQty;
				wedCnt = buyQty;
				thuCnt = buyQty;
			} else if (GoodsEnums.GoodsCycleType.DAYS5_PER_WEEK.getCode().equals(goodsDailyCycleType)) {
				monCnt = buyQty;
				tueCnt = buyQty;
				wedCnt = buyQty;
				thuCnt = buyQty;
				friCnt = buyQty;
			} else if (GoodsEnums.GoodsCycleType.DAYS6_PER_WEEK.getCode().equals(goodsDailyCycleType)) {
				monCnt = buyQty;
				tueCnt = buyQty;
				wedCnt = buyQty;
				thuCnt = buyQty;
				friCnt = buyQty * 2;
			}
		}
		convertOrderDetlDailySetCnt(vo, monCnt, tueCnt, wedCnt, thuCnt, friCnt, orderCnt);
	}

	/**
	 * 일일배송데이터 OrderDetlDailyVo에 요일별 set
	 *
	 * @param vo
	 * @param monCnt
	 * @param tueCnt
	 * @param wedCnt
	 * @param thuCnt
	 * @param friCnt
	 * @param orderCnt
	 */
	private void convertOrderDetlDailySetCnt(OrderDetlDailyVo vo, int monCnt, int tueCnt, int wedCnt, int thuCnt, int friCnt, int orderCnt) {
		vo.setMonCnt(monCnt);
		vo.setTueCnt(tueCnt);
		vo.setWedCnt(wedCnt);
		vo.setThuCnt(thuCnt);
		vo.setFriCnt(friCnt);
		vo.setOrderCnt(orderCnt);
	}

	/**
	 * List<CartGoodsDiscountDto> 할인 dto 리스트를 List<OrderDetlDiscountVo> 주문생성 vo 리스트로 변환
	 *
	 * @param goodsDiscountDtoList
	 * @param urBrandId
	 * @return
	 * @throws Exception
	 */
	private List<OrderDetlDiscountVo> convertCartGoodsDiscountDto(List<CartGoodsDiscountDto> goodsDiscountDtoList, Long urBrandId) throws Exception {
		List<OrderDetlDiscountVo> orderBindOrderDetlDiscountList = new ArrayList<>();

		if (goodsDiscountDtoList != null && !goodsDiscountDtoList.isEmpty()) {
			for (CartGoodsDiscountDto goodsDiscountDto : goodsDiscountDtoList) {
				// 임직원 할인시 초과 데이터 때문에 할인 금액이 0인 경우가 있음
				if (goodsDiscountDto.getDiscountPrice() > 0) {
					// 주문상세 할인
					String goodsCouponName = "";
					if (goodsDiscountDto.getPmCouponIssueId() != null && goodsDiscountDto.getPmCouponIssueId() > 0) {
						goodsCouponName = getCouponName(goodsDiscountDto.getPmCouponIssueId());
					}

					OrderDetlDiscountVo orderDetlDiscountVo = OrderDetlDiscountVo.builder()
							.discountTp(goodsDiscountDto.getDiscountType())
							.pmCouponIssueId(StringUtil.nvlLong(goodsDiscountDto.getPmCouponIssueId()))
							.pmCouponNm(goodsCouponName).discountPrice(goodsDiscountDto.getDiscountPrice())
							.psEmplDiscGrpId(StringUtil.nvlLong(goodsDiscountDto.getPsEmplDiscGrpId()))
							.urBrandId(urBrandId).build();
					orderBindOrderDetlDiscountList.add(orderDetlDiscountVo);
				}
			}
		}

		return orderBindOrderDetlDiscountList;
	}

	/**
	 * 쿠폰명 조회
	 *
	 * @param pmCouponIssueId
	 * @return
	 * @throws Exception
	 */
	private String getCouponName(Long pmCouponIssueId) throws Exception {
		CouponApplicationListByUserVo couponInfo = promotionCouponBiz.getCouponApplicationListByPmCouponIssueId(pmCouponIssueId);
		if (couponInfo == null) {
			return "";
		} else {
			return couponInfo.getDisplayCouponName();
		}
	}

	/**
	 * 주문 OD_ORDER_DETL.GOODS_DELIVERY_TYPE 데이터 등록하기 위한 값 조회
	 *
	 * @param deliveryTypeCode
	 * @param dawnDeliveryYn
	 * @return
	 * @throws Exception
	 */
	private String getGoodsDeliveryType(String deliveryTypeCode, String dawnDeliveryYn) throws Exception {
		GoodsEnums.GoodsDeliveryType goodsDeliveryType = GoodsEnums.GoodsDeliveryType.NORMAL;
		ShoppingEnums.DeliveryType deliveryType = ShoppingEnums.DeliveryType.findByCode(deliveryTypeCode);

		if(deliveryType.getCode().equals(ShoppingEnums.DeliveryType.RENTAL.getCode())) {
			goodsDeliveryType = GoodsEnums.GoodsDeliveryType.NORMAL;
		} else if (deliveryType.getCode().equals(ShoppingEnums.DeliveryType.DAILY.getCode())) {
			goodsDeliveryType = GoodsEnums.GoodsDeliveryType.DAILY;
		} else if ("Y".equals(dawnDeliveryYn)) {
			goodsDeliveryType = GoodsEnums.GoodsDeliveryType.DAWN;
		} else if (ShoppingEnums.DeliveryType.SHOP_DELIVERY.getCode().equals(deliveryTypeCode)) {
			goodsDeliveryType = GoodsEnums.GoodsDeliveryType.SHOP;
		} else if (ShoppingEnums.DeliveryType.SHOP_PICKUP.getCode().equals(deliveryTypeCode)) {
			goodsDeliveryType = GoodsEnums.GoodsDeliveryType.SHOP;
		} else if (ShoppingEnums.DeliveryType.RESERVATION.getCode().equals(deliveryTypeCode)) {
			goodsDeliveryType = GoodsEnums.GoodsDeliveryType.RESERVATION;
		}

		return goodsDeliveryType.getCode();
	}

	/**
	 * 할인데이터(dto)로 즉시할인, 쿠폰 할인, 장바구니 쿠폰 할인 구분으로 집계
	 *
	 * @param list
	 * @param discountTypeCode
	 * @return
	 * @throws Exception
	 */
	private int getGoodsDiscountPrice(List<CartGoodsDiscountDto> list, String discountTypeCode) throws Exception {
		if (list == null) {
			return 0;
		} else {
			if (exceptionCouponPriceCode.equals(discountTypeCode)) {
				return list.stream().filter(
						dto -> (!GoodsEnums.GoodsDiscountType.CART_COUPON.getCode().equals(dto.getDiscountType())
								&& !GoodsEnums.GoodsDiscountType.GOODS_COUPON.getCode().equals(dto.getDiscountType())))
						.collect(Collectors.summingInt(CartGoodsDiscountDto::getDiscountPrice));
			} else {
				return list.stream().filter(dto -> discountTypeCode.equals(dto.getDiscountType()))
						.collect(Collectors.summingInt(CartGoodsDiscountDto::getDiscountPrice));
			}
		}
	}

	/**
	 * 할인데이터(vo)로 즉시할인, 쿠폰 할인, 장바구니 쿠폰 할인 구분으로 집계
	 *
	 * @param list
	 * @param discountTypeCode
	 * @return
	 * @throws Exception
	 */
	private int getGoodsDiscountPriceByVo(List<OrderDetlDiscountVo> list, String discountTypeCode) throws Exception {
		if (list == null) {
			return 0;
		} else {
			if (exceptionCouponPriceCode.equals(discountTypeCode)) {
				return list.stream()
						.filter(dto -> (!GoodsEnums.GoodsDiscountType.CART_COUPON.getCode().equals(dto.getDiscountTp())
								&& !GoodsEnums.GoodsDiscountType.GOODS_COUPON.getCode().equals(dto.getDiscountTp())))
						.collect(Collectors.summingInt(OrderDetlDiscountVo::getDiscountPrice));
			} else {
				return list.stream().filter(dto -> discountTypeCode.equals(dto.getDiscountTp()))
						.collect(Collectors.summingInt(OrderDetlDiscountVo::getDiscountPrice));
			}
		}
	}

	/**
	 * 주문 OD_ORDER_DETL.ORDER_STATUS_DELI_TP 데이터 등록하기 위한 값 조회
	 *
	 * @param goodsTypeCode
	 * @param deliveryTypeCode
	 * @param dawnDeliveryYn
	 * @return
	 */
	private String getOrderStatusDetailType(String goodsTypeCode, String deliveryTypeCode, String dawnDeliveryYn) {
		OrderEnums.OrderStatusDetailType type = null;
		if (GoodsEnums.GoodsType.INCORPOREITY.getCode().equals(goodsTypeCode)) {
			type = OrderEnums.OrderStatusDetailType.BOX;
		} else if (GoodsEnums.GoodsType.RENTAL.getCode().equals(goodsTypeCode)) {
			type = OrderEnums.OrderStatusDetailType.RENTAL;
		} else if (ShoppingEnums.DeliveryType.DAILY.getCode().equals(deliveryTypeCode)) {
			type = OrderEnums.OrderStatusDetailType.DAILY;
		} else if ("Y".equals(dawnDeliveryYn)) {
			type = OrderEnums.OrderStatusDetailType.DAWN;
		} else if (ShoppingEnums.DeliveryType.SHOP_DELIVERY.getCode().equals(deliveryTypeCode)) {
			type = OrderEnums.OrderStatusDetailType.SHOP_DELIVERY;
		} else if (ShoppingEnums.DeliveryType.SHOP_PICKUP.getCode().equals(deliveryTypeCode)) {
			type = OrderEnums.OrderStatusDetailType.SHOP_PICKUP;
		} else {
			type = OrderEnums.OrderStatusDetailType.NORMAL;
		}
		return type.getCode();
	}
}
