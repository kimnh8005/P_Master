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
import kr.co.pulmuone.v1.goods.goods.dto.GoodsShopListRequestDto;
import kr.co.pulmuone.v1.goods.goods.service.GoodsShopListBiz;
import lombok.RequiredArgsConstructor;

/**
* <PRE>
* Forbiz Korea
* 매장 상품 조회 Controller
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 11. 26.                정형진          최초작성
* =======================================================================
* </PRE>
*/
@RestController
@RequiredArgsConstructor
public class GoodsShopListController {

	private final GoodsShopListBiz goodsShopListBiz;

	@Autowired
    private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

	@ApiOperation(value = "매장전용 상품 목록 조회")
    @PostMapping(value = "/admin/goods/list/getGoodsShopList")
    public ApiResult<?> getGoodsShopList(HttpServletRequest request, GoodsShopListRequestDto paramDto) throws Exception{

	    return goodsShopListBiz.getGoodsShopList(BindUtil.bindDto(request, GoodsShopListRequestDto.class));
    }

	@ApiOperation(value = "매장 상품 엑셀 다운로드 목록 조회")
    @PostMapping(value = "/admin/goods/list/goodShopExportExcel")
    public ModelAndView goodShopExportExcel(@RequestBody GoodsShopListRequestDto paramDto) {

        ExcelDownloadDto excelDownloadDto = goodsShopListBiz.getGoodsShopListExcel(paramDto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;

    }



}
