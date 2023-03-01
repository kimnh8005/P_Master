package kr.co.pulmuone.v1.api.Integratederp.order.service;

import java.util.List;

import kr.co.pulmuone.v1.api.Integratederp.order.dto.ErpIfDeliveryRequestDto;
import kr.co.pulmuone.v1.api.Integratederp.order.dto.ErpIfDlvConditionRequestDto;
import kr.co.pulmuone.v1.api.Integratederp.order.dto.ErpIfMissRequestDto;
import kr.co.pulmuone.v1.api.Integratederp.order.dto.ErpIfSalesRequestDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;

/**
 * <PRE>
 * Forbiz Korea
 * 주문|취소 ERP 연동
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20200923   	ykk     최초작성
 * =======================================================================
 * </PRE>
 */

public interface OrderErpBiz {


	//주문|취소 입력
	public  ApiResult<?> addIfCustordInpByErp() throws Exception;

	//송장 조회
	public  ApiResult<?> getIfDlvSrchByErp(String ordNum) throws Exception;

	//송장 조회 완료
	public  ApiResult<?> putIfDlvFlagByErp(List<ErpIfDeliveryRequestDto> erpIfDeliveryRequestDto) throws Exception;

	//미출 조회
	public  ApiResult<?> getIfMisSrchByErp(String ordNum) throws Exception;

	//송장 조회 완료
	public  ApiResult<?> putIfMisFlagByErp(List<ErpIfMissRequestDto> erpIfMissRequestDto) throws Exception;


	//매출 확정 내역 조회
	public  ApiResult<?> getIfSalSrchByErp(String ordNum) throws Exception;


	//매출 확정 조회 완료
	public  ApiResult<?> putIfSalFlagByErp(List<ErpIfSalesRequestDto> erpIfSalesRequestDto) throws Exception;



}
