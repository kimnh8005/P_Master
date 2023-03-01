package kr.co.pulmuone.bos.customer.stndpnt;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
import kr.co.pulmuone.v1.customer.stndpnt.dto.StandingPointRequestDto;
import kr.co.pulmuone.v1.customer.stndpnt.dto.StandingPointResponseDto;
import kr.co.pulmuone.v1.customer.stndpnt.service.StandingPointBiz;

@RestController
public class StandingPointController {

	@Autowired
	private StandingPointBiz standingPointBiz;

	@Autowired(required=true)
	private HttpServletRequest request;

	@Autowired
    private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

	/**
	 * 상품입점상담 관리 조회
	 * @param StandingPointRequestDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "상품입점상담 관리 조회")
	@PostMapping(value = "/admin/customer/stndpnt/getStandingPointList")
	@ApiResponse(code = 900, message = "response data", response = StandingPointResponseDto.class)
	public ApiResult<?> getStandingPointList(StandingPointRequestDto standingPointRequestDto) throws Exception {
		return standingPointBiz.getStandingPointList((StandingPointRequestDto) BindUtil.convertRequestToObject(request, StandingPointRequestDto.class));
	}

    /**
     * 상품입점상담 관리 리스트 엑셀 다운로드 목록 조회
     *
     * @param MasterItemListRequestDto
     * @return ModelAndView
     */
    @ApiOperation(value = "상품입점상담 관리 리스트 엑셀 다운로드 목록 조회")
    @PostMapping(value = "/admin/customer/stndpnt/getStandingPointExportExcel")
    public ModelAndView getStandingPointExportExcel(@RequestBody StandingPointRequestDto standingPointRequestDto) {

        ExcelDownloadDto excelDownloadDto = standingPointBiz.getStandingPointExportExcel(standingPointRequestDto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;

    }




	/**
	 * 상품입점상담 관리 상세조회
	 * @param FeedbackBosRequestDto
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/customer/stndpnt/getDetailStandingPoint")
	@ApiOperation(value = "상품입점상담 관리 상세조회")
	@ResponseBody
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = FeedbackBosDetailResponseDto.class)
	})
	public ApiResult<?> getDetailStandingPoint(StandingPointRequestDto standingPointRequestDto) throws Exception {
		return standingPointBiz.getDetailStandingPoint(standingPointRequestDto);
	}


//

	/**
	 * 상품입점상담 승인 상태변경
	 * @param QnaBosRequestDto
	 * @return QnaBosDetailResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/customer/stndpnt/putStandingPointStatus")
	@ApiOperation(value = "상품입점상담 승인 상태변경")
	@ResponseBody
	public ApiResult<?> putStandingPointStatus(StandingPointRequestDto standingPointRequestDto) throws Exception {
		return standingPointBiz.putStandingPointStatus(standingPointRequestDto);
	}

}
