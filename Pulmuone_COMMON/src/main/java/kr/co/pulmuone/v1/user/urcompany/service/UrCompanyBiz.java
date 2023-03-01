package kr.co.pulmuone.v1.user.urcompany.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.user.urcompany.dto.AddClientRequestDto;
import kr.co.pulmuone.v1.user.urcompany.dto.GetClientRequestDto;
import kr.co.pulmuone.v1.user.urcompany.dto.GetCompanyListRequestDto;
import kr.co.pulmuone.v1.user.urcompany.dto.GetStoreListRequestDto;
import kr.co.pulmuone.v1.user.urcompany.dto.GetSupplierListRequestDto;
import kr.co.pulmuone.v1.user.urcompany.dto.GetWarehouseListRequestDto;
import kr.co.pulmuone.v1.user.urcompany.dto.PutClientRequestDto;

public interface UrCompanyBiz {

	/**
	 * 거래처 목록 조회
	 */
	ApiResult<?> getCompanyList(GetCompanyListRequestDto getCompanyListRequestDto) throws Exception;

	/**
	 * 거래처 상세조회
	 */
	ApiResult<?> getClient(GetClientRequestDto getClientRequestDto) throws Exception;

	/**
	 * 공급업체 조회
	 */
	ApiResult<?> getSupplierCompanyList(GetSupplierListRequestDto getSupplierListRequestDto) throws Exception;

	/**
	 * 출고처 별 공급업체 조회
	 */
	ApiResult<?> getSupplierCompanyListByWhareHouse(GetSupplierListRequestDto getSupplierListRequestDto) throws Exception;

	/**
	 * 출고처 검색 팝업
	 */
	ApiResult<?> getWarehouseList(GetWarehouseListRequestDto getWarehouseListRequestDto) throws Exception;

	/**
	 * 매장 조회
	 */
	ApiResult<?> getStoreList(GetStoreListRequestDto getStoreListRequestDto) throws Exception;


	/**
	 * 출고처 등록
	 */
	ApiResult<?> addClient(AddClientRequestDto addClientRequestDto) throws Exception;

	/**
	 * 출고처 수정
	 */
	ApiResult<?> putClient(PutClientRequestDto putClientRequestDto) throws Exception;


	ApiResult<?> getWarehouseGroupByWarehouseList(GetCompanyListRequestDto getCompanyListRequestDto) throws Exception;


	ApiResult<?> getSupplierCompanyByWarehouseList(GetCompanyListRequestDto getCompanyListRequestDto) throws Exception;
}
