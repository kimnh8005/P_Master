package kr.co.pulmuone.bos.promotion.notissue;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.promotion.notissue.dto.PointNotIssueListRequestDto;
import kr.co.pulmuone.v1.promotion.notissue.dto.PointNotIssueListResponseDto;
import kr.co.pulmuone.v1.promotion.notissue.service.PointNotIssueBiz;
import kr.co.pulmuone.v1.promotion.point.service.PointBiz;

@RestController
public class PromotionPointNotIssueController {

	@Autowired
	private PointNotIssueBiz pointNotIssueBiz;

	@Autowired(required=true)
	private HttpServletRequest request;

	@Autowired
	private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

	@Autowired
	private PointBiz pointBiz;

	/**
	 * 적립금 미지급 내역 리스트조회
	 */
	@PostMapping(value = "/admin/promotion/notissue/getPointNotIssueList")
	@ApiOperation(value = "적립금 미지급 내역 리스트조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = PointNotIssueListResponseDto.class)
	})
	public ApiResult<?> getPointNotIssueList(PointNotIssueListRequestDto pointNotIssueListRequestDto) throws Exception {
		return pointNotIssueBiz.getPointNotIssueList((PointNotIssueListRequestDto) BindUtil.convertRequestToObject(request, PointNotIssueListRequestDto.class));
	}

	/**
	 * 적립금 미지급 내역 엑셀 선택 다운로드
	 * @param model
	 * @return
	 */
	@PostMapping(value = "/admin/promotion/notissue/getPointNotIssueListExportExcel")
	public ModelAndView getPointNotIssueListExportExcel(@RequestBody PointNotIssueListRequestDto pointNotIssueListRequestDto) throws Exception {

		ModelAndView modelAndView = new ModelAndView(excelDownloadView);
		modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, pointNotIssueBiz.getPointNotIssueListExportExcel(pointNotIssueListRequestDto));

		return modelAndView;
	}


	/**
	 * 단건 미지급 적립금 지급처리
	 *
	 * @param
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "단건 미지급 적립금 지급처리")
	@PostMapping(value = "/admiun/promotion/notissue/depositNotIssuePoints")
	public ApiResult<?> depositNotIssuePoints(Long pmPointNotIssueId, Long urUserId) throws Exception {
		return pointBiz.depositNotIssuePoints(pmPointNotIssueId, urUserId);
	}
}
