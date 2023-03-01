package kr.co.pulmuone.v1.calculate.collation.service;

import kr.co.pulmuone.v1.calculate.collation.dto.CalPgDetlListRequestDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;

/**
 * <PRE>
 * Forbiz Korea
 * 정산관리 > 대사관리 > PG 대사 상세내역 Interface
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 * 버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 * 1.0		2021. 04. 26.	이원호		최초작성
 * =======================================================================
 * </PRE>
 */

public interface CalPgDetlBiz {

    /**
     * PG 대사 상세내역 리스트 조회
     *
     * @param dto CalPgDetlListRequestDto
     * @return ApiResult<?>
     */
    ApiResult<?> getPgDetailList(CalPgDetlListRequestDto dto);

    /**
     * PG 대사 상세내역 엑셀 다운로드
     *
     * @param dto CalPgDetlListRequestDto
     * @return ExcelDownloadDto
     */
    ExcelDownloadDto getPgDetlListExportExcel(CalPgDetlListRequestDto dto);

}