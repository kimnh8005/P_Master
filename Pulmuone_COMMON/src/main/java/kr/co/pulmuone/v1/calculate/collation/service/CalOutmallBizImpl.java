package kr.co.pulmuone.v1.calculate.collation.service;

import kr.co.pulmuone.v1.calculate.collation.dto.*;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.ExcelUploadEnums;
import kr.co.pulmuone.v1.comm.enums.ExcelUploadValidateEnums;
import kr.co.pulmuone.v1.comm.excel.factory.OrderExcelUploadFactory;
import kr.co.pulmuone.v1.comm.excel.util.OrderExcelSetData;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.comm.util.ExcelUploadUtil;
import kr.co.pulmuone.v1.outmall.order.dto.vo.OutMallExcelInfoVo;
import kr.co.pulmuone.v1.outmall.order.util.OutmallOrderUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <PRE>
 * Forbiz Korea
 * 정산관리 > 대사관리 > 외부몰 주문 대사 BizImpl
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 03. 05.	이명수		최초작성
 * =======================================================================
 * </PRE>
 */

@Service
public class CalOutmallBizImpl implements CalOutmallBiz {

	@Autowired
	CalOutmallService calOutmallService;

	@Autowired
	private OrderExcelUploadFactory orderExcelUploadFactory;


	private String pgUploadType = ExcelUploadEnums.ExcelUploadType.CAL_OUTMALL.getCode();

	/**
	 * 외부몰 주문 대사 리스트 조회
	 * @param calOutmallListRequestDto
	 * @return
	 */
	@Override
	@UserMaskingRun(system = "BOS")
	public ApiResult<?> getOutmallList(CalOutmallListRequestDto calOutmallListRequestDto) {

//		calOutmallListRequestDto.setSalesDeliveryGubunList(calOutmallService.getSearchKeyToSearchKeyList(calOutmallListRequestDto.getSalesDeliveryGubun(), Constants.ARRAY_SEPARATORS)); // 구분

		long totalCnt = calOutmallService.getOutmallListCount(calOutmallListRequestDto);

		List<CalOutmallListDto> outmallList = new ArrayList<>();
		if (totalCnt > 0) {
			outmallList = calOutmallService.getOutmallList(calOutmallListRequestDto);
		}

		return ApiResult.success(
				CalOutmallListResponseDto.builder()
									.rows(outmallList)
									.total(totalCnt)
									.build()
		);
	}

