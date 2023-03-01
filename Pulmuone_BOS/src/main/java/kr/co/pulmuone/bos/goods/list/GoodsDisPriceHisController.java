package kr.co.pulmuone.bos.goods.list;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import io.swagger.annotations.ApiOperation;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsDisPriceHisRequestDto;
import kr.co.pulmuone.v1.goods.goods.service.GoodsDisPriceHisBiz;
import lombok.RequiredArgsConstructor;

/**
* <PRE>
* Forbiz Korea
* 상품 할인 업데이트 조회 Controller
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 12. 01                정형진          최초작성
* =======================================================================
* </PRE>
*/
@RestController
@RequiredArgsConstructor
public class GoodsDisPriceHisController {

	private final GoodsDisPriceHisBiz goodsDisPriceHisBiz;

	@Autowired
    private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

	@ApiOperation(value = "상품 할인 업데이트 조회")
    @PostMapping(value = "/admin/goods/list/getGoodsDisPriceHisList")
    public ApiResult<?> getGoodsDisPriceHisList(HttpServletRequest request, GoodsDisPriceHisRequestDto paramDto) throws Exception{

	    return goodsDisPriceHisBiz.getGoodsDisPriceHisList(BindUtil.bindDto(request, GoodsDisPriceHisRequestDto.class));
    }

	@ApiOperation(value = "묶음 상품 엑셀 다운로드 목록 조회")
    @PostMapping(value = "/admin/goods/list/goodsDisPriceExportExcel")
    public ModelAndView goodsDisPriceExportExcel(@RequestBody GoodsDisPriceHisRequestDto paramDto) {

        ExcelDownloadDto excelDownloadDto = goodsDisPriceHisBiz.getGoodsDisPriceExcelList(paramDto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;

    }



}
