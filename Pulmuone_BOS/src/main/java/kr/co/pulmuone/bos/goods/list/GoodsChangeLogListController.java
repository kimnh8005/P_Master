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
import kr.co.pulmuone.v1.goods.goods.dto.GoodsChangeLogListRequestDto;
import kr.co.pulmuone.v1.goods.goods.service.GoodsChangeLogListBiz;
import lombok.RequiredArgsConstructor;

/**
* <PRE>
* Forbiz Korea
* 상품 업데이트 내역 Controller
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일				:  작성자	  :  작성내역
* -----------------------------------------------------------------------
*  1.0	2021. 04. 21.				임상건		  최초작성
* =======================================================================
* </PRE>
*/
@RestController
@RequiredArgsConstructor
public class GoodsChangeLogListController {

	private final GoodsChangeLogListBiz goodsChangeLogListBiz;

	@Autowired
	private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

	@ApiOperation(value = "상품 목록 조회")
	@PostMapping(value = "/admin/goods/list/getGoodsChangeLogList")
	public ApiResult<?> getGoodsChangeLogList(HttpServletRequest request, GoodsChangeLogListRequestDto goodsChangeLogListRequestDto) throws Exception{

		return goodsChangeLogListBiz.getGoodsChangeLogList(BindUtil.bindDto(request, GoodsChangeLogListRequestDto.class));
	}

	/**
	 * 상품 엑셀 다운로드 목록 조회
	 *
	 * @param MasterItemListRequestDto
	 * @return ModelAndView
	 */
	@ApiOperation(value = "상품 리스트 엑셀 다운로드 목록 조회")
	@PostMapping(value = "/admin/goods/list/goodsChangeLogListExportExcel")
	public ModelAndView goodsChangeLogListExportExcel(@RequestBody GoodsChangeLogListRequestDto goodsChangeLogListRequestDto) {

		ExcelDownloadDto excelDownloadDto = goodsChangeLogListBiz.getGoodsChangeLogListExcel(goodsChangeLogListRequestDto);

		ModelAndView modelAndView = new ModelAndView(excelDownloadView);
		modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

		return modelAndView;

	}
}