package kr.co.pulmuone.bos.customer.notice;

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
import kr.co.pulmuone.v1.customer.faq.dto.FaqBosResponseDto;
import kr.co.pulmuone.v1.customer.notice.dto.NoticeBosDetailResponseDto;
import kr.co.pulmuone.v1.customer.notice.dto.NoticeBosRequestDto;
import kr.co.pulmuone.v1.customer.notice.dto.NoticeBosResponseDto;
import kr.co.pulmuone.v1.customer.notice.service.CustomerNoticeBiz;

@RestController
public class CustomerNoticeController {

	@Autowired
	private CustomerNoticeBiz customerNoticeBiz;

	@Autowired(required=true)
	private HttpServletRequest request;

	/**
	 * 공지사항 관리  조회
	 * @param NoticeBosRequestDto
	 * @return NoticeBosResponseDto
	 * @throws Exception
	 */
	@ApiOperation(value = "공지사항관리  조회")
	@PostMapping(value = "/admin/customer/notice/getNoticeList")
	@ApiResponse(code = 900, message = "response data", response = NoticeBosResponseDto.class)
	public ApiResult<?> getNoticeList(NoticeBosRequestDto noticeBosRequestDto) throws Exception {
		return customerNoticeBiz.getNoticeList((NoticeBosRequestDto) BindUtil.convertRequestToObject(request, NoticeBosRequestDto.class));
	}


	/**
	 * 공지사항 신규 등록
	 * @param NoticeBosRequestDto
	 * @return NoticeBosResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/customer/notice/addNoticeInfo")
	@ApiOperation(value = "공지사항 신규 등록")
	@ResponseBody
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = NoticeBosResponseDto.class),
    })
	public ApiResult<?> addFaqInfo(NoticeBosRequestDto noticeBosRequestDto) throws Exception {

		return customerNoticeBiz.addNoticeInfo(noticeBosRequestDto);
	}

	/**
	 * 공지사항 수정
	 * @param NoticeBosRequestDto
	 * @return NoticeBosResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/customer/notice/putNoticeInfo")
	@ApiOperation(value = "공지사항 수정")
	@ResponseBody
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = NoticeBosResponseDto.class),
    })
	public ApiResult<?> putNoticeInfo(NoticeBosRequestDto noticeBosRequestDto) throws Exception {

		return customerNoticeBiz.putNoticeInfo(noticeBosRequestDto);
	}

	/**
	 * 공지사항 삭제
	 * @param NoticeBosRequestDto
	 * @return FaqBosResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/customer/notice/deleteNoticeInfo")
	@ApiOperation(value = "공지사항 삭제")
	@ResponseBody
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = FaqBosResponseDto.class),
    })
	public ApiResult<?> deleteNoticeInfo(NoticeBosRequestDto noticeBosRequestDto) throws Exception {

		return customerNoticeBiz.deleteNoticeInfo(noticeBosRequestDto);
	}



	/**
	 * 공지사항 관리 상세조회
	 * @param NoticeBosRequestDto
	 * @return NoticeBosDetailResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/customer/notice/getDetailNotice")
	@ApiOperation(value = "공지사항 관리 상세조회")
	@ResponseBody
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = NoticeBosDetailResponseDto.class)
	})
	public ApiResult<?> getDetailNotice(NoticeBosRequestDto noticeBosRequestDto) throws Exception {
		return customerNoticeBiz.getDetailNotice(noticeBosRequestDto);
	}

	/**
	 * 공지사항 수정내역  조회
	 * @param noticeBosRequestDto
	 * @return NoticeBosResponseDto
	 * @throws Exception
	 */
	@ApiOperation(value = "공지사항 수정내역  조회")
	@PostMapping(value = "/admin/customer/notice/getNoticeHistoryList")
	@ApiResponse(code = 900, message = "response data", response = NoticeBosResponseDto.class)
	public ApiResult<?> getNoticeHistoryList(NoticeBosRequestDto noticeBosRequestDto) throws Exception {
		return customerNoticeBiz.getNoticeHistoryList((NoticeBosRequestDto) BindUtil.convertRequestToObject(request, NoticeBosRequestDto.class));
	}
}
