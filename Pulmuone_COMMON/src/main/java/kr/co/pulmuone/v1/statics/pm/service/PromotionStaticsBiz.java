package kr.co.pulmuone.v1.statics.pm.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.statics.pm.dto.PromotionStaticsRequestDto;
import kr.co.pulmuone.v1.statics.pm.dto.PromotionStaticsResponseDto;

public interface PromotionStaticsBiz {

    /**
     * 내부광고코드별 매출현황통계
     *
     * @param dto PromotionStaticsRequestDto
     * @return ApiResult<?>
     * @throws BaseException
     */
    ApiResult<?> getStaticsInternalAdvertisingList(PromotionStaticsRequestDto dto) throws BaseException;

    /**
     * 내부광고코드별 매출현황통계 엑셀 다운로드
     *
     * @param dto PromotionStaticsRequestDto
     * @return ExcelDownloadDto
     * @throws BaseException
     */
    ExcelDownloadDto getExportExcelStaticsInternalAdvertisingList(PromotionStaticsRequestDto dto) throws BaseException;



    /**
     * 외부광고코드별 매출현황 통계
     *
     * @param dto PromotionStaticsRequestDto
     * @return ApiResult<?>
     * @throws BaseException
     */
    ApiResult<?> getStaticsAdvertisingList(PromotionStaticsRequestDto dto) throws BaseException;

    /**
     * 외부광고코드별 매출현황 통계엑셀 다운로드
     *
     * @param dto PromotionStaticsRequestDto
     * @return ExcelDownloadDto
     * @throws BaseException
     */
    ExcelDownloadDto getExportExcelStaticsAdvertisingList(PromotionStaticsRequestDto dto) throws BaseException;


    /**
     * 쿠폰별 매출현황 통계
     *
     * @param dto PromotionStaticsRequestDto
     * @return ApiResult<?>
     * @throws BaseException
     */
    ApiResult<?> getStaticsCouponSaleStatusList(PromotionStaticsRequestDto dto) throws BaseException;

    /**
     * 쿠폰별 매출현황 통계 엑셀 다운로드
     *
     * @param dto PromotionStaticsRequestDto
     * @return ExcelDownloadDto
     * @throws BaseException
     */
    ExcelDownloadDto getExportExcelStaticsCouponSaleStatusList(PromotionStaticsRequestDto dto) throws BaseException;


    /**
     * 회원등급 쿠폰현황 통계
     *
     * @param dto PromotionStaticsRequestDto
     * @return ApiResult<?>
     * @throws BaseException
     */
    ApiResult<?> getStaticsUserGroupCouponStatusList(PromotionStaticsRequestDto dto) throws BaseException;

    /**
     * 회원등급 쿠폰현황 통계 엑셀 다운로드
     *
     * @param dto PromotionStaticsRequestDto
     * @return ExcelDownloadDto
     * @throws BaseException
     */
    ExcelDownloadDto getExportExcelStaticsUserGroupCouponStatusList(PromotionStaticsRequestDto dto) throws BaseException;

    /**
     * 적립금 현황 통계
     *
     * @param dto PromotionStaticsRequestDto
     * @return ApiResult<?>
     * @throws BaseException
     */
    ApiResult<?> getStaticsPointStatusList(PromotionStaticsRequestDto dto) throws BaseException;

    /**
     * 적립금 현황 통계 엑셀 다운로드
     *
     * @param dto PromotionStaticsRequestDto
     * @return ExcelDownloadDto
     * @throws BaseException
     */
    ExcelDownloadDto getExportExcelStaticsPointStatusList(PromotionStaticsRequestDto dto) throws BaseException;


    /**
     * 내부광고 코드 관리 유형 조회
     */
    PromotionStaticsResponseDto getAdvertisingType(PromotionStaticsRequestDto dto) throws Exception;

    /**
     * 외부광고코드별 매출현황 상품별 통계
     */
    ApiResult<?> getStaticsAdvertisingGoodsList(PromotionStaticsRequestDto dto) throws BaseException;

    /**
     * 외부광고코드별 매출현황 통계 상품별 엑셀 다운로드
     */
    ExcelDownloadDto getExportExcelStaticsAdvertisingGoodsList(PromotionStaticsRequestDto dto) throws BaseException;

}
