package kr.co.pulmuone.v1.user.warehouse.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.user.warehouse.dto.SaveWarehouseHolidayRequestDto;
import kr.co.pulmuone.v1.user.warehouse.dto.WarehouseHolidayRequestDto;

public interface WarehouseHolidayBiz {

	/**
	 * 출고처 휴일 설정 목록 조회
	 */
	ApiResult<?> getWarehouseHolidayList(WarehouseHolidayRequestDto warehouseHolidayRequestDto) throws Exception;


	/**
	 * 출고처 설정 대상 목록
	 */
	ApiResult<?> getWarehouseSetList(WarehouseHolidayRequestDto warehouseHolidayRequestDto) throws Exception;

	/**
	 * 출고처 설정 대상 수정 목록
	 */
	ApiResult<?> getWarehouseHolidayDetail(WarehouseHolidayRequestDto warehouseHolidayRequestDto) throws Exception;


	/**
	 * 출고처 설정 확정 목록
	 */
	ApiResult<?> getConfirmWarehouseHolidayList(WarehouseHolidayRequestDto warehouseHolidayRequestDto) throws Exception;

	/**
	 * 출고처 휴일설정 등록
	 */
	ApiResult<?> addWarehouseHoliday(SaveWarehouseHolidayRequestDto saveWarehouseHolidayRequestDto) throws Exception;

	/**
	 * 출고처 휴일설정 수정
	 */
	ApiResult<?> putWarehouseHoliday(SaveWarehouseHolidayRequestDto saveWarehouseHolidayRequestDto) throws Exception;

	/**
	 * Scheduler 출고처 설정 목록
	 */
	ApiResult<?> getScheduleWarehouseHolidayList(WarehouseHolidayRequestDto warehouseHolidayRequestDto) throws Exception;


	/**
	 * Scheduler 출고처 휴일정보 조회
	 */
	ApiResult<?> getHolidayWarehouseInfo(WarehouseHolidayRequestDto warehouseHolidayRequestDto) throws Exception;

	/**
	 * 출고처 휴일리스트 조회
	 */
	ApiResult<?> getWarehouseHolidayListById(WarehouseHolidayRequestDto warehouseHolidayRequestDto) throws Exception;


}
