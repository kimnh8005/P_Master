package kr.co.pulmuone.bos.goods.list;

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
import kr.co.pulmuone.v1.goods.goods.dto.GoodsAllModifyRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsAllModifyResponseDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsPackageListRequestDto;
import kr.co.pulmuone.v1.goods.goods.service.GoodsAllModifyBiz;
import lombok.RequiredArgsConstructor;

/**
* <PRE>
* Forbiz Korea
* 상품일괄수정 Controller
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2021. 01. 08.               정형진          최초작성
* =======================================================================
* </PRE>
*/
@RestController
@RequiredArgsConstructor
public class GoodsAllModifyController {

	private final GoodsAllModifyBiz goodsAllModifyBiz;

    @Autowired
    private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

	@ApiOperation(value = "상품 목록 조회")
    @PostMapping(value = "/admin/goods/list/getGoodsAllModifyList")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GoodsAllModifyResponseDto.class)
	})
    public ApiResult<?> getGoodsAllModifyList(HttpServletRequest request, GoodsAllModifyRequestDto goodsAllModifyRequestDto) throws Exception{

	    return goodsAllModifyBiz.getGoodsAllModifyList(goodsAllModifyRequestDto);
    }

	@ApiOperation(value = "상품 목록 조회")
    @PostMapping(value = "/admin/goods/list/putGoodsAllModify")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GoodsAllModifyResponseDto.class)
	})
    public ApiResult<?> putGoodsAllModify(@RequestBody GoodsAllModifyRequestDto goodsAllModifyRequestDto) throws Exception{

	    return goodsAllModifyBiz.putGoodsAllModify(goodsAllModifyRequestDto);
    }

	@ApiOperation(value = "추가 상품 목록 조회")
    @PostMapping(value = "/admin/goods/list/getGoodsAdditionList")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GoodsAllModifyResponseDto.class)
	})
    public ApiResult<?> getGoodsAdditionList(HttpServletRequest request, GoodsAllModifyRequestDto goodsAllModifyRequestDto) throws Exception{

	    return goodsAllModifyBiz.getGoodsAdditionList(goodsAllModifyRequestDto);
    }

	@ApiOperation(value = "추가 상품 목록 조회")
    @PostMapping(value = "/admin/goods/list/getGoodsNoticeInfoList")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GoodsAllModifyResponseDto.class)
	})
    public ApiResult<?> getGoodsNoticeInfoList(HttpServletRequest request, GoodsAllModifyRequestDto goodsAllModifyRequestDto) throws Exception{

	    return goodsAllModifyBiz.getGoodsNoticeInfoList(goodsAllModifyRequestDto);
    }

	@ApiOperation(value = "묶음 상품 엑셀 다운로드 목록 조회")
    @PostMapping(value = "/admin/goods/list/createGoodsAllModifyExcel")
    public ModelAndView createGoodsAllModifyExcel(@RequestBody GoodsAllModifyRequestDto paramDto) {

        ExcelDownloadDto excelDownloadDto = goodsAllModifyBiz.createGoodsAllModifyExcel(paramDto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;

    }



}
