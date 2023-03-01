package kr.co.pulmuone.bos.user.shipping;

import kr.co.pulmuone.bos.user.shipping.service.UserShippingBosService;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.user.buyer.dto.*;
import kr.co.pulmuone.v1.user.buyer.service.UserBuyerBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.servlet.http.HttpServletRequest;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20200702		   	천혜현            최초작성
 * =======================================================================
 * </PRE>
 */
@RestController
public class UserShippingController {

	@Autowired
	private UserShippingBosService userShippingBosService;

	@Autowired(required=true)
	private HttpServletRequest request;


	/**
	 * 배송지 리스트조회
	 * @param	CommonGetShippingAddressListRequestDto
	 * @return	CommonGetShippingAddressListResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/ur/userShipping/getShippingAddressList")
	@ApiOperation(value = "배송지 리스트 조회", httpMethod = "POST", notes = "배송지 리스트 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = CommonGetShippingAddressListResponseDto.class),
	})
	public ApiResult<?> getShippingAddressList(CommonGetShippingAddressListRequestDto dto) throws Exception {
		return ApiResult.success(userShippingBosService.getShippingAddressList((CommonGetShippingAddressListRequestDto) BindUtil.convertRequestToObject(request, CommonGetShippingAddressListRequestDto.class)));
	}


	/**
	 * 배송지 단일조회
	 * @param	CommonGetShippingAddressRequestDto
	 * @return	GetShippingAddressResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/ur/userShipping/getShippingAddress")
	@ApiOperation(value = "배송지 단일조회", httpMethod = "POST", notes = "배송지 단일조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetRefundBankResponseDto.class),
	})
	public ApiResult<?> getShippingAddress(CommonGetShippingAddressRequestDto dto) throws Exception {
		return ApiResult.success(userShippingBosService.getShippingAddress(dto));
	}


	/**
	 * 배송지 수정
	 * @param	CommonSaveShippingAddressRequestDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/ur/userShipping/putShippingAddress")
	@ApiOperation(value = "배송지 수정", httpMethod = "POST", notes = "배송지 수정")
	public ApiResult<?> putShippingAddress(CommonSaveShippingAddressRequestDto dto) throws Exception {
		return ApiResult.success(userShippingBosService.putShippingAddress(dto));
	}


	/**
	 * 배송지 등록
	 * @param	CommonSaveShippingAddressRequestDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/ur/userShipping/addShippingAddress")
	@ApiOperation(value = "배송지 등록", httpMethod = "POST", notes = "배송지 등록")
	public ApiResult<?> addShippingAddress(CommonSaveShippingAddressRequestDto dto) throws Exception {
		return ApiResult.success(userShippingBosService.addShippingAddress(dto));
	}

	/**
	 * 배송지 목록에서 주문배송지로 설정
	 * @param	CommonSaveShippingAddressRequestDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/ur/userShipping/putShippingAddressIntoOrderShippingZone")
	@ApiOperation(value = "배송지 목록에서 주문배송지로 설정", httpMethod = "POST", notes = "배송지 목록에서 주문배송지로 설정")
	public ApiResult<?> putShippingAddressIntoOrderShippingZone(CommonSaveShippingAddressRequestDto dto) throws Exception {
		return userShippingBosService.putShippingAddressIntoOrderShippingZone(dto);
	}

}

