package kr.co.pulmuone.bos.item.master;

import javax.servlet.http.HttpServletRequest;

import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.goods.item.dto.ItemStoreStockRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.goods.item.dto.ItemStorePriceLogRequestDto;
import kr.co.pulmuone.v1.goods.item.service.GoodsItemStoreBiz;
import org.springframework.web.servlet.ModelAndView;


/**
 * <PRE>
* Forbiz Korea
* 마스터 품목 매장 정보 Controller
 * </PRE>
 *
 * <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2021. 06. 10.               홍진영         최초작성
* =======================================================================
 * </PRE>
 */
@RestController
public class GoodsItemStoreController {

	@Autowired
	private GoodsItemStoreBiz goodsItemStoreBiz;

	@Autowired
	private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

	/**
	 *
	 * @param ilItemCode
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "품목 매장정보 리스트 조회")
	@PostMapping(value = "/admin/item/store/getStoreList")
	public ApiResult<?> getStoreList(@RequestParam(value = "ilItemCode", required = true) String ilItemCode) throws Exception {
		return ApiResult.success(goodsItemStoreBiz.getStoreList(ilItemCode));
	}

	/**
	 *
	 * @param ilItemCode
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "품목 매장정보 리스트 조회")
	@PostMapping(value = "/admin/item/store/getStorePriceLogList")
	public ApiResult<?> getStorePriceLogList(HttpServletRequest request, ItemStorePriceLogRequestDto reqDto) throws Exception {
		return ApiResult.success(goodsItemStoreBiz.getStorePriceLogList(BindUtil.bindDto(request, ItemStorePriceLogRequestDto.class)));
	}

	/**
	 * 매장 재고 리스트
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "매장 재고 리스트 조회")
	@PostMapping(value = "/admin/item/store/getItemStoreStockList")
	public ApiResult<?> getItemStoreStockList(HttpServletRequest request) throws Exception {
		return ApiResult.success(goodsItemStoreBiz.getItemStoreStockList(BindUtil.bindDto(request, ItemStoreStockRequestDto.class)));
	}

	/**
	 * 매장 재고 리스트 엑셀 다운
	 * @param itemStoreStockRequestDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "매장 재고 리스트 엑셀")
	@PostMapping(value = "/admin/item/store/itemStoreStockListExcel")
	public ModelAndView getItemStoreStockListExcel(@RequestBody ItemStoreStockRequestDto itemStoreStockRequestDto) throws Exception {

		ExcelDownloadDto excelDownloadDto = goodsItemStoreBiz.getItemStoreStockListExcel(itemStoreStockRequestDto);

		ModelAndView modelAndView = new ModelAndView(excelDownloadView);
		modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

		return modelAndView;
	}

}
