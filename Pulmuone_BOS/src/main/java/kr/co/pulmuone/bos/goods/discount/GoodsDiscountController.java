package kr.co.pulmuone.bos.goods.discount;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.goods.discount.dto.GoodsDisExcelUploadRequestDto;
import kr.co.pulmuone.v1.goods.discount.dto.GoodsDiscountUploadRequestDto;
import kr.co.pulmuone.v1.goods.discount.dto.vo.GoodsDiscountVo;
import kr.co.pulmuone.v1.goods.discount.service.GoodsDiscountBiz;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

/**
* <PRE>
* Forbiz Korea
* 상품할인 Controller
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 9. 8.                손진구          최초작성
* =======================================================================
* </PRE>
*/
@RestController
@RequiredArgsConstructor
@Slf4j
public class GoodsDiscountController {
    private final GoodsDiscountBiz goodsDiscountBiz;

    @Autowired
    private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

    @GetMapping(value = "/admin/goods/discount/getItemInfoAndGoodsDiscountList")
    @ApiOperation(value = "단품상세정보 및 상품할인 리스트 조회", httpMethod = "GET")
    @ApiImplicitParams({ @ApiImplicitParam(name = "goodsId", value = "상품 PK", required = true, dataType = "Long")
                       , @ApiImplicitParam(name = "discountTypeCode", value = "할인유형코드", required = true, dataType = "String") })
    public ApiResult<?> getItemInfoAndGoodsDiscountList(  @RequestParam(value = "goodsId", required = true) Long goodsId
                                                        , @RequestParam(value = "discountTypeCode", required = true) String discountTypeCode) {

        return goodsDiscountBiz.getItemInfoAndGoodsDiscountList(goodsId, discountTypeCode);
    }

    @GetMapping(value = "/admin/goods/discount/deleteGoodsDiscount")
    @ApiOperation(value = "상품할인 삭제", httpMethod = "GET")
    @ApiImplicitParams({ @ApiImplicitParam(name = "goodsId", value = "상품ID", required = true, dataType = "String")
						,  @ApiImplicitParam(name = "goodsDiscountApprId", value = "상품할인승인 PK", required = true, dataType = "Long")
    					,  @ApiImplicitParam(name = "goodsDiscountId", value = "상품할인 PK", required = true, dataType = "Long")
    					,  @ApiImplicitParam(name = "discountTypeCode", value = "할인유형코드", required = true, dataType = "String")
    					,  @ApiImplicitParam(name = "goodsType", value = "상품유형", required = true, dataType = "String") })
    public ApiResult<?> deleteGoodsDiscount(  @RequestParam(value = "goodsId", required = true) String goodsId
            , @RequestParam(value = "goodsDiscountApprId", required = true) Long goodsDiscountApprId
    		, @RequestParam(value = "goodsDiscountId", required = true) Long goodsDiscountId
            , @RequestParam(value = "discountTypeCode", required = true) String discountTypeCode
            , @RequestParam(value = "goodsType", required = false) String goodsType) throws Exception {

        return goodsDiscountBiz.deleteGoodsDiscount(goodsId, goodsDiscountApprId, goodsDiscountId, discountTypeCode, goodsType);
    }


    /**
     * 가격조회 ERP 연동 후 상품 할인 데이터 처리
     * @return ApiResult<?>
     * @throws Exception
     */
//    @PostMapping(value = "/admin/goods/discount/putGoodsDiscountWithErpIfPriceBatch")
//    public ApiResult<?> putItemPriceListWithErpIfPriceBatch() throws Exception{
//
//        return goodsDiscountBiz.putGoodsDiscountWithErpIfPriceBatch();
//    }

    /**
     * 상품 할인 엑셀 업로드
     *
     * @param GoodsDisExcelUploadRequestDto
     * @return ApiResult<?>
     */
    @ApiOperation(value = "상품 할인 엑셀 업로드")
	@PostMapping(value = "/admin/goods/discount/addExcelUpload")
	public ApiResult<?> addExcelUpload(@RequestBody GoodsDisExcelUploadRequestDto dto) throws Exception {
        log.info("excelDto : " + dto);
		//dto.setUploadList(BindUtil.convertJsonArrayToDtoList(dto.getUpload(), GoodsDiscountVo.class));

		return goodsDiscountBiz.addExcelUpload(dto);
	}

    /**
     * 상품 할인 일괄 업로드 실패 내역 엑셀 다운로드
     *
     * @param GoodsDiscountUploadRequestDto
     * @return ModelAndView
     */
    @ApiOperation(value = "임직원 상품 할인 엑셀 업로드")
	@PostMapping(value = "/admin/goods/discount/addEmployeeExcelUpload")
	public ApiResult<?> addEmployeeExcelUpload(@RequestBody GoodsDisExcelUploadRequestDto dto) throws Exception {
        log.info("employeeExcelDto : " + dto);
		//dto.setUploadList(BindUtil.convertJsonArrayToDtoList(dto.getUpload(), GoodsDiscountVo.class));

		return goodsDiscountBiz.addEmployeeExcelUpload(dto);
	}

    /**
     * 상품할인 일괄업로드 조회
     *
     * @param GoodsDisExcelUploadRequestDto
     * @return ApiResult<?>
     */
    @PostMapping(value = "/admin/goods/discount/getGoodsDiscountUploadList")
    @ApiOperation(value = "상품할인 일괄업로드 조회")
    public ApiResult<?> getGoodsDiscountUploadList(HttpServletRequest request, GoodsDiscountUploadRequestDto paramDto) throws Exception{
        return goodsDiscountBiz.getGoodsDiscountUploadList(BindUtil.bindDto(request, GoodsDiscountUploadRequestDto.class));
    }

