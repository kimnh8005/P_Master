package kr.co.pulmuone.v1.calculate.collation.service;

import kr.co.pulmuone.v1.calculate.collation.dto.CalSalesListRequestDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;

/**
 * <PRE>
 * Forbiz Korea
 * 정산관리 > 대사관리 > 통합몰 매출 대사 Interface
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 * 버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 * 1.0		2021. 04. 28.	이원호		최초작성
 * =======================================================================
 * </PRE>
 */

public interface CalSalesBiz {

    /**
     * 통합몰 매출 대사 리스트 조회
     *
     * @param dto CalSalesListRequestDto
     * @return ApiResult<?>
     */
    ApiResult<?> getSalesList(CalSalesListRequestDto dto);

    /**
     * 통합몰 매출 대사 엑셀 다운로드
     *
     * @param dto CalPgDetlListRequestDto
     * @return ExcelDownloadDto
     */
    ExcelDownloadDto getSalesListExportExcel(CalSalesListRequestDto dto) throws Exception;

}