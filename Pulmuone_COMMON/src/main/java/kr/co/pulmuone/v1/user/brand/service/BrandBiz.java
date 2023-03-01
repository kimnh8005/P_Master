package kr.co.pulmuone.v1.user.brand.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.user.brand.dto.AddBrandRequestDto;
import kr.co.pulmuone.v1.user.brand.dto.GetBrandListRequestDto;
import kr.co.pulmuone.v1.user.brand.dto.GetBrandRequestDto;
import kr.co.pulmuone.v1.user.brand.dto.PutBrandRequestDto;

public interface BrandBiz {

	/**
	 * 브랜드 목록 조회
	 */
	ApiResult<?> getBrandList(GetBrandListRequestDto getBrandListRequestDto) throws Exception;

	/**
	 * 브랜드 목록 조회 (리스트박스 조회용)
	 */
	ApiResult<?> searchBrandList(GetBrandRequestDto getBrandRequestDto) throws Exception;


	/**
	 * 브랜드 상세 조회
	 */
	ApiResult<?> getBrand(GetBrandRequestDto getBrandRequestDto) throws Exception;

	/**
	 * 브랜드 등록
	 */
	ApiResult<?> addBrand(AddBrandRequestDto addBrandRequestDto) throws Exception;


	/**
	 * 브랜드 수정
	 */
	ApiResult<?> putBrand(PutBrandRequestDto putBrandRequestDto) throws Exception;

}
