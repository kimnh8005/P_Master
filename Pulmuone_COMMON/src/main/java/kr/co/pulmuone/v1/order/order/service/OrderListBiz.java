package kr.co.pulmuone.v1.order.order.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.order.order.dto.OrderListRequestDto;
import kr.co.pulmuone.v1.statics.data.dto.DataDownloadStaticsResponseDto;

/**
 * <PRE>
 * Forbiz Korea
 * 주문리스트 관련 Interface
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 14.            이명수         최초작성
 *  1.1    2020. 12. 15.            석세동         수정
 *  1.2    2021. 01. 11.            김명진         엑셀다운로드 추가
 * =======================================================================
 * </PRE>
 */
public interface OrderListBiz {

	/**
	 * 주문 리스트
	 * @param orderListRequestDto
	 * @return
	 */
	ApiResult<?> getOrderList(OrderListRequestDto orderListRequestDto);

	/**
	 * 주문 리스트 엑셀 다운로드
	 * @param orderListRequestDto
	 * @return
	 */
	ExcelDownloadDto getOrderExcelList(OrderListRequestDto orderListRequestDto);

	/**
	 * 매장 주문 리스트 엑셀 다운로드
	 * @param orderListRequestDto
	 * @return
	 */
	ExcelDownloadDto getShopOrderExcelList(OrderListRequestDto orderListRequestDto);

	/**
	 * 주문 상세 리스트
	 * @param orderListRequestDto
	 * @return
	 */
	ApiResult<?> getOrderDetailList(OrderListRequestDto orderListRequestDto);

	/**
	 * 주문 상세 리스트 엑셀다운로드
	 * @param orderListRequestDto
	 * @return
	 */
	ExcelDownloadDto getOrderDetailExcelList(OrderListRequestDto orderListRequestDto);

	DataDownloadStaticsResponseDto getOrderDetailExcelListMake(OrderListRequestDto orderListRequestDto) throws Exception;

	/**
	 * 미출 주문 상세 리스트
	 * @param orderListRequestDto
	 * @return
	 */
	ApiResult<?> getUnreleasedOrderDetailList(OrderListRequestDto orderListRequestDto);

	/**
	 * 미출 주문 상세 리스트 엑셀다운로드
	 * @param orderListRequestDto
	 * @return
	 */
	ExcelDownloadDto getUnreleasedOrderDetailExcelList(OrderListRequestDto orderListRequestDto);

	/**
	 * 반품 주문 상세 리스트
	 * @param orderListRequestDto
	 * @return
	 */
	ApiResult<?> getReturnOrderDetailList(OrderListRequestDto orderListRequestDto);

	/**
	 * 반품 주문 상세 리스트 엑셀다운로드
	 * @param orderListRequestDto
	 * @return
	 */
	ExcelDownloadDto getReturnOrderDetailExcelList(OrderListRequestDto orderListRequestDto);
	/**
	 * 환불 주문 상세 리스트
	 * @param orderListRequestDto
	 * @return
	 */
	ApiResult<?> getRefundOrderDetailList(OrderListRequestDto orderListRequestDto);

	/**
	 * 환불 주문 상세 리스트 엑셀다운로드
	 * @param orderListRequestDto
	 * @return
	 */
	ExcelDownloadDto getRefundOrderDetailExcelList(OrderListRequestDto orderListRequestDto);

	/**
	 * CS환불 승인리스트 조회
	 * @param csRefundRequest
	 * @return
	 */
	ApiResult<?> getCsRefundApprovalList(OrderListRequestDto csRefundRequest);

	/**
	 * CS환불 승인리스트 엑셀다운로드
	 * @param orderListRequestDto
	 * @return
	 */
	ExcelDownloadDto getCsRefundApprovalExcelList(OrderListRequestDto orderListRequestDto);

	/**
	 * 상세리스트 카운트 요약정보
	 * @param orderListRequestDto
	 * @return
	 */
	ApiResult<?> getOrderTotalCountInfo(OrderListRequestDto orderListRequestDto);

	/**
	 * 반품 사유 코드 목록 조회
	 * @param
	 * @return
	 */
	ApiResult<?> getReturnReasonList();
}
