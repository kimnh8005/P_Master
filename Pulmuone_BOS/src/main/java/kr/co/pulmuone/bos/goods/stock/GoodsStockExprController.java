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
import kr.co.pulmuone.v1.goods.stock.dto.StockExprRequestDto;
import kr.co.pulmuone.v1.goods.stock.dto.StockExprResponseDto;
import kr.co.pulmuone.v1.goods.stock.service.GoodsStockExprBiz;
import lombok.RequiredArgsConstructor;

/**
 * <PRE>
 * Forbiz Korea
 * 유통기한별 재고 연동 내역 관리
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0   2020.12.08	    이성준            최초작성
 * =======================================================================
 * </PRE>
 */
@RestController
@RequiredArgsConstructor
public class GoodsStockExprController {

	private final GoodsStockExprBiz goodsStockExprBiz;

	@Autowired(required=true)
	private HttpServletRequest request;

	@Autowired
    private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

	@ApiOperation(value = "유통기한별 재고 연동 내역 관리 조회")
	@PostMapping(value = "/admin/goods/stock/getStockExprList")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = StockExprResponseDto.class)
	})
	public ApiResult<?> getStockExprList(StockExprRequestDto dto) throws Exception {
		return goodsStockExprBiz.getStockExprList(BindUtil.bindDto(request, StockExprRequestDto.class));
	}

	@ApiOperation(value = "통합ERP 재고 연동 내역 관리 조회")
	@PostMapping(value = "/admin/goods/stock/getStockErpList")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = StockExprResponseDto.class)
	})
	public ApiResult<?> getStockErpList(StockExprRequestDto dto) throws Exception {
		return goodsStockExprBiz.getStockErpList(BindUtil.bindDto(request, StockExprRequestDto.class));
	}

	@ApiOperation(value = "재고 미연동 품목리스트 조회")
	@PostMapping(value = "/admin/goods/stock/getStockNonErpList")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = StockExprResponseDto.class)
	})
	public ApiResult<?> getStockNonErpList(StockExprRequestDto dto) throws Exception {
		return goodsStockExprBiz.getStockNonErpList(BindUtil.bindDto(request, StockExprRequestDto.class));
	}

	@ApiOperation(value = "재고 미연동 품목리스트 - 재고수정")
	@PostMapping(value = "/admin/goods/stock/putStockNonErp")
	@ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = StockExprResponseDto.class)
	})
	public ApiResult<?> putStockNonErp(StockExprRequestDto dto) throws Exception {
		return goodsStockExprBiz.putStockNonErp(dto);
	}

	@ApiOperation(value = "유통기한별 재고 연동 내역 관리 엑셀 다운로드")
    @PostMapping(value = "/admin/goods/stock/getStockExprExportExcel")
    public ModelAndView getStockExprExportExcel(@RequestBody StockExprRequestDto dto) {

        ExcelDownloadDto excelDownloadDto = goodsStockExprBiz.getStockExprExportExcel(dto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;

    }

	@ApiOperation(value = "통합ERP 재고 연동 내역 관리 엑셀 다운로드")
    @PostMapping(value = "/admin/goods/stock/getStockErpExportExcel")
    public ModelAndView getStockErpExportExcel(@RequestBody StockExprRequestDto dto) {

        ExcelDownloadDto excelDownloadDto = goodsStockExprBiz.getStockErpExportExcel(dto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }

	@ApiOperation(value = "재고 미연동 품목리스트 엑셀 다운로드")
    @PostMapping(value = "/admin/goods/stock/getStockNonErpExportExcel")
    public ModelAndView getStockNonErpExportExcel(@RequestBody StockExprRequestDto dto) {

        ExcelDownloadDto excelDownloadDto = goodsStockExprBiz.getStockNonErpExportExcel(dto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }

}
