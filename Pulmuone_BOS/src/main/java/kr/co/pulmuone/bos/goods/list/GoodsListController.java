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
import kr.co.pulmuone.v1.goods.goods.dto.GoodsListRequestDto;
import kr.co.pulmuone.v1.goods.goods.service.GoodsListBiz;
import lombok.RequiredArgsConstructor;

/**
* <PRE>
* Forbiz Korea
* 상품리스트 Controller
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 10. 05.                손진구          최초작성
* =======================================================================
* </PRE>
*/
@RestController
@RequiredArgsConstructor
public class GoodsListController {

    private final GoodsListBiz goodsListBiz;

    @Autowired
    private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

	@ApiOperation(value = "상품 목록 조회")
    @PostMapping(value = "/admin/goods/list/getGoodsList")
    public ApiResult<?> getGoodsList(HttpServletRequest request, GoodsListRequestDto goodsListRequestDto) throws Exception{

	    return goodsListBiz.getGoodsList(BindUtil.bindDto(request, GoodsListRequestDto.class));
    }

	@ApiOperation(value = "상품목록 판매상태 변경")
    @PostMapping(value = "/admin/goods/list/putGoodsListSaleStatusChange")
    public ApiResult<?> putGoodsListSaleStatusChange(@RequestBody GoodsListRequestDto goodsListRequestDto) throws Exception{

	    return goodsListBiz.putGoodsListSaleStatusChange(goodsListRequestDto);
    }

	/**
     * 상품 엑셀 다운로드 목록 조회
     *
     * @param MasterItemListRequestDto
     * @return ModelAndView
     */
    @ApiOperation(value = "상품 리스트 엑셀 다운로드 목록 조회")
    @PostMapping(value = "/admin/goods/list/goodsExportExcel")
    public ModelAndView goodsExportExcel(@RequestBody GoodsListRequestDto goodsListRequestDto) {

        ExcelDownloadDto excelDownloadDto = goodsListBiz.getGoodsListExcel(goodsListRequestDto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }

}