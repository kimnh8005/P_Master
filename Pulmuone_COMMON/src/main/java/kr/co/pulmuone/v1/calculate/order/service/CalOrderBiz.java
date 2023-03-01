package kr.co.pulmuone.v1.calculate.order.service;

import kr.co.pulmuone.v1.calculate.order.dto.CalOrderListRequestDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;

/**
 * <PRE>
 * Forbiz Korea
 * 정산관리 > 주문정산 > 주문 정산 Interface
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

public interface CalOrderBiz {

	/**
	 * 부문 구문 전체 조회
	 * @return
	 */
	public ApiResult<?> getOuIdAllList();

	/**
	 * 상품 정산 리스트 조회
	 * @param calOrderListRequestDto
	 * @return
	 */
	public ApiResult<?> getOrderList(CalOrderListRequestDto calOrderListRequestDto);

	//상품 정산 리스트 목록  엑셀 다운로드
	ExcelDownloadDto getOrderListExportExcel(CalOrderListRequestDto calOrderListRequestDto);
}