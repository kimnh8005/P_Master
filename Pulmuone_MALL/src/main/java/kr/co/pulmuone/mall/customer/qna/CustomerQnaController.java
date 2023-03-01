package kr.co.pulmuone.mall.customer.qna;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.customer.qna.dto.OnetooneQnaByUserRequestDto;
import kr.co.pulmuone.v1.customer.qna.dto.OnetooneQnaByUserResponseDto;
import kr.co.pulmuone.v1.customer.qna.dto.OnetooneQnaOrderInfoByUserResponseDto;
import kr.co.pulmuone.v1.user.buyer.dto.CommonGetCodeListResponseDto;
import kr.co.pulmuone.mall.customer.qna.service.CustomerQnaMallService;
import lombok.RequiredArgsConstructor;

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
*  1.0    2020. 11. 30.     최윤지          최초작성
* =======================================================================
* </PRE>
*/

@RestController
@RequiredArgsConstructor
public class CustomerQnaController {

	@Autowired
	CustomerQnaMallService customerQnaMallService;

	@GetMapping(value = "/customer/qna/getQnaInfoByCustomer")
	@ApiOperation(value = "1:1문의화면 정보 - 유형")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = CommonGetCodeListResponseDto.class),
			@ApiResponse(code = 901, message = "" + "[NEED_LOGIN] NEED_LOGIN - 로그인 필요 \n") }
				)
	public ApiResult<?> getQnaInfoByCustomer() throws Exception {
		return customerQnaMallService.getQnaInfoByCustomer();
	}

	@PostMapping(value="/customer/qna/addQnaByUser")
	@ApiOperation(value="1:1 문의 등록")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : null"),
			@ApiResponse(code = 901, message = "" + "[NEED_LOGIN] NEED_LOGIN - 로그인 필요 \n") }
				)
	public ApiResult<?> addQnaByUser(OnetooneQnaByUserRequestDto onetooneQnaByUserRequestDto) throws Exception {

		return customerQnaMallService.addQnaByUser(onetooneQnaByUserRequestDto);

	}

	@PostMapping(value="/customer/qna/putQnaByUser")
	@ApiOperation(value="1:1 문의 수정")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : null"),
			@ApiResponse(code = 901, message = "" + "[NEED_LOGIN] NEED_LOGIN - 로그인 필요 \n") }
				)
	public ApiResult<?> putQnaByUser(OnetooneQnaByUserRequestDto onetooneQnaByUserRequestDto) throws Exception {

		return customerQnaMallService.putQnaByUser(onetooneQnaByUserRequestDto);
	}

	@PostMapping(value="/customer/qna/getQnaDetailByUser")
	@ApiOperation(value="1:1 문의 상세 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = OnetooneQnaByUserResponseDto.class),
			@ApiResponse(code = 901, message = "" + "[NEED_LOGIN] NEED_LOGIN - 로그인 필요 \n") }
				)
	public ApiResult<?> getQnaDetailByUser(Long csQnaId) throws Exception {
		return customerQnaMallService.getQnaDetailByUser(csQnaId);
	}

	@PostMapping(value="/customer/qna/getOrderInfoPopupByQna")
	@ApiOperation(value="1:1 문의 주문조회 팝업 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = OnetooneQnaOrderInfoByUserResponseDto.class),
			@ApiResponse(code = 901, message = "" + "[NEED_LOGIN] NEED_LOGIN - 로그인 필요 \n") }
				)
	public ApiResult<?> getOrderInfoPopupByQna(String searchPeriod) throws Exception {
		return customerQnaMallService.getOrderInfoPopupByQna(searchPeriod);

	}

}
