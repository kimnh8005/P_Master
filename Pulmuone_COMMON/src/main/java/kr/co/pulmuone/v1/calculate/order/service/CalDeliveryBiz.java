package kr.co.pulmuone.v1.calculate.order.service;

import kr.co.pulmuone.v1.calculate.order.dto.CalDeliveryListRequestDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;

/**
 * <PRE>
 * Forbiz Korea
 * 정산관리 > 주문정산 > 택배비 내역 Interface
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

public interface CalDeliveryBiz {

	/**
	 * 부문 구문 전체 조회
	 * @return
	 */
	public ApiResult<?> getOuIdAllList();

	/**
	 * 택배비내역 리스트 조회
	 * @param calDeliveryListRequestDto
	 * @return
	 */
	public ApiResult<?> getDeliveryList(CalDeliveryListRequestDto calDeliveryListRequestDto);


	// 택배비내역  리스트 목록  엑셀 다운로드
	ExcelDownloadDto getDeliveryListExportExcel(CalDeliveryListRequestDto calDeliveryListRequestDto);
}