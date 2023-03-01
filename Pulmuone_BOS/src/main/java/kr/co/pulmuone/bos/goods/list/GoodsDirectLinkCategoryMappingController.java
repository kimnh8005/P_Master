package kr.co.pulmuone.bos.goods.list;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsDirectLinkCategoryMappingRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsDirectLinkCategoryMappingResponseDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsDirectLinkCategoryMappingListVo;
import kr.co.pulmuone.v1.goods.goods.service.GoodsDirectLinkCategoryMappingBiz;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <PRE>
 *
 * 직연동 카테고리 관리 Service
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 12. 27.             송지윤          최초작성
 * =======================================================================
 * </PRE>
 */
@RestController
@RequiredArgsConstructor
public class GoodsDirectLinkCategoryMappingController {

	private final GoodsDirectLinkCategoryMappingBiz goodsDirectLinkCategoryMappingBiz;

    @Autowired
    private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

	@ApiOperation(value = "네이버 표준 카테고리 맵핑 조회")
    @RequestMapping(value = "/admin/goods/list/getGoodsGearCategoryMappingList")
    @ApiResponse(code = 900, message = "response data", response = GoodsDirectLinkCategoryMappingRequestDto.class)
    public ApiResult<?> getGoodsDirectLinkCategoryMappingList(GoodsDirectLinkCategoryMappingRequestDto paramDto) throws Exception{
        System.out.println("# ##################################### #");
        System.out.println("# GoodsDirectLinkCategoryMappingController.getGoodsDirectLinkCategoryMappingList #");
        System.out.println("# ##################################### #");
	    return ApiResult.success(goodsDirectLinkCategoryMappingBiz.getGoodsDirectLinkCategoryMappingList(paramDto));
    }

    @ApiOperation(value = "네이버 표준 카테고리 맵핑 조회내역 다운로드")
    @RequestMapping(value = "/admin/goods/list/getGoodsDirectLinkCategoryMappingListExcel")
    @ApiResponse(code = 900, message = "response data", response = GoodsDirectLinkCategoryMappingRequestDto.class)
    public ModelAndView getGoodsDirectLinkCategoryMappingListExcel(@RequestBody GoodsDirectLinkCategoryMappingRequestDto paramDto) throws Exception{
        System.out.println("# ##################################### #");
        System.out.println("# GoodsDirectLinkCategoryMappingController.getGoodsDirectLinkCategoryMappingListExcel #");
        System.out.println("# ##################################### #");

        ExcelDownloadDto excelDownloadDto = goodsDirectLinkCategoryMappingBiz.getGoodsDirectLinkCategoryMappingListExcel(paramDto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }

    @ApiOperation(value = "직연동 카테고리 관리 목록 리스트")
    @RequestMapping(value = "/admin/goods/list/getIfNaverCategoryList")
    @ApiResponse(code = 900, message = "response data", response = GoodsDirectLinkCategoryMappingListVo.class)
    public ApiResult<?> getIfNaverCategoryList(GoodsDirectLinkCategoryMappingRequestDto paramDto) throws Exception{
        System.out.println("# ##################################### #");
        System.out.println("# GoodsDirectLinkCategoryMappingController.getIfNaverCategoryList #");
        System.out.println("# ##################################### #");
        return ApiResult.success(goodsDirectLinkCategoryMappingBiz.getIfNaverCategoryList(paramDto));
    }

    @ApiOperation(value = "네이버 표준 카테고리 맵핑 등록")
    @RequestMapping(value = "/admin/goods/list/addGoodsDirectLinkCategoryMapping")
    @ApiResponse(code = 900, message = "response data", response = GoodsDirectLinkCategoryMappingListVo.class)
    public ApiResult<?> addGoodsDirectLinkCategoryMapping(@RequestBody GoodsDirectLinkCategoryMappingRequestDto paramDto) throws Exception{
        System.out.println("# ##################################### #");
        System.out.println("# GoodsDirectLinkCategoryMappingController.addGoodsGearCategoryMappingList #");
        System.out.println("# ##################################### #");
        return ApiResult.success(goodsDirectLinkCategoryMappingBiz.addGoodsDirectLinkCategoryMapping(paramDto));
    }

    @ApiOperation(value = "네이버 표준 카테고리 맵핑 수정")
    @RequestMapping(value = "/admin/goods/list/putGoodsDirectLinkCategoryMapping")
    @ApiResponse(code = 900, message = "response data", response = GoodsDirectLinkCategoryMappingListVo.class)
    public ApiResult<?> putGoodsDirectLinkCategoryMapping(@RequestBody GoodsDirectLinkCategoryMappingRequestDto paramDto) throws Exception{
        System.out.println("# ##################################### #");
        System.out.println("# GoodsDirectLinkCategoryMappingController.addGoodsGearCategoryMappingList #");
        System.out.println("# ##################################### #");
        return ApiResult.success(goodsDirectLinkCategoryMappingBiz.putGoodsDirectLinkCategoryMapping(paramDto));
    }

}
