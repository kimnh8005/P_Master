package kr.co.pulmuone.v1.statics.outbound.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.statics.outbound.dto.MissOutboundStaticsRequestDto;
import kr.co.pulmuone.v1.statics.outbound.dto.OutboundStaticsRequestDto;
import org.springframework.web.servlet.ModelAndView;

/**
 * <PRE>
 * Forbiz Korea
 * 통계관리 출고통계 COMMON Interface
 *
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 * 버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 * 1.0       2021.04.08.              dgyoun         최초작성
 * 1.1       2021.05.12              이원호          수정사항 반영-기획변경 적용
 * =======================================================================
 * </PRE>
 */

public interface OutboundStaticsBiz {

    /**
     * 출고처/판매처 별 출고통계 리스트 조회
     *
     * @param dto OutboundStaticsRequestDto
     * @return ApiResult<?>
     * @throws BaseException
     */
    ApiResult<?> getOutboundStaticsList(OutboundStaticsRequestDto dto) throws BaseException;

    /**
     * 출고처/판매처 별 출고통계 엑셀 다운로드
     *
     * @param dto OutboundStaticsRequestDto
     * @return ExcelDownloadDto
     * @throws BaseException
     */
    ExcelDownloadDto getExportExcelOutboundStaticsList(OutboundStaticsRequestDto dto) throws BaseException;

    /**
     * 미출 통계 리스트 조회
     *
     * @param dto OutboundStaticsRequestDto
     * @return ApiResult<?>
     * @throws BaseException
     */
    ApiResult<?> getMissOutboundStaticsList(MissOutboundStaticsRequestDto dto) throws BaseException;

    /**
     * 미출 통계 리스트 엑셀 다운로드
     *
     * @param dto OutboundStaticsRequestDto
     * @return ExcelDownloadDto
     * @throws BaseException
     */
    ExcelDownloadDto getExportExcelMissOutboundStaticsList(MissOutboundStaticsRequestDto dto) throws BaseException;

}
