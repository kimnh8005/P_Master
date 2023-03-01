package kr.co.pulmuone.bos.customer.qna;

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
import kr.co.pulmuone.v1.customer.qna.dto.QnaBosDetailResponseDto;
import kr.co.pulmuone.v1.customer.qna.dto.QnaBosRequestDto;
import kr.co.pulmuone.v1.customer.qna.dto.QnaBosResponseDto;
import kr.co.pulmuone.v1.customer.qna.dto.vo.QnaBosDetailVo;
import kr.co.pulmuone.v1.customer.qna.service.CustomerQnaBiz;

@RestController
public class CustomerQnaController {

	@Autowired
	private CustomerQnaBiz customerQnaBiz;

	@Autowired(required=true)
	private HttpServletRequest request;

	@Autowired
    private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

	/**
	 * 통합몰문의관리  목록조회
	 * @param QnaBosRequestDto
	 * @return QnaBosResponseDto
	 * @throws Exception
	 */
	@ApiOperation(value = "통합몰문의관리  목록조회")
	@PostMapping(value = "/admin/customer/qna/getQnaList")
	@ApiResponse(code = 900, message = "response data", response = QnaBosResponseDto.class)
	public ApiResult<?> getQnaList(QnaBosRequestDto qnaBosRequestDto) throws Exception {
		return customerQnaBiz.getQnaList((QnaBosRequestDto) BindUtil.convertRequestToObject(request, QnaBosRequestDto.class));
	}

    /**
     * 통합몰문의관리 리스트 엑셀 다운로드 목록 조회
     *
     * @param QnaBosRequestDto
     * @return ModelAndView
     */
    @ApiOperation(value = "통합몰문의관리 리스트 엑셀 다운로드 목록 조회")
    @PostMapping(value = "/admin/customer/qna/qnaExportExcel")
    public ModelAndView qnaExportExcel(@RequestBody QnaBosRequestDto qnaBosRequestDto) {

        ExcelDownloadDto excelDownloadDto = customerQnaBiz.qnaExportExcel(qnaBosRequestDto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;

    }

	/**
	 * 통합몰문의관리 상세조회
	 * @param QnaBosRequestDto
	 * @return QnaBosDetailResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/customer/qna/getQnaDetail")
	@ApiOperation(value = "통합몰문의관리 상세조회")
	@ResponseBody
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = QnaBosDetailResponseDto.class)
	})
	public ApiResult<?> getQnaDetail(QnaBosRequestDto qnaBosRequestDto) throws Exception {
		return customerQnaBiz.getQnaDetail(qnaBosRequestDto);
	}


	/**
	 * 답변진행 상태변경
	 * @param QnaBosRequestDto
	 * @return QnaBosDetailResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/customer/qna/putQnaAnswerStatus")
	@ApiOperation(value = "답변진행 상태변경")
	@ResponseBody
	public ApiResult<?> putQnaAnswerStatus(QnaBosRequestDto qnaBosRequestDto) throws Exception {
		return customerQnaBiz.putQnaAnswerStatus(qnaBosRequestDto);
	}


	/**
	 * 문의 답변정보 수정
	 * @param QnaBosRequestDto
	 * @return QnaBosDetailResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/customer/qna/putQnaInfo")
	@ApiOperation(value = "문의 답변정보 수정")
	@ResponseBody
	public ApiResult<?> putQnaInfo(QnaBosRequestDto qnaBosRequestDto) throws Exception {
		return customerQnaBiz.putQnaInfo(qnaBosRequestDto);
	}


	/**
	 * 1:1문의관리 상세 첨부파일 이미지
	 * @param String csQnaId
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "1:1문의관리 상세 첨부파일 이미지")
	@ApiResponses(value = {
		@ApiResponse(code = 900, message = "response data", response = QnaBosDetailVo.class)
	})
	@PostMapping(value = "/admin/customer/qna/getImageList")
	@ResponseBody
	public ApiResult<?> getImageList(@RequestParam(value = "csQnaId", required = true) String csQnaId) throws Exception {
		return customerQnaBiz.getImageList(csQnaId);
	}

}
