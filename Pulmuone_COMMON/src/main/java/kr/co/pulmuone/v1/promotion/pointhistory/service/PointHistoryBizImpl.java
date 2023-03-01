package kr.co.pulmuone.v1.promotion.pointhistory.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.api.constant.LegalTypes;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.promotion.pointhistory.dto.*;
import kr.co.pulmuone.v1.promotion.pointhistory.dto.vo.PointDetailHistoryVo;
import kr.co.pulmuone.v1.promotion.pointhistory.dto.vo.PointHistoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


@Service
public class PointHistoryBizImpl implements PointHistoryBiz{

	@Autowired
	private PointHistoryService pointHistoryService;

	@Override
	public ApiResult<?> getPointNormalHistoryList(PointHistoryListRequestDto pointHistoryListRequestDto) throws Exception {
		return ApiResult.success(pointHistoryService.getPointNormalHistoryList(pointHistoryListRequestDto));
	}

	@Override
	public ApiResult<?> getPointHistoryList(PointHistoryListRequestDto pointHistoryListRequestDto) throws Exception {
		PointHistoryListResponseDto result = new PointHistoryListResponseDto();

		if(pointHistoryListRequestDto.getCondiValue() != null && !pointHistoryListRequestDto.getCondiValue().isEmpty()) {
			ArrayList<String> array = new ArrayList<>();
			StringTokenizer st = new StringTokenizer(pointHistoryListRequestDto.getCondiValue(), "\n|,");
			while(st.hasMoreElements()) {
				String object = (String)st.nextElement();
				array.add(object);
			}
			pointHistoryListRequestDto.setCondiValueArray(array);
		}


		Page<PointHistoryVo> pointHistoryList = pointHistoryService.getPointHistoryList(pointHistoryListRequestDto);


		result.setTotal(pointHistoryList.getTotal());
		result.setRows(pointHistoryList.getResult());

		return ApiResult.success(result);
	}

	@Override
	public ApiResult<?> getPointDetailHistory(PointDetailHistoryRequestDto pointDetailHistoryRequestDto) throws Exception {
		PointDetailHistoryResponseDto result = new PointDetailHistoryResponseDto();

		List<PointDetailHistoryVo> pointHistoryList = pointHistoryService.getPointDetailHistory(pointDetailHistoryRequestDto);

		result.setRows(pointHistoryList);

		return ApiResult.success(result);
	}


	@Override
	public ApiResult<?> getLoginInfo(PointDetailHistoryRequestDto pointDetailHistoryRequestDto) throws Exception {
		PointAdminInfoResponseDto result = new PointAdminInfoResponseDto();

		result = pointHistoryService.getLoginInfo(pointDetailHistoryRequestDto);

		return ApiResult.success(result);
	}



	@Override
	public ApiResult<?> getTotalPointHistory(PointHistoryListRequestDto pointHistoryListRequestDto) throws Exception {
		PointAdminInfoResponseDto result = new PointAdminInfoResponseDto();

		if(pointHistoryListRequestDto.getCondiValue() != null && !pointHistoryListRequestDto.getCondiValue().isEmpty()) {
			ArrayList<String> array = new ArrayList<>();
			StringTokenizer st = new StringTokenizer(pointHistoryListRequestDto.getCondiValue(), "\n|,");
			while(st.hasMoreElements()) {
				String object = (String)st.nextElement();
				array.add(object);
			}
			pointHistoryListRequestDto.setCondiValueArray(array);
		}

		result = pointHistoryService.getTotalPointHistory(pointHistoryListRequestDto);

		return ApiResult.success(result);
	}

