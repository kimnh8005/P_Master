package kr.co.pulmuone.v1.user.warehouse.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.user.warehouse.dto.WarehouseModifyDto;
import kr.co.pulmuone.v1.user.warehouse.dto.WarehouseRequestDto;
import kr.co.pulmuone.v1.user.warehouse.dto.WarehouseTemplateRequestDto;

import java.util.List;

public interface WarehouseBiz {


	/**
	 * 출고처 목록 조회
	 */
	ApiResult<?> getWarehouseList(WarehouseRequestDto warehouseRequestDto) throws Exception;

	/**
	 * 출고처 상세조회
	 */
	ApiResult<?> getWarehouse(WarehouseRequestDto warehouseRequestDto) throws Exception;

	/**
	 * 출고처 정보 등록
	 */
	ApiResult<?> addWarehouse(WarehouseModifyDto warehouseModifyDto) throws Exception;


	/**
	 * 출고처 정보 수정
	 */
	ApiResult<?> putWarehouse(WarehouseModifyDto warehouseModifyDto) throws Exception;

	/**
	 * 배송정책  정보 조회
	 * @param warehouseTemplateRequestDto
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> getShippingTemplate(WarehouseTemplateRequestDto warehouseTemplateRequestDto) throws Exception;

	List<String> getWarehouseCompanyName() throws Exception;

	ExcelDownloadDto getWarehouseExcelDownload(WarehouseRequestDto warehouseRequestDto);
}
