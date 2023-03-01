package kr.co.pulmuone.mall.customer.faq;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.customer.faq.dto.GetFaqListByUserRequsetDto;
import kr.co.pulmuone.v1.customer.faq.dto.GetFaqListByUserResponseDto;
import kr.co.pulmuone.v1.user.buyer.dto.CommonGetCodeListResponseDto;
import kr.co.pulmuone.mall.customer.faq.service.CustomerFaqMallService;
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
*  1.0    2020. 11. 25.    최윤지          최초작성
* =======================================================================
* </PRE>
*/

@RestController
@RequiredArgsConstructor
public class CustomerFaqController {

	@Autowired
	CustomerFaqMallService customerFaqMallService;

	@PostMapping(value = "/customer/faq/getFaqListByUser")
	@ApiOperation(value = "FAQ 게시판 리스트 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetFaqListByUserResponseDto.class) })
	public ApiResult<?> getFaqListByUser(HttpServletRequest request, GetFaqListByUserRequsetDto getFaqListByUserRequsetDto) throws Exception{
		return customerFaqMallService.getFaqListByUser(getFaqListByUserRequsetDto);
	}

	@GetMapping(value = "/customer/faq/getFaqInfoByUser")
	@ApiOperation(value = "FAQ 게시판 정보")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response =CommonGetCodeListResponseDto.class) })
	public ApiResult<?> getFaqInfoByUser() throws Exception {
		return customerFaqMallService.getFaqInfoByUser();
	}

	@PostMapping(value = "/customer/faq/addFaqViews")
	@ApiOperation(value = "FAQ 게시글 조회수 증가")
	public ApiResult<?> addFaqViews(Long csFaqId) throws Exception {
		return customerFaqMallService.addFaqViews(csFaqId);
	}
}
