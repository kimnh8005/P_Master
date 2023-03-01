package kr.co.pulmuone.v1.order.delivery.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.order.delivery.dto.OrderBulkTrackingNumberDetlListRequestDto;
import kr.co.pulmuone.v1.order.delivery.dto.OrderBulkTrackingNumberExcelUploadRequestDto;
import kr.co.pulmuone.v1.order.delivery.dto.OrderBulkTrackingNumberListRequestDto;
import kr.co.pulmuone.v1.order.delivery.dto.vo.OrderTrackingNumberVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * <PRE>
 * Forbiz Korea
 * 일괄 송장 Biz
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일				:  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 24.       이규한          	  최초작성
 * =======================================================================
 * </PRE>
 */
public interface OrderBulkTrackingNumberBiz {
	/** 일괄송장 엑셀 업로드 */
	public ApiResult<?> orderBulkTrackingNumberExcelUpload(MultipartFile file, OrderBulkTrackingNumberExcelUploadRequestDto paramDto);
	/** 일괄송장 입력 내역 목록 조회 */
	public ApiResult<?> getOrderBulkTrackingNumberList(OrderBulkTrackingNumberListRequestDto paramDto);
	/** 일괄송장 입력 실패내역 엑셀 다운로드 */
	public ExcelDownloadDto getOrderBulkTrackingNumberFailList(OrderBulkTrackingNumberListRequestDto paramDto);
	/** 일괄 송장 입력 내역 상세 목록 조회 */
	public ApiResult<?> getOrderBulkTrackingNumberDetlList(OrderBulkTrackingNumberDetlListRequestDto paramDto);
	/** 일괄 송장 입력 내역 상세 목록  엑셀 다운로드 */
	public ExcelDownloadDto getOrderBulkTrackingNumberDetlExcelList(OrderBulkTrackingNumberDetlListRequestDto paramDto);
	/** 주문상세 배송상태 업데이트 */
	public ApiResult<?> putOrderDetailDeliveryStatus(OrderTrackingNumberVo orderTrackingNumberVo, String orderStatus);

	public int addOrderTrackingNumber(OrderTrackingNumberVo orderTrackingNumberVo);

	public int putOrderTrackingNumber(OrderTrackingNumberVo orderTrackingNumberVo);
}