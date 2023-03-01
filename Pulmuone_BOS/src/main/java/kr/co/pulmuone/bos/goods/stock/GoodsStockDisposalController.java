package kr.co.pulmuone.bos.goods.stock;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.base.service.StComnBiz;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.goods.stock.dto.GoodsStockDisposalRequestDto;
import kr.co.pulmuone.v1.goods.stock.service.GoodsStockDisposalBiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GoodsStockDisposalController {

    private final HttpServletRequest request;
    private final ExcelDownloadView excelDownloadView;
    private final GoodsStockDisposalBiz goodsStockDisposalBiz;

    /**
     * 유통기한별 재고 리스트 조회
     * @param goodsStockDisposalRequestDto
     * @return
     */
    @ApiOperation(value = "유통기한별 재고 리스트 조회")
    @PostMapping(value = "/admin/goods/stock/getGoodsStockDisposalList")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "request data", response = GoodsStockDisposalRequestDto.class),
            @ApiResponse(code = 901, message = "" + "DUPLICATE_DATA - 중복된 데이터가 존재합니다.")
    })
    public ApiResult<?> getGoodsStockDisposalList(GoodsStockDisposalRequestDto goodsStockDisposalRequestDto) throws Exception {
        GoodsStockDisposalRequestDto bindDto = (GoodsStockDisposalRequestDto) BindUtil.convertRequestToObject(request, GoodsStockDisposalRequestDto.class);
        return goodsStockDisposalBiz.getGoodsStockDisposalList(bindDto);
    }


    /**
     * 유통기한별 재고 리스트 엑셀조회
     * @param goodsStockDisposalRequestDto
     * @return
     */
    @ApiOperation(value = "유통기한별 재고 리스트 엑셀조회")
    @PostMapping(value = "/admin/goods/stock/getGoodsStockDisposalExcelList")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "request data", response = GoodsStockDisposalRequestDto.class),
            @ApiResponse(code = 901, message = "" + "DUPLICATE_DATA - 중복된 데이터가 존재합니다.")
    })
    public ModelAndView getGoodsStockDisposalExcelList(@RequestBody GoodsStockDisposalRequestDto goodsStockDisposalRequestDto) {
        ExcelDownloadDto excelDownloadDto = goodsStockDisposalBiz.getGoodsStockDisposalExcelList(goodsStockDisposalRequestDto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }

}
