package kr.co.pulmuone.v1.calculate.collation.service;

import kr.co.pulmuone.v1.calculate.collation.dto.CalOutmallListRequestDto;
import kr.co.pulmuone.v1.calculate.collation.dto.CalOutmallUploadDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import org.springframework.web.multipart.MultipartFile;

/**
 * <PRE>
 * Forbiz Korea
 * 정산관리 > 대사관리 > 외부몰 주문 대사 Interface
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

public interface CalOutmallBiz {


	/**
	 * 외부몰 주문 대사 리스트 조회
	 * @param calOutmallListRequestDto
	 * @return
	 */
	public ApiResult<?> getOutmallList(CalOutmallListRequestDto calOutmallListRequestDto);

	/**
	 * 외부몰 주문 대사 엑셀 업로드
	 * @param file
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> addOutmallExcelUpload(MultipartFile file) throws Exception;

	public CalOutmallUploadDto getSellerInfo(String sellersNm);


	/**
	 * 외부몰 주문 대사 상세내역 리스트 조회
	 * @param calOutmallListRequestDto
	 * @return
	 */
	public ApiResult<?> getOutmallDetlList(CalOutmallListRequestDto calOutmallListRequestDto);


	/**
	 * 외부몰 주문 대사 상세내역 엑셀 리스트 조회
	 * @param calOutmallListRequestDto
	 * @return
	 * @throws Exception
	 */
	public ExcelDownloadDto getOutmallDetlExcelList(CalOutmallListRequestDto calOutmallListRequestDto) throws Exception;

	/**
	 * BOS 클레임 사유 건수 조회
	 * @param psClaimBosId
	 * @return
	 * @throws Exception
	 */
	public int getPsClaimBosCount(long psClaimBosId);

	/**
	 * 외부몰 주문 대사 업로드 실패내역 엑셀 리스트 조회
	 * @param calOutmallListRequestDto
	 * @return
	 * @throws Exception
	 */
	public ExcelDownloadDto getCalOutmallUploadFailList(CalOutmallListRequestDto calOutmallListRequestDto) throws Exception;

}