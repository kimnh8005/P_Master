package kr.co.pulmuone.v1.api.Integratederp.shipto.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.api.Integratederp.order.dto.ErpIfDeliveryLineResponseDto;
import kr.co.pulmuone.v1.api.Integratederp.order.dto.ErpIfDeliveryRequestDto;
import kr.co.pulmuone.v1.api.Integratederp.order.dto.ErpIfDeliveryResponseDto;
import kr.co.pulmuone.v1.api.Integratederp.order.dto.ErpIfDlvConditionRequestDto;
import kr.co.pulmuone.v1.api.Integratederp.order.dto.ErpIfMissRequestDto;
import kr.co.pulmuone.v1.api.Integratederp.order.dto.ErpIfMissResponseDto;
import kr.co.pulmuone.v1.api.Integratederp.order.dto.ErpIfSalesRequestDto;
import kr.co.pulmuone.v1.api.Integratederp.order.dto.ErpIfSalesResponseDto;
import kr.co.pulmuone.v1.comm.api.dto.basic.ErpIfGoodsUpdateRequestDto;
import kr.co.pulmuone.v1.comm.api.dto.vo.BaseApiResponseVo;
import kr.co.pulmuone.v1.comm.api.service.ErpApiExchangeService;
import kr.co.pulmuone.v1.comm.util.JsonUtil;

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
public class ShiptoErpService {

	private static final Logger log = LoggerFactory.getLogger(ShiptoErpService.class);

    //ERP API 인터페이스 ID
    private static final String SUPPLIER_SEARCH_INTERFACE_ID = "IF_SHIPTO_SRCH";	//납품처정보 조회

    @Autowired
    ErpApiExchangeService erpApiExchangeService;


	/**
     * 납품처정보 조회 ERP 연동 IF_SHIPTO_SRCH
     * ERP 연동 후 결과값 return
	 * @return
	 */
	protected BaseApiResponseVo getIfShiptoSrchByErp(String splId) throws Exception {
        Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put("splId", splId);

		BaseApiResponseVo baseApiResponseVo = erpApiExchangeService.get(parameterMap, SUPPLIER_SEARCH_INTERFACE_ID);

	    if (!baseApiResponseVo.isSuccess()) {
	        log.error(" ============== getIfShiptoSrchByErp API Call Failure");
	    }

        return baseApiResponseVo;
	}

}