	/**
	 * 외부몰 주문 대사 엑셀 업로드
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> addOutmallExcelUpload(MultipartFile file) throws Exception {
		if (ExcelUploadUtil.isFile(file) != true) return ApiResult.result(null, ExcelUploadValidateEnums.UploadResponseCode.EXCEL_UPLOAD_NONE);

		OrderExcelSetData orderExcelSetData = new OrderExcelSetData();
		OutmallOrderUtil outmallOrderUtil = new OutmallOrderUtil();

		//업로드 현황 정보 설정
		OutMallExcelInfoVo infoVo = outmallOrderUtil.setOutmallExcelInfo();

		CalOutmallListRequestDto calOutmallListRequestDto = new CalOutmallListRequestDto();

		// Excel Import 정보 -> Dto 변환
		Sheet uploadSheet = ExcelUploadUtil.excelParse(file);
		if (uploadSheet == null) return ApiResult.result(null, ExcelUploadValidateEnums.UploadResponseCode.EXCEL_TRANSFORM_FAIL);
		calOutmallListRequestDto.setCreateId(Long.parseLong(calOutmallListRequestDto.getUserVo().getUserId()));
		calOutmallListRequestDto.setOriginNm(file.getOriginalFilename());

		// 외부몰 주문업로드 정보 등록
		calOutmallService.addOdOrderMaster(calOutmallListRequestDto);

		// Excel 데이터 Mapping
		List<CalOutmallUploadDto> excelList = (List<CalOutmallUploadDto>) orderExcelUploadFactory.setExcelData(pgUploadType, uploadSheet);


		// 항목별 검증 진행
		excelList = (List<CalOutmallUploadDto>) orderExcelUploadFactory.getDefaultRowItemValidator(pgUploadType, excelList);


		//업로드 현황 설정
		List<CalOutmallUploadDto> successVoList = new ArrayList<>();
		List<CalOutmallUploadDto> failVoList = new ArrayList<>();
		for (CalOutmallUploadDto item : excelList) {
			System.out.println("item : " + item);
			item.setOdOutMallCompareUploadInfoId(calOutmallListRequestDto.getOdOutMallCompareUploadInfoId());
			item.setCreateId(calOutmallListRequestDto.getCreateId());
			if (item.isSuccess()) {
				item.setSuccessYn("Y");
				successVoList.add(item);
			} else {
				item.setSuccessYn("N");
				failVoList.add(item);
			}
			calOutmallService.addOdOrderUploadDetail(item);
		}

		// Upload 정보 수정
		calOutmallListRequestDto.setSuccessCnt(successVoList.size());
		calOutmallListRequestDto.setFailCnt(failVoList.size());
		calOutmallService.putOutmallCountInfo(calOutmallListRequestDto);

		// Return 값 설정
		CalOutmallUploadResponseDto responseDto = new CalOutmallUploadResponseDto();
		responseDto.setTotalCount(excelList.size());
		responseDto.setSuccessCount(successVoList.size());
		responseDto.setFailCount(failVoList.size());

		if (responseDto.getFailCount() > 0) {
			List<String> failMessageList = new ArrayList<>();
			failVoList.stream()
					.forEach(item -> {
						String[] arr = item.getFailMessage().split(Constants.ARRAY_SEPARATORS);
						for(String str:arr){
							if (!"".equals(str.trim())) {
								failMessageList.add(str);
							}
						}
					});

			responseDto.setFailMessage(
					failMessageList.stream()
							.distinct()
							.collect(Collectors.joining("<br/>"))
			);
			return ApiResult.result(responseDto, ExcelUploadValidateEnums.UploadResponseCode.UPLOAD_FAIL);
		}

		return ApiResult.success(responseDto);


	}

	public CalOutmallUploadDto getSellerInfo(String  sellersNm) {

		CalOutmallUploadDto sellDto = new CalOutmallUploadDto();
        sellDto = calOutmallService.getSellerInfo(sellersNm);

		return sellDto;
	}

	/**
	 * 외부몰 주문 대사 상세내역 리스트 조회
	 * @param calOutmallListRequestDto
	 * @return
	 */
	@Override
	public ApiResult<?> getOutmallDetlList(CalOutmallListRequestDto calOutmallListRequestDto) {

		calOutmallListRequestDto.setOmSellersIdList(getSearchKeyToSearchKeyList(calOutmallListRequestDto.getOmSellersId(), Constants.ARRAY_SEPARATORS)); // 판매처
		CalOutmallDetlListDto  totalVo = calOutmallService.getOutmallDetlListCount(calOutmallListRequestDto);
		List<CalOutmallDetlListDto> outmallList = new ArrayList<>();
		if (totalVo.getTotalCnt() > 0) {
			outmallList = calOutmallService.getOutmallDetlList(calOutmallListRequestDto);
		}

		return ApiResult.success(
				CalOutmallDetlListResponseDto.builder()
									.rows(outmallList)
									.total(totalVo.getTotalCnt())
									.totalAmt(totalVo.getTotalAmt())
									.build()
		);
	}

    protected List<String> getSearchKeyToSearchKeyList(String searchKey, String splitKey) {
        List<String> searchKeyList = new ArrayList<String>();
        if( StringUtils.isNotEmpty(searchKey) && searchKey.indexOf("ALL") < 0 ) {

            searchKeyList.addAll(Stream.of(searchKey.split(splitKey))
                                       .map(String::trim)
                                       .filter( x -> StringUtils.isNotEmpty(x) )
                                       .collect(Collectors.toList()));
        }
        return searchKeyList;
    }


