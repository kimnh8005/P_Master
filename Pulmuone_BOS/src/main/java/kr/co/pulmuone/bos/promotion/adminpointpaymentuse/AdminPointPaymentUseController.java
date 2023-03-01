package kr.co.pulmuone.bos.promotion.adminpointpaymentuse;

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
import kr.co.pulmuone.v1.promotion.adminpointpaymentuse.dto.AdminPointPaymentUseListRequestDto;
import kr.co.pulmuone.v1.promotion.adminpointpaymentuse.dto.AdminPointPaymentUseListResponseDto;
import kr.co.pulmuone.v1.promotion.adminpointpaymentuse.service.AdminPointPaymentUseBiz;

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
 *  1.0    20201012		       안치열            최초작성
 * =======================================================================
 * </PRE>
 */

@RestController
public class AdminPointPaymentUseController {

	@Autowired
	private AdminPointPaymentUseBiz adminPointPaymentUseBiz;

	@Autowired(required=true)
	private HttpServletRequest request;

	@Autowired
	private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

	/**
	 * 관리자 적립금 지급/차감 내역 리스트조회
	 */
	@PostMapping(value = "/admin/promotion/adminPointPaymentUse/getAdminPointPaymentUseList")
	@ApiOperation(value = "관리자 적립금 지급/차감 내역 리스트조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = AdminPointPaymentUseListResponseDto.class)
	})
	public ApiResult<?> getAdminPointPaymentUseList(AdminPointPaymentUseListRequestDto adminPointPaymentUseListRequestDto) throws Exception {
		return adminPointPaymentUseBiz.getAdminPointPaymentUseList((AdminPointPaymentUseListRequestDto) BindUtil.convertRequestToObject(request, AdminPointPaymentUseListRequestDto.class));
	}



	/**
	 * 관리자 적립금 지급/차감 내역 엑셀 선택 다운로드
	 * @param model
	 * @return
	 */
	@PostMapping(value = "/admin/promotion/adminPointPaymentUse/adminPointPaymentUseListExportExcel")
	public ModelAndView adminPointPaymentUseListExportExcel(@RequestBody AdminPointPaymentUseListRequestDto adminPointPaymentUseListRequestDto) throws Exception {

		ModelAndView modelAndView = new ModelAndView(excelDownloadView);
		modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, adminPointPaymentUseBiz.adminPointPaymentUseListExportExcel(adminPointPaymentUseListRequestDto));

		return modelAndView;
	}
}
