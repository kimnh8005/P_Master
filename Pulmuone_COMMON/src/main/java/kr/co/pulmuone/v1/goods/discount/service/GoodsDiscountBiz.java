package kr.co.pulmuone.v1.goods.discount.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.goods.discount.dto.GoodsDisExcelUploadRequestDto;
import kr.co.pulmuone.v1.goods.discount.dto.GoodsDiscountUploadRequestDto;

public interface GoodsDiscountBiz {

    public ApiResult<?> getItemInfoAndGoodsDiscountList(Long goodsId, String discountTypeCode);
    public ApiResult<?> deleteGoodsDiscount(String goodsId, Long goodsDiscountApprId, Long goodsDiscountId, String discountTypeCode, String goodsType) throws Exception;
//    public ApiResult<?> putGoodsDiscountWithErpIfPriceBatch() throws Exception;
    public ApiResult<?> addGoodsDiscountWithErpIfPriceBatch(String ilGoodsId, String ilItemCode) throws Exception ;

	ApiResult<?> addExcelUpload(GoodsDisExcelUploadRequestDto paramDto) throws Exception;

	public ApiResult<?> getGoodsDiscountUploadList(GoodsDiscountUploadRequestDto paramDto);

	ExcelDownloadDto getGoodsDiscountUploadFailList(GoodsDiscountUploadRequestDto paramDto);

	public ApiResult<?> getGoodsDiscountUploadDtlList(GoodsDiscountUploadRequestDto paramDto);

	public ApiResult<?> getGoodsDisEmpUploadList(GoodsDiscountUploadRequestDto paramDto);

	ExcelDownloadDto createGoodsDiscountUploadDtlListExcel(GoodsDiscountUploadRequestDto paramDto);

	ApiResult<?> addEmployeeExcelUpload(GoodsDisExcelUploadRequestDto paramDto) throws Exception;

	ExcelDownloadDto createGoodsDiscountEmpUploadFailList(GoodsDiscountUploadRequestDto paramDto);

	public ApiResult<?> getGoodsDiscountEmpUploadDtlList(GoodsDiscountUploadRequestDto paramDto);

	ExcelDownloadDto createGoodsDiscountEmpUploadDtlListExcel(GoodsDiscountUploadRequestDto paramDto);

	public ApiResult<?> discountEndDateUpdate(Long goodsDiscountId, String discountEndDate) throws Exception;

	public void spGoodsPriceUpdateWhenGoodsDiscountChanges(String ilGoodsId) throws Exception;

	int putGoodsDiscount(Long ilGoodsId, String discountTypeCode, String discountStartDateTime);



}
