package kr.co.pulmuone.v1.api.Integratederp.order.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
 *  1.0    20200923   	강윤경     최초작성
 * =======================================================================
 * </PRE>
 */

@Service
public class OrderErpBizImpl implements OrderErpBiz {

	private static final Logger log = LoggerFactory.getLogger(OrderErpBizImpl.class);

	@Autowired
	private OrderErpService orderErpService;


	/**
	 * 주문|취소 입력
	 * @param
	 * @return
	 * @throws	Exception
	 */
	@Override
	public ApiResult<?> addIfCustordInpByErp() throws Exception {
		return ApiResult.success(orderErpService.addIfCustordInpByErp());
	}



	/**
	 * 송장 조회
	 * @param
	 * @return
	 * @throws	Exception
	 */
	@Override
	public ApiResult<?> getIfDlvSrchByErp(String ordNum) throws Exception {
		return ApiResult.success(orderErpService.getIfDlvSrchByErp(ordNum));
	}


	/**
	 * 송장 조회 완료
	 * @param
	 * @return
	 * @throws	Exception
	 */
	@Override
	public ApiResult<?> putIfDlvFlagByErp(List<ErpIfDeliveryRequestDto> erpIfDeliveryRequestDto) throws Exception {
		return ApiResult.success(orderErpService.putIfDlvFlagByErp(erpIfDeliveryRequestDto));
	}



	/**
	 * 미출 조회
	 * @param
	 * @return
	 * @throws	Exception
	 */
	@Override
	public ApiResult<?> getIfMisSrchByErp(String ordNum) throws Exception {
		return ApiResult.success(orderErpService.getIfMisSrchByErp(ordNum));
	}



	/**
	 * 송장 조회 완료
	 * @param
	 * @return
	 * @throws	Exception
	 */
	@Override
	public ApiResult<?> putIfMisFlagByErp(List<ErpIfMissRequestDto> erpIfMissRequestDto) throws Exception {
		return ApiResult.success(orderErpService.putIfMisFlagByErp(erpIfMissRequestDto));
	}





	/**
	 * 매출 확정 내역 조회
	 * @param
	 * @return
	 * @throws	Exception
	 */
	@Override
	public ApiResult<?> getIfSalSrchByErp(String ordNum) throws Exception {
		return ApiResult.success(orderErpService.getIfSalSrchByErp(ordNum));
	}



	/**
	 * 매출 확정 조회 완료
	 * @param
	 * @return
	 * @throws	Exception
	 */
	@Override
	public ApiResult<?> putIfSalFlagByErp(List<ErpIfSalesRequestDto> erpIfSalesRequestDto) throws Exception {
		return ApiResult.success(orderErpService.putIfSalFlagByErp(erpIfSalesRequestDto));
	}


}
