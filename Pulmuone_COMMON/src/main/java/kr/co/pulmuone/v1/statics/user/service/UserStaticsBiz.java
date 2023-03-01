package kr.co.pulmuone.v1.statics.user.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.statics.user.dto.UserCountStaticsRequestDto;
import kr.co.pulmuone.v1.statics.user.dto.UserGroupStaticsRequestDto;
import kr.co.pulmuone.v1.statics.user.dto.UserTypeStaticsRequestDto;

/**
 * =======================================================================
 * 버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 * 1.0       2021.07.23.              이원호         최초작성
 * =======================================================================
 */

public interface UserStaticsBiz {

    /**
     * 회원 유형별 판매/매출 현황 통계
     *
     * @param dto UserTypeStaticsRequestDto
     * @return ApiResult<?>
     */
    ApiResult<?> getUserTypeStaticsList(UserTypeStaticsRequestDto dto);

    /**
     * 회원 유형별 판매/매출 현황 통계 엑셀다운로드
     *
     * @param dto UserTypeStaticsRequestDto
     * @return ExcelDownloadDto
     */
    ExcelDownloadDto getUserTypeStaticsExcelDownload(UserTypeStaticsRequestDto dto);

    /**
     * 일반 회원 등급별 판매현황 통계
     *
     * @param dto UserGroupStaticsRequestDto
     * @return ApiResult<?>
     */
    ApiResult<?> getUserGroupStaticsList(UserGroupStaticsRequestDto dto);

    /**
     * 일반 회원 등급별 판매현황 통계
     *
     * @param dto UserGroupStaticsRequestDto
     * @return ExcelDownloadDto
     */
    ExcelDownloadDto getUserGroupStaticsExcelDownload(UserGroupStaticsRequestDto dto);

    /**
     * 회원 보유현황 통계
     *
     * @param dto UserCountStaticsRequestDto
     * @return ApiResult<?>
     */
    ApiResult<?> getUserCountStaticsList(UserCountStaticsRequestDto dto);

    /**
     * 회원 보유현황 통계
     *
     * @param dto UserCountStaticsRequestDto
     * @return ExcelDownloadDto
     */
    ExcelDownloadDto getUserCountStaticsExcelDownload(UserCountStaticsRequestDto dto);

}
