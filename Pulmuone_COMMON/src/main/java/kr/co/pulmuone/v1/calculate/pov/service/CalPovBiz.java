package kr.co.pulmuone.v1.calculate.pov.service;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import kr.co.pulmuone.v1.calculate.pov.dto.vo.CalPovProcessVo;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;

/**
 * <PRE>
 * Forbiz Korea
 * 정산관리 > POV I/F > POV I/F Interface
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 03. 05.	이명수		최초작성
 * =======================================================================
 * </PRE>
 */

public interface CalPovBiz {

	/**
	 * POV I/F 리스트 조회
	 *
	 * @param calPovListRequestDto
	 * @return
	 */
	public ApiResult<?> getPovList(String year, String month) throws Exception;

	/**
	 * POV I/F 리스트 엑셀 다운로드
	 *
	 * @param calPovListRequestDto
	 * @return
	 */
	public ExcelDownloadDto getPovListExportExcel(String year, String month) throws Exception;

	/**
	 * POV I/F 엑셀 업로드
	 *
	 * @param file
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> addPovExcelUpload(MultipartFile file, CalPovProcessVo calPovProcessVo) throws Exception;

	/**
	 *
	 * @param year
	 * @param month
	 * @return
	 * @throws Exception
	 */
	public ApiResult<?> odPovInterface(String scenario, String year, String month, String user) throws Exception;

}