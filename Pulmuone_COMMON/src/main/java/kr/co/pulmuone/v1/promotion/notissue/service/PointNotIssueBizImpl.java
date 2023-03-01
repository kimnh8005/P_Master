package kr.co.pulmuone.v1.promotion.notissue.service;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.promotion.notissue.dto.PointNotIssueListRequestDto;
import kr.co.pulmuone.v1.promotion.notissue.dto.PointNotIssueListResponseDto;
import kr.co.pulmuone.v1.promotion.notissue.dto.vo.PointNotIssueListVo;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("rawtypes")
@Slf4j
@Service
public class PointNotIssueBizImpl implements PointNotIssueBiz {

	  @Autowired
	  PointNotIssueService pointNotIssueService;


	  @Override
	  public ApiResult<?> getPointNotIssueList(PointNotIssueListRequestDto pointNotIssueListRequestDto) throws Exception {
		PointNotIssueListResponseDto result = new PointNotIssueListResponseDto();

		if (!pointNotIssueListRequestDto.getFindKeyword().isEmpty()) {
			ArrayList<String> array = new ArrayList<>();
			StringTokenizer st = new StringTokenizer(pointNotIssueListRequestDto.getFindKeyword(), "\n|,");
			while (st.hasMoreElements()) {
				String object = (String) st.nextElement();
				array.add(object);
			}
			pointNotIssueListRequestDto.setFindKeywordArray(array);
		}

		pointNotIssueListRequestDto.setMaxPoint(Constants.DEPOSIT_MAXIMUM_POSSIBLE_POINT);


	  	Page<PointNotIssueListVo> voList = pointNotIssueService.getPointNotIssueList(pointNotIssueListRequestDto);

	    result.setRows(voList.getResult());
	    result.setTotal(voList.getTotal());

	    return  ApiResult.success(result);
	  }


	 /**
	 * 쿠폰 지급내역 엑셀 선택 다운로드
	 */
	@Override
	public ExcelDownloadDto getPointNotIssueListExportExcel(PointNotIssueListRequestDto pointNotIssueListRequestDto) throws Exception {
		String excelFileName = "적립금 미지급 내역"; // 엑셀 파일 이름: 확장자는 xlsx 	자동 설정됨
		String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

		/*
		 * 배열 내 순서가 엑셀 본문의 컬럼 순서와 매칭됨
		 *
		 */

		/*
		 * 컬럼별 width 목록 : 단위 pixel
		 * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
		 */
		Integer[] widthListOfFirstWorksheet = { //
				300, 300, 400, 300, 300, 300, 350, 350, 350 };

		/*
		 * 본문 데이터 컬럼별 정렬 목록
		 * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
		 * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
		 */
		String[] alignListOfFirstWorksheet = { //
				"center", "center", "center", "center","center", "center", "center", "center", "center","left" };

		/*
		 * 본문 데이터 컬럼별 데이터 property 목록
		 * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
		 */
		String[] propertyListOfFirstWorksheet = { //
				"userNm", "loginId", "pointProcessTpName", "issueVal", "partPointVal", "redepositPointVal", "organizationNm", "createDt", "depositDt" };

		/*
		 * 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
		 *
		 */
		String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
				"회원명", "회원ID", "지급구분", "미지급 금액", "부분지급 금액", "잔여지급 금액", "분담조직", "등록일자", "유효기간"
		};

		/*
		 * 워크시트 DTO 생성 후 정보 세팅
		 *
		 */
		ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder() //
				.workSheetName(excelSheetName) // 엑셀 파일내 워크시트 명
				.propertyList(propertyListOfFirstWorksheet) // 컬럼별 데이터 property 목록
				.widthList(widthListOfFirstWorksheet) // 컬럼별 너비 목록
				.alignList(alignListOfFirstWorksheet) // 컬럼별 정렬 목록
				.build();

		firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet);

		if (!pointNotIssueListRequestDto.getFindKeyword().isEmpty()) {
			ArrayList<String> array = new ArrayList<>();
			StringTokenizer st = new StringTokenizer(pointNotIssueListRequestDto.getFindKeyword(), "\n|,");
			while (st.hasMoreElements()) {
				String object = (String) st.nextElement();
				array.add(object);
			}
			pointNotIssueListRequestDto.setFindKeywordArray(array);
		}

		List<PointNotIssueListVo> notIssueList = null;
		try
		{
			notIssueList = pointNotIssueService.getPointNotIssueListExportExcel(pointNotIssueListRequestDto);
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