	/**
	 * 지급내역 엑셀  다운로드
	 */
	@Override
	public ExcelDownloadDto getPointHistoryListExportExcel(PointHistoryListRequestDto pointHistoryListRequestDto, String legalType) throws Exception {

		String excelFileName; // 엑셀 파일 이름: 확장자는 xlsx 	자동 설정됨
		String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

		// 올가 적립금 내역의 경우 아래 엑셀파일명으로 변경한다.
		if(LegalTypes.ORGA.getCode().equals(legalType)){
			excelFileName = "올가 적립금 내역";
			pointHistoryListRequestDto.setExcelDownType("orderByList");
		} else {
			excelFileName = "적립금" + ("orderByList".equals(pointHistoryListRequestDto.getExcelDownType()) ? "주문건별" : "분담조직별") + "내역"; // 엑셀 파일 이름: 확장자는 xlsx 	자동 설정됨
		}

		/*
		 * 배열 내 순서가 엑셀 본문의 컬럼 순서와 매칭됨
		 *
		 */

		/*
		 * 컬럼별 width 목록 : 단위 pixel
		 * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
		 */
		Integer[] orga_widthListOfFirstWorksheet = { //
				300, 300, 300, 400, 400, 200, 400, 400, 400, 400, 400, 400, 400, 400, 600, 200, 200 };
		Integer[] nonOrga_widthListOfFirstWorksheet = { //
				300, 300, 300, 400, 200, 400, 400, 400, 400, 400, 400, 400, 400, 600, 200, 200 };
		Integer[] widthListOfFirstWorksheet = LegalTypes.ORGA.getCode().equals(legalType) ? orga_widthListOfFirstWorksheet : nonOrga_widthListOfFirstWorksheet;

		/*
		 * 본문 데이터 컬럼별 정렬 목록
		 * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
		 * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
		 */
		String[] orga_alignListOfFirstWorksheet = { //
				"center", "center", "center", "center", "center", "center", "center","right","right", "center", "center", "center", "center", "center", "center", "center", "center" };
		String[] nonOrga_alignListOfFirstWorksheet = { //
				"center", "center", "center", "center", "center", "center","right","right", "center", "center", "center", "center", "center", "center", "center", "center" };
		String[] alignListOfFirstWorksheet = LegalTypes.ORGA.getCode().equals(legalType) ? orga_alignListOfFirstWorksheet : nonOrga_alignListOfFirstWorksheet;

		/*
		 * 본문 데이터 컬럼별 데이터 property 목록
		 * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
		 */
		String[] orga_propertyListOfFirstWorksheet = { //
				"urUserName", "urUserId", "paymentTypeName", "orgaMemberNo", "pointTypeName", "pmPointId", "pointName", "amount", "usedAmount", "odid", "pointDetailTypeName", "createDate", "expirationDt", "organizationNm", "pointUsedMsg", "createId", "createNm" };
		String[] nonOrga_propertyListOfFirstWorksheet = { //
				"urUserName", "urUserId", "paymentTypeName", "pointTypeName", "pmPointId", "pointName", "amount", "usedAmount", "odid", "pointDetailTypeName", "createDate", "expirationDt", "organizationNm", "pointUsedMsg", "createId", "createNm" };
		String[] propertyListOfFirstWorksheet = LegalTypes.ORGA.getCode().equals(legalType) ? orga_propertyListOfFirstWorksheet : nonOrga_propertyListOfFirstWorksheet;

		/*
		 * 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
		 *
		 */
		String[] orga_firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
				"회원명", "회원ID", "구분", "올가회원번호", "적립금 설정", "적립금번호", "적립금명", "내역", "적립/차감액", "주문번호", "상세구분", "지급/차감 일자", "유효기간", "분담조직", "사유", "현업 ID", "현업 이름" };
		String[] nonOrga_firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
				"회원명", "회원ID", "구분", "적립금 설정", "적립금번호", "적립금명", "내역", "적립/차감액", "주문번호", "상세구분", "지급/차감 일자", "유효기간", "분담조직", "사유", "현업 ID", "현업 이름" };
		String[] firstHeaderListOfFirstWorksheet = LegalTypes.ORGA.getCode().equals(legalType) ? orga_firstHeaderListOfFirstWorksheet : nonOrga_firstHeaderListOfFirstWorksheet;

		/*
		 * 워크시트 DTO 생성 후 정보 세팅
		 */
		ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder() //
				.workSheetName(excelSheetName) // 엑셀 파일내 워크시트 명
				.propertyList(propertyListOfFirstWorksheet) // 컬럼별 데이터 property 목록
				.widthList(widthListOfFirstWorksheet) // 컬럼별 너비 목록
				.alignList(alignListOfFirstWorksheet) // 컬럼별 정렬 목록
				.build();

		firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet);

		if(pointHistoryListRequestDto.getCondiValue() != null && !pointHistoryListRequestDto.getCondiValue().isEmpty()) {
			ArrayList<String> array = new ArrayList<>();
			StringTokenizer st = new StringTokenizer(pointHistoryListRequestDto.getCondiValue(), "\n|,");
			while(st.hasMoreElements()) {
				String object = (String)st.nextElement();
				array.add(object);
			}
			pointHistoryListRequestDto.setCondiValueArray(array);
		}

		List<PointHistoryVo> notIssueList = null;
		try
		{
			notIssueList = pointHistoryService.getPointHistoryListExportExcel(pointHistoryListRequestDto);
		}
		catch (Exception e)
		{
			throw e; // 추후 CustomException 으로 변환 예정
		}
		firstWorkSheetDto.setExcelDataList(notIssueList);

		ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder().excelFileName(excelFileName).build();

		excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

		return excelDownloadDto;
	}

}
