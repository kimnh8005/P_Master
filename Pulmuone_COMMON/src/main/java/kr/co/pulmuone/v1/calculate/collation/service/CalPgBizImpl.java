package kr.co.pulmuone.v1.calculate.collation.service;

import kr.co.pulmuone.v1.calculate.collation.dto.*;
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
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.outmall.order.dto.vo.OutMallExcelInfoVo;
import kr.co.pulmuone.v1.outmall.order.util.OutmallOrderUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <PRE>
 * Forbiz Korea
 * 정산관리 > 대사관리 > PG 거래 내역 대사 BizImpl
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
public class CalPgBizImpl implements CalPgBiz {

	@Autowired
	CalPgService calPgService;

	@Autowired
	private OrderExcelUploadFactory orderExcelUploadFactory;


	private String pgUploadType = ExcelUploadEnums.ExcelUploadType.CAL_PG.getCode();
	/**
	 * PG 거래 내역 대사 리스트 조회
	 * @param calPgListRequestDto
	 * @return
	 */
	@Override
	public ApiResult<?> getPgList(CalPgListRequestDto calPgListRequestDto) {

		calPgListRequestDto.setSalesDeliveryGubunList(calPgService.getSearchKeyToSearchKeyList(calPgListRequestDto.getSalesDeliveryGubun(), Constants.ARRAY_SEPARATORS)); // 구분

		long totalCnt = calPgService.getPgListCount(calPgListRequestDto);

		List<CalPgListDto> pgList = new ArrayList<>();
		if (totalCnt > 0) {
			pgList = calPgService.getPgList(calPgListRequestDto);
		}

		return ApiResult.success(
				CalPgListResponseDto.builder()
									.rows(pgList)
									.total(totalCnt)
									.build()
		);
	}

	/**
	 * PG 거래 내역 대사 엑셀 업로드
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> addPgExcelUpload(MultipartFile file, CalPgListRequestDto dto) throws Exception {
		if (ExcelUploadUtil.isFile(file) != true) return ApiResult.result(null, ExcelUploadValidateEnums.UploadResponseCode.EXCEL_UPLOAD_NONE);

		OrderExcelSetData orderExcelSetData = new OrderExcelSetData();
		OutmallOrderUtil outmallOrderUtil = new OutmallOrderUtil();

		//업로드 현황 정보 설정
		OutMallExcelInfoVo infoVo = outmallOrderUtil.setOutmallExcelInfo();

		CalPgListRequestDto calPgListRequestDto = new CalPgListRequestDto();

		// Excel Import 정보 -> Dto 변환
		Sheet uploadSheet = ExcelUploadUtil.excelParse(file);
		if (uploadSheet == null) return ApiResult.result(null, ExcelUploadValidateEnums.UploadResponseCode.EXCEL_TRANSFORM_FAIL);
		calPgListRequestDto.setCreateId(Long.parseLong(calPgListRequestDto.getUserVo().getUserId()));
		calPgListRequestDto.setOriginNm(file.getOriginalFilename());
		calPgListRequestDto.setPgUploadGubun(dto.getPgUploadGubun());
		// 외부몰 주문업로드 정보 등록
		calPgService.addOdOrderMaster(calPgListRequestDto);


		// Excel 데이터 Mapping
		List<CalPgUploadDto> excelList = (List<CalPgUploadDto>) orderExcelUploadFactory.setExcelData(pgUploadType, uploadSheet);

		// 항목별 검증 진행
		excelList = (List<CalPgUploadDto>) orderExcelUploadFactory.getDefaultRowItemValidator(pgUploadType, excelList);

		//업로드 현황 설정
		List<CalPgUploadDto> successVoList = new ArrayList<>();
		List<CalPgUploadDto> failVoList = new ArrayList<>();
		for (CalPgUploadDto item : excelList) {
			System.out.println("item : " + item);

			item.setOdPgCompareUploadInfoId(calPgListRequestDto.getOdPgCompareUploadInfoId());
			item.setCreateId(calPgListRequestDto.getCreateId());
			item.setPgUploadGubun(dto.getPgUploadGubun());
			String type ="";
			if(item.getTransAmt() != null && !StringUtil.isEmpty(item.getTransAmt())) {
				if(Integer.parseInt(item.getTransAmt()) > 0) {
					type = ExcelUploadEnums.PayType.G.getCode();			// 결제
				}else {
					type = ExcelUploadEnums.PayType.F.getCode();			// 환불
				}
			}
			item.setType(type);
			item.setPgService(dto.getPgUploadGubun());

			if (item.isSuccess()) {
				item.setSuccessYn("Y");
				successVoList.add(item);
			} else {
				item.setSuccessYn("N");
				failVoList.add(item);
			}
			calPgService.addOdPgCompareUploadDetail(item);
			calPgService.addOdPgCompareUploadDetailInfo(item);
		}

		// Upload 정보 수정
		calPgListRequestDto.setSuccessCnt(successVoList.size());
		calPgListRequestDto.setFailCnt(failVoList.size());
		calPgService.putPgCountInfo(calPgListRequestDto);

		// Return 값 설정
		CalPgUploadResponseDto responseDto = new CalPgUploadResponseDto();
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



	/**
	 * PG 거래내역 대사 파일 업로드 실패내역 엑셀 다운로드
	 * @param calPgListRequestDto
	 * @return ExcelDownloadDto
	 * @throws Exception
	 */
	@Override
	public ExcelDownloadDto getCalPgUploadFailList(CalPgListRequestDto calPgListRequestDto) {

		List<CalPgFailListDto> supportPriceExcelList = calPgService.getCalPgUploadFailList(calPgListRequestDto);

		String excelFileName = "PG 거래내역 대사 파일 업로드 실패내역" + "_" + DateUtil.getCurrentDate();
		String excelSheetName = "sheet";
		/* 화면값보다 20더 하면맞다. */
		Integer[] widthListOfFirstWorksheet = {  200, 200, 200, 200, 200, 200, 200, 200,
												 200, 200, 200, 200, 200, 200, 200, 200,
												 200, 200, 200, 200, 200, 200, 200, 200, 200 };

		String[] alignListOfFirstWorksheet = { 	"center", "center", "center", "center", "center", "center", "center", "center",
												"center", "center", "center", "center", "center", "center", "center", "center",
												"center", "center", "center", "center", "center", "center", "center", "center", "center" };

		String[] propertyListOfFirstWorksheet = {	"odid", "tid", "approvalDt", "transAmt", "deductAmt", "accountAmt", "commission", "vat",
													"giveDt", "escrowCommission", "escrowVat", "mPoint", "mPointCommission", "mPointVat", "marketingCommission", "marketingVat",
													"certificationCommission", "certificationVat", "freeCommission", "cardNm", "cardAuthNum", "cardQuota", "bankNm", "bankAccountNumber", "accountDt"};

		String[] firstHeaderListOfFirstWorksheet = {	"주문번호", "거래ID", "결제일자", "결제금액", "공제금액", "정산금액", "수수료", "부가세",
														"대금지급일", "에스크로 수수료", "에스크로 부가세", "M포인트 금액", "M포인트 수수료", "M포인트 부가세", "마케팅 수수료", "마케팅 부가세",
														"인증 수수료", "인증 부가세", "무이자 수수료", "카드명", "카드 승인번호", "할부개월수", "은행명", "계좌번호", "정산일자"  };


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