package kr.co.pulmuone.bos.customer.qna;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.api.ezadmin.dto.EZAdminResponseDefaultDto;
import kr.co.pulmuone.v1.api.ezadmin.service.EZAdminBiz;
import kr.co.pulmuone.v1.base.dto.vo.GetCodeListResultVo;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.customer.qna.dto.QnaBosDetailResponseDto;
import kr.co.pulmuone.v1.customer.qna.dto.QnaBosRequestDto;
import kr.co.pulmuone.v1.customer.qna.dto.QnaBosResponseDto;
import kr.co.pulmuone.v1.customer.qna.service.OutmallQnaBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
public class OutmallQnaController {
	@Autowired
	private EZAdminBiz ezAdminBiz;

	@Autowired
	private OutmallQnaBiz outmallQnaBiz;

	@Autowired(required=true)
	private HttpServletRequest request;

	@Autowired
    private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

	/**
     *외부몰 문의관리 > 문의채널 조회 EZ Admin API 호출
     * @return EZAdminResponseDefaultDto
     *
     */
	@GetMapping(value = "/admin/customer/qna/getEZAdminEtcShopInfoCheckBox")
	@ApiOperation(value = "외부몰 문의관리 > 문의채널 조회 EZ Admin API 호출", httpMethod = "GET")
	@ApiResponses(value = {
			@ApiResponse(code = 0000, message = "response data", response = EZAdminResponseDefaultDto.class)
	})
	public ApiResult<?> getEZAdminEtcShopInfoCheckBox(){
		return ezAdminBiz.getEtcInfo("shopinfo");
	}

	/**
     *외부몰 문의관리 > 목록 조회 EZ Admin API 호출
     * @return EZAdminResponseDefaultDto
     *
     */
	@PostMapping(value = "/admin/customer/qna/getOutmallQnaList")
	@ApiOperation(value = "외부몰 문의관리 > 목록 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = QnaBosResponseDto.class)
	})
	public ApiResult<?> getOutmallQnaList(QnaBosRequestDto qnaBosRequestDto) throws Exception {
		return outmallQnaBiz.getOutmallQnaList((QnaBosRequestDto) BindUtil.convertRequestToObject(request, QnaBosRequestDto.class));
	}

	 /**
     * 외부몰 문의관리 리스트 엑셀 다운로드 목록 조회
     *
     * @param qnaBosRequestDto
     * @return ModelAndView
     */
    @ApiOperation(value = "외부몰 문의관리 리스트 엑셀 다운로드 목록 조회")
    @PostMapping(value = "/admin/customer/qna/getOutmallQnaExportExcel")
    public ModelAndView getOutmallQnaExportExcel(@RequestBody QnaBosRequestDto qnaBosRequestDto) throws Exception {

        ExcelDownloadDto excelDownloadDto = outmallQnaBiz.getOutmallQnaExportExcel(qnaBosRequestDto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;

    }

    /**
	 * 외부몰 문의관리 상세조회
	 * @param qnaBosRequestDto
	 * @return QnaBosDetailResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/customer/qna/getOutmallQnaDetail")
	@ApiOperation(value = "외부몰문의관리 상세조회")
	@ResponseBody
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = QnaBosDetailResponseDto.class)
	})
	public ApiResult<?> getOutmallQnaDetail(QnaBosRequestDto qnaBosRequestDto) throws Exception {
		return outmallQnaBiz.getOutmallQnaDetail(qnaBosRequestDto);
	}

	/**
	 * 답변진행 상태변경
	 * @param qnaBosRequestDto
	 * @return QnaBosDetailResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/customer/qna/putOutmallQnaAnswerStatus")
	@ApiOperation(value = "답변진행 상태변경")
	@ResponseBody
	public ApiResult<?> putOutmallQnaAnswerStatus(QnaBosRequestDto qnaBosRequestDto) throws Exception {
		return outmallQnaBiz.putOutmallQnaAnswerStatus(qnaBosRequestDto);
	}

	/**
	 * 외부몰문의 답변정보 수정
	 * @param qnaBosRequestDto
	 * @return QnaBosDetailResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/customer/qna/putOutmallQnaInfo")
	@ApiOperation(value = "외부몰문의 답변정보 수정")
	@ResponseBody
	public ApiResult<?> putOutmallQnaInfo(QnaBosRequestDto qnaBosRequestDto) throws Exception {
		return outmallQnaBiz.putOutmallQnaInfo(qnaBosRequestDto);
	}

	@GetMapping(value = "/admin/customer/qna/getOutmallNameList")
    @ApiOperation(value = "외부몰명 체크박스 리스트")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = GetCodeListResultVo.class)
    })
    public ApiResult<?> getOutmallNameList() throws Exception {
        return outmallQnaBiz.getOutmallNameList();
    }

}