    /**
	 * 외부몰 주문 대사 상세내역 엑셀 리스트 조회
	 * @param calOutmallListRequestDto
	 * @return ExcelDownloadDto
	 * @throws Exception
	 */
	@Override
	public ExcelDownloadDto getOutmallDetlExcelList(CalOutmallListRequestDto calOutmallListRequestDto) {

		calOutmallListRequestDto.setExcelYn("Y");
		calOutmallListRequestDto.setOmSellersIdList(getSearchKeyToSearchKeyList(calOutmallListRequestDto.getOmSellersId(), Constants.ARRAY_SEPARATORS)); // 판매처
		List<CalOutmallDetlListDto> supportPriceExcelList = calOutmallService.getOutmallDetlList(calOutmallListRequestDto);

        String excelFileName = "외부몰 주문대사 상세내역" + "_" + DateUtil.getCurrentDate();
        String excelSheetName = "sheet";
        /* 화면값보다 20더 하면맞다. */
		Integer[] widthListOfFirstWorksheet = {  180, 180, 180, 180, 180, 180, 180, 180, 180, 180,
				180, 180 };

		String[] alignListOfFirstWorksheet = { 	"center", "center", "center", "center", "center", "center", "center", "center", "center", "center",
				"center", "center"};

		String[] propertyListOfFirstWorksheet = {	"uploadSellersNm", "uploadOutmallDetailId", "orderAmt", "contrastAmt", "settlePrice", "supplierNm", "odid", "sellersNm", "outmallDetailId", "vatRemovePaidPrice",
				"vat", "paidPrice" };

		String[] firstHeaderListOfFirstWorksheet = {	"판매처", "외부몰 주문번호", "매출금액", "대비금액", "최종 매출금액", "공급업체명", "주문번호", "판매처명", "외부몰 주문번호", "매출금액 (VAT 제외)",
				"VAT", "매출금액(VAT 포함)" };

        ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder()
                                                               .workSheetName(excelSheetName)
                                                               .propertyList(propertyListOfFirstWorksheet)
                                                               .widthList(widthListOfFirstWorksheet)
                                                               .alignList(alignListOfFirstWorksheet)
                                                               .build();

        firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet);

        firstWorkSheetDto.setExcelDataList(supportPriceExcelList);

        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder().excelFileName(excelFileName).build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

		return excelDownloadDto;
	}

	/**
	 * BOS 클레임 사유 건수 조회
	 * @param psClaimBosId
	 * @return
	 * @throws Exception
	 */
	@Override
	public int getPsClaimBosCount(long psClaimBosId) {
		return calOutmallService.getPsClaimBosCount(psClaimBosId);
	}

	/**
	 * 외부몰 주문 대사 업로드 실패내역 엑셀 리스트 조회
	 * @param calOutmallListRequestDto
	 * @return ExcelDownloadDto
	 * @throws Exception
	 */
	@Override
	public ExcelDownloadDto getCalOutmallUploadFailList(CalOutmallListRequestDto calOutmallListRequestDto) {

		List<CalOutmallDetlListDto> supportPriceExcelList = calOutmallService.getCalOutmallUploadFailList(calOutmallListRequestDto);

		String excelFileName = "외부몰 주문대사 업로드 실패내역" + "_" + DateUtil.getCurrentDate();
		String excelSheetName = "sheet";
		/* 화면값보다 20더 하면맞다. */
		Integer[] widthListOfFirstWorksheet = {  200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200 };

		String[] alignListOfFirstWorksheet = { 	"center", "center", "center", "center", "center", "center", "center", "center", "center", "center",
				"center", "center", "center"};

		String[] propertyListOfFirstWorksheet = {	"outmallId", "sellersNm", "orderAmt", "orderCnt", "icDt", "orderIfDt", "settleDt", "goodsNm", "discountPrice", "couponPrice",
				"etcDiscountPrice", "settlePrice", "settleItemCnt" };

		String[] firstHeaderListOfFirstWorksheet = {	"외부몰 주문번호", "판매처", "매출금액", "판매수량", "결제일자", "주문일자", "매출확정일자", "상품명", "에누리금액", "쿠폰금액",
				"기타 공제금액", "최종 매출금액", "최종 판매수량" };

		ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder()
				.workSheetName(excelSheetName)
				.propertyList(propertyListOfFirstWorksheet)
				.widthList(widthListOfFirstWorksheet)
				.alignList(alignListOfFirstWorksheet)
				.build();

		firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet);

		firstWorkSheetDto.setExcelDataList(supportPriceExcelList);

		ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder().excelFileName(excelFileName).build();

		excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

		return excelDownloadDto;
	}
}