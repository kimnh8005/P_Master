package kr.co.pulmuone.v1.comm.mapper.user.warehouse;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.user.warehouse.dto.SaveWarehouseHolidayRequestDto;
import kr.co.pulmuone.v1.user.warehouse.dto.WarehouseHolidayRequestDto;
import kr.co.pulmuone.v1.user.warehouse.dto.vo.WarehouseHolidayResultVo;
import kr.co.pulmuone.v1.user.warehouse.dto.vo.WarehouseResultVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WarehouseHolidayMapper {

	Page<WarehouseHolidayResultVo> getWarehouseHolidayList(WarehouseHolidayRequestDto warehouseHolidayRequestDto) throws Exception;

	Page<WarehouseHolidayResultVo> getScheduleWarehouseHolidayList(WarehouseHolidayRequestDto warehouseHolidayRequestDto) throws Exception;

	Page<WarehouseHolidayResultVo> getWarehouseSetList(WarehouseHolidayRequestDto warehouseHolidayRequestDto) throws Exception;

	Page<WarehouseHolidayResultVo> getWarehouseHolidayDetail(WarehouseHolidayRequestDto warehouseHolidayRequestDto) throws Exception;

	Page<WarehouseHolidayResultVo> getConfirmWarehouseHolidayList(WarehouseHolidayRequestDto warehouseHolidayRequestDto) throws Exception;

	List<WarehouseHolidayResultVo> getOldConfirmWarehouseHolidayList(WarehouseHolidayRequestDto warehouseHolidayRequestDto) throws Exception;


	int addWarehouseHoliday(SaveWarehouseHolidayRequestDto saveWarehouseHolidayRequestDto);

	int delWarehouseHoliday(WarehouseHolidayResultVo  warehouseHolidayResultVo);

	WarehouseResultVo getHolidayWarehouseInfo(WarehouseHolidayRequestDto warehouseHolidayRequestDto) throws Exception;

	Page<WarehouseHolidayResultVo> getWarehouseHolidayListById(WarehouseHolidayRequestDto warehouseHolidayRequestDto) throws Exception;

	int getDuplicateHldy(WarehouseHolidayResultVo  warehouseHolidayResultVo);
}
