package kr.co.pulmuone.bos.order.regular;

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
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.order.order.dto.OrderListResponseDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularReqListRequestDto;
import kr.co.pulmuone.v1.order.regular.service.OrderRegularBiz;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송주문신청리스트 관련 Controller
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 02. 04.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */
@RestController
public class OrderRegularReqListController {


    @Autowired
    private OrderRegularBiz orderRegularBiz;

    @Autowired
    private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

    /**
     * 정기배송 신청 리스트
     *
     * @param orderListRequestDto
     * @return ApiResult<?>
     */
    @ApiOperation(value = "정기배송 신청 리스트", httpMethod = "POST")
	@PostMapping(value = "/admin/order/getOrderRegularReqList")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = OrderListResponseDto.class)
	})
    public ApiResult<?> getOrderRegularReqList(HttpServletRequest request) throws Exception {

		RegularReqListRequestDto regularReqListRequestDto = BindUtil.bindDto(request, RegularReqListRequestDto.class);

        return ApiResult.success(orderRegularBiz.getOrderRegularReqList(regularReqListRequestDto));
    }

    /**
     * 정기배송 신청 리스트 엑셀다운로드
     *
     * @param orderListRequestDto
     * @return ApiResult<?>
     */
    @ApiOperation(value = "정기배송 신청 리스트 엑셀다운로드")
    @PostMapping(value = "/admin/order/getRegularReqExcelList")
    public ModelAndView getRegularReqExcelList(@RequestBody RegularReqListRequestDto regularReqListRequestDto) throws Exception {

        ExcelDownloadDto excelDownloadDto = orderRegularBiz.getOrderRegularReqListExcel(regularReqListRequestDto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }
}
