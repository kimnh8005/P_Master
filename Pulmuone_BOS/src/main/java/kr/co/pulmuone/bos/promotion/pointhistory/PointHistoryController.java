package kr.co.pulmuone.bos.promotion.pointhistory;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.api.constant.LegalTypes;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.promotion.pointhistory.dto.PointAdminInfoResponseDto;
import kr.co.pulmuone.v1.promotion.pointhistory.dto.PointDetailHistoryRequestDto;
import kr.co.pulmuone.v1.promotion.pointhistory.dto.PointDetailHistoryResponseDto;
import kr.co.pulmuone.v1.promotion.pointhistory.dto.PointHistoryListRequestDto;
import kr.co.pulmuone.v1.promotion.pointhistory.service.PointHistoryBiz;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class PointHistoryController {

	@Autowired
	private PointHistoryBiz pointHistoryBiz;

	@Autowired(required=true)
	private HttpServletRequest request;

	@Autowired
	private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

	/**
	 * 적립금 내역 리스트조회
	 */
	@PostMapping(value = "/admin/promotion/pointHistory/getPointHistoryList")
	@ApiOperation(value = "적립금 내역 리스트조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = PointDetailHistoryResponseDto.class)
	})
	public ApiResult<?> getPointHistoryList(PointHistoryListRequestDto pointHistoryListRequestDto) throws Exception {
		return pointHistoryBiz.getPointNormalHistoryList((PointHistoryListRequestDto) BindUtil.convertRequestToObject(request, PointHistoryListRequestDto.class));
	}

	/**
	 * 올가 적립금 이전 내역 리스트조회
	 */
	@PostMapping(value = "/admin/promotion/pointHistory/getOrgaPointHistoryList")
	@ApiOperation(value = "올가 적립금 이전 내역 리스트조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = PointHistoryListRequestDto.class)
	})
	public ApiResult<?> getOrgaPointHistoryList(PointHistoryListRequestDto pointHistoryListRequestDto) throws Exception {
		return pointHistoryBiz.getPointHistoryList((PointHistoryListRequestDto) BindUtil.convertRequestToObject(request, PointHistoryListRequestDto.class));
	}

	/**
	 * 적립금 상세 내역 리스트조회
	 */
	@PostMapping(value = "/admin/promotion/pointHistory/getPointDetailHistory")
	@ApiOperation(value = "적립금 상세 내역 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = PointDetailHistoryResponseDto.class)
	})
	public ApiResult<?> getPointDetailHistory(PointDetailHistoryRequestDto pointDetailHistoryRequestDto) throws Exception {
		return pointHistoryBiz.getPointDetailHistory(pointDetailHistoryRequestDto);
	}

	/**
	 * 로그인 정보 조회
	 */
	@PostMapping(value = "/admin/promotion/pointHistory/getLoginInfo")
	@ApiOperation(value = "로그인 정보 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = PointAdminInfoResponseDto.class)
	})
	public ApiResult<?> getLoginInfo(PointDetailHistoryRequestDto pointDetailHistoryRequestDto) throws Exception {
		return pointHistoryBiz.getLoginInfo(pointDetailHistoryRequestDto);
	}

	/**
	 * 적립금 내역 엑셀 다운로드
	 * @param model
	 * @return
	 */
	@PostMapping(value = "/admin/promotion/pointHistory/getPointHistoryListExportExcel")
	public ModelAndView getPointHistoryListExportExcel(@RequestBody PointHistoryListRequestDto pointHistoryListRequestDto) throws Exception {
		String legalType = "";
		ModelAndView modelAndView = new ModelAndView(excelDownloadView);
		modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, pointHistoryBiz.getPointHistoryListExportExcel(pointHistoryListRequestDto, legalType));

		return modelAndView;
	}

	/**
	 * 올가 적립금 내역 엑셀 다운로드
	 * @param pointHistoryListRequestDto
	 * @return
	 */
	@PostMapping(value = "/admin/promotion/pointHistory/getOrgaPointHistoryListExportExcel")
	public ModelAndView getOrgaPointHistoryListExportExcel(@RequestBody PointHistoryListRequestDto pointHistoryListRequestDto) throws Exception {
		String legalType = LegalTypes.ORGA.getCode();
		ModelAndView modelAndView = new ModelAndView(excelDownloadView);
		modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, pointHistoryBiz.getPointHistoryListExportExcel(pointHistoryListRequestDto, legalType));

		return modelAndView;
	}

	/**
	 * 적립금 내역 리스트조회
	 */
//	@PostMapping(value = "/admin/promotion/pointHistory/getTotalPointHistory")
//	@ApiOperation(value = "적립금 내역 리스트조회")
//	@ApiResponses(value = {
//			@ApiResponse(code = 900, message = "response data", response = PointDetailHistoryResponseDto.class)
//	})
//	public ApiResult<?> getTotalPointHistory(PointHistoryListRequestDto pointHistoryListRequestDto) throws Exception {
//		return pointHistoryBiz.getTotalPointHistory(pointHistoryListRequestDto);
//	}

	/**
	 * 올가 적립금 내역 리스트조회
	 */
	@PostMapping(value = "/admin/promotion/pointHistory/getTotalOrgaPointHistory")
	@ApiOperation(value = "적립금 내역 리스트조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = PointDetailHistoryResponseDto.class)
	})
	public ApiResult<?> getTotalOrgaPointHistory(PointHistoryListRequestDto pointHistoryListRequestDto) throws Exception {
		return pointHistoryBiz.getTotalPointHistory(pointHistoryListRequestDto);
	}
}
