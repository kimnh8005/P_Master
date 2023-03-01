package kr.co.pulmuone.v1.comm.mapper.store.delivery;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.pulmuone.v1.shopping.cart.dto.CartStoreShippingDateScheduleDto;
import kr.co.pulmuone.v1.store.delivery.dto.ShippingPossibilityStoreDeliveryAreaDto;
import kr.co.pulmuone.v1.store.delivery.dto.StoreDeliveryScheduleDto;

@Mapper
public interface StoreDeliveryMapper {

	ShippingPossibilityStoreDeliveryAreaDto getShippingPossibilityStoreDeliveryAreaInfo(@Param("storeType") String storeType, @Param("storeDeliveralbeItemTypeBySupplierIdList") List<String> storeDeliveralbeItemTypeBySupplierIdList, @Param("receiverZipCode") String receiverZipCode, @Param("buildingCode") String buildingCode, @Param("storeDeliveryType") String storeDeliveryType) throws Exception;

	boolean isDawnDeliveryArea(@Param("zipCode") String zipCode, @Param("buildingCode") String buildingCode) throws Exception;

	List<String> getStoreDeliveryArea(@Param("zipCode") String zipCode, @Param("buildingCode") String buildingCode) throws Exception;

	List<String> getDailyDeliveryArea(@Param("zipCode") String zipCode, @Param("buildingCode") String buildingCode) throws Exception;

	List<CartStoreShippingDateScheduleDto> getStoreSchedule(@Param("urStoreId") String urStoreId, @Param("urDeliveryAreaId") String urDeliveryAreaId, @Param("arrivalScheduledDate") LocalDate arrivalScheduledDate) throws Exception;

	StoreDeliveryScheduleDto getStoreScheduleByUrStoreScheduleId(@Param("urStoreScheduleId") Long urStoreScheduleId, @Param("arrivalScheduledDate") LocalDate arrivalScheduledDate) throws Exception;

	ShippingPossibilityStoreDeliveryAreaDto getUrStoreDeliveryAreaId(@Param("urStoreId") String urStoreId, @Param("storeDeliveryType") String storeDeliveryType, @Param("storeDeliverableItemType") String storeDeliverableItemType) throws Exception;
}
