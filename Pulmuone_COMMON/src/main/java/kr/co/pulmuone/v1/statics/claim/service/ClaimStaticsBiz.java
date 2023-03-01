package kr.co.pulmuone.v1.statics.claim.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.statics.claim.dto.ClaimReasonStaticsRequestDto;
import kr.co.pulmuone.v1.statics.claim.dto.ClaimStaticsRequestDto;

/**
 * =======================================================================
 * 버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 * 1.0       2021.07.22.              이원호         최초작성
 * =======================================================================
 */

public interface ClaimStaticsBiz {

    /**
     * 클레임 현황 통계
     *
     * @param dto ClaimStaticsRequestDto
     * @return ApiResult<?>
     */
    ApiResult<?> getClaimStaticsList(ClaimStaticsRequestDto dto);

    /**
     * 클레임 현황 통계 엑셀다운로드
     *
     * @param dto ClaimStaticsRequestDto
     * @return ExcelDownloadDto
     */
    ExcelDownloadDto getClaimStaticsExcelDownload(ClaimStaticsRequestDto dto);

    /**
     * 클레임 사유별 현황 통계
     *
     * @param dto ClaimReasonStaticsRequestDto
     * @return ApiResult<?>
     */
    ApiResult<?> getClaimReasonStaticsList(ClaimReasonStaticsRequestDto dto);

    /**
     * 클레임 사유별 현황 통계 엑셀다운로드
     *
     * @param dto ClaimReasonStaticsRequestDto
     * @return ExcelDownloadDto
     */
    ExcelDownloadDto getClaimReasonStaticsExcelDownload(ClaimReasonStaticsRequestDto dto);

}
