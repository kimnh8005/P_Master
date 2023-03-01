package kr.co.pulmuone.bos.order.document;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.base.dto.vo.GetCodeListResultVo;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.enums.OrderEnums.OrderExcelNm;
import kr.co.pulmuone.v1.comm.enums.OrderEnums.OrderStatus;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.order.order.dto.*;
import kr.co.pulmuone.v1.order.order.service.OrderListBiz;
import kr.co.pulmuone.v1.order.order.service.OrderOrderBiz;
import kr.co.pulmuone.v1.user.buyer.dto.GetBuyerListRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * <PRE>
 * Forbiz Korea
 * 현금영수증 발급 관련 Controller
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 05. 20.            천혜현         최초작성
 *
 * =======================================================================
 * </PRE>
 */
@RestController
public class CashReceiptController {

	@Autowired
	private OrderOrderBiz orderOrderBiz;

	@Autowired
	private ExcelDownloadView  excelDownloadView; // 엑셀 다운로드 뷰

	/**
	 * 현금영수증 발급내역 조회
	 *
	 * @param cashReceiptIssuedListRequestDto
	 * @return ApiResult<?>
	 */
	@ApiOperation(value = "현금영수증 발급내역 조회", httpMethod = "POST")
	@PostMapping(value = "/admin/order/document/getCashReceiptIssuedList")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = CashReceiptIssuedListResponseDto.class)
	})
	public ApiResult<?> getCashReceiptIssuedList(HttpServletRequest request, CashReceiptIssuedListRequestDto cashReceiptIssuedListRequestDto) throws Exception {
		return ApiResult.success(orderOrderBiz.getCashReceiptIssuedList(BindUtil.bindDto(request, CashReceiptIssuedListRequestDto.class)));
	}

	/**
	 * 현금영수증 발급
	 *
	 * @param cashReceiptIssueRequestDto
	 * @return ApiResult<?>
	 */
	@ApiOperation(value = "현금영수증 발급", httpMethod = "POST")
	@PostMapping(value = "/admin/order/document/cashReceiptIssue")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>")
	})
	public ApiResult<?> cashReceiptIssue(CashReceiptIssueRequestDto cashReceiptIssueRequestDto) throws Exception {
		return ApiResult.success(orderOrderBiz.cashReceiptIssue(cashReceiptIssueRequestDto));
	}

	/**
	 * 현금영수증 발급내역 엑셀다운로드
	 *
	 * @param cashReceiptIssuedListRequestDto
	 * @return GetBuyerListResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/order/document/getCashReceiptIssuedListExportExcel")
	@ApiOperation(value = "현금영수증 발급내역 엑셀다운로드 엑셀다운로드", httpMethod = "POST", notes = "현금영수증 발급내역 엑셀다운로드 엑셀다운로드")
	public ModelAndView getCashReceiptIssuedListExportExcel(@RequestBody CashReceiptIssuedListRequestDto cashReceiptIssuedListRequestDto) throws Exception {

		ModelAndView modelAndView = new ModelAndView(excelDownloadView);
		modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel,
				orderOrderBiz.getCashReceiptIssuedListExportExcel(cashReceiptIssuedListRequestDto));

		return modelAndView;
	}

}
