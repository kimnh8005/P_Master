package kr.co.pulmuone.v1.calculate.order.service;

		import kr.co.pulmuone.v1.calculate.order.dto.CalGoodsListRequestDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;

/**
 * <PRE>
 * Forbiz Korea
 * 정산관리 > 주문정산 > 상품정산 Interface
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

public interface CalGoodsBiz {

	/**
	 * 부문 구문 전체 조회
	 * @return
	 */
	ApiResult<?> getOuIdAllList();

	/**
	 * 상품 정산 리스트 목록 조회
	 * @param calGoodsListRequestDto
	 * @return
	 */
	ApiResult<?> getGoodsList(CalGoodsListRequestDto calGoodsListRequestDto);

	/**
	 * 상품 정산 리스트 목록  엑셀 다운로드
	 * @param calGoodsListRequestDto
	 * @return
	 */
	ExcelDownloadDto getGoodsListExportExcel(CalGoodsListRequestDto calGoodsListRequestDto);

	/**
	 * 상품 정산 (IF 아닌) 리스트 목록 조회
	 * @param calGoodsListRequestDto
	 * @return
	 */
	ApiResult<?> getGoodsNotIfList(CalGoodsListRequestDto calGoodsListRequestDto);

	/**
	 * 상품 정산 (IF 아닌) 리스트 목록  엑셀 다운로드
	 * @param calGoodsListRequestDto
	 * @return
	 */
	ExcelDownloadDto getGoodsNotIfListExportExcel(CalGoodsListRequestDto calGoodsListRequestDto);

	/**
	 * 매장 상품 정산 리스트 조회
	 * @param calGoodsListRequestDto
	 * @return
	 */
	ApiResult<?> getStoreGoodsList(CalGoodsListRequestDto calGoodsListRequestDto);

    /**
     * 매장 상품 정산 리스트 목록  엑셀 다운로드
     * @param calGoodsListRequestDto
     * @return
     */
    ExcelDownloadDto getStoreGoodsListExportExcel(CalGoodsListRequestDto calGoodsListRequestDto);
}