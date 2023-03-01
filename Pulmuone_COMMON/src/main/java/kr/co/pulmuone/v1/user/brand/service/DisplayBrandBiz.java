package kr.co.pulmuone.v1.user.brand.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.user.brand.dto.DisplayBrandListRequestDto;
import kr.co.pulmuone.v1.user.brand.dto.DisplayBrandRequestDto;

public interface DisplayBrandBiz {

	/**
	 * 전시 브랜드 목록 조회
	 */
	ApiResult<?> getDisplayBrandList(DisplayBrandListRequestDto displayBrandListRequestDto) throws Exception;

	/**
	 * 전시 브랜드 목록 조회 (리스트박스 조회용)
	 */
	ApiResult<?> searchDisplayBrandList(DisplayBrandRequestDto displayBrandRequestDto) throws Exception;

	/**
	 * 전시 브랜드 상세 조회
	 */
	ApiResult<?> getDisplayBrand(DisplayBrandRequestDto displayBrandRequestDto) throws Exception;

	/**
	 * 전시 브랜드 등록
	 */
	ApiResult<?> addDisplayBrand(DisplayBrandRequestDto displayBrandRequestDto) throws Exception;


	/**
	 * 전시 브랜드 수정
	 */
	ApiResult<?> putDisplayBrand(DisplayBrandRequestDto displayBrandRequestDto) throws Exception;

}
