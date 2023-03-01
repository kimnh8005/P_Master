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
import kr.co.pulmuone.v1.goods.goods.dto.GoodsPackageListRequestDto;
import kr.co.pulmuone.v1.goods.goods.service.GoodsPackageListBiz;
import kr.co.pulmuone.v1.goods.item.dto.OrgaDiscountRequestDto;
import lombok.RequiredArgsConstructor;

/**
* <PRE>
* Forbiz Korea
* 묶음 상품 조회 Controller
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 11. 20.                정형진          최초작성
* =======================================================================
* </PRE>
*/
@RestController
@RequiredArgsConstructor
public class GoodsPackageListController {

	private final GoodsPackageListBiz goodsPackageList;
	@Autowired
    private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

	@ApiOperation(value = "묶음 상품 목록 조회")
    @PostMapping(value = "/admin/goods/list/getGoodsPackageList")
    public ApiResult<?> getGoodsPackageList(HttpServletRequest request, GoodsPackageListRequestDto paramDto) throws Exception{

	    return goodsPackageList.getGoodsPackageList(BindUtil.bindDto(request, GoodsPackageListRequestDto.class));
    }

	@ApiOperation(value = "묶음 상품 목록 상세 조회")
    @PostMapping(value = "/admin/goods/list/getGoodsPackageDetailList")
    public ApiResult<?> getGoodsPackageDetailList(HttpServletRequest request, GoodsPackageListRequestDto paramDto) throws Exception{

	    return goodsPackageList.getGoodsPackageDetailList(BindUtil.bindDto(request, GoodsPackageListRequestDto.class));
    }

	@ApiOperation(value = "묶음 상품 엑셀 다운로드 목록 조회")
    @PostMapping(value = "/admin/goods/list/goodPackageExportExcel")
    public ModelAndView goodPackageExportExcel(@RequestBody GoodsPackageListRequestDto paramDto) {

        ExcelDownloadDto excelDownloadDto = goodsPackageList.getGoodsPackageExcelList(paramDto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;

    }

}
