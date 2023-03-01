package kr.co.pulmuone.bos.customer.feedback;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.customer.feedback.dto.FeedbackBosDetailResponseDto;
import kr.co.pulmuone.v1.customer.feedback.dto.FeedbackBosRequestDto;
import kr.co.pulmuone.v1.customer.feedback.dto.FeedbackBosResponseDto;
import kr.co.pulmuone.v1.customer.feedback.dto.vo.FeedbackBosDetailVo;
import kr.co.pulmuone.v1.customer.feedback.service.FeedbackBiz;

@RestController
public class FeedbackController {

	@Autowired
	private FeedbackBiz feedbackBiz;

	@Autowired(required=true)
	private HttpServletRequest request;

	@Autowired
    private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

	/**
	 * 후기관리  조회
	 * @param FeedbackRequestDto
	 * @return FeedbackResponseDto
	 * @throws Exception
	 */
	@ApiOperation(value = "후기관리  조회")
	@PostMapping(value = "/admin/customer/feedback/getFeedbackList")
	@ApiResponse(code = 900, message = "response data", response = FeedbackBosResponseDto.class)
	public ApiResult<?> getFeedbackList(FeedbackBosRequestDto feedbackBosRequestDto) throws Exception {
		return feedbackBiz.getFeedbackList((FeedbackBosRequestDto) BindUtil.convertRequestToObject(request, FeedbackBosRequestDto.class));
	}


    /**
     * 후기관리 리스트 엑셀 다운로드 목록 조회
     *
     * @param MasterItemListRequestDto
     * @return ModelAndView
     */
    @ApiOperation(value = "후기관리 리스트 엑셀 다운로드 목록 조회")
    @PostMapping(value = "/admin/customer/feedback/feedbackExportExcel")
    public ModelAndView feedbackExportExcel(@RequestBody FeedbackBosRequestDto feedbackBosRequestDto) {

        ExcelDownloadDto excelDownloadDto = feedbackBiz.feedbackExportExcel(feedbackBosRequestDto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;

    }



	/**
	 * 후기관리 상세조회
	 * @param FeedbackBosRequestDto
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/customer/feedback/getDetailFeedback")
	@ApiOperation(value = "후기관리 상세조회")
	@ResponseBody
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = FeedbackBosDetailResponseDto.class)
	})
	public ApiResult<?> getDetailFeedback(FeedbackBosRequestDto feedbackBosRequestDto) throws Exception {
		return feedbackBiz.getDetailFeedback(feedbackBosRequestDto);
	}


	/**
	 * 후기관리 상세 첨부파일 이미지
	 * @param String feedbackId
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "후기관리 상세 첨부파일 이미지")
	@ApiResponses(value = {
		@ApiResponse(code = 900, message = "response data", response = FeedbackBosDetailVo.class)
	})
	@PostMapping(value = "/admin/customer/feedback/getImageList")
	@ResponseBody
	public ApiResult<?> getImageList(@RequestParam(value = "feedbackId", required = true) String feedbackId) throws Exception {
		return feedbackBiz.getImageList(feedbackId);
	}



	/**
	 * 후기관리정보 수정
	 * @param FeedbackBosRequestDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "후기관리정보 수정")
	@PostMapping(value = "/admin/customer/feedback/putFeedbackInfo")
	public ApiResult<?> putFeedbackInfo(FeedbackBosRequestDto feedbackBosRequestDto) throws Exception {

		return feedbackBiz.putFeedbackInfo(feedbackBosRequestDto);
	}

}
