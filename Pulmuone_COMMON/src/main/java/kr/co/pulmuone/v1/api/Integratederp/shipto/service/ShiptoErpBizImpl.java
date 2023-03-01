package kr.co.pulmuone.v1.api.Integratederp.shipto.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.client.util.StringUtils;

import kr.co.pulmuone.v1.api.Integratederp.order.dto.ErpIfDeliveryRequestDto;
import kr.co.pulmuone.v1.api.Integratederp.order.dto.ErpIfDeliveryResponseDto;
import kr.co.pulmuone.v1.api.Integratederp.order.dto.ErpIfDlvConditionRequestDto;
import kr.co.pulmuone.v1.api.Integratederp.order.dto.ErpIfMissRequestDto;
import kr.co.pulmuone.v1.api.Integratederp.order.dto.ErpIfSalesRequestDto;
import kr.co.pulmuone.v1.api.Integratederp.shipto.dto.ErpIfShiptoSrchResponseDto;
import kr.co.pulmuone.v1.comm.api.dto.vo.BaseApiResponseVo;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.ApiEnums;
import kr.co.pulmuone.v1.comm.util.StringUtil;

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
 *  1.0    20210115   	천혜현     최초작성
 * =======================================================================
 * </PRE>
 */

@Service
public class ShiptoErpBizImpl implements ShiptoErpBiz {

	@Autowired
	private ShiptoErpService shiptoErpService;


	/**
	 * 납품처정보 조회
	 * @param supplierCode
	 * @return
	 * @throws	Exception
	 */
	@Override
	public ApiResult<?> getIfShiptoSrchByErp(String splId) throws Exception {

		BaseApiResponseVo baseApiResponseVo = shiptoErpService.getIfShiptoSrchByErp(splId);

		if(baseApiResponseVo.isSuccess()) {
			List<ErpIfShiptoSrchResponseDto> erpIfShiptoSrchResponseDtoList = baseApiResponseVo.deserialize(ErpIfShiptoSrchResponseDto.class);

			for(ErpIfShiptoSrchResponseDto dto : erpIfShiptoSrchResponseDtoList) {
				if(StringUtil.isNotEmpty(dto.getSplId())) {
					return ApiResult.success(dto);
				}
			}
		}

		return ApiResult.result(ApiEnums.IfShiptoSrchByErp.FAIL);
	}


}
