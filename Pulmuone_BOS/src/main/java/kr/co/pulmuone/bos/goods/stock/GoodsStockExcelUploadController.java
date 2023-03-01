package kr.co.pulmuone.bos.goods.stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.goods.stock.dto.StockExcelUploadListRequestDto;
import kr.co.pulmuone.v1.goods.stock.dto.StockExcelUploadRequestDto;
import kr.co.pulmuone.v1.goods.stock.dto.StockExcelUploadResponseDto;
import kr.co.pulmuone.v1.goods.stock.service.GoodsStockExcelUploadBiz;
import lombok.RequiredArgsConstructor;

/**
 * <PRE>
 * Forbiz Korea
 * ERP 재고 엑셀 업로드
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0   2020.11.11	    이성준            최초작성
 * =======================================================================
 * </PRE>
 */
@RestController
@RequiredArgsConstructor
public class GoodsStockExcelUploadController {

	private final GoodsStockExcelUploadBiz goodsStockExcelUploadBiz;

	@Autowired
    private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

	@ApiOperation(value = "ERP 재고 엑셀 업로드")
	@PostMapping(value = "/admin/goods/stock/addExcelUpload")
	@ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = StockExcelUploadResponseDto.class)
	})
	public ApiResult<?> addExcelUpload(StockExcelUploadRequestDto dto) throws Exception {

		dto.setUploadList(BindUtil.convertJsonArrayToDtoList(dto.getUpload(), StockExcelUploadListRequestDto.class));

		return goodsStockExcelUploadBiz.addExcelUpload(dto);
	}

	@ApiOperation(value = "용인물류 유통기한별 재고리스트 엑셀 다운로드")
    @PostMapping(value = "/admin/goods/stock/getStockExprListExportExcel")
    public ModelAndView getStockExprListExportExcel() {

        ExcelDownloadDto excelDownloadDto = goodsStockExcelUploadBiz.getStockExprListExportExcel();

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }

}
