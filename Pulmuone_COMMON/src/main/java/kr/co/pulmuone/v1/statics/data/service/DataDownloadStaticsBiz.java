package kr.co.pulmuone.v1.statics.data.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.statics.data.dto.DataDownloadStaticsRequestDto;

/**
 * <PRE>
 * 통계관리 > 데이터 추출 COMMON Interface
 *
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 * 버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 * 1.0       2021.06.10.              whseo         최초작성
 * =======================================================================
 * </PRE>
 */

public interface DataDownloadStaticsBiz {

    /**
     * 데이터 추출 조회
     *
     * @param dto DataDownloadStaticsRequestDto
     * @return ApiResult<?>
     * @throws BaseException
     */
    ApiResult<?> getDataDownloadStaticsList(DataDownloadStaticsRequestDto dto) throws BaseException;


}
