package kr.co.pulmuone.bos.customer.faq;

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
import kr.co.pulmuone.v1.customer.faq.dto.FaqBosRequestDto;
import kr.co.pulmuone.v1.customer.faq.dto.FaqBosResponseDto;
import kr.co.pulmuone.v1.customer.faq.service.CustomerFaqBiz;
import kr.co.pulmuone.v1.customer.feedback.dto.FeedbackBosDetailResponseDto;

@RestController
public class FaqController {

	@Autowired
	private CustomerFaqBiz faqBiz;

	@Autowired(required=true)
	private HttpServletRequest request;

	/**
	 * FAQ 관리  조회
	 * @param FaqBosRequestDto
	 * @return FaqBosResponseDto
	 * @throws Exception
	 */
	@ApiOperation(value = "후기관리  조회")
	@PostMapping(value = "/admin/customer/faq/getFaqList")
	@ApiResponse(code = 900, message = "response data", response = FaqBosResponseDto.class)
	public ApiResult<?> getFaqList(FaqBosRequestDto faqBosRequestDto) throws Exception {
		return faqBiz.getFaqList((FaqBosRequestDto) BindUtil.convertRequestToObject(request, FaqBosRequestDto.class));
	}

	/**
	 * FAQ 신규 등록
	 * @param FaqBosRequestDto
	 * @return FaqBosResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/customer/faq/addFaqInfo")
	@ApiOperation(value = "FAQ 신규 등록")
	@ResponseBody
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = FaqBosResponseDto.class),
    })
	public ApiResult<?> addFaqInfo(FaqBosRequestDto faqBosRequestDto) throws Exception {

		return faqBiz.addFaqInfo(faqBosRequestDto);
	}

	/**
	 * FAQ 수정
	 * @param FaqBosRequestDto
	 * @return FaqBosResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/customer/faq/putFaqInfo")
	@ApiOperation(value = "FAQ 수정")
	@ResponseBody
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = FaqBosResponseDto.class),
    })
	public ApiResult<?> putFaqInfo(FaqBosRequestDto faqBosRequestDto) throws Exception {

		return faqBiz.putFaqInfo(faqBosRequestDto);
	}

	/**
	 * FAQ 삭제
	 * @param FaqBosRequestDto
	 * @return FaqBosResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/customer/faq/deleteFaqInfo")
	@ApiOperation(value = "FAQ 삭제")
	@ResponseBody
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = FaqBosResponseDto.class),
    })
	public ApiResult<?> deleteFaqInfo(FaqBosRequestDto faqBosRequestDto) throws Exception {

		return faqBiz.deleteFaqInfo(faqBosRequestDto);
	}



	/**
	 * FAQ 관리 상세조회
	 * @param FeedbackBosRequestDto
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/customer/faq/getDetailFaq")
	@ApiOperation(value = "FAQ 관리 상세조회")
	@ResponseBody
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = FeedbackBosDetailResponseDto.class)
	})
	public ApiResult<?> getDetailFaq(FaqBosRequestDto faqBosRequestDto) throws Exception {
		return faqBiz.getDetailFaq(faqBosRequestDto);
	}

}
