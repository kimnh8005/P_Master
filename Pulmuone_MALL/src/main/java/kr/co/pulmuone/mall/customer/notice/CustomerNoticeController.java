package kr.co.pulmuone.mall.customer.notice;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.customer.notice.dto.GetNoticeByUserResponseDto;
import kr.co.pulmuone.v1.customer.notice.dto.GetNoticeListByUserRequsetDto;
import kr.co.pulmuone.v1.customer.notice.dto.GetNoticeListByUserResponseDto;
import kr.co.pulmuone.mall.customer.notice.service.CustomerNoticeMallService;
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
*  1.0    2020. 11. 27.    최윤지          최초작성
* =======================================================================
* </PRE>
*/

@RestController
@RequiredArgsConstructor
public class CustomerNoticeController {

	@Autowired
	CustomerNoticeMallService customerNoticeMallService;

	@PostMapping(value = "/customer/notice/getNoticeListByUser")
	@ApiOperation(value = "공지사항 리스트 조회")
	@ApiResponses(value = {
	@ApiResponse(code = 900, message = "response data", response = GetNoticeListByUserResponseDto.class) })
	public ApiResult<?> getNoticeListByUser(HttpServletRequest request, GetNoticeListByUserRequsetDto getNoticeListByUserRequsetDto) throws Exception {
		return customerNoticeMallService.getNoticeListByUser(getNoticeListByUserRequsetDto);
	}

	@PostMapping(value = "/customer/notice/getNoticeByUser")
	@ApiOperation(value = "공지사항 상세 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetNoticeByUserResponseDto.class)})
	public ApiResult<?> getNoticeByUser(Long csNoticeId) throws Exception {
		return customerNoticeMallService.getNoticeByUser(csNoticeId);
	}


}
