package kr.co.pulmuone.bos.item.master;

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
import kr.co.pulmuone.v1.goods.item.dto.MasterItemListRequestDto;
import kr.co.pulmuone.v1.goods.item.service.GoodsItemBiz;

/**
 * <PRE>
* Forbiz Korea
* 마스터 품목 리스트 Controller
 * </PRE>
 *
 * <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 10. 21.               박주형         최초작성
* =======================================================================
 * </PRE>
 */
@RestController
public class GoodsItemListController {

    @Autowired
    private GoodsItemBiz goodsItemBiz;

    @Autowired
    private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

    /**
     * 마스터품목 리스트 조회
     *
     * @param MasterItemListRequestDto
     * @return ApiResult<?>
     */
    @ApiOperation(value = "마스터품목 리스트 조회")
    @PostMapping(value = "/admin/item/master/getItemList")
    public ApiResult<?> getItemList(HttpServletRequest request, MasterItemListRequestDto masterItemListRequestDto) throws Exception {

        masterItemListRequestDto = BindUtil.bindDto(request, MasterItemListRequestDto.class);
        return goodsItemBiz.getItemList(masterItemListRequestDto);

    }

    /**
     * 마스터 품목 리스트 엑셀 다운로드 목록 조회
     *
     * @param MasterItemListRequestDto
     * @return ModelAndView
     */
    @ApiOperation(value = "마스터 품목 리스트 엑셀 다운로드 목록 조회")
    @PostMapping(value = "/admin/item/master/itemExportExcel")
    public ModelAndView itemExportExcel(@RequestBody MasterItemListRequestDto masterItemListRequestDto) {

        ExcelDownloadDto excelDownloadDto = goodsItemBiz.getItemListExcel(masterItemListRequestDto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;

    }

    /**
     * 마스터품목 리스트 조회
     *
     * @param MasterItemListRequestDto
     * @return ApiResult<?>
     */
    @ApiOperation(value = "마스터품목 리스트 조회")
    @PostMapping(value = "/admin/item/master/getItemGoodsPackageList")
    public ApiResult<?> getItemGoodsPackageList(HttpServletRequest request, MasterItemListRequestDto masterItemListRequestDto) throws Exception {

        masterItemListRequestDto = BindUtil.bindDto(request, MasterItemListRequestDto.class);
        return goodsItemBiz.getItemGoodsPackageList(masterItemListRequestDto);

    }



}
