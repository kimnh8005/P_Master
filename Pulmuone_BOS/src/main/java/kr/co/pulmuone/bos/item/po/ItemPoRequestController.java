package kr.co.pulmuone.bos.item.po;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.goods.item.dto.ItemPoRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemPoRequestResponseDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemPoRequestVo;
import kr.co.pulmuone.v1.goods.item.service.GoodsItemPoRequestBiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <PRE>
 * Forbiz Korea
 * 행사발주관리
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021.01.21	  정형진
 * =======================================================================
 * </PRE>
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class ItemPoRequestController {

	private final GoodsItemPoRequestBiz goodsItemPoRequestBiz;

    @Autowired
    private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

	@ApiOperation(value = "행사발주관리 조회")
	@PostMapping(value = "/admin/item/poRequest/getPoRequestList")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = ItemPoRequestResponseDto.class)
    })
	public ApiResult<?> getPoRequestList(HttpServletRequest request, ItemPoRequestDto paramDto) throws Exception {
		return goodsItemPoRequestBiz.getPoRequestList(BindUtil.bindDto(request, ItemPoRequestDto.class));
	}

	@ApiOperation(value = "행사발주관리 추가")
	@PostMapping(value = "/admin/item/poRequest/addPoRequest")
	@ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = ItemPoRequestResponseDto.class),
            @ApiResponse(code = 901, message = "" + "DUPLICATE_DATA - 중복된 데이터가 존재합니다.")
    })
	public ApiResult<?> addItemPoRequest(ItemPoRequestDto paramDto) throws Exception {
		     return goodsItemPoRequestBiz.addItemPoRequest(paramDto);
    }

	@ApiOperation(value = "발주 유형관리 상세 조회")
	@PostMapping(value = "/admin/item/poRequest/getPoRequest")
	@ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = ItemPoRequestResponseDto.class)
    })
	public ApiResult<?> getPoRequest(@RequestParam(value = "ilPoEventId", required = true) String ilPoEventId) {
		return goodsItemPoRequestBiz.getPoRequest(ilPoEventId);
	}

	@ApiOperation(value = "행사발주관리 수정")
	@PostMapping(value = "/admin/item/poRequest/putPoRequest")
	@ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = ItemPoRequestResponseDto.class),
            @ApiResponse(code = 901, message = "" + "DUPLICATE_DATA - 중복된 데이터가 존재합니다.")
    })
	public ApiResult<?> putPoRequest(ItemPoRequestDto paramDto) throws Exception {
		     return goodsItemPoRequestBiz.putPoRequest(paramDto);
    }

	@ApiOperation(value = "행사발주관리 삭제")
	@PostMapping(value = "/admin/item/poRequest/delPoRequest")
	@ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = ItemPoRequestResponseDto.class),
            @ApiResponse(code = 901, message = "" + "DUPLICATE_DATA - 중복된 데이터가 존재합니다.")
    })
	public ApiResult<?> delPoRequest(ItemPoRequestDto paramDto) throws Exception {
		     return goodsItemPoRequestBiz.delPoRequest(paramDto);
    }

	@ApiOperation(value = "행사발주  엑셀 업로드")
	@PostMapping(value = "/admin/item/poRequest/addPoRequestExcelUpload")
	@ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = ItemPoRequestResponseDto.class),
            @ApiResponse(code = 901, message = "" + "DUPLICATE_DATA - 중복된 데이터가 존재합니다.")
    })
	public ApiResult<?> addPoRequestExcelUpload(ItemPoRequestDto paramDto) throws Exception {
			paramDto.setUploadList(BindUtil.convertJsonArrayToDtoList(paramDto.getUpload(), ItemPoRequestVo.class));
			return goodsItemPoRequestBiz.addPoRequestExcelUpload(paramDto);
    }

	@ApiOperation(value = "행사발주관리 업로드 조회")
	@PostMapping(value = "/admin/item/poRequest/getPoRequestUploadList")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = ItemPoRequestResponseDto.class)
    })
	public ApiResult<?> getPoRequestUploadList(HttpServletRequest request, ItemPoRequestDto paramDto) throws Exception {
		return goodsItemPoRequestBiz.getPoRequestUploadList(BindUtil.bindDto(request, ItemPoRequestDto.class));
	}

	/**
     * 행사 발주 엑셀 업로드 실패내역 다운로드
     *
     * @param ItemPoRequestDto
     * @return ModelAndView
     */
    @ApiOperation(value = "행사 발주 엑셀 업로드 실패내역 다운로드")
    @PostMapping(value = "/admin/item/poRequest/createPoRequestUplodFailList")
    public ModelAndView createPoRequestUplodFailList(@RequestBody ItemPoRequestDto paramDto) {

        ExcelDownloadDto excelDownloadDto = goodsItemPoRequestBiz.createPoRequestUplodFailList(paramDto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }

    /**
     * 행사 발주 내역 다운로드
     *
     * @param GoodsDiscountUploadRequestDto
     * @return ModelAndView
     */
    @ApiOperation(value = "행사 발주 내역 다운로드")
    @PostMapping(value = "/admin/item/poRequest/createPoRequestList")
    public ModelAndView createPoRequestList(@RequestBody ItemPoRequestDto paramDto) {

        ExcelDownloadDto excelDownloadDto = goodsItemPoRequestBiz.createPoRequestList(paramDto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }


}
