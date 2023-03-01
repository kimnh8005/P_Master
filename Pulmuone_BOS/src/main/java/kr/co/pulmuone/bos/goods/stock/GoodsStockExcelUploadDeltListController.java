package kr.co.pulmuone.bos.goods.stock;

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
import kr.co.pulmuone.v1.goods.stock.dto.StockExcelUploadDetlListRequestDto;
import kr.co.pulmuone.v1.goods.stock.dto.StockExcelUploadDetlListResponseDto;
import kr.co.pulmuone.v1.goods.stock.service.GoodsStockExcelUploadDetlListBiz;
import lombok.RequiredArgsConstructor;

/**
 * <PRE>
 * Forbiz Korea
 * ERP 재고 엑셀 업로드 상세내역
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0   2020.11.23	    이성준            최초작성
 * =======================================================================
 * </PRE>
 */
@RestController
@RequiredArgsConstructor
public class GoodsStockExcelUploadDeltListController {

	private final GoodsStockExcelUploadDetlListBiz goodsStockExcelUploadDetlListBiz;

	@Autowired
    private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

	@Autowired(required=true)
	private HttpServletRequest request;

	@ApiOperation(value = "ERP 재고 엑셀 업로드 상세내역 조회")
	@PostMapping(value = "/admin/goods/stock/getStockUploadDetlList")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = StockExcelUploadDetlListResponseDto.class)
	})
	public ApiResult<?> getStockUploadDetlList(StockExcelUploadDetlListRequestDto dto) throws Exception {

		return goodsStockExcelUploadDetlListBiz.getStockUploadDetlList(BindUtil.bindDto(request, StockExcelUploadDetlListRequestDto.class));
	}

    @ApiOperation(value = "ERP 재고 엑셀 업로드 상세내역 엑셀 다운로드 목록 조회")
    @PostMapping(value = "/admin/goods/stock/getStockUploadDetlExportExcel")
    public ModelAndView getStockUploadDetlExcel(@RequestBody StockExcelUploadDetlListRequestDto dto) {

        ExcelDownloadDto excelDownloadDto = goodsStockExcelUploadDetlListBiz.getStockUploadDetlExcel(dto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;

    }

}
