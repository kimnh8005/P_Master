package kr.co.pulmuone.v1.goods.goods.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import kr.co.pulmuone.v1.goods.goods.dto.*;
import kr.co.pulmuone.v1.policy.config.service.PolicyConfigBiz;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.constants.BuyerConstants;
import kr.co.pulmuone.v1.comm.constants.GoodsConstants;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.GoodsCycleType;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.GoodsCycleTypeByGreenJuice;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.GoodsDiscountType;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.GoodsType;
import kr.co.pulmuone.v1.comm.util.DeviceUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.goods.dto.vo.BasicSelectGoodsVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.DetailSelectGoodsVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsCertificationListResultVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsNutritionListResultVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsReserveOptionVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsSpecListResultVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ItemStoreInfoVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.OrderSelectGoodsVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.PackageGoodsResultVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlVo;
import kr.co.pulmuone.v1.order.order.service.OrderOrderBiz;
import kr.co.pulmuone.v1.policy.benefit.dto.vo.PolicyBenefitEmployeeBrandGroupByUserVo;
import kr.co.pulmuone.v1.policy.benefit.dto.vo.PolicyBenefitEmployeeByUserVo;
import kr.co.pulmuone.v1.policy.benefit.service.PolicyBenefitEmployeeBiz;
import kr.co.pulmuone.v1.store.delivery.dto.ShippingPossibilityStoreDeliveryAreaDto;
import kr.co.pulmuone.v1.store.delivery.service.StoreDeliveryBiz;
import kr.co.pulmuone.v1.store.warehouse.service.StoreWarehouseBiz;
import kr.co.pulmuone.v1.store.warehouse.service.dto.vo.UrWarehouseVo;
import kr.co.pulmuone.v1.user.store.service.StoreBiz;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GoodsGoodsBizImpl implements GoodsGoodsBiz {

	@Autowired
	private GoodsGoodsService goodsGoodsService;

	@Autowired
	private StoreWarehouseBiz storeWarehouseBiz;

	@Autowired
	private OrderOrderBiz orderOrderBiz;

	@Autowired
	private StoreDeliveryBiz storeDeliveryBiz;

	@Autowired
	private PolicyBenefitEmployeeBiz policyBenefitEmployeeBiz;

	@Autowired
	private GoodsStoreStockBiz goodsStoreStockBiz;

	@Autowired
	private PolicyConfigBiz policyConfigBiz;

	@Autowired
	private StoreBiz storeBiz;

	@Override
	public BasicSelectGoodsVo getBasicSelectGoods(GoodsRequestDto goodsRequestDto) throws Exception {
		return goodsGoodsService.getBasicSelectGoods(goodsRequestDto);
	}

	/**
	 * 상품 기본 정보
	 *
	 * @param goodsRequestDto
	 * @return BasicSelectGoodsVo
	 * @throws Exception
	 */
	@Override
	public BasicSelectGoodsVo getGoodsBasicInfo(GoodsRequestDto goodsRequestDto) throws Exception {

		// 상품 기본 정보
		BasicSelectGoodsVo basicSelectGoodsVo = getBasicSelectGoods(goodsRequestDto);

		if (goodsRequestDto.isStoreDelivery()
				&& (GoodsEnums.SaleStatus.OUT_OF_STOCK_BY_SYSTEM.getCode().equals(basicSelectGoodsVo.getSaleStatus())
						|| GoodsEnums.SaleStatus.OUT_OF_STOCK_BY_MANAGER.getCode().equals(basicSelectGoodsVo.getSaleStatus()))) {
			// 매장 배송은 상품은 판매 상태가 일시품절은 상태 안보고 재고만 바라보도록 처리
			basicSelectGoodsVo.setSaleStatus(GoodsEnums.SaleStatus.ON_SALE.getCode());
			basicSelectGoodsVo.setSystemSaleStatus(GoodsEnums.SaleStatus.ON_SALE.getCode());
		}

		if (basicSelectGoodsVo != null
				// 묶음 상품은 구성상품의 재고 구성상픔으로 본상품의 재고가 결정이 되기때문에 재고 로직 불필요
				&& !GoodsEnums.GoodsType.PACKAGE.getCode().equals(basicSelectGoodsVo.getGoodsType())
				// 예약상품이면서 매장배송 가능한 상품의 경우 매장배송 상품으로 접근시 재고 조회 로직 타야함
				&& !(GoodsEnums.SaleType.RESERVATION.getCode().equals(basicSelectGoodsVo.getSaleType()) && !goodsRequestDto.isStoreDelivery())) {

			int stock = 0;
			// 판매 가능한 상태의 상품만 재고로직 을 탐
			if (GoodsEnums.SaleStatus.ON_SALE.getCode().equals(basicSelectGoodsVo.getSaleStatus())) {
				LocalDate scheduledDate = goodsRequestDto.getArrivalScheduledDate();

				// 매장 배송 상품일때
				if (goodsRequestDto.isStoreDelivery()) {
					// 배장 배송일때
					if (goodsRequestDto.getStoreDeliveryInfo() != null) {

						if (goodsRequestDto.isRealTimeStoreStock() && StringUtil.isNotEmpty(goodsRequestDto.getStoreDeliveryInfo().getUrStoreId()) && StringUtil.isNotEmpty(basicSelectGoodsVo.getIlItemCode())) {
							// 매장 재고 실시간 업데이트
							goodsStoreStockBiz.getGoodsStockOrgaShop(goodsRequestDto.getStoreDeliveryInfo().getUrStoreId(), basicSelectGoodsVo.getIlItemCode());
						}

						ItemStoreInfoVo itemStoreInfo = getItemStoreInfo(goodsRequestDto.getStoreDeliveryInfo().getUrStoreId(), basicSelectGoodsVo.getIlItemCode());
						if (itemStoreInfo != null) {
							stock = itemStoreInfo.getStoreStock();

							StorePriceDto storePriceDto = getStoreSalePrice(basicSelectGoodsVo.getDiscountType(), basicSelectGoodsVo.getRecommendedPrice(), basicSelectGoodsVo.getSalePrice(), itemStoreInfo.getStoreSalePrice());

							basicSelectGoodsVo.setSalePrice(storePriceDto.getSalePrice());
							basicSelectGoodsVo.setDiscountType(storePriceDto.getDiscountType());
							basicSelectGoodsVo.setDiscountTypeName(storePriceDto.getDiscountTypeName());
						}
					}

					if(stock > 0){
						Long storeUrWarehouseId = getBasicStoreWarehouseStoreId();
						UrWarehouseVo warehouseVo = storeWarehouseBiz.getWarehouse(storeUrWarehouseId);
                        basicSelectGoodsVo.setUrWareHouseId(warehouseVo.getUrWarehouseId());
                        basicSelectGoodsVo.setWarehouseGroupCode(warehouseVo.getWarehouseGroupCode());
                        basicSelectGoodsVo.setDawnDeliveryYn(warehouseVo.getDawnDeliveryYn());
						List<ArrivalScheduledDateDto> arrivalScheduledDateDtoList = getStoreArrivalScheduledDateDtoList(storeUrWarehouseId, goodsRequestDto.getStoreDeliveryInfo().getUrStoreId(), stock, goodsRequestDto.getBuyQty());

						// 도착 예정일에 따른 분기 처리
						ArrivalScheduledDateDto arrivalScheduledDateDto = null;
						if (arrivalScheduledDateDtoList != null && arrivalScheduledDateDtoList.size() > 0 && scheduledDate != null) {
							arrivalScheduledDateDto = getArrivalScheduledDateDtoByArrivalScheduledDate(arrivalScheduledDateDtoList, scheduledDate);
						} else {
							arrivalScheduledDateDto = getLatestArrivalScheduledDateDto(arrivalScheduledDateDtoList);
						}

						basicSelectGoodsVo.setArrivalScheduledDateDtoList(arrivalScheduledDateDtoList);
						basicSelectGoodsVo.setArrivalScheduledDateDto(arrivalScheduledDateDto);
					}
				} else {
					// 상품 재고 정보
					List<ArrivalScheduledDateDto> arrivalScheduledDateDtoList = null;
					if (goodsRequestDto.isDailyDelivery()) {
						// 베이비밀 일일배송상품 일괄배송에 택배 권역일 경우
						arrivalScheduledDateDtoList = getDailyDeliveryArrivalScheduledDateDtoList(
								basicSelectGoodsVo.getUrWareHouseId(), basicSelectGoodsVo.getIlGoodsId(), goodsRequestDto.getBuyQty());
					} else if (goodsRequestDto.isGoodsDailyBulk()) {
						// 베이비밀 일일배송상품 일괄배송에 가맹점 배송일 경우
						arrivalScheduledDateDtoList = getArrivalScheduledDateDtoListForBabymealBulk(
								basicSelectGoodsVo.getUrWareHouseId(), basicSelectGoodsVo.getIlGoodsId(),
								goodsRequestDto.isDawnDelivery(), goodsRequestDto.getBuyQty(), goodsRequestDto.getGoodsDailyCycleType());
					} else {
						arrivalScheduledDateDtoList = getArrivalScheduledDateDtoList(
								basicSelectGoodsVo.getUrWareHouseId(), basicSelectGoodsVo.getIlGoodsId(),
								goodsRequestDto.isDawnDelivery(), goodsRequestDto.getBuyQty(), goodsRequestDto.getGoodsDailyCycleType());
					}

					// 중복 품목 재고 정보
					if (goodsRequestDto.getOverlapBuyItem() != null) {
						convertOverlapBuyItem(goodsRequestDto.getOverlapBuyItem(), basicSelectGoodsVo.getIlItemCode(), goodsRequestDto.getBuyQty(), goodsRequestDto.isDawnDelivery(), arrivalScheduledDateDtoList);
					}

					// 도착 예정일에 따른 분기 처리
					ArrivalScheduledDateDto arrivalScheduledDateDto = null;
					if (arrivalScheduledDateDtoList != null && arrivalScheduledDateDtoList.size() > 0 && scheduledDate != null) {
						arrivalScheduledDateDto = getArrivalScheduledDateDtoByArrivalScheduledDate(arrivalScheduledDateDtoList, scheduledDate);
					}

					// 지정 도착예정일 관련 재고가 없으면
					if (arrivalScheduledDateDto == null && scheduledDate != null) {
						// 정기배송 배송예정일이 dDay15일 이상인경우는 재고는 무한대로 처리
						long daysGap = ChronoUnit.DAYS.between(LocalDate.now(), scheduledDate);
						if (daysGap > GoodsConstants.GOODS_STOCK_MAX_DDAY) {
							arrivalScheduledDateDto = getArrivalScheduledDateDtoByIlGoodsIdArrivalScheduledDate(goodsRequestDto.getIlGoodsId(), scheduledDate);
							if(arrivalScheduledDateDto != null) {
								arrivalScheduledDateDto.setStock(GoodsConstants.GOODS_MAX_STOCK);
								stock = arrivalScheduledDateDto.getStock();

								if(goodsRequestDto.isGoodsDailyBulk() || StringUtil.isNotEmpty(goodsRequestDto.getGoodsDailyCycleType())) {
									arrivalScheduledDateDto.setOrderDate(arrivalScheduledDateDtoList.get(0).getOrderDate());
								}

								arrivalScheduledDateDtoList = new ArrayList<ArrivalScheduledDateDto>();
								arrivalScheduledDateDtoList.add(arrivalScheduledDateDto);
							}
							basicSelectGoodsVo.setArrivalScheduledDateDtoList(arrivalScheduledDateDtoList);
							basicSelectGoodsVo.setArrivalScheduledDateDto(arrivalScheduledDateDto);
						}
					} else {
						if (arrivalScheduledDateDtoList != null && arrivalScheduledDateDtoList.size() > 0) {
							if (arrivalScheduledDateDto != null) {
								arrivalScheduledDateDtoList.clear();
								arrivalScheduledDateDtoList.add(arrivalScheduledDateDto);
							} else {
								arrivalScheduledDateDto = getLatestArrivalScheduledDateDto(arrivalScheduledDateDtoList);
							}

							// 재고는 가장 큰 값으로 리턴
							stock = arrivalScheduledDateDtoList.stream().map(ArrivalScheduledDateDto::getStock).max(Integer::compare).orElse(0);
							basicSelectGoodsVo.setArrivalScheduledDateDtoList(arrivalScheduledDateDtoList);
							basicSelectGoodsVo.setArrivalScheduledDateDto(arrivalScheduledDateDto);
						}
					}
				}
			}
			basicSelectGoodsVo.setStockQty(stock);

			// 상품의 판매상태 다시 체크
			String saleStatus = goodsGoodsService.getSaleStatus(basicSelectGoodsVo.getSaleStatus(), stock);
			basicSelectGoodsVo.setSaleStatus(saleStatus);
		}
		return basicSelectGoodsVo;
	}

	/**
	 * 상품 상세 조회
	 *
	 * @param ilGoodsId
	 * @return DetailSelectGoodsVo
	 * @throws Exception
	 */
	@Override
	public DetailSelectGoodsVo getDetailSelectGoods(Long ilGoodsId) throws Exception {
		return goodsGoodsService.getDetailSelectGoods(ilGoodsId);
	}

	/**
	 * 상품 상세 조회(품목코드이용)
	 *
	 * @param ilItemCd
	 * @return DetailSelectGoodsVo
	 * @throws Exception
	 */
	@Override
	public DetailSelectGoodsVo getDetailSelectGoodsForItem(String ilItemCd) throws Exception {
		return goodsGoodsService.getDetailSelectGoodsForItem(ilItemCd);
	}

	/**
	 * 상품 상세 조회(바코드이용)
	 *
	 * @param barCode
	 * @return DetailSelectGoodsVo
	 * @throws Exception
	 */
	@Override
	public DetailSelectGoodsVo getDetailSelectGoodsForBarCode(String barCode) throws Exception {
		return goodsGoodsService.getDetailSelectGoodsForBarCode(barCode);
	}

	/**
	 * 상품 인증정보
	 *
	 * @param ilGoodsId
	 * @return List<GoodsCertificationListResultVo>
	 * @throws Exception
	 */
	@Override
	public List<GoodsCertificationListResultVo> getGoodsCertificationList(Long ilGoodsId) throws Exception {
		return goodsGoodsService.getGoodsCertificationList(ilGoodsId);
	}

	/**
	 * 상품정보제공고시 항목
	 *
	 * @param ilGoodsId
	 * @return List<GoodsSpecListResultVo>
	 * @throws Exception
	 */
	@Override
	public List<GoodsSpecListResultVo> getGoodsSpecList(Long ilGoodsId) throws Exception {
		return goodsGoodsService.getGoodsSpecList(ilGoodsId);
	}

	/**
	 * 비회원구매가능 여부
	 *
	 * @param goodsType,saleType
	 * @return boolean
	 * @throws Exception
	 */
	@Override
	public boolean isBuyNonMember(String goodsType, String saleType, String limitMaxType) throws Exception {
		return goodsGoodsService.isBuyNonMember(goodsType, saleType, limitMaxType);
	}

	/**
	 * 상품 영양정보
	 *
	 * @param ilItemCode
	 * @return List<GoodsNutritionListResultVo>
	 * @throws Exception
	 */
	@Override
	public List<GoodsNutritionListResultVo> getGoodsNutritionList(String ilItemCode) throws Exception {
		return goodsGoodsService.getGoodsNutritionList(ilItemCode);
	}

	/**
	 * 추가구성 상품 리스트
	 *
	 * @param ilGoodsId
	 * @return List<AdditionalGoodsResultVo>
	 * @throws Exception
	 */
	@Override
	public List<AdditionalGoodsDto> getAdditionalGoodsInfoList(Long ilGoodsId, boolean isMember, boolean isEmployee,
			boolean isDawnDelivery, LocalDate arrivalScheduledDate) throws Exception {
		List<AdditionalGoodsDto> additionalGoodsList = new ArrayList<>();

		List<HashMap> additionalGoodsResultList = goodsGoodsService.getAdditionalGoodsList(ilGoodsId);

		if (!additionalGoodsResultList.isEmpty()) {
			for (int i = 0; i < additionalGoodsResultList.size(); i++) {
				AdditionalGoodsDto additionalGoodsDto = new AdditionalGoodsDto();
				Long targetGoodsId = (Long) additionalGoodsResultList.get(i).get("TARGET_GOODS_ID");

				GoodsRequestDto goodsDto = GoodsRequestDto.builder().ilGoodsId(targetGoodsId)
						.deviceInfo(DeviceUtil.getDirInfo()).isApp(DeviceUtil.isApp()).isMember(isMember)
						.isEmployee(isEmployee).isDawnDelivery(isDawnDelivery)
						.arrivalScheduledDate(arrivalScheduledDate).build();

				// 상품 기본 정보
				BasicSelectGoodsVo basicSelectGoodsVo = getGoodsBasicInfo(goodsDto);

				additionalGoodsDto.setIlGoodsId(targetGoodsId);
				additionalGoodsDto.setUrBrandId(basicSelectGoodsVo.getUrBrandId());
				additionalGoodsDto.setGoodsName(basicSelectGoodsVo.getGoodsName());
				additionalGoodsDto.setRecommendedPrice(basicSelectGoodsVo.getRecommendedPrice());
				int salePrice = (int) additionalGoodsResultList.get(i).get("SALE_PRICE");
				// 판매가가 정가보다 클경우 정가로 처리
				if(basicSelectGoodsVo.getRecommendedPrice() < salePrice) {
					salePrice = basicSelectGoodsVo.getRecommendedPrice();
				}
				additionalGoodsDto.setSalePrice(salePrice);
				additionalGoodsDto.setStockQty(basicSelectGoodsVo.getStockQty());
				additionalGoodsDto.setSaleStatus(basicSelectGoodsVo.getSaleStatus());
				additionalGoodsList.add(additionalGoodsDto);
			}
		}

		return additionalGoodsList;
	}

	/**
	 * 묶음상품 정보
	 *
	 * @param ilGoodsId
	 * @return List<PackageGoodsResultVo>
	 * @throws Exception
	 */
	@Override
	public List<PackageGoodsListDto> getPackagGoodsInfoList(Long ilGoodsId, boolean isMember, boolean isEmployee,
			boolean isDawnDelivery, LocalDate arrivalScheduledDate, int buyQty) throws Exception {
		return getPackagGoodsInfoList(ilGoodsId, isMember, isEmployee, isDawnDelivery, arrivalScheduledDate, buyQty, null);
	}

	/**
	 * 묶음 상품 정보 - 중복 품목 재고
	 */
	@Override
	public List<PackageGoodsListDto> getPackagGoodsInfoList(Long ilGoodsId, boolean isMember, boolean isEmployee,
			boolean isDawnDelivery, LocalDate arrivalScheduledDate, int buyQty, HashMap<String, Integer> overlapBuyItem) throws Exception {
		List<PackageGoodsListDto> packageGoodsList = new ArrayList<>();

		List<PackageGoodsResultVo> packageGoodsVoList = goodsGoodsService.getPackageGoodsList(ilGoodsId);
		if (StringUtil.isNotEmpty(packageGoodsVoList)) {
			for (PackageGoodsResultVo packageGoodsVo : packageGoodsVoList) {
				PackageGoodsListDto packageGoodsDto = new PackageGoodsListDto();
				List<GoodsNutritionListResultVo> packageGoodsNutritionList = new ArrayList<>();

				// 묶음상품 기본정보 (묶음 상품의 구성상품은 그냥 조회)
				GoodsRequestDto packageGoodsRequestDto = GoodsRequestDto.builder()
						.ilGoodsId(new Long(packageGoodsVo.getIlGoodsId()))
						.onlySearchIlGoodsId(true).isBosCreateOrder(true).isDawnDelivery(isDawnDelivery)
						.arrivalScheduledDate(arrivalScheduledDate).buyQty(buyQty * packageGoodsVo.getGoodsQty())
						.overlapBuyItem(overlapBuyItem).build();
				BasicSelectGoodsVo packageGoods = getGoodsBasicInfo(packageGoodsRequestDto);

				// 묶음상품 상세정보
				DetailSelectGoodsVo packageGoodsDetail = goodsGoodsService
						.getDetailSelectGoods(new Long(packageGoodsVo.getIlGoodsId()));

				// 묶음상품 영양정보
				if (packageGoodsDetail.getNutritionDisplayYn().equals("Y")) {
					packageGoodsNutritionList = goodsGoodsService.getGoodsNutritionList(packageGoods.getIlItemCode());
				}

				// 상품정보제공고시
				List<GoodsSpecListResultVo> packageGoodsSpecList = goodsGoodsService
						.getGoodsSpecList(new Long(packageGoodsVo.getIlGoodsId()));

				// 묶음상품 Dto 세팅
				packageGoodsDto.setIlGoodsId(packageGoods.getIlGoodsId());
				packageGoodsDto.setGoodsName(packageGoods.getGoodsName());
				packageGoodsDto.setGift(GoodsType.GIFT.getCode().equals(packageGoods.getGoodsType()) || GoodsType.GIFT_FOOD_MARKETING.getCode().equals(packageGoods.getGoodsType()));
				packageGoodsDto.setUrBrandId(packageGoods.getUrBrandId());
				packageGoodsDto.setSpec(packageGoodsSpecList);
				packageGoodsDto.setNutritionDispYn(packageGoodsDetail.getNutritionDisplayYn());
				packageGoodsDto.setNutrition(packageGoodsNutritionList);
				packageGoodsDto.setNutritionQtyPerOnce(packageGoodsDetail.getNutritionQtyPerOnce());
				packageGoodsDto.setNutritionQtyTotal(packageGoodsDetail.getNutritionQtyTotal());
				packageGoodsDto.setNutritionEtc(packageGoodsDetail.getNutritionEtc());
				packageGoodsDto.setBasicDescription(packageGoodsDetail.getBasicDescription());
				packageGoodsDto.setDetailDescription(packageGoodsDetail.getDetailDescription());
				packageGoodsDto.setRecommendedPrice(packageGoods.getRecommendedPrice());
				packageGoodsDto.setSalePrice(packageGoodsVo.getUnitSalePrice());
				packageGoodsDto.setSaleTotalPrice(packageGoodsVo.getSalePrice());
				packageGoodsDto.setTaxYn(packageGoodsVo.getTaxYn());
				packageGoodsDto.setGoodsQty(packageGoodsVo.getGoodsQty());
				packageGoodsDto.setSaleStatus(packageGoods.getSaleStatus());
				packageGoodsDto.setArrivalScheduledDateDtoList(packageGoods.getArrivalScheduledDateDtoList());
				packageGoodsDto.setIlItemCd(packageGoods.getIlItemCode());
				List<GoodsImageDto> goodsImageList = goodsGoodsService.getItemImageList(packageGoods.getIlItemCode());
				if (goodsImageList != null && !goodsImageList.isEmpty()) {
					packageGoodsDto.setThumbnailPath(goodsImageList.stream().filter(img -> "Y".equals(img.getBasicYn()))
							.findAny().orElse(goodsImageList.get(0)).getMsImage());
				}

				packageGoodsList.add(packageGoodsDto);
			}

			// 묶음 상품은 도착 예정일 같은 상품끼리 재고 처리 해야함
			List<LocalDate> intersectionArrivalScheduledDateList = goodsGoodsService
					.intersectionArrivalScheduledDateListByDto(packageGoodsList.stream()
							.map(PackageGoodsListDto::getArrivalScheduledDateDtoList).collect(Collectors.toList()));

			// 구성상품 품목별 총 구성수량 확인
			Map<String, Integer> totalItemQuantity = packageGoodsVoList.stream()
					.collect(Collectors.toMap(e->e.getIlItemCd(), e->e.getGoodsQty(), Integer::sum));

			packageGoodsList.stream().forEach(dto -> {
				try {
					List<ArrivalScheduledDateDto> arrivalScheduledDateDtoList = goodsGoodsService
							.intersectionArrivalScheduledDateDtoList(dto.getArrivalScheduledDateDtoList(),
									intersectionArrivalScheduledDateList);

					// 묶음상품 구성상품의 실 재고수량 = 구성상품 재고 / 묶음상품내 같은 품목코드의 총 구성수량
					if(arrivalScheduledDateDtoList != null && !arrivalScheduledDateDtoList.isEmpty()){
						for(ArrivalScheduledDateDto arrivalScheduledDateDto : arrivalScheduledDateDtoList){
							int arrivalScheduledDateDtoStock = arrivalScheduledDateDto.getStock();
							int itemQuantity = totalItemQuantity.get(dto.getIlItemCd()); // 묶음상품내 같은 품목코드의 총 구성수량

							if(arrivalScheduledDateDtoStock > 0){
								arrivalScheduledDateDtoStock = (int) Math.floor(arrivalScheduledDateDtoStock / itemQuantity);
							}
							arrivalScheduledDateDto.setStock(arrivalScheduledDateDtoStock);
						}
						dto.setArrivalScheduledDateDtoList(arrivalScheduledDateDtoList);
						dto.setArrivalScheduledDateDto(goodsGoodsService.getLatestArrivalScheduledDateDto(arrivalScheduledDateDtoList));
						dto.setStockQty(dto.getArrivalScheduledDateDto().getStock());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}

		return packageGoodsList;
	}

	/**
	 * 묶음상품 재계산
	 *
	 * @param goodsSaleStatus
	 * @param packageGoodsList
	 * @return RecalculationPackageDto
	 * @throws Exception
	 */
	@Override
	public RecalculationPackageDto getRecalculationPackage(String goodsSaleStatus, List<PackageGoodsListDto> packageGoodsList) throws Exception{
		RecalculationPackageDto recalculationPackageDto = new RecalculationPackageDto();
		recalculationPackageDto.setSaleStatus(goodsSaleStatus);

		List<ArrivalScheduledDateDto> tempPackageArrivalScheduledDateDtoList = null;
		ArrivalScheduledDateDto tempArrivalScheduledDateDto = null;

		for (PackageGoodsListDto packageList : packageGoodsList) {

		  if (!packageList.getSaleStatus().equals(GoodsEnums.SaleStatus.ON_SALE.getCode())) {
				if ((GoodsEnums.SaleStatus.ON_SALE.getCode().equals(recalculationPackageDto.getSaleStatus())
						|| GoodsEnums.SaleStatus.OUT_OF_STOCK_BY_SYSTEM.getCode().equals(recalculationPackageDto.getSaleStatus()))
						&& (GoodsEnums.SaleStatus.OUT_OF_STOCK_BY_SYSTEM.getCode().equals(packageList.getSaleStatus())
						|| GoodsEnums.SaleStatus.OUT_OF_STOCK_BY_MANAGER.getCode().equals(packageList.getSaleStatus()))) {
					// 품절 상태는 품절 처리로
					recalculationPackageDto.setSaleStatus(GoodsEnums.SaleStatus.OUT_OF_STOCK_BY_SYSTEM.getCode());
				} else {
					// 품절 외 상태는 판매 중지로 처리
					recalculationPackageDto.setSaleStatus(GoodsEnums.SaleStatus.STOP_SALE.getCode());
				}
		  }

	      	// 묶음 상품 스케줄별 최소 수량 구하기 위한 로직
			if (tempPackageArrivalScheduledDateDtoList == null || packageList.getArrivalScheduledDateDtoList() == null) {
				tempPackageArrivalScheduledDateDtoList = packageList.getArrivalScheduledDateDtoList();
			} else {
				tempPackageArrivalScheduledDateDtoList = getMinStockArrivalScheduledDateDtoList(
								tempPackageArrivalScheduledDateDtoList,packageList.getArrivalScheduledDateDtoList());
			}
        }

		tempArrivalScheduledDateDto = goodsGoodsService.getLatestArrivalScheduledDateDto(tempPackageArrivalScheduledDateDtoList);
		int stock = 0;
		if (tempPackageArrivalScheduledDateDtoList != null) {
			stock = tempPackageArrivalScheduledDateDtoList.stream().map(ArrivalScheduledDateDto::getStock).max(Integer::compare).orElse(0);
		}

        recalculationPackageDto.setArrivalScheduledDateDtoList(tempPackageArrivalScheduledDateDtoList);
        recalculationPackageDto.setArrivalScheduledDateDto(tempArrivalScheduledDateDto);
        recalculationPackageDto.setStockQty(stock);
        recalculationPackageDto.setSaleStatus(goodsGoodsService.getSaleStatus(recalculationPackageDto.getSaleStatus(), stock));

		return recalculationPackageDto;
	}


	/**
	 * 예약상품 옵션 정보
	 *
	 * @param ilGoodsId
	 * @return List<ReserveOptionListDto>
	 * @throws Exception
	 */
	@Override
	public List<GoodsReserveOptionListDto> getGoodsReserveOptionList(Long ilGoodsId) throws Exception {
		return goodsGoodsService.getGoodsReserveOptionList(ilGoodsId);
	}

	@Override
	public GoodsReserveOptionVo getGoodsReserveOption(Long ilGoodsReserveOptnId) throws Exception {
		return goodsGoodsService.getGoodsReserveOption(ilGoodsReserveOptnId);
	}

	/**
	 * 일일배송 주기 - 녹즙
	 *
	 * @param zipCode String
	 * @param buildingCode String
	 * @return List<GoodsDailyCycleDto>
	 * @throws Exception Exception
	 */
	public List<GoodsDailyCycleDto> getGreenJuiceDailyCycleList(String zipCode, String buildingCode) throws Exception {
		List<GoodsDailyCycleDto> goodsDailyCycleList = new ArrayList<>();
		ShippingPossibilityStoreDeliveryAreaDto areaInfo = storeDeliveryBiz.getShippingPossibilityStoreDeliveryAreaInfoByDailySupplierId(GoodsConstants.GREEN_JUICE_UR_SUPPLIER_ID, zipCode, buildingCode);

		// 조회값 없을경우 매일로 설정
		if (areaInfo == null) {
			areaInfo = new ShippingPossibilityStoreDeliveryAreaDto();
			areaInfo.setStoreDeliveryIntervalType(GoodsEnums.StoreDeliveryInterval.EVERY.getCode());
		}
		if(areaInfo.getStoreDeliveryIntervalType() == null){
			areaInfo.setStoreDeliveryIntervalType(GoodsEnums.StoreDeliveryInterval.EVERY.getCode());
		}

		// 배송주기
		List<HashMap<String, String>> goodsDailyCycleGreenJuiceList = goodsGoodsService.getGoodsDailyCycleGreenJuiceList();
		for (HashMap<String, String> goodsDailyCyleGreenJuice : goodsDailyCycleGreenJuiceList) {
			// 배송기간
			List<HashMap<String, String>> goodsDailyCycleTermGreenJuiceList = goodsGoodsService.getGoodsDailyCycleTermGreenJuiceList();
			// 배송주기가 주 1일인 경우만 배송기간 중 ‘4주’ 옵션을 선택하지 못하게 예외처리
			if(GoodsCycleTypeByGreenJuice.DAY1_PER_WEEK.getCode().equals(goodsDailyCyleGreenJuice.get("GOODS_CYCLE_TP"))) {
				goodsDailyCycleTermGreenJuiceList.remove(0);
			}
			// 녹즙 요일코드
			List<HashMap<String, String>> dailyCycleGreenJuiceWeekList = goodsGoodsService
					.getDailyCycleGreenJuiceWeekList(areaInfo.getStoreDeliveryIntervalType());
			GoodsDailyCycleDto goodsDailyCycleDto = new GoodsDailyCycleDto();
			goodsDailyCycleDto.setGoodsDailyCycleType(goodsDailyCyleGreenJuice.get("GOODS_CYCLE_TP"));
			goodsDailyCycleDto.setGoodsDailyCycleTypeName(goodsDailyCyleGreenJuice.get("GOODS_CYCLE_TP_NM"));
			goodsDailyCycleDto.setGoodsDailyCycleTypeQty(
					Integer.parseInt(goodsDailyCyleGreenJuice.get("GOODS_CYCLE_TP_QTY")));
			goodsDailyCycleDto.setTerm(goodsDailyCycleTermGreenJuiceList);
			goodsDailyCycleDto.setWeek(dailyCycleGreenJuiceWeekList);
			goodsDailyCycleDto.setGoodsDailyCycleTypeDayQty(
					Integer.parseInt(goodsDailyCyleGreenJuice.get("GOODS_CYCLE_TP_DAY_QTY")));
			goodsDailyCycleDto.setIsEnable(true);
			// 격일의 경우 주4일, 주5일, 주6일 비활성화
			if (areaInfo.getStoreDeliveryIntervalType().equals(GoodsEnums.StoreDeliveryInterval.TWO_DAYS.getCode())) {
				if (goodsDailyCyleGreenJuice.get("GOODS_CYCLE_TP").equals(GoodsEnums.GoodsCycleTypeByGreenJuice.DAYS4_PER_WEEK.getCode())
						|| goodsDailyCyleGreenJuice.get("GOODS_CYCLE_TP").equals(GoodsEnums.GoodsCycleTypeByGreenJuice.DAYS5_PER_WEEK.getCode())
						|| goodsDailyCyleGreenJuice.get("GOODS_CYCLE_TP").equals(GoodsEnums.GoodsCycleTypeByGreenJuice.DAYS6_PER_WEEK.getCode())) {
					goodsDailyCycleDto.setIsEnable(false);
				}
			}
			goodsDailyCycleList.add(goodsDailyCycleDto);
		}

		return goodsDailyCycleList;
	}

	/**
	 * 일일배송 주기
	 *
	 * @param ilGoodsId, goodsDailyType
	 * @return List<HashMap>
	 * @throws Exception
	 */
	@Override
	public List<GoodsDailyCycleDto> getGoodsDailyCycleList(Long ilGoodsId, String goodsDailyType, String zipCode, String buildingCode) throws Exception {
		List<GoodsDailyCycleDto> goodsDailyCycleList = new ArrayList<>();

		// 일일배송 - 녹즙일 때
		if (goodsDailyType.equals(GoodsEnums.GoodsDailyType.GREENJUICE.getCode())) {
			goodsDailyCycleList = getGreenJuiceDailyCycleList(zipCode, buildingCode);
			if (goodsGoodsService.isCleanseOption(ilGoodsId)) {
				// 일일상품인 경우 스토어 배송권역 정보, 세션의 우편번호/건물번호 조회하여 데이터가 있으면 isShippingPossibility 가능
				ShippingPossibilityStoreDeliveryAreaDto areaInfo = storeDeliveryBiz.getShippingPossibilityStoreDeliveryAreaInfo(ilGoodsId, zipCode, buildingCode);
				// 조회값 없을경우 매일로 설정
				if (areaInfo == null) {
					areaInfo = new ShippingPossibilityStoreDeliveryAreaDto();
					areaInfo.setStoreDeliveryIntervalType(GoodsEnums.StoreDeliveryInterval.EVERY.getCode());
				}

				goodsGoodsService.convertCleanseOption(areaInfo.getStoreDeliveryIntervalType(), goodsDailyCycleList);
			}
		} else { // 일일배송 - 베이비밀,잇슬림일 때
			// 일일상품인 경우 스토어 배송권역 정보, 세션의 우편번호/건물번호 조회하여 데이터가 있으면 isShippingPossibility 가능
			ShippingPossibilityStoreDeliveryAreaDto areaInfo = storeDeliveryBiz.getShippingPossibilityStoreDeliveryAreaInfo(ilGoodsId, zipCode, buildingCode);
			// 조회값 없을경우 매일로 설정
			if (areaInfo == null) {
				areaInfo = new ShippingPossibilityStoreDeliveryAreaDto();
				areaInfo.setStoreDeliveryIntervalType(GoodsEnums.StoreDeliveryInterval.EVERY.getCode());
			}
			// 배송주기
			List<HashMap<String, String>> goodsDailyCycleTypeList = goodsGoodsService.getGoodsDailyCycleList(ilGoodsId);
			// 배송기간
			for (HashMap<String, String> goodsDailyCycle : goodsDailyCycleTypeList) {
				List<HashMap<String, String>> goodsDailyCycleTermList = goodsGoodsService
						.getGoodsDailyCycleTermList(ilGoodsId, goodsDailyCycle.get("GOODS_CYCLE_TP"));
				GoodsDailyCycleDto goodsDailyCycleDto = new GoodsDailyCycleDto();
				goodsDailyCycleDto.setGoodsDailyCycleType(goodsDailyCycle.get("GOODS_CYCLE_TP"));
				GoodsCycleType goodsCycleType = GoodsCycleType.findByCode(goodsDailyCycle.get("GOODS_CYCLE_TP"));
				goodsDailyCycleDto.setGoodsDailyCycleTypeName(goodsCycleType.getCodeName());
				goodsDailyCycleDto.setGoodsDailyCycleTypeWeekText(goodsCycleType.getWeekText());
				goodsDailyCycleDto.setGoodsDailyCycleTypeQty(Integer.parseInt(goodsDailyCycle.get("GOODS_CYCLE_TP_QTY")));
				goodsDailyCycleDto.setGoodsDailyCycleTypeDayQty(Integer.parseInt(goodsDailyCycle.get("GOODS_CYCLE_TP_DAY_QTY")));
				goodsDailyCycleDto.setTerm(goodsDailyCycleTermList);
				goodsDailyCycleDto.setIsEnable(false);
				if (GoodsEnums.StoreDeliveryInterval.TWO_DAYS.getCode().equals(areaInfo.getStoreDeliveryIntervalType())) {
					if (GoodsEnums.GoodsCycleTypeByGreenJuice.DAY1_PER_WEEK.getCode().equals(goodsDailyCycleDto.getGoodsDailyCycleType())
							|| GoodsEnums.GoodsCycleTypeByGreenJuice.DAYS3_PER_WEEK.getCode().equals(goodsDailyCycleDto.getGoodsDailyCycleType())) {
						goodsDailyCycleDto.setIsEnable(true);
					}
				} else if (GoodsEnums.StoreDeliveryInterval.EVERY.getCode().equals(areaInfo.getStoreDeliveryIntervalType())) {
					goodsDailyCycleDto.setIsEnable(true);
				}
				goodsDailyCycleList.add(goodsDailyCycleDto);
			}
		}
		return goodsDailyCycleList;
	}

	/**
	 * 일괄 배송
	 *
	 * @param ilGoodsId
	 * @return List<HashMap>
	 * @throws Exception
	 */
	@Override
	public List<HashMap<String, String>> getGoodsDailyBulkList(Long ilGoodsId) throws Exception {
		List<HashMap<String, String>> goodsDailyBulkList = new ArrayList<>();
		goodsDailyBulkList = goodsGoodsService.getGoodsDailyBulkList(ilGoodsId);
		for (HashMap<String, String> goodsDailyBulkMap : goodsDailyBulkList) {
			GoodsEnums.GoodsDailyBulkQtyType goodsDailyBulkQtyType = GoodsEnums.GoodsDailyBulkQtyType.findByCode(goodsDailyBulkMap.get("goodsBulkType"));
			goodsDailyBulkMap.put("goodsBulkTypeQty", (goodsDailyBulkQtyType == null ? "0" : goodsDailyBulkQtyType.getCodeName()));
		}

		return goodsDailyBulkList;
	}

	/**
	 * 상품 이미지
	 *
	 * @param ilGoodsId
	 * @return List<HashMap>
	 * @throws Exception
	 */
	@Override
	public List<GoodsImageDto> getGoodsImageList(String goodsType, Long ilGoodsId, String ilItemCode,
			String goodsPackageImageType) throws Exception {
		List<GoodsImageDto> goodsImageList = new ArrayList<>();

		// 묶음상품인경우
		if (goodsType.equals(GoodsEnums.GoodsType.PACKAGE.getCode())) {

			// 개별상품 전용인 경우
			if (goodsPackageImageType.equals(GoodsEnums.GoodsPackageImageType.NORMAL_GOODS.getCode())) {

				goodsImageList = goodsGoodsService.getGoodsPackageBasicItemImageList(ilGoodsId);

				// 묶음상품 전용인 경우
			} else if (goodsPackageImageType.equals(GoodsEnums.GoodsPackageImageType.PACKAGE_GOODS.getCode())) {

				goodsImageList = goodsGoodsService.getGoodsPackageGoodsImageList(ilGoodsId);

				// 묶음&개별상품 조합인 경우
			} else if (goodsPackageImageType.equals(GoodsEnums.GoodsPackageImageType.MIXED.getCode())) {

				// 묶음상품 기본이미지
				GoodsImageDto goodsPackageBasicGoodsImage = goodsGoodsService.getGoodsBasicImage(ilGoodsId);
				// 개별상품 이미지 리스트
				List<GoodsImageDto> goodsPackageBasicItemImageList = goodsGoodsService
						.getGoodsPackageBasicItemImageList(ilGoodsId);

				goodsImageList.add(goodsPackageBasicGoodsImage);
				for (GoodsImageDto goodsImageDto : goodsPackageBasicItemImageList) {
					goodsImageList.add(goodsImageDto);
				}

			}
		} else {
			goodsImageList = goodsGoodsService.getItemImageList(ilItemCode);
		}

		return goodsImageList;
	}

	/**
	 * 상품 이미지
	 *
	 * @param ilGoodsId
	 * @return List<HashMap>
	 * @throws Exception
	 */
	@Override
	public GoodsImageDto getGoodsBasicImage(String goodsType, Long ilGoodsId, String ilItemCode,
			String goodsPackageImageType) throws Exception {
		GoodsImageDto goodsImage = null;

		// 묶음상품인경우

		if (goodsType.equals(GoodsEnums.GoodsType.PACKAGE.getCode())) {

			// 개별상품 전용인 경우
			if (goodsPackageImageType.equals(GoodsEnums.GoodsPackageImageType.NORMAL_GOODS.getCode())) {

				// 아이템
				goodsImage = goodsGoodsService.getItemBaiscImage(ilItemCode);

			} else {
				// 상품
				goodsImage = goodsGoodsService.getGoodsBasicImage(ilGoodsId);
			}
		} else {
			// 아이템
			goodsImage = goodsGoodsService.getItemBaiscImage(ilItemCode);
		}

		return goodsImage;
	}

	/**
	 * 판매상태에 따른 스토어 타입
	 *
	 * @param saleType
	 * @return List<String>
	 * @throws Exception
	 */
	@Override
	public String getStoreTypeBySaleType(String saleType) throws Exception {
		return goodsGoodsService.getStoreTypeBySaleType(saleType);
	}

	/**
	 * 배송가능품목유형
	 *
	 * @param urSupplierId
	 * @return List<String>
	 * @throws Exception
	 */
	@Override
	public List<String> getStoreDeliverableItemTypeBySupplierId(Long urSupplierId) throws Exception {
		return goodsGoodsService.getStoreDeliverableItemTypeBySupplierId(urSupplierId);
	}

	/**
	 * 배송주기에 따른 격일 체크
	 *
	 * @param goodsCycleType
	 * @return boolean
	 * @throws Exception
	 */
	@Override
	public boolean isEveryOtherDay(String goodsCycleType) throws Exception {
		return goodsGoodsService.isEveryOtherDay(goodsCycleType);
	}

	/**
	 * 임직원할인 계산
	 *
	 * @param employeeDiscountRate, employeeRemainingPoint, recommendedPrice
	 * @return EmployeeDiscountInfoDto
	 * @throws Exception
	 */
	@Override
	public EmployeeDiscountInfoDto employeeDiscountCalculation(int employeeDiscountRate, int employeeRemainingPoint,
			int recommendedPrice, int qty) throws Exception {
		return goodsGoodsService.employeeDiscountCalculation(employeeDiscountRate, employeeRemainingPoint,
				recommendedPrice, qty);
	}

	@Override
	public EmployeeDiscountInfoDto employeeDiscountCalculationPackage(Long ilGoodsId, List<PolicyBenefitEmployeeByUserVo> policyBenefitEmployeeInfo, List<PackageGoodsListDto> goodsPackageList, int qty) throws Exception {
		List<EmployeeDiscountInfoDto> discountList = new ArrayList<EmployeeDiscountInfoDto>();
		if(policyBenefitEmployeeInfo != null) {
			List<GoodsPackageEmployeeDiscountDto> goodsEmployeeDiscountList = getPackageGoodsEmployeeDiscountRatio(ilGoodsId);
			for (PackageGoodsListDto goodsPackage : goodsPackageList) {
				if (!goodsPackage.isGift()) {
					PolicyBenefitEmployeeByUserVo findPolicyBenefitEmployeeVo = policyBenefitEmployeeBiz
							.findEmployeeDiscountBrandByUser(policyBenefitEmployeeInfo, goodsPackage.getUrBrandId());

			        PolicyBenefitEmployeeBrandGroupByUserVo policyBenefitEmployeeBrandByUserVo = policyBenefitEmployeeBiz
							.getDiscountRatioEmployeeDiscountBrand(findPolicyBenefitEmployeeVo, goodsPackage.getUrBrandId());

					if(policyBenefitEmployeeBrandByUserVo!= null) {
						int employeeDiscountRatio = policyBenefitEmployeeBrandByUserVo.getDiscountRatio();
						if (goodsEmployeeDiscountList != null && !goodsEmployeeDiscountList.isEmpty()) {
							GoodsPackageEmployeeDiscountDto goodsPackageEmployeeDiscountDto = findGoodsPackageEmployeeDiscountDto(goodsEmployeeDiscountList, goodsPackage.getIlGoodsId());
							if (goodsPackageEmployeeDiscountDto!= null && goodsPackageEmployeeDiscountDto.getDiscountRatio() > 0) {
								employeeDiscountRatio = goodsPackageEmployeeDiscountDto.getDiscountRatio();
							}
						}
						discountList.add(goodsGoodsService.employeeDiscountCalculation(employeeDiscountRatio, BuyerConstants.EMPLOYEE_MAX_POINT, goodsPackage.getRecommendedPrice(), goodsPackage.getGoodsQty() * qty));
					} else {
						EmployeeDiscountInfoDto discountDto = new EmployeeDiscountInfoDto();
						discountDto.setNoDiscountPrice(goodsPackage.getRecommendedPrice() * goodsPackage.getGoodsQty() * qty, goodsPackage.getRecommendedPrice());
						discountList.add(discountDto);
					}
				}
			}
		}

		return sumEmployeeDiscountInfoDtoList(discountList);
	}

	@Override
	public void employeeDiscountCalculationAddGoods(List<PolicyBenefitEmployeeByUserVo> policyBenefitEmployeeInfo, List<AdditionalGoodsDto> additionalGoodsList) throws Exception {
		for(AdditionalGoodsDto addGoodsDto : additionalGoodsList) {

			PolicyBenefitEmployeeByUserVo findPolicyBenefitEmployeeVo = policyBenefitEmployeeBiz
					.findEmployeeDiscountBrandByUser(policyBenefitEmployeeInfo, addGoodsDto.getUrBrandId());

	        PolicyBenefitEmployeeBrandGroupByUserVo policyBenefitEmployeeBrandByUserVo = policyBenefitEmployeeBiz
					.getDiscountRatioEmployeeDiscountBrand(findPolicyBenefitEmployeeVo, addGoodsDto.getUrBrandId());

	        if(policyBenefitEmployeeBrandByUserVo!= null) {
				EmployeeDiscountInfoDto employeeDiscountInfoDto = goodsGoodsService.employeeDiscountCalculation(policyBenefitEmployeeBrandByUserVo.getDiscountRatio(), BuyerConstants.EMPLOYEE_MAX_POINT, addGoodsDto.getRecommendedPrice(), 1);
				addGoodsDto.setSalePrice(employeeDiscountInfoDto.getDiscountAppliedPrice());
			}
		}
	}

	@Override
	public EmployeeDiscountInfoDto sumEmployeeDiscountInfoDtoList(List<EmployeeDiscountInfoDto> list) throws Exception {
		EmployeeDiscountInfoDto result = new EmployeeDiscountInfoDto();

		result.setExcessPrice(list.stream().collect(Collectors.summingInt(EmployeeDiscountInfoDto::getExcessPrice)));
		result.setDiscountPrice(list.stream().collect(Collectors.summingInt(EmployeeDiscountInfoDto::getDiscountPrice)));
		result.setDiscountAppliedPrice(list.stream().collect(Collectors.summingInt(EmployeeDiscountInfoDto::getDiscountAppliedPrice)));
		result.setSalePrice(list.stream().collect(Collectors.summingInt(EmployeeDiscountInfoDto::getSalePrice)));

		return result;
	}

	/**
	 * 최근 도착 예정 dto
	 */
	@Override
	public ArrivalScheduledDateDto getLatestArrivalScheduledDateDto(Long urWarehouseId, Long ilGoodsId,
			boolean isDawnDelivery, String goodsDailyCycleType) throws Exception {
		List<ArrivalScheduledDateDto> list = getArrivalScheduledDateDtoList(urWarehouseId, ilGoodsId, isDawnDelivery,
				goodsDailyCycleType);
		return goodsGoodsService.getLatestArrivalScheduledDateDto(list);
	}

	/**
	 * 최근 도착 예정 dto
	 */
	@Override
	public ArrivalScheduledDateDto getLatestArrivalScheduledDateDto(List<ArrivalScheduledDateDto> list)
			throws Exception {
		return goodsGoodsService.getLatestArrivalScheduledDateDto(list);
	}

	/**
	 * 최근 도착 예정일 같은 최근 도착 예정일 DTO 조회
	 */
	@Override
	public ArrivalScheduledDateDto getArrivalScheduledDateDtoByArrivalScheduledDate(List<ArrivalScheduledDateDto> list,
			LocalDate arrivalScheduledDate) throws Exception {
		return goodsGoodsService.getArrivalScheduledDateDtoByArrivalScheduledDate(list, arrivalScheduledDate);
	}

	@Override
	public ArrivalScheduledDateDto getArrivalScheduledDateDtoByOrderDate(List<ArrivalScheduledDateDto> list,
			LocalDate orderDate) throws Exception {
		return goodsGoodsService.getArrivalScheduledDateDtoByOrderDate(list, orderDate);
	}

	/**
	 * 상품별 출고 예정일 합집합 리스트 리턴
	 */
	@Override
	public List<LocalDate> unionArrivalScheduledDateListByDto(
			List<List<ArrivalScheduledDateDto>> goodsArrivalScheduledDateList) throws Exception {
		return goodsGoodsService.unionArrivalScheduledDateListByDto(goodsArrivalScheduledDateList);
	}

	/**
	 * 상품별 출고 예정일 교집합 리스트 리턴
	 */
	@Override
	public List<LocalDate> intersectionArrivalScheduledDateListByDto(
			List<List<ArrivalScheduledDateDto>> goodsArrivalScheduledDateList) throws Exception {
		return goodsGoodsService.intersectionArrivalScheduledDateListByDto(goodsArrivalScheduledDateList);
	}

	/**
	 * 출고 예정일 Dto 리스트 추출
	 */
	@Override
	public List<ArrivalScheduledDateDto> intersectionArrivalScheduledDateDtoList(List<ArrivalScheduledDateDto> list,
			List<LocalDate> arrivalScheduledDateList) throws Exception {
		return goodsGoodsService.intersectionArrivalScheduledDateDtoList(list, arrivalScheduledDateList);
	}

	/**
	 * 도착 예정일 리스트
	 */
	@Override
	public List<ArrivalScheduledDateDto> getArrivalScheduledDateDtoList(Long urWarehouseId, Long ilGoodsId,
			boolean isDawnDelivery, String goodsDailyCycleType) throws Exception {
		return getArrivalScheduledDateDtoList(urWarehouseId, ilGoodsId, isDawnDelivery, 1, goodsDailyCycleType);
	}

	/**
	 * 도착 예정일 리스트 - 구매수량 체크
	 */
	@Override
	public List<ArrivalScheduledDateDto> getArrivalScheduledDateDtoList(Long urWarehouseId, Long ilGoodsId,
			boolean isDawnDelivery, int buyQty, String goodsDailyCycleType) throws Exception {

		LocalDate goodsDailyTpIfDate = LocalDate.now();

		UrWarehouseVo warehouseVo = storeWarehouseBiz.getWarehouse(urWarehouseId);

		// 품목 체크 일자 리스트
		List<ArrivalScheduledDateDto> scheduledDateList = goodsGoodsService.getGheckRangeDateList();

		// 주문마감 시간 확인 후 도착일 변경 / 최초 주문IF일자 조회
		IfSpecificDateDto ifSpecificDate = getArrivalScheduleCloseTimeAndFistOrderDate(goodsDailyTpIfDate, scheduledDateList, isDawnDelivery, warehouseVo);
		goodsDailyTpIfDate = ifSpecificDate.getGoodsDailyTpIfDate();
		scheduledDateList = ifSpecificDate.getScheduledDateList();

		// 특정날짜 제외한 도착예정일 조회
		scheduledDateList = getArrivalScheduleExcludedSpecificDate(scheduledDateList, warehouseVo, ilGoodsId, isDawnDelivery, buyQty, goodsDailyCycleType);

		// 일일배송상품일경우 인터페이스 일자는 [주문마감 전 : 금일, 이후 : 익일] 로 고정
		if (StringUtil.isNotEmpty(goodsDailyCycleType)) {
			getDailyGoodsFixedFastestOrderIfDate(goodsDailyTpIfDate, scheduledDateList);
		}

		return scheduledDateList;
	}

	/**
	 * 선주문상품의 도착예정일 리스트 구하기
	 */
	@Override
	public List<ArrivalScheduledDateDto> getAddDeliveryScheduleDateForPreOrderGoods(Long urWarehouseId, boolean isDawnDelivery, int baseDay, int addDay) throws Exception {
		LocalDate goodsDailyTpIfDate = LocalDate.now();

		//출고처 정보 조회
		UrWarehouseVo warehouseVo = storeWarehouseBiz.getWarehouse(urWarehouseId);

		//D0~D15까지 일자만 생성
		List<ArrivalScheduledDateDto> scheduledDateList = goodsGoodsService.getAddDeliveryScheduleRangeDateList(baseDay, addDay);
		
		//주문마감시간 체크하여, 주문마감시간 이후이면 D0 remove 처리
		IfSpecificDateDto ifSpecificDate = getArrivalScheduleCloseTimeAndFistOrderDate(goodsDailyTpIfDate, scheduledDateList, isDawnDelivery, warehouseVo);
		scheduledDateList = ifSpecificDate.getScheduledDateList();

		//배송패턴정보 조회로 {주문IF일, 출고일, 배송일} 구함
		Long psShippingPatternId = warehouseVo.getPsShippingPatternId();
		if (isDawnDelivery) {
			psShippingPatternId = warehouseVo.getDawnDeliveryPsShippingPatternId();
		}
		scheduledDateList = goodsGoodsService.setShippingPatternDay(psShippingPatternId, scheduledDateList);

		// 출고처 휴일 제거
		if (scheduledDateList != null && scheduledDateList.size() > 0) {
			scheduledDateList = storeWarehouseBiz.rmoveHoliday(warehouseVo.getUrWarehouseId(), scheduledDateList, isDawnDelivery);
		}

		// 일별 출고 한도
		if (scheduledDateList != null && scheduledDateList.size() > 0) {
			if(warehouseVo.getLimitCnt() > 0) {
				scheduledDateList = orderOrderBiz.removeDailyDeliveryLimitCnt(warehouseVo.getUrWarehouseId(), scheduledDateList,
						warehouseVo.getLimitCnt(), false);
			}
			// 새벽배송일때
			if (isDawnDelivery && warehouseVo.getDawnLimitCnt() > 0
					&& scheduledDateList != null && scheduledDateList.size() > 0) {
				scheduledDateList = orderOrderBiz.removeDailyDeliveryLimitCnt(warehouseVo.getUrWarehouseId(), scheduledDateList,
						warehouseVo.getDawnLimitCnt(), true);
			}
		}

		// 도착완료일자 중복 제거
		scheduledDateList = goodsGoodsService.uniqueArrivalScheduledDateList(scheduledDateList);

		return scheduledDateList;
	}
	
	
	/**
	 * 일일배송상품일경우 인터페이스 일자는 [주문마감 전 : 금일, 이후 : 익일] 로 고정
	 */
	@Override
	public void getDailyGoodsFixedFastestOrderIfDate(LocalDate goodsDailyTpIfDate, List<ArrivalScheduledDateDto> scheduledDateList) {
		goodsGoodsService.getDailyGoodsFixedFastestOrderIfDate(goodsDailyTpIfDate, scheduledDateList);
	}

	/**
	 * 도착 예정일 리스트 - 구매수량 체크
	 * 베이비밀 일괄배송 상품
	 */
	@Override
	public List<ArrivalScheduledDateDto> getArrivalScheduledDateDtoListForBabymealBulk(
			Long urWarehouseId
			, Long ilGoodsId
			, boolean isDawnDelivery
			, int buyQty
			, String goodsDailyCycleType) throws Exception {

		LocalDate goodsDailyTpIfDate = LocalDate.now();

		UrWarehouseVo warehouseVo = storeWarehouseBiz.getWarehouse(urWarehouseId);

		// 품목 체크 일자 리스트
		List<ArrivalScheduledDateDto> scheduledDateList = goodsGoodsService.getGheckRangeDateList();

		// 주문마감 시간 확인 후 도착일 변경 / 최초 주문IF일자 조회
		IfSpecificDateDto ifSpecificDate = getArrivalScheduleCloseTimeAndFistOrderDate(goodsDailyTpIfDate, scheduledDateList, isDawnDelivery, warehouseVo);
		goodsDailyTpIfDate = ifSpecificDate.getGoodsDailyTpIfDate();
		scheduledDateList = ifSpecificDate.getScheduledDateList();

		// 특정날짜 제외한 도착예정일 조회
		scheduledDateList = getArrivalScheduleExcludedSpecificDate(scheduledDateList, warehouseVo, ilGoodsId, isDawnDelivery, buyQty, goodsDailyCycleType);

		// 일일배송상품일경우 인터페이스 일자는 [주문마감 전 : 금일, 이후 : 익일] 로 고정
		getDailyGoodsFixedFastestOrderIfDate(goodsDailyTpIfDate, scheduledDateList);

		return scheduledDateList;
	}

	/**
	 * 주문마감 시간 확인 후 도착일 변경 / 최초 주문IF일자 조회
	 */
	public IfSpecificDateDto getArrivalScheduleCloseTimeAndFistOrderDate(
			LocalDate goodsDailyTpIfDate
			, List<ArrivalScheduledDateDto> scheduledDateList
			, boolean isDawnDelivery
			, UrWarehouseVo warehouseVo) throws Exception {

		LocalDate nowDate = LocalDate.now();
		LocalTime nowTime = LocalTime.now();

		// 주문일자가 처음 오늘 날짜이면 주문 마감시간 체크
		if (nowDate.isEqual(scheduledDateList.get(0).getOrderDate())) {
			if (isDawnDelivery) {
				// 새벽 배송일떄
				if (nowTime.isAfter(goodsGoodsService.convertCutoffTime(warehouseVo.getDawnDeliveryCutoffTime()))) {
					scheduledDateList.remove(0);
				}
			} else {
				// 일반 배송일떄
				if (nowTime.isAfter(goodsGoodsService.convertCutoffTime(warehouseVo.getCutoffTime()))) {
					scheduledDateList.remove(0);
				}
			}

			goodsDailyTpIfDate = scheduledDateList.get(0).getOrderDate();
		}

		return IfSpecificDateDto.builder()
				.goodsDailyTpIfDate(goodsDailyTpIfDate)
				.scheduledDateList(scheduledDateList)
				.build();

	}

	public List<ArrivalScheduledDateDto> getArrivalScheduleExcludedSpecificDate(
			List<ArrivalScheduledDateDto> scheduledDateList
			, UrWarehouseVo warehouseVo
			, Long ilGoodsId
			, boolean isDawnDelivery
			, int buyQty
			, String goodsDailyCycleType) throws Exception {

		// 배송 패턴 체크
		Long psShippingPatternId = warehouseVo.getPsShippingPatternId();
		if (isDawnDelivery) {
			psShippingPatternId = warehouseVo.getDawnDeliveryPsShippingPatternId();
		}
		// 배송 패턴에 따른 재고 리스트
		scheduledDateList = goodsGoodsService.setArrivalScheduledDateDtoByShippingPattern(psShippingPatternId,
				scheduledDateList, ilGoodsId, buyQty);

		if (StringUtil.isNotEmpty(goodsDailyCycleType)) {
			// 일일배송 패턴중 화요일 없는 케이스 예외 처리
			scheduledDateList = goodsGoodsService.convertScheduledDateListTuesday(scheduledDateList);

			// 일일 배송 주기 타입에 따른 도착일자 제거
			scheduledDateList = goodsGoodsService.filterScheduledDateListByGoodsDailyCycleType(goodsDailyCycleType,
					scheduledDateList);
		}

		// 출고처 휴일 제거
		if (scheduledDateList != null && scheduledDateList.size() > 0) {
			scheduledDateList = storeWarehouseBiz.rmoveHoliday(warehouseVo.getUrWarehouseId(), scheduledDateList, isDawnDelivery);
		}

		// 일별 출고 한도
		if (scheduledDateList != null && scheduledDateList.size() > 0) {
			if(warehouseVo.getLimitCnt() > 0) {
				scheduledDateList = orderOrderBiz.removeDailyDeliveryLimitCnt(warehouseVo.getUrWarehouseId(), scheduledDateList,
						warehouseVo.getLimitCnt(), false);
			}
			// 새벽배송일때
			if (isDawnDelivery && warehouseVo.getDawnLimitCnt() > 0
					&& scheduledDateList != null && scheduledDateList.size() > 0) {
				scheduledDateList = orderOrderBiz.removeDailyDeliveryLimitCnt(warehouseVo.getUrWarehouseId(), scheduledDateList,
						warehouseVo.getDawnLimitCnt(), true);
			}
		}

		// 도착완료일자 중복 제거
		scheduledDateList = goodsGoodsService.uniqueArrivalScheduledDateList(scheduledDateList);
		return scheduledDateList;
	}

	/**
	 * 기본 매장 출고처 정보 조회
	 */
	@Override
	public Long getBasicStoreWarehouseStoreId() throws Exception {
		Long storeUrWarehouseId = 0L;

		// 매장상품 출고처 조회
		try {
			storeUrWarehouseId = Long.parseLong(policyConfigBiz.getConfigValue("WAREHOUSE_STORE_ID"));
		}catch (Exception e) {
			log.error(e.getMessage());
		}

		return storeUrWarehouseId;
	}

	/**
	 * 매장 배송/픽업 도착 예정일 리스트
	 */
	@Override
	public List<ArrivalScheduledDateDto> getStoreArrivalScheduledDateDtoList(Long urWarehouseId, String urStoreId, int stock, int buyQty) throws Exception {
		if (stock < buyQty) {
			return null;
		} else {
			UrWarehouseVo warehouseVo = storeWarehouseBiz.getWarehouse(urWarehouseId);

			// 품목 체크 일자 리스트
			List<ArrivalScheduledDateDto> scheduledDateList = goodsGoodsService.getGheckRangeDateList();

			// 배송 패턴에 따른 재고 리스트
			scheduledDateList = goodsGoodsService.setStoreArrivalScheduledDateDtoByShippingPattern(
					warehouseVo.getPsShippingPatternId(), scheduledDateList, stock);

			// 출고처 휴일 제거
			if (scheduledDateList != null && scheduledDateList.size() > 0) {
				scheduledDateList = storeWarehouseBiz.updateHolidayUnDelivery(urWarehouseId, scheduledDateList, false);
			}

			// 매장 휴일 제거
			if (scheduledDateList != null && scheduledDateList.size() > 0) {
				scheduledDateList = storeBiz.rmoveUnDeliveryDate(urStoreId, scheduledDateList);
			}

			// 도착완료일자 중복 제거
			scheduledDateList = goodsGoodsService.uniqueArrivalScheduledDateList(scheduledDateList);

			return scheduledDateList;
		}
	}

	/**
	 * 일일배송 출고 예정일 리스트
	 */
	@Override
	public List<String> getDaliyForwardingScheduledDateDtoList(Long urWarehouseId, List<String> deliveryDateList) throws Exception {
		UrWarehouseVo warehouseVo = storeWarehouseBiz.getWarehouse(urWarehouseId);

		// 품목 체크 일자 리스트
		List<ArrivalScheduledDateDto> scheduledDateList = goodsGoodsService.getDaliyGheckRangeDateList(deliveryDateList);

		// 배송 패턴에 따른 출고 예정일 리스트
		scheduledDateList = goodsGoodsService.setDaliyForwardingScheduledDateDtoByShippingPattern(
				warehouseVo.getPsShippingPatternId(), scheduledDateList);

		// 출고처 휴일 제거
		if (scheduledDateList != null && scheduledDateList.size() > 0) {
			scheduledDateList = storeWarehouseBiz.rmoveHoliday(urWarehouseId, scheduledDateList, false);
		}

		// 도착완료일자 중복 제거
		scheduledDateList = goodsGoodsService.uniqueArrivalScheduledDateList(scheduledDateList);

		List<String> scheduleDelvDateList = new ArrayList<>();
		for(ArrivalScheduledDateDto arrivalScheduledDateDto : scheduledDateList) {
			LocalDate arrivalScheduledDate = arrivalScheduledDateDto.getArrivalScheduledDate();
			String scheduleDelvDate = arrivalScheduledDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			scheduleDelvDateList.add(scheduleDelvDate);
		}

		return scheduleDelvDateList;
	}

	/**
	 * 일일상품 택배배송 도착 예정일 리스트
	 */
	@Override
	public List<ArrivalScheduledDateDto> getDailyDeliveryArrivalScheduledDateDtoList(Long urWarehouseId, Long ilGoodsId, int buyQty) throws Exception {
		UrWarehouseVo warehouseVo = storeWarehouseBiz.getWarehouse(urWarehouseId);
		LocalDate nowDate = LocalDate.now();
		LocalTime nowTime = LocalTime.now();

		// 품목 체크 일자 리스트
		List<ArrivalScheduledDateDto> scheduledDateList = goodsGoodsService.getGheckRangeDateList();

		boolean isStoreDelivery = "Y".equals(warehouseVo.getStoreYn());

		// 주문일자가 처음 오늘 날짜이면 주문 마감시간 체크
		if (nowDate.isEqual(scheduledDateList.get(0).getOrderDate())) {
			// 매장 택배 배송일때
			if (isStoreDelivery && warehouseVo.getStoreCutoffTime() != null) {
				if (nowTime.isAfter(goodsGoodsService.convertCutoffTime(warehouseVo.getStoreCutoffTime()))) {
					scheduledDateList.remove(0);
				}
			} else {
				// 일일배송 패턴으로 배송일떄
				if (nowTime.isAfter(goodsGoodsService.convertCutoffTime(warehouseVo.getCutoffTime()))) {
					scheduledDateList.remove(0);
				}
			}
		}

		// 배송 패턴 체크
		Long psShippingPatternId = warehouseVo.getStoreShippingPatternId();
		if (psShippingPatternId == null) {
			psShippingPatternId = warehouseVo.getPsShippingPatternId();
		}
		// 배송 패턴에 따른 재고 리스트
		scheduledDateList = goodsGoodsService.setArrivalScheduledDateDtoByShippingPattern(psShippingPatternId, scheduledDateList, ilGoodsId, buyQty);

		// 출고처 휴일 제거
		if (scheduledDateList != null && scheduledDateList.size() > 0) {
			scheduledDateList = storeWarehouseBiz.rmoveHoliday(urWarehouseId, scheduledDateList, false);
		}

		// 일별 출고 한도
		if (scheduledDateList != null && scheduledDateList.size() > 0) {
			int limitCnt = warehouseVo.getStoreLimitCnt();
			if (limitCnt == 0) {
				limitCnt = warehouseVo.getLimitCnt();
			}
			if (limitCnt > 0) {
				scheduledDateList = orderOrderBiz.removeDailyDeliveryLimitCnt(urWarehouseId, scheduledDateList,
						warehouseVo.getLimitCnt(), false);
			}
		}

		// 도착완료일자 중복 제거
		scheduledDateList = goodsGoodsService.uniqueArrivalScheduledDateList(scheduledDateList);

		return scheduledDateList;
	}

	@Override
	public List<ArrivalScheduledDateDto> getMinStockArrivalScheduledDateDtoList(List<ArrivalScheduledDateDto> list1,
			List<ArrivalScheduledDateDto> list2) throws Exception {
		return goodsGoodsService.getMinStockArrivalScheduledDateDtoList(list1, list2);
	}

	@Override
	public List<LocalDate> intersectionArrivalScheduledDateList(List<List<LocalDate>> goodsArrivalScheduledDateList)
			throws Exception {
		return goodsGoodsService.intersectionArrivalScheduledDateList(goodsArrivalScheduledDateList);
	}

	/**
	 * 상품 전시타입 정보 조회
	 */
	@Override
	public HashMap<String, String> getGoodsDisplayInfo(GoodsRequestDto goodsRequestDto) throws Exception {
		return goodsGoodsService.getGoodsDisplayInfo(goodsRequestDto);
	}

	@Override
	public OrderSelectGoodsVo getOrderGoodsInfo(Long ilGoodsId) throws Exception {
		return goodsGoodsService.getOrderGoodsInfo(ilGoodsId);
	}

	@Override
	public MallGoodsDetailDto getMallGoodsDetail(Long ilGoodsId) throws Exception {
		MallGoodsDetailDto resDto = new MallGoodsDetailDto();
		List<GoodsNutritionListResultVo> goodsNutritionList = new ArrayList<>();
		List<PackageGoodsListDto> goodsPackageList = new ArrayList<>();

		// 상품 상세 정보
		DetailSelectGoodsVo goodsDetail = getDetailSelectGoods(ilGoodsId);
		List<GoodsSpecListResultVo> goodsSpecList = getGoodsSpecList(ilGoodsId);

		resDto.setGoodsType(goodsDetail.getGoodsTp());
		resDto.setBasicDescription(goodsDetail.getBasicDescription());
		resDto.setDetailDescription(goodsDetail.getDetailDescription());
		resDto.setSpec(goodsSpecList);
		resDto.setNutritionDispYn(goodsDetail.getNutritionDisplayYn());

		// 영양정보
		if (goodsDetail.getNutritionDisplayYn().equals("Y")) {
			goodsNutritionList = getGoodsNutritionList(goodsDetail.getIlItemCode());
		}

		resDto.setNutrition(goodsNutritionList);
		resDto.setNutritionQtyPerOnce(goodsDetail.getNutritionQtyPerOnce());
		resDto.setNutritionQtyTotal(goodsDetail.getNutritionQtyTotal());
		resDto.setNutritionEtc(goodsDetail.getNutritionEtc());
		resDto.setNutritionDispDefault(goodsDetail.getNutritionDisplayDefault());
		resDto.setGoodsPackageBasicDescriptionYn(goodsDetail.getGoodsPackageBasicDescYn());
		resDto.setGoodsPackageBasicDescription(goodsDetail.getGoodsPackageBasicDesc());

		// 묶음상품일 경우
		if (goodsDetail.getGoodsTp().equals(GoodsEnums.GoodsType.PACKAGE.getCode())) {

			List<PackageGoodsResultVo> packageList = goodsGoodsService.getPackageGoodsList(ilGoodsId);
			for (PackageGoodsResultVo goodsPackageVo : packageList) {
				PackageGoodsListDto packageGoodsDto = new PackageGoodsListDto();
				List<GoodsNutritionListResultVo> packageGoodsNutritionList = new ArrayList<>();

				Long packageGoodsId = Long.valueOf(goodsPackageVo.getIlGoodsId());

				// 상품 상세 정보
				DetailSelectGoodsVo packageGoodsDetail = getDetailSelectGoods(packageGoodsId);
				List<GoodsSpecListResultVo> packageGoodsSpecList = getGoodsSpecList(packageGoodsId);

				packageGoodsDto.setIlGoodsId(packageGoodsId);
				packageGoodsDto.setGoodsName(packageGoodsDetail.getGoodsName());
				packageGoodsDto.setBasicDescription(packageGoodsDetail.getBasicDescription());
				packageGoodsDto.setDetailDescription(packageGoodsDetail.getDetailDescription());
				packageGoodsDto.setSpec(packageGoodsSpecList);
				packageGoodsDto.setNutritionDispYn(packageGoodsDetail.getNutritionDisplayYn());

				// 영양정보
				if (packageGoodsDetail.getNutritionDisplayYn().equals("Y")) {
					packageGoodsNutritionList = getGoodsNutritionList(packageGoodsDetail.getIlItemCode());
				}

				packageGoodsDto.setNutrition(packageGoodsNutritionList);
				packageGoodsDto.setNutritionQtyPerOnce(packageGoodsDetail.getNutritionQtyPerOnce());
				packageGoodsDto.setNutritionQtyTotal(packageGoodsDetail.getNutritionQtyTotal());
				packageGoodsDto.setNutritionEtc(packageGoodsDetail.getNutritionEtc());
				packageGoodsDto.setNutritionDispDefault(packageGoodsDetail.getNutritionDisplayDefault());

				goodsPackageList.add(packageGoodsDto);
			}
		}

		resDto.setGoodsPackageList(goodsPackageList);

		return resDto;
	}

	@Override
	public LocalDate getNextArrivalScheduledDate(List<LocalDate> list, LocalDate date) throws Exception {
		return list.stream().filter(arrivalScheduledDate -> date.isBefore(arrivalScheduledDate)).findAny().orElse(null);
	}

	@Override
	public List<ArrivalScheduledDateDto> returnListByStoreAreaInfo(ShippingPossibilityStoreDeliveryAreaDto areaDto,
			List<ArrivalScheduledDateDto> list) throws Exception {
		return goodsGoodsService.returnListByStoreAreaInfo(areaDto, list);
	}

	@Override
	public List<ArrivalScheduledDateDto> getArrivalScheduledDateDtoListByWeekCode(List<ArrivalScheduledDateDto> list, Long urWarehouseId, String GoodsDailyCycleType, String weekCode) throws Exception {
		List<String> weekTypes = new ArrayList<String>(GoodsCycleType.findByCode(GoodsDailyCycleType).getWeekCodeList());
		if (GoodsCycleType.DAY1_PER_WEEK.getCode().equals(GoodsDailyCycleType)) {
			weekTypes.add(weekCode);
		}
		if(!weekTypes.isEmpty()) {
			list = goodsGoodsService.getArrivalScheduledDateDtoByWeekCode(list, weekTypes);
		}
		if (GoodsCycleType.DAY1_PER_WEEK.getCode().equals(GoodsDailyCycleType)) {

			if(CollectionUtils.isEmpty(list)){
				// 현재날짜 기준 +7일 도착예정일 list 추출
				List<ArrivalScheduledDateDto> scheduledDateDtoList = goodsGoodsService.getGheckRangeDateList().stream().limit(7).collect(Collectors.toList());

				// 해당 패턴,요일에 맞는 도착예정일 list 조회
				list = goodsGoodsService.getArrivalScheduledDateDtoByShippingPatternAndWeekCode(scheduledDateDtoList, urWarehouseId, weekCode);
			}

			list = goodsGoodsService.getArrivalScheduledDateDtoAddWeek(list,10);

			if (list != null && !list.isEmpty()) {
				list = storeWarehouseBiz.rmoveHoliday(urWarehouseId, list, false);

				// FO 노출되는 날짜 5일 고정
				if(list.size() > 5){
					list = list.stream().limit(5).collect(Collectors.toList());
				}
			}

		}
		return list;
	}

	@Override
	public ArrivalScheduledDateDto getArrivalScheduledDateDtoByIlGoodsIdArrivalScheduledDate(long ilGoodsId, LocalDate arrivalScheduledDate) throws Exception{
		return goodsGoodsService.getArrivalScheduledDateDtoByIlGoodsIdArrivalScheduledDate(ilGoodsId, arrivalScheduledDate);
	}

	@Override
	public void convertRegularArrivalScheduledDate(BasicSelectGoodsVo vo) throws Exception {
		goodsGoodsService.convertRegularArrivalScheduledDate(vo);
	}

	@Override
	public List<OrderDetlVo> getOutmallGoodsPackList(long ilGoodsId) throws Exception {
		return goodsGoodsService.getOutmallGoodsPackList(ilGoodsId);
	}

	@Override
	public int getGoodsEmployeeDiscountRatio(Long ilGoodsId) throws Exception {
		return goodsGoodsService.getGoodsEmployeeDiscountRatio(ilGoodsId);
	}

	@Override
	public List<GoodsPackageEmployeeDiscountDto> getPackageGoodsEmployeeDiscountRatio(Long ilGoodsId) throws Exception {
		return goodsGoodsService.getPackageGoodsEmployeeDiscountRatio(ilGoodsId);
	}

	@Override
	public GoodsPackageEmployeeDiscountDto findGoodsPackageEmployeeDiscountDto(List<GoodsPackageEmployeeDiscountDto> list, Long ilGoodsId) throws Exception {
		return list.stream().filter(dto -> ilGoodsId.equals(dto.getIlGoodsId())).findAny().orElse(null);
	}

	@Override
	public String getSaleStatus(String saleStatus, int stock) throws Exception {
		return goodsGoodsService.getSaleStatus(saleStatus, stock);
	}

	@Override
	public List<ItemStoreInfoVo> getItemStoreInfoList(String urStoreId, List<String> ilItemCds) throws Exception {
		return goodsGoodsService.getItemStoreInfoList(urStoreId, ilItemCds);
	}

	@Override
	public StorePriceDto getStoreSalePrice(String mallDiscountType, int mallRecommendedPrice, int mallSalePrice, int storeSalePrice) throws Exception {
		StorePriceDto resDto = new StorePriceDto();
		if(GoodsDiscountType.PRIORITY.getCode().equals(mallDiscountType)) {
			resDto.setDiscountType(mallDiscountType);
			resDto.setSalePrice(mallSalePrice);
		} else {
			// 매장 판매가가 정가보다 높은 경우 매장 판매가 = 정가로 변경
			if (mallRecommendedPrice < storeSalePrice) {
				storeSalePrice = mallRecommendedPrice;
			}

			if(mallRecommendedPrice > storeSalePrice) {
				resDto.setDiscountType(GoodsEnums.GoodsDiscountType.O2O_SHOP.getCode());
			} else {
				resDto.setDiscountType(GoodsEnums.GoodsDiscountType.NONE.getCode());
			}
			resDto.setSalePrice(storeSalePrice);
		}

		resDto.setDiscountTypeName(GoodsDiscountType.findByCode(resDto.getDiscountType()).getCodeName());

		return resDto;
	}

	@Override
	public ItemStoreInfoVo getItemStoreInfo(String urStoreId, String ilItemCd) throws Exception {
		List<ItemStoreInfoVo> list = goodsGoodsService.getItemStoreInfoList(urStoreId, Arrays.asList(ilItemCd));
		if (CollectionUtils.isNotEmpty(list)) {
			return list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 선물 가능여부
	 */
	@Override
	public boolean isPresentPossible(List<ArrivalScheduledDateDto> list) throws Exception {
		return goodsGoodsService.isPresentPossible(list);
	}

	/**
	 * 중복 품목 재고 처리
	 */
	@Override
	public void convertOverlapBuyItem(HashMap<String, Integer> overlapBuyItem, String ilItemCode, int buyQty, boolean isDawnDelivery, List<ArrivalScheduledDateDto> arrivalScheduledDateDtoList) {
		goodsGoodsService.convertOverlapBuyItem(overlapBuyItem, ilItemCode, buyQty, isDawnDelivery, arrivalScheduledDateDtoList);
	}

	/**
	 * 식단 정보
	 */
	@Override
	public GoodsPageInfoMealDto getGoodsPageInfoMeal(Long ilGoodsId, List<ArrivalScheduledDateDto> goodsArrivalScheduledDateDtoList) throws Exception {

		GoodsPageInfoMealDto resulte = goodsGoodsService.getGoodsPageInfoMeal(ilGoodsId);
		if (resulte != null && goodsArrivalScheduledDateDtoList != null
				&& !goodsArrivalScheduledDateDtoList.isEmpty()) {
			resulte.setDeliveryDateList(
					goodsGoodsService.getMealPossibleDeliveryDateList(ilGoodsId, goodsArrivalScheduledDateDtoList.stream()
							.map(ArrivalScheduledDateDto::getArrivalScheduledDate).collect(Collectors.toList())));
		}
		return resulte;
	}

	/**
	 * 식단 스케줄 조회
	 */
	@Override
	public List<GoodsDailyMealScheduleDto> getGoodsDailyMealSchedule(GoodsDailyMealScheduleRequestDto reqDto) throws Exception {
		return goodsGoodsService.getGoodsDailyMealSchedule(reqDto);
	}

	/**
	 * 식단 컨텐츠 상세 조회
	 */
	@Override
	public GoodsDailyMealContentsDto getGoodsDailyMealContents(String ilGoodsDailyMealContsCd) throws Exception {
		return goodsGoodsService.getGoodsDailyMealContents(ilGoodsDailyMealContsCd);
	}

	@Override
	public List<ArrivalScheduledDateDto> getArrivalScheduledDateDtoByShippingPatternAndWeekCode(List<ArrivalScheduledDateDto> list, Long urWarehouseId, String weekCode) throws Exception {
		return goodsGoodsService.getArrivalScheduledDateDtoByShippingPatternAndWeekCode(list, urWarehouseId, weekCode);
	}
}
