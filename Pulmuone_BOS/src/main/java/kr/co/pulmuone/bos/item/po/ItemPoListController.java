package kr.co.pulmuone.bos.item.po;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.StockEnums;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.goods.item.dto.ItemPoListRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemPoListResponseDto;
import kr.co.pulmuone.v1.goods.item.service.GoodsItemPoListBiz;
import lombok.RequiredArgsConstructor;

/**
 * <PRE>
 * Forbiz Korea
 * 발주 리스트
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0   20210126	   	   이성준            최초작성
 * =======================================================================
 * </PRE>
 */
@RestController
@RequiredArgsConstructor
public class ItemPoListController {

	private final GoodsItemPoListBiz goodsItemPoListBiz;

	@Autowired(required=true)
	private HttpServletRequest request;

	@Autowired
    private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

	@ApiOperation(value = "발주 목록 조회")
	@PostMapping(value = "/admin/item/po/getPoList")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = ItemPoListResponseDto.class)
	})
	public ApiResult<?> getPoList(ItemPoListRequestDto dto) throws Exception {
		return goodsItemPoListBiz.getPoList(BindUtil.bindDto(request, ItemPoListRequestDto.class));
	}

	@ApiOperation(value = "발주정보 조회")
	@GetMapping(value = "/admin/item/po/getPoInfoList")
	@ResponseBody
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = ItemPoListResponseDto.class)
	})
	public ApiResult<?> getPoInfoList(ItemPoListRequestDto dto){
		return goodsItemPoListBiz.getPoInfoList(dto);
	}

	@ApiOperation(value = "발주 내역 조회")
	@PostMapping(value = "/admin/item/po/getPoResultList")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = ItemPoListResponseDto.class)
	})
	public ApiResult<?> getPoResultList(ItemPoListRequestDto dto) throws Exception {
		return goodsItemPoListBiz.getPoResultList(BindUtil.bindDto(request, ItemPoListRequestDto.class));
	}

	@GetMapping(value = "/admin/item/po/getPoTpList")
	@ResponseBody
	@ApiOperation(value = "발주유형 검색")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = ItemPoListResponseDto.class)
	})
	public ApiResult<?> getPoTpList(ItemPoListRequestDto dto) {
		return goodsItemPoListBiz.getPoTpList(dto);
	}

	@GetMapping(value = "/admin/item/po/getOnChangePoTpList")
	@ResponseBody
	@ApiOperation(value = "발주유형 검색(onChange)")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = ItemPoListResponseDto.class)
	})
	public ApiResult<?> getOnChangePoTpList(ItemPoListRequestDto dto) {
		return goodsItemPoListBiz.getOnChangePoTpList(dto);
	}

	@GetMapping(value = "/admin/item/po/getErpCtgryList")
	@ResponseBody
	@ApiOperation(value = "ERP 카테고리 (대분류) 검색")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = ItemPoListResponseDto.class)
	})
	public ApiResult<?> getErpCtgryList(ItemPoListRequestDto dto) {
		return goodsItemPoListBiz.getErpCtgryList(dto);
	}

	@ApiOperation(value = "발주 수량 수정")
	@PostMapping(value = "/admin/item/po/putItemPo")
	public ApiResult<?> putItemPo(@RequestBody ItemPoListRequestDto dto) throws Exception {
		return goodsItemPoListBiz.putItemPo(dto);
	}

	@ApiOperation(value = "발주리스트 엑셀 다운로드")
    @PostMapping(value = "/admin/item/po/getPoListExportExcel")
    public ModelAndView getPoListExportExcel(@RequestBody ItemPoListRequestDto dto) {

        ExcelDownloadDto excelDownloadDto = null;

        if(dto.getSearchUrSupplierId().equals(StockEnums.UrSupplierId.SUPPLIER_PFF.getCode())) {//풀무원 식품
           excelDownloadDto = goodsItemPoListBiz.getPfPoListExportExcel(dto);
        }else if(dto.getSearchUrSupplierId().equals(StockEnums.UrSupplierId.SUPPLIER_ORGA.getCode())) {//올가
           excelDownloadDto = goodsItemPoListBiz.getOgPoListExportExcel(dto);
        }

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }

	@ApiOperation(value = "발주내역 엑셀 다운로드")
    @PostMapping(value = "/admin/item/po/getPoResultListExportExcel")
    public ModelAndView getPoResultListExportExcel(@RequestBody ItemPoListRequestDto dto) {

        ExcelDownloadDto excelDownloadDto = null;

        if(dto.getSearchUrSupplierId().equals(StockEnums.UrSupplierId.SUPPLIER_PFF.getCode())) {//풀무원 식품
           excelDownloadDto = goodsItemPoListBiz.getPfPoResultListExportExcel(dto);
        }else if(dto.getSearchUrSupplierId().equals(StockEnums.UrSupplierId.SUPPLIER_ORGA.getCode())) {//올가
           excelDownloadDto = goodsItemPoListBiz.getOgPoResultListExportExcel(dto);
        }

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }

}



