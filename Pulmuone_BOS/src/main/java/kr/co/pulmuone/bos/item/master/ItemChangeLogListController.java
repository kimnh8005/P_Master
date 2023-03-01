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
import kr.co.pulmuone.v1.goods.goods.dto.GoodsChangeLogListRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsChangeLogListResponseDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemChangeLogListRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemChangeLogListResponseDto;
import kr.co.pulmuone.v1.goods.item.service.ItemChangeLogListBiz;
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
public class ItemChangeLogListController {

	private final ItemChangeLogListBiz goodsChangeLogListBiz;

	@Autowired
	private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

	@ApiOperation(value = "마스터 품목 업데이트 내역")
	@PostMapping(value = "/admin/item/master/getItemChangeLogList")
	public ApiResult<?> getItemChangeLogList(HttpServletRequest request, ItemChangeLogListRequestDto goodsChangeLogListRequestDto) throws Exception{

		return goodsChangeLogListBiz.getItemChangeLogList(BindUtil.bindDto(request, ItemChangeLogListRequestDto.class));
	}

	/**
	 * 상품 엑셀 다운로드 목록 조회
	 *
	 * @param MasterItemListRequestDto
	 * @return ModelAndView
	 */
	@ApiOperation(value = "마스터 품목 업데이트 내역 엑셀 다운로드")
	@PostMapping(value = "/admin/item/master/itemChangeLogListExportExcel")
	public ModelAndView goodsChangeLogListExportExcel(@RequestBody ItemChangeLogListRequestDto goodsChangeLogListRequestDto) {

		ExcelDownloadDto excelDownloadDto = goodsChangeLogListBiz.getItemChangeLogListExcel(goodsChangeLogListRequestDto);

		ModelAndView modelAndView = new ModelAndView(excelDownloadView);
		modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

		return modelAndView;

	}

	/**업데이트 상세 내역
	  *
	  * @param itemChangeLogListRequestDto
	  * @return
	  * @throws Exception
	  */
	@ApiOperation(value = "업데이트 상세 내역")
	@PostMapping(value = "/admin/item/master/getItemChangeLogPopup")
	public ApiResult<?> getGoodsChangeLogPopup (ItemChangeLogListRequestDto itemChangeLogListRequestDto) throws Exception {

		ItemChangeLogListResponseDto result = goodsChangeLogListBiz.getItemChangeLogPopup(itemChangeLogListRequestDto);
		return ApiResult.success(result);
	}
}