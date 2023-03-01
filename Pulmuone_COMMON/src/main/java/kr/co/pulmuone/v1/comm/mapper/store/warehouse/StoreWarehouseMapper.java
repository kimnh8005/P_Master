package kr.co.pulmuone.v1.comm.mapper.store.warehouse;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.pulmuone.v1.goods.goods.dto.ArrivalScheduledDateDto;
import kr.co.pulmuone.v1.store.warehouse.service.dto.vo.UrWarehouseVo;

@Mapper
public interface StoreWarehouseMapper {

	List<LocalDate> getCheckWarehouseHolidayList(@Param("urWarehouseId") Long urWarehouseId,
			@Param("scheduledDateList") List<ArrivalScheduledDateDto> scheduledDateList,@Param("isDawnDelivery") boolean isDawnDelivery) throws Exception;

	UrWarehouseVo getWarehouse(Long urWarehouseId) throws Exception;
}
