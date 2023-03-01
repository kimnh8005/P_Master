package kr.co.pulmuone.v1.store.delivery.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.goods.goods.dto.ArrivalScheduledDateDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.DetailSelectGoodsVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ShippingAreaVo;
import kr.co.pulmuone.v1.goods.goods.service.GoodsGoodsBiz;
import kr.co.pulmuone.v1.goods.goods.service.GoodsShippingTemplateBiz;
import kr.co.pulmuone.v1.shopping.cart.dto.CartStoreShippingDateDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartStoreShippingDateScheduleDto;
import kr.co.pulmuone.v1.store.delivery.dto.ShippingAddressPossibleDeliveryInfoDto;
import kr.co.pulmuone.v1.store.delivery.dto.ShippingPossibilityStoreDeliveryAreaDto;
import kr.co.pulmuone.v1.store.delivery.dto.StoreDeliveryScheduleDto;

@Service
public class StoreDeliveryBizImpl implements StoreDeliveryBiz {

	@Autowired
	private GoodsGoodsBiz goodsGoodsBiz;

	@Autowired
	private GoodsShippingTemplateBiz goodsShippingTemplateBiz;

    @Autowired
    private StoreDeliveryService storeDeliveryService;

    /**
     * 스토어 배송권역 정보 조회
     *
     * @param storeType, storeDeliveralbeItemType, receiverZipCode, buildingCode
     * @return ShippingPossibilityStoreDeliveryAreaDto
     * @throws Exception
     */
	@Override
	public ShippingPossibilityStoreDeliveryAreaDto getShippingPossibilityStoreDeliveryAreaInfo(String storeType, List<String> storeDeliveralbeItemType, String receiverZipCode, String buildingCode) throws Exception {

		return shippingPossibilityStoreDeliveryAreaInfo(storeType, storeDeliveralbeItemType, receiverZipCode, buildingCode);
	}

	@Override
	public ShippingPossibilityStoreDeliveryAreaDto getShippingPossibilityStoreDeliveryAreaInfo(String storeType,
			List<String> storeDeliveralbeItemType, String receiverZipCode, String buildingCode, String storeDeliveryType) throws Exception {

		return storeDeliveryService.getShippingPossibilityStoreDeliveryAreaInfo(storeType,
				storeDeliveralbeItemType, receiverZipCode, buildingCode, storeDeliveryType);

	}

    /**
     * 상품의 스토어 배송권역 정보 조회
     */
    @Override
    public ShippingPossibilityStoreDeliveryAreaDto getShippingPossibilityStoreDeliveryAreaInfo(Long ilGoodsId, String receiverZipCode, String buildingCode) throws Exception {

    	DetailSelectGoodsVo goods = goodsGoodsBiz.getDetailSelectGoods(ilGoodsId);

        // 판매상태에 따른 스토어 타입
        String storeType = goodsGoodsBiz.getStoreTypeBySaleType(goods.getSaleType());

        // 공급처ID에 따른 배송가능 품목유형
        List<String> storeDeliveralbeItemTypeBySupplierIdList = goodsGoodsBiz.getStoreDeliverableItemTypeBySupplierId(goods.getUrSupplierId());

        // 일일상품인 경우 스토어 배송권역 정보, 세션의 우편번호/건물번호 조회하여 데이터가 있으면
        return shippingPossibilityStoreDeliveryAreaInfo(storeType, storeDeliveralbeItemTypeBySupplierIdList, receiverZipCode, buildingCode);
    }

    /**
     * get 배송 주소로 배송 가능 정보 조회
     */
    @Override
    public ShippingAddressPossibleDeliveryInfoDto getShippingAddressPossibleDeliveryInfo(String zipCode, String buildingCode) throws Exception {
        return storeDeliveryService.getShippingPossibilityDeliveryArea(zipCode, buildingCode);
    }

    /**
     * 일일배송 상품의 스토어 배송권역 정보 조회
     */
    @Override
    public ShippingPossibilityStoreDeliveryAreaDto getShippingPossibilityStoreDeliveryAreaInfoByDailySupplierId(Long urSupplierId, String receiverZipCode, String buildingCode) throws Exception {

        // 판매상태에 따른 스토어 타입
        String storeType = goodsGoodsBiz.getStoreTypeBySaleType(GoodsEnums.SaleType.DAILY.getCode());

        // 공급처ID에 따른 배송가능 품목유형
        List<String> storeDeliveralbeItemTypeBySupplierIdList = goodsGoodsBiz.getStoreDeliverableItemTypeBySupplierId(urSupplierId);

        // 일일상품인 경우 스토어 배송권역 정보, 세션의 우편번호/건물번호 조회하여 데이터가 있으면
		return shippingPossibilityStoreDeliveryAreaInfo(storeType, storeDeliveralbeItemTypeBySupplierIdList, receiverZipCode, buildingCode);
    }

    /**
     * 매장배송 스토어 배송 권역 정보 조회
     */
    @Override
    public ShippingPossibilityStoreDeliveryAreaDto getStoreShippingPossibilityStoreDeliveryAreaInfo(String receiverZipCode, String buildingCode) throws Exception {

        // 판매상태에 따른 스토어 타입
        String storeType = goodsGoodsBiz.getStoreTypeBySaleType(GoodsEnums.SaleType.SHOP.getCode());

        // 공급처ID에 따른 배송가능 품목유형
//        List<String> storeDeliveralbeItemTypeBySupplierIdList = goodsGoodsBiz.getStoreDeliverableItemTypeBySupplierId(urSupplierId);

		return storeDeliveryService.getShippingPossibilityStoreDeliveryAreaInfo(storeType, new ArrayList<>(), receiverZipCode, buildingCode);
    }

