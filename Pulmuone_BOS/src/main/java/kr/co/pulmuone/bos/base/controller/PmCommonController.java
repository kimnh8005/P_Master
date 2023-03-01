package kr.co.pulmuone.bos.base.controller;

import javax.servlet.http.HttpServletRequest;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.base.dto.vo.PmCouponCommonResultVo;
import kr.co.pulmuone.v1.base.dto.vo.PmPointCommonResultVo;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.pulmuone.v1.base.service.PmCommonBiz;


/**
 * <PRE>
 * Forbiz Korea
 * pm 공통 처리 API
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20200826    강윤경              최초작성
 * =======================================================================
 * </PRE>
 */
@RestController
public class PmCommonController {

	@Autowired
	private PmCommonBiz pmCommontBiz;

	@Autowired(required=true)
	private HttpServletRequest request;

	@PostMapping(value = "/admin/comm/getPmPointList")
	@ApiOperation(value = "적립금 목록 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = PmPointCommonResultVo.class)
	})
	public ApiResult<?> getPmPointList() {
		return pmCommontBiz.getPmPointList();
	}


	/**
	 * 쿠폰목록  전체 조회
	 * @param
	 * @return PmCommonResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/comm/getPmCpnList")
	@ApiOperation(value = "쿠폰목록 전체 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = PmCouponCommonResultVo.class)
	})
	public ApiResult<?> getPmCpnList() {
		return pmCommontBiz.getPmCpnList();
	}

}
