package kr.co.pulmuone.v1.calculate.collation.service;

import kr.co.pulmuone.v1.calculate.collation.dto.CalPgListRequestDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import org.springframework.web.multipart.MultipartFile;

/**
 * <PRE>
 * Forbiz Korea
 * 정산관리 > 대사관리 > PG 거래 내역 대사 Interface
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

public interface CalPgBiz {

	/**
	 * PG 거래 내역 대사 리스트 조회
	 * @param calPgListRequestDto
	 * @return
	 */
	public ApiResult<?> getPgList(CalPgListRequestDto calPgListRequestDto);

	/**
	 * PG 거래내역 엑셀 업로드
	 * @param file
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> addPgExcelUpload(MultipartFile file, CalPgListRequestDto calPgListRequestDto) throws Exception;


	/**
	 * PG 거래내역 엑셀 업로드 실패내역 엑셀 리스트 조회
	 * @param calPgListRequestDto
	 * @return
	 * @throws Exception
	 */
	public ExcelDownloadDto getCalPgUploadFailList(CalPgListRequestDto calPgListRequestDto) throws Exception;

}