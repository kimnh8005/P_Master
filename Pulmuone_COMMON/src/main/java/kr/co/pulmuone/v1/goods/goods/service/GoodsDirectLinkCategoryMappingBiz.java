package kr.co.pulmuone.v1.goods.goods.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsDirectLinkCategoryMappingRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsDirectLinkCategoryMappingResponseDto;

public interface GoodsDirectLinkCategoryMappingBiz {

	/* 네이버 표준 카테고리 맵핑 조회 */
	GoodsDirectLinkCategoryMappingResponseDto getGoodsDirectLinkCategoryMappingList(GoodsDirectLinkCategoryMappingRequestDto paramDto) throws BaseException;

	/* 네이버 표준 카테고리 맵핑 조회내역 다운로드 */
	ExcelDownloadDto getGoodsDirectLinkCategoryMappingListExcel(GoodsDirectLinkCategoryMappingRequestDto paramDto) throws BaseException;

	/* 네이버 카테고리 조회 */
	GoodsDirectLinkCategoryMappingResponseDto getIfNaverCategoryList(GoodsDirectLinkCategoryMappingRequestDto paramDto) throws BaseException;

	/* 네이버 표준 카테고리 맵핑 등록 */
	GoodsDirectLinkCategoryMappingResponseDto addGoodsDirectLinkCategoryMapping(GoodsDirectLinkCategoryMappingRequestDto paramDto) throws BaseException;

	/* 네이버 표준 카테고리 맵핑 수정 */
	GoodsDirectLinkCategoryMappingResponseDto putGoodsDirectLinkCategoryMapping(GoodsDirectLinkCategoryMappingRequestDto paramDto) throws BaseException;

}
