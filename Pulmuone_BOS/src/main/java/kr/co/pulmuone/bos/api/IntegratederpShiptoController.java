package kr.co.pulmuone.bos.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.pulmuone.v1.api.Integratederp.shipto.service.ShiptoErpBiz;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import lombok.extern.slf4j.Slf4j;


/**
 * <PRE>
 * Forbiz Korea
 * ERP 연동 테스트 Controller
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

@Slf4j
@RestController
public class IntegratederpShiptoController {

	@Autowired
	private ShiptoErpBiz shiptoErpBiz;


	@GetMapping(value = "/admin/api/getIfShiptoSrchByErp")
	public ApiResult<?> getIfShiptoSrchByErp(String splId) throws Exception{
		return shiptoErpBiz.getIfShiptoSrchByErp(splId);
	}
}

