package kr.co.pulmuone.bos.user.warehouse;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.user.warehouse.dto.DeliveryPatternDetailResponseDto;
import kr.co.pulmuone.v1.user.warehouse.dto.DeliveryPatternRequestDto;
import kr.co.pulmuone.v1.user.warehouse.dto.DeliveryPatternResponseDto;
import kr.co.pulmuone.v1.user.warehouse.service.DeliveryPatternBiz;

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
 *  1.0    20200916		       안치열            최초작성
 * =======================================================================
 * </PRE>
 */

@RestController
public class DeliveryPatternController {

	@Autowired
	private DeliveryPatternBiz deliveryPatternBiz;

	@Autowired(required=true)
	private HttpServletRequest request;


	/**
	 * 배송패턴 리스트 조회
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/ur/warehouse/getDeliveryPatternList")
	@ResponseBody
	@ApiOperation(value = "배송패턴 리스트 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = DeliveryPatternResponseDto.class)
	})
	public ApiResult<?>  getDeliveryPatternList(DeliveryPatternRequestDto deliveryPatternRequestDto) throws Exception {

		return deliveryPatternBiz.getDeliveryPatternList((DeliveryPatternRequestDto) BindUtil.convertRequestToObject(request, DeliveryPatternRequestDto.class));
	}

	/**
	 * 배송패턴 상세조회
	 * @param
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/ur/warehouse/getShippingPattern")
	@ResponseBody
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = DeliveryPatternDetailResponseDto.class)
	})
	public ApiResult<?>  getShippingPattern(DeliveryPatternRequestDto deliveryPatternRequestDto) throws Exception {

		return deliveryPatternBiz.getShippingPattern(deliveryPatternRequestDto);
	}


	/**
	 * 배송패턴 등록
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/ur/warehouse/addDeliveryPattern")
	@ResponseBody
	@ApiOperation(value = "배송패턴 등록")
	public ApiResult<?> addDeliveryPattern(DeliveryPatternRequestDto deliveryPatternRequestDto) throws Exception {

		return deliveryPatternBiz.addDeliveryPattern(deliveryPatternRequestDto);
	}


	/**
	 * 배송패턴 수정
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/ur/warehouse/putDeliveryPattern")
	@ResponseBody
	@ApiOperation(value = "배송패턴 수정")
	public ApiResult<?> putDeliveryPattern(DeliveryPatternRequestDto deliveryPatternRequestDto) throws Exception {

		return deliveryPatternBiz.putDeliveryPattern(deliveryPatternRequestDto);
	}
}