    /**
     * 임직원 할인 일괄업로드 조회
     *
     * @param GoodsDisExcelUploadRequestDto
     * @return ApiResult<?>
     */
    @PostMapping(value = "/admin/goods/discount/getGoodsDisEmpUploadList")
    @ApiOperation(value = "상품할인 일괄업로드 조회")
    public ApiResult<?> getGoodsDisEmpUploadList(HttpServletRequest request, GoodsDiscountUploadRequestDto paramDto) throws Exception{
        return goodsDiscountBiz.getGoodsDisEmpUploadList(BindUtil.bindDto(request, GoodsDiscountUploadRequestDto.class));
    }



    /**
     * 상품 할인 일괄 업로드 실패 내역 엑셀 다운로드
     *
     * @param GoodsDiscountUploadRequestDto
     * @return ModelAndView
     */
    @ApiOperation(value = "상품 할인 일괄 업로드 실패 내역 엑셀 다운로드")
    @PostMapping(value = "/admin/goods/discount/createGoodsDiscountUploadFailList")
    public ModelAndView getGoodsDiscountUploadFailList(@RequestBody GoodsDiscountUploadRequestDto paramDto) {

        ExcelDownloadDto excelDownloadDto = goodsDiscountBiz.getGoodsDiscountUploadFailList(paramDto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }

    /**
     * 상품할인 일괄업로드 상세 조회
     *
     * @param GoodsDiscountUploadRequestDto
     * @return ModelAndView
     */
    @PostMapping(value = "/admin/goods/discount/getGoodsDiscountUploadDtlList")
    @ApiOperation(value = "상품할인 일괄업로드 상세 조회")
    public ApiResult<?> getGoodsDiscountUploadDtlList(HttpServletRequest request, GoodsDiscountUploadRequestDto paramDto) throws Exception{
        return goodsDiscountBiz.getGoodsDiscountUploadDtlList(BindUtil.bindDto(request, GoodsDiscountUploadRequestDto.class));
    }

    /**
     * 상품 할인 일괄 업로드 상세 조회 엑셀 다운로드
     *
     * @param GoodsDiscountUploadRequestDto
     * @return ModelAndView
     */
    @ApiOperation(value = "상품 할인 일괄 업로드 상세 조회 엑셀 다운로드")
    @PostMapping(value = "/admin/goods/discount/createGoodsDiscountUploadDtlListExcel")
    public ModelAndView createGoodsDiscountUploadDtlListExcel(@RequestBody GoodsDiscountUploadRequestDto paramDto) {

        ExcelDownloadDto excelDownloadDto = goodsDiscountBiz.createGoodsDiscountUploadDtlListExcel(paramDto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }

    /**
     * 임직원 상품 할인 일괄 업로드 실패 내역 엑셀 다운로드
     *
     * @param GoodsDiscountUploadRequestDto
     * @return ModelAndView
     */
    @ApiOperation(value = "임직원 상품 할인 일괄 업로드 실패 내역 엑셀 다운로드")
    @PostMapping(value = "/admin/goods/discount/createGoodsDiscountEmpUploadFailList")
    public ModelAndView createGoodsDiscountEmpUploadFailList(@RequestBody GoodsDiscountUploadRequestDto paramDto) {

        ExcelDownloadDto excelDownloadDto = goodsDiscountBiz.createGoodsDiscountEmpUploadFailList(paramDto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }

    /**
     * 임직원 상품 할인 일괄업로드 상세 조회
     *
     * @param GoodsDiscountUploadRequestDto
     * @return ModelAndView
     */
    @PostMapping(value = "/admin/goods/discount/getGoodsDiscountEmpUploadDtlList")
    @ApiOperation(value = "임직원 상품 할인 일괄업로드 상세 조회")
    public ApiResult<?> getGoodsDiscountEmpUploadDtlList(HttpServletRequest request, GoodsDiscountUploadRequestDto paramDto) throws Exception{
        return goodsDiscountBiz.getGoodsDiscountEmpUploadDtlList(BindUtil.bindDto(request, GoodsDiscountUploadRequestDto.class));
    }

    /**
     * 임직원 상품 할인 일괄 업로드 실패 내역 엑셀 다운로드
     *
     * @param GoodsDiscountUploadRequestDto
     * @return ModelAndView
     */
    @ApiOperation(value = "임직원 상품 할인 일괄 업로드 내역 엑셀 다운로드")
    @PostMapping(value = "/admin/goods/discount/createGoodsDiscountEmpUploadDtlListExcel")
    public ModelAndView createGoodsDiscountEmpUploadDtlListExcel(@RequestBody GoodsDiscountUploadRequestDto paramDto) {

        ExcelDownloadDto excelDownloadDto = goodsDiscountBiz.createGoodsDiscountEmpUploadDtlListExcel(paramDto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }

    /**
     * 할인 종료일 업데이트
     *
     * @param goodsDiscountId, discountTypeCode
     * @return ModelAndView
     */
    @GetMapping(value = "/admin/goods/discount/discountEndDateUpdate")
    @ApiOperation(value = "할인 종료일 업데이트", httpMethod = "GET")
    @ApiImplicitParams({ @ApiImplicitParam(name = "goodsDiscountId", value = "상품할인 PK", required = true, dataType = "Long")
    					,  @ApiImplicitParam(name = "discountTypeCode", value = "할인유형코드", required = true, dataType = "String") })
    public ApiResult<?> discountEndDateUpdate(  @RequestParam(value = "goodsDiscountId", required = true) Long goodsDiscountId
            , @RequestParam(value = "discountEndDate", required = true) String discountEndDate) throws Exception {

        return goodsDiscountBiz.discountEndDateUpdate(goodsDiscountId, discountEndDate);
    }




}