package kr.co.pulmuone.bos.goods.stock;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
import kr.co.pulmuone.v1.goods.stock.dto.StockListRequestDto;
import kr.co.pulmuone.v1.goods.stock.dto.StockListResponseDto;
import kr.co.pulmuone.v1.goods.stock.dto.vo.StockListResultVo;
import kr.co.pulmuone.v1.goods.stock.service.GoodsStockListBiz;
import lombok.RequiredArgsConstructor;

/**
 * <PRE>
 * Forbiz Korea
 * 품목별 재고리스트
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0   2020.11.04	    이성준            최초작성
 * =======================================================================
 * </PRE>
 */
@RestController
@RequiredArgsConstructor
public class GoodsStockListController {

	private final GoodsStockListBiz goodsStockListBiz;

	@Autowired(required=true)
	private HttpServletRequest request;

	@Autowired
    private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

	@ApiOperation(value = "품목별 재고리스트 조회")
	@PostMapping(value = "/admin/goods/stock/getStockList")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = StockListResponseDto.class)
	})
	public ApiResult<?> getStockList(StockListRequestDto dto) throws Exception {
		return goodsStockListBiz.getStockList(BindUtil.bindDto(request, StockListRequestDto.class));
	}

	@ApiOperation(value = "품목별 재고리스트 주문정보 조회")
	@PostMapping(value = "/admin/goods/stock/getStockInfo")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = StockListResponseDto.class)
	})
	public ApiResult<?> getStockInfo(StockListRequestDto stockListRequestDto) throws Exception {
		return goodsStockListBiz.getStockInfo(stockListRequestDto);
	}

	@ApiOperation(value = "품목별 재고상세정보 목록 조회")
	@PostMapping(value = "/admin/goods/stock/getStockDetailList")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = StockListResponseDto.class)
	})
	public ApiResult<?> getStockDetailList(StockListRequestDto stockListRequestDto) throws Exception {
		return goodsStockListBiz.getStockDetailList(stockListRequestDto);
	}

	@ApiOperation(value = "공통 선주문 설정 팝업 정보 조회")
	@PostMapping(value = "/admin/goods/stock/getStockPreOrderPopupInfo")
	@ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = StockListResultVo.class)
	})
	public ApiResult<?> getStockPreOrderPopupInfo(StockListRequestDto stockListRequestDto) throws Exception {
		return goodsStockListBiz.getStockPreOrderPopupInfo(stockListRequestDto);
	}

	@ApiOperation(value = "품목별 재고리스트 선주문 여부 수정")
	@PostMapping(value = "/admin/goods/stock/putStockPreOrder")
	@ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = StockListResponseDto.class)
	})
	public ApiResult<?> putStockPreOrder(StockListRequestDto stockListRequestDto) throws Exception {
		return goodsStockListBiz.putStockPreOrder(stockListRequestDto);
	}

	@ApiOperation(value = "품목별 재고리스트 엑셀 다운로드")
    @PostMapping(value = "/admin/goods/stock/getStockListExportExcel")
    public ModelAndView getStockListExportExcel(@RequestBody StockListRequestDto dto) {

        ExcelDownloadDto excelDownloadDto = goodsStockListBiz.getStockListExportExcel(dto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;

    }

	@ApiOperation(value = "출고기준관리 조회")
	@RequestMapping(value = "/admin/goods/stock/getStockDeadlineDropDownList")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = StockListResponseDto.class)
	})
	@ResponseBody
	public ApiResult<?> getStockDeadlineDropDownList(StockListRequestDto dto) throws Exception {
		return goodsStockListBiz.getStockDeadlineDropDownList(dto);
	}

	@ApiOperation(value = "출고기준정보 수정")
	@PostMapping(value = "/admin/goods/stock/putStockDeadlineInfo")
	@ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = StockListResponseDto.class)
	})
	public ApiResult<?> putStockDeadlineInfo(StockListRequestDto stockListRequestDto) throws Exception {
		return goodsStockListBiz.putStockDeadlineInfo(stockListRequestDto);
	}


	@ApiOperation(value = "재고조정수량 저장/수정")
	@PostMapping(value = "/admin/goods/stock/putStockAdjustCount")
	@ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = StockListResponseDto.class)
	})
	public ApiResult<?> putStockAdjustCount(StockListRequestDto stockListRequestDto) throws Exception {
		return goodsStockListBiz.putStockAdjustCount(stockListRequestDto);
	}

}