    @Override
    public ShippingPossibilityStoreDeliveryAreaDto getStoreShippingPossibilityStoreDeliveryAreaInfo(String receiverZipCode, String buildingCode, String storeDeliveryType) throws Exception {
        // 판매상태에 따른 스토어 타입
        String storeType = goodsGoodsBiz.getStoreTypeBySaleType(GoodsEnums.SaleType.SHOP.getCode());

        // 공급처ID에 따른 배송가능 품목유형
//        List<String> storeDeliveralbeItemTypeBySupplierIdList = goodsGoodsBiz.getStoreDeliverableItemTypeBySupplierId(urSupplierId);

		return storeDeliveryService.getShippingPossibilityStoreDeliveryAreaInfo(storeType, new ArrayList<>(), receiverZipCode, buildingCode, storeDeliveryType);
    }

    /**
     * 새벽배송 권역 여부
     */
	@Override
	public boolean isDawnDeliveryArea(String zipCode, String buildingCode) throws Exception {
		return storeDeliveryService.isDawnDeliveryArea(zipCode, buildingCode);
	}

	@Override
	public List<CartStoreShippingDateScheduleDto> getStoreSchedule(String urStoreId, String urDeliveryAreaId, LocalDate arrivalScheduledDate) throws Exception {
		return storeDeliveryService.getStoreSchedule(urStoreId, urDeliveryAreaId, arrivalScheduledDate);
	}

	@Override
	public CartStoreShippingDateDto convertCartStoreShippingDateDto(ArrivalScheduledDateDto dateDto, ShippingPossibilityStoreDeliveryAreaDto storeDeliveryAreaInfo) throws Exception {
		CartStoreShippingDateDto storeShippingDateDto = new CartStoreShippingDateDto();
		storeShippingDateDto.setArrivalScheduledDate(dateDto.getArrivalScheduledDate());
		storeShippingDateDto.setSchedule(getStoreSchedule(storeDeliveryAreaInfo.getUrStoreId(), storeDeliveryAreaInfo.getUrDeliveryAreaId(), dateDto.getArrivalScheduledDate()));
		if (dateDto.isUnDelivery()) {
			for (CartStoreShippingDateScheduleDto scheduleDto : storeShippingDateDto.getSchedule()) {
				scheduleDto.setPossibleSelect(false);
			}
		}
		return storeShippingDateDto;
	}

	@Override
	public StoreDeliveryScheduleDto getStoreScheduleByUrStoreScheduleId(Long urStoreScheduleId, LocalDate arrivalScheduledDate) throws Exception {
		return storeDeliveryService.getStoreScheduleByUrStoreScheduleId(urStoreScheduleId, arrivalScheduledDate);
	}

	@Override
    public ShippingPossibilityStoreDeliveryAreaDto getUrStoreDeliveryAreaId(String urStoreId, String storeDeliveryType, String storeDeliverableItemType) throws Exception{
	    return storeDeliveryService.getUrStoreDeliveryAreaId(urStoreId, storeDeliveryType, storeDeliverableItemType);
    }

	@Override
	public boolean isPossibleSelectStoreSchedule(List<CartStoreShippingDateScheduleDto> list) throws Exception {
		return storeDeliveryService.isPossibleSelectStoreSchedule(list);
	}

	/**
	 * 권역 정보 일일 배송 권역 , 제주/도서산간 지역 외 택배지역 처리 로직
	 *
	 * @param storeType
	 * @param storeDeliveralbeItemTypeBySupplierIdList
	 * @param receiverZipCode
	 * @param buildingCode
	 * @return
	 * @throws Exception
	 */
	private ShippingPossibilityStoreDeliveryAreaDto shippingPossibilityStoreDeliveryAreaInfo(String storeType, List<String> storeDeliveralbeItemTypeBySupplierIdList,String receiverZipCode, String buildingCode) throws Exception {

		ShippingPossibilityStoreDeliveryAreaDto result = storeDeliveryService.getShippingPossibilityStoreDeliveryAreaInfo(storeType, storeDeliveralbeItemTypeBySupplierIdList, receiverZipCode, buildingCode);
		// DM 조회 일때만
		if (result == null && storeDeliveralbeItemTypeBySupplierIdList.contains(GoodsEnums.StoreDeliverableItem.DM.getCode())) {
			// 일일배송 권역이 아니면 도서산간 지역 체크
			if (receiverZipCode != null) {
				ShippingAreaVo shippingAreaVo = goodsShippingTemplateBiz.getShippingArea(receiverZipCode);
				if (shippingAreaVo == null || (!"Y".equals(shippingAreaVo.getIslandYn()) && !"Y".equals(shippingAreaVo.getJejuYn()))) {
					// 도서 산간 지역이 아닐 경우 택배 권역 처리
					result = storeDeliveryService.makeStoreDeliveryIntervalParcelInfo();
				}
			}
		}

		return result;
	}
}