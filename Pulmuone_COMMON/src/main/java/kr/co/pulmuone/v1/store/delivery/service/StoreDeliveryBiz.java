package kr.co.pulmuone.v1.store.delivery.service;

import java.time.LocalDate;
import java.util.List;

import kr.co.pulmuone.v1.goods.goods.dto.ArrivalScheduledDateDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartStoreShippingDateDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartStoreShippingDateScheduleDto;
import kr.co.pulmuone.v1.store.delivery.dto.ShippingAddressPossibleDeliveryInfoDto;
import kr.co.pulmuone.v1.store.delivery.dto.ShippingPossibilityStoreDeliveryAreaDto;
import kr.co.pulmuone.v1.store.delivery.dto.StoreDeliveryScheduleDto;

public interface StoreDeliveryBiz {

	ShippingPossibilityStoreDeliveryAreaDto getShippingPossibilityStoreDeliveryAreaInfo(String storeType, List<String> storeDeliveralbeItemTypeBySupplierIdList,
			String receiverZipCode, String buildingCode) throws Exception;

	ShippingPossibilityStoreDeliveryAreaDto getShippingPossibilityStoreDeliveryAreaInfo(String storeType, List<String> storeDeliveralbeItemTypeBySupplierIdList,
			String receiverZipCode, String buildingCode, String storeDeliveryType) throws Exception;

	ShippingPossibilityStoreDeliveryAreaDto getShippingPossibilityStoreDeliveryAreaInfo(Long ilGoodsId, String receiverZipCode, String buildingCode) throws Exception;

	ShippingAddressPossibleDeliveryInfoDto getShippingAddressPossibleDeliveryInfo(String zipCode, String buildingCode) throws Exception;

	ShippingPossibilityStoreDeliveryAreaDto getShippingPossibilityStoreDeliveryAreaInfoByDailySupplierId(Long urSupplierId, String receiverZipCode, String buildingCode) throws Exception;

	ShippingPossibilityStoreDeliveryAreaDto getStoreShippingPossibilityStoreDeliveryAreaInfo(String receiverZipCode, String buildingCode) throws Exception;

	ShippingPossibilityStoreDeliveryAreaDto getStoreShippingPossibilityStoreDeliveryAreaInfo(String receiverZipCode, String buildingCode, String storeDeliveryType) throws Exception;

	boolean isDawnDeliveryArea(String zipCode, String buildingCode) throws Exception;

	List<CartStoreShippingDateScheduleDto> getStoreSchedule(String urStoreId, String urDeliveryAreaId, LocalDate arrivalScheduledDate) throws Exception;

	CartStoreShippingDateDto convertCartStoreShippingDateDto(ArrivalScheduledDateDto dateDto, ShippingPossibilityStoreDeliveryAreaDto storeDeliveryAreaInfo) throws Exception;

	StoreDeliveryScheduleDto getStoreScheduleByUrStoreScheduleId(Long urStoreScheduleId, LocalDate arrivalScheduledDate) throws Exception;

	ShippingPossibilityStoreDeliveryAreaDto getUrStoreDeliveryAreaId(String urStoreId, String storeDeliveryType, String storeDeliverableItemType) throws Exception;

	boolean isPossibleSelectStoreSchedule(List<CartStoreShippingDateScheduleDto> list) throws Exception;
}