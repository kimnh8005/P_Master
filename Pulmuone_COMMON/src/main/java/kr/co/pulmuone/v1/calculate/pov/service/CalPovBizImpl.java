package kr.co.pulmuone.v1.calculate.pov.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import kr.co.pulmuone.v1.calculate.pov.dto.CalPovCostSummaryDto;
import kr.co.pulmuone.v1.calculate.pov.dto.CalPovListResponseDto;
import kr.co.pulmuone.v1.calculate.pov.dto.CalPovProcessDto;
import kr.co.pulmuone.v1.calculate.pov.dto.CalPovUploadDto;
import kr.co.pulmuone.v1.calculate.pov.dto.vo.CalPovAllocationVo;
import kr.co.pulmuone.v1.calculate.pov.dto.vo.CalPovProcessVo;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.ExcelUploadEnums;
import kr.co.pulmuone.v1.comm.enums.ExcelUploadValidateEnums;
import kr.co.pulmuone.v1.comm.enums.PovEnums.ExcelUploadResult;
import kr.co.pulmuone.v1.comm.enums.PovEnums.PovAllocationType;
import kr.co.pulmuone.v1.comm.excel.factory.OrderExcelUploadFactory;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.comm.util.ExcelUploadUtil;

/**
 * <PRE>
 * Forbiz Korea
 * 정산관리 > POV I/F > POV I/F BizImpl
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
public class CalPovBizImpl implements CalPovBiz {

	@Autowired
	CalPovService calPovService;

	@Autowired
	private OrderExcelUploadFactory orderExcelUploadFactory;

	/**
	 * POV I/F 리스트 조회
	 *
	 * @param calPovListRequestDto
	 * @return
	 */
	@Override
	public ApiResult<?> getPovList(String year, String month) throws Exception {
		CalPovListResponseDto resDto = new CalPovListResponseDto();
		resDto.setList(calPovService.mapSummaryDTO(calPovService.getPovList(year, month)));
		resDto.setProcess(new CalPovProcessDto(calPovService.getPovProcessList(year, month)));
		return ApiResult.success(resDto);
	}

	@Override
	public ExcelDownloadDto getPovListExportExcel(String year, String month) throws Exception {

		String excelFileName = "온라인사업부_" + year + "년_" + month + "월_정산지표"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
		String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

		/*
		 * 컬럼별 width 목록 : 단위 pixel ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는
		 * 120 pixel 로 고정됨
		 */
		Integer[] widthListOfFirstWorksheet = { //
				100, 150, 150, 150, 150, 150, 150, 150, 150, 150, 150, 150, 150 };

		/*
		 * 본문 데이터 컬럼별 정렬 목록 ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left"
		 * (좌측 정렬) 로 고정 "left", "center", "right", "justify", "distributed" 가 아닌 다른 값
		 * 지정시 "left" (좌측 정렬) 로 지정됨
		 */
		String[] alignListOfFirstWorksheet = { //
				"center", "right", "right", "right", "right", "right", "right", "right", "right", "right", "right", "right", "right" };

		/*
		 * 본문 데이터 컬럼별 데이터 property 목록 ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야
		 * 함
		 */

		String[] propertyListOfFirstWorksheet = { //
				"corporationName", "tempMeCost", "tempOvCost", "tempVdcCost", "tempMogeCost", "finalMeCost", "finalOvCost",
				"finalVdcCost", "finalMogeCost", "diffMeCost", "diffOvCost", "diffVdcCost", "diffMogeCost" };

		// 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
		String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
				"법인", "가마감", "가마감", "가마감", "가마감", "마감", "마감", "마감", "마감", "확정차액 (가마감 - 마감)", "확정차액 (가마감 - 마감)", "확정차액 (가마감 - 마감)", "확정차액 (가마감 - 마감)" };

		String[] secondHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
				"법인", "ME", "O.V", "V.DC", "MOGE", "ME", "O.V", "V.DC", "MOGE", "ME", "O.V", "V.DC", "MOGE" };

		// 워크시트 DTO 생성 후 정보 세팅
		ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder() //
				.workSheetName(excelSheetName) // 엑셀 파일내 워크시트 명
				.propertyList(propertyListOfFirstWorksheet) // 컬럼별 데이터 property 목록
				.widthList(widthListOfFirstWorksheet) // 컬럼별 너비 목록
				.alignList(alignListOfFirstWorksheet) // 컬럼별 정렬 목록
				.build();

		// 엑셀 다단 헤더 구성 : ( 헤더 행 index , 헤더 제목 배열 ) 형식으로 세팅
		firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet); // 첫 번째 헤더 컬럼
		firstWorkSheetDto.setHeaderList(1, secondHeaderListOfFirstWorksheet); // 첫 번째 헤더 컬럼

		/*
		 * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능, 페이지네이션 처리하지 않음 excelData 를 세팅하지 않으면 샘플
		 * 엑셀로 다운로드됨
		 */
		List<CalPovCostSummaryDto> itemList = calPovService.mapSummaryDTO(calPovService.getPovList(year, month));

		firstWorkSheetDto.setExcelDataList(itemList);

		// excelDownloadDto 생성 후 workSheetDto 추가
		ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
				.excelFileName(excelFileName) //
				.build();

		excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

		return excelDownloadDto;
	}

	/**
	 * POV I/F 엑셀 업로드
	 *
	 * @param file
	 * @param uploadType
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BaseException.class, Exception.class })
	public ApiResult<?> addPovExcelUpload(MultipartFile file, CalPovProcessVo calPovProcessVo) throws Exception {
		if (ExcelUploadUtil.isFile(file) != true)
			return ApiResult.result(null, ExcelUploadValidateEnums.UploadResponseCode.EXCEL_UPLOAD_NONE);

		if (!calPovService.validateUploadExcel(calPovProcessVo)) {
			return ApiResult.result(ExcelUploadResult.NOT_STATUS_EXCEL_UPLOAD);
		}
		calPovService.deletePovProcessAndAllocation(calPovProcessVo);
		calPovService.savePovProcess(calPovProcessVo);

		// Excel Import 정보 -> Dto 변환
		List<CalPovAllocationVo> voList = new ArrayList<CalPovAllocationVo>();

		voList.addAll(getExcelData(file, PovAllocationType.ME, calPovProcessVo));
		voList.addAll(getExcelData(file, PovAllocationType.OV, calPovProcessVo));
		voList.addAll(getExcelData(file, PovAllocationType.VDC, calPovProcessVo));
		voList.addAll(getExcelData(file, PovAllocationType.MOGE, calPovProcessVo));

		if (CollectionUtils.isEmpty(voList)) {
			return ApiResult.result(ExcelUploadResult.NOT_UPLOAD_DATA);
		} else {
			for (CalPovAllocationVo vo : voList) {
				calPovService.insertPovAllocation(vo);
			}

			return ApiResult.success();
		}
	}

	private List<CalPovAllocationVo> getExcelData(MultipartFile file, PovAllocationType povAllocationType, CalPovProcessVo calPovProcessVo) throws Exception {
		Sheet uploadSheet = ExcelUploadUtil.excelParse(file, povAllocationType.getSheetIndex());
		if (uploadSheet != null) {
			List<CalPovAllocationVo> list = new ArrayList<CalPovAllocationVo>();

			String povUploadType = povAllocationType.getExcelUploadType();

			// Excel 데이터 Mapping
			List<CalPovUploadDto> excelList = (List<CalPovUploadDto>) orderExcelUploadFactory.setExcelData(povUploadType, uploadSheet);

			// 항목별 검증 진행
			excelList = (List<CalPovUploadDto>) orderExcelUploadFactory.getDefaultRowItemValidator(povUploadType, excelList);

			// 업로드 현황 설정
			for (CalPovUploadDto item : excelList) {
				list.add(new CalPovAllocationVo(calPovProcessVo.getScenario(), povAllocationType.getCode(),
						calPovProcessVo.getYear(), calPovProcessVo.getMonth(), item.getCorporationCode(),
						item.getChannelCode(), item.getSkuCode(), item.getAccountCode(), item.getCost(),
						calPovProcessVo.getCreator(), item.getFactoryCode()));
			}

			return list;
		} else {
			return null;
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BaseException.class, Exception.class })
	public ApiResult<?> odPovInterface(String scenario, String year, String month, String user) throws Exception {
		if (!calPovService.validateInterface(scenario, year, month, user)) {
			return ApiResult.result(ExcelUploadResult.NOT_STATUS_INTERFACE);
		}
		if (calPovService.findRemotePovProcessByScenarioAndYearAndMonth(scenario, year, month) == null) {
			return ApiResult.result(ExcelUploadResult.NO_REMOTE_POV_DATA);
		}

		CalPovProcessVo process = calPovService.getPovProcessList(scenario, year, month);
		process.doInterface(user);

		calPovService.updatePovProcessWhenInterfaced(process);
		calPovService.doRemoteInterface(process);

		return ApiResult.success();
	}
}