package kr.co.pulmuone.v1.store.warehouse.service;

import java.time.LocalDate;
import java.util.List;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.WarehouseEnums.SetlYn;
import kr.co.pulmuone.v1.goods.goods.dto.ArrivalScheduledDateDto;
import kr.co.pulmuone.v1.store.delivery.dto.WarehouseUnDeliveryableInfoDto;
import kr.co.pulmuone.v1.store.warehouse.service.dto.vo.UrWarehouseVo;

public interface StoreWarehouseBiz {

	List<ArrivalScheduledDateDto> rmoveHoliday(Long urWarehouseId, List<ArrivalScheduledDateDto> scheduledDateList, boolean isDawnDelivery) throws Exception;

	List<ArrivalScheduledDateDto> updateHolidayUnDelivery(Long urWarehouseId, List<ArrivalScheduledDateDto> scheduledDateList, boolean isDawnDelivery) throws Exception;

	UrWarehouseVo getWarehouse(Long urWarehouseId) throws Exception;

	ApiResult<?> checkOverWarehouseCutoffTime(Long urWarehouseId, LocalDate forwardingScheduledDate, Boolean isDawnDelivery) throws Exception;

	SetlYn getWarehouseSetlYn(Long urWarehouseId) throws Exception;

	WarehouseUnDeliveryableInfoDto getUnDeliverableInfo(Long urWarehouseId, String zipCode, boolean isDawnDelivery) throws Exception;
}